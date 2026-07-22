package fis.dsw.sgc.reservas.service;

import fis.dsw.sgc.conexion_bd.DBConnection;
import fis.dsw.sgc.finanzas.service.IFachadaParaReservas;
import fis.dsw.sgc.reservas.dao.EspacioReservableDAOSQLite;
import fis.dsw.sgc.reservas.dao.IEspacioReservableDAO;
import fis.dsw.sgc.reservas.dao.IObservacionReservaDAO;
import fis.dsw.sgc.reservas.dao.IReservaDAO;
import fis.dsw.sgc.reservas.dao.ObservacionReservaDAOSQLite;
import fis.dsw.sgc.reservas.dao.ReservaDAOSQLite;
import fis.dsw.sgc.reservas.dto.EspacioReservableDTO;
import fis.dsw.sgc.reservas.dto.ObservacionReservaDTO;
import fis.dsw.sgc.reservas.dto.ReservaDTO;
import fis.dsw.sgc.reservas.model.EstadoReserva;
import fis.dsw.sgc.reservas.model.Reserva;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion del servicio de Reservas.
 *
 * Persistencia: delega en los DAO SQLite del propio modulo.
 * Finanzas (GRA): usa IFachadaParaReservas (patron Fachada del modulo de
 *   Finanzas) para verificar la mora del residente y registrar la deuda de
 *   la reserva. La fachada es opcional (se inyecta desde Main); si no esta
 *   presente, la reserva se crea igual pero sin interaccion financiera.
 */
public class ServicioReservasImpl implements IServicioReservas {

    private final IReservaDAO reservaDAO;
    private final IObservacionReservaDAO observacionDAO;
    private final IEspacioReservableDAO espacioDAO;

    // Conexion con el modulo de Finanzas (opcional; inyectada desde Main).
    private IFachadaParaReservas fachadaFinanzas;

    /** Constructor por defecto: cablea los DAO SQLite del modulo. */
    public ServicioReservasImpl() {
        this(new ReservaDAOSQLite(),
             new ObservacionReservaDAOSQLite(),
             new EspacioReservableDAOSQLite(),
             null);
    }

    /** Constructor completo para inyeccion de dependencias (tests / Main). */
    public ServicioReservasImpl(IReservaDAO reservaDAO,
                                IObservacionReservaDAO observacionDAO,
                                IEspacioReservableDAO espacioDAO,
                                IFachadaParaReservas fachadaFinanzas) {
        this.reservaDAO = reservaDAO;
        this.observacionDAO = observacionDAO;
        this.espacioDAO = espacioDAO;
        this.fachadaFinanzas = fachadaFinanzas;
    }

    /** Permite a Main inyectar la fachada de Finanzas despues de construir el servicio. */
    public void setFachadaFinanzas(IFachadaParaReservas fachadaFinanzas) {
        this.fachadaFinanzas = fachadaFinanzas;
    }

    // ==================================================================
    // Consultas
    // ==================================================================

    @Override
    public List<Reserva> listarReservasPorUsuario(int idUsuario) {
        return aModelo(reservaDAO.buscarPorUsuario(idUsuario));
    }

    @Override
    public List<Reserva> listarTodasLasReservas() {
        return aModelo(reservaDAO.buscarTodas());
    }

    @Override
    public Reserva buscarReserva(int idReserva) {
        ReservaDTO dto = reservaDAO.buscarPorId(idReserva);
        if (dto == null) return null;
        Reserva reserva = Reserva.desdeDTO(dto);
        // Carga (composicion) de las observaciones asociadas.
        for (ObservacionReservaDTO obsDto : observacionDAO.buscarPorReserva(idReserva)) {
            reserva.agregarObservacion(
                    fis.dsw.sgc.reservas.model.Observacion.desdeDTO(obsDto));
        }
        return reserva;
    }

    @Override
    public List<Reserva> listarReservasPorEspacioYFecha(int idEspacioComun, String fecha) {
        return aModelo(reservaDAO.buscarPorEspacioYFecha(idEspacioComun, fecha));
    }

    @Override
    public List<EspacioReservableDTO> listarEspaciosDisponibles() {
        return espacioDAO.listarDisponibles();
    }

    // ==================================================================
    // Comandos
    // ==================================================================

    @Override
    public boolean crearReserva(int idUsuario, int idEspacioComun, String fecha,
                                String horaInicio, String horaFin) {

        // 1) Verificacion de mora en Finanzas (patron Fachada).
        //    El residente con deudas en mora NO puede reservar.
        String cedula = obtenerNumeroDocumento(idUsuario);
        if (fachadaFinanzas != null && cedula != null
                && fachadaFinanzas.tieneDeudasEnMora(cedula)) {
            System.out.println("[Reservas] Reserva rechazada: el residente " + cedula
                    + " tiene deudas en mora.");
            return false;
        }

        // 2) Tarifa vigente del espacio (copia del costo al momento de reservar).
        int costoCentavos = 0;
        EspacioReservableDTO espacio = espacioDAO.buscarPorId(idEspacioComun);
        if (espacio != null) {
            costoCentavos = espacio.getCostoReservaCentavos();
        }

        // 3) Construir la reserva candidata.
        Reserva nueva = new Reserva();
        nueva.setIdResidente(idUsuario);
        nueva.setIdEspacio(idEspacioComun);
        nueva.setFechaReserva(fecha);
        nueva.setHoraInicio(horaInicio);
        nueva.setHoraFin(horaFin);
        nueva.setCostoAplicadoCentavos(costoCentavos);
        nueva.setEstado(EstadoReserva.ACTIVA);

        // 4) Validacion de solapamiento contra las reservas activas del dia.
        for (Reserva existente : listarReservasPorEspacioYFecha(idEspacioComun, fecha)) {
            if (nueva.seSuperponeCon(existente)) {
                System.out.println("[Reservas] Reserva rechazada: el horario se "
                        + "superpone con una reserva activa existente.");
                return false;
            }
        }

        // 5) Persistir. (El trigger de la BD tambien valida el solapamiento).
        reservaDAO.guardar(nueva.aDTO());

        // 6) Registrar la deuda por reserva en Finanzas, si aplica.
        if (fachadaFinanzas != null && cedula != null && costoCentavos > 0) {
            LocalDate fechaMaximaPago = LocalDate.now().plusDays(7);
            String descripcion = "Reserva de "
                    + (espacio != null ? espacio.getNombre() : "espacio comun")
                    + " (" + fecha + " " + horaInicio + "-" + horaFin + ")";
            fachadaFinanzas.registrarDeuda(cedula, "RESERVA", fechaMaximaPago,
                    descripcion, costoCentavos / 100.0);
        }

        return true;
    }

    @Override
    public boolean cancelarReserva(int idReserva, String motivo) {
        ReservaDTO dto = reservaDAO.buscarPorId(idReserva);
        if (dto == null) {
            System.out.println("[Reservas] No existe la reserva " + idReserva);
            return false;
        }
        reservaDAO.cancelar(idReserva, motivo);
        return true;
    }

    @Override
    public void registrarObservacion(int idReserva, int idAutor, String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            System.out.println("[Reservas] Observacion vacia, no se registra.");
            return;
        }
        observacionDAO.guardar(new ObservacionReservaDTO(idReserva, idAutor, texto));
    }

    @Override
    public void finalizarReservasVencidas() {
        reservaDAO.finalizarReservasVencidas();
    }

    // ==================================================================
    // Utilidades internas
    // ==================================================================

    private List<Reserva> aModelo(List<ReservaDTO> dtos) {
        List<Reserva> lista = new ArrayList<>();
        if (dtos == null) return lista;
        for (ReservaDTO dto : dtos) {
            lista.add(Reserva.desdeDTO(dto));
        }
        return lista;
    }

    /**
     * Obtiene el numero de documento (cedula) del residente a partir de su
     * id_usuario. Se consulta la tabla usuario (GRB) en modo lectura porque
     * la fachada de Finanzas trabaja con la cedula, mientras que Reservas
     * maneja el id numerico. Cuando el modulo de Administracion exponga esta
     * relacion mediante su propia API, esta consulta deberia reemplazarse.
     */
    private String obtenerNumeroDocumento(int idUsuario) {
        String sql = "SELECT numero_documento FROM usuario WHERE id_usuario = ?";
        try (PreparedStatement ps = DBConnection.getInstance().getConnection()
                .prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("numero_documento");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener numero de documento: " + e.getMessage());
        }
        return null;
    }
}
