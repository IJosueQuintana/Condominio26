package fis.dsw.sgc.check_in.model;

public class RegistroEntradaFactory {

    public static RegistroEntradaResidente crearEntradaResidente(String nombres, String apellidos, String cedula,
                                                                 String fecha, String hora, String departamento) {
        RegistroEntradaResidente entrada = new RegistroEntradaResidente();
        entrada.setNombres(nombres);
        entrada.setApellidos(apellidos);
        entrada.setCedula(cedula);
        entrada.setFechaLlegada(fecha);
        entrada.setHoraLlegada(hora);
        entrada.setNumeroDepartamento(departamento);
        return entrada;
    }

    public static RegistroEntradaVisitante crearEntradaVisitante(String nombres, String apellidos, String cedula,
                                                                 String fecha, String hora, Integer idVisita,
                                                                 String residenteAnfitrion, boolean autorizado) {
        RegistroEntradaVisitante entrada = new RegistroEntradaVisitante();
        entrada.setNombres(nombres);
        entrada.setApellidos(apellidos);
        entrada.setCedula(cedula);
        entrada.setFechaLlegada(fecha);
        entrada.setHoraLlegada(hora);
        entrada.setIdVisita(idVisita);
        entrada.setResidenteAnfitrion(residenteAnfitrion);
        entrada.setAutorizadoPorResidente(autorizado);
        return entrada;
    }

    public static RegistroEntradaExterna crearEntradaExterna(String nombres, String apellidos, String cedula,
                                                             String fecha, String hora, String entidad,
                                                             String motivo, String placa) {
        RegistroEntradaExterna entrada = new RegistroEntradaExterna();
        entrada.setNombres(nombres);
        entrada.setApellidos(apellidos);
        entrada.setCedula(cedula);
        entrada.setFechaLlegada(fecha);
        entrada.setHoraLlegada(hora);
        entrada.setEntidadProcedencia(entidad);
        entrada.setInformacionAdicional(motivo);
        entrada.setPlacaVehiculo(placa);
        return entrada;
    }
}
