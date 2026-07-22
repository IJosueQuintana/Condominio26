package fis.dsw.sgc.reservas.model;

import fis.dsw.sgc.reservas.dto.ObservacionReservaDTO;

/**
 * Observacion contenida en una Reserva (relacion de COMPOSICION).
 * Se corresponde con la tabla observacion_reserva y con ObservacionReservaDTO.
 *
 * idAutor referencia al Usuario (GRB) que escribio la observacion
 * (columna id_usuario en la BD / DTO).
 */
public class Observacion {

    private int id;             // id_observacion
    private int idReserva;      // id_reserva (reserva a la que pertenece)
    private int idAutor;        // id_usuario (autor de la observacion)
    private String texto;       // texto
    private String fechaHora;   // fecha_hora (la asigna la BD)

    // Auxiliar de presentacion (viene del JOIN con usuario)
    private String nombreAutor; // nombre_usuario

    public Observacion() {
    }

    public Observacion(int idReserva, int idAutor, String texto) {
        this.idReserva = idReserva;
        this.idAutor = idAutor;
        this.texto = texto;
    }

    public static Observacion desdeDTO(ObservacionReservaDTO dto) {
        Observacion obs = new Observacion();
        obs.id = dto.getIdObservacion();
        obs.idReserva = dto.getIdReserva();
        obs.idAutor = dto.getIdUsuario();
        obs.texto = dto.getTexto();
        obs.fechaHora = dto.getFechaHora();
        obs.nombreAutor = dto.getNombreUsuario();
        return obs;
    }

    public ObservacionReservaDTO aDTO() {
        ObservacionReservaDTO dto = new ObservacionReservaDTO(idReserva, idAutor, texto);
        dto.setIdObservacion(id);
        dto.setFechaHora(fechaHora);
        dto.setNombreUsuario(nombreAutor);
        return dto;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdReserva() { return idReserva; }
    public void setIdReserva(int idReserva) { this.idReserva = idReserva; }

    public int getIdAutor() { return idAutor; }
    public void setIdAutor(int idAutor) { this.idAutor = idAutor; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }

    public String getNombreAutor() { return nombreAutor; }
    public void setNombreAutor(String nombreAutor) { this.nombreAutor = nombreAutor; }
}