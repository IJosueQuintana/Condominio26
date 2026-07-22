package fis.dsw.sgc.check_in.service;

import fis.dsw.sgc.conexion_bd.DBConnection;
import fis.dsw.sgc.check_in.dao.IAlertaSeguridadDAO;
import fis.dsw.sgc.check_in.exception.CheckInException;
import fis.dsw.sgc.check_in.model.AlertaSeguridad;
import fis.dsw.sgc.check_in.model.EstadoAlerta;
import fis.dsw.sgc.check_in.model.PrioridadAlerta;
import fis.dsw.sgc.check_in.model.TipoAlerta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AlertaSeguridadServiceImpl implements IAlertaSeguridadService {

    private final IAlertaSeguridadDAO alertaSeguridadDAO;

    public AlertaSeguridadServiceImpl() {
        this(new fis.dsw.sgc.check_in.dao.AlertaSeguridadDAO());
    }

    public AlertaSeguridadServiceImpl(IAlertaSeguridadDAO alertaSeguridadDAO) {
        this.alertaSeguridadDAO = alertaSeguridadDAO;
    }

    @Override
    public AlertaSeguridad emitirAlerta(TipoAlerta tipoAlerta, String destinatarioTipo,
                                        String identificadorDestino, String mensaje,
                                        int idUsuarioReporta) throws CheckInException {
        if (tipoAlerta == null) {
            throw new CheckInException("Debe seleccionar un tipo de alerta válido.");
        }
        if (!tipoAlerta.validarMensaje(mensaje)) {
            throw new CheckInException("El contenido del mensaje de alerta no puede estar vacío.");
        }

        AlertaSeguridad alerta = new AlertaSeguridad();
        alerta.setTipo(tipoAlerta);
        alerta.setDescripcion("[" + destinatarioTipo + " - " + (identificadorDestino != null ? identificadorDestino : "Todos") + "] " + mensaje.trim());
        alerta.setNivel(tipoAlerta.obtenerPrioridadPorDefecto());
        alerta.setEstado(EstadoAlerta.ABIERTA);
        alerta.setIdUsuarioReporta(idUsuarioReporta > 0 ? idUsuarioReporta : 1);
        alerta.setFechaCreacion(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        boolean guardado = alertaSeguridadDAO.guardar(alerta);
        if (!guardado) {
            throw new CheckInException("No se pudo guardar la alerta de seguridad en la base de datos.");
        }

        // Integración con el Módulo de Comunicaciones: registrar notificaciones para los destinatarios
        List<Integer> idsUsuarios = obtenerIdsUsuariosDestinatarios(destinatarioTipo, identificadorDestino);
        String tituloNotif = "ALERTA " + tipoAlerta.obtenerNombre().toUpperCase();
        for (Integer idUsr : idsUsuarios) {
            alertaSeguridadDAO.registrarNotificacion(idUsr, tituloNotif, mensaje.trim(), "ALERTA_EMERGENCIA");
        }

        return alerta;
    }

    @Override
    public List<AlertaSeguridad> obtenerHistorialAlertas() {
        return alertaSeguridadDAO.listarTodas();
    }

    private List<Integer> obtenerIdsUsuariosDestinatarios(String destinatarioTipo, String identificadorDestino) {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT id_usuario FROM usuario WHERE estado = 'ACTIVO'";

        if ("Residente / unidad específica".equalsIgnoreCase(destinatarioTipo) && identificadorDestino != null && !identificadorDestino.isBlank()) {
            sql = "SELECT id_usuario FROM usuario WHERE (numero_documento = ? OR correo = ?) AND estado = 'ACTIVO'";
        } else if ("Torre / bloque específico".equalsIgnoreCase(destinatarioTipo) && identificadorDestino != null && !identificadorDestino.isBlank()) {
            sql = "SELECT DISTINCT u.id_usuario FROM usuario u " +
                  "LEFT JOIN usuario_inmueble ui ON u.id_usuario = ui.id_usuario " +
                  "LEFT JOIN inmueble i ON ui.id_inmueble = i.id_inmueble " +
                  "LEFT JOIN edificio e ON i.id_edificio = e.id_edificio " +
                  "WHERE (e.nombre LIKE ? OR i.codigo LIKE ? OR i.numero LIKE ?) AND u.estado = 'ACTIVO'";
        }

        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (sql.contains("WHERE (numero_documento = ?")) {
                pstmt.setString(1, identificadorDestino.trim());
                pstmt.setString(2, identificadorDestino.trim());
            } else if (sql.contains("WHERE (e.nombre LIKE ?")) {
                String pat = "%" + identificadorDestino.trim() + "%";
                pstmt.setString(1, pat);
                pstmt.setString(2, pat);
                pstmt.setString(3, pat);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getInt("id_usuario"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar destinatarios de notificaciones: " + e.getMessage());
        }

        if (ids.isEmpty()) {
            try (PreparedStatement pstmtAll = conn.prepareStatement("SELECT id_usuario FROM usuario WHERE estado = 'ACTIVO'");
                 ResultSet rsAll = pstmtAll.executeQuery()) {
                while (rsAll.next()) {
                    ids.add(rsAll.getInt("id_usuario"));
                }
            } catch (SQLException ignored) {}
        }
        return ids;
    }
}
