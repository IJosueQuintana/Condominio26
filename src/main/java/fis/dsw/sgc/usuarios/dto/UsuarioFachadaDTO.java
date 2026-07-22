package fis.dsw.sgc.usuarios.dto;

public class UsuarioFachadaDTO {
    private final int idUsuario;
    private final String cedula;

    public UsuarioFachadaDTO(int idUsuario, String cedula) {
        this.idUsuario = idUsuario;
        this.cedula = cedula;
    }

    public int getIdUsuario() { return idUsuario; }
    public String getCedula() { return cedula; }
}
