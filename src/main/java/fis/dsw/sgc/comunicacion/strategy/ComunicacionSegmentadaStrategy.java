package fis.dsw.sgc.comunicacion.strategy;

import fis.dsw.sgc.comunicacion.dto.EnviarComunicacionDTO;

import java.util.Set;

/**
 * Estrategia para comunicaciones destinadas a grupos específicos.
 */
public final class ComunicacionSegmentadaStrategy
        implements IComunicacionStrategy {

    private static final Set<String> TIPOS_SOPORTADOS = Set.of(
            "MENSAJE_RESIDENTES",
            "COMUNICADO_TRABAJADORES"
    );

    @Override
    public boolean soporta(String tipo) {
        return TIPOS_SOPORTADOS.contains(tipo);
    }

    @Override
    public void validar(EnviarComunicacionDTO dto) {
        /*
         * Las validaciones comunes ya se ejecutan en el Service.
         * Esta clase concentra el punto de extensión para futuras
         * reglas por roles o segmentos, sin modificar el Service.
         */
    }
}
