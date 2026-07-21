package fis.dsw.sgc.finanzas.service;

import fis.dsw.sgc.finanzas.dto.*;
import java.time.LocalDate;

public interface IReportesService {
    ReporteGastosDTO generarReporteGastos(LocalDate fechaInicio, LocalDate fechaFin);
    ReportePagosDTO generarReportedePagosRealizados(LocalDate fechaInicio, LocalDate fechaFin);
    ReportePagosDTO consultarPagosEfectuados(LocalDate fechaInicio, LocalDate fechaFin, String numeroCedulaIdentidadResidente);
    ReporteRendicionDTO generarReporteRendicionCuentas(LocalDate fechaInicio, LocalDate fechaFin, String observaciones);
    ReporteRendicionDTO consultarReporteRendicionCuentas(LocalDate fechaInicio, LocalDate fechaFin);
}