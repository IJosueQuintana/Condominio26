package fis.dsw.sgc.finanzas.model;

public class DeudaReserva implements ITipoDeuda {
    @Override
    public double calcularValor(double valorBase) {
        return valorBase; // La reserva es un valor fijo
    }

    @Override
    public String getMotivo() { return "RESERVA"; }
}