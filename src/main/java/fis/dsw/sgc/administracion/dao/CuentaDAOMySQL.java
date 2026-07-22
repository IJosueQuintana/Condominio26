package fis.dsw.sgc.administracion.dao;

import fis.dsw.sgc.administracion.model.Cuenta;
import fis.dsw.sgc.administracion.model.EstadoCuenta;
import fis.dsw.sgc.administracion.model.NombreRol;
import fis.dsw.sgc.administracion.model.Rol;
import fis.dsw.sgc.administracion.util.PasswordHasher;
import fis.dsw.sgc.conexion_bd.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CuentaDAOMySQL implements ICuentaDAO {
    private Connection dbConn;

    @Override
    public void guardar(Cuenta cuenta) {}

    @Override
    public void actualizar(Cuenta cuenta) {}

    @Override
    public Cuenta buscarPorIdUsuario(int idUsuario) {
        return null;
    }

    @Override
    public Cuenta autenticar(String correo, String contrasena) {
        String sql = "SELECT c.hash_contrasena, c.estado, u.id_usuario "
                + "FROM cuenta c JOIN usuario u ON u.id_usuario = c.id_usuario "
                + "WHERE u.correo = ?";

        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, correo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                String estado = rs.getString("estado");
                if (!EstadoCuenta.ACTIVA.name().equals(estado)) {
                    return null;
                }

                if (!PasswordHasher.verificar(contrasena, rs.getString("hash_contrasena"))) {
                    return null;
                }

                Cuenta cuenta = new Cuenta();
                cuenta.setEstado(EstadoCuenta.valueOf(estado));
                cuenta.setRoles(cargarRolesPorUsuario(conn, rs.getInt("id_usuario")));
                return cuenta;
            }
        } catch (SQLException e) {
            System.err.println("Error al autenticar cuenta: " + e.getMessage());
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
}
