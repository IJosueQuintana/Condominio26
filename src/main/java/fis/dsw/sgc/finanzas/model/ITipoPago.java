package fis.dsw.sgc.finanzas.model;



public interface ITipoPago {
    // Retorna true si la transacción fue exitosa
    boolean procesarTransaccion(Pago pago, Deuda deuda);
}