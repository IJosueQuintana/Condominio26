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

    // Se eliminaron las variables que restringían a un solo menú activo a la vez

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

    @FXML void irAPagarDeuda(ActionEvent event)                      { cargarVista("/finanzas/fxml/pagarDeuda.fxml");    }
    @FXML void irAGenerarRendicionCuentas(ActionEvent event)         { cargarVista("/finanzas/fxml/generarRendicionCuentas.fxml"); }
    @FXML void irAConsultarDeudas(ActionEvent event)                 { cargarVista("/finanzas/fxml/consultarDeudas.fxml");         }
    @FXML void irARegistrarDeuda(ActionEvent event)                  { cargarVista("/finanzas/fxml/pendiente.fxml");         }
    @FXML void irADefinirValorMensualAlicuta(ActionEvent event)      { cargarVista("/finanzas/fxml/pendiente.fxml");         }
    @FXML void irAGenerarReportePagosInternos(ActionEvent event)     { cargarVista("/finanzas/fxml/generarReportePagosRealizados.fxml"); }
    @FXML void irARegistrarPagoExterno(ActionEvent event)            { cargarVista("/finanzas/fxml/pendiente.fxml");         }
    @FXML void irAGenerarReporteGastos(ActionEvent event)            { cargarVista("/finanzas/fxml/pendiente.fxml");         }
    @FXML void irASolicitarPagoEnCuotas(ActionEvent event)           { cargarVista("/finanzas/fxml/pendiente.fxml");         }
    @FXML void irAGenerarCertificadoNoDeudor(ActionEvent event)      { cargarVista("/finanzas/fxml/pendiente.fxml");         }
    @FXML void irAConsultarPagos(ActionEvent event)                  { cargarVista("/finanzas/fxml/pendiente.fxml");         }
    @FXML void irAConsultarReporteRendicionCuentas(ActionEvent event){ cargarVista("/finanzas/fxml/pendiente.fxml");         }
    @FXML void irARegistrarEntidadBancaria(ActionEvent event)        { cargarVista("/finanzas/fxml/pendiente.fxml");         }

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

    private void cargarVista(String rutaFxml) {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource(rutaFxml));
            contentPane.getChildren().clear();
            contentPane.getChildren().add(vista);
        } catch (IOException | NullPointerException e) {
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
}