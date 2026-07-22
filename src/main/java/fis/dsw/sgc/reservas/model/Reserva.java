package fis.dsw.sgc.reservas.model;

import fis.dsw.sgc.reservas.dto.ObservacionReservaDTO;
import fis.dsw.sgc.reservas.dto.ReservaDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Entidad de dominio Reserva (modulo GRD).
 *
 * Conexiones:
 *  - Usuario (GRB)         -> idResidente  (id_usuario)
 *  - EspacioComun (GRC)    -> idEspacio    (id_espacio_comun)
 *  - COMPOSICION Observacion -> observaciones  (Reserva "1" *-- "*" Observacion)
 *
 * Tipos alineados con ReservaDTO / ObservacionReservaDTO y la BD:
 * ids int, fechas/horas String, costo en centavos (int).
 */
public class Reserva {

    private int id;                     // id_reserva
    private int idResidente;            // id_usuario  -> Usuario
    private int idEspacio;              // id_espacio_comun -> EspacioComun
    private String fechaCreacion;       // fecha_creacion
    private String fechaReserva;        // fecha_reserva  ("YYYY-MM-DD")
    private String horaInicio;          // hora_inicio    ("HH:MM[:SS]")
    private String horaFin;             // hora_fin
    private int costoAplicadoCentavos;  // costo_aplicado_centavos
    private EstadoReserva estado;       // estado (texto en BD)
    private String motivoCancelacion;   // motivo_cancelacion
    private String fechaCancelacion;    // fecha_cancelacion

    // COMPOSICION: la Reserva contiene y es duena de sus Observaciones.
    private final List<Observacion> observaciones = new ArrayList<>();

    // Auxiliares de presentacion (vienen de los JOIN del DAO)
    private String nombreResidente;     // nombre_residente
    private String nombreEspacio;       // nombre_espacio

    public Reserva() {
    }

    // ==================================================================
    // Comportamiento (segun el diagrama de clases)
    // ==================================================================

    public void cambiarEstado(EstadoReserva nuevoEstado) {
        this.estado = nuevoEstado;
    }

    /** Materializa la composicion: la observacion pasa a pertenecer a esta reserva. */
    public void agregarObservacion(Observacion obs) {
        if (obs != null) {
            obs.setIdReserva(this.id);
            this.observaciones.add(obs);
        }
    }

    /** Se superponen si son del mismo espacio, misma fecha y sus horarios chocan. */
    public boolean seSuperponeCon(Reserva otra) {
        if (otra == null) return false;
        if (this.idEspacio != otra.idEspacio) return false;
        if (this.fechaReserva == null || !this.fechaReserva.equals(otra.fechaReserva)) return false;
        if (this.horaInicio == null || this.horaFin == null
                || otra.horaInicio == null || otra.horaFin == null) return false;
        // Horas en formato 24h con relleno de ceros -> comparables como texto.
        return this.horaInicio.compareTo(otra.horaFin) < 0
                && otra.horaInicio.compareTo(this.horaFin) < 0;
    }

    // costoAplicado como valor decimal (segun el diagrama), sobre los centavos del DTO.
    public double getCostoAplicado() {
        return costoAplicadoCentavos / 100.0;
    }

    public void setCostoAplicado(double costo) {
        this.costoAplicadoCentavos = (int) Math.round(costo * 100);
    }

    // ==================================================================
    // Mapeo con los DTO existentes
    // ==================================================================

    public static Reserva desdeDTO(ReservaDTO dto) {
        Reserva r = new Reserva();
        r.id = dto.getIdReserva();
        r.idResidente = dto.getIdUsuario();
        r.idEspacio = dto.getIdEspacioComun();
        r.fechaCreacion = dto.getFechaCreacion();
        r.fechaReserva = dto.getFechaReserva();
        r.horaInicio = dto.getHoraInicio();
        r.horaFin = dto.getHoraFin();
        r.costoAplicadoCentavos = dto.getCostoAplicadoCentavos();
        r.estado = parseEstado(dto.getEstado());
        r.motivoCancelacion = dto.getMotivoCancelacion();
        r.fechaCancelacion = dto.getFechaCancelacion();
        r.nombreResidente = dto.getNombreResidente();
        r.nombreEspacio = dto.getNombreEspacio();
        return r;
    }

    public ReservaDTO aDTO() {
        ReservaDTO dto = new ReservaDTO();
        dto.setIdReserva(id);
        dto.setIdUsuario(idResidente);
        dto.setIdEspacioComun(idEspacio);
        dto.setFechaCreacion(fechaCreacion);
        dto.setFechaReserva(fechaReserva);
        dto.setHoraInicio(horaInicio);
        dto.setHoraFin(horaFin);
        dto.setCostoAplicadoCentavos(costoAplicadoCentavos);
        dto.setEstado(estado != null ? estado.name() : null);
        dto.setMotivoCancelacion(motivoCancelacion);
        dto.setFechaCancelacion(fechaCancelacion);
        dto.setNombreResidente(nombreResidente);
        dto.setNombreEspacio(nombreEspacio);
        return dto;
    }

    /** Convierte las observaciones contenidas a DTOs para persistirlas con el DAO. */
    public List<ObservacionReservaDTO> observacionesADTO() {
        List<ObservacionReservaDTO> lista = new ArrayList<>();
        for (Observacion o : observaciones) {
            lista.add(o.aDTO());
        }
        return lista;
    }

    /**
     * Convierte el texto de estado de la BD al enum.
     * La BD usa el CHECK ('ACTIVA','CANCELADA','FINALIZADA'), que coincide
     * con EstadoReserva. Ante un valor desconocido se asume ACTIVA.
     */
    private static EstadoReserva parseEstado(String estado) {
        if (estado == null) return null;
        switch (estado.trim().toUpperCase()) {
            case "CANCELADA":  return EstadoReserva.CANCELADA;
            case "FINALIZADA": return EstadoReserva.FINALIZADA;
            case "ACTIVA":
            default:           return EstadoReserva.ACTIVA;
        }
    }

    // ==================================================================
    // getters / setters
    // ==================================================================

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdResidente() { return idResidente; }
    public void setIdResidente(int idResidente) { this.idResidente = idResidente; }

    public int getIdEspacio() { return idEspacio; }
    public void setIdEspacio(int idEspacio) { this.idEspacio = idEspacio; }

    public String getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getFechaReserva() { return fechaReserva; }
    public void setFechaReserva(String fechaReserva) { this.fechaReserva = fechaReserva; }

    public String getHoraInicio() { return horaInicio; }
    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }

    public String getHoraFin() { return horaFin; }
    public void setHoraFin(String horaFin) { this.horaFin = horaFin; }

    public int getCostoAplicadoCentavos() { return costoAplicadoCentavos; }
    public void setCostoAplicadoCentavos(int c) { this.costoAplicadoCentavos = c; }

    public EstadoReserva getEstado() { return estado; }
    public void setEstado(EstadoReserva estado) { this.estado = estado; }

    public String getMotivoCancelacion() { return motivoCancelacion; }
    public void setMotivoCancelacion(String m) { this.motivoCancelacion = m; }

    public String getFechaCancelacion() { return fechaCancelacion; }
    public void setFechaCancelacion(String f) { this.fechaCancelacion = f; }

    /** Solo lectura: para agregar usa agregarObservacion(...) y respeta la composicion. */
    public List<Observacion> getObservaciones() {
        return Collections.unmodifiableList(observaciones);
    }

    public String getNombreResidente() { return nombreResidente; }
    public void setNombreResidente(String n) { this.nombreResidente = n; }

    public String getNombreEspacio() { return nombreEspacio; }
    public void setNombreEspacio(String n) { this.nombreEspacio = n; }
    
}