package fis.dsw.sgc.check_in.dao;

import fis.dsw.sgc.check_in.model.Usuario_Checkin;
import fis.dsw.sgc.check_in.model.VisitaProgramada;

import java.util.List;

public interface IProgramacionVisitaDAO {
    boolean programarVisita(VisitaProgramada visita);
    boolean actualizarFechaHora(int idVisita, String nuevaFecha, String nuevaHora);
    List<VisitaProgramada> obtenerVisitasProgramadas();
    boolean cancelarVisitaProgramada(Integer idVisita);
    List<Usuario_Checkin> obtenerResidentes();
    /** Recupera una visita programada por su ID. Retorna null si no existe. */
    VisitaProgramada obtenerVisitaPorId(int idVisita);
    /** Marca una visita programada como REALIZADA. */
    boolean marcarComoRealizada(int idVisita);
    /** Obtiene el nombre del residente anfitrión y su departamento por id_usuario. */
    String obtenerInfoResidentePorId(int idResidente);
}

