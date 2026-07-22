package fis.dsw.sgc.administracion.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Cuenta {
    private UUID idCuenta;
    private String contrasena;
    private EstadoCuenta estado;
    private Date fechaCreacion;
    private Date fechaModificacion;

    private List<Rol> roles = new ArrayList<>();

    public UUID getIdCuenta() { return idCuenta; }
    public void setIdCuenta(UUID idCuenta) { this.idCuenta = idCuenta; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public EstadoCuenta getEstado() { return estado; }
    public void setEstado(EstadoCuenta estado) { this.estado = estado; }

    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Date getFechaModificacion() { return fechaModificacion; }
    public void setFechaModificacion(Date fechaModificacion) { this.fechaModificacion = fechaModificacion; }

    public List<Rol> getRoles() { return roles; }
    public void setRoles(List<Rol> roles) { this.roles = roles; }

    public void activar() {
        this.estado = EstadoCuenta.ACTIVA;
    }

    public void desactivar() {
        this.estado = EstadoCuenta.DESACTIVADA;
    }
}