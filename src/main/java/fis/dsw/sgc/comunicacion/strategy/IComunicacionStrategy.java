package fis.dsw.sgc.comunicacion.strategy;

import fis.dsw.sgc.comunicacion.dto.EnviarComunicacionDTO;

/**
 * Define las reglas variables para cada tipo de comunicación.
 */
public interface IComunicacionStrategy {

    /**
     * Indica si esta estrategia puede procesar el tipo recibido.
     *
     * @param tipo código del tipo de comunicación
     * @return true cuando la estrategia soporta el tipo
     */
    boolean soporta(String tipo);

    /**
     * Aplica las reglas particulares del tipo de comunicación.
     *
     * @param dto datos de la comunicación
     */
    void validar(EnviarComunicacionDTO dto);
}
