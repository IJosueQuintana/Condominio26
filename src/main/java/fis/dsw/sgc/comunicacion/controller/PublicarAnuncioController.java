package fis.dsw.sgc.comunicacion.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PublicarAnuncioController {
    private static final int LIMITE_TITULO = 120;
    private static final int LIMITE_CONTENIDO = 2000;
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML private ComboBox<String> cmbTipoAnuncio;
    @FXML private ComboBox<String> cmbPrioridad;
    @FXML private DatePicker dpFechaPublicacion;
    @FXML private DatePicker dpFechaExpiracion;
    @FXML private TextField txtTitulo;
    @FXML private TextArea txtContenido;
    @FXML private Label lblContadorTitulo;
    @FXML private Label lblContadorContenido;
    @FXML private Label lblMensaje;
    @FXML private TableView<AnuncioReciente> tblAnunciosRecientes;
    @FXML private TableColumn<AnuncioReciente, String> colFecha;
    @FXML private TableColumn<AnuncioReciente, String> colTipo;
    @FXML private TableColumn<AnuncioReciente, String> colPrioridad;
    @FXML private TableColumn<AnuncioReciente, String> colTitulo;
    @FXML private TableColumn<AnuncioReciente, String> colVigencia;
    @FXML private TableColumn<AnuncioReciente, String> colEstado;

    private final ObservableList<AnuncioReciente> anuncios = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        cmbTipoAnuncio.setItems(FXCollections.observableArrayList(
                "Anuncio general", "Aviso de mantenimiento", "Boletín informativo", "Alerta de emergencia"));
        cmbPrioridad.setItems(FXCollections.observableArrayList("Baja", "Normal", "Alta", "Urgente"));
        cmbTipoAnuncio.getSelectionModel().select("Anuncio general");
        cmbPrioridad.getSelectionModel().select("Normal");
        dpFechaPublicacion.setValue(LocalDate.now());
        dpFechaExpiracion.setValue(LocalDate.now().plusDays(7));

        txtTitulo.setTextFormatter(new TextFormatter<String>(c -> c.getControlNewText().length() <= LIMITE_TITULO ? c : null));
        txtContenido.setTextFormatter(new TextFormatter<String>(c -> c.getControlNewText().length() <= LIMITE_CONTENIDO ? c : null));
        txtTitulo.textProperty().addListener((o,a,n) -> actualizarContadores());
        txtContenido.textProperty().addListener((o,a,n) -> actualizarContadores());

        colFecha.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().fecha()));
        colTipo.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().tipo()));
        colPrioridad.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().prioridad()));
        colTitulo.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().titulo()));
        colVigencia.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().vigencia()));
        colEstado.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().estado()));
        tblAnunciosRecientes.setItems(anuncios);
        tblAnunciosRecientes.setPlaceholder(new Label("No existen anuncios recientes para mostrar."));
        actualizarContadores();
        cargarAnuncios();
    }

    @FXML
    private void publicarAnuncio() {
        if (!validarFormulario()) return;
        String vigencia = dpFechaPublicacion.getValue().format(FORMATO) + " - " + dpFechaExpiracion.getValue().format(FORMATO);
        anuncios.add(0, new AnuncioReciente(LocalDate.now().format(FORMATO), cmbTipoAnuncio.getValue(),
                cmbPrioridad.getValue(), txtTitulo.getText().trim(), vigencia, "Preparado"));
        mostrarMensaje("El anuncio fue validado y quedó preparado para conectarse con el servicio de Comunicación.", "message-success");
        limpiarRedaccion();
    }

    @FXML private void limpiarFormulario() {
        cmbTipoAnuncio.getSelectionModel().select("Anuncio general");
        cmbPrioridad.getSelectionModel().select("Normal");
        dpFechaPublicacion.setValue(LocalDate.now());
        dpFechaExpiracion.setValue(LocalDate.now().plusDays(7));
        limpiarRedaccion();
        mostrarMensaje("Formulario limpiado.", "message-info");
    }

    @FXML private void cancelar() {
        limpiarFormulario();
        mostrarMensaje("Operación cancelada. No se publicó ningún anuncio.", "message-info");
    }

    @FXML private void seleccionarAnuncio() {
        AnuncioReciente seleccionado = tblAnunciosRecientes.getSelectionModel().getSelectedItem();
        if (seleccionado != null) mostrarMensaje("Seleccionado: “" + seleccionado.titulo() + "”.", "message-info");
    }

    public void cargarAnuncios() {
        anuncios.clear();
        // Integración pendiente: comunicacionService.obtenerAnunciosRecientes().
    }

    private boolean validarFormulario() {
        if (cmbTipoAnuncio.getValue() == null) return error("Seleccione el tipo de anuncio.", cmbTipoAnuncio);
        if (cmbPrioridad.getValue() == null) return error("Seleccione la prioridad.", cmbPrioridad);
        if (dpFechaPublicacion.getValue() == null) return error("Seleccione la fecha de publicación.", dpFechaPublicacion);
        if (dpFechaExpiracion.getValue() == null) return error("Seleccione la fecha de expiración.", dpFechaExpiracion);
        if (dpFechaExpiracion.getValue().isBefore(dpFechaPublicacion.getValue())) return error("La fecha de expiración no puede ser anterior a la publicación.", dpFechaExpiracion);
        if (txtTitulo.getText() == null || txtTitulo.getText().trim().length() < 5) return error("Ingrese un título de al menos 5 caracteres.", txtTitulo);
        if (txtContenido.getText() == null || txtContenido.getText().trim().length() < 10) return error("Ingrese un contenido de al menos 10 caracteres.", txtContenido);
        return true;
    }

    private boolean error(String texto, Control control) {
        mostrarMensaje(texto, "message-error");
        control.requestFocus();
        return false;
    }

    private void limpiarRedaccion() { txtTitulo.clear(); txtContenido.clear(); actualizarContadores(); }
    private void actualizarContadores() {
        lblContadorTitulo.setText((txtTitulo.getText() == null ? 0 : txtTitulo.getText().length()) + " / " + LIMITE_TITULO);
        lblContadorContenido.setText((txtContenido.getText() == null ? 0 : txtContenido.getText().length()) + " / " + LIMITE_CONTENIDO);
    }
    private void mostrarMensaje(String texto, String clase) { lblMensaje.setText(texto); lblMensaje.getStyleClass().setAll("message-label", clase); }

    public record AnuncioReciente(String fecha, String tipo, String prioridad, String titulo, String vigencia, String estado) {}
}
