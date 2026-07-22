package fis.dsw.sgc.finanzas.service;

import fis.dsw.sgc.finanzas.dto.EntidadBancariaDTO;

public class ConfiguracionFinancieraService implements IConfiguracionFinancieraService {

    @Override
    public Double definirValorMensualDeAlicuotas(Double valorMensualEsperado) {
        // TODO: reemplazar por la orquestación real (Model -> DAO) cuando exista el DAO.
        // Valor quemado de prueba: se regresa el mismo valor recibido, simulando que se guardó.
        return valorMensualEsperado;
    }

    @Override
    public EntidadBancariaDTO registrarEntidadBancaria(EntidadBancariaDTO entidadBancaria) {
        // TODO: reemplazar por la orquestación real (Model -> DAO) cuando exista el DAO.
        // Valor quemado de prueba: se regresa el mismo DTO recibido, simulando que se guardó.
        return entidadBancaria;
    }
}
