package fis.dsw.sgc.finanzas.service;

import fis.dsw.sgc.finanzas.model.Deuda;
import fis.dsw.sgc.finanzas.model.Pago;

public interface IPagoFactory {
    Pago crearPago(String metodo, Deuda deuda, double monto, String datoExtra);
}