package fis.dsw.sgc.finanzas.model;

import fis.dsw.sgc.finanzas.dto.DetalleGastoDTO;
import fis.dsw.sgc.finanzas.dto.ReporteGastosDTO;
import java.time.LocalDate;
import java.util.List;

public class ReporteGastos extends ReporteFinanciero<ReporteGastosDTO> {

    private List<DetalleGastoDTO> gastosBrutos; // Inyectados por el Service
    private ReporteGastosDTO dto;

    public ReporteGastos(LocalDate fechaInicio, LocalDate fechaFin, List<DetalleGastoDTO> gastosBrutos) {
        super(fechaInicio, fechaFin);
        this.gastosBrutos = gastosBrutos;
        this.dto = new ReporteGastosDTO();
        this.dto.detalles = gastosBrutos;
    }

    @Override
    protected void recopilarDatos() {
        // Los datos ya fueron inyectados por el Service (MOCK por ahora)
    }

    @Override
    protected void calcularTotales() {
        for (DetalleGastoDTO gasto : gastosBrutos) {
            dto.totalGeneral += gasto.valor;
            switch (gasto.motivo.toUpperCase()) {
                case "AGUA": dto.totalAgua += gasto.valor; break;
                case "LUZ": dto.totalLuz += gasto.valor; break;
                case "TELEFONO": dto.totalTelefono += gasto.valor; break;
                case "INTERNET": dto.totalInternet += gasto.valor; break;
                case "SUELDO": dto.totalSueldos += gasto.valor; break;
                default: dto.totalOtros += gasto.valor; break;
            }
        }
    }

    @Override
    protected ReporteGastosDTO formatearSalida() {
        return this.dto;
    }
}