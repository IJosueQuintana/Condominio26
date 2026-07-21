package fis.dsw.sgc.finanzas.service;

import fis.dsw.sgc.finanzas.model.*;
import java.time.LocalDate;

public class GastoFactoryImpl {

    public Gasto crearGasto(String motivo, String descripcion, double valor, LocalDate fechaGasto) {
        switch (motivo.toUpperCase().trim()) {
            case "SERVICIO BASICO":
                return new GastoServicioBasico(descripcion, valor, fechaGasto);
            case "SUELDOS":
                return new GastoSueldos(descripcion, valor, fechaGasto);
            case "OTROS":
                return new GastoOtros(descripcion, valor, fechaGasto);
            default:
                throw new IllegalArgumentException("Motivo de pago inválido. Debe ser SERVICIO BASICO, SUELDOS u OTROS.");
        }
    }
}