package fis.dsw.sgc.check_in.model;

public class TipoAlertaAvisoGeneral implements TipoAlerta {
    @Override
    public String obtenerNombre() {
        return "Aviso General";
    }

    @Override
    public PrioridadAlerta obtenerPrioridadPorDefecto() {
        return PrioridadAlerta.BAJA;
    }

    @Override
    public boolean validarMensaje(String mensaje) {
        return mensaje != null && !mensaje.trim().isEmpty();
    }
}
