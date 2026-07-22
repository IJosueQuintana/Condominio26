package fis.dsw.sgc.check_in.service;

import fis.dsw.sgc.check_in.model.VisitaProgramada;

import java.util.List;
import java.util.Map;

public interface IProgramVisitaService {
    List<VisitaProgramada> obtenerVisitasProgramadas();
    boolean cancelarVisitaProgramada(Integer idVisita);
    boolean programarVisita(VisitaProgramada visitaProgramada);
    Map<String, Integer> obtenerResidentes();
    boolean actualizarFechaHora(Integer idVisita, String nuevaFecha, String nuevaHora);
}

