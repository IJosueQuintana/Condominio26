package fis.dsw.sgc.finanzas.service;

import fis.dsw.sgc.administracion.exception.ResidenteNoExisteException;
import fis.dsw.sgc.administracion.service.IGestionUsuariosAPI;
import fis.dsw.sgc.usuarios.dto.ResidenteFachadaDTO;
import fis.dsw.sgc.finanzas.dao.IReportesDAO;
import fis.dsw.sgc.finanzas.dto.*;
import fis.dsw.sgc.finanzas.exception.FechasInvalidasException;
import fis.dsw.sgc.finanzas.exception.NoExistenPagosException;
import fis.dsw.sgc.finanzas.exception.ReporteRendicionNoExisteException;
import fis.dsw.sgc.finanzas.model.*;
import java.time.LocalDate;
import java.util.List;

public class ReportesServiceImpl implements IReportesService {

    private IReportesDAO reportesDAO;
    private IGestionUsuariosAPI gestionUsuariosAPI;

    // ¡Mucho más limpio! Solo depende de SU dao y de la fachada externa.
    public ReportesServiceImpl(IReportesDAO reportesDAO, IGestionUsuariosAPI gestionUsuariosAPI) {
        this.reportesDAO = reportesDAO;
        this.gestionUsuariosAPI = gestionUsuariosAPI;
    }

    @Override
    public ReporteGastosDTO generarReporteGastos(LocalDate fechaInicio, LocalDate fechaFin) {

        if(fechaInicio.isAfter(fechaFin) || fechaInicio.isAfter(LocalDate.now()) || fechaFin.isAfter(LocalDate.now())){
            throw new FechasInvalidasException("Las fechas no cumplen un formato válido");
        }
        // Le pedimos los gastos al ReportesDAO
        List<DetalleGastoDTO> gastosBD = reportesDAO.buscarGastosPorRangoFechas(fechaInicio, fechaFin);

        ReporteGastos reporte = new ReporteGastos(fechaInicio, fechaFin, gastosBD);
        ReporteGastosDTO resultado = reporte.generarReporte();

        System.out.println("Reporte generado correctamente");
        return resultado;
    }

    @Override
    public ReportePagosDTO generarReportedePagosRealizados(LocalDate fechaInicio, LocalDate fechaFin) {

        if(fechaInicio.isAfter(fechaFin) || fechaInicio.isAfter(LocalDate.now()) || fechaFin.isAfter(LocalDate.now())){
            throw new FechasInvalidasException("Las fechas no cumplen un formato válido");
        }

        // Buscamos los pagos de TODOS los usuarios en el rango[cite: 24]
        List<DetallePagoDTO> pagosBD = reportesDAO.buscarPagosPorRangoFechas(fechaInicio, fechaFin);

        // Validamos el escenario alterno 5[cite: 24]
        if (pagosBD.isEmpty()) {
            throw new NoExistenPagosException("No existen Pagos entre el fechaInicio y el fechaFin");
        }

        ReportePagos reporte = new ReportePagos(fechaInicio, fechaFin, pagosBD);
        ReportePagosDTO resultado = reporte.generarReporte();

        System.out.println("Reporte generado correctamente");
        return resultado;
    }

    @Override
    public ReportePagosDTO consultarPagosEfectuados(LocalDate fechaInicio, LocalDate fechaFin, String cedula) {

        if(fechaInicio.isAfter(fechaFin) || fechaInicio.isAfter(LocalDate.now()) || fechaFin.isAfter(LocalDate.now())){
            throw new FechasInvalidasException("Las fechas no cumplen un formato válido");
        }

        // DECLARAMOS LA VARIABLE FUERA DEL TRY PARA QUE EXISTA EN TODO EL MÉTODO[cite: 31]
        ResidenteFachadaDTO residente = null;
        try{
            residente = gestionUsuariosAPI.obtenerResidentePorCedula(cedula);
        }catch (ResidenteNoExisteException e){
            throw new RuntimeException(e.getMessage());
        }

        // AHORA SÍ PODEMOS USAR 'residente' SIN ERROR DE COMPILACIÓN[cite: 31]
        List<DetallePagoDTO> pagosBD = reportesDAO.buscarPagosPorRangoFechasYUsuario(fechaInicio, fechaFin, residente.getIdUsuario());

        if (pagosBD.isEmpty()) {
            throw new NoExistenPagosException("No existen Pagos asociados para el Residente " + residente.getNombreCompleto() + " durante fechaInicio y fechaFin");
        }

        ReportePagos reporte = new ReportePagos(fechaInicio, fechaFin, pagosBD);
        ReportePagosDTO resultado = reporte.generarReporte();

        System.out.println("Reporte generado correctamente");
        return resultado;
    }

    @Override
    public ReporteRendicionDTO generarReporteRendicionCuentas(LocalDate fechaInicio, LocalDate fechaFin, String observaciones) {
        if (reportesDAO.existeReporteRendicion(fechaInicio, fechaFin)) {
            throw new FechasInvalidasException("Ya se generó un reporte de rendición de cuentas para las fechas especificadas, puede consultarlo en cualquier momento");
        }

        if (observaciones == null || !observaciones.matches("^[a-zA-Z0-9 áéíóúÁÉÍÓÚñÑ]+$")) {
            throw new IllegalArgumentException("Las observaciones del reporte de rendición cuentas solo admite caracteres del alfabeto, espacios en blanco y numeros");
        }

        ReporteGastosDTO gastosCalculados = generarReporteGastos(fechaInicio, fechaFin);

        ReportePagosDTO pagosCalculados;
        try {
            pagosCalculados = generarReportedePagosRealizados(fechaInicio, fechaFin);
        } catch (NoExistenPagosException e) {
            // ¡Corregido el tipo de excepción para que atrape la que acabamos de usar!
            pagosCalculados = new ReportePagosDTO(); // DTO vacío si no hay ingresos
        }

        ReporteRendicion reporte = new ReporteRendicion(fechaInicio, fechaFin, observaciones, gastosCalculados, pagosCalculados);
        ReporteRendicionDTO resultado = reporte.generarReporte();

        System.out.println("MÓDULO COMUNICACIÓN (MOCK): Notificando a residentes sobre nuevo reporte.");

        reportesDAO.guardarReporteRendicion(resultado);

        System.out.println("Reporte de Rendición de Cuentas generado exitosamente, disponible para la consulta de los residentes");
        System.out.println("Reporte generado y notificado a los Residentes de forma correcta");

        return resultado;
    }

    @Override
    public ReporteRendicionDTO consultarReporteRendicionCuentas(LocalDate fechaInicio, LocalDate fechaFin) {
        ReporteRendicionDTO reporteBD = reportesDAO.buscarReporteRendicion(fechaInicio, fechaFin);

        if (reporteBD == null) {
            throw new ReporteRendicionNoExisteException("No existe un reporte de rendición de cuentas para las fechas especificadas, este atento a sus notificaciones");
        }

        System.out.println("Reporte Consultado exitosamente");
        return reporteBD;
    }
}