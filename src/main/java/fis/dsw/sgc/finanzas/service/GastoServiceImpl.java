package fis.dsw.sgc.finanzas.service;

import fis.dsw.sgc.finanzas.dto.NuevoGastoDTO;
import fis.dsw.sgc.finanzas.model.Gasto;
import java.time.LocalDate;

public class GastoServiceImpl implements IGastoService {

    private GastoFactoryImpl gastoFactory;

    // MOCK DAO
    // private IGastoDAO gastoDAO;

    public GastoServiceImpl() {
        this.gastoFactory = new GastoFactoryImpl();
    }

    @Override
    public void registrarPagosCondominio(NuevoGastoDTO dto) {

        // 1. Validar reglas de negocio básicas del Caso de Uso[cite: 15]
        if (dto.getFechaPago() == null || dto.getFechaPago().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha del pago debe ser menor o igual a la fecha actual.");
        }

        if (dto.getValorPagado() == null || dto.getValorPagado() <= 0) {
            throw new IllegalArgumentException("El valor pagado debe ser mayor a 0 y ser un decimal válido.");
        }

        // 2. Creación del objeto Gasto según su motivo (El Factory valida si el motivo existe)[cite: 15]
        Gasto nuevoGasto = gastoFactory.crearGasto(
                dto.getMotivo(),
                dto.getDescripcion(),
                dto.getValorPagado(),
                dto.getFechaPago()
        );

        // 3. Validar descripción delegando al modelo (Polimorfismo)[cite: 15]
        if (!nuevoGasto.validarDetalle()) {
            if (nuevoGasto.getTipoGasto().equals("SERVICIO_BASICO")) {
                throw new IllegalArgumentException("La descripción para servicios básicos debe ser exactamente AGUA, LUZ, TELEFONO o INTERNET.");
            } else {
                throw new IllegalArgumentException("La descripción excede los 200 caracteres o contiene caracteres no permitidos.");
            }
        }

        // 4. Guardar en Base de Datos (MOCK)[cite: 15]
        // gastoDAO.guardar(nuevoGasto);

        // 5. Mensaje de éxito final[cite: 15]
        System.out.println("Pago externo guardado exitosamente");
    }
}