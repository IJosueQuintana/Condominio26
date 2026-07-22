package fis.dsw.sgc.reservas.dao;

import fis.dsw.sgc.reservas.dto.ReservaDTO;

import java.util.List;

public interface IReservaDAO {
    void guardar(ReservaDTO reserva);
    void actualizar(ReservaDTO reserva);
    ReservaDTO buscarPorId(int idReserva);
    List<ReservaDTO> buscarPorUsuario(int idUsuario);
    List<ReservaDTO> buscarPorEspacioYFecha(int idEspacioComun, String fechaReserva);
    List<ReservaDTO> buscarTodas();
    void cancelar(int idReserva, String motivoCancelacion);
    void finalizarReservasVencidas();
}
