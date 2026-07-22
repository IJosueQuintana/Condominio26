package fis.dsw.sgc.check_in.model;

public class IngresoParqueadero {
    private int idIngresoParqueadero;
    private int idRegistroEntrada;
    private int idParqueadero;
    private String fechaHoraIngreso;
    private String fechaHoraSalida;
    private String estado;

    public IngresoParqueadero() {
        this.estado = "OCUPADO";
    }

    public IngresoParqueadero(int idIngresoParqueadero, int idRegistroEntrada, int idParqueadero,
                              String fechaHoraIngreso, String estado) {
        this.idIngresoParqueadero = idIngresoParqueadero;
        this.idRegistroEntrada = idRegistroEntrada;
        this.idParqueadero = idParqueadero;
        this.fechaHoraIngreso = fechaHoraIngreso;
        this.estado = estado;
    }

    public int getIdIngresoParqueadero() {
        return idIngresoParqueadero;
    }

    public void setIdIngresoParqueadero(int idIngresoParqueadero) {
        this.idIngresoParqueadero = idIngresoParqueadero;
    }

    public int getIdRegistroEntrada() {
        return idRegistroEntrada;
    }

    public void setIdRegistroEntrada(int idRegistroEntrada) {
        this.idRegistroEntrada = idRegistroEntrada;
    }

    public int getIdParqueadero() {
        return idParqueadero;
    }

    public void setIdParqueadero(int idParqueadero) {
        this.idParqueadero = idParqueadero;
    }

    public String getFechaHoraIngreso() {
        return fechaHoraIngreso;
    }

    public void setFechaHoraIngreso(String fechaHoraIngreso) {
        this.fechaHoraIngreso = fechaHoraIngreso;
    }

    public String getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    public void setFechaHoraSalida(String fechaHoraSalida) {
        this.fechaHoraSalida = fechaHoraSalida;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
