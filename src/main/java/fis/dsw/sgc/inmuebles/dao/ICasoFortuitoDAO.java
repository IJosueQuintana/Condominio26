package fis.dsw.sgc.inmuebles.dao;

import java.util.List;

import fis.dsw.sgc.inmuebles.dto.CasoFortuitoDTO;
import fis.dsw.sgc.inmuebles.model.CasoFortuito;

public interface ICasoFortuitoDAO {
    void guardarIncidente(CasoFortuito caso);
    List<CasoFortuitoDTO> listarPorInmueble(int idInmueble);
}