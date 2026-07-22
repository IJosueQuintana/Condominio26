package fis.dsw.sgc.administracion.dao;

import fis.dsw.sgc.administracion.model.Rol;
import java.util.List;

public interface IRolDAO {
    void guardar(Rol rol);
    void actualizar(Rol rol);
    List<Rol> listarTodos();
    void asignarRolAUsuario(int idUsuario, int idRol);
}