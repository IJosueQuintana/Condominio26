package fis.dsw.sgc.comunicacion.dto;
public record HistorialDTO(long id, String fecha, String tipo, String prioridad, String asunto,
                           String estado, String emisor, String detalle) {}
