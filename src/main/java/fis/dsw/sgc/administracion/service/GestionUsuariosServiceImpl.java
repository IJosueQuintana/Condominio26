package fis.dsw.sgc.administracion.service;

import fis.dsw.sgc.administracion.dao.CuentaDAOMySQL;
import fis.dsw.sgc.administracion.dao.ICuentaDAO;
import fis.dsw.sgc.administracion.dao.IUsuarioDAO;
import fis.dsw.sgc.administracion.dao.UsuarioDAOMySQL;
import fis.dsw.sgc.administracion.exception.ResidenteNoExisteException;
import fis.dsw.sgc.administracion.model.NombreRol;
import fis.dsw.sgc.administracion.model.Usuario;
import fis.dsw.sgc.usuarios.dto.ResidenteFachadaDTO;

import java.util.List;
import java.util.UUID;

public class GestionUsuariosServiceImpl implements IGestionUsuariosAPI {
    private IUsuarioDAO usuarioDAO;
    private ICuentaDAO cuentaDAO;

    public GestionUsuariosServiceImpl() {
        this.usuarioDAO = new UsuarioDAOMySQL();
        this.cuentaDAO = new CuentaDAOMySQL();
    }

    @Override
    public boolean autenticar(String correo, String contrasena) {
        return cuentaDAO.autenticar(correo, contrasena) != null;
    }

    @Override
    public Usuario obtenerUsuarioPorCorreo(String correo) {
        return usuarioDAO.buscarPorCorreo(correo);
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

    @Override
    public ResidenteFachadaDTO obtenerResidentePorCedula(String cedula) {
        ResidenteFachadaDTO residente = usuarioDAO.buscarResidentePorCedula(cedula);
        if (residente == null) {
            throw new ResidenteNoExisteException(cedula);
        }
        return residente;
    }
}