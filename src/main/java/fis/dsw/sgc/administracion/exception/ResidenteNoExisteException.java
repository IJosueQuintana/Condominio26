package fis.dsw.sgc.administracion.exception;

public class ResidenteNoExisteException extends RuntimeException {
    public ResidenteNoExisteException(String cedula) {
        super("No existe un Residente con la cédula " + cedula);
    }
}
