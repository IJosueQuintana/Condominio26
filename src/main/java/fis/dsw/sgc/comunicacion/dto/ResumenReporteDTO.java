package fis.dsw.sgc.comunicacion.dto;
public record ResumenReporteDTO(String tipo, int cantidad, int enviadas, int fallidas, double tasaExito) {
    public String tasaExitoFormateada() { return String.format("%.1f %%", tasaExito); }
}
