package fis.dsw.sgc.administracion.model;

import java.util.ArrayList;
import java.util.List;

public class Rol {
    private int idRol;
    private NombreRol nombre;
    private String descripcion;

    private List<Permiso> permisos = new ArrayList<>();

    public int getIdRol() { return idRol; }
    public void setIdRol(int idRol) { this.idRol = idRol; }

    public NombreRol getNombre() { return nombre; }
    public void setNombre(NombreRol nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public List<Permiso> getPermisos() { return permisos; }
    public void setPermisos(List<Permiso> permisos) { this.permisos = permisos; }

    public List<Permiso> obtenerPermisos() {
        return permisos;
    }
}