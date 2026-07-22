package fis.dsw.sgc.administracion.service;

import fis.dsw.sgc.administracion.dao.CuentaDAOMySQL;
import fis.dsw.sgc.administracion.dao.ICuentaDAO;
import fis.dsw.sgc.administracion.dao.IPermisoDAO;
import fis.dsw.sgc.administracion.dao.IUsuarioDAO;
import fis.dsw.sgc.administracion.dao.PermisoDAOMySQL;
import fis.dsw.sgc.administracion.dao.UsuarioDAOMySQL;
import fis.dsw.sgc.administracion.exception.ResidenteNoExisteException;
import fis.dsw.sgc.administracion.model.NombreRol;
import fis.dsw.sgc.administracion.model.Usuario;
import fis.dsw.sgc.usuarios.dto.ResidenteFachadaDTO;
import fis.dsw.sgc.usuarios.dto.UsuarioFachadaDTO;

import java.util.List;

public class GestionUsuariosServiceImpl implements IGestionUsuariosAPI {
    private IUsuarioDAO usuarioDAO;
    private ICuentaDAO cuentaDAO;
    private IPermisoDAO permisoDAO;

    public GestionUsuariosServiceImpl() {
        this.usuarioDAO = new UsuarioDAOMySQL();
        this.cuentaDAO = new CuentaDAOMySQL();
        this.permisoDAO = new PermisoDAOMySQL();
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
    public Usuario obtenerUsuarioPorId(int idUsuario) {
        return null;
    }

    @Override
    public boolean validarPermiso(int idCuenta, String nombrePermiso) {
        return permisoDAO.existePermisoParaCuenta(idCuenta, nombrePermiso);
    }

    @Override
    public List<String> obtenerPermisosPorCuenta(int idCuenta) {
        return permisoDAO.listarPermisosPorCuenta(idCuenta);
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

    @Override
    public UsuarioFachadaDTO obtenerUsuarioFachadaPorCorreo(String correo) {
        Usuario u = usuarioDAO.buscarPorCorreo(correo);
        if (u == null) {
            return null;
        }
        return new UsuarioFachadaDTO(u.getIdUsuario(), u.getCedula());
    }
}
