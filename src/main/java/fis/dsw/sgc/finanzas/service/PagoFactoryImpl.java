package fis.dsw.sgc.finanzas.service;

import fis.dsw.sgc.finanzas.model.*;

public class PagoFactoryImpl implements IPagoFactory {

    @Override
    public Pago crearPago(String metodo, Deuda deuda, double monto, String datoExtra) {
        Pago nuevoPago = new Pago();
        nuevoPago.setDeuda(deuda);
        nuevoPago.setValorPagado(monto);

        switch (metodo.toUpperCase()) {
            case "EFECTIVO":
                nuevoPago.setModalidad(new PagoEfectivo());
                break;
            case "TRANSFERENCIA":
                // datoExtra sería el código del comprobante
                nuevoPago.setModalidad(new PagoTransferencia(datoExtra));
                break;
            case "TARJETA":
                // datoExtra sería el número de tarjeta (simplificado)
                nuevoPago.setModalidad(new PagoTarjeta(datoExtra));
                break;
            default:
                throw new IllegalArgumentException("Método de pago no soportado: " + metodo);
        }

        return nuevoPago;
    }
}