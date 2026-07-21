package fis.dsw.sgc.finanzas.model;

public class DeudaAlicuota implements ITipoDeuda {
    private double tamanoDepartamento;
    private double tamanoCondominio;

    public DeudaAlicuota(double tamanoDepartamento, double tamanoCondominio) {
        this.tamanoDepartamento = tamanoDepartamento;
        this.tamanoCondominio = tamanoCondominio;
    }

    @Override
    public double calcularValor(double valorBase) {
        if (tamanoCondominio <= 0) throw new IllegalArgumentException("Tamaño del condominio inválido.");
        return valorBase * (tamanoDepartamento / tamanoCondominio);
    }

    @Override
    public String getMotivo() { return "ALICUOTA"; }
}