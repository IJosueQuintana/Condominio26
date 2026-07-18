package fis.dsw.sgc.reservas.service;

import fis.dsw.sgc.reservas.dao.IReservaDAO;
import java.util.Date;
import java.sql.Time;

public class ServicioReservasImpl implements IServicioReservas {
    private IReservaDAO reservaDAO;

    @Override
    public boolean crearReserva(String idUsr, String idEsp, Date f, Time ini, Time fin) {
        return false;
    }

    @Override
    public boolean cancelarReserva(String idRes) {
        return false;
    }

    @Override
    public void registrarObservacion(String idRes, String texto) {}

    @Override
    public void adjudicarMultaPorConflicto(String idUsr, String idRes, String mot, double monto) {}
}
