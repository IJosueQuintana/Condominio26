package fis.dsw.sgc.administracion.dao;

import fis.dsw.sgc.administracion.model.Cuenta;

public interface ICuentaDAO {
    void guardar(Cuenta cuenta);
    void actualizar(Cuenta cuenta);
    Cuenta buscarPorIdUsuario(int idUsuario);
    Cuenta autenticar(String correo, String contrasena);
}