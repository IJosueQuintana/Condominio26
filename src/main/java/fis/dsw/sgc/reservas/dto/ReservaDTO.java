package fis.dsw.sgc.reservas.dto;

public class ReservaDTO {

    private int idReserva;
    private int idUsuario;
    private int idEspacioComun;
    private String fechaCreacion;
    private String fechaReserva;
    private String horaInicio;
    private String horaFin;
    private int costoAplicadoCentavos;
    private String estado;
    private String motivoCancelacion;
    private String fechaCancelacion;

    // Campos auxiliares para presentación (vienen de JOINs)
    private String nombreResidente;
    private String nombreEspacio;

    public ReservaDTO() {
    }

    public ReservaDTO(int idReserva, int idUsuario, int idEspacioComun,
                      String fechaReserva, String horaInicio, String horaFin,
                      int costoAplicadoCentavos, String estado) {
        this.idReserva = idReserva;
        this.idUsuario = idUsuario;
        this.idEspacioComun = idEspacioComun;
        this.fechaReserva = fechaReserva;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.costoAplicadoCentavos = costoAplicadoCentavos;
        this.estado = estado;
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdEspacioComun() {
        return idEspacioComun;
    }

    public void setIdEspacioComun(int idEspacioComun) {
        this.idEspacioComun = idEspacioComun;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(String fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public int getCostoAplicadoCentavos() {
        return costoAplicadoCentavos;
    }

    public void setCostoAplicadoCentavos(int costoAplicadoCentavos) {
        this.costoAplicadoCentavos = costoAplicadoCentavos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMotivoCancelacion() {
        return motivoCancelacion;
    }

    public void setMotivoCancelacion(String motivoCancelacion) {
        this.motivoCancelacion = motivoCancelacion;
    }

    public String getFechaCancelacion() {
        return fechaCancelacion;
    }

    public void setFechaCancelacion(String fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion;
    }

    public String getNombreResidente() {
        return nombreResidente;
    }

    public void setNombreResidente(String nombreResidente) {
        this.nombreResidente = nombreResidente;
    }

    public String getNombreEspacio() {
        return nombreEspacio;
    }

    public void setNombreEspacio(String nombreEspacio) {
        this.nombreEspacio = nombreEspacio;
    }
}
