package fis.dsw.sgc.finanzas.model;

public class DeudaMulta implements ITipoDeuda {
    @Override
    public double calcularValor(double valorBase) {
        return valorBase; // La multa es un valor fijo ingresado
    }

    @Override
    public String getMotivo() { return "MULTA"; }
}