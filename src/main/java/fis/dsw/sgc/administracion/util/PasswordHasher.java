package fis.dsw.sgc.administracion.util;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordHasher {

    private PasswordHasher() {
    }

    public static String hash(String contrasenaPlana) {
        return BCrypt.hashpw(contrasenaPlana, BCrypt.gensalt());
    }

    public static boolean verificar(String contrasenaPlana, String hash) {
        try {
            return BCrypt.checkpw(contrasenaPlana, hash);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
