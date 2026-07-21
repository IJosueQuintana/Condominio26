package fis.dsw.sgc.finanzas.service;

import fis.dsw.sgc.finanzas.dto.NuevaDeudaDTO;

import java.time.LocalDate;


public interface IFachadaParaReservas {

    boolean tieneDeudasEnMora(String cedulaResidente);

    void registrarDeuda(String cedula, String reserva, LocalDate fechaMaximaPago, String descripcion, double v);
}
