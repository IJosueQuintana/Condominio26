package fis.dsw.sgc.finanzas.model;

public interface IEstadoDeuda {
    void procesarPago(Deuda deuda);
    void aplicarMora(Deuda deuda);
    void anular(Deuda deuda);
    String getNombreEstado();
}
