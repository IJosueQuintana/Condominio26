package fis.dsw.sgc.finanzas.model;

public class EstadoPagada implements IEstadoDeuda {
    @Override
    public void procesarPago(Deuda deuda) {
        throw new IllegalStateException("Esta deuda ya fue pagada.");
    }

    @Override
    public void aplicarMora(Deuda deuda) {
        throw new IllegalStateException("No se puede aplicar mora a una deuda pagada.");
    }

    @Override
    public void anular(Deuda deuda) {
        throw new IllegalStateException("No se puede eliminar una deuda que ya fue pagada y registrada.");
    }

    @Override
    public String getNombreEstado() { return "PAGADA"; }
}