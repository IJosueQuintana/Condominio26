package fis.dsw.sgc.check_in.model;

public class Visitante {
    private int idVisitante;
    private String nombre;
    private String identificacion;
    private String telefono;
    private String placaVehiculo;

    public Visitante() {}

    public Visitante(int idVisitante, String nombre, String identificacion, String telefono, String placaVehiculo) {
        this.idVisitante = idVisitante;
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.telefono = telefono;
        this.placaVehiculo = placaVehiculo;
    }

    public void actualizarDatos(String nombre, String telefono, String placaVehiculo) {
        if (nombre != null && !nombre.isBlank()) this.nombre = nombre;
        if (telefono != null && !telefono.isBlank()) this.telefono = telefono;
        if (placaVehiculo != null) this.placaVehiculo = placaVehiculo;
    }

    public int getIdVisitante() {
        return idVisitante;
    }

    public void setIdVisitante(int idVisitante) {
        this.idVisitante = idVisitante;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPlacaVehiculo() {
        return placaVehiculo;
    }

    public void setPlacaVehiculo(String placaVehiculo) {
        this.placaVehiculo = placaVehiculo;
    }
}
