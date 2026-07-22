package fis.dsw.sgc.comunicacion.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public class EnviarMensajeController {

    private static final int LIMITE_ASUNTO = 120;
    private static final int LIMITE_CONTENIDO = 2000;

    private static final String TIPO_RESIDENTES = "Mensaje a residentes";
    private static final String TIPO_TRABAJADORES = "Comunicado a trabajadores";
    private static final String TIPO_GLOBAL = "Mensaje global";
    private static final String TIPO_URGENTE = "Mensaje urgente";
    private static final String TIPO_ALERTA = "Alerta de emergencia";
    private static final String TIPO_BOLETIN = "Boletín informativo";

    private static final String PRIORIDAD_BAJA = "Baja";
    private static final String PRIORIDAD_NORMAL = "Normal";
    private static final String PRIORIDAD_ALTA = "Alta";
    private static final String PRIORIDAD_URGENTE = "Urgente";

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private static final Map<String, String> DESTINATARIOS_POR_TIPO = crearMapaDestinatarios();

    @FXML private ComboBox<String> cmbTipoMensaje;
    @FXML private ComboBox<String> cmbPrioridad;
    @FXML private TextField txtAsunto;
    @FXML private TextArea txtContenido;

    @FXML private Label lblDestinatarios;
    @FXML private Label lblContadorAsunto;
    @FXML private Label lblContadorCaracteres;
    @FXML private Label lblMensaje;

    @FXML private TableView<MensajeReciente> tblMensajesRecientes;
    @FXML private TableColumn<MensajeReciente, String> colFecha;
    @FXML private TableColumn<MensajeReciente, String> colTipo;
    @FXML private TableColumn<MensajeReciente, String> colPrioridad;
    @FXML private TableColumn<MensajeReciente, String> colAsunto;
    @FXML private TableColumn<MensajeReciente, String> colEstado;

    @FXML private Button btnCancelar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnEnviar;

    private final ObservableList<MensajeReciente> mensajesRecientes =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarSelectores();
        configurarLimitesDeTexto();
        configurarTabla();
        configurarEventos();

        actualizarDestinatarios();
        actualizarContadores();
        cargarMensajes();
    }

    private void configurarSelectores() {
        cmbTipoMensaje.setItems(FXCollections.observableArrayList(
                TIPO_RESIDENTES,
                TIPO_TRABAJADORES,
                TIPO_GLOBAL,
                TIPO_URGENTE,
                TIPO_ALERTA,
                TIPO_BOLETIN
        ));

        cmbPrioridad.setItems(FXCollections.observableArrayList(
                PRIORIDAD_BAJA,
                PRIORIDAD_NORMAL,
                PRIORIDAD_ALTA,
                PRIORIDAD_URGENTE
        ));

        cmbTipoMensaje.getSelectionModel().select(TIPO_RESIDENTES);
        cmbPrioridad.getSelectionModel().select(PRIORIDAD_NORMAL);
    }

    private void configurarLimitesDeTexto() {
        txtAsunto.setTextFormatter(new javafx.scene.control.TextFormatter<String>(cambio ->
                cambio.getControlNewText().length() <= LIMITE_ASUNTO ? cambio : null
        ));

        txtContenido.setTextFormatter(new javafx.scene.control.TextFormatter<String>(cambio ->
                cambio.getControlNewText().length() <= LIMITE_CONTENIDO ? cambio : null
        ));
    }

    private void configurarTabla() {
        colFecha.setCellValueFactory(dato ->
                new ReadOnlyStringWrapper(dato.getValue().getFecha()));
        colTipo.setCellValueFactory(dato ->
                new ReadOnlyStringWrapper(dato.getValue().getTipo()));
        colPrioridad.setCellValueFactory(dato ->
                new ReadOnlyStringWrapper(dato.getValue().getPrioridad()));
        colAsunto.setCellValueFactory(dato ->
                new ReadOnlyStringWrapper(dato.getValue().getAsunto()));
        colEstado.setCellValueFactory(dato ->
                new ReadOnlyStringWrapper(dato.getValue().getEstado()));

        tblMensajesRecientes.setItems(mensajesRecientes);
        tblMensajesRecientes.setPlaceholder(
                new Label("No existen comunicaciones recientes para mostrar.")
        );
    }

    private void configurarEventos() {
        cmbTipoMensaje.valueProperty().addListener((observable, anterior, actual) -> {
            actualizarDestinatarios();
            ajustarPrioridadSegunTipo(actual);
            limpiarMensajeEstado();
        });

        cmbPrioridad.valueProperty().addListener((observable, anterior, actual) ->
                limpiarMensajeEstado());

        txtAsunto.textProperty().addListener((observable, anterior, actual) -> {
            actualizarContadores();
            limpiarMensajeEstado();
        });

        txtContenido.textProperty().addListener((observable, anterior, actual) -> {
            actualizarContadores();
            limpiarMensajeEstado();
        });
    }

    @FXML
    private void enviarMensaje() {
        if (!validarFormulario()) {
            return;
        }

        String tipo = cmbTipoMensaje.getValue();
        String prioridad = cmbPrioridad.getValue();
        String asunto = txtAsunto.getText().trim();

        /*
         * Punto de integración pendiente:
         * EnviarComunicacionDTO solicitud = construirSolicitud();
         * comunicacionService.enviarMensaje(solicitud);
         *
         * El Service será responsable de persistir la comunicación,
         * generar notificaciones y registrar el historial.
         */

        mensajesRecientes.add(0, new MensajeReciente(
                LocalDateTime.now().format(FORMATO_FECHA),
                tipo,
                prioridad,
                asunto,
                "Preparado"
        ));

        mostrarMensaje(
                "El mensaje fue validado y quedó preparado para conectarse con el servicio de Comunicación.",
                "message-success"
        );

        limpiarCamposDeRedaccion();
    }

    @FXML
    private void limpiarFormulario() {
        cmbTipoMensaje.getSelectionModel().select(TIPO_RESIDENTES);
        cmbPrioridad.getSelectionModel().select(PRIORIDAD_NORMAL);
        limpiarCamposDeRedaccion();
        tblMensajesRecientes.getSelectionModel().clearSelection();
        mostrarMensaje("Formulario limpiado.", "message-info");
    }

    @FXML
    private void cancelar() {
        cmbTipoMensaje.getSelectionModel().select(TIPO_RESIDENTES);
        cmbPrioridad.getSelectionModel().select(PRIORIDAD_NORMAL);
        limpiarCamposDeRedaccion();
        tblMensajesRecientes.getSelectionModel().clearSelection();
        mostrarMensaje("Operación cancelada. No se envió ninguna comunicación.", "message-info");
    }

    public void cargarMensajes() {
        mensajesRecientes.clear();

        /*
         * Punto de integración pendiente:
         * mensajesRecientes.setAll(comunicacionService.obtenerMensajesRecientes());
         *
         * No se cargan datos ficticios para evitar confundir información de
         * demostración con registros reales del sistema.
         */
    }

    @FXML
    private void seleccionarMensaje() {
        MensajeReciente seleccionado =
                tblMensajesRecientes.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            return;
        }

        mostrarMensaje(
                "Seleccionado: “" + seleccionado.getAsunto() + "” · Estado: "
                        + seleccionado.getEstado() + ".",
                "message-info"
        );
    }

    private void actualizarDestinatarios() {
        String tipo = cmbTipoMensaje.getValue();
        String destinatarios = DESTINATARIOS_POR_TIPO.getOrDefault(
                tipo,
                "Los destinatarios serán determinados por el servicio de Comunicación."
        );
        lblDestinatarios.setText(destinatarios);
    }

    private void ajustarPrioridadSegunTipo(String tipo) {
        if (TIPO_URGENTE.equals(tipo) || TIPO_ALERTA.equals(tipo)) {
            cmbPrioridad.getSelectionModel().select(PRIORIDAD_URGENTE);
            return;
        }

        if (TIPO_BOLETIN.equals(tipo)) {
            cmbPrioridad.getSelectionModel().select(PRIORIDAD_BAJA);
            return;
        }

        cmbPrioridad.getSelectionModel().select(PRIORIDAD_NORMAL);
    }

    private void actualizarContadores() {
        int caracteresAsunto = txtAsunto.getText() == null ? 0 : txtAsunto.getText().length();
        int caracteresContenido = txtContenido.getText() == null ? 0 : txtContenido.getText().length();

        lblContadorAsunto.setText(caracteresAsunto + " / " + LIMITE_ASUNTO);
        lblContadorCaracteres.setText(caracteresContenido + " / " + LIMITE_CONTENIDO);
    }

    private boolean validarFormulario() {
        if (cmbTipoMensaje.getValue() == null) {
            mostrarMensaje("Seleccione el tipo de mensaje.", "message-error");
            cmbTipoMensaje.requestFocus();
            return false;
        }

        if (cmbPrioridad.getValue() == null) {
            mostrarMensaje("Seleccione la prioridad de la comunicación.", "message-error");
            cmbPrioridad.requestFocus();
            return false;
        }

        if (txtAsunto.getText() == null || txtAsunto.getText().isBlank()) {
            mostrarMensaje("Ingrese el asunto del mensaje.", "message-error");
            txtAsunto.requestFocus();
            return false;
        }

        if (txtAsunto.getText().trim().length() < 5) {
            mostrarMensaje("El asunto debe contener al menos 5 caracteres.", "message-error");
            txtAsunto.requestFocus();
            return false;
        }

        if (txtContenido.getText() == null || txtContenido.getText().isBlank()) {
            mostrarMensaje("Escriba el contenido de la comunicación.", "message-error");
            txtContenido.requestFocus();
            return false;
        }

        if (txtContenido.getText().trim().length() < 10) {
            mostrarMensaje("El contenido debe contener al menos 10 caracteres.", "message-error");
            txtContenido.requestFocus();
            return false;
        }

        return true;
    }

    private void limpiarCamposDeRedaccion() {
        txtAsunto.clear();
        txtContenido.clear();
        actualizarContadores();
        txtAsunto.requestFocus();
    }

    private void mostrarMensaje(String texto, String claseDeEstado) {
        lblMensaje.setText(texto);
        lblMensaje.getStyleClass().setAll("message-label", claseDeEstado);
    }

    private void limpiarMensajeEstado() {
        if (!lblMensaje.getText().isBlank()) {
            lblMensaje.setText("");
            lblMensaje.getStyleClass().setAll("message-label");
        }
    }

    private static Map<String, String> crearMapaDestinatarios() {
        Map<String, String> destinatarios = new LinkedHashMap<>();
        destinatarios.put(TIPO_RESIDENTES,
                "Destinatarios: todos los residentes activos del condominio.");
        destinatarios.put(TIPO_TRABAJADORES,
                "Destinatarios: todos los trabajadores activos del condominio.");
        destinatarios.put(TIPO_GLOBAL,
                "Destinatarios: residentes y trabajadores activos del condominio.");
        destinatarios.put(TIPO_URGENTE,
                "Destinatarios: usuarios definidos según el alcance del evento urgente.");
        destinatarios.put(TIPO_ALERTA,
                "Destinatarios: usuarios afectados por la situación de emergencia.");
        destinatarios.put(TIPO_BOLETIN,
                "Destinatarios: comunidad definida para la distribución del boletín.");
        return Map.copyOf(destinatarios);
    }

    public static final class MensajeReciente {
        private final String fecha;
        private final String tipo;
        private final String prioridad;
        private final String asunto;
        private final String estado;

        public MensajeReciente(
                String fecha,
                String tipo,
                String prioridad,
                String asunto,
                String estado
        ) {
            this.fecha = fecha;
            this.tipo = tipo;
            this.prioridad = prioridad;
            this.asunto = asunto;
            this.estado = estado;
        }

        public String getFecha() {
            return fecha;
        }

        public String getTipo() {
            return tipo;
        }

        public String getPrioridad() {
            return prioridad;
        }

        public String getAsunto() {
            return asunto;
        }

        public String getEstado() {
            return estado;
        }
    }
}