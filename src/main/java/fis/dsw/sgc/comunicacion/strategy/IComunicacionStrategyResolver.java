package fis.dsw.sgc.comunicacion.strategy;

/**
 * Abstracción inyectable encargada de seleccionar la Strategy correcta.
 */
public interface IComunicacionStrategyResolver {

    /**
     * Obtiene la estrategia correspondiente al tipo solicitado.
     *
     * @param tipo código del tipo de comunicación
     * @return estrategia compatible
     */
    IComunicacionStrategy obtener(String tipo);
}
