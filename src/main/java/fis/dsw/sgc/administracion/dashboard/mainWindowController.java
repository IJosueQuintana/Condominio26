package fis.dsw.sgc.administracion.dashboard;

import fis.dsw.sgc.core.util.NavigationUtil;
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

import java.io.IOException;
import java.io.InputStream;

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

    @FXML private Button btnAdministracion;
    @FXML private Button btnFinanzas;
    @FXML private Button btnInmuebles;
    @FXML private Button btnReservas;
    @FXML private Button btnCheckIn;
    @FXML private Button btnComunicacion;

    private static final String AVATAR_PATH  = "/administracion/img/avatar.jpg";
    private static final String HOME_BG_PATH = "/administracion/img/home_bg.jpg";
    private static final String CLASE_ACTIVO = "active";

    private Button botonSeccionActiva = null;
    private VBox   submenuActivo      = null;

    @FXML
    public void initialize() {
        cargarAvatar();
        irAInicio(null);
    }

    public void setUsuario(String nombre, String rol) {
        lblNombreUsuario.setText(nombre);
        lblRolUsuario.setText(rol);
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

    // ==================== Selección visual del menú ====================

    private void marcarSeccionActiva(Button boton) {
        if (botonSeccionActiva != null) botonSeccionActiva.getStyleClass().remove(CLASE_ACTIVO);
        if (boton != null)             boton.getStyleClass().add(CLASE_ACTIVO);
        botonSeccionActiva = boton;
    }

    // ==================== Toggle genérico de submenús ====================

    private void toggleSubmenu(Button boton, VBox submenu) {
        boolean estaAbierto = submenu.isVisible();

        // Si hay otro submenú abierto, cerrarlo primero
        if (submenuActivo != null && submenuActivo != submenu) {
            submenuActivo.setVisible(false);
            submenuActivo.setManaged(false);
        }

        boolean nuevoEstado = !estaAbierto;
        submenu.setVisible(nuevoEstado);
        submenu.setManaged(nuevoEstado);
        submenuActivo = nuevoEstado ? submenu : null;

        if (nuevoEstado) {
            marcarSeccionActiva(boton);
        } else if (botonSeccionActiva == boton) {
            marcarSeccionActiva(null);
        }
    }

    // ==================== Botones del menú lateral ====================

    @FXML void toggleAdministracion(ActionEvent event) { toggleSubmenu(btnAdministracion, submenuAdministracion); }
    @FXML void toggleFinanzas(ActionEvent event)        { toggleSubmenu(btnFinanzas,       submenuFinanzas);       }
    @FXML void toggleInmuebles(ActionEvent event)       { toggleSubmenu(btnInmuebles,      submenuInmuebles);      }
    @FXML void toggleReservas(ActionEvent event)        { toggleSubmenu(btnReservas,        submenuReservas);       }
    @FXML void toggleCheckIn(ActionEvent event)         { toggleSubmenu(btnCheckIn,         submenuCheckIn);        }
    @FXML void toggleComunicacion(ActionEvent event)    { toggleSubmenu(btnComunicacion,    submenuComunicacion);   }

    // ==================== Inicio ====================

    @FXML
    void irAInicio(ActionEvent event) {
        if (submenuActivo != null) {
            submenuActivo.setVisible(false);
            submenuActivo.setManaged(false);
            submenuActivo = null;
        }
        marcarSeccionActiva(null);
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

    @FXML void irARegistrarPagoExterno(ActionEvent event)    { cargarVista("/finanzas/fxml/registrarPagoExterno.fxml");    }
    @FXML void irAGenerarRendicionCuentas(ActionEvent event) { cargarVista("/finanzas/fxml/generarRendicionCuentas.fxml"); }
    @FXML void irAValidarPago(ActionEvent event)             { cargarVista("/finanzas/fxml/validarPago.fxml");             }
    @FXML void irAConsultarDeudas(ActionEvent event)         { cargarVista("/finanzas/fxml/consultarDeudas.fxml");         }

    // ==================== Submenú Reservas ====================

    @FXML void irAAnadirReserva(ActionEvent event)       { cargarVista("/reservas/fxml/anadirReserva.fxml");       }
    @FXML void irAVerReserva(ActionEvent event)          { cargarVista("/reservas/fxml/verReserva.fxml");          }
    @FXML void irAAgregarObservacion(ActionEvent event)  { cargarVista("/reservas/fxml/agregarObservacion.fxml");  }
    @FXML void irACancelarObservacion(ActionEvent event) { cargarVista("/reservas/fxml/cancelarObservacion.fxml"); }

    // ==================== Submenú Check-In ====================

    @FXML void irARegistrarEntradaResidente(ActionEvent event)  { cargarVista("/check_in/fxml/registrarEntradaResidente.fxml"); }
    @FXML void irARegistrarEntradaExterna(ActionEvent event)    { cargarVista("/check_in/fxml/registrarEntradaExterna.fxml");   }
    @FXML void irAProgramarVisita(ActionEvent event)            { cargarVista("/check_in/fxml/programarVisita.fxml");           }
    @FXML void irAGestionarHistorialIngresos(ActionEvent event) { cargarVista("/check_in/fxml/gestionarHistorialIngresos.fxml");}
    @FXML void irAEnviarAlerta(ActionEvent event)               { cargarVista("/check_in/fxml/enviarAlerta.fxml");              }

    // ==================== Notificaciones ====================

    @FXML
    void irANotificaciones(ActionEvent event) {
        if (submenuActivo != null) {
            submenuActivo.setVisible(false);
            submenuActivo.setManaged(false);
            submenuActivo = null;
        }
        marcarSeccionActiva(null);
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

    private void cargarVista(String rutaFxml) {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource(rutaFxml));
            contentPane.getChildren().clear();
            contentPane.getChildren().add(vista);
        } catch (IOException | NullPointerException e) {
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
}
