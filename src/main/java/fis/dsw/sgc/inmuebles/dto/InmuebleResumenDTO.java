package fis.dsw.sgc.inmuebles.dto;

public class InmuebleResumenDTO {
    private final int idInmueble;
    private final String codigo;
    private final String direccion;
    private final String tipo;
    private final String propietario;
    private final String estado;

    public InmuebleResumenDTO(int idInmueble, String codigo, String direccion,
                               String tipo, String propietario, String estado) {
        this.idInmueble = idInmueble;
        this.codigo = codigo;
        this.direccion = direccion;
        this.tipo = tipo;
        this.propietario = propietario;
        this.estado = estado;
    }

    public int getIdInmueble() { return idInmueble; }
    public String getCodigo() { return codigo; }
    public String getDireccion() { return direccion; }
    public String getTipo() { return tipo; }
    public String getPropietario() { return propietario; }
    public String getEstado() { return estado; }
}