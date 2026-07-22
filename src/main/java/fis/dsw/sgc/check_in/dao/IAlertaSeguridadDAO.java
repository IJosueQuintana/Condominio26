package fis.dsw.sgc.check_in.dao;

import fis.dsw.sgc.check_in.model.AlertaSeguridad;
import java.util.List;

public interface IAlertaSeguridadDAO {
    boolean guardar(AlertaSeguridad alerta);
    List<AlertaSeguridad> listarTodas();
    boolean registrarNotificacion(int idUsuarioDestino, String titulo, String contenido, String tipo);
}
