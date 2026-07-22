package fis.dsw.sgc.check_in.dto;

public class RegistroEntradaDTO {
    private int idEntrada;
    private String hora;
    private String fecha;
    private String persona;
    private String cedula;
    private String tipoEntrada;
    private String destino;
    private String observaciones;
    private String parqueadero;

    public RegistroEntradaDTO() {}

    public RegistroEntradaDTO(int idEntrada, String hora, String fecha, String persona, String cedula,
                              String tipoEntrada, String destino, String observaciones, String parqueadero) {
        this.idEntrada = idEntrada;
        this.hora = hora;
        this.fecha = fecha;
        this.persona = persona;
        this.cedula = cedula;
        this.tipoEntrada = tipoEntrada;
        this.destino = destino;
        this.observaciones = observaciones;
        this.parqueadero = parqueadero;
    }

    public int getIdEntrada() {
        return idEntrada;
    }

    public void setIdEntrada(int idEntrada) {
        this.idEntrada = idEntrada;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getTipoEntrada() {
        return tipoEntrada;
    }

    public void setTipoEntrada(String tipoEntrada) {
        this.tipoEntrada = tipoEntrada;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getParqueadero() {
        return parqueadero;
    }

    public void setParqueadero(String parqueadero) {
        this.parqueadero = parqueadero;
    }
}
