package fis.dsw.sgc.check_in.model;

public class AlertaSeguridad {
    private int idAlerta;
    private Integer idRegistroEntrada;
    private int idUsuarioReporta;
    private TipoAlerta tipo;
    private String descripcion;
    private PrioridadAlerta nivel;
    private String fechaCreacion;
    private EstadoAlerta estado;

    public AlertaSeguridad() {
        this.estado = EstadoAlerta.ABIERTA;
        this.nivel = PrioridadAlerta.MEDIA;
    }

    public AlertaSeguridad(int idAlerta, Integer idRegistroEntrada, int idUsuarioReporta, TipoAlerta tipo,
                           String descripcion, PrioridadAlerta nivel, String fechaCreacion, EstadoAlerta estado) {
        this.idAlerta = idAlerta;
        this.idRegistroEntrada = idRegistroEntrada;
        this.idUsuarioReporta = idUsuarioReporta;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.nivel = nivel;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
    }

    public void cambiarEstado(EstadoAlerta nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public int getIdAlerta() {
        return idAlerta;
    }

    public void setIdAlerta(int idAlerta) {
        this.idAlerta = idAlerta;
    }

    public Integer getIdRegistroEntrada() {
        return idRegistroEntrada;
    }

    public void setIdRegistroEntrada(Integer idRegistroEntrada) {
        this.idRegistroEntrada = idRegistroEntrada;
    }

    public int getIdUsuarioReporta() {
        return idUsuarioReporta;
    }

    public void setIdUsuarioReporta(int idUsuarioReporta) {
        this.idUsuarioReporta = idUsuarioReporta;
    }

    public TipoAlerta getTipo() {
        return tipo;
    }

    public void setTipo(TipoAlerta tipo) {
        this.tipo = tipo;
        if (tipo != null && this.nivel == null) {
            this.nivel = tipo.obtenerPrioridadPorDefecto();
        }
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public PrioridadAlerta getNivel() {
        return nivel;
    }

    public void setNivel(PrioridadAlerta nivel) {
        this.nivel = nivel;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public EstadoAlerta getEstado() {
        return estado;
    }

    public void setEstado(EstadoAlerta estado) {
        this.estado = estado;
    }
}
