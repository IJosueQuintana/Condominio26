package fis.dsw.sgc.inmuebles.service;

import java.util.List;

import fis.dsw.sgc.inmuebles.dao.CasoFortuitoDAOMySQL;
import fis.dsw.sgc.inmuebles.dao.ICasoFortuitoDAO;
import fis.dsw.sgc.inmuebles.dao.IInmuebleDAO;
import fis.dsw.sgc.inmuebles.dto.CasoFortuitoDTO;
import fis.dsw.sgc.inmuebles.dto.DimensionesInmuebleDTO;
import fis.dsw.sgc.inmuebles.dto.InmuebleResumenDTO;
import fis.dsw.sgc.inmuebles.dto.OpcionComboDTO;
import fis.dsw.sgc.inmuebles.model.CasoFortuito;
import fis.dsw.sgc.inmuebles.model.Inmueble;

public class InmueblesServiceImpl implements IInmueblesService {

    private final IInmuebleDAO inmuebleDAO;
    private final ICasoFortuitoDAO casoFortuitoDAO;

    public InmueblesServiceImpl(IInmuebleDAO inmuebleDAO) {
        this(inmuebleDAO, new CasoFortuitoDAOMySQL());
    }

    public InmueblesServiceImpl(IInmuebleDAO inmuebleDAO, ICasoFortuitoDAO casoFortuitoDAO) {
        this.inmuebleDAO = inmuebleDAO;
        this.casoFortuitoDAO = casoFortuitoDAO;
    }

    @Override
    public List<InmuebleResumenDTO> listarInmuebles(String filtro) {
        String filtroLimpio = (filtro == null) ? "" : filtro.trim();
        return inmuebleDAO.buscarResumen(filtroLimpio);
    }

    @Override
    public List<OpcionComboDTO> obtenerEdificios() {
        return inmuebleDAO.listarEdificios();
    }

    @Override
    public List<OpcionComboDTO> obtenerTiposInmueble() {
        return inmuebleDAO.listarTiposInmueble();
    }

    @Override
    public int registrarInmueble(Inmueble inmueble) {
        if (inmueble.getCodigo() == null || inmueble.getCodigo().isBlank()) {
            throw new IllegalArgumentException("El código del inmueble es obligatorio.");
        }
        if (inmueble.getIdTipoInmueble() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un tipo de inmueble.");
        }
        if (inmuebleDAO.existeCodigo(inmueble.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un inmueble con el código '" + inmueble.getCodigo() + "'.");
        }
        if (inmueble.getEstado() == null || inmueble.getEstado().isBlank()) {
            inmueble.setEstado("DISPONIBLE");
        }
        return inmuebleDAO.guardar(inmueble);
    }

    @Override
    public Inmueble buscarInmueblePorCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            return null;
        }
        return inmuebleDAO.buscarPorCodigo(codigo.trim());
    }

    @Override
    public void actualizarInmueble(Inmueble inmueble) {
        if (inmueble.getIdInmueble() <= 0) {
            throw new IllegalArgumentException("No se ha cargado ningún inmueble para editar.");
        }
        if (inmueble.getCodigo() == null || inmueble.getCodigo().isBlank()) {
            throw new IllegalArgumentException("El código del inmueble es obligatorio.");
        }
        if (inmueble.getIdTipoInmueble() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un tipo de inmueble.");
        }
        if (inmuebleDAO.existeCodigoParaOtroInmueble(inmueble.getCodigo(), inmueble.getIdInmueble())) {
            throw new IllegalArgumentException("Ya existe otro inmueble con el código '" + inmueble.getCodigo() + "'.");
        }
        if (inmueble.getEstado() == null || inmueble.getEstado().isBlank()) {
            inmueble.setEstado("DISPONIBLE");
        }
        inmuebleDAO.actualizar(inmueble);
    }

    @Override
    public List<OpcionComboDTO> obtenerPropietarios() {
        return inmuebleDAO.listarPropietariosDisponibles();
    }

    @Override
    public Integer obtenerPropietarioActual(int idInmueble) {
        return inmuebleDAO.obtenerIdPropietario(idInmueble);
    }

    @Override
    public void asignarPropietario(int idInmueble, Integer idUsuario) {
        if (idUsuario == null) {
            inmuebleDAO.quitarPropietario(idInmueble);
        } else {
            inmuebleDAO.asignarPropietario(idInmueble, idUsuario);
        }
    }

    @Override
    public void registrarCasoFortuito(int idInmueble, String descripcion) {
        if (idInmueble <= 0) {
            throw new IllegalArgumentException("Debe buscar y seleccionar un inmueble válido primero.");
        }
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripción del incidente es obligatoria.");
        }

        CasoFortuito caso = new CasoFortuito();
        caso.setIdInmueble(idInmueble);
        caso.setDescripcion(descripcion.trim());
        casoFortuitoDAO.guardarIncidente(caso);
    }

   @Override
    public List<CasoFortuitoDTO> listarCasosFortuitos(int idInmueble) {
        return casoFortuitoDAO.listarPorInmueble(idInmueble);
    }

    @Override
    public DimensionesInmuebleDTO obtenerDimensiones(int idInmueble) {
        Inmueble inmueble = inmuebleDAO.buscarPorId(idInmueble);
        if (inmueble == null) {
            throw new IllegalArgumentException("No existe un inmueble con id " + idInmueble + ".");
        }

        double areaDepartamento = (inmueble.getAreaM2() == null) ? 0.0 : inmueble.getAreaM2();
        double areaCondominio = inmuebleDAO.obtenerAreaTotalCondominio();

        return new DimensionesInmuebleDTO(areaCondominio, areaDepartamento);
    }
}