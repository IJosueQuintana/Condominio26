package fis.dsw.sgc.check_in.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.Map;

public class EnviarAlertaController {

    private static final String EMERGENCIA = "Emergencia";
    private static final String SIMULACRO = "Simulacro";
    private static final String AVISO_GENERAL = "Aviso General";

    private static final String TODOS = "Todos los residentes";
    private static final String TORRE = "Torre / bloque específico";
    private static final String UNIDAD = "Residente / unidad específica";

    // Refleja TipoAlerta.obtenerPrioridadPorDefecto() del dominio (Emergencia -> CRITICA).
    private static final Map<String, String> PRIORIDAD_POR_TIPO = Map.of(
            EMERGENCIA, "Crítica",
            SIMULACRO, "Media",
            AVISO_GENERAL, "Baja"
    );

    @FXML private VBox panelPrincipal;
    @FXML private VBox panelConfirmacion;

    @FXML private ChoiceBox<String> cbTipoAlerta;
    @FXML private Label lblPrioridad;
    @FXML private ChoiceBox<String> cbDestinatarios;
    @FXML private TextField txtDestinatarioEspecifico;
    @FXML private TextArea txtMensaje;
    @FXML private Label lblMensajeEstado;
    @FXML private Label lblResumen;
    @FXML private Button btnRevisar;

    @FXML
    public void initialize() {
        cbTipoAlerta.setItems(FXCollections.observableArrayList(EMERGENCIA, SIMULACRO, AVISO_GENERAL));
        cbDestinatarios.setItems(FXCollections.observableArrayList(TODOS, TORRE, UNIDAD));

        cbTipoAlerta.getSelectionModel().selectedItemProperty().addListener((obs, old, tipo) -> actualizarPrioridad(tipo));
        cbTipoAlerta.getSelectionModel().selectFirst();

        cbDestinatarios.getSelectionModel().selectFirst();
    }

    private void actualizarPrioridad(String tipo) {
        String prioridad = PRIORIDAD_POR_TIPO.getOrDefault(tipo, "Media");
        lblPrioridad.setText("Prioridad: " + prioridad);
        lblPrioridad.getStyleClass().setAll("badge-estado", "badge-" + prioridad.toLowerCase()
                .replace("crítica", "critica"));
    }

    @FXML
    void revisarAlerta(ActionEvent event) {
        if (txtMensaje.getText() == null || txtMensaje.getText().isBlank()) {
            mostrarError("Escriba el contenido del mensaje antes de enviar la alerta.");
            return;
        }

        String destinatarios = cbDestinatarios.getValue();
        if (!TODOS.equals(destinatarios) && txtDestinatarioEspecifico.getText().isBlank()) {
            mostrarError("Indique la torre, bloque o unidad destino de la alerta.");
            return;
        }

        String destinoResumen = TODOS.equals(destinatarios)
                ? TODOS
                : destinatarios + " (" + txtDestinatarioEspecifico.getText().trim() + ")";

        lblResumen.setText(
                "Tipo: " + cbTipoAlerta.getValue() + " · Prioridad: " + PRIORIDAD_POR_TIPO.get(cbTipoAlerta.getValue()) + "\n" +
                "Destinatarios: " + destinoResumen + "\n" +
                "Mensaje: " + txtMensaje.getText().trim()
        );

        panelPrincipal.setVisible(false);
        panelPrincipal.setManaged(false);
        panelConfirmacion.setVisible(true);
        panelConfirmacion.setManaged(true);
    }

    @FXML
    void cancelarConfirmacion(ActionEvent event) {
        panelConfirmacion.setVisible(false);
        panelConfirmacion.setManaged(false);
        panelPrincipal.setVisible(true);
        panelPrincipal.setManaged(true);
    }

    @FXML
    void confirmarEnvio(ActionEvent event) {
        // Aquí se invocará servicioAlertas -> servicioNotificaciones
        cancelarConfirmacion(event);
        mostrarExito("Alerta enviada correctamente a los destinatarios seleccionados.");
        limpiarFormulario(null);
    }

    @FXML
    void limpiarFormulario(ActionEvent event) {
        txtMensaje.clear();
        txtDestinatarioEspecifico.clear();
        cbTipoAlerta.getSelectionModel().selectFirst();
        cbDestinatarios.getSelectionModel().selectFirst();
    }

    private void mostrarExito(String mensaje) {
        lblMensajeEstado.setText(mensaje);
        lblMensajeEstado.getStyleClass().setAll("message-label", "message-success");
    }

    private void mostrarError(String mensaje) {
        lblMensajeEstado.setText(mensaje);
        lblMensajeEstado.getStyleClass().setAll("message-label", "message-error");
    }
}
