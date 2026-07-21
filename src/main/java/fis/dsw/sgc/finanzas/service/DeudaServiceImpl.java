package fis.dsw.sgc.finanzas.service;

import fis.dsw.sgc.finanzas.dto.CuotaDTO;
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
    public List<CuotaDTO> solicitarPagoEnCuotas(Integer idDeuda, Integer numeroMesesADiferir) {
        if (numeroMesesADiferir < 3 || numeroMesesADiferir > 12) {
            throw new IllegalArgumentException("El número de meses a diferir la deuda debe ser de al menos 3 y como máximo 12.");
        }

        // 1. Consultar si tiene deudas en mora (MOCK)
        // boolean tieneMora = deudaDAO.verificarDeudasEnMoraPorUsuario(deudaDiferir.getIdUsuario());
        boolean tieneMora = false;
        if (tieneMora) {
            throw new IllegalStateException("No puede ser beneficiario a este beneficio porque tiene deudas en estado EN MORA");
        }

        // 2. Obtener la deuda original a diferir
        Deuda deudaDiferir = mockBuscarDeuda(idDeuda);

        // 3. Generar las nuevas Deudas (Cuotas) y los DTOs
        double valorCuota = deudaDiferir.getSaldo() / numeroMesesADiferir;
        List<CuotaDTO> cuotasGeneradas = new ArrayList<>();

        // Creamos N deudas como dicta el Modelo de Dominio, saltando de un mes en un mes
        for (int i = 1; i <= numeroMesesADiferir; i++) {
            Deuda cuota = new Deuda();
            cuota.setIdUsuario(deudaDiferir.getIdUsuario());
            cuota.setTipoDeuda(deudaDiferir.getTipoDeuda());
            cuota.setValorBase(valorCuota);
            cuota.setSaldo(valorCuota);

            LocalDate fechaVencimientoCuota = LocalDate.now().plusMonths(i);
            cuota.setFechaVencimiento(fechaVencimientoCuota);

            cuota.setEstado(new EstadoPendiente());
            cuota.setDescripcion("Cuota " + i + " de " + numeroMesesADiferir + " - " + deudaDiferir.getDescripcion());

            // deudaDAO.guardar(cuota); // MOCK: El DAO multiplicaría el saldo por 100 aquí

            // --- TRANSFORMACIÓN AL NUEVO CuotaDTO ---
            // Formateamos los datos requeridos como String
            String numeroCuotaStr = "Cuota " + i + " de " + numeroMesesADiferir;
            String fechaMaximaPagoStr = fechaVencimientoCuota.toString(); // Formato: YYYY-MM-DD
            // Usamos String.format para asegurar que el valor tenga 2 decimales
            String valorStr = String.format(java.util.Locale.US, "%.2f", valorCuota);

            cuotasGeneradas.add(new CuotaDTO(numeroCuotaStr, fechaMaximaPagoStr, valorStr));
        }

        // 4. Anular/Pagar la deuda original porque ya fue diferida
        deudaDiferir.setEstado(new EstadoPagada());
        // deudaDAO.actualizar(deudaDiferir);

        System.out.println("Deuda diferida exitosamente en " + numeroMesesADiferir + " cuotas.");
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

    @Override
    public void registrarDeudaAlicuotaMensual(String numeroCedulaResidente) {
        // 1. El Sistema verifica si es el primer día del mes
        if (LocalDate.now().getDayOfMonth() != 1) {
            System.out.println("Hoy no es el primer día del mes. No se genera alícuota automática.");
            return;
        }

        // 2. Verificar existencia del residente (MOCK)
        boolean existeResidente = true; // usuariosFacade.existeResidente(numeroCedulaResidente);
        if (!existeResidente) {
            throw new IllegalArgumentException("No existe un residente con la cédula proporcionada.");
        }

        int idResidenteMock = 101;
        double valorAlicuota = 150.00; // Esto luego vendrá del DAO consultando 'configuracion_alicuota'

        // 3. Generar la deuda
        Deuda nuevaDeuda = deudaFactory.crearDeuda(
                "ALICUOTA",
                idResidenteMock,
                valorAlicuota,
                Date.from(LocalDate.now().plusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant()) // Damos 10 días para pagar
        );
        nuevaDeuda.setDescripcion("Alícuota mensual generada automáticamente");

        // deudaDAO.guardar(nuevaDeuda);

        System.out.println("Deuda de alícuota mensual registrada con éxito para el residente con cédula: " + numeroCedulaResidente);
    }

    @Override
    public void enviarRecordatorioDeudaPendiente(String numeroCedulaResidente) {
        // 1. Consultar deudas del residente
        List<Deuda> deudasDelResidente = mockObtenerDeudasResidente(numeroCedulaResidente);
        LocalDate hoy = LocalDate.now();

        for (Deuda deuda : deudasDelResidente) {
            // 2. Verificar que esté PENDIENTE
            if (deuda.getEstado() != null && deuda.getEstado().getNombreEstado().equals("PENDIENTE")) {

                // Calcular días restantes (utilizamos ChronoUnit de java.time)
                long diasRestantes = java.time.temporal.ChronoUnit.DAYS.between(hoy, deuda.getFechaVencimiento());

                // 3. Si quedan 3 días o menos (pero no está vencida)
                if (diasRestantes >= 0 && diasRestantes <= 3) {
                    // MOCK del Módulo de Comunicación (Fachada)
                    // comunicacionFacade.enviarNotificacionParticular(numeroCedulaResidente, "Estimado residente, le recordamos que su deuda vence en " + diasRestantes + " días.");

                    System.out.println("MÓDULO COMUNICACIÓN (MOCK): Enviando recordatorio a " + numeroCedulaResidente +
                            " -> Su deuda por " + deuda.getTipoDeuda().getMotivo() + " vence en " + diasRestantes + " días.");
                }
            }
        }
    }

    @Override
    public void registrarMoraDeuda(String numeroCedulaResidente) {
        // 1. Consultar deudas del residente
        List<Deuda> deudasDelResidente = mockObtenerDeudasResidente(numeroCedulaResidente);
        LocalDate hoy = LocalDate.now();

        for (Deuda deuda : deudasDelResidente) {
            // 2. Verificar que esté PENDIENTE
            if (deuda.getEstado() != null && deuda.getEstado().getNombreEstado().equals("PENDIENTE")) {

                // 3. Verificar si la fecha máxima de pago ya pasó
                if (deuda.getFechaVencimiento().isBefore(hoy)) {

                    // ¡Aquí brilla tu Modelo de Dominio Rico! Le delegamos la lógica de negocio a la entidad
                    deuda.aplicarMora();

                    // MOCK: Actualizar la deuda en base de datos
                    // deudaDAO.actualizar(deuda); // El DAO aquí convertiría el nuevo saldo a centavos

                    System.out.println("Mora del 15% aplicada automáticamente a la deuda ID: " + deuda.getIdDeuda() +
                            ". Nuevo saldo con mora: " + deuda.getSaldo());
                }
            }
        }
    }

    // --- METODO UTILITARIO PARA LOS MOCKS DE CONSULTA ---
    private List<Deuda> mockObtenerDeudasResidente(String cedula) {
        List<Deuda> lista = new ArrayList<>();

        // Deuda 1: Vencida (Para probar registrarMoraDeuda)
        Deuda d1 = mockBuscarDeuda(1);
        d1.setFechaVencimiento(LocalDate.now().minusDays(5));

        // Deuda 2: Por vencer en 2 días (Para probar enviarRecordatorio)
        Deuda d2 = mockBuscarDeuda(2);
        d2.setFechaVencimiento(LocalDate.now().plusDays(2));

        lista.add(d1);
        lista.add(d2);
        return lista;
    }


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