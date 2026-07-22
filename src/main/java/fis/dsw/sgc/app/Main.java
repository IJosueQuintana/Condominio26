package fis.dsw.sgc.app;

import fis.dsw.sgc.finanzas.service.FachadaParaReservasImpl;
import fis.dsw.sgc.inmuebles.service.InmueblesServiceImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // INYECCIÓN DE DEPENDENCIAS PARA TODOS LOS MODULOS


        // INSTANCIAS DE FACTORYS

        fis.dsw.sgc.finanzas.service.IDeudaFactory deudaFactory = new fis.dsw.sgc.finanzas.service.DeudaFactoryImpl();
        fis.dsw.sgc.finanzas.service.GastoFactoryImpl gastoFactory = new fis.dsw.sgc.finanzas.service.GastoFactoryImpl();
        fis.dsw.sgc.finanzas.service.IPagoFactory pagoFactory = new fis.dsw.sgc.finanzas.service.PagoFactoryImpl();

        // INSTANCIAS DE DAOS

        fis.dsw.sgc.finanzas.dao.IDeudaDAO deudaDAO = new fis.dsw.sgc.finanzas.dao.DeudaDAOImpl();
        fis.dsw.sgc.finanzas.dao.IPagoDAO pagoDAO = new fis.dsw.sgc.finanzas.dao.PagoDAOImpl();
        fis.dsw.sgc.finanzas.dao.IGastoDAO gastoDAO = new fis.dsw.sgc.finanzas.dao.GastoDAOImpl();
        fis.dsw.sgc.finanzas.dao.IReportesDAO reportesDAO = new fis.dsw.sgc.finanzas.dao.ReportesDAOImpl();

        fis.dsw.sgc.inmuebles.dao.IInmuebleDAO inmuebleDAO = new  fis.dsw.sgc.inmuebles.dao.InmuebleDAOMySQL();
        fis.dsw.sgc.inmuebles.dao.IEspacioReservableDAO espacioReservableDAO = new  fis.dsw.sgc.inmuebles.dao.EspacioReservableDAO();
        fis.dsw.sgc.inmuebles.dao.ICasoFortuitoDAO casoFortuitoDAO = new  fis.dsw.sgc.inmuebles.dao.CasoFortuitoDAOMySQL();

        fis.dsw.sgc.check_in.dao.IAlertaSeguridadDAO alertaSeguridadDAO = new fis.dsw.sgc.check_in.dao.AlertaSeguridadDAO();
        fis.dsw.sgc.check_in.dao.IProgramacionVisitaDAO programacionVisitaDAO = new fis.dsw.sgc.check_in.dao.ProgramacionVisitaDAO();
        fis.dsw.sgc.check_in.dao.IRegistroEntradaDAO registroEntradaDAO = new fis.dsw.sgc.check_in.dao.RegistroEntradaDAO();

        fis.dsw.sgc.reservas.dao.IReservaDAO reservaDAO = new fis.dsw.sgc.reservas.dao.ReservaDAOSQLite();
        fis.dsw.sgc.reservas.dao.IObservacionReservaDAO observacionReservaDAO = new fis.dsw.sgc.reservas.dao.ObservacionReservaDAOSQLite();

        fis.dsw.sgc.administracion.dao.ICuentaDAO cuentaDAO = new fis.dsw.sgc.administracion.dao.CuentaDAOMySQL();
        fis.dsw.sgc.administracion.dao.IPermisoDAO permisoDAO = new fis.dsw.sgc.administracion.dao.PermisoDAOMySQL();
        fis.dsw.sgc.administracion.dao.IRolDAO rolDAO = new fis.dsw.sgc.administracion.dao.RolDAOMySQL();
        fis.dsw.sgc.administracion.dao.IUsuarioDAO usuarioDAO = new fis.dsw.sgc.administracion.dao.UsuarioDAOMySQL();

        fis.dsw.sgc.comunicacion.dao.IComunicacionDAO comunicacionDAO = new  fis.dsw.sgc.comunicacion.dao.ComunicacionDAOSQLite();

        // FACHADAS Y SERVICE PARA FACHADAS
        fis.dsw.sgc.administracion.service.IGestionUsuariosAPI usuariosService = new  fis.dsw.sgc.administracion.service.GestionUsuariosServiceImpl();
        fis.dsw.sgc.inmuebles.service.IInmueblesService inmueblesService = new fis.dsw.sgc.inmuebles.service.InmueblesServiceImpl(inmuebleDAO,casoFortuitoDAO,espacioReservableDAO);
        fis.dsw.sgc.finanzas.service.IDeudaService deudaService = new  fis.dsw.sgc.finanzas.service.DeudaServiceImpl(deudaFactory,deudaDAO,inmueblesService,usuariosService);
        fis.dsw.sgc.finanzas.service.IFachadaParaReservas fachaReservas = new fis.dsw.sgc.finanzas.service.FachadaParaReservasImpl(deudaService);

        //INSTANCIAS DE SERVICE

        fis.dsw.sgc.finanzas.service.IPagoService pagoService = new fis.dsw.sgc.finanzas.service.PagoServiceImpl(pagoFactory,pagoDAO,deudaDAO);
        fis.dsw.sgc.finanzas.service.IGastoService gastoService  = new fis.dsw.sgc.finanzas.service.GastoServiceImpl(gastoDAO);
        fis.dsw.sgc.finanzas.service.IReportesService reportesService = new fis.dsw.sgc.finanzas.service.ReportesServiceImpl(reportesDAO,usuariosService);
        fis.dsw.sgc.check_in.service.ICheckInService checkInService = new fis.dsw.sgc.check_in.service.CheckInServiceImpl(registroEntradaDAO,programacionVisitaDAO);
        fis.dsw.sgc.check_in.service.IAlertaSeguridadService alertaSeguridadService = new fis.dsw.sgc.check_in.service.AlertaSeguridadServiceImpl(alertaSeguridadDAO);
        fis.dsw.sgc.check_in.service.IProgramVisitaService programVisitaService = new fis.dsw.sgc.check_in.service.ProgramVisitaService(programacionVisitaDAO);
        fis.dsw.sgc.reservas.service.IServicioReservas servicioReservas = new fis.dsw.sgc.reservas.service.ServicioReservasImpl(reservaDAO,observacionReservaDAO,inmueblesService,fachaReservas);
        fis.dsw.sgc.comunicacion.service.IComunicacionService comunicacionService = new fis.dsw.sgc.comunicacion.service.ComunicacionServiceImpl(comunicacionDAO);



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