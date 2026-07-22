package fis.dsw.sgc.check_in.service;

import fis.dsw.sgc.check_in.exception.CheckInException;
import fis.dsw.sgc.check_in.model.AlertaSeguridad;
import fis.dsw.sgc.check_in.model.TipoAlerta;

import java.util.List;

public interface IAlertaSeguridadService {
    AlertaSeguridad emitirAlerta(TipoAlerta tipoAlerta, String destinatarioTipo, String identificadorDestino, String mensaje, int idUsuarioReporta) throws CheckInException;
    List<AlertaSeguridad> obtenerHistorialAlertas();
}
