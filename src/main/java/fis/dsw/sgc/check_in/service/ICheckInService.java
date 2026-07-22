package fis.dsw.sgc.check_in.service;

import fis.dsw.sgc.check_in.dto.RegistroEntradaDTO;
import fis.dsw.sgc.check_in.exception.CheckInException;
import fis.dsw.sgc.check_in.model.ColeccionHistorialEntradas;
import fis.dsw.sgc.check_in.model.RegistroEntradaExterna;

import fis.dsw.sgc.check_in.model.RegistroEntradaResidente;
import fis.dsw.sgc.check_in.model.RegistroEntradaVisitante;
import fis.dsw.sgc.check_in.model.ReporteHistorial;

import java.util.List;

public interface ICheckInService {
    RegistroEntradaResidente registrarEntradaResidente(String cedula) throws CheckInException;
    RegistroEntradaExterna registrarEntradaExterna(String nombres, String apellidos, String cedula, String destino, String infoAdicional, boolean requiereParqueadero, String placa, String numeroParqueadero) throws CheckInException;
    RegistroEntradaVisitante registrarEntradaVisitante(String cedulaVisitante, Integer idVisita, boolean requiereParqueadero, String placa, String numeroParqueadero) throws CheckInException;
    List<RegistroEntradaDTO> obtenerHistorialDTO(String fecha, String tipo, String cedula);
    List<RegistroEntradaDTO> obtenerHistorialDTOAvanzado(String fechaInicio, String fechaFin, String tipo, String busquedaTexto, String placa);
    ColeccionHistorialEntradas obtenerColeccionHistorial(String fecha, String tipo, String cedula);
    ReporteHistorial generarReporteEstadistico(String fecha, String tipo, String cedula);
    List<String> obtenerParqueaderosDisponibles(String tipo);
    String[] buscarDatosResidentePorCedula(String cedula);
}
