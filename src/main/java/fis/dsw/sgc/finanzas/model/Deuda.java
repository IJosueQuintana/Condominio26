package fis.dsw.sgc.finanzas.model;

import java.time.LocalDate;

public class Deuda {
    private int idDeuda;
    private int idUsuario;
    private Integer idInmueble;
    private Integer idReserva;

    private ITipoDeuda tipoDeuda;
    private IEstadoDeuda estado;

    private String descripcion;
    private double valorBase;
    private double mora;
    private double total;
    private double saldo;
    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;
    private String observaciones;

    public Deuda() {
        this.fechaEmision = LocalDate.now();
    }

    // --- MÉTODOS DE COMPORTAMIENTO (RICH DOMAIN MODEL) ---
    public void calcularValorTotal() {
        this.valorBase = tipoDeuda.calcularValor(this.valorBase);
        this.total = this.valorBase + this.mora;
        this.saldo = this.total;
    }

    public void procesarPago(double montoPagado) {
        estado.procesarPago(this);

        if (montoPagado <= 0) {
            throw new IllegalArgumentException("El monto a pagar debe ser mayor a 0.");
        }

        this.saldo -= montoPagado;
        if (this.saldo < 0) {
            this.saldo = 0;
        }
    }

    public void aplicarMora() {
        estado.aplicarMora(this);
        this.mora = this.valorBase * 0.15; // 15% según tus reglas previas
        this.total = this.valorBase + this.mora;
        this.saldo += this.mora;
    }

    public void anular() {
        estado.anular(this);
        this.saldo = 0;
    }

    public void modificarFechaVencimiento(LocalDate nuevaFecha) {
        LocalDate fechaActual = LocalDate.now();
        if (nuevaFecha.isAfter(fechaActual) && nuevaFecha.isAfter(this.fechaVencimiento)) {
            this.fechaVencimiento = nuevaFecha;
        } else {
            throw new IllegalArgumentException("La nueva fecha debe ser mayor a la fecha actual y a la fecha de pago actual.");
        }
    }

    // --- GETTERS Y SETTERS ---
    public int getIdDeuda() { return idDeuda; }
    public void setIdDeuda(int idDeuda) { this.idDeuda = idDeuda; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public IEstadoDeuda getEstado() { return this.estado; }
    public void setEstado(IEstadoDeuda estado) { this.estado = estado; }
    public ITipoDeuda getTipoDeuda() { return this.tipoDeuda; }
    public void setTipoDeuda(ITipoDeuda tipoDeuda) { this.tipoDeuda = tipoDeuda; }

    public LocalDate getFechaVencimiento() { return this.fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
    public double getValorBase() { return valorBase; }
    public void setValorBase(double valorBase) { this.valorBase = valorBase; }
    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}