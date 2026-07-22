package fis.dsw.sgc.administracion.service;

import fis.dsw.sgc.administracion.model.NombreRol;
import fis.dsw.sgc.administracion.model.Usuario;

import java.util.List;
import java.util.UUID;

/**
 * Fachada del módulo GRB (Gestión de Usuarios y Administradores).
 * Es la única puerta de entrada que deben usar otros módulos (Finanzas,
 * Reservas, Check-in, Comunicación) para interactuar con este módulo.
 */
public interface IGestionUsuariosAPI {

    boolean autenticar(String correo, String contrasena);

    Usuario obtenerUsuarioPorId(UUID idUsuario);

    boolean validarPermiso(UUID idCuenta, String recurso);

    List<Usuario> listarUsuariosPorRol(NombreRol rol);

    void iniciarRecuperacionContrasena(String correo);
}