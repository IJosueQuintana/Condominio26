package fis.dsw.sgc.check_in.model;

public class RegistroEntradaVisitante extends RegistroEntrada {
    private boolean autorizadoPorResidente;
    private String residenteAnfitrion;

    public RegistroEntradaVisitante() {
        this.tipoEntrada = "VISITANTE";
    }

    public RegistroEntradaVisitante(int idEntrada, String nombres, String apellidos, String cedula,
                                    String fechaLlegada, String horaLlegada, boolean autorizadoPorResidente,
                                    String residenteAnfitrion) {
        super(idEntrada, nombres, apellidos, cedula, fechaLlegada, horaLlegada, "VISITANTE");
        this.autorizadoPorResidente = autorizadoPorResidente;
        this.residenteAnfitrion = residenteAnfitrion;
    }

    public boolean validarAutorizacion() {
        return autorizadoPorResidente;
    }

    @Override
    public String obtenerDetalleEntrada() {
        return "Entrada Visitante: " + nombres + " " + apellidos + " (Ced: " + cedula + ") - Anfitrión: " + residenteAnfitrion;
    }

    public boolean isAutorizadoPorResidente() {
        return autorizadoPorResidente;
    }

    public void setAutorizadoPorResidente(boolean autorizadoPorResidente) {
        this.autorizadoPorResidente = autorizadoPorResidente;
    }

    public String getResidenteAnfitrion() {
        return residenteAnfitrion;
    }

    public void setResidenteAnfitrion(String residenteAnfitrion) {
        this.residenteAnfitrion = residenteAnfitrion;
    }
}
