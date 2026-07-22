package fis.dsw.sgc.inmuebles.service;

import java.util.List;

import fis.dsw.sgc.inmuebles.dto.CasoFortuitoDTO;
import fis.dsw.sgc.inmuebles.dto.DimensionesInmuebleDTO;
import fis.dsw.sgc.inmuebles.dto.InmuebleResumenDTO;
import fis.dsw.sgc.inmuebles.dto.OpcionComboDTO;
import fis.dsw.sgc.inmuebles.model.Inmueble;

public interface IInmueblesService {
    List<InmuebleResumenDTO> listarInmuebles(String filtro);

    List<OpcionComboDTO> obtenerEdificios();

    List<OpcionComboDTO> obtenerTiposInmueble();

    /** Registra el inmueble y devuelve el id generado. */
    int registrarInmueble(Inmueble inmueble);

    Inmueble buscarInmueblePorCodigo(String codigo);

    void actualizarInmueble(Inmueble inmueble);

    /** Lista de usuarios que se pueden asignar como propietarios de un inmueble. */
    List<OpcionComboDTO> obtenerPropietarios();

    /** Id del propietario principal activo del inmueble, o null si no tiene. */
    Integer obtenerPropietarioActual(int idInmueble);

    /** Asigna al usuario indicado como propietario principal. Si idUsuario es null, se lo quita. */
    void asignarPropietario(int idInmueble, Integer idUsuario);

    /**
     * Registra un caso fortuito (incidente imprevisto) para un inmueble.
     */
    void registrarCasoFortuito(int idInmueble, String descripcion);

    /**
     * Lista el historial de casos fortuitos registrados para un inmueble.
     */
    List<CasoFortuitoDTO> listarCasosFortuitos(int idInmueble);

    /**
     * Devuelve el área total del condominio (suma de todos los inmuebles)
     * junto con el área del departamento indicado.
     */
    DimensionesInmuebleDTO obtenerDimensiones(int idInmueble);
}