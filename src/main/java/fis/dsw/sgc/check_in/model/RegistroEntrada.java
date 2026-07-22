package fis.dsw.sgc.check_in.model;

public abstract class RegistroEntrada {
    protected int idEntrada;
    protected Integer idVisita;
    protected String nombres;
    protected String apellidos;
    protected String cedula;
    protected String fechaLlegada;
    protected String horaLlegada;
    protected String informacionAdicional;
    protected String observaciones;
    protected String tipoEntrada;
    protected String placaVehiculo;

    public RegistroEntrada() {}

    public RegistroEntrada(int idEntrada, String nombres, String apellidos, String cedula,
                           String fechaLlegada, String horaLlegada, String tipoEntrada) {
        this.idEntrada = idEntrada;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.cedula = cedula;
        this.fechaLlegada = fechaLlegada;
        this.horaLlegada = horaLlegada;
        this.tipoEntrada = tipoEntrada;
    }

    public abstract String obtenerDetalleEntrada();

    public int getIdEntrada() {
        return idEntrada;
    }

    public void setIdEntrada(int idEntrada) {
        this.idEntrada = idEntrada;
    }

    public Integer getIdVisita() {
        return idVisita;
    }

    public void setIdVisita(Integer idVisita) {
        this.idVisita = idVisita;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getFechaLlegada() {
        return fechaLlegada;
    }

    public void setFechaLlegada(String fechaLlegada) {
        this.fechaLlegada = fechaLlegada;
    }

    public String getHoraLlegada() {
        return horaLlegada;
    }

    public void setHoraLlegada(String horaLlegada) {
        this.horaLlegada = horaLlegada;
    }

    public String getInformacionAdicional() {
        return informacionAdicional;
    }

    public void setInformacionAdicional(String informacionAdicional) {
        this.informacionAdicional = informacionAdicional;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getTipoEntrada() {
        return tipoEntrada;
    }

    public void setTipoEntrada(String tipoEntrada) {
        this.tipoEntrada = tipoEntrada;
    }

    public String getPlacaVehiculo() {
        return placaVehiculo;
    }

    public void setPlacaVehiculo(String placaVehiculo) {
        this.placaVehiculo = placaVehiculo;
    }
}
