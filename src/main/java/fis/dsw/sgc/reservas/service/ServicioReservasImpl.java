package fis.dsw.sgc.reservas.service;

import fis.dsw.sgc.conexion_bd.DBConnection;
import fis.dsw.sgc.finanzas.service.IFachadaParaReservas;
import fis.dsw.sgc.inmuebles.service.IInmueblesService;
import fis.dsw.sgc.reservas.dao.IObservacionReservaDAO;
import fis.dsw.sgc.reservas.dao.IReservaDAO;
import fis.dsw.sgc.reservas.dao.ObservacionReservaDAOSQLite;
import fis.dsw.sgc.reservas.dao.ReservaDAOSQLite;
import fis.dsw.sgc.inmuebles.dto.EspacioReservableDTO;
import fis.dsw.sgc.reservas.dto.ObservacionReservaDTO;
import fis.dsw.sgc.reservas.dto.ReservaDTO;
import fis.dsw.sgc.reservas.model.EstadoActiva;
import fis.dsw.sgc.reservas.model.EstadoCancelada;
import fis.dsw.sgc.reservas.model.EstadoFinalizada;
import fis.dsw.sgc.reservas.model.Reserva;
import fis.dsw.sgc.administracion.service.IGestionUsuariosAPI;
import fis.dsw.sgc.administracion.model.Usuario;

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
    private IInmueblesService servicioInmuebles;

    // Conexion con el modulo de Finanzas (opcional; inyectada desde Main).
    private IFachadaParaReservas fachadaFinanzas;

    // Conexion con el modulo de Usuarios (inyectada desde Main).
    private IGestionUsuariosAPI gestionUsuariosAPI;

    private static ServicioReservasImpl instancia;

    public static ServicioReservasImpl getInstancia() {
        if (instancia == null) {
            instancia = new ServicioReservasImpl();
        }
        return instancia;
    }

    public ServicioReservasImpl() {
        this(new ReservaDAOSQLite(),
             new ObservacionReservaDAOSQLite(),
             null,
             null);
    }

    /** Constructor completo para inyeccion de dependencias (tests / Main). */
    public ServicioReservasImpl(IReservaDAO reservaDAO,
                                IObservacionReservaDAO observacionDAO,
                                IInmueblesService servicioInmuebles,
                                IFachadaParaReservas fachadaFinanzas) {
        this.reservaDAO = reservaDAO;
        this.observacionDAO = observacionDAO;
        this.servicioInmuebles = servicioInmuebles;
        this.fachadaFinanzas = fachadaFinanzas;
    }

    public void setServicioInmuebles(IInmueblesService s) {
        this.servicioInmuebles = s;
    }

    public void setGestionUsuariosAPI(IGestionUsuariosAPI api) {
        this.gestionUsuariosAPI = api;
    }

    /** Permite a Main inyectar la fachada de Finanzas despues de construir el servicio. */
    public void setFachadaFinanzas(IFachadaParaReservas fachadaFinanzas) {
        this.fachadaFinanzas = fachadaFinanzas;
    }

    // ==================================================================
    // Consultas
    // ==================================================================

    @Override
    public int obtenerIdUsuarioPorCorreo(String correo) {
        String sql = "SELECT id_usuario FROM usuario WHERE correo = ?";
        try (PreparedStatement ps = DBConnection.getInstance().getConnection()
                .prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_usuario");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener id_usuario por correo: " + e.getMessage());
        }
        return -1; // No encontrado
    }

    @Override
    public List<Reserva> listarReservasPorUsuario(int idUsuario) {
        List<Reserva> reservas = aModelo(reservaDAO.buscarPorUsuario(idUsuario));
        for (Reserva r : reservas) {
            cargarObservaciones(r);
            r.getEstado().evaluarVencimiento(r, this);
        }
        return reservas;
    }

    @Override
    public List<Reserva> listarTodasLasReservas() {
        List<Reserva> reservas = aModelo(reservaDAO.buscarTodas());
        for (Reserva r : reservas) {
            cargarObservaciones(r);
            r.getEstado().evaluarVencimiento(r, this);
        }
        return reservas;
    }

    @Override
    public Reserva buscarReserva(int idReserva) {
        ReservaDTO dto = reservaDAO.buscarPorId(idReserva);
        if (dto == null) return null;
        Reserva reserva = Reserva.desdeDTO(dto);
        cargarObservaciones(reserva);
        return reserva;
    }

    private void cargarObservaciones(Reserva reserva) {
        for (ObservacionReservaDTO obsDto : observacionDAO.buscarPorReserva(reserva.getId())) {
            reserva.agregarObservacion(
                    fis.dsw.sgc.reservas.model.Observacion.desdeDTO(obsDto));
        }
    }

    @Override
    public List<Reserva> listarReservasPorEspacioYFecha(int idEspacioComun, String fecha) {
        List<Reserva> reservas = aModelo(reservaDAO.buscarPorEspacioYFecha(idEspacioComun, fecha));
        for (Reserva r : reservas) {
            r.getEstado().evaluarVencimiento(r, this);
        }
        return reservas;
    }

    @Override
    public List<EspacioReservableDTO> listarEspaciosDisponibles() {
        if (servicioInmuebles != null) {
            return servicioInmuebles.listarEspaciosReservables();
        }
        return new ArrayList<>();
    }

    // ==================================================================
    // Comandos
    // ==================================================================

    @Override
    public String crearReserva(int idUsuario, int idEspacioComun, String fecha,
                                String horaInicio, String horaFin) {

        // 1) Verificacion de mora en Finanzas (patron Fachada).
        //    El residente con deudas en mora NO puede reservar.
        String cedula = obtenerNumeroDocumento(idUsuario);
        if (fachadaFinanzas != null && cedula != null
                && fachadaFinanzas.tieneDeudasEnMora(cedula)) {
            System.out.println("[Reservas] Reserva rechazada: el residente " + cedula
                    + " tiene deudas en mora.");
            return "No puedes hacer la reserva porque tienes una deuda pendiente. Por favor, paga tu deuda para continuar.";
        }

        // 2) Tarifa vigente del espacio (copia del costo al momento de reservar).
        int costoCentavos = 0;
        EspacioReservableDTO espacio = null;
        if (servicioInmuebles != null) {
            espacio = servicioInmuebles.buscarEspacioReservablePorId(idEspacioComun);
        }
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
        nueva.setEstado(new EstadoActiva());

        // 4) Validacion de solapamiento contra las reservas activas del dia.
        for (Reserva existente : listarReservasPorEspacioYFecha(idEspacioComun, fecha)) {
            if (nueva.seSuperponeCon(existente)) {
                System.out.println("[Reservas] Reserva rechazada: el horario se "
                        + "superpone con una reserva activa existente.");
                return "No puedes reservar en esta hora porque estás chocando con un horario donde ya se está ocupando el espacio.";
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
            fachadaFinanzas.registrarDeuda(
                    cedula, "RESERVA", fechaMaximaPago,
                    descripcion, costoCentavos / 100.0);
        }

        return null;
    }

    @Override
    public boolean cancelarReserva(int idReserva, String motivo) {
        ReservaDTO dto = reservaDAO.buscarPorId(idReserva);
        if (dto == null) {
            System.out.println("[Reservas] No existe la reserva " + idReserva);
            return false;
        }
        reservaDAO.cancelar(idReserva, motivo);
        
        // Enviar notificacion de cancelacion al residente
        try {
            fis.dsw.sgc.comunicacion.service.IComunicacionService comm = new fis.dsw.sgc.comunicacion.service.ComunicacionServiceImpl(new fis.dsw.sgc.comunicacion.dao.ComunicacionDAOSQLite());
            long emisor = comm.obtenerIdEmisorActual();
            String titulo = "Reserva Cancelada";
            String contenido = "Su reserva del espacio " + dto.getNombreEspacio() + " para la fecha " + dto.getFechaReserva() + " ha sido cancelada.\nMotivo: " + motivo;
            fis.dsw.sgc.comunicacion.dto.EnviarComunicacionDTO dtoCom = new fis.dsw.sgc.comunicacion.dto.EnviarComunicacionDTO(emisor, (long) dto.getIdUsuario(), "MENSAJE_RESIDENTES", "NORMAL", titulo, contenido);
            comm.enviarMensaje(dtoCom);
        } catch (Exception e) {
            System.err.println("Error al enviar notificacion de cancelacion: " + e.getMessage());
        }
        
        return true;
    }

    @Override
    public void registrarObservacion(int idReserva, int idAutor, String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            System.out.println("[Reservas] Observacion vacia, no se registra.");
            return;
        }
        
        Reserva reserva = buscarReserva(idReserva);
        if (reserva != null) {
            if (reserva.getObservaciones().size() >= 2) {
                System.out.println("[Reservas] Límite de observaciones (2) alcanzado para la reserva " + idReserva);
                return;
            }
            boolean yaComento = reserva.getObservaciones().stream()
                    .anyMatch(obs -> obs.getIdAutor() == idAutor);
            if (yaComento) {
                System.out.println("[Reservas] El usuario " + idAutor + " ya realizó una observación para esta reserva.");
                return;
            }
        }
        
        observacionDAO.guardar(new ObservacionReservaDTO(idReserva, idAutor, texto));
    }

    @Override
    public void solicitarMulta(int idReserva, String motivo) {
        if (fachadaFinanzas != null) {
            Reserva reserva = buscarReserva(idReserva);
            if (reserva != null) {
                String cedula = obtenerNumeroDocumento(reserva.getIdResidente());
                if (cedula != null) {
                    fachadaFinanzas.registrarDeuda(
                        cedula, "MULTA", LocalDate.now().plusDays(7),
                        "Multa por reserva: " + motivo, 50.0
                    );
                }
            }
        } else {
            System.out.println("[Reservas] Solicitud de multa a Finanzas -> motivo: "
                    + motivo + " (reserva " + idReserva + ") - FACHADA NULA");
        }
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
        if (gestionUsuariosAPI != null) {
            Usuario u = gestionUsuariosAPI.obtenerUsuarioPorId(idUsuario);
            return u != null ? u.getCedula() : null;
        } else {
            System.err.println("[Reservas] API de Gestión de Usuarios no inyectada");
            return null;
        }
    }
}
