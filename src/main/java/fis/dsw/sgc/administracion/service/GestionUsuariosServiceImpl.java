package fis.dsw.sgc.administracion.service;

import fis.dsw.sgc.administracion.dao.ICuentaDAO;
import fis.dsw.sgc.administracion.dao.IUsuarioDAO;
import fis.dsw.sgc.administracion.model.NombreRol;
import fis.dsw.sgc.administracion.model.Usuario;

import java.util.List;
import java.util.UUID;

public class GestionUsuariosServiceImpl implements IGestionUsuariosAPI {
    private IUsuarioDAO usuarioDAO;
    private ICuentaDAO cuentaDAO;

    @Override
    public boolean autenticar(String correo, String contrasena) {
        return false;
    }

    @Override
    public Usuario obtenerUsuarioPorId(UUID idUsuario) {
        return null;
    }

    @Override
    public boolean validarPermiso(UUID idCuenta, String recurso) {
        return false;
    }

    @Override
    public List<Usuario> listarUsuariosPorRol(NombreRol rol) {
        return null;
    }

    @Override
    public void iniciarRecuperacionContrasena(String correo) {}
}