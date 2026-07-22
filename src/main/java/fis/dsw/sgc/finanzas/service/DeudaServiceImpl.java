package fis.dsw.sgc.finanzas.service;

import fis.dsw.sgc.administracion.exception.ResidenteNoExisteException;
import fis.dsw.sgc.administracion.service.GestionUsuariosServiceImpl;
import fis.dsw.sgc.administracion.service.IGestionUsuariosAPI;
import fis.dsw.sgc.finanzas.dao.DeudaDAOImpl;
import fis.dsw.sgc.inmuebles.service.InmueblesServiceImpl;
import fis.dsw.sgc.usuarios.dto.ResidenteFachadaDTO;
import fis.dsw.sgc.finanzas.dao.IDeudaDAO;
import fis.dsw.sgc.finanzas.dto.*;
import fis.dsw.sgc.finanzas.exception.DeudaNoExisteException;
import fis.dsw.sgc.finanzas.exception.NoSePuedeDiferirException;
import fis.dsw.sgc.finanzas.model.*;
import fis.dsw.sgc.inmuebles.dto.DimensionesInmuebleDTO;
import fis.dsw.sgc.inmuebles.service.IInmueblesService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeudaServiceImpl implements IDeudaService {

    private IDeudaFactory deudaFactory;
    private IDeudaDAO deudaDAO;
    private IInmueblesService inmueblesService;
    private IGestionUsuariosAPI gestionUsuariosAPI;

    // Inyección de dependencias para cumplir con buenas prácticas y pruebas unitarias
    public DeudaServiceImpl(IDeudaFactory deudaFactory, IDeudaDAO deudaDAO, IInmueblesService inmueblesService, IGestionUsuariosAPI gestionUsuariosAPI) {
        this.deudaFactory = deudaFactory;
        this.deudaDAO = deudaDAO;
        this.inmueblesService = inmueblesService;
        this.gestionUsuariosAPI = gestionUsuariosAPI;
    }

    public DeudaServiceImpl(){
        this.deudaFactory = new DeudaFactoryImpl();
        this.deudaDAO = new DeudaDAOImpl();
        this.inmueblesService = new InmueblesServiceImpl();
        this.gestionUsuariosAPI = new GestionUsuariosServiceImpl();
    }

    @Override
    public void registrarDeuda(NuevaDeudaDTO dto) {
        // 1 y 2. Consultar Fachada de Usuarios (El método lanza excepción si no existe)
        try{
            ResidenteFachadaDTO residente = gestionUsuariosAPI.obtenerResidentePorCedula(dto.getCedulaResidente());
            Date legacyDate = Date.from(dto.getFechaMaximaPago().atStartOfDay(ZoneId.systemDefault()).toInstant());
            Deuda nuevaDeuda;

            // 3. Crear deuda a través del Factory
            if (dto.getMotivoDeuda().equalsIgnoreCase("ALICUOTA")) {
                // Consultamos la fachada de Inmuebles usando el ID que nos dio la fachada de Usuarios
                DimensionesInmuebleDTO dimensiones = inmueblesService.obtenerDimensiones(residente.getIdDepartamento());

                nuevaDeuda = deudaFactory.crearDeudaAlicuota(
                        residente.getIdUsuario(),
                        dto.getValor(),
                        legacyDate,
                        dimensiones.getTamanoMetrosCuadradosDepartamento(),
                        dimensiones.getTamanoMetrosCuadradosCondominio()
                );
            } else {
                nuevaDeuda = deudaFactory.crearDeuda(dto.getMotivoDeuda(), residente.getIdUsuario(), dto.getValor(), legacyDate);
            }

            nuevaDeuda.setDescripcion(dto.getDescripcion());

            // 4. Guardar en Base de Datos Real
            deudaDAO.guardar(nuevaDeuda);

            System.out.println("Deuda por motivo de " + dto.getMotivoDeuda().toLowerCase() +
                    " con el valor de " + dto.getValor() +
                    " registrada exitosamente para el residente " + residente.getNombreCompleto());
        }catch (ResidenteNoExisteException e){
            throw new RuntimeException(e.getMessage());
        }



    }

    @Override
    public void modificarFechaMaximaDePagoDeUnaDeuda(Integer idDeuda, LocalDate nuevaFechaMaximaPago) {
        Deuda deudaBD = deudaDAO.buscarPorId(idDeuda);
        if (deudaBD == null) {
            throw new DeudaNoExisteException("No existe una deuda con el identificador proporcionado.");
        }

        // Delegamos al Modelo Rico
        deudaBD.modificarFechaVencimiento(nuevaFechaMaximaPago);

        // Actualizamos BD
        deudaDAO.actualizar(deudaBD);
        System.out.println("Fecha máxima de pago modificada con éxito");
    }

    @Override
    public void eliminarDeuda(Integer idDeuda) {
        Deuda deudaBD = deudaDAO.buscarPorId(idDeuda);
        if (deudaBD == null) {
            throw new DeudaNoExisteException("No existe una deuda con el identificador proporcionado.");
        }

        deudaBD.anular(); // Transiciona a EstadoEliminada (ANULADA para la BD)
        deudaDAO.actualizar(deudaBD);
        System.out.println("Deuda Eliminada Exitosamente");
    }

    @Override
    public List<CuotaDTO> solicitarPagoEnCuotas(Integer idDeuda, Integer numeroMesesADiferir) {
        if (numeroMesesADiferir < 3 || numeroMesesADiferir > 12) {
            throw new NoSePuedeDiferirException("El número de meses a diferir la deuda debe ser de al menos 3 y como máximo 12.");
        }

        Deuda deudaDiferir = deudaDAO.buscarPorId(idDeuda);
        if (deudaDiferir == null) throw new DeudaNoExisteException("No existe la deuda a diferir.");

        // Verificamos mora en base de datos real
        if (deudaDAO.verificarDeudasEnMoraPorUsuario(deudaDiferir.getIdUsuario())) {
            throw new NoSePuedeDiferirException("No puede ser beneficiario a este beneficio porque tiene deudas en estado EN MORA");
        }

        double valorCuota = deudaDiferir.getSaldo() / numeroMesesADiferir;
        List<CuotaDTO> cuotasGeneradas = new ArrayList<>();

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

            deudaDAO.guardar(cuota); // Guardamos cuota real

            String numeroCuotaStr = "Cuota " + i + " de " + numeroMesesADiferir;
            String valorStr = String.format(java.util.Locale.US, "%.2f", valorCuota);
            cuotasGeneradas.add(new CuotaDTO(numeroCuotaStr, fechaVencimientoCuota.toString(), valorStr));
        }

        // CLAVE: La anulamos para no duplicar los ingresos en los reportes
        deudaDiferir.anular();
        deudaDAO.actualizar(deudaDiferir);

        System.out.println("Deuda diferida exitosamente en " + numeroMesesADiferir + " cuotas.");
        return cuotasGeneradas;
    }

    @Override
    public List<DeudaConsultadaDTO> consultarDeuda(String numeroCedulaResidente) {
        try{
            ResidenteFachadaDTO residente = gestionUsuariosAPI.obtenerResidentePorCedula(numeroCedulaResidente);
            List<Deuda> deudasBD = deudaDAO.buscarDeudasActivasPorUsuario(residente.getIdUsuario());

            List<DeudaConsultadaDTO> deudasActivas = new ArrayList<>();

            for (Deuda d : deudasBD) {
                // Nota la magia aquí: usamos getNombreEstadoUI() para la interfaz de usuario en JavaFX
                deudasActivas.add(new DeudaConsultadaDTO(
                        d.getIdDeuda(),
                        d.getTipoDeuda().getMotivo(),
                        d.getSaldo(),
                        d.getFechaVencimiento(),
                        d.getEstado().getNombreEstadoUI()
                ));
            }

            if (deudasActivas.isEmpty()) {
                System.out.println("El Residente no tiene deudas");
            } else {
                System.out.println("Deudas del Residente");
            }

            return deudasActivas;
        }catch (ResidenteNoExisteException e){
            throw new RuntimeException(e.getMessage());
        }


    }

    @Override
    public void registrarDeudaAlicuotaMensual(String numeroCedulaResidente) {
        if (LocalDate.now().getDayOfMonth() != 1) return;

        try{
            ResidenteFachadaDTO residente = gestionUsuariosAPI.obtenerResidentePorCedula(numeroCedulaResidente);
            DimensionesInmuebleDTO dimensiones = inmueblesService.obtenerDimensiones(residente.getIdDepartamento());

            // Este valor lo pueden consultar luego de la tabla de configuraciones si tu equipo lo requiere
            double valorAlicuotaBase = 150.00;

            Deuda nuevaDeuda = deudaFactory.crearDeudaAlicuota(
                    residente.getIdUsuario(),
                    valorAlicuotaBase,
                    Date.from(LocalDate.now().plusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    dimensiones.getTamanoMetrosCuadradosDepartamento(),
                    dimensiones.getTamanoMetrosCuadradosCondominio()
            );
            nuevaDeuda.setDescripcion("Alícuota mensual generada automáticamente");

            deudaDAO.guardar(nuevaDeuda);
            System.out.println("Deuda de alícuota mensual registrada con éxito para el residente con cédula: " + numeroCedulaResidente);
        }catch (ResidenteNoExisteException e){
            throw new RuntimeException(e.getMessage());
        }


    }

    @Override
    public void enviarRecordatorioDeudaPendiente(String numeroCedulaResidente) {
        try{
            ResidenteFachadaDTO residente = gestionUsuariosAPI.obtenerResidentePorCedula(numeroCedulaResidente);
            List<Deuda> deudasBD = deudaDAO.buscarDeudasActivasPorUsuario(residente.getIdUsuario());

            LocalDate hoy = LocalDate.now();

            for (Deuda deuda : deudasBD) {
                if (deuda.getEstado() instanceof EstadoPendiente) {
                    long diasRestantes = java.time.temporal.ChronoUnit.DAYS.between(hoy, deuda.getFechaVencimiento());

                    if (diasRestantes >= 0 && diasRestantes <= 3) {
                        System.out.println("MÓDULO COMUNICACIÓN (MOCK): Enviando correo a " + residente.getCorreoElectronico() +
                                " -> Su deuda vence en " + diasRestantes + " días.");
                    }
                }
            }
        }catch (ResidenteNoExisteException e){
            throw new RuntimeException(e.getMessage());
        }


    }

    @Override
    public void registrarMoraDeuda(String numeroCedulaResidente) {
        try{
            ResidenteFachadaDTO residente = gestionUsuariosAPI.obtenerResidentePorCedula(numeroCedulaResidente);
            List<Deuda> deudasBD = deudaDAO.buscarDeudasActivasPorUsuario(residente.getIdUsuario());

            LocalDate hoy = LocalDate.now();

            for (Deuda deuda : deudasBD) {
                if (deuda.getEstado() instanceof EstadoPendiente && deuda.getFechaVencimiento().isBefore(hoy)) {
                    deuda.aplicarMora();
                    deudaDAO.actualizar(deuda); // Se guardan el nuevo saldo y estado
                    System.out.println("Mora del 15% aplicada a deuda ID: " + deuda.getIdDeuda());
                }
            }
        }catch (ResidenteNoExisteException e){
            throw new RuntimeException(e.getMessage());
        }

    }
}