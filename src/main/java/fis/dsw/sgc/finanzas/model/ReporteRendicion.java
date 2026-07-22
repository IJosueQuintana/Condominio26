package fis.dsw.sgc.finanzas.model;

import fis.dsw.sgc.finanzas.dto.*;
import java.time.LocalDate;

public class ReporteRendicion extends ReporteFinanciero<ReporteRendicionDTO> {

    private ReporteGastosDTO gastosDTO;
    private ReportePagosDTO pagosDTO;
    private String observaciones;
    private ReporteRendicionDTO dto;

    public ReporteRendicion(LocalDate fechaInicio, LocalDate fechaFin, String observaciones, ReporteGastosDTO gastos, ReportePagosDTO pagos) {
        super(fechaInicio, fechaFin);
        this.observaciones = observaciones;
        this.gastosDTO = gastos;
        this.pagosDTO = pagos;
        this.dto = new ReporteRendicionDTO();
    }

    @Override
    protected void recopilarDatos() {}

    @Override
    protected void calcularTotales() {
        // Mapeo de Gastos
        dto.totalServiciosBasicos = gastosDTO.totalAgua + gastosDTO.totalLuz + gastosDTO.totalTelefono + gastosDTO.totalInternet;
        dto.totalSueldosGastos = gastosDTO.totalSueldos;
        dto.totalOtrosGastos = gastosDTO.totalOtros;
        dto.totalGastosGeneral = gastosDTO.totalGeneral;

        // Mapeo de Ingresos
        dto.totalMultas = pagosDTO.totalMultas;
        dto.totalAlicuotas = pagosDTO.totalAlicuotas;
        dto.totalReservas = pagosDTO.totalReservas;
        dto.totalIngresosGeneral = pagosDTO.totalGeneral;

        // Balance Neto
        dto.balanceNeto = dto.totalIngresosGeneral - dto.totalGastosGeneral;
    }

    @Override
    protected ReporteRendicionDTO formatearSalida() {
        dto.fechaInicio = this.fechaInicio.toString();
        dto.fechaFin = this.fechaFin.toString();
        dto.observaciones = this.observaciones;
        return dto;
    }
}