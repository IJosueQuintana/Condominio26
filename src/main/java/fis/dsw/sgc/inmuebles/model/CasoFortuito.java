package fis.dsw.sgc.inmuebles.model;

public class CasoFortuito {
    private int idCaso;
    private int idInmueble;
    private String descripcion;
    private String fecha;
    private String estado;

    public int getIdCaso() { return idCaso; }
    public void setIdCaso(int idCaso) { this.idCaso = idCaso; }

    public int getIdInmueble() { return idInmueble; }
    public void setIdInmueble(int idInmueble) { this.idInmueble = idInmueble; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}