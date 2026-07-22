package fis.dsw.sgc.finanzas.service;

import fis.dsw.sgc.finanzas.dto.EntidadBancariaDTO;

public interface IConfiguracionFinancieraService {

    // Caso de uso: definirValorMensualDeAlicuotas
    Double definirValorMensualDeAlicuotas(Double valorMensualEsperado);

    // Caso de uso: registrarEntidadBancaria
    EntidadBancariaDTO registrarEntidadBancaria(EntidadBancariaDTO entidadBancaria);
}
