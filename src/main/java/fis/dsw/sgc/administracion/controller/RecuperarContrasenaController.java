package fis.dsw.sgc.administracion.controller;

import fis.dsw.sgc.administracion.model.TokenRestablecimiento;
import fis.dsw.sgc.administracion.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.Date;

public class RecuperarContrasenaController {

    // Panel Paso 1
    @FXML private VBox panelSolicitud;
    @FXML private TextField txtCorreo;
    @FXML private Label lblMensajeSolicitud;

    // Panel Paso 2
    @FXML private VBox panelRestablecimiento;
    @FXML private TextField txtToken;
    @FXML private PasswordField txtNuevaContrasena;
    @FXML private Label lblMensajeRestablecimiento;

    private TokenRestablecimiento tokenGenerado;

    @FXML
    void enviarToken(ActionEvent event) {
        String correo = txtCorreo.getText() == null ? "" : txtCorreo.getText().trim();

        if (correo.isEmpty()) {
            mostrarMensaje(lblMensajeSolicitud, "Debe ingresar su correo.", "message-error");
            return;
        }

        Usuario usuario = DatosDemoGRB.buscarPorCorreo(correo);
        if (usuario == null) {
            mostrarMensaje(lblMensajeSolicitud, "No existe una cuenta asociada a este correo electrónico.", "message-error");
            return;
        }

        // Simular Generar y "enviar" Token
        tokenGenerado = new TokenRestablecimiento();
        tokenGenerado.setCodigo("123456"); // Token hardcodeado para la demo
        tokenGenerado.setFechaExpiracion(new Date(System.currentTimeMillis() + 3600000));
        tokenGenerado.setIntentos(0);

        mostrarMensaje(lblMensajeSolicitud, "Token enviado exitosamente. (Para la demo use: 123456)", "message-success");

        panelSolicitud.setDisable(true);
        panelRestablecimiento.setVisible(true);
        panelRestablecimiento.setManaged(true);
    }

    @FXML
    void restablecerContrasena(ActionEvent event) {
        String tokenIngresado = txtToken.getText() == null ? "" : txtToken.getText().trim();
        String nuevaClave = txtNuevaContrasena.getText() == null ? "" : txtNuevaContrasena.getText();

        if (tokenIngresado.isEmpty() || nuevaClave.isEmpty()) {
            mostrarMensaje(lblMensajeRestablecimiento, "Todos los campos son obligatorios.", "message-error");
            return;
        }

        if (tokenGenerado == null || !tokenGenerado.esValido() || !tokenGenerado.getCodigo().equals(tokenIngresado)) {
            mostrarMensaje(lblMensajeRestablecimiento, "Token inválido o expirado.", "message-error");
            return;
        }

        if (nuevaClave.length() < 6) {
            mostrarMensaje(lblMensajeRestablecimiento, "La contraseña no cumple con los requisitos de seguridad.", "message-error");
            return;
        }

        mostrarMensaje(lblMensajeRestablecimiento, "Contraseña restablecida exitosamente.", "message-success");

        // Deshabilitar formulario para finalizar el proceso
        txtToken.setDisable(true);
        txtNuevaContrasena.setDisable(true);
    }

    private void mostrarMensaje(Label label, String texto, String estilo) {
        label.getStyleClass().setAll("message-label", estilo);
        label.setText(texto);
    }
}