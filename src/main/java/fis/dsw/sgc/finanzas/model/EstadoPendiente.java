package fis.dsw.sgc.finanzas.model;

public class EstadoPendiente implements IEstadoDeuda {
    @Override
    public void procesarPago(Deuda deuda) {
        // Permitido. El Service luego decidirá si pasa a EN_PROCESO o PAGADA según el método.
    }

    @Override
    public void aplicarMora(Deuda deuda) {
        deuda.setEstado(new EstadoMora());
    }

    @Override
    public void anular(Deuda deuda) {
        deuda.setEstado(new EstadoEliminada());
    }

    @Override
    public String getNombreEstado() { return "PENDIENTE"; }
}