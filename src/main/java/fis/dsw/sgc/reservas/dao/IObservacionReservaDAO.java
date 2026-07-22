package fis.dsw.sgc.reservas.dao;

import fis.dsw.sgc.reservas.dto.ObservacionReservaDTO;

import java.util.List;

public interface IObservacionReservaDAO {
    void guardar(ObservacionReservaDTO observacion);
    List<ObservacionReservaDTO> buscarPorReserva(int idReserva);
}
