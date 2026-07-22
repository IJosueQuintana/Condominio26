package fis.dsw.sgc.reservas.controller;

import fis.dsw.sgc.reservas.service.IServicioReservas;
import fis.dsw.sgc.reservas.service.ServicioReservasImpl;

/**
 * Controlador coordinador (no ligado a un FXML) que centraliza las acciones
 * de alto nivel del modulo de Reservas y delega en el servicio de aplicacion.
 *
 * Las pantallas concretas usan sus propios controladores JavaFX
 * (AnadirReservaController, VerReservaController, AuditarReservasController),
 * pero esta clase queda como fachada consistente con IServicioReservas por si
 * se requiere orquestacion desde otras capas.
 */
public class ControladorReservas {

    private final IServicioReservas servicioReservas;

    public ControladorReservas() {
        this(new ServicioReservasImpl());
    }

    public ControladorReservas(IServicioReservas servicioReservas) {
        this.servicioReservas = servicioReservas;
    }

    /** Consulta las reservas activas de un espacio en una fecha ("YYYY-MM-DD"). */
    public void consultarDisponibilidadUI(int idEspacioComun, String fecha) {
        servicioReservas.listarReservasPorEspacioYFecha(idEspacioComun, fecha);
    }

    /** Genera una nueva reserva delegando la validacion en el servicio. */
    public boolean generarSolicitudReservaUI(int idUsuario, int idEspacioComun,
                                             String fecha, String horaInicio, String horaFin) {
        return servicioReservas.crearReserva(idUsuario, idEspacioComun, fecha, horaInicio, horaFin);
    }

    /** Cancela una reserva por su id. */
    public boolean cancelarReservaUI(int idReserva, String motivo) {
        return servicioReservas.cancelarReserva(idReserva, motivo);
    }

    /**
     * La adjudicacion/creacion de multas es responsabilidad del modulo de
     * Finanzas (GRA). Desde Reservas solo se solicitaria via la fachada
     * IFachadaParaReservas cuando Main la inyecte. Metodo pendiente.
     */
    public void adjudicarMultaUI() {
        // Pendiente de integracion con Finanzas (ver reporte de avance).
    }
}
