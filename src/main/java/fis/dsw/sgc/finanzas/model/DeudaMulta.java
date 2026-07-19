package fis.dsw.sgc.finanzas.model;

public class DeudaMulta implements ITipoDeuda {
    @Override
    public double calcularValor(double valorBase) {
        return 0;
    }

    @Override
    public String getMotivo() {
        return "";
    }
}
