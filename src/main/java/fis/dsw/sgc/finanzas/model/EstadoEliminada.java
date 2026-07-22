package fis.dsw.sgc.finanzas.model;

public class EstadoEliminada implements IEstadoDeuda {
    @Override
    public void procesarPago(Deuda deuda) {
        throw new IllegalStateException("No se puede pagar una deuda eliminada.");
    }

    @Override
    public void aplicarMora(Deuda deuda) {
        throw new IllegalStateException("No se puede aplicar mora a una deuda eliminada.");
    }

    @Override
    public void anular(Deuda deuda) {
        throw new IllegalStateException("La deuda ya está eliminada.");
    }

    @Override
    public String getNombreEstado() { return "ELIMINADA"; }
}