package fis.dsw.sgc.finanzas.model;

public class EstadoEnProceso implements IEstadoDeuda {
    @Override
    public void procesarPago(Deuda deuda) {
        throw new IllegalStateException("El pago ya está siendo procesado (ej: esperando confirmación de transferencia).");
    }

    @Override
    public void aplicarMora(Deuda deuda) {
        throw new IllegalStateException("No se puede aplicar mora, el pago está en verificación.");
    }

    @Override
    public void anular(Deuda deuda) {
        deuda.setEstado(new EstadoEliminada());
    }

    @Override
    public String getNombreEstado() { return "EN PROCESO"; }
}