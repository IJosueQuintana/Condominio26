package fis.dsw.sgc.finanzas.dto;

import java.time.LocalDate;

public class GastoDTO {

    // Estos atributos deben coincidir en nombre con lo que configures en el PropertyValueFactory de las columnas
    private String motivo;
    private String descripcion;
    private LocalDate fecha;
    private double valor;

    public GastoDTO(String motivo, String descripcion, LocalDate fecha, double valor) {
        this.motivo = motivo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.valor = valor;
    }

    // Getters y Setters requeridos por JavaFX para renderizar las celdas de la tabla

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}