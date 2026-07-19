package fis.dsw.sgc.finanzas.model;

import java.time.LocalDate;


public class Deuda {
    // --- ATRIBUTOS DE IDENTIFICACIÓN Y RELACIÓN ---
    private int idDeuda;
    private int idUsuario; // Referencia al Residente (Módulo Externo)
    private Integer idInmueble; // Nullable, referencia al Departamento
    private Integer idReserva; // Nullable, por si la deuda es por reserva[cite: 3]

    // --- PATRONES DE DISEÑO ---
    private ITipoDeuda tipoDeuda; // Strategy: Define cómo se calcula el valor
    private IEstadoDeuda estado; // State: Define qué acciones están permitidas[cite: 1]

    // --- ATRIBUTOS DE NEGOCIO ---
    private String descripcion;
    private double valorBase; // Manejamos double en el Model. El DAO lo pasará a centavos.
    private double mora;
    private double total;
    private double saldo;
    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;
    private String observaciones;

    // --- CONSTRUCTOR ---
    public Deuda() {
        this.fechaEmision = LocalDate.now();
    }

    // --- MÉTODOS DE COMPORTAMIENTO (RICH DOMAIN MODEL) ---

    /**
     * Calcula el valor base inicial delegando al patrón Strategy.
     */
    public void calcularValorTotal() {
        // La estrategia (Alicuota, Multa, Reserva) aplica sus fórmulas[cite: 1]
        this.valorBase = tipoDeuda.calcularValor(this.valorBase);
        this.total = this.valorBase + this.mora;
        this.saldo = this.total; // Al inicio, el saldo es igual al total
    }

    /**
     * Caso de uso: pagarDeuda / registrarPagoEfectivoTransferenciaResidente
     */
    public void procesarPago(double montoPagado) {
        // El estado actual dictamina si se puede o no pagar[cite: 1]
        // Si está 'EN_MORA' o 'PENDIENTE', pasa; si está 'ANULADA', lanza excepción.
        estado.procesarPago(this);

        if (montoPagado <= 0) {
            throw new IllegalArgumentException("El monto a pagar debe ser mayor a 0.");
        }

        this.saldo -= montoPagado;
        if (this.saldo < 0) {
            this.saldo = 0;
        }

        // Nota: La transición al estado 'PAGADA' puede hacerse aquí si saldo == 0,
        // o el mismo IEstadoDeuda puede encargarse de transicionar.
    }

    /**
     * Caso de uso: registrarMoraDeuda
     */
    public void aplicarMora() {
        // Delegamos al estado para que cambie su estado interno a 'EN_MORA'[cite: 1]
        estado.aplicarMora(this);

        // Requisito: aplicar un interés del 15% sobre el valor a pagar de la deuda
        this.mora = this.valorBase * 0.15;
        this.total = this.valorBase + this.mora;
        this.saldo += this.mora; // Se le suma la mora al saldo deudor
    }

    /**
     * Caso de uso: eliminarDeuda[cite: 2]
     */
    public void anular() {
        // Delegamos al estado para que transicione a 'ANULADA'[cite: 1]
        estado.anular(this);
        this.saldo = 0; // Una deuda anulada ya no tiene saldo exigible
    }

    /**
     * Caso de uso: modificarFechaMaximaDePagoDeUnaDeuda[cite: 2]
     */
    public void modificarFechaVencimiento(LocalDate nuevaFecha) {
        LocalDate fechaActual = LocalDate.now();

        // Requisito: mayor a la fecha actual y mayor a la fecha de pago actual[cite: 2]
        if (nuevaFecha.isAfter(fechaActual) && nuevaFecha.isAfter(this.fechaVencimiento)) {
            this.fechaVencimiento = nuevaFecha;
        } else {
            // Esto será capturado por tu bloque EXCEPTION
            throw new IllegalArgumentException("La nueva fecha debe ser mayor a la fecha actual y a la fecha de pago actual.");
        }
    }

    // --- GETTERS Y SETTERS ---
    // (Genera todos los getters y setters aquí. Recuerda que getEstado()
    // y setEstado(IEstadoDeuda estado) son cruciales para el patrón State).

    public IEstadoDeuda getEstado() { return this.estado; }
    public void setEstado(IEstadoDeuda estado) { this.estado = estado; }
    public ITipoDeuda getTipoDeuda() { return this.tipoDeuda; }
    public void setTipoDeuda(ITipoDeuda tipoDeuda) { this.tipoDeuda = tipoDeuda; }
    public LocalDate getFechaVencimiento() { return this.fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
    // ... el resto de getters/setters ...
}
