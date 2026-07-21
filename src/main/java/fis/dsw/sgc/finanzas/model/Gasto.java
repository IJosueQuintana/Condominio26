package fis.dsw.sgc.finanzas.model;

import java.time.LocalDate;

public abstract class Gasto {
    protected int idGasto;
    protected String descripcion;
    protected double valor;
    protected LocalDate fechaGasto;

    public Gasto(String descripcion, double valor, LocalDate fechaGasto) {
        this.descripcion = descripcion;
        this.valor = valor;
        this.fechaGasto = fechaGasto;
    }

    // Método abstracto para que cada hijo valide sus propias reglas
    public abstract boolean validarDetalle();

    // Identificador del tipo de gasto para guardarlo en BD o usarlo en reportes
    public abstract String getTipoGasto();

    // Getters
    public int getIdGasto() { return idGasto; }
    public String getDescripcion() { return descripcion; }
    public double getValor() { return valor; }
    public LocalDate getFechaGasto() { return fechaGasto; }
}