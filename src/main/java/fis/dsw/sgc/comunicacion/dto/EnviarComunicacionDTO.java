package fis.dsw.sgc.comunicacion.dto;
public record EnviarComunicacionDTO(long idEmisor, Long idReceptor, String tipo, String prioridad, String asunto, String contenido) {}
