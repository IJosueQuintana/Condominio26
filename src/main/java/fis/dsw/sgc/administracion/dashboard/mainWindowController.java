package fis.dsw.sgc.administracion.dashboard;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;

import fis.dsw.sgc.core.session.SesionUsuario;
import fis.dsw.sgc.administracion.model.Usuario;
import fis.dsw.sgc.core.util.NavigationUtil;
import fis.dsw.sgc.finanzas.controller.pagarDeudaController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class mainWindowController {

    @FXML private Label lblNombreUsuario;
    @FXML private Label lblRolUsuario;
    @FXML private ImageView avatarImage;
    @FXML private StackPane contentPane;

    @FXML private VBox submenuAdministracion;
    @FXML private VBox submenuFinanzas;
    @FXML private VBox submenuInmuebles;
    @FXML private VBox submenuReservas;
    @FXML private VBox submenuCheckIn;
    @FXML private VBox submenuComunicacion;
    @FXML private VBox reservasBox;

    @FXML private Button btnAdministracion;
    @FXML private Button btnFinanzas;
    @FXML private Button btnInmuebles;
    @FXML private Button btnReservas;
    @FXML private Button btnAuditarReservas;
    @FXML private Button btnCheckIn;
    @FXML private Button btnComunicacion;

    private static final String AVATAR_PATH  = "/administracion/img/avatar.jpg";
    private static final String HOME_BG_PATH = "/administracion/img/home_bg.jpg";
    private static final String CLASE_ACTIVO = "active";

    // Se eliminaron las variables que restringían a un solo menú activo a la vez

    private final fis.dsw.sgc.finanzas.service.IPagoService pagoService;
    private final fis.dsw.sgc.finanzas.service.IGastoService gastoService;
    private final fis.dsw.sgc.finanzas.service.IReportesService reportesService;
    private final fis.dsw.sgc.check_in.service.ICheckInService checkInService;
    private final fis.dsw.sgc.check_in.service.IAlertaSeguridadService alertaSeguridadService;
    private final fis.dsw.sgc.check_in.service.IProgramVisitaService programVisitaService;
    private final fis.dsw.sgc.reservas.service.IServicioReservas servicioReservas;
    private final fis.dsw.sgc.comunicacion.service.IComunicacionService comunicacionService;

    public mainWindowController(fis.dsw.sgc.finanzas.service.IPagoService pagoService, fis.dsw.sgc.finanzas.service.IGastoService gastoService, fis.dsw.sgc.finanzas.service.IReportesService reportesService, fis.dsw.sgc.check_in.service.ICheckInService checkInService, fis.dsw.sgc.check_in.service.IAlertaSeguridadService alertaSeguridadService, fis.dsw.sgc.check_in.service.IProgramVisitaService programVisitaService, fis.dsw.sgc.reservas.service.IServicioReservas servicioReservas, fis.dsw.sgc.comunicacion.service.IComunicacionService comunicacionService) {
        this.pagoService = pagoService;
        this.gastoService = gastoService;
        this.reportesService = reportesService;
        this.checkInService = checkInService;
        this.alertaSeguridadService = alertaSeguridadService;
        this.programVisitaService = programVisitaService;
        this.servicioReservas = servicioReservas;
        this.comunicacionService = comunicacionService;
    }

    @FXML
    public void initialize() {
        cargarAvatar();
        cargarDatosUsuarioSesion();
        irAInicio(null);
    }

    public void setUsuario(String nombre, String rol) {
        lblNombreUsuario.setText(nombre);
        lblRolUsuario.setText(rol);
    }

    private void cargarDatosUsuarioSesion() {
        Usuario usuario = SesionUsuario.obtenerInstancia().getUsuarioActual();
        if (usuario == null) {
            return;
        }

        String roles = usuario.getCuenta() == null ? "" : usuario.getCuenta().getRoles().stream()
                .map(rol -> rol.getNombre().name())
                .collect(Collectors.joining(", "));

        setUsuario(usuario.getNombre(), roles);

        if (btnAuditarReservas != null) {
            boolean esAdmin = roles.contains("ADMINISTRADOR");
            btnAuditarReservas.setVisible(esAdmin);
            btnAuditarReservas.setManaged(esAdmin);
        }

        if (reservasBox != null) {
            boolean tieneAccesoReservas = roles.contains("ADMINISTRADOR") ||
                                          roles.contains("RESIDENTE") ||
                                          roles.contains("PROPIETARIO") ||
                                          roles.contains("PRESIDENTE");
            reservasBox.setVisible(tieneAccesoReservas);
            reservasBox.setManaged(tieneAccesoReservas);
        }
    }

    // ==================== Avatar ====================

    private void cargarAvatar() {
        Image imagen = cargarImagen(AVATAR_PATH);
        if (imagen != null) avatarImage.setImage(imagen);
        avatarImage.setClip(new Circle(35, 35, 35));
    }

    private Image cargarImagen(String ruta) {
        InputStream stream = getClass().getResourceAsStream(ruta);
        if (stream == null) {
            System.out.println("[dashboard] No se encontró la imagen: " + ruta
                    + " -> colócala en src/main/resources" + ruta);
            return null;
        }
        return new Image(stream);
    }

    // ==================== Toggle genérico de submenús ====================

    private void toggleSubmenu(Button boton, VBox submenu) {
        boolean estaAbierto = submenu.isVisible();
        boolean nuevoEstado = !estaAbierto;

        // Solo cambiamos la visibilidad del menú clickeado, sin afectar a los demás
        submenu.setVisible(nuevoEstado);
        submenu.setManaged(nuevoEstado);

        // Alternamos la clase visual activa exclusivamente para el botón clickeado
        if (nuevoEstado) {
            if (!boton.getStyleClass().contains(CLASE_ACTIVO)) {
                boton.getStyleClass().add(CLASE_ACTIVO);
            }
        } else {
            boton.getStyleClass().remove(CLASE_ACTIVO);
        }
    }

    // ==================== Botones del menú lateral ====================

    @FXML void toggleAdministracion(ActionEvent event) { toggleSubmenu(btnAdministracion, submenuAdministracion); }
    @FXML void toggleFinanzas(ActionEvent event)       { toggleSubmenu(btnFinanzas,       submenuFinanzas);       }
    @FXML void toggleInmuebles(ActionEvent event)      { toggleSubmenu(btnInmuebles,      submenuInmuebles);      }
    @FXML void toggleReservas(ActionEvent event)       { toggleSubmenu(btnReservas,       submenuReservas);       }
    @FXML void toggleCheckIn(ActionEvent event)        { toggleSubmenu(btnCheckIn,        submenuCheckIn);        }
    @FXML void toggleComunicacion(ActionEvent event)   { toggleSubmenu(btnComunicacion,   submenuComunicacion);   }

    // ==================== Inicio ====================

    @FXML
    void irAInicio(ActionEvent event) {
        // Se eliminó la lógica que forzaba el cierre de los menús al ir a la pantalla de inicio
        contentPane.getChildren().clear();
        Image fondo = cargarImagen(HOME_BG_PATH);
        if (fondo != null) {
            ImageView vista = new ImageView(fondo);
            vista.setPreserveRatio(false);
            vista.fitWidthProperty().bind(contentPane.widthProperty());
            vista.fitHeightProperty().bind(contentPane.heightProperty());
            contentPane.getChildren().add(vista);
        } else {
            contentPane.getChildren().add(crearPlaceholder(
                    "Coloca la imagen del condominio en:\nsrc/main/resources" + HOME_BG_PATH));
        }
    }

    // ==================== Submenú Finanzas ====================

    @FXML void irAPagarDeuda(ActionEvent event)    {
        pagarDeudaController pagarDeudaController = new pagarDeudaController(pagoService);
        cargarVista("/finanzas/fxml/pagarDeuda.fxml",pagarDeudaController);    }
    @FXML void irAGenerarRendicionCuentas(ActionEvent event) { cargarVista("/finanzas/fxml/generarRendicionCuentas.fxml"); }
    @FXML void irAConsultarDeudas(ActionEvent event)         { cargarVista("/finanzas/fxml/consultarDeudas.fxml");         }
    @FXML void irARegistrarDeuda(ActionEvent event)         { cargarVista("/finanzas/fxml/registrarDeuda.fxml");         }
    @FXML void irAConfiguracionFinanciera(ActionEvent event) { cargarVista("/finanzas/fxml/configuracionFinanciera.fxml"); }
    @FXML void irARegistrarPagoExterno(ActionEvent event)         { cargarVista("/finanzas/fxml/registrarPagoExterno.fxml");         }
    @FXML void irAGenerarReporteGastos(ActionEvent event)         { cargarVista("/finanzas/fxml/generarReporteGastos.fxml");         }
    @FXML void irASolicitarPagoEnCuotas(ActionEvent event)         { cargarVista("/finanzas/fxml/solicitarPagoEnCuotas.fxml");         }
  
    @FXML void irAConsultarReporteRendicionCuentas(ActionEvent event)         { cargarVista("/finanzas/fxml/consultarRendicionCuentas.fxml");         }
    @FXML void irAGenerarReportePagosInternos(ActionEvent event)     { cargarVista("/finanzas/fxml/generarReportePagosRealizados.fxml"); }
    @FXML void irAGenerarCertificadoNoDeudor(ActionEvent event)      { cargarVista("/finanzas/fxml/generarCertificadoNoDeudor.fxml");         }

    // ==================== Submenú Inmuebles ====================

    @FXML void irAVerInmuebles(ActionEvent event) { cargarVista("/inmuebles/fxml/inmuebles_home.fxml"); }
    @FXML void irARegistrarInmueble(ActionEvent event) { cargarVista("/inmuebles/fxml/registrarInmueble.fxml"); }
    @FXML void irAEditarInmueble(ActionEvent event) { cargarVista("/inmuebles/fxml/editarInmueble.fxml"); }
    @FXML void irARegistrarCasoFortuito(ActionEvent event) { cargarVista("/inmuebles/fxml/registrarCasoFortuito.fxml"); }

    // ==================== Submenú Reservas ====================

    @FXML void irAAnadirReserva(ActionEvent event)       { cargarVista("/reservas/fxml/anadirReserva.fxml");       }
    @FXML void irAVerReserva(ActionEvent event)          { cargarVista("/reservas/fxml/verReserva.fxml");          }
    @FXML void irAAuditarReservas(ActionEvent event)     { cargarVista("/reservas/fxml/auditarReservas.fxml");     }

    // ==================== Submenú Check-In ====================

    @FXML void irARegistrarEntradaResidente(ActionEvent event)  { cargarVista("/check_in/fxml/registrarEntradaResidente.fxml"); }
    @FXML void irARegistrarEntradaExterna(ActionEvent event)    { cargarVista("/check_in/fxml/registrarEntradaExterna.fxml");   }
    @FXML void irAProgramarVisita(ActionEvent event)            { cargarVista("/check_in/fxml/programarVisita.fxml");           }
    @FXML void irAGestionarHistorialIngresos(ActionEvent event) { cargarVista("/check_in/fxml/gestionarHistorialIngresos.fxml");}
    @FXML void irAEnviarAlerta(ActionEvent event)               { cargarVista("/check_in/fxml/enviarAlerta.fxml");              }

    // ==================== Submenú Comunicación ====================

    @FXML void irAEnviarMensaje(ActionEvent event) { cargarVista("/comunicacion/fxml/enviarMensaje.fxml");}
    @FXML void irAPublicarAnuncio(ActionEvent event) { cargarVista("/comunicacion/fxml/publicarAnuncio.fxml");}
    @FXML void irAConsultarHistorialComunicacion(ActionEvent event) { cargarVista("/comunicacion/fxml/consultarHistorial.fxml");}
    @FXML void irAGestionarNotificaciones(ActionEvent event) { cargarVista("/comunicacion/fxml/gestionarNotificaciones.fxml");}
    @FXML void irAGenerarReporteComunicacion(ActionEvent event) { cargarVista("/comunicacion/fxml/generarReporteComunicacion.fxml");}



    // ==================== Notificaciones ====================

    @FXML
    void irANotificaciones(ActionEvent event) {
        // Se eliminó la lógica que forzaba el cierre de los menús al ver las notificaciones
        contentPane.getChildren().clear();
        contentPane.getChildren().add(crearPlaceholder("No hay notificaciones"));
    }

    // ==================== Botones de ejemplo (pendientes de definir) ====================

    @FXML
    void opcionPendiente(ActionEvent event) {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(crearPlaceholder("Funcionalidad en construcción"));
    }

    // ==================== Utilidades ====================

    private void cargarVista(String rutaFxml, Object controller) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource(rutaFxml));
            loader.setController(controller);
            javafx.scene.Parent vista = loader.load();

            contentPane.getChildren().clear();
            contentPane.getChildren().add(vista);
        } catch (java.io.IOException | java.lang.NullPointerException e) {
            e.printStackTrace();
            contentPane.getChildren().clear();
            contentPane.getChildren().add(crearPlaceholder("Vista aún no implementada:\n" + rutaFxml));
        }
    }


    private Label crearPlaceholder(String mensaje) {
        Label aviso = new Label(mensaje);
        aviso.getStyleClass().add("placeholder-label");
        return aviso;
    }

    @FXML
    void logOut(ActionEvent event) throws IOException {
        String vista = "/administracion/fxml/login.fxml";
        Parent root = FXMLLoader.load(getClass().getResource(vista));
        NavigationUtil.changeScene(event, root);
    }

    // ==================== Administración (GRB) ====================

    @FXML
    void irARegistrarCuenta(ActionEvent event) {
        cargarVista("/administracion/fxml/registrarCuenta.fxml");
    }

    @FXML
    void irAActualizarPerfil(ActionEvent event) {
        cargarVista("/administracion/fxml/actualizarPerfil.fxml");
    }

    @FXML
    void irAActualizarInformacionCuenta(ActionEvent event) {
        cargarVista("/administracion/fxml/actualizarInformacionDeCuenta.fxml");
    }

    @FXML
    void irADesactivarCuenta(ActionEvent event) {
        cargarVista("/administracion/fxml/desactivarCuenta.fxml");
    }

    @FXML
    void irAAsignarRol(ActionEvent event) {
        cargarVista("/administracion/fxml/asignarRol.fxml");
    }

    @FXML
    void irADefinirPermisos(ActionEvent event) {
        cargarVista("/administracion/fxml/definirPermisos.fxml");
    }

    @FXML
    void irARecuperarContrasena(ActionEvent event) {
        cargarVista("/administracion/fxml/recuperarContrasena.fxml");
    }

}
