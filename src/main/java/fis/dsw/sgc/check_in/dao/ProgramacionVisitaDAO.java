package fis.dsw.sgc.check_in.dao;

import fis.dsw.sgc.check_in.model.EstadoVisita;
import fis.dsw.sgc.check_in.model.Usuario_Checkin;
import fis.dsw.sgc.check_in.model.VisitaProgramada;
import fis.dsw.sgc.conexion_bd.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProgramacionVisitaDAO implements IProgramacionVisitaDAO {

    public ProgramacionVisitaDAO() {}

    private Connection getConn() {
        return DBConnection.getInstance().getConnection();
    }

    @Override
    public boolean programarVisita(VisitaProgramada visita) {
        String sql = "INSERT INTO visitas_programadas (" +
                     "id_residente, nombres_visita, apellidos_visita, cedula_visita, " +
                     "tipo_visita, fecha_programada, hora_programada, placa_vehiculo, estado, motivo_visita) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = getConn();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
//            if (visita.getIdResidente() != null && visita.getIdResidente() > 0) {
//                ps.setInt(1, visita.getIdResidente());
//            } else {
//                ps.setInt(1, 1);
//            }
            ps.setObject(1, visita.getIdResidente(), java.sql.Types.INTEGER);
            ps.setString(2, visita.getNombresVisita() != null ? visita.getNombresVisita() : "");
            ps.setString(3, visita.getApellidosVisita() != null ? visita.getApellidosVisita() : "");
            ps.setString(4, visita.getCedulaVisita() != null ? visita.getCedulaVisita() : "");
            ps.setString(5, visita.getTipoVisita() != null ? visita.getTipoVisita() : "");
            ps.setString(6, visita.getFechaProgramada() != null ? visita.getFechaProgramada() : "");
            ps.setString(7, visita.getHoraProgramada() != null ? visita.getHoraProgramada() : "");
            ps.setString(8, visita.getPlaca() != null ? visita.getPlaca() : "N/A");
            ps.setString(9, visita.getEstado() != null ? visita.getEstado() : "PROGRAMADA");
            ps.setString(10, visita.getMotivoVisita() != null ? visita.getMotivoVisita() : "");

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al programar visita en SQLite: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean actualizarFechaHora(int idVisita, String nuevaFecha, String nuevaHora) {
        String sql = "UPDATE visitas_programadas SET fecha_programada = ?, hora_programada = ? WHERE id_visita = ?";
        Connection conn = getConn();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevaFecha);
            ps.setString(2, nuevaHora);
            ps.setInt(3, idVisita);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar fecha/hora de visita: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<VisitaProgramada> obtenerVisitasProgramadas() {
        String sql = "SELECT * FROM visitas_programadas ORDER BY id_visita DESC";
        List<VisitaProgramada> visitas = new ArrayList<>();
        Connection conn = getConn();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                VisitaProgramada visita = new VisitaProgramada();
                visita.setIdVisita(rs.getInt("id_visita"));
                visita.setIdResidente(rs.getInt("id_residente"));
                visita.setNombresVisita(rs.getString("nombres_visita"));
                visita.setApellidosVisita(rs.getString("apellidos_visita"));
                visita.setCedulaVisita(rs.getString("cedula_visita"));
                try {
                    visita.setTelefonoVisita(rs.getString("telefono_visita"));
                } catch (SQLException ignored) {}
                visita.setFechaProgramada(rs.getString("fecha_programada"));
                visita.setHoraProgramada(rs.getString("hora_programada"));
                visita.setPlaca(rs.getString("placa_vehiculo"));
                String estadoStr = rs.getString("estado");
                visita.setEstado(estadoStr != null ? EstadoVisita.valueOf(estadoStr.toUpperCase()) : EstadoVisita.PROGRAMADA);
                visita.setMotivoVisita(rs.getString("motivo_visita"));
                visita.setTipoVisita(rs.getString("tipo_visita"));
                visitas.add(visita);
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar visitas programadas: " + e.getMessage());
            e.printStackTrace();
        }
        return visitas;
    }

    @Override
    public boolean cancelarVisitaProgramada(Integer idVisita) {
        String sql = "UPDATE visitas_programadas SET estado = 'CANCELADA' WHERE id_visita = ?";
        Connection conn = getConn();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVisita);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al cancelar visita programada: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Usuario_Checkin> obtenerResidentes() {
        String sql = "SELECT u.id_usuario, u.numero_documento, u.nombres, u.apellidos, u.correo, u.telefono, u.estado, " +
                     "u.fecha_registro, u.fecha_actualizacion, " +
                     "(COALESCE(e.nombre, 'Torre A') || ' - ' || COALESCE(i.codigo, i.numero, '101')) AS lugar_residencia " +
                     "FROM usuario u " +
                     "LEFT JOIN usuario_inmueble ui ON u.id_usuario = ui.id_usuario " +
                     "LEFT JOIN inmueble i ON ui.id_inmueble = i.id_inmueble " +
                     "LEFT JOIN edificio e ON i.id_edificio = e.id_edificio " +
                     "WHERE u.estado = 'ACTIVO'";
        List<Usuario_Checkin> residentes = new ArrayList<>();
        Connection conn = getConn();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Usuario_Checkin usuario = new Usuario_Checkin();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombres(rs.getString("nombres"));
                usuario.setApellidos(rs.getString("apellidos"));
                usuario.setNumeroDocumento(rs.getString("numero_documento"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setEstado(rs.getString("estado"));
                usuario.setFechaRegistro(rs.getString("fecha_registro"));
                usuario.setFechaActualizacion(rs.getString("fecha_actualizacion"));
                usuario.setVivienda(rs.getString("lugar_residencia"));
                residentes.add(usuario);
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar residentes para visitas: " + e.getMessage());
            e.printStackTrace();
        }
        return residentes;
    }

    @Override
    public VisitaProgramada obtenerVisitaPorId(int idVisita) {
        String sql = "SELECT * FROM visitas_programadas WHERE id_visita = ?";
        Connection conn = getConn();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVisita);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    VisitaProgramada v = new VisitaProgramada();
                    v.setIdVisita(rs.getInt("id_visita"));
                    v.setIdResidente(rs.getInt("id_residente"));
                    v.setNombresVisita(rs.getString("nombres_visita"));
                    v.setApellidosVisita(rs.getString("apellidos_visita"));
                    v.setCedulaVisita(rs.getString("cedula_visita"));
                    String estadoStr = rs.getString("estado");
                    v.setEstado(estadoStr != null ? EstadoVisita.valueOf(estadoStr.toUpperCase()) : EstadoVisita.PROGRAMADA);
                    v.setMotivoVisita(rs.getString("motivo_visita"));
                    return v;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener visita por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean marcarComoRealizada(int idVisita) {
        String sql = "UPDATE visitas_programadas SET estado = 'REALIZADA' WHERE id_visita = ?";
        Connection conn = getConn();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVisita);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al marcar visita como realizada: " + e.getMessage());
        }
        return false;
    }

    @Override
    public String obtenerInfoResidentePorId(int idResidente) {
        String sql = "SELECT u.nombres, u.apellidos, " +
                     "COALESCE(i.codigo, i.numero, 'Sin unidad') AS unidad " +
                     "FROM usuario u " +
                     "LEFT JOIN usuario_inmueble ui ON u.id_usuario = ui.id_usuario " +
                     "LEFT JOIN inmueble i ON ui.id_inmueble = i.id_inmueble " +
                     "WHERE u.id_usuario = ? LIMIT 1";
        Connection conn = getConn();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idResidente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nombres") + " " + rs.getString("apellidos")
                           + " (Depto. " + rs.getString("unidad") + ")";
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener info de residente por ID: " + e.getMessage());
        }
        return null;
    }
}
