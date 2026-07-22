package fis.dsw.sgc.check_in.dao;

import fis.dsw.sgc.check_in.model.IngresoParqueadero;
import fis.dsw.sgc.check_in.model.RegistroEntrada;
import java.util.List;

public interface IRegistroEntradaDAO {
    int guardar(RegistroEntrada registro);
    List<RegistroEntrada> listarTodos();
    List<RegistroEntrada> buscarPorFiltro(String fecha, String tipo, String cedula);
    List<RegistroEntrada> buscarPorFiltroAvanzado(String fechaInicio, String fechaFin, String tipo, String busquedaTexto, String placa);
    boolean registrarIngresoParqueadero(IngresoParqueadero ingreso);
    List<String> obtenerParqueaderosDisponibles(String tipo);
    Integer obtenerIdParqueaderoPorNumero(String numero);
    String obtenerDepartamentoPorCedulaResidente(String cedula);
    String obtenerDepartamentoYResidentePorId(int idResidente);
    Integer obtenerIdParqueaderoDeResidente(int idResidente);
    /** Busca nombres, apellidos y departamento de un residente activo por cédula. Retorna null si no existe. */
    String[] buscarDatosResidentePorCedula(String cedula);
}
