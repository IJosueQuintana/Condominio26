package fis.dsw.sgc.finanzas.model;

import java.time.LocalDate;

public class GastoServicioBasico extends Gasto {

    private String tipoServicio; // AGUA, LUZ, TELEFONO o INTERNET

    public GastoServicioBasico(String descripcion, double valor, LocalDate fechaGasto) {
        super(descripcion, valor, fechaGasto);
        this.tipoServicio = descripcion.toUpperCase().trim();
    }

    @Override
    public boolean validarDetalle() {
        // Valida estrictamente que sea uno de los 4 servicios básicos permitidos[cite: 15]
        return tipoServicio.equals("AGUA") ||
                tipoServicio.equals("LUZ") ||
                tipoServicio.equals("TELEFONO") ||
                tipoServicio.equals("INTERNET");
    }

    @Override
    public String getTipoGasto() {
        return "SERVICIO_BASICO";
    }
}