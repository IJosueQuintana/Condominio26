package fis.dsw.sgc.administracion.dao;

import fis.dsw.sgc.administracion.model.Usuario;
import java.sql.Connection;
import java.util.List;
import java.util.UUID;

public class UsuarioDAOMySQL implements IUsuarioDAO {
    private Connection dbConn;

    @Override
    public void guardar(Usuario usuario) {}

    @Override
    public void actualizar(Usuario usuario) {}

    @Override
    public Usuario buscarPorId(UUID idUsuario) {
        return null;
    }

    @Override
    public Usuario buscarPorCorreo(String correo) {
        return null;
    }

    @Override
    public List<Usuario> listarTodos() {
        return null;
    }
}