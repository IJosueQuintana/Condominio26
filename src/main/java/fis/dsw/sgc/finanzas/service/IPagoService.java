package fis.dsw.sgc.finanzas.service;

import fis.dsw.sgc.finanzas.dto.PagoTarjetaDTO;

public interface IPagoService {
    void registrarPagoEfectivoTransferenciaResidente(Integer idDeuda);
    void pagarDeuda(Integer idDeuda, String metodoPago);

    // Ahora recibe un único objeto limpio
    void pagarDeudaTarjeta(PagoTarjetaDTO pagoTarjetaDTO);
}