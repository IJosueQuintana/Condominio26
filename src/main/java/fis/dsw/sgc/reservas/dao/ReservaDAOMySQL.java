package fis.dsw.sgc.reservas.dao;

import fis.dsw.sgc.reservas.model.Reserva;
import java.sql.Connection;
import java.util.List;

public class ReservaDAOMySQL implements IReservaDAO {
    private Connection dbConn;

    @Override
    public void guardar(Reserva reserva) {}

    @Override
    public void actualizar(Reserva reserva) {}

    @Override
    public List<Reserva> buscarPorUsuario(String idUsuario) {
        return null;
    }
}
