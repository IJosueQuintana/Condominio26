package fis.dsw.sgc.check_in.service;

import fis.dsw.sgc.check_in.dao.IRegistroEntradaDAO;
import fis.dsw.sgc.check_in.dao.IProgramacionVisitaDAO;
import fis.dsw.sgc.check_in.dto.RegistroEntradaDTO;
import fis.dsw.sgc.check_in.exception.CheckInException;
import fis.dsw.sgc.check_in.model.ColeccionHistorialEntradas;
import fis.dsw.sgc.check_in.model.IngresoParqueadero;
import fis.dsw.sgc.check_in.model.IteradorRegistroEntrada;
import fis.dsw.sgc.check_in.model.RegistroEntrada;
import fis.dsw.sgc.check_in.model.RegistroEntradaExterna;
import fis.dsw.sgc.check_in.model.RegistroEntradaFactory;
import fis.dsw.sgc.check_in.model.RegistroEntradaResidente;
import fis.dsw.sgc.check_in.model.RegistroEntradaVisitante;
import fis.dsw.sgc.check_in.model.ReporteHistorial;
import fis.dsw.sgc.check_in.model.VisitaProgramada;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CheckInServiceImpl implements ICheckInService {

    private final IRegistroEntradaDAO registroEntradaDAO;
    private final IProgramacionVisitaDAO programacionVisitaDAO;
    private static final DateTimeFormatter HORA_FORMATO = DateTimeFormatter.ofPattern("HH:mm");

    public CheckInServiceImpl() {
        this(new fis.dsw.sgc.check_in.dao.RegistroEntradaDAO(),
             new fis.dsw.sgc.check_in.dao.ProgramacionVisitaDAO());
    }

    /** Constructor principal para DI manual (jefe de proyecto). */
    public CheckInServiceImpl(IRegistroEntradaDAO registroEntradaDAO) {
        this(registroEntradaDAO, new fis.dsw.sgc.check_in.dao.ProgramacionVisitaDAO());
    }

    /** Constructor completo para DI manual con ambos DAOs. */
    public CheckInServiceImpl(IRegistroEntradaDAO registroEntradaDAO,
                              IProgramacionVisitaDAO programacionVisitaDAO) {
        this.registroEntradaDAO = registroEntradaDAO;
        this.programacionVisitaDAO = programacionVisitaDAO;
    }

    @Override
    public String[] buscarDatosResidentePorCedula(String cedula) {
        // Delegado al DAO — sin SQL en el service
        return registroEntradaDAO.buscarDatosResidentePorCedula(cedula);
    }

    @Override
    public RegistroEntradaResidente registrarEntradaResidente(String cedula) throws CheckInException {
        if (cedula == null || cedula.isBlank()) {
            throw new CheckInException("La identificación del residente no puede estar vacía.");
        }

        String[] datosResidente = buscarDatosResidentePorCedula(cedula);
        if (datosResidente == null) {
            throw new CheckInException("No se encontró un residente activo registrado con la cédula " + cedula);
        }

        String fechaActual = LocalDate.now().toString();
        String horaActual = LocalTime.now().format(HORA_FORMATO);

        RegistroEntradaResidente entrada = RegistroEntradaFactory.crearEntradaResidente(
                datosResidente[0],
                datosResidente[1],
                cedula.trim(),
                fechaActual,
                horaActual,
                datosResidente[2]
        );
        entrada.setInformacionAdicional(datosResidente[2]);

        int id = registroEntradaDAO.guardar(entrada);
        if (id <= 0) {
            throw new CheckInException("No se pudo registrar la entrada en la base de datos.");
        }
        entrada.setIdEntrada(id);
        return entrada;
    }

    @Override
    public RegistroEntradaExterna registrarEntradaExterna(String nombres, String apellidos, String cedula,
                                                         String destino, String infoAdicional,
                                                         boolean requiereParqueadero, String placa,
                                                         String numeroParqueadero) throws CheckInException {
        if (nombres == null || nombres.isBlank() || cedula == null || cedula.isBlank() || destino == null || destino.isBlank()) {
            throw new CheckInException("Nombre, identificación y residente/destino son obligatorios.");
        }

        String fechaActual = LocalDate.now().toString();
        String horaActual = LocalTime.now().format(HORA_FORMATO);

        RegistroEntradaExterna entrada = RegistroEntradaFactory.crearEntradaExterna(
                nombres.trim(),
                apellidos != null ? apellidos.trim() : "",
                cedula.trim(),
                fechaActual,
                horaActual,
                destino.trim(),
                infoAdicional,
                requiereParqueadero ? placa : null
        );

        int idEntrada = registroEntradaDAO.guardar(entrada);
        if (idEntrada <= 0) {
            throw new CheckInException("Error al guardar la entrada externa en la base de datos.");
        }
        entrada.setIdEntrada(idEntrada);

        if (requiereParqueadero) {
            if (numeroParqueadero == null || numeroParqueadero.isBlank()) {
                throw new CheckInException("Seleccione un parqueadero válido.");
            }
            Integer idParqueadero = registroEntradaDAO.obtenerIdParqueaderoPorNumero(numeroParqueadero);
            if (idParqueadero != null && idParqueadero > 0) {
                IngresoParqueadero ingresoParq = new IngresoParqueadero();
                ingresoParq.setIdRegistroEntrada(idEntrada);
                ingresoParq.setIdParqueadero(idParqueadero);
                registroEntradaDAO.registrarIngresoParqueadero(ingresoParq);
            }
        }

        return entrada;
    }

    @Override
    public RegistroEntradaVisitante registrarEntradaVisitante(String cedulaVisitante, Integer idVisita,
                                                              boolean requiereParqueadero, String placa,
                                                              String numeroParqueadero) throws CheckInException {
        if (cedulaVisitante == null || cedulaVisitante.isBlank()) {
            throw new CheckInException("Cédula del visitante requerida.");
        }

        String nombres = "Visitante";
        String apellidos = "";
        String residenteAnfitrion = "Residentes";
        String fechaActual = LocalDate.now().toString();
        String horaActual = LocalTime.now().format(HORA_FORMATO);

        // Toda la lógica de BD ahora en el DAO — el service solo coordina
        if (idVisita != null && idVisita > 0) {
            VisitaProgramada visita = programacionVisitaDAO.obtenerVisitaPorId(idVisita);
            if (visita != null) {
                nombres = visita.getNombresVisita() != null ? visita.getNombresVisita() : "Visitante";
                apellidos = visita.getApellidosVisita() != null ? visita.getApellidosVisita() : "";
                if (visita.getIdResidente() != null && visita.getIdResidente() > 0) {
                    String infoRes = programacionVisitaDAO.obtenerInfoResidentePorId(visita.getIdResidente());
                    if (infoRes != null) residenteAnfitrion = infoRes;
                }
                programacionVisitaDAO.marcarComoRealizada(idVisita);
            }
        }

        RegistroEntradaVisitante entrada = RegistroEntradaFactory.crearEntradaVisitante(
                nombres, apellidos, cedulaVisitante.trim(), fechaActual, horaActual,
                idVisita, residenteAnfitrion, true
        );
        entrada.setPlacaVehiculo(requiereParqueadero ? placa : null);

        int idEntrada = registroEntradaDAO.guardar(entrada);
        if (idEntrada <= 0) {
            throw new CheckInException("Error al guardar la entrada del visitante.");
        }
        entrada.setIdEntrada(idEntrada);

        if (requiereParqueadero && numeroParqueadero != null && !numeroParqueadero.isBlank()) {
            Integer idParqueadero = registroEntradaDAO.obtenerIdParqueaderoPorNumero(numeroParqueadero);
            if (idParqueadero != null) {
                IngresoParqueadero ingresoParq = new IngresoParqueadero();
                ingresoParq.setIdRegistroEntrada(idEntrada);
                ingresoParq.setIdParqueadero(idParqueadero);
                registroEntradaDAO.registrarIngresoParqueadero(ingresoParq);
            }
        }

        return entrada;
    }

    @Override
    public ColeccionHistorialEntradas obtenerColeccionHistorial(String fecha, String tipo, String cedula) {
        List<RegistroEntrada> entradas = registroEntradaDAO.buscarPorFiltro(fecha, tipo, cedula);
        return new ColeccionHistorialEntradas(entradas);
    }

    @Override
    public List<RegistroEntradaDTO> obtenerHistorialDTO(String fecha, String tipo, String cedula) {
        return obtenerHistorialDTOAvanzado(fecha, null, tipo, cedula, null);
    }

    @Override
    public List<RegistroEntradaDTO> obtenerHistorialDTOAvanzado(String fechaInicio, String fechaFin, String tipo, String busquedaTexto, String placa) {
        List<RegistroEntrada> entradas = registroEntradaDAO.buscarPorFiltroAvanzado(fechaInicio, fechaFin, tipo, busquedaTexto, placa);
        ColeccionHistorialEntradas coleccion = new ColeccionHistorialEntradas(entradas);
        IteradorRegistroEntrada iterador = coleccion.crearIterador();
        List<RegistroEntradaDTO> dtos = new ArrayList<>();

        while (iterador.tieneSiguiente()) {
            RegistroEntrada e = iterador.siguiente();
            String persona = e.getNombres() + " " + e.getApellidos();
            String destino = e.getInformacionAdicional() != null ? e.getInformacionAdicional() : "N/A";
            String parq = e.getPlacaVehiculo() != null && !e.getPlacaVehiculo().isBlank() ? e.getPlacaVehiculo() : "-";

            dtos.add(new RegistroEntradaDTO(
                    e.getIdEntrada(),
                    e.getHoraLlegada(),
                    e.getFechaLlegada(),
                    persona.trim(),
                    e.getCedula(),
                    e.getTipoEntrada(),
                    destino,
                    e.getObservaciones() != null ? e.getObservaciones() : "",
                    parq
            ));
        }

        return dtos;
    }

    @Override
    public ReporteHistorial generarReporteEstadistico(String fecha, String tipo, String cedula) {
        ColeccionHistorialEntradas coleccion = obtenerColeccionHistorial(fecha, tipo, cedula);
        ReporteHistorial reporte = new ReporteHistorial();
        reporte.setFechaGeneracion(LocalDate.now().toString());
        reporte.setDescripcion("Reporte estadístico de Check-In e ingresos");
        reporte.generarResumen(coleccion.crearIterador());
        return reporte;
    }

    @Override
    public List<String> obtenerParqueaderosDisponibles(String tipo) {
        return registroEntradaDAO.obtenerParqueaderosDisponibles(tipo);
    }
}
