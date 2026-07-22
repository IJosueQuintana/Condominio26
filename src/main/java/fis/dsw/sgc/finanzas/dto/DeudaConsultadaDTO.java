package fis.dsw.sgc.finanzas.dto;

import java.time.LocalDate;

public class DeudaConsultadaDTO {
    private Integer idDeuda;
    private String motivo;
    private Double saldoPendiente;
    private LocalDate fechaVencimiento;
    private String estadoActual;

    public DeudaConsultadaDTO(Integer idDeuda, String motivo, Double saldoPendiente, LocalDate fechaVencimiento, String estadoActual) {
        this.idDeuda = idDeuda;
        this.motivo = motivo;
        this.saldoPendiente = saldoPendiente;
        this.fechaVencimiento = fechaVencimiento;
        this.estadoActual = estadoActual;
    }

    // Getters
    public Integer getIdDeuda() { return idDeuda; }
    public String getMotivo() { return motivo; }
    public Double getSaldoPendiente() { return saldoPendiente; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public String getEstadoActual() { return estadoActual; }
}