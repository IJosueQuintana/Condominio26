package fis.dsw.sgc.reservas.dao;

import fis.dsw.sgc.reservas.dto.EspacioReservableDTO;

import java.util.List;

/**
 * DAO de solo lectura sobre la tabla espacio_comun (propiedad del modulo
 * de Inmuebles). El modulo de Reservas lo usa para obtener nombre, tarifa,
 * horario y capacidad de los espacios que se pueden reservar.
 */
public interface IEspacioReservableDAO {

    /** Espacios en estado DISPONIBLE, ordenados por nombre. */
    List<EspacioReservableDTO> listarDisponibles();

    /** Todos los espacios comunes (para auditoria / listados completos). */
    List<EspacioReservableDTO> listarTodos();

    EspacioReservableDTO buscarPorId(int idEspacioComun);
}
