package fis.dsw.sgc.finanzas.service;

import fis.dsw.sgc.finanzas.dto.PagoTarjetaDTO;
import fis.dsw.sgc.finanzas.model.*;

import java.time.LocalDate;

public class PagoServiceImpl implements IPagoService {

    private IPagoFactory pagoFactory;

    // DAOs inyectados a futuro
    // private IPagoDAO pagoDAO;
    // private IDeudaDAO deudaDAO;

    public PagoServiceImpl() {
        this.pagoFactory = new PagoFactoryImpl();
    }

    @Override
    public void registrarPagoEfectivoTransferenciaResidente(Integer idDeuda) {
        // 1. El Sistema verifica el idDeuda[cite: 14].
        Deuda deudaMock = mockBuscarDeuda(idDeuda);

        // 2. SI NO existe, lanza error (Escenario Alterno 1)[cite: 14]
        if (deudaMock == null) {
            throw new IllegalArgumentException("No existe una deuda con el identificador proporcionado");
        }

        // 3. SI existe, verifica el estado de la Deuda[cite: 14]
        String estadoActual = deudaMock.getEstado().getNombreEstado();

        // 4. SI el estado es 'PAGADA', lanza error (Escenario Alterno 2)[cite: 14]
        if (estadoActual.equals("PAGADA")) {
            throw new IllegalStateException("Esta deuda ya ha sido pagada");
        }

        // 5. SI el estado es 'EN PROCESO', cambia a 'PAGADA' y registra el Pago[cite: 14]
        if (estadoActual.equals("EN PROCESO")) {

            // Delegamos al modelo rico procesar el pago final
            deudaMock.setEstado(new EstadoPagada());
            // deudaDAO.actualizar(deudaMock); // MOCK

            // Generamos la entidad Pago oficial a través del Factory
            Pago nuevoPago = pagoFactory.crearPago("EFECTIVO", deudaMock, deudaMock.getSaldo(), null);
            nuevoPago.ejecutarCobro();
            nuevoPago.setEstado("VALIDADO");

            // pagoDAO.guardar(nuevoPago); // MOCK

            // 6. Muestra mensaje[cite: 14]
            System.out.println("Pago registrado exitosamente");
        } else {
            throw new IllegalStateException("La deuda no se encuentra 'EN PROCESO' para ser validada.");
        }
    }

    // --- MÉTODOS MOVIDOS DESDE DEUDA SERVICE ---

    @Override
    public void pagarDeuda(Integer idDeuda, String metodoPago) {
        Deuda deudaMock = mockBuscarDeuda(idDeuda);
        if (deudaMock == null) throw new IllegalArgumentException("No existe la deuda.");

        if (metodoPago.equalsIgnoreCase("EFECTIVO") || metodoPago.equalsIgnoreCase("TRANSFERENCIA")) {
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
    public void pagarDeudaTarjeta(PagoTarjetaDTO dto) {
        Deuda deudaMock = mockBuscarDeuda(dto.getIdDeuda());
        if (deudaMock == null) {
            throw new IllegalArgumentException("No existe la deuda.");
        }

        // Validamos rápidamente que la tarjeta no esté caducada
        if (dto.getFechaVencimiento().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La tarjeta ingresada se encuentra caducada.");
        }

        // Usamos el Factory para orquestar el pago, pasándole el número desde el DTO
        Pago pagoTarjeta = pagoFactory.crearPago("TARJETA", deudaMock, deudaMock.getSaldo(), dto.getNumeroTarjeta());

        if (pagoTarjeta.ejecutarCobro()) {
            deudaMock.procesarPago(deudaMock.getSaldo());
            deudaMock.setEstado(new EstadoPagada());

            // deudaDAO.actualizar(deudaMock);
            // pagoDAO.guardar(pagoTarjeta);

            System.out.println("Deuda cancelada exitosamente con tarjeta a nombre de: " + dto.getNombreTitular());
        }
    }

    // --- UTILITARIO MOCK ---
    private Deuda mockBuscarDeuda(Integer idDeuda) {
        Deuda d = new Deuda();
        d.setIdDeuda(idDeuda);
        d.setValorBase(150.0);
        d.setSaldo(150.0);

        // Lo ponemos "EN PROCESO" por defecto para que tu prueba básica pase hoy
        d.setEstado(new EstadoEnProceso());
        d.setTipoDeuda(new DeudaAlicuota(100, 1000));
        return d;
    }
}