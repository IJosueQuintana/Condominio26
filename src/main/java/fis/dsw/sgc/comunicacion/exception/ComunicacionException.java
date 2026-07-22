package fis.dsw.sgc.comunicacion.exception;
public class ComunicacionException extends RuntimeException {
    public ComunicacionException(String message) { super(message); }
    public ComunicacionException(String message, Throwable cause) { super(message, cause); }
}
