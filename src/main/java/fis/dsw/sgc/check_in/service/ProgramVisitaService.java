package fis.dsw.sgc.check_in.service;
import fis.dsw.sgc.check_in.dao.IProgramacionVisitaDAO;
import fis.dsw.sgc.check_in.model.Usuario_Checkin;
import fis.dsw.sgc.check_in.model.VisitaProgramada;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramVisitaService implements IProgramVisitaService {
    private final IProgramacionVisitaDAO programacionVisitaDAO;
    private final List<Usuario_Checkin> residentes;


    public ProgramVisitaService(IProgramacionVisitaDAO programacionVisitaDAO) {
        this.programacionVisitaDAO = programacionVisitaDAO;
        this.residentes = programacionVisitaDAO.obtenerResidentes();
    }

    @Override
    public List<VisitaProgramada> obtenerVisitasProgramadas() {
        return programacionVisitaDAO.obtenerVisitasProgramadas();
    }

    @Override
    public boolean cancelarVisitaProgramada(Integer idVisita) {
        return programacionVisitaDAO.cancelarVisitaProgramada(idVisita);
    }

    @Override
    public boolean programarVisita(VisitaProgramada visitaProgramada) {
        return programacionVisitaDAO.programarVisita(visitaProgramada);
    }

    @Override
    public Map<String, Integer> obtenerResidentes() {
        Map<String, Integer> mapaResidentes = new HashMap<>();

        if (this.residentes != null) {
            for (Usuario_Checkin u : this.residentes) {
                String residente = String.format("%s %s (%s)", u.getNombres(), u.getApellidos(), u.getVivienda());
                mapaResidentes.put(residente, u.getIdUsuario());
            }
        }
        return mapaResidentes;
    }

    public Map<Integer, String> obtenerNombresResidentesPorId() {
        Map<Integer, String> mapa = new HashMap<>();
        if (this.residentes != null) {
            for (Usuario_Checkin u : this.residentes) {
                String nombreCompleto = String.format("%s %s", u.getNombres(), u.getApellidos());
                mapa.put(u.getIdUsuario(), nombreCompleto);
            }
        }
        return mapa;
    }

    @Override
    public boolean actualizarFechaHora(Integer idVisita, String nuevaFecha, String nuevaHora) {
        return programacionVisitaDAO.actualizarFechaHora(idVisita, nuevaFecha, nuevaHora);
    }
}