package fis.dsw.sgc.finanzas.model;

public class PagoTransferencia implements ITipoPago {
    private String comprobanteTransferencia;

    public PagoTransferencia(String comprobanteTransferencia) {
        this.comprobanteTransferencia = comprobanteTransferencia;
    }

    @Override
    public boolean procesarTransaccion(Pago pago, Deuda deuda) {
        if (comprobanteTransferencia == null || comprobanteTransferencia.trim().isEmpty()) {
            throw new IllegalArgumentException("El comprobante de transferencia es obligatorio.");
        }
        // Aquí iría la lógica de verificación con el banco, pero por ahora es exitosa
        return true;
    }
}