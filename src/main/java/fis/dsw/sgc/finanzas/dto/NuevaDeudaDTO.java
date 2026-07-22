package fis.dsw.sgc.finanzas.dto;

import java.time.LocalDate;

public class NuevaDeudaDTO {
    private String cedulaResidente;
    private String motivoDeuda; // ALICUOTA, MULTA, RESERVA
    private LocalDate fechaMaximaPago;
    private String descripcion;
    private Double valor;

    // Constructores, Getters y Setters
    public NuevaDeudaDTO(String cedulaResidente, String motivoDeuda, LocalDate fechaMaximaPago, String descripcion, Double valor) {
        this.cedulaResidente = cedulaResidente;
        this.motivoDeuda = motivoDeuda;
        this.fechaMaximaPago = fechaMaximaPago;
        this.descripcion = descripcion;
        this.valor = valor;
    }

    public String getCedulaResidente() { return cedulaResidente; }
    public String getMotivoDeuda() { return motivoDeuda; }
    public LocalDate getFechaMaximaPago() { return fechaMaximaPago; }
    public String getDescripcion() { return descripcion; }
    public Double getValor() { return valor; }
}