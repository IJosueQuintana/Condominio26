package fis.dsw.sgc.administracion.controller;

import fis.dsw.sgc.administracion.model.Usuario;
import fis.dsw.sgc.administracion.model.SesionUsuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ActualizarPerfilController {

    private static final String MSG_INICIAL =
            "Edite los campos que desee actualizar y pulse Guardar cambios.";

    @FXML private Label lblUsuarioActual;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtFotoPerfil;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;
    @FXML private Label lblMensaje;

    @FXML
    public void initialize() {
        cargarDatosUsuarioActual();
        setMensaje(MSG_INICIAL, "message-info");
    }

    private void cargarDatosUsuarioActual() {
        Usuario usuario = SesionUsuario.obtenerInstancia().getUsuarioActual();

        if (usuario == null) {
            // Datos de ejemplo hasta conectar con el servicio, para poder previsualizar la vista
            lblUsuarioActual.setText("demo@condominio.com");
            txtTelefono.setText("0999999999");
            txtDireccion.setText("Av. de ejemplo y calle secundaria");
            txtFotoPerfil.setText("");
            return;
        }

        lblUsuarioActual.setText(usuario.getNombre() + " " + usuario.getApellido()
                + " (" + usuario.getCorreo() + ")");

        if (usuario.getPerfil() != null) {
            txtTelefono.setText(usuario.getPerfil().getTelefono());
            txtDireccion.setText(usuario.getPerfil().getDireccion());
            txtFotoPerfil.setText(usuario.getPerfil().getFotoPerfil());
        }
    }

    @FXML
    void guardar(ActionEvent event) {
        String telefono = valorSeguro(txtTelefono);
        String direccion = valorSeguro(txtDireccion);

        if (!telefono.isEmpty() && !telefono.matches("\\d{7,10}")) {
            setMensaje("El teléfono debe tener entre 7 y 10 dígitos.", "message-error");
            return;
        }

        if (direccion.isEmpty()) {
            setMensaje("La dirección no puede estar vacía.", "message-error");
            return;
        }

        // Datos de ejemplo hasta conectar con el servicio/DAO real
        setMensaje("Perfil actualizado correctamente.", "message-success");
    }

    @FXML
    void cancelar(ActionEvent event) {
        cargarDatosUsuarioActual();
        setMensaje(MSG_INICIAL, "message-info");
    }

    private String valorSeguro(TextField campo) {
        return campo.getText() == null ? "" : campo.getText().trim();
    }

    private void setMensaje(String texto, String estilo) {
        lblMensaje.getStyleClass().removeAll("message-info", "message-success", "message-error");
        if (!lblMensaje.getStyleClass().contains("message-label")) {
            lblMensaje.getStyleClass().add("message-label");
        }
        lblMensaje.getStyleClass().add(estilo);
        lblMensaje.setText(texto);
    }
}