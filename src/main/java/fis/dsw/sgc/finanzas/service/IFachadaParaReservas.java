package fis.dsw.sgc.finanzas.service;

import fis.dsw.sgc.finanzas.dto.NuevaDeudaDTO;



public interface IFachadaParaReservas {
    void registrarDeuda(NuevaDeudaDTO nuevaDeuda);
    boolean tieneDeudasEnMora(String cedulaResidente);
}
