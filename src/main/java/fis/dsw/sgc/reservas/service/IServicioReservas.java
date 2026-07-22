package fis.dsw.sgc.reservas.service;

import fis.dsw.sgc.reservas.dto.EspacioReservableDTO;
import fis.dsw.sgc.reservas.model.Reserva;

import java.util.List;

/**
 * Servicio de aplicacion del modulo GRD - Reservas.
 *
 * Firmas alineadas con el modelo/DTO/BD:
 *  - ids como int (id_usuario, id_espacio_comun, id_reserva)
 *  - fechas y horas como String ("YYYY-MM-DD" / "HH:MM")
 *
 * Conexiones con otros modulos:
 *  - GRB (Usuario)      : el idUsuario identifica al residente que reserva.
 *  - GRC (EspacioComun) : el idEspacioComun identifica el espacio ocupado.
 *  - GRA (Finanzas)     : a traves de IFachadaParaReservas se verifica la
 *                         mora del residente y se registra la deuda de reserva.
 */
public interface IServicioReservas {

    // -------- Consultas --------
    List<Reserva> listarReservasPorUsuario(int idUsuario);

    List<Reserva> listarTodasLasReservas();

    Reserva buscarReserva(int idReserva);

    List<Reserva> listarReservasPorEspacioYFecha(int idEspacioComun, String fecha);

    List<EspacioReservableDTO> listarEspaciosDisponibles();

    // -------- Comandos --------

    /**
     * Crea una reserva ACTIVA si el residente no tiene deudas en mora y el
     * horario no se superpone con otra reserva activa del mismo espacio.
     *
     * @return true si la reserva se creo; false si fue rechazada (mora o solapamiento).
     */
    boolean crearReserva(int idUsuario, int idEspacioComun, String fecha,
                         String horaInicio, String horaFin);

    boolean cancelarReserva(int idReserva, String motivo);

    void registrarObservacion(int idReserva, int idAutor, String texto);

    void finalizarReservasVencidas();
}
