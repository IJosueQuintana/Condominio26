package fis.dsw.sgc.finanzas.service;

import fis.dsw.sgc.finanzas.model.Deuda;

import java.time.LocalDate;

public interface IDeudaService {

    void registrarDeuda(String cedulaResidente, String motivoDeuda, LocalDate fechaMaximaPago, String descripcion, Double valor);
    void modificarFechaMaximaDePagoDeUnaDeuda(Integer idDeuda, LocalDate nuevaFechaMaximaPago);
    void eliminarDeuda(Integer idDeuda);
    void pagarDeuda(Integer idDeuda, String metodoPago);
    void pagarDeudaTarjeta(Integer idDeuda, String numeroTarjeta, LocalDate fechaVencimientoTarjeta, String nombreTitularTarjeta, Integer ccv);
    void solicitarPagoEnCuotas(Integer idDeuda, Integer numeroMesesADiferir);
    void consultarDeuda(String numeroCedulaResidente);
    void registrarDeudaAlicuotaMensual(String numeroCedulaResidente);
    void enviarRecordatorioDeudaPendiente(String numeroCedulaResidente);
    void registrarMoraDeuda(String numeroCedulaResidente);

}
