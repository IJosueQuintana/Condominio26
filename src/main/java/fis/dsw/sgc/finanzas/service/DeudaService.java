package fis.dsw.sgc.finanzas.service;

import fis.dsw.sgc.finanzas.dao.IDeudaDAO;
import fis.dsw.sgc.finanzas.model.Deuda;

import java.time.LocalDate;

public class DeudaService {

    private final IDeudaDAO deudaDAO;
    private final DeudaFactory deudaFactory; // Tu patrón Factory[cite: 1]

    public DeudaService(IDeudaDAO deudaDAO, DeudaFactory deudaFactory) {
        this.deudaDAO = deudaDAO;
        this.deudaFactory = deudaFactory;
    }


    // AQUI VIVIRAN LOS CASOS DE USO

}
