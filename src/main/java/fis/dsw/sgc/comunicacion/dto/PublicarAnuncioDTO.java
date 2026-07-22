package fis.dsw.sgc.comunicacion.dto;
import java.time.LocalDate;
public record PublicarAnuncioDTO(long idAutor, String tipo, String prioridad, String titulo, String contenido,
                                 LocalDate fechaPublicacion, LocalDate fechaExpiracion) {}
