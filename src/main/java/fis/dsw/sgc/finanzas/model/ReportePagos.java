package fis.dsw.sgc.finanzas.model;

import fis.dsw.sgc.finanzas.dto.DetallePagoDTO;
import fis.dsw.sgc.finanzas.dto.ReportePagosDTO;
import java.time.LocalDate;
import java.util.List;

public class ReportePagos extends ReporteFinanciero<ReportePagosDTO> {

    private List<DetallePagoDTO> pagosBrutos;
    private ReportePagosDTO dto;

    public ReportePagos(LocalDate fechaInicio, LocalDate fechaFin, List<DetallePagoDTO> pagosBrutos) {
        super(fechaInicio, fechaFin);
        this.pagosBrutos = pagosBrutos;
        this.dto = new ReportePagosDTO();
        this.dto.detalles = pagosBrutos;
    }

    @Override
    protected void recopilarDatos() {}

    @Override
    protected void calcularTotales() {
        for (DetallePagoDTO pago : pagosBrutos) {
            dto.totalGeneral += pago.valor;
            if (pago.motivo.equalsIgnoreCase("MULTA")) dto.totalMultas += pago.valor;
            else if (pago.motivo.equalsIgnoreCase("ALICUOTA")) dto.totalAlicuotas += pago.valor;
            else if (pago.motivo.equalsIgnoreCase("RESERVA")) dto.totalReservas += pago.valor;
        }
    }

    @Override
    protected ReportePagosDTO formatearSalida() {
        return this.dto;
    }
}