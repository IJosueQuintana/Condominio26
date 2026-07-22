package fis.dsw.sgc.administracion.dao;

import fis.dsw.sgc.administracion.model.Rol;
import java.sql.Connection;
import java.util.List;

public class RolDAOMySQL implements IRolDAO {
    private Connection dbConn;

    @Override
    public void guardar(Rol rol) {}

    @Override
    public void actualizar(Rol rol) {}

    @Override
    public List<Rol> listarTodos() {
        return null;
    }

    @Override
    public void asignarRolAUsuario(int idUsuario, int idRol) {}
}