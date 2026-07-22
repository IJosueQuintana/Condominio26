package fis.dsw.sgc.finanzas.dto;

import java.time.LocalDate;

public class DetalleGastoDTO {
    public String motivo; // AGUA, LUZ, SUELDO, etc.
    public String descripcion;
    public Double valor;
    public LocalDate fecha; // NUEVO CAMPO

    public DetalleGastoDTO(String motivo, String descripcion, Double valor, LocalDate fecha) {
        this.motivo = motivo;
        this.descripcion = descripcion;
        this.valor = valor;
        this.fecha = fecha;
    }
}