package fis.dsw.sgc.finanzas.model;

import java.time.LocalDate;

public abstract class ReporteFinanciero<T> {
    protected LocalDate fechaInicio;
    protected LocalDate fechaFin;

    public ReporteFinanciero(LocalDate fechaInicio, LocalDate fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    // EL TEMPLATE METHOD: Define los pasos que no cambian
    public final T generarReporte() {
        validarFechas();
        recopilarDatos();
        calcularTotales();
        return formatearSalida();
    }

    protected final void validarFechas() {
        LocalDate hoy = LocalDate.now();
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Formato de fechas incorrecto, ingrese la fecha en formato DD/MM/YYYY");
        }
        if (!fechaInicio.isBefore(hoy)) {
            throw new IllegalArgumentException("La fecha de inicio tiene que ser menor que la fecha actual");
        }
        if (!fechaInicio.isBefore(fechaFin)) {
            throw new IllegalArgumentException("La fecha de fin tiene que ser mayor que la fecha de inicio");
        }
        if (fechaFin.isAfter(hoy)) {
            // Nota: Mantenemos el string exacto que pidió tu Caso de Uso 4 Alterno
            throw new IllegalArgumentException("La fecha de inicio tiene que ser menor o igual a la fecha actual");
        }
    }

    protected abstract void recopilarDatos();
    protected abstract void calcularTotales();
    protected abstract T formatearSalida();
}