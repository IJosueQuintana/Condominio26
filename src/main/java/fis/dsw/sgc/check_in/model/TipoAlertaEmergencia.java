package fis.dsw.sgc.check_in.model;

public class TipoAlertaEmergencia implements TipoAlerta {
    @Override
    public String obtenerNombre() {
        return "Emergencia";
    }

    @Override
    public PrioridadAlerta obtenerPrioridadPorDefecto() {
        return PrioridadAlerta.CRITICA;
    }

    @Override
    public boolean validarMensaje(String mensaje) {
        return mensaje != null && !mensaje.trim().isEmpty();
    }
}
