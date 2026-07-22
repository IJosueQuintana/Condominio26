package fis.dsw.sgc.administracion.dao;

import java.util.List;

public interface IPermisoDAO {
    boolean existePermisoParaCuenta(int idCuenta, String nombrePermiso);
    List<String> listarPermisosPorCuenta(int idCuenta);
}
