package fis.dsw.sgc.finanzas.model;

public class EntidadBancaria {

    private String nombre;
    private String numeroCuenta;
    private String cedulaTitular;
    private String tipo;
    private String emailTitular;

    public EntidadBancaria() {
    }

    public EntidadBancaria(String nombre, String numeroCuenta, String cedulaTitular,
                            String tipo, String emailTitular) {
        this.nombre = nombre;
        this.numeroCuenta = numeroCuenta;
        this.cedulaTitular = cedulaTitular;
        this.tipo = tipo;
        this.emailTitular = emailTitular;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getCedulaTitular() {
        return cedulaTitular;
    }

    public void setCedulaTitular(String cedulaTitular) {
        this.cedulaTitular = cedulaTitular;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEmailTitular() {
        return emailTitular;
    }

    public void setEmailTitular(String emailTitular) {
        this.emailTitular = emailTitular;
    }
}
