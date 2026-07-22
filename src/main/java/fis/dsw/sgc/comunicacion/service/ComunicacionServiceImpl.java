package fis.dsw.sgc.comunicacion.service;

import fis.dsw.sgc.comunicacion.dao.ComunicacionSchemaInitializer;
import fis.dsw.sgc.comunicacion.dao.IComunicacionDAO;
import fis.dsw.sgc.comunicacion.dto.AnuncioResumenDTO;
import fis.dsw.sgc.comunicacion.dto.EnviarComunicacionDTO;
import fis.dsw.sgc.comunicacion.dto.HistorialDTO;
import fis.dsw.sgc.comunicacion.dto.MensajeResumenDTO;
import fis.dsw.sgc.comunicacion.dto.NotificacionDTO;
import fis.dsw.sgc.comunicacion.dto.PublicarAnuncioDTO;
import fis.dsw.sgc.comunicacion.dto.ResumenReporteDTO;
import fis.dsw.sgc.comunicacion.exception.ComunicacionException;
import fis.dsw.sgc.comunicacion.strategy.ComunicacionStrategyResolver;
import fis.dsw.sgc.comunicacion.strategy.IComunicacionStrategyResolver;
import fis.dsw.sgc.comunicacion.util.ComunicacionCatalogos;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class ComunicacionServiceImpl
        implements IComunicacionService {

    private final IComunicacionDAO dao;
    private final IComunicacionStrategyResolver strategyResolver;

    /**
     * Mantiene compatible la composición actual del proyecto:
     * new ComunicacionServiceImpl(dao).
     */
    public ComunicacionServiceImpl(IComunicacionDAO dao) {
        this(
                dao,
                ComunicacionStrategyResolver.porDefecto()
        );
    }

    /**
     * Constructor completamente inyectable para pruebas o composición manual.
     */
    public ComunicacionServiceImpl(
            IComunicacionDAO dao,
            IComunicacionStrategyResolver strategyResolver
    ) {
        this.dao = Objects.requireNonNull(
                dao,
                "El DAO de Comunicación no puede ser nulo."
        );

        this.strategyResolver = Objects.requireNonNull(
                strategyResolver,
                "El resolver de estrategias no puede ser nulo."
        );

        inicializarEsquemaComunicacion();
    }

    private void inicializarEsquemaComunicacion() {
        try {
            ComunicacionSchemaInitializer.inicializar();
        } catch (SQLException exception) {
            throw new ComunicacionException(
                    "No se pudo preparar Comunicación en SQLite: "
                            + exception.getMessage(),
                    exception
            );
        }
    }

    @Override
    public long obtenerIdEmisorActual() {
        try {
            return dao.obtenerIdEmisorPredeterminado();

        } catch (SQLException exception) {
            throw error(
                    "No se pudo determinar el emisor.",
                    exception
            );
        }
    }

    @Override
    public long enviarMensaje(
            EnviarComunicacionDTO dto
    ) {
        validarMensaje(dto);

        strategyResolver
                .obtener(dto.tipo())
                .validar(dto);

        try {
            return dao.guardarMensaje(dto);

        } catch (SQLException exception) {
            throw error(
                    "No se pudo enviar el mensaje.",
                    exception
            );
        }
    }

    @Override
    public long publicarAnuncio(
            PublicarAnuncioDTO dto
    ) {
        validarAnuncio(dto);

        try {
            return dao.guardarAnuncio(dto);

        } catch (SQLException exception) {
            throw error(
                    "No se pudo publicar el anuncio.",
                    exception
            );
        }
    }

    @Override
    public List<MensajeResumenDTO> obtenerMensajesRecientes(
            int limite
    ) {
        try {
            return dao.listarMensajesRecientes(
                    Math.max(1, limite)
            );

        } catch (SQLException exception) {
            throw error(
                    "No se cargaron los mensajes.",
                    exception
            );
        }
    }

    @Override
    public List<AnuncioResumenDTO> obtenerAnunciosRecientes(
            int limite
    ) {
        try {
            return dao.listarAnunciosRecientes(
                    Math.max(1, limite)
            );

        } catch (SQLException exception) {
            throw error(
                    "No se cargaron los anuncios.",
                    exception
            );
        }
    }

    @Override
    public List<NotificacionDTO> buscarNotificaciones(
            String tipo,
            String estado,
            String criterio
    ) {
        try {
            String codigoTipo =
                    ComunicacionCatalogos.codigoTipoNotificacion(
                            tipo
                    );

            String codigoEstado =
                    ComunicacionCatalogos.codigoEstadoNotificacion(
                            estado
                    );

            return dao.buscarNotificaciones(
                    codigoTipo,
                    codigoEstado,
                    criterio
            );

        } catch (SQLException exception) {
            throw error(
                    "No se cargaron las notificaciones.",
                    exception
            );
        }
    }

    @Override
    public void marcarNotificacionLeida(
            long id
    ) {
        validarId(id);

        try {
            dao.marcarNotificacionLeida(id);

        } catch (SQLException exception) {
            throw error(
                    "No se marcó la notificación.",
                    exception
            );
        }
    }

    @Override
    public void eliminarNotificacion(
            long id
    ) {
        validarId(id);

        try {
            dao.eliminarNotificacion(id);

        } catch (SQLException exception) {
            throw error(
                    "No se eliminó la notificación.",
                    exception
            );
        }
    }

    @Override
    public List<HistorialDTO> buscarHistorial(
            LocalDate desde,
            LocalDate hasta,
            String tipo,
            String estado,
            String criterio
    ) {
        validarRango(desde, hasta);

        try {
            return dao.buscarHistorial(
                    desde,
                    hasta,
                    tipo,
                    estado,
                    criterio
            );

        } catch (SQLException exception) {
            throw error(
                    "No se consultó el historial.",
                    exception
            );
        }
    }

    @Override
    public List<ResumenReporteDTO> generarResumen(
            LocalDate inicio,
            LocalDate fin,
            String tipo
    ) {
        validarRango(inicio, fin);

        try {
            return dao.generarResumen(
                    inicio,
                    fin,
                    tipo
            );

        } catch (SQLException exception) {
            throw error(
                    "No se generó el resumen.",
                    exception
            );
        }
    }

    private void validarMensaje(
            EnviarComunicacionDTO dto
    ) {
        if (dto == null) {
            throw new ComunicacionException(
                    "Los datos del mensaje son obligatorios."
            );
        }

        validarId(dto.idEmisor());

        validarTexto(
                dto.tipo(),
                1,
                60,
                "tipo"
        );

        validarTexto(
                dto.prioridad(),
                1,
                20,
                "prioridad"
        );

        validarPrioridad(dto.prioridad());

        validarTexto(
                dto.asunto(),
                5,
                120,
                "asunto"
        );

        validarTexto(
                dto.contenido(),
                10,
                2000,
                "contenido"
        );
    }

    private void validarAnuncio(
            PublicarAnuncioDTO dto
    ) {
        if (dto == null) {
            throw new ComunicacionException(
                    "Los datos del anuncio son obligatorios."
            );
        }

        validarId(dto.idAutor());

        validarTexto(
                dto.tipo(),
                1,
                60,
                "tipo"
        );

        validarTexto(
                dto.prioridad(),
                1,
                20,
                "prioridad"
        );

        validarPrioridad(dto.prioridad());

        validarTexto(
                dto.titulo(),
                5,
                120,
                "título"
        );

        validarTexto(
                dto.contenido(),
                10,
                2000,
                "contenido"
        );

        if (dto.fechaPublicacion() == null) {
            throw new ComunicacionException(
                    "La fecha de publicación es obligatoria."
            );
        }

        if (
                dto.fechaExpiracion() != null
                        && dto.fechaExpiracion().isBefore(
                        dto.fechaPublicacion()
                )
        ) {
            throw new ComunicacionException(
                    "La fecha de expiración no puede ser "
                            + "anterior a la fecha de publicación."
            );
        }
    }

    private void validarPrioridad(
            String prioridad
    ) {
        switch (prioridad.trim().toUpperCase()) {
            case "BAJA", "NORMAL", "ALTA", "URGENTE" -> {
                // Prioridad permitida.
            }

            default -> throw new ComunicacionException(
                    "La prioridad debe ser BAJA, NORMAL, "
                            + "ALTA o URGENTE."
            );
        }
    }

    private void validarRango(
            LocalDate inicio,
            LocalDate fin
    ) {
        if (inicio == null || fin == null) {
            throw new ComunicacionException(
                    "Debe seleccionar el rango completo."
            );
        }

        if (fin.isBefore(inicio)) {
            throw new ComunicacionException(
                    "La fecha final no puede ser anterior "
                            + "a la fecha inicial."
            );
        }
    }

    private void validarId(
            long id
    ) {
        if (id <= 0) {
            throw new ComunicacionException(
                    "El identificador no es válido."
            );
        }
    }

    private void validarTexto(
            String valor,
            int minimo,
            int maximo,
            String nombre
    ) {
        if (valor == null || valor.isBlank()) {
            throw new ComunicacionException(
                    "El campo " + nombre + " es obligatorio."
            );
        }

        int longitud = valor.trim().length();

        if (longitud < minimo || longitud > maximo) {
            throw new ComunicacionException(
                    "El campo " + nombre
                            + " debe tener entre "
                            + minimo + " y "
                            + maximo + " caracteres."
            );
        }
    }

    private ComunicacionException error(
            String mensaje,
            SQLException exception
    ) {
        return new ComunicacionException(
                mensaje
                        + " Detalle: "
                        + exception.getMessage(),
                exception
        );
    }
}
