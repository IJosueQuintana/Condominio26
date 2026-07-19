package fis.dsw.sgc.finanzas.service;

import fis.dsw.sgc.finanzas.model.Deuda;

import java.util.Date;

public class DeudaFactory {

    public Deuda crearDeuda(String motivo, int idResidente, double valor, Date fecha) {
        Deuda nuevaDeuda = new Deuda();

        // TODO: Reto 1 - Asignar los datos básicos (residente, valorBase, fecha, descripcion) a nuevaDeuda

        // TODO: Reto 2 - Hacer un switch o if-else basado en el "motivo" para instanciar el ITipoDeuda correcto

        // TODO: Reto 3 - Asignar el estado inicial por defecto (EstadoPendiente)

        return nuevaDeuda;
    }
}
