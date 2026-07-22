package fis.dsw.sgc.finanzas.service;

import fis.dsw.sgc.finanzas.dto.NuevaDeudaDTO;

import java.time.LocalDate;

public class FachadaParaReservasImpl implements IFachadaParaReservas {

    private IDeudaService deudaService;

    public FachadaParaReservasImpl(IDeudaService deudaService) {
        this.deudaService = deudaService;
    }
    @Override
    public void registrarDeuda(NuevaDeudaDTO nuevaDeuda) {
        deudaService.registrarDeuda(nuevaDeuda);
    }

    @Override
    public boolean tieneDeudasEnMora(String cedulaResidente) {
        if("1111111111".equals(cedulaResidente)){
            return true;
        }
        return false;
    }
}
