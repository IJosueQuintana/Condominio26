package fis.dsw.sgc.check_in.dao;

import fis.dsw.sgc.conexion_bd.DBConnection;
import fis.dsw.sgc.check_in.model.IngresoParqueadero;
import fis.dsw.sgc.check_in.model.RegistroEntrada;
import fis.dsw.sgc.check_in.model.RegistroEntradaFactory;
import fis.dsw.sgc.check_in.model.RegistroEntradaResidente;
import fis.dsw.sgc.check_in.model.RegistroEntradaVisitante;
import fis.dsw.sgc.check_in.model.RegistroEntradaExterna;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RegistroEntradaDAO implements IRegistroEntradaDAO {

    // Constructor explícito requerido para instanciación manual desde Main 
    public RegistroEntradaDAO() {}

    @Override
    public int guardar(RegistroEntrada registro) {
        String sql = "INSERT INTO registro_entrada (id_visita, nombres, apellidos, cedula, fecha_llegada, hora_llegada, informacion_adicional, observaciones, tipo_entrada, placa_vehiculo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = DBConnection.getInstance().getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (registro.getIdVisita() != null && registro.getIdVisita() > 0) {
                pstmt.setInt(1, registro.getIdVisita());
            } else {
                pstmt.setNull(1, java.sql.Types.INTEGER);
            }
            pstmt.setString(2, registro.getNombres() != null ? registro.getNombres() : "");
            pstmt.setString(3, registro.getApellidos() != null ? registro.getApellidos() : "");
            pstmt.setString(4, registro.getCedula() != null ? registro.getCedula() : "");
            pstmt.setString(5, registro.getFechaLlegada() != null ? registro.getFechaLlegada() : "");
            pstmt.setString(6, registro.getHoraLlegada() != null ? registro.getHoraLlegada() : "");
            pstmt.setString(7, registro.getInformacionAdicional());
            pstmt.setString(8, registro.getObservaciones());
            pstmt.setString(9, registro.getTipoEntrada() != null ? registro.getTipoEntrada() : "VISITANTE");
            pstmt.setString(10, registro.getPlacaVehiculo());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idGenerado = generatedKeys.getInt(1);
                        registro.setIdEntrada(idGenerado);
                        return idGenerado;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar registro de entrada: " + e.getMessage());
        }
        return -1;
    }

    @Override
    public List<RegistroEntrada> listarTodos() {
        return buscarPorFiltro(null, null, null);
    }

    @Override
    public List<RegistroEntrada> buscarPorFiltro(String fecha, String tipo, String cedula) {
        return buscarPorFiltroAvanzado(fecha, null, tipo, cedula, null);
    }

    @Override
    public List<RegistroEntrada> buscarPorFiltroAvanzado(String fechaInicio, String fechaFin, String tipo, String busquedaTexto, String placa) {
        List<RegistroEntrada> resultados = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM registro_entrada WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (fechaInicio != null && !fechaInicio.isBlank()) {
            sql.append(" AND fecha_llegada >= ?");
            params.add(fechaInicio.trim());
        }
        if (fechaFin != null && !fechaFin.isBlank()) {
            sql.append(" AND fecha_llegada <= ?");
            params.add(fechaFin.trim());
        }
        if (tipo != null && !tipo.isBlank() && !"TODOS".equalsIgnoreCase(tipo.trim())) {
            sql.append(" AND UPPER(tipo_entrada) = ?");
            params.add(tipo.trim().toUpperCase());
        }
        if (busquedaTexto != null && !busquedaTexto.isBlank()) {
            sql.append(" AND (cedula LIKE ? OR UPPER(nombres) LIKE ? OR UPPER(apellidos) LIKE ? OR UPPER(informacion_adicional) LIKE ? OR UPPER(observaciones) LIKE ?)");
            String pattern = "%" + busquedaTexto.trim().toUpperCase() + "%";
            params.add(pattern);
            params.add(pattern);
            params.add(pattern);
            params.add(pattern);
            params.add(pattern);
        }
        if (placa != null && !placa.isBlank()) {
            sql.append(" AND UPPER(placa_vehiculo) LIKE ?");
            params.add("%" + placa.trim().toUpperCase() + "%");
        }

        sql.append(" ORDER BY id_entrada DESC");

        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    resultados.add(mapearEntrada(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar registros de entrada: " + e.getMessage());
        }
        return resultados;
    }

    @Override
    public boolean registrarIngresoParqueadero(IngresoParqueadero ingreso) {
        String sql = "INSERT INTO ingreso_parqueadero (id_registro_entrada, id_parqueadero, estado) VALUES (?, ?, ?)";
        Connection conn = DBConnection.getInstance().getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ingreso.getIdRegistroEntrada());
            pstmt.setInt(2, ingreso.getIdParqueadero());
            pstmt.setString(3, ingreso.getEstado() != null ? ingreso.getEstado() : "OCUPADO");
            
            // También actualizamos el estado del parqueadero a OCUPADO
            String updateParq = "UPDATE parqueadero SET estado = 'OCUPADO' WHERE id_parqueadero = ?";
            try (PreparedStatement uPstmt = conn.prepareStatement(updateParq)) {
                uPstmt.setInt(1, ingreso.getIdParqueadero());
                uPstmt.executeUpdate();
            }

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar ingreso parqueadero: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<String> obtenerParqueaderosDisponibles(String tipo) {
        List<String> parqueaderos = new ArrayList<>();
        String sql = "SELECT numero FROM parqueadero WHERE estado = 'DISPONIBLE'";
        if (tipo != null && !tipo.isBlank()) {
            sql += " AND tipo = ?";
        }
        sql += " ORDER BY numero";

        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (tipo != null && !tipo.isBlank()) {
                pstmt.setString(1, tipo.toUpperCase());
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    parqueaderos.add(rs.getString("numero"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar parqueaderos disponibles: " + e.getMessage());
        }
        return parqueaderos;
    }

    @Override
    public Integer obtenerIdParqueaderoPorNumero(String numero) {
        String sql = "SELECT id_parqueadero FROM parqueadero WHERE numero = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, numero);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_parqueadero");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar id parqueadero: " + e.getMessage());
        }
        return null;
    }

    @Override
    public String obtenerDepartamentoPorCedulaResidente(String cedula) {
        String sql = "SELECT i.codigo, i.numero FROM usuario u " +
                     "JOIN usuario_inmueble ui ON u.id_usuario = ui.id_usuario " +
                     "JOIN inmueble i ON ui.id_inmueble = i.id_inmueble " +
                     "WHERE u.numero_documento = ? AND ui.estado = 'ACTIVO' LIMIT 1";

        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cedula);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String codigo = rs.getString("codigo");
                    String numero = rs.getString("numero");
                    return (numero != null && !numero.isBlank()) ? "Depto. " + numero : codigo;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener departamento del residente: " + e.getMessage());
        }
        return null;
    }

    @Override
    public String obtenerDepartamentoYResidentePorId(int idResidente) {
        String sql = "SELECT u.nombres, u.apellidos, i.numero, i.codigo FROM usuario u " +
                     "LEFT JOIN usuario_inmueble ui ON u.id_usuario = ui.id_usuario " +
                     "LEFT JOIN inmueble i ON ui.id_inmueble = i.id_inmueble " +
                     "WHERE u.id_usuario = ? LIMIT 1";
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idResidente);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String nombreComp = rs.getString("nombres") + " " + rs.getString("apellidos");
                    String depto = rs.getString("numero");
                    if (depto == null) depto = rs.getString("codigo");
                    return (depto != null ? "Depto. " + depto + " - " : "") + nombreComp.trim();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener datos de residente por id: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Integer obtenerIdParqueaderoDeResidente(int idResidente) {
        String sql = "SELECT p.id_parqueadero FROM usuario_inmueble ui " +
                     "JOIN parqueadero p ON ui.id_inmueble = p.id_inmueble " +
                     "WHERE ui.id_usuario = ? AND p.estado = 'DISPONIBLE' LIMIT 1";
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idResidente);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_parqueadero");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar parqueadero de residente: " + e.getMessage());
        }
        return null;
    }

    private RegistroEntrada mapearEntrada(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_entrada");
        int idVisita = rs.getInt("id_visita");
        String nombres = rs.getString("nombres");
        String apellidos = rs.getString("apellidos");
        String cedula = rs.getString("cedula");
        String fecha = rs.getString("fecha_llegada");
        String hora = rs.getString("hora_llegada");
        String info = rs.getString("informacion_adicional");
        String obs = rs.getString("observaciones");
        String tipo = rs.getString("tipo_entrada");
        String placa = rs.getString("placa_vehiculo");

        RegistroEntrada entrada;
        if ("RESIDENTE".equalsIgnoreCase(tipo)) {
            RegistroEntradaResidente r = RegistroEntradaFactory.crearEntradaResidente(nombres, apellidos, cedula, fecha, hora, info);
            entrada = r;
        } else if ("EXTERNA".equalsIgnoreCase(tipo)) {
            RegistroEntradaExterna e = RegistroEntradaFactory.crearEntradaExterna(nombres, apellidos, cedula, fecha, hora, info, obs, placa);
            entrada = e;
        } else {
            RegistroEntradaVisitante v = RegistroEntradaFactory.crearEntradaVisitante(nombres, apellidos, cedula, fecha, hora, idVisita > 0 ? idVisita : null, info, true);
            entrada = v;
        }

        entrada.setIdEntrada(id);
        entrada.setInformacionAdicional(info);
        entrada.setObservaciones(obs);
        entrada.setPlacaVehiculo(placa);
        return entrada;
    }

    @Override
    public String[] buscarDatosResidentePorCedula(String cedula) {
        if (cedula == null || cedula.isBlank()) return null;

        String sql = "SELECT u.nombres, u.apellidos, i.numero, i.codigo FROM usuario u " +
                     "LEFT JOIN usuario_inmueble ui ON u.id_usuario = ui.id_usuario " +
                     "LEFT JOIN inmueble i ON ui.id_inmueble = i.id_inmueble " +
                     "WHERE u.numero_documento = ? AND u.estado = 'ACTIVO' LIMIT 1";

        try (PreparedStatement pstmt = DBConnection.getInstance().getConnection().prepareStatement(sql)) {
            pstmt.setString(1, cedula.trim());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String nombres = rs.getString("nombres");
                    String apellidos = rs.getString("apellidos");
                    String numero = rs.getString("numero");
                    if (numero == null || numero.isBlank()) numero = rs.getString("codigo");
                    String depto = (numero != null) ? "Depto. " + numero : "Sin unidad";
                    return new String[]{nombres, apellidos, depto};
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar datos de residente: " + e.getMessage());
        }
        return null;
    }
}
