package fis.dsw.sgc.administracion.dao;

import fis.dsw.sgc.administracion.model.Usuario;
import fis.dsw.sgc.usuarios.dto.ResidenteFachadaDTO;
import java.util.List;

public interface IUsuarioDAO {
    void guardar(Usuario usuario);
    void actualizar(Usuario usuario);
    Usuario buscarPorId(int idUsuario);
    Usuario buscarPorCorreo(String correo);
    List<Usuario> listarTodos();
    ResidenteFachadaDTO buscarResidentePorCedula(String cedula);
}