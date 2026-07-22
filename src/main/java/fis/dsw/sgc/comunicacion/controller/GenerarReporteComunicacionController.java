package fis.dsw.sgc.comunicacion.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class GenerarReporteComunicacionController {
    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;
    @FXML private ComboBox<String> cmbTipo;
    @FXML private ComboBox<String> cmbFormato;
    @FXML private Label lblMensaje;
    @FXML private Label lblTotalComunicaciones;
    @FXML private Label lblTotalEnviadas;
    @FXML private Label lblTotalFallidas;
    @FXML private TableView<ResumenReporte> tblResumen;
    @FXML private TableColumn<ResumenReporte, String> colTipo;
    @FXML private TableColumn<ResumenReporte, String> colCantidad;
    @FXML private TableColumn<ResumenReporte, String> colEnviadas;
    @FXML private TableColumn<ResumenReporte, String> colFallidas;
    @FXML private TableColumn<ResumenReporte, String> colTasaExito;

    private final ObservableList<ResumenReporte> resumen = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        dpFechaInicio.setValue(LocalDate.now().withDayOfMonth(1));
        dpFechaFin.setValue(LocalDate.now());
        cmbTipo.setItems(FXCollections.observableArrayList("Todos", "Mensajes", "Anuncios", "Notificaciones", "Alertas", "Boletines"));
        cmbFormato.setItems(FXCollections.observableArrayList("PDF", "Excel"));
        cmbTipo.getSelectionModel().selectFirst();
        cmbFormato.getSelectionModel().selectFirst();

        colTipo.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().tipo()));
        colCantidad.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().cantidad()));
        colEnviadas.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().enviadas()));
        colFallidas.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().fallidas()));
        colTasaExito.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().tasaExito()));
        tblResumen.setItems(resumen);
        tblResumen.setPlaceholder(new Label("Genere una vista previa para consultar el resumen."));
        actualizarIndicadores();
    }

    @FXML private void generarVistaPrevia() {
        if (!validarFechas()) return;
        resumen.clear();
        // Integración pendiente: reporteService.generarResumen(filtros).
        actualizarIndicadores();
        mostrarMensaje("Vista previa preparada. Se mostrarán datos cuando el servicio esté conectado.", "message-info");
    }

    @FXML private void generarReporte() {
        if (!validarFechas()) return;
        String formato = cmbFormato.getValue();
        mostrarMensaje("La generación en formato " + formato + " quedó preparada para conectarse con el servicio de reportes.", "message-success");
    }

    @FXML private void limpiarFiltros() {
        dpFechaInicio.setValue(LocalDate.now().withDayOfMonth(1));
        dpFechaFin.setValue(LocalDate.now());
        cmbTipo.getSelectionModel().selectFirst();
        cmbFormato.getSelectionModel().selectFirst();
        resumen.clear();
        actualizarIndicadores();
        mostrarMensaje("Filtros restablecidos.", "message-info");
    }

    private boolean validarFechas() {
        if (dpFechaInicio.getValue() == null || dpFechaFin.getValue() == null) {
            mostrarMensaje("Seleccione el rango completo de fechas.", "message-error"); return false;
        }
        if (dpFechaFin.getValue().isBefore(dpFechaInicio.getValue())) {
            mostrarMensaje("La fecha fin no puede ser anterior a la fecha inicio.", "message-error"); return false;
        }
        return true;
    }

    private void actualizarIndicadores() {
        int total = resumen.stream().mapToInt(r -> parse(r.cantidad())).sum();
        int enviadas = resumen.stream().mapToInt(r -> parse(r.enviadas())).sum();
        int fallidas = resumen.stream().mapToInt(r -> parse(r.fallidas())).sum();
        lblTotalComunicaciones.setText(String.valueOf(total));
        lblTotalEnviadas.setText(String.valueOf(enviadas));
        lblTotalFallidas.setText(String.valueOf(fallidas));
    }
    private int parse(String valor) { try { return Integer.parseInt(valor); } catch (NumberFormatException e) { return 0; } }
    private void mostrarMensaje(String texto, String clase) { lblMensaje.setText(texto); lblMensaje.getStyleClass().setAll("message-label", clase); }

    public record ResumenReporte(String tipo, String cantidad, String enviadas, String fallidas, String tasaExito) {}
}
