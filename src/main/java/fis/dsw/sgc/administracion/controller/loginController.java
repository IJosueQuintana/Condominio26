package fis.dsw.sgc.administracion.controller;

import fis.dsw.sgc.administracion.dashboard.mainWindowController;
import fis.dsw.sgc.core.util.NavigationUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import fis.dsw.sgc.core.session.SesionUsuario;
import fis.dsw.sgc.administracion.service.GestionUsuariosServiceImpl;
import fis.dsw.sgc.administracion.service.IGestionUsuariosAPI;

import java.io.IOException;

public class loginController {

    // Se elimina el duplicado y se quita el 'final' del arreglo
    private final IGestionUsuariosAPI gestionUsuariosService;
    private Object[] dependenciasDashboard;

    public loginController(IGestionUsuariosAPI gestionUsuariosService) {
        this.gestionUsuariosService = gestionUsuariosService;
        // Al no ser final, ya no es obligatorio inicializar el arreglo aquí
    }

    public loginController(IGestionUsuariosAPI gestionUsuariosService, Object[] dependenciasDashboard) {
        this.gestionUsuariosService = gestionUsuariosService;
        this.dependenciasDashboard = dependenciasDashboard;
    }

    public loginController() {
        this(new GestionUsuariosServiceImpl());
    }

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private TextField txtContrasenaVisible;

    @FXML
    private CheckBox chkMostrarContrasena;

    @FXML
    private Label lblMensaje;

    @FXML
    public void initialize() {
        if (txtContrasena != null && txtContrasenaVisible != null) {
            txtContrasenaVisible.textProperty().bindBidirectional(txtContrasena.textProperty());
        }
    }

    @FXML
    public void toggleMostrarContrasena() {
        if (chkMostrarContrasena.isSelected()) {
            txtContrasenaVisible.setVisible(true);
            txtContrasenaVisible.setManaged(true);
            txtContrasena.setVisible(false);
            txtContrasena.setManaged(false);
        } else {
            txtContrasenaVisible.setVisible(false);
            txtContrasenaVisible.setManaged(false);
            txtContrasena.setVisible(true);
            txtContrasena.setManaged(true);
        }
    }

    @FXML
    public void login(ActionEvent event) {
        String correo = txtUsuario.getText();
        String contrasena = txtContrasena.getText();

        lblMensaje.setText("");

        if (correo == null || correo.trim().isEmpty() || contrasena == null || contrasena.trim().isEmpty()) {
            lblMensaje.setText("Usuario o contraseña vacíos");
            lblMensaje.setStyle("-fx-text-fill: #ff5566;");

        } else if (gestionUsuariosService.autenticar(correo, contrasena)) {
            SesionUsuario.obtenerInstancia().setUsuarioActual(gestionUsuariosService.obtenerUsuarioPorCorreo(correo));
            cargarDashboard(event);

        } else {
            lblMensaje.setText("Usuario o contraseña incorrectos");
            lblMensaje.setStyle("-fx-text-fill: #ff5566;");
        }
    }

    private void cargarDashboard(ActionEvent event) {
        try {
            String vista = "/administracion/fxml/dashboard.fxml";

            // 1. Instanciamos el loader en lugar de usar el método estático
            FXMLLoader loader = new FXMLLoader(getClass().getResource(vista));

            // 2. Le decimos a JavaFX CÓMO construir el mainWindowController
            loader.setControllerFactory(clazz -> {
                if (clazz == mainWindowController.class) {
                    // ¡Aquí ocurre la inyección! Le pasamos el arreglo preparado
                    return new mainWindowController(dependenciasDashboard);
                }
                try {
                    return clazz.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            // 3. Cargamos la vista usando la instancia
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.setTitle("SISTEMA DE GESTIÓN DE CONDOMINIO");
            stage.setMaximized(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            lblMensaje.setText("Error al cargar");
            lblMensaje.setStyle("-fx-text-fill: #ff5566;");
        }
    }
}