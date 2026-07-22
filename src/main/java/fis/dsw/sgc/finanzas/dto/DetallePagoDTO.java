package fis.dsw.sgc.finanzas.dto;

public class DetallePagoDTO {
    public String cedula;
    public String motivo; // ALICUOTA, MULTA, RESERVA
    public Double valor;

    public DetallePagoDTO(String cedula, String motivo, Double valor) {
        this.cedula = cedula; this.motivo = motivo; this.valor = valor;
    }
}