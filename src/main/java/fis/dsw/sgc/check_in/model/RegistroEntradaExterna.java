package fis.dsw.sgc.check_in.model;

public class RegistroEntradaExterna extends RegistroEntrada {
    private String entidadProcedencia;

    public RegistroEntradaExterna() {
        this.tipoEntrada = "EXTERNA";
    }

    public RegistroEntradaExterna(int idEntrada, String nombres, String apellidos, String cedula,
                                  String fechaLlegada, String horaLlegada, String entidadProcedencia) {
        super(idEntrada, nombres, apellidos, cedula, fechaLlegada, horaLlegada, "EXTERNA");
        this.entidadProcedencia = entidadProcedencia;
    }

    public void registrarMotivoExterno(String motivo) {
        this.informacionAdicional = motivo;
    }

    @Override
    public String obtenerDetalleEntrada() {
        return "Entrada Externa: " + nombres + " " + apellidos + " (Ced: " + cedula + ") - Entidad: " + entidadProcedencia;
    }

    public String getEntidadProcedencia() {
        return entidadProcedencia;
    }

    public void setEntidadProcedencia(String entidadProcedencia) {
        this.entidadProcedencia = entidadProcedencia;
    }
}
