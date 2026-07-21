package fis.dsw.sgc.finanzas.model;

public class PagoTarjeta implements ITipoPago {
    private String numeroTarjeta;
    private String codigoTransaccion;

    public PagoTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    @Override
    public boolean procesarTransaccion(Pago pago, Deuda deuda) {
        // Simulamos la llamada a la pasarela de pagos (DataFast, Stripe, etc.)
        this.codigoTransaccion = "TXN-" + System.currentTimeMillis();
        return true;
    }
}