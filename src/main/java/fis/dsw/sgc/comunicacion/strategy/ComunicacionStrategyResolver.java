package fis.dsw.sgc.comunicacion.strategy;

import fis.dsw.sgc.comunicacion.exception.ComunicacionException;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Resuelve la estrategia de comunicación según el código del tipo.
 */
public final class ComunicacionStrategyResolver
        implements IComunicacionStrategyResolver {

    private final List<IComunicacionStrategy> estrategias;

    /**
     * Constructor inyectable para pruebas o composición manual.
     */
    public ComunicacionStrategyResolver(
            List<IComunicacionStrategy> estrategias
    ) {
        Objects.requireNonNull(
                estrategias,
                "La colección de estrategias no puede ser nula."
        );

        if (estrategias.isEmpty()) {
            throw new IllegalArgumentException(
                    "Debe registrarse al menos una estrategia."
            );
        }

        this.estrategias = List.copyOf(estrategias);
    }

    /**
     * Composición predeterminada usada para mantener compatible el Main actual.
     */
    public static IComunicacionStrategyResolver porDefecto() {
        return new ComunicacionStrategyResolver(
                List.of(
                        new ComunicacionSegmentadaStrategy(),
                        new ComunicacionGlobalStrategy(),
                        new ComunicacionUrgenteStrategy()
                )
        );
    }

    @Override
    public IComunicacionStrategy obtener(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new ComunicacionException(
                    "El tipo de comunicación es obligatorio."
            );
        }

        String tipoNormalizado = tipo
                .trim()
                .toUpperCase(Locale.ROOT);

        return estrategias.stream()
                .filter(estrategia ->
                        estrategia.soporta(tipoNormalizado)
                )
                .findFirst()
                .orElseThrow(() ->
                        new ComunicacionException(
                                "No existe una estrategia para el tipo: "
                                        + tipoNormalizado
                        )
                );
    }
}
