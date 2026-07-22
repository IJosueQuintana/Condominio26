package fis.dsw.sgc.check_in.model;

public class VisitaProgramada {

    private int idVisita;
    private Integer idResidente;

    private String nombresVisita;
    private String apellidosVisita;
    private String cedulaVisita;

    private String fechaProgramada;
    private String horaProgramada;
    private String estado;
    private String motivoVisita;
    private String tipoVisita;
    private String informacionAdicional;
    private String placa;

    public VisitaProgramada() {}

    public VisitaProgramada(int idVisita, int idResidente, String nombresVisita,
                            String apellidosVisita, String cedulaVisita, String fechaProgramada,
                            String horaProgramada, String estado, String motivoVisita) {
        this.idVisita = idVisita;
        this.idResidente = idResidente;
        this.nombresVisita = nombresVisita;
        this.apellidosVisita = apellidosVisita;
        this.cedulaVisita = cedulaVisita;
        this.fechaProgramada = fechaProgramada;
        this.horaProgramada = horaProgramada;
        this.estado = estado;
        this.motivoVisita = motivoVisita;
    }

    // Getters y setters
    public int getIdVisita() {
        return idVisita;
    }

    public void setIdVisita(int idVisita) {
        this.idVisita = idVisita;
    }

    public Integer getIdResidente() {
        return idResidente;
    }

    public void setIdResidente(int idResidente) {
        this.idResidente = idResidente;
    }

    public String getNombresVisita() {
        return nombresVisita;
    }

    public void setNombresVisita(String nombresVisita) {
        this.nombresVisita = nombresVisita;
    }

    public String getApellidosVisita() {
        return apellidosVisita;
    }

    public void setApellidosVisita(String apellidosVisita) {
        this.apellidosVisita = apellidosVisita;
    }

    public String getCedulaVisita() {
        return cedulaVisita;
    }

    public void setCedulaVisita(String cedulaVisita) {
        this.cedulaVisita = cedulaVisita;
    }


    public String getFechaProgramada() {
        return fechaProgramada;
    }

    public void setFechaProgramada(String fechaProgramada) {
        this.fechaProgramada = fechaProgramada;
    }

    public String getHoraProgramada() {
        return horaProgramada;
    }

    public void setHoraProgramada(String horaProgramada) {
        this.horaProgramada = horaProgramada;
    }


    public String getEstado() {
        return estado.toString();
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMotivoVisita() {
        return motivoVisita;
    }

    public void setMotivoVisita(String motivoVisita) {
        this.motivoVisita = motivoVisita;
    }

    public String getTipoVisita() {
        return tipoVisita;
    }

    public void setTipoVisita(String tipoVisita) {
        this.tipoVisita = tipoVisita;
    }

    public String getInformacionAdicional() {
        return informacionAdicional;
    }

    public void setInformacionAdicional(String informacionAdicional) {
        this.informacionAdicional = informacionAdicional;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }
}
