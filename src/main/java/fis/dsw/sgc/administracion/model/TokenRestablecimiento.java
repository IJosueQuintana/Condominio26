package fis.dsw.sgc.administracion.model;

import java.util.Date;

public class TokenRestablecimiento {
    private int idToken;
    private String codigo;
    private Date fechaExpiracion;
    private Integer intentos;

    public int getIdToken() { return idToken; }
    public void setIdToken(int idToken) { this.idToken = idToken; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public Date getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(Date fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }

    public Integer getIntentos() { return intentos; }
    public void setIntentos(Integer intentos) { this.intentos = intentos; }

    public boolean estaExpirado() {
        return fechaExpiracion != null && fechaExpiracion.before(new Date());
    }

    public boolean esValido() {
        return !estaExpirado() && intentos != null && intentos < 5;
    }
}