package fis.dsw.sgc.check_in.exception;

public class CheckInException extends Exception {
    public CheckInException(String mensaje) {
        super(mensaje);
    }

    public CheckInException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
