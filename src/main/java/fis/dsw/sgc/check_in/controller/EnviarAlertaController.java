package fis.dsw.sgc.check_in.controller;

import fis.dsw.sgc.check_in.exception.CheckInException;
import fis.dsw.sgc.check_in.model.TipoAlerta;
import fis.dsw.sgc.check_in.model.TipoAlertaAvisoGeneral;
import fis.dsw.sgc.check_in.model.TipoAlertaEmergencia;
import fis.dsw.sgc.check_in.model.TipoAlertaSimulacro;
import fis.dsw.sgc.check_in.service.IAlertaSeguridadService;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Map;

public class EnviarAlertaController {

    private static final String EMERGENCIA = "Emergencia";
    private static final String SIMULACRO = "Simulacro";
    private static final String AVISO_GENERAL = "Aviso General";

    private static final String TODOS = "Todos los residentes";
    private static final String TORRE = "Torre / bloque específico";
    private static final String UNIDAD = "Residente / unidad específica";

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

    @FXML private Label lblDestinatarioDinamico;
    @FXML private ComboBox<String> cbTorreBloque;
    @FXML private HBox boxBuscarResidente;
    @FXML private TextField txtIdentificacionResidente;

    @FXML private TextArea txtMensaje;
    @FXML private Label lblMensajeEstado;
    @FXML private Label lblResumen;

    private IAlertaSeguridadService alertaSeguridadService;

    public EnviarAlertaController() {
        this(new fis.dsw.sgc.check_in.service.AlertaSeguridadServiceImpl());
    }

    public EnviarAlertaController(IAlertaSeguridadService alertaSeguridadService) {
        this.alertaSeguridadService = alertaSeguridadService;
    }

    /** Setter para DI manual por mainWindowController tras FXMLLoader */
    public void setAlertaSeguridadService(IAlertaSeguridadService alertaSeguridadService) {
        this.alertaSeguridadService = alertaSeguridadService;
    }

    public IAlertaSeguridadService getAlertaSeguridadService() {
        return alertaSeguridadService;
    }

    @FXML
    public void initialize() {
        cbTipoAlerta.setItems(FXCollections.observableArrayList(EMERGENCIA, SIMULACRO, AVISO_GENERAL));
        cbDestinatarios.setItems(FXCollections.observableArrayList(TODOS, TORRE, UNIDAD));

        cbTorreBloque.setItems(FXCollections.observableArrayList("Torre A", "Torre B", "Bloque P-01", "Bloque P-02"));

        cbTipoAlerta.getSelectionModel().selectedItemProperty().addListener((obs, old, tipo) -> actualizarPrioridad(tipo));
        cbTipoAlerta.getSelectionModel().selectFirst();

        cbDestinatarios.getSelectionModel().selectedItemProperty().addListener((obs, old, tipo) -> actualizarFormularioDestino(tipo));
        cbDestinatarios.getSelectionModel().selectFirst();
    }

    private void actualizarPrioridad(String tipo) {
        String prioridad = PRIORIDAD_POR_TIPO.getOrDefault(tipo, "Media");
        lblPrioridad.setText("Prioridad: " + prioridad);
        lblPrioridad.getStyleClass().setAll("badge-estado", "badge-" + prioridad.toLowerCase().replace("crítica", "critica"));
    }

    private void actualizarFormularioDestino(String destinatario) {
        boolean esTodos = TODOS.equals(destinatario);
        boolean esTorre = TORRE.equals(destinatario);
        boolean esUnidad = UNIDAD.equals(destinatario);

        lblDestinatarioDinamico.setVisible(!esTodos);
        lblDestinatarioDinamico.setManaged(!esTodos);

        cbTorreBloque.setVisible(esTorre);
        cbTorreBloque.setManaged(esTorre);

        boxBuscarResidente.setVisible(esUnidad);
        boxBuscarResidente.setManaged(esUnidad);

        if (esTorre) lblDestinatarioDinamico.setText("Seleccione Torre / Bloque");
        if (esUnidad) lblDestinatarioDinamico.setText("Identificación del Residente");
    }

    @FXML
    void buscarResidenteAlerta(ActionEvent event) {
        if (txtIdentificacionResidente.getText().isBlank()) {
            mostrarError("Ingrese la identificación para buscar al residente.");
            return;
        }
        mostrarExito("Residente verificado. Puede continuar con el envío de la alerta.");
    }

    @FXML
    void revisarAlerta(ActionEvent event) {
        if (txtMensaje.getText() == null || txtMensaje.getText().isBlank()) {
            mostrarError("Escriba el contenido del mensaje antes de enviar la alerta.");
            return;
        }

        String destinatarios = cbDestinatarios.getValue();
        String destinoResumen = "";

        if (TORRE.equals(destinatarios)) {
            if (cbTorreBloque.getValue() == null) {
                mostrarError("Seleccione la torre o bloque destino.");
                return;
            }
            destinoResumen = "Bloque/Torre: " + cbTorreBloque.getValue();
        } else if (UNIDAD.equals(destinatarios)) {
            if (txtIdentificacionResidente.getText().isBlank()) {
                mostrarError("Ingrese la identificación del residente y verifíquelo.");
                return;
            }
            destinoResumen = "Residente (ID): " + txtIdentificacionResidente.getText().trim();
        } else {
            destinoResumen = TODOS;
        }

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
        if (alertaSeguridadService == null) {
            mostrarError("El servicio de alertas no ha sido inyectado.");
            return;
        }

        String nombreTipo = cbTipoAlerta.getValue();
        TipoAlerta tipoAlerta;

        if (EMERGENCIA.equalsIgnoreCase(nombreTipo)) {
            tipoAlerta = new TipoAlertaEmergencia();
        } else if (SIMULACRO.equalsIgnoreCase(nombreTipo)) {
            tipoAlerta = new TipoAlertaSimulacro();
        } else {
            tipoAlerta = new TipoAlertaAvisoGeneral();
        }

        String destinatarioTipo = cbDestinatarios.getValue();
        String identificadorDestino = UNIDAD.equals(destinatarioTipo) ? txtIdentificacionResidente.getText().trim() : cbTorreBloque.getValue();

        try {
            int idUsuarioActual = 1;
            if (fis.dsw.sgc.core.session.SesionUsuario.obtenerInstancia().getUsuarioActual() != null) {
                idUsuarioActual = fis.dsw.sgc.core.session.SesionUsuario.obtenerInstancia().getUsuarioActual().getIdUsuario();
            }
            alertaSeguridadService.emitirAlerta(tipoAlerta, destinatarioTipo, identificadorDestino, txtMensaje.getText().trim(), idUsuarioActual);
            cancelarConfirmacion(event);
            mostrarExito("Alerta enviada correctamente y registrada en las notificaciones del sistema.");
            limpiarFormulario(null);
        } catch (CheckInException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    void limpiarFormulario(ActionEvent event) {
        txtMensaje.clear();
        txtIdentificacionResidente.clear();
        cbTorreBloque.getSelectionModel().clearSelection();
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