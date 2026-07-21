package fis.dsw.sgc.finanzas.service;

import fis.dsw.sgc.finanzas.dto.NuevoGastoDTO;

public interface IGastoService {
    void registrarPagosCondominio(NuevoGastoDTO nuevoGastoDTO);
}