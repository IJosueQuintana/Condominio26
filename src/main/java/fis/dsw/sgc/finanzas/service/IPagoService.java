package fis.dsw.sgc.finanzas.service;

import java.time.LocalDate;

public interface IPagoService {

    void registrarPagoEfectivoTransferenciaResidente(Integer idDeuda); // Caso de uso: registrarPagoEfectivoTransferenciaResidente[cite: 3]
    void generarReporteDePagosRealizados(LocalDate fechaInicio, LocalDate fechaFin); // Caso de uso: generarReporteDePagosRealizados[cite: 3]
    void consultarPagosEfectuados(LocalDate fechaInicio, LocalDate fechaFin, String cedula); // Caso de uso: consultarPagosEfectuados[cite: 3]
}