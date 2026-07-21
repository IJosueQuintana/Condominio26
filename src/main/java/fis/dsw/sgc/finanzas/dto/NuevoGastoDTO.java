package fis.dsw.sgc.finanzas.dto;

import java.time.LocalDate;

public class NuevoGastoDTO {
    private LocalDate fechaPago;
    private Double valorPagado;
    private String motivo;
    private String descripcion;

    public NuevoGastoDTO(LocalDate fechaPago, Double valorPagado, String motivo, String descripcion) {
        this.fechaPago = fechaPago;
        this.valorPagado = valorPagado;
        this.motivo = motivo;
        this.descripcion = descripcion;
    }

    // Getters
    public LocalDate getFechaPago() { return fechaPago; }
    public Double getValorPagado() { return valorPagado; }
    public String getMotivo() { return motivo; }
    public String getDescripcion() { return descripcion; }
}