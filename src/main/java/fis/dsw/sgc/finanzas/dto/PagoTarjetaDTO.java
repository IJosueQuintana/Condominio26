package fis.dsw.sgc.finanzas.dto;

import java.time.LocalDate;

public class PagoTarjetaDTO {
    private Integer idDeuda;
    private String numeroTarjeta;
    private LocalDate fechaVencimiento;
    private String nombreTitular;
    private Integer ccv;

    public PagoTarjetaDTO(Integer idDeuda, String numeroTarjeta, LocalDate fechaVencimiento, String nombreTitular, Integer ccv) {
        this.idDeuda = idDeuda;
        this.numeroTarjeta = numeroTarjeta;
        this.fechaVencimiento = fechaVencimiento;
        this.nombreTitular = nombreTitular;
        this.ccv = ccv;
    }

    // Getters
    public Integer getIdDeuda() { return idDeuda; }
    public String getNumeroTarjeta() { return numeroTarjeta; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public String getNombreTitular() { return nombreTitular; }
    public Integer getCcv() { return ccv; }
}