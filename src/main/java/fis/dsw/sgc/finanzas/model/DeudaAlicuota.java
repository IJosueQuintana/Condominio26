package fis.dsw.sgc.finanzas.model;

import java.io.Serializable;

public class DeudaAlicuota implements ITipoDeuda {
    @Override
    public double calcularValor(double valorBase) {
        return 0;
    }

    @Override
    public String getMotivo() {
        return "";
    }
}
