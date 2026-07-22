package fis.dsw.sgc.administracion.service;

import fis.dsw.sgc.administracion.model.NombreRol;
import fis.dsw.sgc.administracion.model.Usuario;
import fis.dsw.sgc.usuarios.dto.ResidenteFachadaDTO;

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

    /**
     * Indica si la cuenta tiene concedido el permiso indicado.
     *
     * Flujo de uso desde otro módulo:
     *   1. Usuario u = SesionUsuario.obtenerInstancia().getUsuarioActual();
     *   2. int idCuenta = u.getCuenta().getIdCuenta();
     *   3. boolean puede = api.validarPermiso(idCuenta, "NOMBRE_PERMISO");
     *
     * El chequeo es por igualdad exacta contra permiso.nombre: se recorren los
     * roles de la cuenta (usuario_rol -> rol_permiso -> permiso) y se devuelve
     * true si alguno concede un permiso con ese nombre. GRB solo verifica
     * existencia; cada módulo define y envía sus propios nombres de permiso.
     *
     * @param idCuenta id_cuenta de la sesión actual
     * @param nombrePermiso nombre único del permiso (p. ej. "RESERVAS_BTN_CANCELAR")
     * @return true si la cuenta tiene el permiso, false en caso contrario
     */
    boolean validarPermiso(int idCuenta, String nombrePermiso);

    /**
     * Lista los nombres de todos los permisos concedidos a la cuenta (por sus roles).
     */
    List<String> obtenerPermisosPorCuenta(int idCuenta);

    List<Usuario> listarUsuariosPorRol(NombreRol rol);

    void iniciarRecuperacionContrasena(String correo);

    ResidenteFachadaDTO obtenerResidentePorCedula(String cedula);
}