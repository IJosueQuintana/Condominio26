package fis.dsw.sgc.finanzas.service;

import fis.dsw.sgc.finanzas.model.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DeudaFactoryImpl implements IDeudaFactory {

    @Override
    public Deuda crearDeuda(String motivo, int idResidente, double valor, Date fecha) {
        Deuda nuevaDeuda = new Deuda();
        nuevaDeuda.setIdUsuario(idResidente);
        nuevaDeuda.setValorBase(valor);

        LocalDate fechaVencimiento = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        nuevaDeuda.setFechaVencimiento(fechaVencimiento);

        // Toda deuda nace en PENDIENTE
        nuevaDeuda.setEstado(new EstadoPendiente());

        switch (motivo.toUpperCase()) {
            case "ALICUOTA":
                // Aquí el Service luego usará la Fachada Inmuebles para pasar estos valores reales
                double tamanoDepto = 100.0;
                double tamanoCondominio = 1000.0;
                nuevaDeuda.setTipoDeuda(new DeudaAlicuota(tamanoDepto, tamanoCondominio));
                break;
            case "MULTA":
                nuevaDeuda.setTipoDeuda(new DeudaMulta());
                break;
            case "RESERVA":
                nuevaDeuda.setTipoDeuda(new DeudaReserva());
                break;
            default:
                throw new IllegalArgumentException("Motivo de deuda no reconocido: " + motivo);
        }

        // Delegamos el cálculo inicial al Rich Model
        if (nuevaDeuda.getTipoDeuda() != null) {
            nuevaDeuda.calcularValorTotal();
        }

        return nuevaDeuda;
    }
}