package fis.dsw.sgc.finanzas.model;

import java.time.LocalDate;

public class GastoSueldos extends Gasto {

    public GastoSueldos(String descripcion, double valor, LocalDate fechaGasto) {
        super(descripcion, valor, fechaGasto);
    }

    @Override
    public boolean validarDetalle() {
        // Valida longitud (máximo 200 caracteres)[cite: 15]
        if (this.descripcion == null || this.descripcion.length() > 200) {
            return false;
        }
        // Valida formato: alfabeto, ñ, comas, espacios y puntos[cite: 15]
        return this.descripcion.matches("^[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s.,]+$");
    }

    @Override
    public String getTipoGasto() {
        return "SUELDO";
    }
}