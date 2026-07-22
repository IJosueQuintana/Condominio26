package fis.dsw.sgc.finanzas.model;

public class EstadoMora implements IEstadoDeuda {
    @Override
    public void procesarPago(Deuda deuda) {
        // Permitido pagar estando en mora.
    }

    @Override
    public void aplicarMora(Deuda deuda) {
        throw new IllegalStateException("La deuda ya se encuentra en estado de MORA.");
    }

    @Override
    public void anular(Deuda deuda) {
        deuda.setEstado(new EstadoEliminada());
    }

    @Override
    public String getNombreEstado() { return "EN MORA"; }
}