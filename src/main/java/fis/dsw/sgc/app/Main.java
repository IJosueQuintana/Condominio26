package fis.dsw.sgc.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Inyeccion de dependencias manual
        fis.dsw.sgc.inmuebles.dao.IInmuebleDAO inmuebleDAO = new fis.dsw.sgc.inmuebles.dao.InmuebleDAOMySQL();
        fis.dsw.sgc.inmuebles.service.IInmueblesService servicioInmuebles = new fis.dsw.sgc.inmuebles.service.InmueblesServiceImpl(inmuebleDAO);
        fis.dsw.sgc.reservas.service.ServicioReservasImpl.getInstancia().setServicioInmuebles(servicioInmuebles);

        fis.dsw.sgc.finanzas.dao.IDeudaDAO deudaDAO = new fis.dsw.sgc.finanzas.dao.DeudaDAOImpl();
        fis.dsw.sgc.finanzas.service.IDeudaFactory deudaFactory = new fis.dsw.sgc.finanzas.service.DeudaFactoryImpl();
        fis.dsw.sgc.administracion.service.IGestionUsuariosAPI gestionUsuariosAPI = new fis.dsw.sgc.administracion.service.GestionUsuariosServiceImpl();

        fis.dsw.sgc.finanzas.service.IDeudaService deudaService = new fis.dsw.sgc.finanzas.service.DeudaServiceImpl(
                deudaFactory, deudaDAO, servicioInmuebles, gestionUsuariosAPI);
        fis.dsw.sgc.finanzas.service.IFachadaParaReservas fachada = new fis.dsw.sgc.finanzas.service.FachadaParaReservasImpl(deudaService);
        fis.dsw.sgc.reservas.service.ServicioReservasImpl.getInstancia().setFachadaFinanzas(fachada);

        Parent root = FXMLLoader.load(getClass().getResource("/administracion/fxml/login.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Sistema de Gestión Para Condominio");
        primaryStage.setScene(scene);
        // Permite abrir la interfaz en pantalla completa
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}