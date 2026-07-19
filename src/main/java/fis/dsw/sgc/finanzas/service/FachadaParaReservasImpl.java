package fis.dsw.sgc.finanzas.service;

import java.time.LocalDate;

public class FachadaParaReservasImpl implements IFachadaParaReservas {

    private IDeudaService deudaService;

    public FachadaParaReservasImpl(IDeudaService deudaService) {
        this.deudaService = deudaService;
    }
    @Override
    public void registrarDeuda(String cedulaResidente, String motivoDeuda, LocalDate fechaMaximaPago, String descripcion, Double valor) {
        deudaService.registrarDeuda(cedulaResidente, motivoDeuda, fechaMaximaPago, descripcion, valor);
    }

    @Override
    public boolean tieneDeudasEnMora(String cedulaResidente) {
        if("1111111111".equals(cedulaResidente)){
            return true;
        }
        return false;
    }
}
