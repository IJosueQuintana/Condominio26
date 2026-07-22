package fis.dsw.sgc.administracion.controller;

import fis.dsw.sgc.administracion.model.NombreRol;
import fis.dsw.sgc.administracion.model.Rol;
import fis.dsw.sgc.administracion.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class AsignarRolController {

    @FXML private TextField txtBuscarCorreo;
    @FXML private Label lblMensajeBusqueda;
    @FXML private VBox panelAsignacion;
    @FXML private Label lblUsuarioEncontrado;
    @FXML private Label lblRolesActuales;
    @FXML private ComboBox<NombreRol> cmbNuevosRoles;
    @FXML private Label lblMensajeAsignacion;

    private Usuario usuarioSeleccionado;

    @FXML
    public void initialize() {
        // Cargar los valores del Enum NombreRol en el ComboBox
        cmbNuevosRoles.getItems().setAll(NombreRol.values());
    }

    @FXML
    void buscarUsuario(ActionEvent event) {
        String correo = txtBuscarCorreo.getText() == null ? "" : txtBuscarCorreo.getText().trim();
        panelAsignacion.setVisible(false);
        panelAsignacion.setManaged(false);

        if (correo.isEmpty()) {
            mostrarMensaje(lblMensajeBusqueda, "Ingrese un correo válido.", "message-error");
            return;
        }

        usuarioSeleccionado = DatosDemoGRB.buscarPorCorreo(correo);

        if (usuarioSeleccionado == null) {
            mostrarMensaje(lblMensajeBusqueda, "Usuario no encontrado.", "message-error");
            return;
        }

        lblMensajeBusqueda.setText("");
        lblUsuarioEncontrado.setText(usuarioSeleccionado.getNombre() + " " + usuarioSeleccionado.getApellido());

        actualizarEtiquetaRoles();

        panelAsignacion.setVisible(true);
        panelAsignacion.setManaged(true);
    }

    @FXML
    void asignarRol(ActionEvent event) {
        NombreRol rolSeleccionado = cmbNuevosRoles.getValue();

        if (rolSeleccionado == null) {
            mostrarMensaje(lblMensajeAsignacion, "Debe seleccionar un rol.", "message-error");
            return;
        }

        // Verificar si ya tiene el rol
        boolean yaTieneRol = usuarioSeleccionado.getCuenta().getRoles().stream()
                .anyMatch(r -> r.getNombre() == rolSeleccionado);

        if (yaTieneRol) {
            mostrarMensaje(lblMensajeAsignacion, "El usuario ya posee el rol de " + rolSeleccionado.name(), "message-info");
            return;
        }

        // Simular la asignación del rol
        Rol nuevoRol = new Rol();
        nuevoRol.setNombre(rolSeleccionado);
        nuevoRol.setDescripcion("Rol asignado desde interfaz");
        usuarioSeleccionado.getCuenta().getRoles().add(nuevoRol);

        actualizarEtiquetaRoles();
        mostrarMensaje(lblMensajeAsignacion, "Rol asignado exitosamente.", "message-success");
        cmbNuevosRoles.getSelectionModel().clearSelection();
    }

    private void actualizarEtiquetaRoles() {
        StringBuilder rolesStr = new StringBuilder();
        for (Rol rol : usuarioSeleccionado.getCuenta().getRoles()) {
            rolesStr.append(rol.getNombre().name()).append(" ");
        }
        lblRolesActuales.setText(rolesStr.toString().isEmpty() ? "Ninguno" : rolesStr.toString());
    }

    private void mostrarMensaje(Label label, String texto, String estilo) {
        label.getStyleClass().setAll("message-label", estilo);
        label.setText(texto);
    }
}