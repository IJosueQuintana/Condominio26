package fis.dsw.sgc.comunicacion.dao;

import fis.dsw.sgc.conexion_bd.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class ComunicacionSchemaInitializer {

    private ComunicacionSchemaInitializer() {
    }

    public static synchronized void inicializar() throws SQLException {
        Connection connection =
                DBConnection.getInstance().getConnection();

        if (connection == null || connection.isClosed()) {
            throw new SQLException(
                    "No existe una conexión activa con SQLite."
            );
        }

        boolean autoCommitAnterior = connection.getAutoCommit();

        try {
            connection.setAutoCommit(false);

            boolean estadoNotificacionAgregado =
                    actualizarColumnasAntiguas(connection);

            crearHistorialComunicacion(connection);

            if (estadoNotificacionAgregado) {
                actualizarEstadosAntiguos(connection);
            }

            crearIndices(connection);

            connection.commit();

            System.out.println(
                    "Base de datos de Comunicación actualizada correctamente."
            );

        } catch (SQLException exception) {
            connection.rollback();

            throw new SQLException(
                    "No se pudo actualizar la base antigua de Comunicación.",
                    exception
            );

        } finally {
            connection.setAutoCommit(autoCommitAnterior);
        }
    }

    private static boolean actualizarColumnasAntiguas(
            Connection connection
    ) throws SQLException {

        agregarColumnaSiFalta(
                connection,
                "mensaje",
                "prioridad",
                """
                ALTER TABLE mensaje
                ADD COLUMN prioridad TEXT NOT NULL DEFAULT 'NORMAL'
                CHECK (
                    prioridad IN (
                        'BAJA',
                        'NORMAL',
                        'ALTA',
                        'URGENTE'
                    )
                )
                """
        );

        agregarColumnaSiFalta(
                connection,
                "anuncio",
                "tipo",
                """
                ALTER TABLE anuncio
                ADD COLUMN tipo TEXT NOT NULL DEFAULT 'ANUNCIO_GENERAL'
                CHECK (
                    tipo IN (
                        'ANUNCIO_GENERAL',
                        'AVISO_MANTENIMIENTO',
                        'BOLETIN_INFORMATIVO',
                        'ALERTA_EMERGENCIA'
                    )
                )
                """
        );

        return agregarColumnaSiFalta(
                connection,
                "notificacion",
                "estado",
                """
                ALTER TABLE notificacion
                ADD COLUMN estado TEXT NOT NULL DEFAULT 'PENDIENTE'
                CHECK (
                    estado IN (
                        'PENDIENTE',
                        'ENVIADA',
                        'LEIDA',
                        'FALLIDA',
                        'ELIMINADA'
                    )
                )
                """
        );
    }

    private static boolean agregarColumnaSiFalta(
            Connection connection,
            String tabla,
            String columna,
            String sqlAlterTable
    ) throws SQLException {

        if (existeColumna(connection, tabla, columna)) {
            return false;
        }

        try (Statement statement = connection.createStatement()) {
            statement.execute(sqlAlterTable);
        }

        System.out.println(
                "Migración: agregada columna "
                        + tabla + "." + columna
        );

        return true;
    }

    private static boolean existeColumna(
            Connection connection,
            String tabla,
            String columna
    ) throws SQLException {

        String sql = "PRAGMA table_info(" + tabla + ")";

        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)
        ) {
            while (resultSet.next()) {
                String nombre = resultSet.getString("name");

                if (columna.equalsIgnoreCase(nombre)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static void crearHistorialComunicacion(
            Connection connection
    ) throws SQLException {

        String sql = """
                CREATE TABLE IF NOT EXISTS historial_comunicacion (
                    id_historial INTEGER PRIMARY KEY AUTOINCREMENT,

                    entidad_tipo TEXT NOT NULL
                    CHECK (
                        entidad_tipo IN (
                            'MENSAJE',
                            'ANUNCIO',
                            'NOTIFICACION'
                        )
                    ),

                    id_entidad INTEGER,
                    id_usuario INTEGER,

                    accion TEXT NOT NULL
                    CHECK (
                        accion IN (
                            'CREACION',
                            'ENVIO',
                            'MODIFICACION',
                            'ELIMINACION',
                            'NOTIFICACION'
                        )
                    ),

                    asunto TEXT NOT NULL,
                    tipo TEXT,
                    prioridad TEXT,
                    estado TEXT NOT NULL,
                    detalle TEXT,

                    fecha_registro TEXT NOT NULL
                    DEFAULT CURRENT_TIMESTAMP,

                    FOREIGN KEY (id_usuario)
                        REFERENCES usuario(id_usuario)
                        ON UPDATE CASCADE
                        ON DELETE SET NULL
                )
                """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    private static void actualizarEstadosAntiguos(
            Connection connection
    ) throws SQLException {

        String sql = """
                UPDATE notificacion
                SET estado = CASE
                    WHEN leida = 1 THEN 'LEIDA'
                    WHEN fecha_envio IS NOT NULL THEN 'ENVIADA'
                    ELSE 'PENDIENTE'
                END
                """;

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    private static void crearIndices(
            Connection connection
    ) throws SQLException {

        try (Statement statement = connection.createStatement()) {

            statement.execute("""
                    CREATE INDEX IF NOT EXISTS idx_mensaje_emisor
                    ON mensaje(id_emisor)
                    """);

            statement.execute("""
                    CREATE INDEX IF NOT EXISTS idx_mensaje_tipo
                    ON mensaje(tipo)
                    """);

            statement.execute("""
                    CREATE INDEX IF NOT EXISTS idx_mensaje_estado
                    ON mensaje(estado)
                    """);

            statement.execute("""
                    CREATE INDEX IF NOT EXISTS idx_mensaje_fecha_envio
                    ON mensaje(fecha_envio)
                    """);

            statement.execute("""
                    CREATE INDEX IF NOT EXISTS
                    idx_mensaje_destinatario_usuario
                    ON mensaje_destinatario(id_usuario)
                    """);

            statement.execute("""
                    CREATE INDEX IF NOT EXISTS idx_anuncio_autor
                    ON anuncio(id_autor)
                    """);

            statement.execute("""
                    CREATE INDEX IF NOT EXISTS idx_anuncio_tipo
                    ON anuncio(tipo)
                    """);

            statement.execute("""
                    CREATE INDEX IF NOT EXISTS idx_anuncio_estado
                    ON anuncio(estado)
                    """);

            statement.execute("""
                    CREATE INDEX IF NOT EXISTS idx_notificacion_usuario
                    ON notificacion(id_usuario)
                    """);

            statement.execute("""
                    CREATE INDEX IF NOT EXISTS idx_notificacion_estado
                    ON notificacion(estado)
                    """);

            statement.execute("""
                    CREATE INDEX IF NOT EXISTS
                    idx_historial_comunicacion_fecha
                    ON historial_comunicacion(fecha_registro)
                    """);

            statement.execute("""
                    CREATE INDEX IF NOT EXISTS
                    idx_historial_comunicacion_entidad
                    ON historial_comunicacion(
                        entidad_tipo,
                        id_entidad
                    )
                    """);
        }
    }
}