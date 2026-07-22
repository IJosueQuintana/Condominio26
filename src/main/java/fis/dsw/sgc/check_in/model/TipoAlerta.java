package fis.dsw.sgc.check_in.model;

public interface TipoAlerta {
    String obtenerNombre();
    PrioridadAlerta obtenerPrioridadPorDefecto();
    boolean validarMensaje(String mensaje);
}
