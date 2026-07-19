package fis.dsw.sgc.finanzas.service;

import fis.dsw.sgc.finanzas.model.Deuda;

import java.util.Date;

public interface IDeudaFactory {
    public Deuda crearDeuda(String motivo, int idResidente, double valor, Date fecha);
}
