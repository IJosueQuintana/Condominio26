package fis.dsw.sgc.finanzas.dao;

import fis.dsw.sgc.finanzas.model.Gasto;

public interface IGastoDAO {
    // Métodos de la base de datos para Gasto
    void guardar(Gasto gasto);
    Gasto buscarPorId(int idGasto);
}