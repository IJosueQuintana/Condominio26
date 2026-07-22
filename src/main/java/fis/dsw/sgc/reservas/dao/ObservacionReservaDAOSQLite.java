package fis.dsw.sgc.reservas.dao;

import fis.dsw.sgc.conexion_bd.DBConnection;
import fis.dsw.sgc.reservas.dto.ObservacionReservaDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ObservacionReservaDAOSQLite implements IObservacionReservaDAO {

    private Connection getConnection() {
        return DBConnection.getInstance().getConnection();
    }

    @Override
    public void guardar(ObservacionReservaDTO observacion) {
        String sql = "INSERT INTO observacion_reserva (id_reserva, id_usuario, texto) "
                + "VALUES (?, ?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, observacion.getIdReserva());
            ps.setInt(2, observacion.getIdUsuario());
            ps.setString(3, observacion.getTexto());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al guardar observación: " + e.getMessage());
        }
    }

    @Override
    public List<ObservacionReservaDTO> buscarPorReserva(int idReserva) {
        String sql = "SELECT o.*, u.nombres || ' ' || u.apellidos AS nombre_usuario "
                + "FROM observacion_reserva o "
                + "JOIN usuario u ON o.id_usuario = u.id_usuario "
                + "WHERE o.id_reserva = ? "
                + "ORDER BY o.fecha_hora DESC";
        List<ObservacionReservaDTO> resultado = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, idReserva);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ObservacionReservaDTO dto = new ObservacionReservaDTO();
                    dto.setIdObservacion(rs.getInt("id_observacion"));
                    dto.setIdReserva(rs.getInt("id_reserva"));
                    dto.setIdUsuario(rs.getInt("id_usuario"));
                    dto.setTexto(rs.getString("texto"));
                    dto.setFechaHora(rs.getString("fecha_hora"));
                    dto.setNombreUsuario(rs.getString("nombre_usuario"));
                    resultado.add(dto);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar observaciones: " + e.getMessage());
        }
        return resultado;
    }
}
