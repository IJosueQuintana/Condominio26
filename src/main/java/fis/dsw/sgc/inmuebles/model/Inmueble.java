package fis.dsw.sgc.inmuebles.model;

public class Inmueble {
    private int idInmueble;
    private Integer idEdificio;
    private int idTipoInmueble;
    private String codigo;
    private Integer piso;
    private String numero;
    private Double areaM2;
    private Integer numeroHabitaciones;
    private Integer numeroBanos;
    private String descripcion;
    private boolean disponibleAlquiler;
    private boolean disponibleVenta;
    private String estado;

    public int getIdInmueble() { return idInmueble; }
    public void setIdInmueble(int idInmueble) { this.idInmueble = idInmueble; }

    public Integer getIdEdificio() { return idEdificio; }
    public void setIdEdificio(Integer idEdificio) { this.idEdificio = idEdificio; }

    public int getIdTipoInmueble() { return idTipoInmueble; }
    public void setIdTipoInmueble(int idTipoInmueble) { this.idTipoInmueble = idTipoInmueble; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public Integer getPiso() { return piso; }
    public void setPiso(Integer piso) { this.piso = piso; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public Double getAreaM2() { return areaM2; }
    public void setAreaM2(Double areaM2) { this.areaM2 = areaM2; }

    public Integer getNumeroHabitaciones() { return numeroHabitaciones; }
    public void setNumeroHabitaciones(Integer numeroHabitaciones) { this.numeroHabitaciones = numeroHabitaciones; }

    public Integer getNumeroBanos() { return numeroBanos; }
    public void setNumeroBanos(Integer numeroBanos) { this.numeroBanos = numeroBanos; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public boolean isDisponibleAlquiler() { return disponibleAlquiler; }
    public void setDisponibleAlquiler(boolean disponibleAlquiler) { this.disponibleAlquiler = disponibleAlquiler; }

    public boolean isDisponibleVenta() { return disponibleVenta; }
    public void setDisponibleVenta(boolean disponibleVenta) { this.disponibleVenta = disponibleVenta; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}