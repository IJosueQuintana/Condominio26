package fis.dsw.sgc.finanzas.dto;

public class DetalleGastoDTO {
    public String motivo; // AGUA, LUZ, SUELDO, etc.
    public String descripcion;
    public Double valor;

    public DetalleGastoDTO(String motivo, String descripcion, Double valor) {
        this.motivo = motivo; this.descripcion = descripcion; this.valor = valor;
    }
}
