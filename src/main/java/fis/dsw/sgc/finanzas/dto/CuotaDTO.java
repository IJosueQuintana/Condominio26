package fis.dsw.sgc.finanzas.dto;

// DTO de Cuota: lo que el Service devolvería al Controller al solicitar el pago en cuotas de una deuda.
public class CuotaDTO {

    private String cuota;
    private String fechaMaximaPago;
    private String valor;

    public CuotaDTO() {
    }

    public CuotaDTO(String cuota, String fechaMaximaPago, String valor) {
        this.cuota = cuota;
        this.fechaMaximaPago = fechaMaximaPago;
        this.valor = valor;
    }

    public String getCuota() {
        return cuota;
    }

    public void setCuota(String cuota) {
        this.cuota = cuota;
    }

    public String getFechaMaximaPago() {
        return fechaMaximaPago;
    }

    public void setFechaMaximaPago(String fechaMaximaPago) {
        this.fechaMaximaPago = fechaMaximaPago;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
