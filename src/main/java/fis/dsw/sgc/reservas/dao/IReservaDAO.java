package fis.dsw.sgc.reservas.dao;

import fis.dsw.sgc.reservas.model.Reserva;
import java.util.List;

public interface IReservaDAO {
    void guardar(Reserva reserva);
    void actualizar(Reserva reserva);
    List<Reserva> buscarPorUsuario(String idUsuario);
}
