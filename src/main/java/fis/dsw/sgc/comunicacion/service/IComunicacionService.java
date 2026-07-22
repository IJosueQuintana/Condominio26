package fis.dsw.sgc.comunicacion.service;

import fis.dsw.sgc.comunicacion.dto.*;
import java.time.LocalDate;
import java.util.List;

public interface IComunicacionService {
    long obtenerIdEmisorActual();
    long enviarMensaje(EnviarComunicacionDTO dto);
    long publicarAnuncio(PublicarAnuncioDTO dto);
    List<MensajeResumenDTO> obtenerMensajesRecientes(int limite);
    List<AnuncioResumenDTO> obtenerAnunciosRecientes(int limite);
    List<NotificacionDTO> buscarNotificaciones(String tipo, String estado, String criterio);
    void marcarNotificacionLeida(long id);
    void eliminarNotificacion(long id);
    List<HistorialDTO> buscarHistorial(LocalDate desde, LocalDate hasta, String tipo,
                                       String estado, String criterio);
    List<ResumenReporteDTO> generarResumen(LocalDate inicio, LocalDate fin, String tipo);
}
