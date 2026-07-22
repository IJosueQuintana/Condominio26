package fis.dsw.sgc.reservas.dao;

import fis.dsw.sgc.conexion_bd.DBConnection;
import fis.dsw.sgc.reservas.dto.EspacioReservableDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion SQLite del DAO de espacios reservables.
 * Solo realiza consultas de lectura sobre la tabla espacio_comun.
 */
public class EspacioReservableDAOSQLite implements IEspacioReservableDAO {

    private Connection getConnection() {
        return DBConnection.getInstance().getConnection();
    }

    private static final String BASE_SELECT =
            "SELECT id_espacio_comun, id_inmueble, nombre, capacidad_maxima, "
          + "hora_apertura, hora_cierre, costo_reserva_centavos, "
          + "requiere_aprobacion, reglamento_uso, estado "
          + "FROM espacio_comun ";

    @Override
    public List<EspacioReservableDTO> listarDisponibles() {
        String sql = BASE_SELECT + "WHERE estado = 'DISPONIBLE' ORDER BY nombre";
        return ejecutarLista(sql);
    }

    @Override
    public List<EspacioReservableDTO> listarTodos() {
        String sql = BASE_SELECT + "ORDER BY nombre";
        return ejecutarLista(sql);
    }

    @Override
    public EspacioReservableDTO buscarPorId(int idEspacioComun) {
        String sql = BASE_SELECT + "WHERE id_espacio_comun = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, idEspacioComun);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar espacio comun por ID: " + e.getMessage());
        }
        return null;
    }

    private List<EspacioReservableDTO> ejecutarLista(String sql) {
        List<EspacioReservableDTO> resultado = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultado.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar espacios comunes: " + e.getMessage());
        }
        return resultado;
    }

    private EspacioReservableDTO mapear(ResultSet rs) throws SQLException {
        EspacioReservableDTO dto = new EspacioReservableDTO();
        dto.setIdEspacioComun(rs.getInt("id_espacio_comun"));
        dto.setIdInmueble(rs.getInt("id_inmueble"));
        dto.setNombre(rs.getString("nombre"));
        dto.setCapacidadMaxima(rs.getInt("capacidad_maxima"));
        dto.setHoraApertura(rs.getString("hora_apertura"));
        dto.setHoraCierre(rs.getString("hora_cierre"));
        dto.setCostoReservaCentavos(rs.getInt("costo_reserva_centavos"));
        dto.setRequiereAprobacion(rs.getInt("requiere_aprobacion") == 1);
        dto.setReglamentoUso(rs.getString("reglamento_uso"));
        dto.setEstado(rs.getString("estado"));
        return dto;
    }
}
