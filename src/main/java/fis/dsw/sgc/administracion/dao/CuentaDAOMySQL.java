package fis.dsw.sgc.administracion.dao;

import fis.dsw.sgc.administracion.model.Cuenta;
import java.sql.Connection;
import java.util.UUID;

public class CuentaDAOMySQL implements ICuentaDAO {
    private Connection dbConn;

    @Override
    public void guardar(Cuenta cuenta) {}

    @Override
    public void actualizar(Cuenta cuenta) {}

    @Override
    public Cuenta buscarPorIdUsuario(UUID idUsuario) {
        return null;
    }

    @Override
    public Cuenta autenticar(String correo, String contrasena) {
        return null;
    }
}