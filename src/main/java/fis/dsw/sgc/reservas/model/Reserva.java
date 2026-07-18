package fis.dsw.sgc.reservas.model;

import java.util.Date;
import java.sql.Time;

public class Reserva {
    private String idReserva;
    private Date fechaReserva;
    private Time horaInicio;
    private Time horaFin;
    private String estado;
    
    public String getIdReserva() { return idReserva; }
    public void setIdReserva(String idReserva) { this.idReserva = idReserva; }
    
    public Date getFechaReserva() { return fechaReserva; }
    public void setFechaReserva(Date fechaReserva) { this.fechaReserva = fechaReserva; }
    
    public Time getHoraInicio() { return horaInicio; }
    public void setHoraInicio(Time horaInicio) { this.horaInicio = horaInicio; }
    
    public Time getHoraFin() { return horaFin; }
    public void setHoraFin(Time horaFin) { this.horaFin = horaFin; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    private String nombreResidente;
    public String getNombreResidente() { return nombreResidente; }
    public void setNombreResidente(String nombreResidente) { this.nombreResidente = nombreResidente; }
}
