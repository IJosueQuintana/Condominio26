package fis.dsw.sgc.finanzas.service;

import fis.dsw.sgc.finanzas.dto.EntidadBancariaDTO;

import java.util.ArrayList;
import java.util.List;

public class ConfiguracionFinancieraService implements IConfiguracionFinancieraService {

    // Datos quemados de prueba: simulan lo que hoy estaría persistido, mientras no exista el DAO.
    private Double valorMensualAlicuotasActual = 45.00;
    private final List<EntidadBancariaDTO> entidadesBancariasRegistradas = new ArrayList<>(List.of(
            new EntidadBancariaDTO("Banco Pichincha", "2201234567", "1712345678", "AHORROS", "tesoreria@condominio.com")
    ));

    @Override
    public Double definirValorMensualDeAlicuotas(Double valorMensualEsperado) {
        // TODO: reemplazar por la orquestación real (Model -> DAO) cuando exista el DAO.
        // Valor quemado de prueba: se guarda en memoria y se regresa, simulando que se persistió.
        valorMensualAlicuotasActual = valorMensualEsperado;
        return valorMensualAlicuotasActual;
    }

    @Override
    public Double consultarValorMensualDeAlicuotas() {
        // TODO: reemplazar por la orquestación real (Model -> DAO) cuando exista el DAO.
        return valorMensualAlicuotasActual;
    }

    @Override
    public EntidadBancariaDTO registrarEntidadBancaria(EntidadBancariaDTO entidadBancaria) {
        // TODO: reemplazar por la orquestación real (Model -> DAO) cuando exista el DAO.
        // Valor quemado de prueba: se guarda en memoria y se regresa, simulando que se persistió.
        entidadesBancariasRegistradas.add(entidadBancaria);
        return entidadBancaria;
    }

    @Override
    public List<EntidadBancariaDTO> listarEntidadesBancarias() {
        // TODO: reemplazar por la orquestación real (Model -> DAO) cuando exista el DAO.
        return new ArrayList<>(entidadesBancariasRegistradas);
    }
}
