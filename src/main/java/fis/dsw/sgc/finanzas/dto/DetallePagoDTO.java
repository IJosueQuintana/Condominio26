package fis.dsw.sgc.finanzas.dto;

import java.time.LocalDate;

public class DetallePagoDTO {
    public String cedula;
    public String motivo; // ALICUOTA, MULTA, RESERVA
    public Double valor;
    public LocalDate fecha; // NUEVO CAMPO

    public DetallePagoDTO(String cedula, String motivo, Double valor, LocalDate fecha) {
        this.cedula = cedula;
        this.motivo = motivo;
        this.valor = valor;
        this.fecha = fecha;
    }
}