package fis.dsw.sgc.administracion.controller;

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
import fis.dsw.sgc.administracion.model.SesionUsuario;

import java.io.IOException;

public class loginController {

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
        String usuario = txtUsuario.getText();
        String contrasena = txtContrasena.getText();

        lblMensaje.setText("");

        if (usuario == null || usuario.trim().isEmpty() || contrasena == null || contrasena.trim().isEmpty()) {
            lblMensaje.setText("Usuario o contraseña vacíos");
            lblMensaje.setStyle("-fx-text-fill: #ff5566;");

        } else if ("admin".equals(usuario) && "1234".equals(contrasena)) {
            cargarDashboard(event);

        } else {
            lblMensaje.setText("Usuario o contraseña incorrectos");
            lblMensaje.setStyle("-fx-text-fill: #ff5566;");
        }
    }

    private void cargarDashboard(ActionEvent event) {
        try {
            String vista = "/administracion/fxml/dashboard.fxml";
            Parent root = FXMLLoader.load(getClass().getResource(vista));

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