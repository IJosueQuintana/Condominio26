package fis.dsw.sgc.administracion.model;

public class Permiso {
    private int idPermiso;
    private String nombre;
    private String recurso;

    public int getIdPermiso() { return idPermiso; }
    public void setIdPermiso(int idPermiso) { this.idPermiso = idPermiso; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getRecurso() { return recurso; }
    public void setRecurso(String recurso) { this.recurso = recurso; }
}