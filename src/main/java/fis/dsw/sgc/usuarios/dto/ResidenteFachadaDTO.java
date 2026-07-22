package fis.dsw.sgc.usuarios.dto;

public class ResidenteFachadaDTO {
    private Integer idUsuario;
    private String cedula;
    private String nombreCompleto;
    private String correoElectronico;
    private Integer idDepartamento;

    public ResidenteFachadaDTO(Integer idUsuario, String cedula, String nombreCompleto, String correoElectronico, Integer idDepartamento) {
        this.idUsuario = idUsuario;
        this.cedula = cedula;
        this.nombreCompleto = nombreCompleto;
        this.correoElectronico = correoElectronico;
        this.idDepartamento = idDepartamento;
    }

    public Integer getIdUsuario() { return idUsuario; }
    public String getCedula() { return cedula; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getCorreoElectronico() { return correoElectronico; }
    public Integer getIdDepartamento() { return idDepartamento; }
}
