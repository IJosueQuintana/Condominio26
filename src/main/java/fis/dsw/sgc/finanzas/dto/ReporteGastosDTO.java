package fis.dsw.sgc.finanzas.dto;

import java.util.List;

public class ReporteGastosDTO {
    public List<DetalleGastoDTO> detalles;
    public double totalAgua, totalLuz, totalTelefono, totalInternet, totalSueldos, totalOtros, totalGeneral;
    // (Agrega getters, setters y constructor según necesites en tu UI)
}
