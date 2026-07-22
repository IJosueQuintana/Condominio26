package fis.dsw.sgc.finanzas.model;

import java.time.LocalDateTime;

public class Pago {
    private int idPago;
    private LocalDateTime fechaPago;
    private double valorPagado;

    // Relaciones
    private Deuda deuda; // Relación 1:1 conceptual
    private ITipoPago modalidad; // El Strategy

    private String estado; // REGISTRADO, VALIDADO, RECHAZADO, ANULADO

    public Pago() {
        this.fechaPago = LocalDateTime.now();
        this.estado = "REGISTRADO";
    }

    // --- COMPORTAMIENTO ---
    public boolean ejecutarCobro() {
        if (modalidad == null) {
            throw new IllegalStateException("No se ha definido una modalidad de pago.");
        }
        return modalidad.procesarTransaccion(this, this.deuda);
    }

    // --- GETTERS & SETTERS ---
    public int getIdPago() { return idPago; }
    public void setIdPago(int idPago) { this.idPago = idPago; }
    public LocalDateTime getFechaPago() { return fechaPago; }
    public double getValorPagado() { return valorPagado; }
    public void setValorPagado(double valorPagado) { this.valorPagado = valorPagado; }
    public Deuda getDeuda() { return deuda; }
    public void setDeuda(Deuda deuda) { this.deuda = deuda; }
    public ITipoPago getModalidad() { return modalidad; }
    public void setModalidad(ITipoPago modalidad) { this.modalidad = modalidad; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}