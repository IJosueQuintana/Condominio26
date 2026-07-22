package fis.dsw.sgc.finanzas.service;

import fis.dsw.sgc.finanzas.dto.*;
import fis.dsw.sgc.finanzas.model.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportesServiceImpl implements IReportesService {

    @Override
    public ReporteGastosDTO generarReporteGastos(LocalDate fechaInicio, LocalDate fechaFin) {
        // 1. Obtener datos crudos (MOCK de DAO)
        List<DetalleGastoDTO> gastosMock = new ArrayList<>();
        gastosMock.add(new DetalleGastoDTO("AGUA", "Pago EPMAPS", 50.0));
        gastosMock.add(new DetalleGastoDTO("LUZ", "Pago EEQ", 30.0));
        gastosMock.add(new DetalleGastoDTO("SUELDO", "Conserje", 450.0));

        // 2. Ejecutar Template Method
        ReporteGastos reporte = new ReporteGastos(fechaInicio, fechaFin, gastosMock);
        ReporteGastosDTO resultado = reporte.generarReporte();

        System.out.println("Reporte generado correctamente"); // Escenario Básico Paso 9
        return resultado;
    }

    @Override
    public ReportePagosDTO generarReportedePagosRealizados(LocalDate fechaInicio, LocalDate fechaFin) {
        // 1. Verificar si existen pagos (Escenario Alterno 5)
        boolean existenPagos = true; // MOCK DAO
        if (!existenPagos) {
            throw new IllegalStateException("No existen Pagos entre el fechaInicio y el fechaFin");
        }

        // 2. Obtener datos (MOCK)
        List<DetallePagoDTO> pagosMock = new ArrayList<>();
        pagosMock.add(new DetallePagoDTO("1700000000", "ALICUOTA", 150.0));
        pagosMock.add(new DetallePagoDTO("1700000001", "MULTA", 25.0));

        // 3. Ejecutar Template Method
        ReportePagos reporte = new ReportePagos(fechaInicio, fechaFin, pagosMock);
        ReportePagosDTO resultado = reporte.generarReporte();

        System.out.println("Reporte generado correctamente");
        return resultado;
    }

    @Override
    public ReportePagosDTO consultarPagosEfectuados(LocalDate fechaInicio, LocalDate fechaFin, String cedula) {
        // 1. Validar si existe residente (Escenario Alterno 5)
        boolean existeResidente = true; // MOCK Facade Usuarios
        if (!existeResidente) {
            throw new IllegalArgumentException("No existe un Residente con la cédula proporcionada");
        }

        // 2. Validar si tiene pagos (Escenario Alterno 6)
        boolean tienePagos = true; // MOCK
        if (!tienePagos) {
            throw new IllegalStateException("No existen Pagos asociados para el Residente " + cedula + " durante fechaInicio y fechaFin");
        }

        // 3. MOCK de datos solo para ese residente
        List<DetallePagoDTO> pagosMock = new ArrayList<>();
        pagosMock.add(new DetallePagoDTO(cedula, "ALICUOTA", 150.0));

        ReportePagos reporte = new ReportePagos(fechaInicio, fechaFin, pagosMock);
        ReportePagosDTO resultado = reporte.generarReporte();

        System.out.println("Reporte generado correctamente");
        return resultado;
    }

    @Override
    public ReporteRendicionDTO generarReporteRendicionCuentas(LocalDate fechaInicio, LocalDate fechaFin, String observaciones) {
        // 1. Verificar si ya existe reporte en ese periodo (Escenario Alterno 1)
        boolean reporteExiste = false; // MOCK DAO
        if (reporteExiste) {
            throw new IllegalStateException("Ya se generó un reporte de rendición de cuentas para las fechas especificadas, puede consultarlo en cualquier momento");
        }

        // 2. Validar formato de observaciones (Letras, números y espacios)
        if (observaciones == null || !observaciones.matches("^[a-zA-Z0-9 áéíóúÁÉÍÓÚñÑ]+$")) {
            throw new IllegalArgumentException("Las observaciones del reporte de rendición cuentas solo admite caracteres del alfabeto, espacios en blanco y numeros");
        }

        // 3. Generar sub-reportes para usarlos como insumos
        ReporteGastosDTO gastosCalculados = generarReporteGastos(fechaInicio, fechaFin);
        ReportePagosDTO pagosCalculados = generarReportedePagosRealizados(fechaInicio, fechaFin);

        // 4. Ejecutar Template Method
        ReporteRendicion reporte = new ReporteRendicion(fechaInicio, fechaFin, observaciones, gastosCalculados, pagosCalculados);
        ReporteRendicionDTO resultado = reporte.generarReporte();

        // 5. MOCK Notificación
        System.out.println("MÓDULO COMUNICACIÓN (MOCK): Notificando a residentes sobre nuevo reporte.");

        // 6. Guardar en Base de Datos (MOCK para tabla reporte_rendicion)
        // reporteRendicionDAO.guardar(resultado);

        System.out.println("Reporte de Rendición de Cuentas generado exitosamente, disponible para la consulta de los residentes");
        System.out.println("Reporte generado y notificado a los Residentes de forma correcta");

        return resultado;
    }

    @Override
    public ReporteRendicionDTO consultarReporteRendicionCuentas(LocalDate fechaInicio, LocalDate fechaFin) {
        // 1. Verificar si existe el reporte en ese periodo (Escenario Alterno 1 de Consultar)
        boolean reporteExiste = true; // MOCK DAO
        if (!reporteExiste) {
            throw new IllegalStateException("No existe un reporte de rendición de cuentas para las fechas especificadas, este atento a sus notificaciones");
        }

        // 2. MOCK: Como no hay BD aún, generamos uno falso en vuelo para devolverlo
        ReporteRendicionDTO reporteRecuperado = generarReporteRendicionCuentas(fechaInicio, fechaFin, "Reporte historico");

        System.out.println("Reporte Consultado exitosamente");
        return reporteRecuperado;
    }
}