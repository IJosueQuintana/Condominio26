package fis.dsw.sgc.check_in.model;

public class TipoAlertaSimulacro implements TipoAlerta {
    @Override
    public String obtenerNombre() {
        return "Simulacro";
    }

    @Override
    public PrioridadAlerta obtenerPrioridadPorDefecto() {
        return PrioridadAlerta.MEDIA;
    }

    @Override
    public boolean validarMensaje(String mensaje) {
        return mensaje != null && !mensaje.trim().isEmpty();
    }
}
