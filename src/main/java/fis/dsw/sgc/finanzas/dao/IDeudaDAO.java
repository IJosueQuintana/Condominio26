package fis.dsw.sgc.finanzas.dao;

import fis.dsw.sgc.finanzas.model.Deuda;

import java.util.List;

public interface IDeudaDAO {
    void guardar(Deuda deuda);
    void actualizar(Deuda deuda);
    Deuda buscarPorId(int idDeuda);
    List<Deuda> buscarPorUsuario(int idUsuario);
}
