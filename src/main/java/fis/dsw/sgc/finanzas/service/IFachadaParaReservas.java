package fis.dsw.sgc.finanzas.service;

import java.time.LocalDate;

public interface IFachadaParaReservas {
    void registrarDeuda(String cedulaResidente, String motivoDeuda, LocalDate fechaMaximaPago, String descripcion, Double valor);
    boolean tieneDeudasEnMora(String cedulaResidente);
}
