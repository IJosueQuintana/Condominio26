package fis.dsw.sgc.finanzas.dao;

import fis.dsw.sgc.finanzas.model.Pago;

public interface IPagoDAO {
    // Métodos de la base de datos para Pago
    void guardar(Pago pago);
    Pago buscarPorId(int idPago);
}