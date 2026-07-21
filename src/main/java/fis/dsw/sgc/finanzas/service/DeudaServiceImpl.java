package fis.dsw.sgc.finanzas.service;

import fis.dsw.sgc.finanzas.dto.DeudaConsultadaDTO;
import fis.dsw.sgc.finanzas.dto.NuevaDeudaDTO;
import fis.dsw.sgc.finanzas.model.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeudaServiceImpl implements IDeudaService {

    private IDeudaFactory deudaFactory;

    // Aquí irían inyectados tus DAOs y Fachadas en el futuro
    // private IDeudaDAO deudaDAO;
    // private IUsuariosFacade usuariosFacade;

    public DeudaServiceImpl() {
        this.deudaFactory = new DeudaFactoryImpl();
    }

    @Override
    public void registrarDeuda(NuevaDeudaDTO dto) {


        // 2. Verificar existencia del residente (MOCK - Simulación de fachada)
        // boolean existeResidente = usuariosFacade.existeResidente(dto.getCedulaResidente());
        boolean existeResidente = true;
        if (!existeResidente) {
            throw new IllegalArgumentException("No existe un cliente con el número de cédula de identidad proporcionada.");
        }

        // 3. Crear deuda a través del Factory
        // Convertimos LocalDate a Date legacy para el factory actual
        Date legacyDate = Date.from(dto.getFechaMaximaPago().atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Asumimos un ID de residente mockeado recuperado de la fachada
        int idResidenteMock = 101;

        Deuda nuevaDeuda = deudaFactory.crearDeuda(dto.getMotivoDeuda(), idResidenteMock, dto.getValor(), legacyDate);
        nuevaDeuda.setDescripcion(dto.getDescripcion());

        // 4. Guardar en Base de Datos (MOCK)
        /*
        try {
            // NOTA PARA EL DAO: Multiplicar nuevaDeuda.getValorBase() y saldos por 100 para guardar como INTEGER (centavos)
            // deudaDAO.guardar(nuevaDeuda);
        } catch(Exception e) {
            throw new RuntimeException("Error de base de datos al guardar.");
        }
        */

        System.out.println("Deuda por motivo de " + dto.getMotivoDeuda().toLowerCase() +
                " con el valor de " + dto.getValor() +
                " registrada exitosamente para el residente.");
    }

    @Override
    public void modificarFechaMaximaDePagoDeUnaDeuda(Integer idDeuda, LocalDate nuevaFechaMaximaPago) {
        // 1. Consultar Deuda (MOCK)
        Deuda deudaMock = mockBuscarDeuda(idDeuda);
        if (deudaMock == null) {
            throw new IllegalArgumentException("No existe una deuda con el identificador proporcionado.");
        }

        // 2. Modificar fecha delegando al Modelo Rico (Él hace las validaciones del Caso de Uso)
        deudaMock.modificarFechaVencimiento(nuevaFechaMaximaPago);

        // 3. Actualizar en Base de Datos (MOCK)
        // deudaDAO.actualizar(deudaMock);

        System.out.println("Fecha máxima de pago modificada con éxito");
    }

    @Override
    public void eliminarDeuda(Integer idDeuda) {
        // 1. Consultar Deuda (MOCK)
        Deuda deudaMock = mockBuscarDeuda(idDeuda);
        if (deudaMock == null) {
            throw new IllegalArgumentException("No existe una deuda con el identificador proporcionado.");
        }

        // 2. Delegar anulación al Modelo Rico
        deudaMock.anular(); // Cambiará a EstadoEliminada y pondrá saldo 0

        // 3. Actualizar en Base de Datos (MOCK)
        // deudaDAO.actualizar(deudaMock);

        System.out.println("Deuda Eliminada Exitosamente");
    }

    @Override
    public void pagarDeuda(Integer idDeuda, String metodoPago) {
        // Caso de uso: Pago en Efectivo o Transferencia
        Deuda deudaMock = mockBuscarDeuda(idDeuda);
        if (deudaMock == null) {
            throw new IllegalArgumentException("No existe la deuda.");
        }

        if (metodoPago.equalsIgnoreCase("EFECTIVO") || metodoPago.equalsIgnoreCase("TRANSFERENCIA")) {
            // El estado de la deuda cambia a EN PROCESO esperando validación del contador
            deudaMock.setEstado(new EstadoEnProceso());

            // deudaDAO.actualizar(deudaMock);

            if (metodoPago.equalsIgnoreCase("EFECTIVO")) {
                System.out.println("Acerquese a oficinas de contabilidad para efectuar el pago");
            } else {
                System.out.println("Se revisara el deposito y se actualizara el estado de su deuda en las próximas horas");
            }
        }
    }

    @Override
    public void pagarDeudaTarjeta(Integer idDeuda, String numeroTarjeta, LocalDate fechaVencimientoTarjeta, String nombreTitularTarjeta, Integer ccv) {
        Deuda deudaMock = mockBuscarDeuda(idDeuda);
        if (deudaMock == null) {
            throw new IllegalArgumentException("No existe la deuda.");
        }

        // Simulamos que la plataforma externa aceptó el pago
        boolean pagoAceptado = true; // Aquí iría la integración con pasarela de pagos

        if (pagoAceptado) {
            // Delegamos al modelo rico el procesamiento del pago total
            deudaMock.procesarPago(deudaMock.getSaldo());
            deudaMock.setEstado(new EstadoPagada());

            // deudaDAO.actualizar(deudaMock);

            System.out.println("Deuda cancelada exitosamente");
        }
    }

    @Override
    public List<DeudaConsultadaDTO> solicitarPagoEnCuotas(Integer idDeuda, Integer numeroMesesADiferir) {
        if (numeroMesesADiferir < 3 || numeroMesesADiferir > 11) {
            throw new IllegalArgumentException("El numero de meses a diferir la deuda debe ser de almenos 3 y como máximo 11");
        }

        // 1. Verificar si tiene deudas en mora (MOCK)
        // boolean tieneMora = deudaDAO.verificarDeudasEnMoraPorUsuario(idUsuario);
        boolean tieneMora = false;
        if (tieneMora) {
            throw new IllegalStateException("No puede acceder a este beneficio porque tiene deudas en estado EN MORA");
        }

        Deuda deudaDiferir = mockBuscarDeuda(idDeuda);
        double valorCuota = deudaDiferir.getSaldo() / numeroMesesADiferir;

        List<DeudaConsultadaDTO> cuotasGeneradas = new ArrayList<>();

        // Según tu Modelo de Dominio, creamos N deudas nuevas saltando de 1 en 1 mes
        for (int i = 1; i <= numeroMesesADiferir; i++) {
            Deuda cuota = new Deuda();
            cuota.setIdUsuario(deudaDiferir.getIdUsuario());
            cuota.setTipoDeuda(deudaDiferir.getTipoDeuda()); // Mismo motivo
            cuota.setValorBase(valorCuota);
            cuota.setSaldo(valorCuota);
            cuota.setFechaVencimiento(LocalDate.now().plusMonths(i));
            cuota.setEstado(new EstadoPendiente());
            cuota.setDescripcion("Cuota " + i + " de " + numeroMesesADiferir + " - " + deudaDiferir.getDescripcion());

            // deudaDAO.guardar(cuota); // Guardar en BD cada cuota como deuda independiente

            cuotasGeneradas.add(new DeudaConsultadaDTO(
                    (int)(Math.random() * 1000), // ID simulado
                    cuota.getTipoDeuda().getMotivo(),
                    cuota.getSaldo(),
                    cuota.getFechaVencimiento(),
                    cuota.getEstado().getNombreEstado()
            ));
        }

        // Anulamos o marcamos como procesada la deuda original
        deudaDiferir.setEstado(new EstadoPagada());
        // deudaDAO.actualizar(deudaDiferir);

        System.out.println("Deuda diferida exitosamente");
        return cuotasGeneradas;
    }

    @Override
    public List<DeudaConsultadaDTO> consultarDeuda(String numeroCedulaResidente) {
        // Simulación de búsqueda en Base de Datos de deudas PENDIENTE, EN PROCESO, MORA
        // List<Deuda> deudasBD = deudaDAO.buscarDeudasActivasPorCedula(numeroCedulaResidente);

        List<DeudaConsultadaDTO> deudasActivas = new ArrayList<>();

        // Mapeo Dummy para que el controlador tenga algo que mostrar hoy
        deudasActivas.add(new DeudaConsultadaDTO(1, "ALICUOTA", 150.0, LocalDate.now().plusDays(10), "PENDIENTE"));
        deudasActivas.add(new DeudaConsultadaDTO(2, "MULTA", 25.0, LocalDate.now().minusDays(5), "EN MORA"));

        if (deudasActivas.isEmpty()) {
            System.out.println("El Residente no tiene deudas");
        } else {
            System.out.println("Deudas del Residente");
        }

        return deudasActivas;
    }

    // --- MÉTODOS QUE SE HARÁN DESPUÉS ---
    @Override
    public void registrarDeudaAlicuotaMensual(String numeroCedulaResidente) {}
    @Override
    public void enviarRecordatorioDeudaPendiente(String numeroCedulaResidente) {}
    @Override
    public void registrarMoraDeuda(String numeroCedulaResidente) {}


    // --- UTILS (MOCKS Y VALIDACIONES) ---

//    private boolean validarCedulaEcuatoriana(String cedula) {
//        // Implementación básica del algoritmo del Módulo 10 de Ecuador
//        if (cedula == null || cedula.length() != 10) return false;
//        try {
//            int provincia = Integer.parseInt(cedula.substring(0, 2));
//            if (provincia < 1 || provincia > 24) return false;
//
//            int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};
//            int suma = 0;
//            for (int i = 0; i < 9; i++) {
//                int digito = Character.getNumericValue(cedula.charAt(i));
//                int producto = digito * coeficientes[i];
//                if (producto >= 10) producto -= 9;
//                suma += producto;
//            }
//            int digitoVerificador = Character.getNumericValue(cedula.charAt(9));
//            int decenaSuperior = (suma + 9) / 10 * 10;
//            int calculado = decenaSuperior - suma;
//            if (calculado == 10) calculado = 0;
//
//            return calculado == digitoVerificador;
//        } catch (NumberFormatException e) {
//            return false;
//        }
//    }

    private Deuda mockBuscarDeuda(Integer idDeuda) {
        // Genera una deuda falsa para que los métodos no den NullPointerException hoy
        Deuda d = new Deuda();
        d.setIdDeuda(idDeuda);
        d.setValorBase(100.0);
        d.setSaldo(100.0);
        d.setFechaVencimiento(LocalDate.now().plusDays(15));
        d.setEstado(new EstadoPendiente());
        d.setTipoDeuda(new DeudaAlicuota(100, 1000));
        return d;
    }
}