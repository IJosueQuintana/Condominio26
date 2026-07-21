package fis.dsw.sgc.finanzas.model;

public class PagoEfectivo implements ITipoPago {
    @Override
    public boolean procesarTransaccion(Pago pago, Deuda deuda) {
        // El pago en efectivo es manual, asume que si el cajero lo aprueba, es exitoso.
        return true;
    }
}