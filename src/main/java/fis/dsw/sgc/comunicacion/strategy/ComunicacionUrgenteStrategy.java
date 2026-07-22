package fis.dsw.sgc.comunicacion.strategy;

import fis.dsw.sgc.comunicacion.dto.EnviarComunicacionDTO;
import fis.dsw.sgc.comunicacion.exception.ComunicacionException;

import java.util.Locale;
import java.util.Set;

/**
 * Estrategia para mensajes urgentes y alertas de emergencia.
 */
public final class ComunicacionUrgenteStrategy
        implements IComunicacionStrategy {

    private static final Set<String> TIPOS_SOPORTADOS = Set.of(
            "MENSAJE_URGENTE",
            "ALERTA_EMERGENCIA"
    );

    @Override
    public boolean soporta(String tipo) {
        return TIPOS_SOPORTADOS.contains(tipo);
    }

    @Override
    public void validar(EnviarComunicacionDTO dto) {
        String prioridad = dto.prioridad()
                .trim()
                .toUpperCase(Locale.ROOT);

        if (!prioridad.equals("ALTA")
                && !prioridad.equals("URGENTE")) {
            throw new ComunicacionException(
                    "Los mensajes urgentes y las alertas "
                            + "deben tener prioridad ALTA o URGENTE."
            );
        }
    }
}
