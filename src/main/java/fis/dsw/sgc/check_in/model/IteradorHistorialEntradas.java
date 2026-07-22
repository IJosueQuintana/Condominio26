package fis.dsw.sgc.check_in.model;

import java.util.List;

public class IteradorHistorialEntradas implements IteradorRegistroEntrada {
    private final List<RegistroEntrada> registros;
    private int posicionActual;

    public IteradorHistorialEntradas(List<RegistroEntrada> registros) {
        this.registros = registros;
        this.posicionActual = 0;
    }

    @Override
    public boolean tieneSiguiente() {
        return registros != null && posicionActual < registros.size();
    }

    @Override
    public RegistroEntrada siguiente() {
        if (!tieneSiguiente()) {
            return null;
        }
        RegistroEntrada entrada = registros.get(posicionActual);
        posicionActual++;
        return entrada;
    }

    @Override
    public void reiniciar() {
        this.posicionActual = 0;
    }
}
