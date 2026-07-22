package fis.dsw.sgc.reservas.dao;

import fis.dsw.sgc.conexion_bd.DBConnection;
import fis.dsw.sgc.reservas.dto.ReservaDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAOSQLite implements IReservaDAO {

    private Connection getConnection() {
        return DBConnection.getInstance().getConnection();
    }

    @Override
    public void guardar(ReservaDTO reserva) {
        String sql = "INSERT INTO reserva (id_usuario, id_espacio_comun, fecha_reserva, "
                + "hora_inicio, hora_fin, costo_aplicado_centavos, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, reserva.getIdUsuario());
            ps.setInt(2, reserva.getIdEspacioComun());
            ps.setString(3, reserva.getFechaReserva());
            ps.setString(4, reserva.getHoraInicio());
            ps.setString(5, reserva.getHoraFin());
            ps.setInt(6, reserva.getCostoAplicadoCentavos());
            ps.setString(7, reserva.getEstado());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al guardar reserva: " + e.getMessage());
        }
    }

    @Override
    public void actualizar(ReservaDTO reserva) {
        String sql = "UPDATE reserva SET id_usuario = ?, id_espacio_comun = ?, "
                + "fecha_reserva = ?, hora_inicio = ?, hora_fin = ?, "
                + "costo_aplicado_centavos = ?, estado = ?, "
                + "motivo_cancelacion = ?, fecha_cancelacion = ? "
                + "WHERE id_reserva = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, reserva.getIdUsuario());
            ps.setInt(2, reserva.getIdEspacioComun());
            ps.setString(3, reserva.getFechaReserva());
            ps.setString(4, reserva.getHoraInicio());
            ps.setString(5, reserva.getHoraFin());
            ps.setInt(6, reserva.getCostoAplicadoCentavos());
            ps.setString(7, reserva.getEstado());
            ps.setString(8, reserva.getMotivoCancelacion());
            ps.setString(9, reserva.getFechaCancelacion());
            ps.setInt(10, reserva.getIdReserva());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar reserva: " + e.getMessage());
        }
    }

    @Override
    public ReservaDTO buscarPorId(int idReserva) {
        String sql = "SELECT r.*, u.nombres || ' ' || u.apellidos AS nombre_residente, "
                + "ec.nombre AS nombre_espacio "
                + "FROM reserva r "
                + "JOIN usuario u ON r.id_usuario = u.id_usuario "
                + "JOIN espacio_comun ec ON r.id_espacio_comun = ec.id_espacio_comun "
                + "WHERE r.id_reserva = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, idReserva);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar reserva por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<ReservaDTO> buscarPorUsuario(int idUsuario) {
        String sql = "SELECT r.*, u.nombres || ' ' || u.apellidos AS nombre_residente, "
                + "ec.nombre AS nombre_espacio "
                + "FROM reserva r "
                + "JOIN usuario u ON r.id_usuario = u.id_usuario "
                + "JOIN espacio_comun ec ON r.id_espacio_comun = ec.id_espacio_comun "
                + "WHERE r.id_usuario = ? "
                + "ORDER BY r.fecha_reserva DESC";
        return ejecutarConsultaLista(sql, idUsuario);
    }

    @Override
    public List<ReservaDTO> buscarPorEspacioYFecha(int idEspacioComun, String fechaReserva) {
        String sql = "SELECT r.*, u.nombres || ' ' || u.apellidos AS nombre_residente, "
                + "ec.nombre AS nombre_espacio "
                + "FROM reserva r "
                + "JOIN usuario u ON r.id_usuario = u.id_usuario "
                + "JOIN espacio_comun ec ON r.id_espacio_comun = ec.id_espacio_comun "
                + "WHERE r.id_espacio_comun = ? AND r.fecha_reserva = ? AND r.estado = 'ACTIVA' "
                + "ORDER BY r.hora_inicio";
        List<ReservaDTO> resultado = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, idEspacioComun);
            ps.setString(2, fechaReserva);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(mapearResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar reservas por espacio y fecha: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public List<ReservaDTO> buscarTodas() {
        String sql = "SELECT r.*, u.nombres || ' ' || u.apellidos AS nombre_residente, "
                + "ec.nombre AS nombre_espacio "
                + "FROM reserva r "
                + "JOIN usuario u ON r.id_usuario = u.id_usuario "
                + "JOIN espacio_comun ec ON r.id_espacio_comun = ec.id_espacio_comun "
                + "ORDER BY r.fecha_reserva DESC, r.hora_inicio";
        List<ReservaDTO> resultado = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(mapearResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar todas las reservas: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public void cancelar(int idReserva, String motivoCancelacion) {
        String sql = "UPDATE reserva SET estado = 'CANCELADA', "
                + "motivo_cancelacion = ?, fecha_cancelacion = CURRENT_TIMESTAMP "
                + "WHERE id_reserva = ? AND estado = 'ACTIVA'";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, motivoCancelacion);
            ps.setInt(2, idReserva);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al cancelar reserva: " + e.getMessage());
        }
    }

    @Override
    public void finalizarReservasVencidas() {
        String sql = "UPDATE reserva SET estado = 'FINALIZADA' "
                + "WHERE estado = 'ACTIVA' "
                + "AND (fecha_reserva < DATE('now') "
                + "     OR (fecha_reserva = DATE('now') AND hora_fin <= TIME('now')))";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al finalizar reservas vencidas: " + e.getMessage());
        }
    }

    private List<ReservaDTO> ejecutarConsultaLista(String sql, int parametro) {
        List<ReservaDTO> resultado = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, parametro);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(mapearResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en consulta de reservas: " + e.getMessage());
        }
        return resultado;
    }

    private ReservaDTO mapearResultSet(ResultSet rs) throws SQLException {
        ReservaDTO dto = new ReservaDTO();
        dto.setIdReserva(rs.getInt("id_reserva"));
        dto.setIdUsuario(rs.getInt("id_usuario"));
        dto.setIdEspacioComun(rs.getInt("id_espacio_comun"));
        dto.setFechaCreacion(rs.getString("fecha_creacion"));
        dto.setFechaReserva(rs.getString("fecha_reserva"));
        dto.setHoraInicio(rs.getString("hora_inicio"));
        dto.setHoraFin(rs.getString("hora_fin"));
        dto.setCostoAplicadoCentavos(rs.getInt("costo_aplicado_centavos"));
        dto.setEstado(rs.getString("estado"));
        dto.setMotivoCancelacion(rs.getString("motivo_cancelacion"));
        dto.setFechaCancelacion(rs.getString("fecha_cancelacion"));
        dto.setNombreResidente(rs.getString("nombre_residente"));
        dto.setNombreEspacio(rs.getString("nombre_espacio"));
        return dto;
    }
}
