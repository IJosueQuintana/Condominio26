package fis.dsw.sgc.administracion.dao;

import fis.dsw.sgc.administracion.model.Usuario;
import fis.dsw.sgc.usuarios.dto.ResidenteFachadaDTO;
import java.util.List;
import java.util.UUID;

public interface IUsuarioDAO {
    void guardar(Usuario usuario);
    void actualizar(Usuario usuario);
    Usuario buscarPorId(UUID idUsuario);
    Usuario buscarPorCorreo(String correo);
    List<Usuario> listarTodos();
    ResidenteFachadaDTO buscarResidentePorCedula(String cedula);
}