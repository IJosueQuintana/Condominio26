package fis.dsw.sgc.comunicacion.strategy;

import fis.dsw.sgc.comunicacion.dto.EnviarComunicacionDTO;

import java.util.Set;

/**
 * Estrategia para comunicaciones de alcance general.
 */
public final class ComunicacionGlobalStrategy
        implements IComunicacionStrategy {

    private static final Set<String> TIPOS_SOPORTADOS = Set.of(
            "MENSAJE_GLOBAL",
            "BOLETIN_INFORMATIVO"
    );

    @Override
    public boolean soporta(String tipo) {
        return TIPOS_SOPORTADOS.contains(tipo);
    }

    @Override
    public void validar(EnviarComunicacionDTO dto) {
        /*
         * Las validaciones comunes ya se ejecutan en el Service.
         * Esta clase permite agregar reglas de publicación masiva
         * o de boletines sin introducir condicionales en el Service.
         */
    }
}
