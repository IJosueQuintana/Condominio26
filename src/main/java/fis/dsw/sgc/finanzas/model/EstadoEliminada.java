package fis.dsw.sgc.finanzas.model;

public class EstadoEliminada implements IEstadoDeuda {

    @Override
    public void procesarPago(Deuda deuda) {

    }

    @Override
    public void aplicarMora(Deuda deuda) {

    }

    @Override
    public void anular(Deuda deuda) {

    }

    @Override
    public String getNombreEstado() {
        return "";
    }
}
