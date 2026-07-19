package fis.dsw.sgc.finanzas.service;

import fis.dsw.sgc.finanzas.dao.IDeudaDAO;
import fis.dsw.sgc.finanzas.model.Deuda;

import java.time.LocalDate;

public class DeudaService implements IDeudaService {
    private final IDeudaDAO deudaDAO;
    private final IDeudaFactory deudaFactory; // Cambiamos a la interfaz

    public DeudaService(IDeudaDAO deudaDAO, IDeudaFactory deudaFactory) {
        this.deudaDAO = deudaDAO;
        this.deudaFactory = deudaFactory;
    }

    @Override
    public void registrarDeuda(String cedulaResidente, String motivoDeuda, LocalDate fechaMaximaPago, String descripcion, Double valor) {
        if(motivoDeuda.equals("RESERVA")){
            System.out.println("DEUDA POR RESERVA GENERADA CON EXITO");
            System.out.println("USUARIO: ");
            System.out.println("ROL: ");
            System.out.println("ROL: ");
        }
    }

    @Override
    public void modificarFechaMaximaDePagoDeUnaDeuda(Integer idDeuda, LocalDate nuevaFechaMaximaPago) {

    }

    @Override
    public void eliminarDeuda(Integer idDeuda) {

    }

    @Override
    public void pagarDeuda(Integer idDeuda, String metodoPago) {

    }

    @Override
    public void pagarDeudaTarjeta(Integer idDeuda, String numeroTarjeta, LocalDate fechaVencimientoTarjeta, String nombreTitularTarjeta, Integer ccv) {

    }

    @Override
    public void solicitarPagoEnCuotas(Integer idDeuda, Integer numeroMesesADiferir) {

    }

    @Override
    public void consultarDeuda(String numeroCedulaResidente) {

    }

    @Override
    public void registrarDeudaAlicuotaMensual(String numeroCedulaResidente) {

    }

    @Override
    public void enviarRecordatorioDeudaPendiente(String numeroCedulaResidente) {

    }

    @Override
    public void registrarMoraDeuda(String numeroCedulaResidente) {

    }
}
