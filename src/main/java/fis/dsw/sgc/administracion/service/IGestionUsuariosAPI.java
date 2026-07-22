package fis.dsw.sgc.administracion.service;

import fis.dsw.sgc.administracion.model.NombreRol;
import fis.dsw.sgc.administracion.model.Usuario;
import fis.dsw.sgc.usuarios.dto.ResidenteFachadaDTO;
import fis.dsw.sgc.usuarios.dto.UsuarioFachadaDTO;

import java.util.List;

/**
 * Fachada del módulo GRB (Gestión de Usuarios y Administradores).
 * Es la única puerta de entrada que deben usar otros módulos (Finanzas,
 * Reservas, Check-in, Comunicación) para interactuar con este módulo.
 */
public interface IGestionUsuariosAPI {

    boolean autenticar(String correo, String contrasena);

    Usuario obtenerUsuarioPorCorreo(String correo);

    Usuario obtenerUsuarioPorId(int idUsuario);

    boolean validarPermiso(int idCuenta, String nombrePermiso);

    List<String> obtenerPermisosPorCuenta(int idCuenta);

    List<Usuario> listarUsuariosPorRol(NombreRol rol);

    void iniciarRecuperacionContrasena(String correo);

    ResidenteFachadaDTO obtenerResidentePorCedula(String cedula);

    /**
     * Devuelve el id numérico y la cédula del usuario asociado a un correo.
     * Pensado para módulos externos (Finanzas, Reservas, etc.) que solo
     * tienen el correo disponible desde SesionUsuario y necesitan el
     * identificador real para facturar u operar sobre otras tablas.
     *
     * @param correo correo del usuario logueado
     * @return DTO con idUsuario y cedula, o null si no existe
     */
    UsuarioFachadaDTO obtenerUsuarioFachadaPorCorreo(String correo);
}
