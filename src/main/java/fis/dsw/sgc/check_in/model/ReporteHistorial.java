package fis.dsw.sgc.check_in.model;

public class ReporteHistorial {
    private String descripcion;
    private String fechaGeneracion;
    private int totalEntradas;
    private int totalResidentes;
    private int totalVisitantes;
    private int totalExternos;
    private int totalVehiculos;

    public ReporteHistorial() {}

    public void generarResumen(IteradorRegistroEntrada iterador) {
        if (iterador == null) return;
        
        iterador.reiniciar();
        totalEntradas = 0;
        totalResidentes = 0;
        totalVisitantes = 0;
        totalExternos = 0;
        totalVehiculos = 0;

        while (iterador.tieneSiguiente()) {
            RegistroEntrada entrada = iterador.siguiente();
            totalEntradas++;
            
            if ("RESIDENTE".equalsIgnoreCase(entrada.getTipoEntrada()) || entrada instanceof RegistroEntradaResidente) {
                totalResidentes++;
            } else if ("VISITANTE".equalsIgnoreCase(entrada.getTipoEntrada()) || entrada instanceof RegistroEntradaVisitante) {
                totalVisitantes++;
            } else if ("EXTERNA".equalsIgnoreCase(entrada.getTipoEntrada()) || entrada instanceof RegistroEntradaExterna) {
                totalExternos++;
            }

            if (entrada.getPlacaVehiculo() != null && !entrada.getPlacaVehiculo().isBlank() 
                && !"N/A".equalsIgnoreCase(entrada.getPlacaVehiculo())) {
                totalVehiculos++;
            }
        }
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(String fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public int getTotalEntradas() {
        return totalEntradas;
    }

    public int getTotalResidentes() {
        return totalResidentes;
    }

    public int getTotalVisitantes() {
        return totalVisitantes;
    }

    public int getTotalExternos() {
        return totalExternos;
    }

    public int getTotalVehiculos() {
        return totalVehiculos;
    }
}
