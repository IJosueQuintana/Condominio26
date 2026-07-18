package fis.dsw.sgc.reservas.service;

import java.util.Date;
import java.sql.Time;

public interface IServicioReservas {
    boolean crearReserva(String idUsr, String idEsp, Date f, Time ini, Time fin);
    boolean cancelarReserva(String idRes);
    void registrarObservacion(String idRes, String texto);
    void adjudicarMultaPorConflicto(String idUsr, String idRes, String mot, double monto);
}
