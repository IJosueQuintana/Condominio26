package fis.dsw.sgc.administracion.controller;

import fis.dsw.sgc.administracion.model.NombreRol;
import fis.dsw.sgc.administracion.model.Permiso;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DefinirPermisosController {

    @FXML private ComboBox<NombreRol> cmbRoles;
    @FXML private TextField txtNombrePermiso;
    @FXML private TextField txtRecurso;
    @FXML private Label lblMensaje;

    @FXML
    public void initialize() {
        cmbRoles.getItems().setAll(NombreRol.values());
    }

    @FXML
    void guardarPermiso(ActionEvent event) {
        NombreRol rol = cmbRoles.getValue();
        String nombrePermiso = txtNombrePermiso.getText() == null ? "" : txtNombrePermiso.getText().trim();
        String recurso = txtRecurso.getText() == null ? "" : txtRecurso.getText().trim();

        if (rol == null || nombrePermiso.isEmpty() || recurso.isEmpty()) {
            lblMensaje.getStyleClass().setAll("message-label", "message-error");
            lblMensaje.setText("Todos los campos son obligatorios.");
            return;
        }

        Permiso nuevoPermiso = new Permiso();
        nuevoPermiso.setNombre(nombrePermiso);
        nuevoPermiso.setRecurso(recurso);

        lblMensaje.getStyleClass().setAll("message-label", "message-success");
        lblMensaje.setText("Permisos del rol " + rol.name() + " actualizados exitosamente.");

        txtNombrePermiso.clear();
        txtRecurso.clear();
        cmbRoles.getSelectionModel().clearSelection();
    }
}