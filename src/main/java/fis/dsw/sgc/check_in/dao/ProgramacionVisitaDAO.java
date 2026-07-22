package fis.dsw.sgc.check_in.dao;
import fis.dsw.sgc.check_in.model.Usuario_Checkin;
import fis.dsw.sgc.check_in.model.VisitaProgramada;
import fis.dsw.sgc.conexion_bd.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProgramacionVisitaDAO implements IProgramacionVisitaDAO {
    private final Connection conexion;

    public ProgramacionVisitaDAO() {
        this.conexion = DBConnection.getInstance().getConnection();

    }

    @Override
    public boolean programarVisita(VisitaProgramada visita) {
        String sql = """
        INSERT INTO visitas_programadas(
            id_residente, nombres_visita, apellidos_visita, cedula_visita,
            fecha_programada, hora_programada, motivo_visita, tipo_visita, informacion_adicional, estado, placa_vehiculo)
        VALUES(?,?,?,?,?,?,?,?,?,?,?)
    """;
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            // setObject permite guardar un Integer o un NULL dinámicamente
            ps.setObject(1, visita.getIdResidente(), java.sql.Types.INTEGER);

            ps.setString(2, visita.getNombresVisita());
            ps.setString(3, visita.getApellidosVisita());
            ps.setString(4, visita.getCedulaVisita());
            ps.setString(5, visita.getFechaProgramada());
            ps.setString(6, visita.getHoraProgramada());
            ps.setString(7, visita.getMotivoVisita());
            ps.setString(8, visita.getTipoVisita());
            ps.setString(9, visita.getInformacionAdicional());
            ps.setString(10, visita.getEstado());
            ps.setString(11, visita.getPlaca());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean actualizarFechaHora(int idVisita, String nuevaFecha, String nuevaHora) {
        // Se cambió el "AND" por una "," en la cláusula SET
        String sql = "UPDATE visitas_programadas SET fecha_programada=?, hora_programada=? WHERE id_visita=?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nuevaFecha);
            ps.setString(2, nuevaHora);
            ps.setInt(3, idVisita);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<VisitaProgramada> obtenerVisitasProgramadas() {
        String sql = "SELECT * FROM visitas_programadas";
        List<VisitaProgramada> visitas = new ArrayList<>();

        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                VisitaProgramada visita = new VisitaProgramada();

                visita.setIdVisita(rs.getInt("id_visita"));
                visita.setIdResidente(rs.getInt("id_residente"));
                visita.setNombresVisita(rs.getString("nombres_visita"));
                visita.setApellidosVisita(rs.getString("apellidos_visita"));
                visita.setCedulaVisita(rs.getString("cedula_visita"));
                visita.setFechaProgramada(rs.getString("fecha_programada"));
                visita.setHoraProgramada(rs.getString("hora_programada"));
                visita.setPlaca(rs.getString("placa_vehiculo"));
                visita.setEstado(rs.getString("estado"));
                visita.setMotivoVisita(rs.getString("motivo_visita"));
                visita.setTipoVisita(rs.getString("tipo_visita"));
                visita.setInformacionAdicional(rs.getString("informacion_adicional"));
                visitas.add(visita);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return visitas;
    }

    @Override
    public boolean cancelarVisitaProgramada(Integer idVisita) {
        String sql = """
            UPDATE visitas_programadas set estado='CANCELADA' where id_visita = ?;
        """;
        try (PreparedStatement ps = conexion.prepareStatement(sql)){
            ps.setInt(1, idVisita);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Usuario_Checkin> obtenerResidentes() {
        String sql = """
            SELECT\s
                u.id_usuario, u.numero_documento, u.nombres, u.apellidos, u.correo, u.telefono, u.estado,
                u.fecha_registro, u.fecha_actualizacion,
                (e.nombre || ' - ' || i.codigo) AS lugar_residencia
            FROM usuario u
            JOIN usuario_inmueble ui ON u.id_usuario = ui.id_usuario
            JOIN inmueble i ON ui.id_inmueble = i.id_inmueble
            LEFT JOIN edificio e ON i.id_edificio = e.id_edificio
            WHERE u.estado = 'ACTIVO'
              AND ui.estado = 'ACTIVO'
              AND ui.tipo_relacion IN ('RESIDENTE', 'PROPIETARIO', 'ARRENDATARIO');
        """;
        List<Usuario_Checkin> residentes = new ArrayList<>();

        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Usuario_Checkin usuario = new Usuario_Checkin();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombres(rs.getString("nombres"));
                usuario.setApellidos(rs.getString("apellidos"));
                usuario.setNumeroDocumento(rs.getString("numero_documento"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setEstado(rs.getString("estado"));
                usuario.setFechaRegistro(rs.getString("fecha_registro"));
                usuario.setFechaActualizacion(rs.getString("fecha_actualizacion"));
                usuario.setVivienda(rs.getString("lugar_residencia"));
                residentes.add(usuario);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return residentes;
    }
}

