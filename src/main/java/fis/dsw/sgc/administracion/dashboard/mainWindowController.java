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

    @FXML
    private Label lblNombreUsuario;

    @FXML
    private Label lblRolUsuario;

    @FXML
    private ImageView avatarImage;

    @FXML
    private StackPane contentPane;

    @FXML
    private VBox submenuFinanzas;

    @FXML
    private Button btnAdministracion;
    @FXML
    private Button btnFinanzas;
    @FXML
    private Button btnInmuebles;
    @FXML
    private Button btnReservas;
    @FXML
    private Button btnCheckIn;
    @FXML
    private Button btnComunicacion;

    // Rutas esperadas dentro de src/main/resources/administracion/img/
    private static final String AVATAR_PATH = "/administracion/img/avatar.jpg";
    private static final String HOME_BG_PATH = "/administracion/img/home_bg.jpg";

    private static final String CLASE_ACTIVO = "active";

    private boolean finanzasAbierto = false;
    private Button botonSeccionActiva = null;

    @FXML
    public void initialize() {
        cargarAvatar();
        irAInicio(null);
    }

    /**
     * Debe llamarse desde loginController justo después de un login exitoso,
     * para mostrar el nombre y rol del usuario autenticado.
     */
    public void setUsuario(String nombre, String rol) {
        lblNombreUsuario.setText(nombre);
        lblRolUsuario.setText(rol);
    }

    private void cargarAvatar() {
        Image imagen = cargarImagen(AVATAR_PATH);
        if (imagen != null) {
            avatarImage.setImage(imagen);
        }
        // Recorte circular del avatar
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

    /**
     * Marca `boton` como la sección activa (resaltada en rojo) y quita el
     * resaltado de la que estaba activa antes. Así el color rojo solo
     * aparece cuando el usuario realmente está en esa sección, nunca fijo.
     */
    private void marcarSeccionActiva(Button boton) {
        if (botonSeccionActiva != null) {
            botonSeccionActiva.getStyleClass().remove(CLASE_ACTIVO);
        }
        if (boton != null) {
            boton.getStyleClass().add(CLASE_ACTIVO);
        }
        botonSeccionActiva = boton;
    }

    // ==================== Menú lateral ====================

    @FXML
    void toggleFinanzas(ActionEvent event) {
        finanzasAbierto = !finanzasAbierto;
        submenuFinanzas.setVisible(finanzasAbierto);
        submenuFinanzas.setManaged(finanzasAbierto);

        // El rojo aparece SOLO mientras el submenú está desplegado.
        if (finanzasAbierto) {
            marcarSeccionActiva(btnFinanzas);
        } else if (botonSeccionActiva == btnFinanzas) {
            marcarSeccionActiva(null);
        }
    }

    @FXML
    void irAInicio(ActionEvent event) {
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

    @FXML
    void irAAdministracion(ActionEvent event) {
        marcarSeccionActiva(btnAdministracion);
        cargarVista("/administracion/fxml/administracion_home.fxml");
    }

    @FXML
    void irAInmuebles(ActionEvent event) {
        marcarSeccionActiva(btnInmuebles);
        cargarVista("/inmuebles/fxml/inmuebles_home.fxml");
    }

    @FXML
    void irAReservas(ActionEvent event) {
        marcarSeccionActiva(btnReservas);
        cargarVista("/reservas/fxml/reservas_home.fxml");
    }

    @FXML
    void irACheckIn(ActionEvent event) {
        marcarSeccionActiva(btnCheckIn);
        cargarVista("/check_in/fxml/checkin_home.fxml");
    }

    @FXML
    void irAComunicacion(ActionEvent event) {
        marcarSeccionActiva(btnComunicacion);
        cargarVista("/comunicacion/fxml/comunicacion_home.fxml");
    }

    @FXML
    void irARegistrarPagoExterno(ActionEvent event) {
        cargarVista("/finanzas/fxml/registrarPagoExterno.fxml");
    }

    @FXML
    void irAGenerarRendicionCuentas(ActionEvent event) {
        cargarVista("/finanzas/fxml/generarRendicionCuentas.fxml");
    }

    @FXML
    void irAValidarPago(ActionEvent event) {
        cargarVista("/finanzas/fxml/validarPago.fxml");
    }

    @FXML
    void irAConsultarDeudas(ActionEvent event) {
        cargarVista("/finanzas/fxml/consultarDeudas.fxml");
    }

    /**
     * Carga el FXML de un módulo dentro del área central del dashboard.
     * Si la vista todavía no existe (módulo en construcción), muestra un
     * aviso en vez de romper la aplicación.
     */
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