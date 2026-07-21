package fis.dsw.sgc.finanzas.service;

import fis.dsw.sgc.finanzas.dto.CuotaDTO;
import fis.dsw.sgc.finanzas.dto.DeudaConsultadaDTO;
import fis.dsw.sgc.finanzas.dto.NuevaDeudaDTO;
import java.time.LocalDate;
import java.util.List;

public interface IDeudaService {
    void registrarDeuda(NuevaDeudaDTO nuevaDeudaDTO);
    void modificarFechaMaximaDePagoDeUnaDeuda(Integer idDeuda, LocalDate nuevaFechaMaximaPago);
    void eliminarDeuda(Integer idDeuda);
    void pagarDeuda(Integer idDeuda, String metodoPago);
    void pagarDeudaTarjeta(Integer idDeuda, String numeroTarjeta, LocalDate fechaVencimientoTarjeta, String nombreTitularTarjeta, Integer ccv);
    List<CuotaDTO> solicitarPagoEnCuotas(Integer idDeuda, Integer numeroMesesADiferir);
    List<DeudaConsultadaDTO> consultarDeuda(String numeroCedulaResidente);

    // Estos los dejamos para después como pediste
    void registrarDeudaAlicuotaMensual(String numeroCedulaResidente);
    void enviarRecordatorioDeudaPendiente(String numeroCedulaResidente);
    void registrarMoraDeuda(String numeroCedulaResidente);

}