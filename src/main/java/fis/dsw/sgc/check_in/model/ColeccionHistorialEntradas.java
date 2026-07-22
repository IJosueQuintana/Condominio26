package fis.dsw.sgc.check_in.model;

import java.util.ArrayList;
import java.util.List;

public class ColeccionHistorialEntradas {
    private final List<RegistroEntrada> registros;

    public ColeccionHistorialEntradas() {
        this.registros = new ArrayList<>();
    }

    public ColeccionHistorialEntradas(List<RegistroEntrada> registros) {
        this.registros = registros != null ? registros : new ArrayList<>();
    }

    public void agregarRegistro(RegistroEntrada registro) {
        if (registro != null) {
            this.registros.add(registro);
        }
    }

    public IteradorRegistroEntrada crearIterador() {
        return new IteradorHistorialEntradas(this.registros);
    }

    public List<RegistroEntrada> getRegistros() {
        return registros;
    }
}
