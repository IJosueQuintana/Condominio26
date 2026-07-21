package fis.dsw.sgc.finanzas.service;

import java.time.LocalDate;

public interface IReportesService {
    void generarReporteGastos(LocalDate fechaInicio, LocalDate fechaFin);
    void generarReportedePagosRealizados(LocalDate fechaInicio, LocalDate fechaFin);
    void generarReporteRendicionCuentas(LocalDate fechaInicio, LocalDate fechaFin, String observaciones);
    void consultarReporteRendicionCuentas(LocalDate fechaInicio, LocalDate fechaFin);
}
