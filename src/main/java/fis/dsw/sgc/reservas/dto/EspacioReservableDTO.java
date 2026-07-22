package fis.dsw.sgc.reservas.dto;

/**
 * DTO de solo lectura con la informacion de un espacio comun que el modulo
 * de Reservas necesita para operar (nombre, tarifa, horario, capacidad).
 *
 * La entidad EspacioComun pertenece al modulo de Inmuebles (GRC) y todavia
 * no esta implementada como clase Java. Mientras tanto, este DTO permite
 * que Reservas consulte la tabla espacio_comun directamente para reflejar
 * datos reales de la BD en la interfaz.
 */
public class EspacioReservableDTO {

    private int idEspacioComun;      // id_espacio_comun
    private int idInmueble;          // id_inmueble
    private String nombre;           // nombre
    private int capacidadMaxima;     // capacidad_maxima
    private String horaApertura;     // hora_apertura ("HH:MM")
    private String horaCierre;       // hora_cierre   ("HH:MM")
    private int costoReservaCentavos;// costo_reserva_centavos
    private boolean requiereAprobacion; // requiere_aprobacion (0/1)
    private String reglamentoUso;    // reglamento_uso
    private String estado;           // estado

    public EspacioReservableDTO() {
    }

    public int getIdEspacioComun() { return idEspacioComun; }
    public void setIdEspacioComun(int idEspacioComun) { this.idEspacioComun = idEspacioComun; }

    public int getIdInmueble() { return idInmueble; }
    public void setIdInmueble(int idInmueble) { this.idInmueble = idInmueble; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getCapacidadMaxima() { return capacidadMaxima; }
    public void setCapacidadMaxima(int capacidadMaxima) { this.capacidadMaxima = capacidadMaxima; }

    public String getHoraApertura() { return horaApertura; }
    public void setHoraApertura(String horaApertura) { this.horaApertura = horaApertura; }

    public String getHoraCierre() { return horaCierre; }
    public void setHoraCierre(String horaCierre) { this.horaCierre = horaCierre; }

    public int getCostoReservaCentavos() { return costoReservaCentavos; }
    public void setCostoReservaCentavos(int costoReservaCentavos) { this.costoReservaCentavos = costoReservaCentavos; }

    public boolean isRequiereAprobacion() { return requiereAprobacion; }
    public void setRequiereAprobacion(boolean requiereAprobacion) { this.requiereAprobacion = requiereAprobacion; }

    public String getReglamentoUso() { return reglamentoUso; }
    public void setReglamentoUso(String reglamentoUso) { this.reglamentoUso = reglamentoUso; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    /** Tarifa en unidades monetarias (dolares), derivada de los centavos. */
    public double getCostoReserva() {
        return costoReservaCentavos / 100.0;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
