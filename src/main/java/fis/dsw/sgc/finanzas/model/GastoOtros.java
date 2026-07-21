package fis.dsw.sgc.finanzas.model;

import java.time.LocalDate;

public class GastoOtros extends Gasto {

    public GastoOtros(String descripcion, double valor, LocalDate fechaGasto) {
        super(descripcion, valor, fechaGasto);
    }

    @Override
    public boolean validarDetalle() {
        if (this.descripcion == null || this.descripcion.length() > 200) {
            return false;
        }
        return this.descripcion.matches("^[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s.,]+$");
    }

    @Override
    public String getTipoGasto() {
        return "OTRO";
    }
}