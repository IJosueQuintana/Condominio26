package fis.dsw.sgc.inmuebles.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fis.dsw.sgc.conexion_bd.DBConnection;
import fis.dsw.sgc.inmuebles.dto.InmuebleResumenDTO;
import fis.dsw.sgc.inmuebles.dto.OpcionComboDTO;
import fis.dsw.sgc.inmuebles.model.Inmueble;

public class InmuebleDAOMySQL implements IInmuebleDAO {

    @Override
    public Inmueble buscarPorCodigo(String codigo) {
        String sql = "SELECT * FROM inmueble WHERE codigo = ?";
        Connection conn = DBConnection.getInstance().getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, codigo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearInmueble(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar inmueble por código: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean existeCodigoParaOtroInmueble(String codigo, int idInmueble) {
        String sql = "SELECT 1 FROM inmueble WHERE codigo = ? AND id_inmueble != ?";
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, codigo);
            pstmt.setInt(2, idInmueble);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar código de inmueble: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<OpcionComboDTO> listarEdificios() {
        List<OpcionComboDTO> resultado = new ArrayList<>();
        String sql = "SELECT id_edificio, nombre FROM edificio WHERE estado = 'ACTIVO' ORDER BY nombre";

        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                resultado.add(new OpcionComboDTO(rs.getInt("id_edificio"), rs.getString("nombre")));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar edificios: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public List<OpcionComboDTO> listarTiposInmueble() {
        List<OpcionComboDTO> resultado = new ArrayList<>();
        String sql = "SELECT id_tipo_inmueble, nombre FROM tipo_inmueble WHERE estado = 'ACTIVO' ORDER BY nombre";

        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                resultado.add(new OpcionComboDTO(rs.getInt("id_tipo_inmueble"), rs.getString("nombre")));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar tipos de inmueble: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public boolean existeCodigo(String codigo) {
        String sql = "SELECT 1 FROM inmueble WHERE codigo = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, codigo);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar código de inmueble: " + e.getMessage());
            return false;
        }
    }

    @Override
    public int guardar(Inmueble inmueble) {
        String sql = "INSERT INTO inmueble (id_edificio, id_tipo_inmueble, codigo, piso, numero, " +
                "area_m2, numero_habitaciones, numero_banos, descripcion, disponible_alquiler, " +
                "disponible_venta, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setObject(1, inmueble.getIdEdificio());
            pstmt.setInt(2, inmueble.getIdTipoInmueble());
            pstmt.setString(3, inmueble.getCodigo());
            pstmt.setObject(4, inmueble.getPiso());
            pstmt.setString(5, inmueble.getNumero());
            pstmt.setObject(6, inmueble.getAreaM2());
            pstmt.setObject(7, inmueble.getNumeroHabitaciones());
            pstmt.setObject(8, inmueble.getNumeroBanos());
            pstmt.setString(9, inmueble.getDescripcion());
            pstmt.setInt(10, inmueble.isDisponibleAlquiler() ? 1 : 0);
            pstmt.setInt(11, inmueble.isDisponibleVenta() ? 1 : 0);
            pstmt.setString(12, inmueble.getEstado());
            pstmt.executeUpdate();

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar inmueble: " + e.getMessage());
        }
        return -1;
    }

    @Override
    public void actualizar(Inmueble inmueble) {
        String sql = "UPDATE inmueble SET id_edificio = ?, id_tipo_inmueble = ?, codigo = ?, piso = ?, " +
                "numero = ?, area_m2 = ?, numero_habitaciones = ?, numero_banos = ?, descripcion = ?, " +
                "disponible_alquiler = ?, disponible_venta = ?, estado = ? WHERE id_inmueble = ?";

        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, inmueble.getIdEdificio());
            pstmt.setInt(2, inmueble.getIdTipoInmueble());
            pstmt.setString(3, inmueble.getCodigo());
            pstmt.setObject(4, inmueble.getPiso());
            pstmt.setString(5, inmueble.getNumero());
            pstmt.setObject(6, inmueble.getAreaM2());
            pstmt.setObject(7, inmueble.getNumeroHabitaciones());
            pstmt.setObject(8, inmueble.getNumeroBanos());
            pstmt.setString(9, inmueble.getDescripcion());
            pstmt.setInt(10, inmueble.isDisponibleAlquiler() ? 1 : 0);
            pstmt.setInt(11, inmueble.isDisponibleVenta() ? 1 : 0);
            pstmt.setString(12, inmueble.getEstado());
            pstmt.setInt(13, inmueble.getIdInmueble());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar inmueble: " + e.getMessage());
        }
    }

    @Override
    public Inmueble buscarPorId(int idInmueble) {
        String sql = "SELECT * FROM inmueble WHERE id_inmueble = ?";
        Connection conn = DBConnection.getInstance().getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idInmueble);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearInmueble(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar inmueble por id: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<InmuebleResumenDTO> buscarResumen(String filtro) {
        List<InmuebleResumenDTO> resultado = new ArrayList<>();

        String sql = "SELECT i.id_inmueble, i.codigo, i.piso, i.numero, i.estado, " +
                "       ti.nombre AS tipo, " +
                "       ed.nombre AS nombre_edificio, " +
                "       u.nombres AS propietario_nombres, u.apellidos AS propietario_apellidos " +
                "FROM inmueble i " +
                "JOIN tipo_inmueble ti ON ti.id_tipo_inmueble = i.id_tipo_inmueble " +
                "LEFT JOIN edificio ed ON ed.id_edificio = i.id_edificio " +
                "LEFT JOIN usuario_inmueble ui ON ui.id_inmueble = i.id_inmueble " +
                "       AND ui.tipo_relacion = 'PROPIETARIO' AND ui.estado = 'ACTIVO' " +
                "LEFT JOIN usuario u ON u.id_usuario = ui.id_usuario " +
                "WHERE (? IS NULL OR ? = '' " +
                "       OR i.codigo LIKE '%' || ? || '%' " +
                "       OR ed.nombre LIKE '%' || ? || '%' " +
                "       OR (u.nombres || ' ' || u.apellidos) LIKE '%' || ? || '%') " +
                "GROUP BY i.id_inmueble " +
                "ORDER BY i.codigo";

        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 1; i <= 5; i++) {
                pstmt.setString(i, filtro);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String direccion = rs.getString("nombre_edificio") != null
                            ? rs.getString("nombre_edificio") + (rs.getObject("piso") != null
                                ? ", Piso " + rs.getInt("piso") : "") + " - " + rs.getString("numero")
                            : rs.getString("numero");

                    String nombresPropietario = rs.getString("propietario_nombres");
                    String propietario = nombresPropietario != null
                            ? nombresPropietario + " " + rs.getString("propietario_apellidos")
                            : "Sin propietario asignado";

                    resultado.add(new InmuebleResumenDTO(
                            rs.getInt("id_inmueble"),
                            rs.getString("codigo"),
                            direccion,
                            rs.getString("tipo"),
                            propietario,
                            rs.getString("estado")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar inmuebles: " + e.getMessage());
        }

        return resultado;
    }

    @Override
    public List<OpcionComboDTO> listarPropietariosDisponibles() {
        List<OpcionComboDTO> resultado = new ArrayList<>();
        String sql = "SELECT id_usuario, nombres, apellidos, numero_documento FROM usuario " +
                "WHERE estado = 'ACTIVO' ORDER BY nombres, apellidos";

        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String nombreCompleto = rs.getString("nombres") + " " + rs.getString("apellidos")
                        + " - " + rs.getString("numero_documento");
                resultado.add(new OpcionComboDTO(rs.getInt("id_usuario"), nombreCompleto));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar propietarios disponibles: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public Integer obtenerIdPropietario(int idInmueble) {
        String sql = "SELECT id_usuario FROM usuario_inmueble WHERE id_inmueble = ? " +
                "AND tipo_relacion = 'PROPIETARIO' AND estado = 'ACTIVO' AND es_principal = 1 " +
                "ORDER BY fecha_inicio DESC LIMIT 1";

        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idInmueble);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_usuario");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener propietario del inmueble: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void asignarPropietario(int idInmueble, int idUsuario) {
        Connection conn = DBConnection.getInstance().getConnection();

        String sqlCerrar = "UPDATE usuario_inmueble SET estado = 'INACTIVO', fecha_fin = CURRENT_DATE " +
                "WHERE id_inmueble = ? AND tipo_relacion = 'PROPIETARIO' AND es_principal = 1 AND estado = 'ACTIVO'";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlCerrar)) {
            pstmt.setInt(1, idInmueble);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al cerrar propietario anterior: " + e.getMessage());
            return;
        }

        String sqlInsertar = "INSERT INTO usuario_inmueble (id_usuario, id_inmueble, tipo_relacion, " +
                "es_principal, estado) VALUES (?, ?, 'PROPIETARIO', 1, 'ACTIVO')";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlInsertar)) {
            pstmt.setInt(1, idUsuario);
            pstmt.setInt(2, idInmueble);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al asignar propietario: " + e.getMessage());
        }
    }

    @Override
    public void quitarPropietario(int idInmueble) {
        String sql = "UPDATE usuario_inmueble SET estado = 'INACTIVO', fecha_fin = CURRENT_DATE " +
                "WHERE id_inmueble = ? AND tipo_relacion = 'PROPIETARIO' AND es_principal = 1 AND estado = 'ACTIVO'";

        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idInmueble);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al quitar propietario: " + e.getMessage());
        }
    }

    @Override
    public double obtenerAreaTotalCondominio() {
        String sql = "SELECT SUM(area_m2) AS area_total FROM inmueble";
        Connection conn = DBConnection.getInstance().getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("area_total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el área total del condominio: " + e.getMessage());
        }
        return 0.0;
    }

    private Inmueble mapearInmueble(ResultSet rs) throws SQLException {
        Inmueble inmueble = new Inmueble();
        inmueble.setIdInmueble(rs.getInt("id_inmueble"));
        inmueble.setIdEdificio((Integer) rs.getObject("id_edificio"));
        inmueble.setIdTipoInmueble(rs.getInt("id_tipo_inmueble"));
        inmueble.setCodigo(rs.getString("codigo"));
        inmueble.setPiso((Integer) rs.getObject("piso"));
        inmueble.setNumero(rs.getString("numero"));
        inmueble.setAreaM2((Double) rs.getObject("area_m2"));
        inmueble.setNumeroHabitaciones((Integer) rs.getObject("numero_habitaciones"));
        inmueble.setNumeroBanos((Integer) rs.getObject("numero_banos"));
        inmueble.setDescripcion(rs.getString("descripcion"));
        inmueble.setDisponibleAlquiler(rs.getInt("disponible_alquiler") == 1);
        inmueble.setDisponibleVenta(rs.getInt("disponible_venta") == 1);
        inmueble.setEstado(rs.getString("estado"));
        return inmueble;
    }
}