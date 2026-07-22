package fis.dsw.sgc.comunicacion.dao;

import fis.dsw.sgc.comunicacion.dto.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface IComunicacionDAO {
    long obtenerIdEmisorPredeterminado() throws SQLException;
    long guardarMensaje(EnviarComunicacionDTO dto) throws SQLException;
    long guardarAnuncio(PublicarAnuncioDTO dto) throws SQLException;
    List<MensajeResumenDTO> listarMensajesRecientes(int limite) throws SQLException;
    List<AnuncioResumenDTO> listarAnunciosRecientes(int limite) throws SQLException;
    List<NotificacionDTO> buscarNotificaciones(String tipo, String estado, String criterio) throws SQLException;
    void marcarNotificacionLeida(long id) throws SQLException;
    void eliminarNotificacion(long id) throws SQLException;
    List<HistorialDTO> buscarHistorial(LocalDate desde, LocalDate hasta, String tipo,
                                       String estado, String criterio) throws SQLException;
    List<ResumenReporteDTO> generarResumen(LocalDate inicio, LocalDate fin, String tipo) throws SQLException;
}
