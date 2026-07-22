package fis.dsw.sgc.inmuebles.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fis.dsw.sgc.conexion_bd.DBConnection;
import fis.dsw.sgc.inmuebles.dto.CasoFortuitoDTO;
import fis.dsw.sgc.inmuebles.model.CasoFortuito;

public class CasoFortuitoDAOMySQL implements ICasoFortuitoDAO {

    @Override
    public void guardarIncidente(CasoFortuito caso) {
        String sql = "INSERT INTO caso_fortuito (id_inmueble, descripcion, estado) VALUES (?, ?, 'REGISTRADO')";

        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, caso.getIdInmueble());
            pstmt.setString(2, caso.getDescripcion());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al guardar caso fortuito: " + e.getMessage());
        }
    }

    @Override
    public List<CasoFortuitoDTO> listarPorInmueble(int idInmueble) {
        List<CasoFortuitoDTO> resultado = new ArrayList<>();
        String sql = "SELECT id_caso, descripcion, fecha, estado FROM caso_fortuito " +
                "WHERE id_inmueble = ? ORDER BY fecha DESC";

        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idInmueble);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    resultado.add(new CasoFortuitoDTO(
                            rs.getInt("id_caso"),
                            rs.getString("descripcion"),
                            rs.getString("fecha"),
                            rs.getString("estado")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar casos fortuitos: " + e.getMessage());
        }
        return resultado;
    }
}