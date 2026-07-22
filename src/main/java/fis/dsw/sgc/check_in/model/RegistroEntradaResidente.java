package fis.dsw.sgc.check_in.model;

public class RegistroEntradaResidente extends RegistroEntrada {
    private String numeroDepartamento;

    public RegistroEntradaResidente() {
        this.tipoEntrada = "RESIDENTE";
    }

    public RegistroEntradaResidente(int idEntrada, String nombres, String apellidos, String cedula,
                                    String fechaLlegada, String horaLlegada, String numeroDepartamento) {
        super(idEntrada, nombres, apellidos, cedula, fechaLlegada, horaLlegada, "RESIDENTE");
        this.numeroDepartamento = numeroDepartamento;
    }

    public boolean validarResidencia() {
        return numeroDepartamento != null && !numeroDepartamento.isBlank();
    }

    @Override
    public String obtenerDetalleEntrada() {
        return "Entrada Residente: " + nombres + " " + apellidos + " (Ced: " + cedula + ") - Depto: " + numeroDepartamento;
    }

    public String getNumeroDepartamento() {
        return numeroDepartamento;
    }

    public void setNumeroDepartamento(String numeroDepartamento) {
        this.numeroDepartamento = numeroDepartamento;
    }
}
