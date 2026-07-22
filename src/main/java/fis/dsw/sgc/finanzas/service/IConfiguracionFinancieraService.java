package fis.dsw.sgc.finanzas.service;

import fis.dsw.sgc.finanzas.dto.EntidadBancariaDTO;

import java.util.List;

public interface IConfiguracionFinancieraService {

    // Caso de uso: definirValorMensualDeAlicuotas
    Double definirValorMensualDeAlicuotas(Double valorMensualEsperado);

    // Consulta el valor mensual de alícuotas vigente actualmente
    Double consultarValorMensualDeAlicuotas();

    // Caso de uso: registrarEntidadBancaria
    EntidadBancariaDTO registrarEntidadBancaria(EntidadBancariaDTO entidadBancaria);

    // Consulta las entidades bancarias ya registradas
    List<EntidadBancariaDTO> listarEntidadesBancarias();
}
