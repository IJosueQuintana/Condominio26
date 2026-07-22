package fis.dsw.sgc.check_in.model;

public interface IteradorRegistroEntrada {
    boolean tieneSiguiente();
    RegistroEntrada siguiente();
    void reiniciar();
}
