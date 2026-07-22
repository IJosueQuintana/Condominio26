package fis.dsw.sgc.comunicacion.dao;

import fis.dsw.sgc.comunicacion.dto.*;
import fis.dsw.sgc.comunicacion.util.ComunicacionCatalogos;
import fis.dsw.sgc.conexion_bd.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ComunicacionDAOSQLite implements IComunicacionDAO {

    /**
     * Constructor público para crear el DAO
     * desde la composición manual de dependencias en Main.
     */
    public ComunicacionDAOSQLite() {
    }

    private Connection conexion() throws SQLException {
        Connection c = DBConnection.getInstance().getConnection();

        if (c == null || c.isClosed()) {
            throw new SQLException(
                    "No existe una conexión activa con SQLite."
            );
        }

        return c;
    }

    @Override
    public long obtenerIdEmisorPredeterminado() throws SQLException {
        String sql = """
            SELECT u.id_usuario
            FROM usuario u
            JOIN usuario_rol ur ON ur.id_usuario = u.id_usuario
            JOIN rol r ON r.id_rol = ur.id_rol
            WHERE u.estado='ACTIVO' AND r.nombre='ADMINISTRADOR'
            ORDER BY u.id_usuario LIMIT 1
            """;

        try (PreparedStatement ps = conexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        }

        try (PreparedStatement ps = conexion().prepareStatement(
                "SELECT id_usuario FROM usuario WHERE estado='ACTIVO' ORDER BY id_usuario LIMIT 1");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        }

        throw new SQLException("No existe ningún usuario activo para emitir comunicaciones.");
    }

    @Override
    public long guardarMensaje(EnviarComunicacionDTO dto) throws SQLException {
        Connection c = conexion();
        boolean anterior = c.getAutoCommit();
        c.setAutoCommit(false);

        try {
            long idMensaje;
            String sql = """
                INSERT INTO mensaje
                    (id_emisor, asunto, contenido, tipo, prioridad, fecha_creacion, fecha_envio, estado)
                VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ENVIADO')
                """;

            try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setLong(1, dto.idEmisor());
                ps.setString(2, dto.asunto());
                ps.setString(3, dto.contenido());
                ps.setString(4, dto.tipo());
                ps.setString(5, dto.prioridad());
                ps.executeUpdate();
                idMensaje = idGenerado(ps, "mensaje");
            }

            List<Long> usuarios = destinatarios(c, dto.tipo(), dto.idEmisor());
            for (long idUsuario : usuarios) {
                try (PreparedStatement ps = c.prepareStatement("""
                    INSERT OR IGNORE INTO mensaje_destinatario(id_mensaje,id_usuario,leido,fecha_lectura)
                    VALUES (?, ?, 0, NULL)
                    """)) {
                    ps.setLong(1, idMensaje);
                    ps.setLong(2, idUsuario);
                    ps.executeUpdate();
                }

                insertarNotificacion(c, idUsuario, idMensaje, null,
                        tipoNotificacion(dto.tipo()), dto.asunto(), dto.contenido());
            }

            historial(c, "MENSAJE", idMensaje, dto.idEmisor(), "ENVIO", dto.asunto(),
                    dto.tipo(), dto.prioridad(), "ENVIADO",
                    "Mensaje enviado a " + usuarios.size() + " destinatario(s).");

            c.commit();
            return idMensaje;
        } catch (SQLException e) {
            c.rollback();
            throw e;
        } finally {
            c.setAutoCommit(anterior);
        }
    }

    @Override
    public long guardarAnuncio(PublicarAnuncioDTO dto) throws SQLException {
        Connection c = conexion();
        boolean anterior = c.getAutoCommit();
        c.setAutoCommit(false);

        try {
            long idAnuncio;
            String sql = """
                INSERT INTO anuncio
                    (id_autor, titulo, contenido, fecha_publicacion, fecha_expiracion,
                     prioridad, estado, tipo)
                VALUES (?, ?, ?, ?, ?, ?, 'PUBLICADO', ?)
                """;

            try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setLong(1, dto.idAutor());
                ps.setString(2, dto.titulo());
                ps.setString(3, dto.contenido());
                ps.setString(4, dto.fechaPublicacion() + " 00:00:00");
                ps.setString(5, dto.fechaExpiracion()==null ? null : dto.fechaExpiracion()+" 23:59:59");
                ps.setString(6, dto.prioridad());
                ps.setString(7, dto.tipo());
                ps.executeUpdate();
                idAnuncio = idGenerado(ps, "anuncio");
            }

            List<Long> usuarios = todosActivos(c);
            for (long idUsuario : usuarios) {
                insertarNotificacion(c, idUsuario, null, idAnuncio,
                        "ANUNCIO", dto.titulo(), dto.contenido());
            }

            historial(c, "ANUNCIO", idAnuncio, dto.idAutor(), "CREACION", dto.titulo(),
                    dto.tipo(), dto.prioridad(), "PUBLICADO",
                    "Anuncio publicado para " + usuarios.size() + " usuario(s).");

            c.commit();
            return idAnuncio;
        } catch (SQLException e) {
            c.rollback();
            throw e;
        } finally {
            c.setAutoCommit(anterior);
        }
    }

    @Override
    public List<MensajeResumenDTO> listarMensajesRecientes(int limite) throws SQLException {
        String sql = """
            SELECT id_mensaje,
                   strftime('%d/%m/%Y %H:%M',COALESCE(fecha_envio,fecha_creacion)) fecha,
                   tipo, prioridad, asunto, estado
            FROM mensaje
            ORDER BY COALESCE(fecha_envio,fecha_creacion) DESC
            LIMIT ?
            """;

        List<MensajeResumenDTO> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            ps.setInt(1, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new MensajeResumenDTO(
                            rs.getLong("id_mensaje"),
                            seguro(rs.getString("fecha")),
                            ComunicacionCatalogos.etiquetaTipo(rs.getString("tipo")),
                            ComunicacionCatalogos.etiquetaPrioridad(rs.getString("prioridad")),
                            rs.getString("asunto"),
                            ComunicacionCatalogos.etiquetaEstado(rs.getString("estado"))));
                }
            }
        }
        return lista;
    }

    @Override
    public List<AnuncioResumenDTO> listarAnunciosRecientes(int limite) throws SQLException {
        String sql = """
            SELECT id_anuncio,
                   strftime('%d/%m/%Y %H:%M',fecha_publicacion) fecha,
                   tipo, prioridad, titulo,
                   strftime('%d/%m/%Y',fecha_publicacion)||' - '||
                       COALESCE(strftime('%d/%m/%Y',fecha_expiracion),'Sin expiración') vigencia,
                   estado
            FROM anuncio
            ORDER BY fecha_publicacion DESC
            LIMIT ?
            """;

        List<AnuncioResumenDTO> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            ps.setInt(1, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new AnuncioResumenDTO(
                            rs.getLong("id_anuncio"),
                            seguro(rs.getString("fecha")),
                            ComunicacionCatalogos.etiquetaTipo(rs.getString("tipo")),
                            ComunicacionCatalogos.etiquetaPrioridad(rs.getString("prioridad")),
                            rs.getString("titulo"),
                            rs.getString("vigencia"),
                            ComunicacionCatalogos.etiquetaEstado(rs.getString("estado"))));
                }
            }
        }
        return lista;
    }

    @Override
    public List<NotificacionDTO> buscarNotificaciones(String tipo, String estado, String criterio)
            throws SQLException {
        StringBuilder sql = new StringBuilder("""
            SELECT n.id_notificacion,
                   strftime('%d/%m/%Y %H:%M',n.fecha_creacion) fecha,
                   n.tipo,
                   TRIM(u.nombres||' '||u.apellidos) destinatario,
                   n.titulo, n.estado
            FROM notificacion n
            JOIN usuario u ON u.id_usuario=n.id_usuario
            WHERE 1=1
            """);
        List<Object> p = new ArrayList<>();

        if (tipo!=null && !tipo.isBlank()) { sql.append(" AND n.tipo=?"); p.add(tipo); }
        if (estado!=null && !estado.isBlank()) { sql.append(" AND n.estado=?"); p.add(estado); }
        else sql.append(" AND n.estado<>'ELIMINADA'");

        if (criterio!=null && !criterio.isBlank()) {
            sql.append("""
                AND (LOWER(n.titulo) LIKE ? OR
                     LOWER(u.nombres||' '||u.apellidos) LIKE ? OR
                     LOWER(n.contenido) LIKE ?)
                """);
            String patron="%"+criterio.trim().toLowerCase(Locale.ROOT)+"%";
            p.add(patron); p.add(patron); p.add(patron);
        }

        sql.append(" ORDER BY n.fecha_creacion DESC");
        List<NotificacionDTO> lista=new ArrayList<>();

        try (PreparedStatement ps=conexion().prepareStatement(sql.toString())) {
            parametros(ps,p);
            try (ResultSet rs=ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new NotificacionDTO(
                            rs.getLong("id_notificacion"),
                            seguro(rs.getString("fecha")),
                            ComunicacionCatalogos.etiquetaTipo(rs.getString("tipo")),
                            rs.getString("destinatario"),
                            rs.getString("titulo"),
                            ComunicacionCatalogos.etiquetaEstado(rs.getString("estado"))));
                }
            }
        }
        return lista;
    }

    @Override
    public void marcarNotificacionLeida(long id) throws SQLException {
        try (PreparedStatement ps=conexion().prepareStatement("""
            UPDATE notificacion
            SET leida=1, fecha_lectura=CURRENT_TIMESTAMP, estado='LEIDA'
            WHERE id_notificacion=?
            """)) {
            ps.setLong(1,id);
            if (ps.executeUpdate()==0) throw new SQLException("La notificación ya no existe.");
        }
    }

    @Override
    public void eliminarNotificacion(long id) throws SQLException {
        Connection c=conexion();
        boolean anterior=c.getAutoCommit();
        c.setAutoCommit(false);
        try {
            long idUsuario;
            String titulo,tipo;
            try (PreparedStatement ps=c.prepareStatement(
                    "SELECT id_usuario,titulo,tipo FROM notificacion WHERE id_notificacion=?")) {
                ps.setLong(1,id);
                try (ResultSet rs=ps.executeQuery()) {
                    if (!rs.next()) throw new SQLException("La notificación ya no existe.");
                    idUsuario=rs.getLong("id_usuario");
                    titulo=rs.getString("titulo");
                    tipo=rs.getString("tipo");
                }
            }
            try (PreparedStatement ps=c.prepareStatement(
                    "UPDATE notificacion SET estado='ELIMINADA' WHERE id_notificacion=?")) {
                ps.setLong(1,id); ps.executeUpdate();
            }
            historial(c,"NOTIFICACION",id,idUsuario,"ELIMINACION",titulo,tipo,"NORMAL",
                    "ELIMINADA","Eliminación lógica.");
            c.commit();
        } catch (SQLException e) {
            c.rollback(); throw e;
        } finally {
            c.setAutoCommit(anterior);
        }
    }

    @Override
    public List<HistorialDTO> buscarHistorial(LocalDate desde, LocalDate hasta, String tipo,
                                              String estado, String criterio) throws SQLException {
        StringBuilder sql=new StringBuilder("""
            SELECT h.id_historial,
                   strftime('%d/%m/%Y %H:%M',h.fecha_registro) fecha,
                   h.entidad_tipo,h.tipo,h.prioridad,h.asunto,h.estado,
                   COALESCE(TRIM(u.nombres||' '||u.apellidos),'Sistema') emisor,
                   COALESCE(h.detalle,'') detalle
            FROM historial_comunicacion h
            LEFT JOIN usuario u ON u.id_usuario=h.id_usuario
            WHERE date(h.fecha_registro) BETWEEN ? AND ?
            """);
        List<Object> p=new ArrayList<>();
        p.add(desde.toString()); p.add(hasta.toString());

        if (tipo!=null && !tipo.equalsIgnoreCase("Todos")) {
            switch (tipo) {
                case "Mensaje" -> sql.append(" AND h.entidad_tipo='MENSAJE'");
                case "Anuncio" -> sql.append(" AND h.entidad_tipo='ANUNCIO'");
                case "Notificación" -> sql.append(" AND h.entidad_tipo='NOTIFICACION'");
                case "Alerta" -> sql.append(" AND h.tipo='ALERTA_EMERGENCIA'");
                case "Boletín" -> sql.append(" AND h.tipo='BOLETIN_INFORMATIVO'");
                default -> { sql.append(" AND h.tipo=?"); p.add(tipo); }
            }
        }

        if (estado!=null && !estado.equalsIgnoreCase("Todos")) {
            sql.append(" AND UPPER(h.estado)=?");
            p.add(normalizar(estado));
        }

        if (criterio!=null && !criterio.isBlank()) {
            sql.append("""
                AND (LOWER(h.asunto) LIKE ? OR LOWER(COALESCE(h.detalle,'')) LIKE ? OR
                     LOWER(COALESCE(u.nombres||' '||u.apellidos,'sistema')) LIKE ?)
                """);
            String patron="%"+criterio.trim().toLowerCase(Locale.ROOT)+"%";
            p.add(patron); p.add(patron); p.add(patron);
        }

        sql.append(" ORDER BY h.fecha_registro DESC");
        List<HistorialDTO> lista=new ArrayList<>();

        try (PreparedStatement ps=conexion().prepareStatement(sql.toString())) {
            parametros(ps,p);
            try (ResultSet rs=ps.executeQuery()) {
                while (rs.next()) {
                    String tipoDb=rs.getString("tipo");
                    lista.add(new HistorialDTO(
                            rs.getLong("id_historial"),
                            seguro(rs.getString("fecha")),
                            tipoDb==null||tipoDb.isBlank()
                                    ? rs.getString("entidad_tipo")
                                    : ComunicacionCatalogos.etiquetaTipo(tipoDb),
                            ComunicacionCatalogos.etiquetaPrioridad(rs.getString("prioridad")),
                            rs.getString("asunto"),
                            ComunicacionCatalogos.etiquetaEstado(rs.getString("estado")),
                            rs.getString("emisor"),
                            rs.getString("detalle")));
                }
            }
        }
        return lista;
    }

    @Override
    public List<ResumenReporteDTO> generarResumen(LocalDate inicio, LocalDate fin, String tipo)
            throws SQLException {
        List<ResumenReporteDTO> r=new ArrayList<>();
        if (tipo==null||tipo.equals("Todos")||tipo.equals("Mensajes"))
            r.add(resumen("Mensajes","mensaje","fecha_creacion",
                    "estado='ENVIADO'","estado='CANCELADO'",inicio,fin,null));
        if (tipo==null||tipo.equals("Todos")||tipo.equals("Anuncios"))
            r.add(resumen("Anuncios","anuncio","fecha_publicacion",
                    "estado IN ('PUBLICADO','EXPIRADO')","estado='CANCELADO'",inicio,fin,null));
        if (tipo==null||tipo.equals("Todos")||tipo.equals("Notificaciones"))
            r.add(resumen("Notificaciones","notificacion","fecha_creacion",
                    "estado IN ('ENVIADA','LEIDA')","estado='FALLIDA'",inicio,fin,
                    "estado<>'ELIMINADA'"));
        if ("Alertas".equals(tipo))
            r.add(resumen("Alertas","mensaje","fecha_creacion",
                    "estado='ENVIADO'","estado='CANCELADO'",inicio,fin,
                    "tipo='ALERTA_EMERGENCIA'"));
        if ("Boletines".equals(tipo))
            r.add(resumen("Boletines","mensaje","fecha_creacion",
                    "estado='ENVIADO'","estado='CANCELADO'",inicio,fin,
                    "tipo='BOLETIN_INFORMATIVO'"));
        return r;
    }

    private ResumenReporteDTO resumen(String etiqueta,String tabla,String fecha,String ok,String mal,
                                      LocalDate inicio,LocalDate fin,String extra) throws SQLException {
        String sql="SELECT COUNT(*) cantidad,"+
                "SUM(CASE WHEN "+ok+" THEN 1 ELSE 0 END) enviadas,"+
                "SUM(CASE WHEN "+mal+" THEN 1 ELSE 0 END) fallidas "+
                "FROM "+tabla+" WHERE date("+fecha+") BETWEEN ? AND ?"+
                (extra==null?"":" AND "+extra);
        try (PreparedStatement ps=conexion().prepareStatement(sql)) {
            ps.setString(1,inicio.toString()); ps.setString(2,fin.toString());
            try (ResultSet rs=ps.executeQuery()) {
                rs.next();
                int cantidad=rs.getInt("cantidad"), enviadas=rs.getInt("enviadas"),
                        fallidas=rs.getInt("fallidas");
                double tasa=cantidad==0?0.0:enviadas*100.0/cantidad;
                return new ResumenReporteDTO(etiqueta,cantidad,enviadas,fallidas,tasa);
            }
        }
    }

    private void insertarNotificacion(Connection c,long idUsuario,Long idMensaje,Long idAnuncio,
                                      String tipo,String titulo,String contenido) throws SQLException {
        try (PreparedStatement ps=c.prepareStatement("""
            INSERT INTO notificacion
                (id_usuario,id_mensaje,id_anuncio,tipo,titulo,contenido,
                 fecha_creacion,fecha_envio,leida,fecha_lectura,estado)
            VALUES (?,?,?,?,?,?,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0,NULL,'ENVIADA')
            """)) {
            ps.setLong(1,idUsuario);
            if (idMensaje==null) ps.setNull(2,Types.INTEGER); else ps.setLong(2,idMensaje);
            if (idAnuncio==null) ps.setNull(3,Types.INTEGER); else ps.setLong(3,idAnuncio);
            ps.setString(4,tipo); ps.setString(5,titulo); ps.setString(6,contenido);
            ps.executeUpdate();
        }
    }

    private List<Long> destinatarios(Connection c,String tipo,long emisor) throws SQLException {
        List<Long> r;
        if ("MENSAJE_RESIDENTES".equals(tipo))
            r=porRoles(c,List.of("RESIDENTE","PROPIETARIO"));
        else if ("COMUNICADO_TRABAJADORES".equals(tipo))
            r=porRoles(c,List.of("ADMINISTRADOR","PERSONAL_SEGURIDAD","PRESIDENTE"));
        else r=todosActivos(c);
        if (r.isEmpty()) r.add(emisor);
        return r;
    }

    private List<Long> porRoles(Connection c,List<String> roles) throws SQLException {
        String marcas=String.join(",",roles.stream().map(x->"?").toList());
        String sql=("SELECT DISTINCT u.id_usuario FROM usuario u "+
                "JOIN usuario_rol ur ON ur.id_usuario=u.id_usuario "+
                "JOIN rol r ON r.id_rol=ur.id_rol "+
                "WHERE u.estado='ACTIVO' AND r.nombre IN ("+marcas+") ORDER BY u.id_usuario");
        List<Long> ids=new ArrayList<>();
        try (PreparedStatement ps=c.prepareStatement(sql)) {
            for (int i=0;i<roles.size();i++) ps.setString(i+1,roles.get(i));
            try (ResultSet rs=ps.executeQuery()) { while (rs.next()) ids.add(rs.getLong(1)); }
        }
        return ids;
    }

    private List<Long> todosActivos(Connection c) throws SQLException {
        List<Long> ids=new ArrayList<>();
        try (PreparedStatement ps=c.prepareStatement(
                "SELECT id_usuario FROM usuario WHERE estado='ACTIVO' ORDER BY id_usuario");
             ResultSet rs=ps.executeQuery()) {
            while (rs.next()) ids.add(rs.getLong(1));
        }
        return ids;
    }

    private void historial(Connection c,String entidad,long idEntidad,Long idUsuario,String accion,
                           String asunto,String tipo,String prioridad,String estado,String detalle)
            throws SQLException {
        try (PreparedStatement ps=c.prepareStatement("""
            INSERT INTO historial_comunicacion
                (entidad_tipo,id_entidad,id_usuario,accion,asunto,tipo,prioridad,estado,detalle,fecha_registro)
            VALUES (?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP)
            """)) {
            ps.setString(1,entidad); ps.setLong(2,idEntidad);
            if (idUsuario==null) ps.setNull(3,Types.INTEGER); else ps.setLong(3,idUsuario);
            ps.setString(4,accion); ps.setString(5,asunto); ps.setString(6,tipo);
            ps.setString(7,prioridad); ps.setString(8,estado); ps.setString(9,detalle);
            ps.executeUpdate();
        }
    }

    private long idGenerado(PreparedStatement ps,String entidad) throws SQLException {
        try (ResultSet rs=ps.getGeneratedKeys()) { if (rs.next()) return rs.getLong(1); }
        throw new SQLException("No se obtuvo el id generado de "+entidad+".");
    }

    private void parametros(PreparedStatement ps,List<Object> p) throws SQLException {
        for (int i=0;i<p.size();i++) ps.setObject(i+1,p.get(i));
    }

    private String tipoNotificacion(String tipo) {
        return switch (tipo) {
            case "ALERTA_EMERGENCIA","MENSAJE_URGENTE" -> "ALERTA";
            case "BOLETIN_INFORMATIVO" -> "RECORDATORIO";
            default -> "MENSAJE";
        };
    }

    private String seguro(String s) { return s==null?"":s; }

    private String normalizar(String s) {
        return s.toUpperCase(Locale.ROOT).replace('Í','I').replace('É','E')
                .replace('Á','A').replace('Ó','O').replace('Ú','U').replace('Ñ','N');
    }
}
