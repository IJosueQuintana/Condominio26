package fis.dsw.sgc.check_in.dao;

import fis.dsw.sgc.conexion_bd.DBConnection;
import fis.dsw.sgc.check_in.model.AlertaSeguridad;
import fis.dsw.sgc.check_in.model.EstadoAlerta;
import fis.dsw.sgc.check_in.model.PrioridadAlerta;
import fis.dsw.sgc.check_in.model.TipoAlertaAvisoGeneral;
import fis.dsw.sgc.check_in.model.TipoAlertaEmergencia;
import fis.dsw.sgc.check_in.model.TipoAlertaSimulacro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlertaSeguridadDAO implements IAlertaSeguridadDAO {

    // Constructor explícito requerido para instanciación manual desde Main (DI manual del jefe de proyecto)
    public AlertaSeguridadDAO() {}

    @Override
    public boolean guardar(AlertaSeguridad alerta) {
        String sql = "INSERT INTO alerta_seguridad (id_registro_entrada, id_usuario_reporta, tipo, descripcion, nivel, estado) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = DBConnection.getInstance().getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            if (alerta.getIdRegistroEntrada() != null && alerta.getIdRegistroEntrada() > 0) {
                pstmt.setInt(1, alerta.getIdRegistroEntrada());
            } else {
                pstmt.setNull(1, java.sql.Types.INTEGER);
            }
            pstmt.setInt(2, alerta.getIdUsuarioReporta() > 0 ? alerta.getIdUsuarioReporta() : 1);
            pstmt.setString(3, alerta.getTipo() != null ? alerta.getTipo().obtenerNombre() : "Aviso General");
            pstmt.setString(4, alerta.getDescripcion() != null ? alerta.getDescripcion() : "");
            pstmt.setString(5, alerta.getNivel() != null ? alerta.getNivel().name() : "MEDIA");
            pstmt.setString(6, alerta.getEstado() != null ? alerta.getEstado().name() : "ABIERTA");

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        alerta.setIdAlerta(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error al guardar alerta de seguridad: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<AlertaSeguridad> listarTodas() {
        List<AlertaSeguridad> alertas = new ArrayList<>();
        String sql = "SELECT * FROM alerta_seguridad ORDER BY id_alerta DESC";
        Connection conn = DBConnection.getInstance().getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                AlertaSeguridad a = new AlertaSeguridad();
                a.setIdAlerta(rs.getInt("id_alerta"));
                int idEntrada = rs.getInt("id_registro_entrada");
                if (!rs.wasNull()) a.setIdRegistroEntrada(idEntrada);
                a.setIdUsuarioReporta(rs.getInt("id_usuario_reporta"));
                
                String tipoStr = rs.getString("tipo");
                if ("Emergencia".equalsIgnoreCase(tipoStr)) a.setTipo(new TipoAlertaEmergencia());
                else if ("Simulacro".equalsIgnoreCase(tipoStr)) a.setTipo(new TipoAlertaSimulacro());
                else a.setTipo(new TipoAlertaAvisoGeneral());

                a.setDescripcion(rs.getString("descripcion"));
                try {
                    a.setNivel(PrioridadAlerta.valueOf(rs.getString("nivel").toUpperCase()));
                } catch (Exception ex) {
                    a.setNivel(PrioridadAlerta.MEDIA);
                }
                try {
                    a.setEstado(EstadoAlerta.valueOf(rs.getString("estado").toUpperCase()));
                } catch (Exception ex) {
                    a.setEstado(EstadoAlerta.ABIERTA);
                }
                a.setFechaCreacion(rs.getString("fecha_creacion"));
                alertas.add(a);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar alertas de seguridad: " + e.getMessage());
        }
        return alertas;
    }

    @Override
    public boolean registrarNotificacion(int idUsuarioDestino, String titulo, String contenido, String tipo) {
        String sql = "INSERT INTO notificacion (id_usuario, tipo, titulo, contenido, fecha_creacion, fecha_envio, leida) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0)";
        Connection conn = DBConnection.getInstance().getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuarioDestino);
            pstmt.setString(2, tipo != null ? tipo : "ALERTA");
            pstmt.setString(3, titulo != null ? titulo : "Notificación de Seguridad");
            pstmt.setString(4, contenido != null ? contenido : "");
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar notificación en Comunicaciones: " + e.getMessage());
            return false;
        }
    }
}
