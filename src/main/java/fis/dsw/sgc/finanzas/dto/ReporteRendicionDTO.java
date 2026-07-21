package fis.dsw.sgc.finanzas.dto;

public class ReporteRendicionDTO {
    public String fechaInicio, fechaFin;
    // Totales de Gastos
    public double totalServiciosBasicos, totalSueldosGastos, totalOtrosGastos, totalGastosGeneral;
    // Totales de Ingresos (Pagos)
    public double totalMultas, totalAlicuotas, totalReservas, totalIngresosGeneral;
    // Rendición
    public double balanceNeto;
    public String observaciones;
}
