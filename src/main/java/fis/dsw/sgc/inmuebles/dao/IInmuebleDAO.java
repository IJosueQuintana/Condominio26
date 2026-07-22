package fis.dsw.sgc.inmuebles.dao;

import java.util.List;

import fis.dsw.sgc.inmuebles.dto.InmuebleResumenDTO;
import fis.dsw.sgc.inmuebles.dto.OpcionComboDTO;
import fis.dsw.sgc.inmuebles.model.Inmueble;

public interface IInmuebleDAO {
    /** Guarda el inmueble y devuelve el id generado (o -1 si falló). */
    int guardar(Inmueble inmueble);
    void actualizar(Inmueble inmueble);
    Inmueble buscarPorId(int idInmueble);
    Inmueble buscarPorCodigo(String codigo);
    List<InmuebleResumenDTO> buscarResumen(String filtro);
    List<OpcionComboDTO> listarEdificios();
    List<OpcionComboDTO> listarTiposInmueble();
    boolean existeCodigo(String codigo);
    boolean existeCodigoParaOtroInmueble(String codigo, int idInmueble);

    /** Lista los usuarios activos que se pueden asignar como propietarios. */
    List<OpcionComboDTO> listarPropietariosDisponibles();

    /** Devuelve el id del propietario principal activo del inmueble, o null si no tiene. */
    Integer obtenerIdPropietario(int idInmueble);

    /** Reemplaza al propietario principal activo del inmueble por el usuario indicado. */
    void asignarPropietario(int idInmueble, int idUsuario);

    /** Deja al inmueble sin propietario principal activo. */
    void quitarPropietario(int idInmueble);

    /** Suma del área en m2 de todos los inmuebles del condominio. */
    double obtenerAreaTotalCondominio();
}