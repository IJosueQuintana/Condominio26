package fis.dsw.sgc.reservas.dto;

public class ObservacionReservaDTO {

    private int idObservacion;
    private int idReserva;
    private int idUsuario;
    private String texto;
    private String fechaHora;

    // Campo auxiliar para presentación
    private String nombreUsuario;

    public ObservacionReservaDTO() {
    }

    public ObservacionReservaDTO(int idReserva, int idUsuario, String texto) {
        this.idReserva = idReserva;
        this.idUsuario = idUsuario;
        this.texto = texto;
    }

    public int getIdObservacion() {
        return idObservacion;
    }

    public void setIdObservacion(int idObservacion) {
        this.idObservacion = idObservacion;
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

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}
