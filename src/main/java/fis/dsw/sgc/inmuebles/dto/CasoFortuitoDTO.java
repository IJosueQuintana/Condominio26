package fis.dsw.sgc.inmuebles.dto;

public class CasoFortuitoDTO {
    private final int idCaso;
    private final String descripcion;
    private final String fecha;
    private final String estado;

    public CasoFortuitoDTO(int idCaso, String descripcion, String fecha, String estado) {
        this.idCaso = idCaso;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.estado = estado;
    }

    public int getIdCaso() { return idCaso; }
    public String getDescripcion() { return descripcion; }
    public String getFecha() { return fecha; }
    public String getEstado() { return estado; }
}