package fis.dsw.sgc.inmuebles.dto;

public class DimensionesInmuebleDTO {
    private final double tamanoMetrosCuadradosCondominio;
    private final double tamanoMetrosCuadradosDepartamento;

    public DimensionesInmuebleDTO(double tamanoMetrosCuadradosCondominio,
                                   double tamanoMetrosCuadradosDepartamento) {
        this.tamanoMetrosCuadradosCondominio = tamanoMetrosCuadradosCondominio;
        this.tamanoMetrosCuadradosDepartamento = tamanoMetrosCuadradosDepartamento;
    }

    public double getTamanoMetrosCuadradosCondominio() { return tamanoMetrosCuadradosCondominio; }
    public double getTamanoMetrosCuadradosDepartamento() { return tamanoMetrosCuadradosDepartamento; }
}