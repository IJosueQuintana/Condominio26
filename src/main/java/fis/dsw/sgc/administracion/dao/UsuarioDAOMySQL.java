package fis.dsw.sgc.administracion.dao;

import fis.dsw.sgc.administracion.model.Cuenta;
import fis.dsw.sgc.administracion.model.EstadoCuenta;
import fis.dsw.sgc.administracion.model.NombreRol;
import fis.dsw.sgc.administracion.model.Perfil;
import fis.dsw.sgc.administracion.model.Rol;
import fis.dsw.sgc.administracion.model.Usuario;
import fis.dsw.sgc.conexion_bd.DBConnection;
import fis.dsw.sgc.usuarios.dto.ResidenteFachadaDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOMySQL implements IUsuarioDAO {
    private Connection dbConn;

    @Override
    public void guardar(Usuario usuario) {}

    @Override
    public void actualizar(Usuario usuario) {}

    @Override
    public Usuario buscarPorId(int idUsuario) {
        return null;
    }

    @Override
    public Usuario buscarPorCorreo(String correo) {
        String sql = "SELECT u.nombres, u.apellidos, u.correo, u.telefono, u.id_usuario, u.numero_documento, "
                + "c.id_cuenta, c.estado AS estado_cuenta "
                + "FROM usuario u "
                + "JOIN cuenta c ON c.id_usuario = u.id_usuario "
                + "WHERE u.correo = ?";

        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, correo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setCedula(rs.getString("numero_documento"));
                usuario.setNombre(rs.getString("nombres"));
                usuario.setApellido(rs.getString("apellidos"));
                usuario.setCorreo(rs.getString("correo"));

                Cuenta cuenta = new Cuenta();
                cuenta.setIdCuenta(rs.getInt("id_cuenta"));
                cuenta.setEstado(EstadoCuenta.valueOf(rs.getString("estado_cuenta")));
                cuenta.setRoles(cargarRolesPorUsuario(conn, rs.getInt("id_usuario")));
                usuario.setCuenta(cuenta);

                String telefono = rs.getString("telefono");
                if (telefono != null) {
                    Perfil perfil = new Perfil();
                    perfil.setTelefono(telefono);
                    usuario.setPerfil(perfil);
                }

                return usuario;
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por correo: " + e.getMessage());
            return null;
        }
    }

    private List<Rol> cargarRolesPorUsuario(Connection conn, int idUsuario) throws SQLException {
        List<Rol> roles = new ArrayList<>();
        String sql = "SELECT r.nombre, r.descripcion FROM usuario_rol ur "
                + "JOIN rol r ON r.id_rol = ur.id_rol WHERE ur.id_usuario = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Rol rol = new Rol();
                    rol.setNombre(NombreRol.valueOf(rs.getString("nombre")));
                    rol.setDescripcion(rs.getString("descripcion"));
                    roles.add(rol);
                }
            }
        }
        return roles;
    }

    @Override
    public List<Usuario> listarTodos() {
        return null;
    }

    @Override
    public ResidenteFachadaDTO buscarResidentePorCedula(String cedula) {
        String sql = "SELECT u.id_usuario, u.numero_documento, u.nombres, u.apellidos, u.correo, "
                + "(SELECT ui.id_inmueble FROM usuario_inmueble ui "
                + " WHERE ui.id_usuario = u.id_usuario AND ui.tipo_relacion = 'RESIDENTE' AND ui.estado = 'ACTIVO' "
                + " ORDER BY ui.es_principal DESC, ui.fecha_inicio DESC LIMIT 1) AS id_departamento "
                + "FROM usuario u "
                + "JOIN usuario_rol ur ON ur.id_usuario = u.id_usuario "
                + "JOIN rol r ON r.id_rol = ur.id_rol "
                + "WHERE u.numero_documento = ? AND r.nombre = ? "
                + "LIMIT 1";

        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cedula);
            pstmt.setString(2, NombreRol.RESIDENTE.name());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String nombreCompleto = rs.getString("nombres") + " " + rs.getString("apellidos");
                    int idDepartamento = rs.getInt("id_departamento");
                    Integer idDepartamentoObj = rs.wasNull() ? null : idDepartamento;
                    return new ResidenteFachadaDTO(
                            rs.getInt("id_usuario"),
                            rs.getString("numero_documento"),
                            nombreCompleto,
                            rs.getString("correo"),
                            idDepartamentoObj);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar residente por cédula: " + e.getMessage());
        }
        return null;
    }
}