package fis.dsw.sgc.comunicacion.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class ConsultarHistorialController {
    @FXML private DatePicker dpFechaDesde;
    @FXML private DatePicker dpFechaHasta;
    @FXML private ComboBox<String> cmbTipo;
    @FXML private ComboBox<String> cmbEstado;
    @FXML private TextField txtBusqueda;
    @FXML private Label lblMensaje;
    @FXML private Label lblTotalResultados;
    @FXML private TableView<RegistroHistorial> tblHistorial;
    @FXML private TableColumn<RegistroHistorial, String> colFecha;
    @FXML private TableColumn<RegistroHistorial, String> colTipo;
    @FXML private TableColumn<RegistroHistorial, String> colPrioridad;
    @FXML private TableColumn<RegistroHistorial, String> colAsunto;
    @FXML private TableColumn<RegistroHistorial, String> colEstado;
    @FXML private TableColumn<RegistroHistorial, String> colEmisor;

    private final ObservableList<RegistroHistorial> registros = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        cmbTipo.setItems(FXCollections.observableArrayList("Todos", "Mensaje", "Anuncio", "Notificación", "Alerta", "Boletín"));
        cmbEstado.setItems(FXCollections.observableArrayList("Todos", "Creada", "Programada", "Enviada", "Fallida", "Cancelada", "Eliminada"));
        cmbTipo.getSelectionModel().selectFirst();
        cmbEstado.getSelectionModel().selectFirst();
        dpFechaDesde.setValue(LocalDate.now().minusMonths(1));
        dpFechaHasta.setValue(LocalDate.now());

        colFecha.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().fecha()));
        colTipo.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().tipo()));
        colPrioridad.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().prioridad()));
        colAsunto.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().asunto()));
        colEstado.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().estado()));
        colEmisor.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().emisor()));
        tblHistorial.setItems(registros);
        tblHistorial.setPlaceholder(new Label("No existen registros para los filtros seleccionados."));
        cargarHistorial();
    }

    @FXML private void buscar() {
        if (!validarFechas()) return;
        // Integración pendiente: historialService.buscar(filtros).
        actualizarTotal();
        mostrarMensaje("Búsqueda realizada. Los filtros están preparados para conectarse con el servicio.", "message-info");
    }

    @FXML private void limpiarFiltros() {
        dpFechaDesde.setValue(LocalDate.now().minusMonths(1));
        dpFechaHasta.setValue(LocalDate.now());
        cmbTipo.getSelectionModel().selectFirst();
        cmbEstado.getSelectionModel().selectFirst();
        txtBusqueda.clear();
        tblHistorial.getSelectionModel().clearSelection();
        cargarHistorial();
        mostrarMensaje("Filtros restablecidos.", "message-info");
    }

    @FXML private void verDetalle() {
        RegistroHistorial seleccionado = tblHistorial.getSelectionModel().getSelectedItem();
        if (seleccionado == null) { mostrarMensaje("Seleccione un registro para consultar su detalle.", "message-error"); return; }
        mostrarMensaje("Detalle seleccionado: “" + seleccionado.asunto() + "” · " + seleccionado.estado() + ".", "message-info");
    }

    @FXML private void actualizarHistorial() {
        cargarHistorial();
        mostrarMensaje("Historial actualizado.", "message-success");
    }

    public void cargarHistorial() {
        registros.clear();
        actualizarTotal();
        // Integración pendiente: historialService.obtenerHistorial().
    }

    private boolean validarFechas() {
        if (dpFechaDesde.getValue() == null || dpFechaHasta.getValue() == null) {
            mostrarMensaje("Seleccione el rango completo de fechas.", "message-error"); return false;
        }
        if (dpFechaHasta.getValue().isBefore(dpFechaDesde.getValue())) {
            mostrarMensaje("La fecha hasta no puede ser anterior a la fecha desde.", "message-error"); return false;
        }
        return true;
    }

    private void actualizarTotal() { lblTotalResultados.setText(registros.size() + " resultado(s)"); }
    private void mostrarMensaje(String texto, String clase) { lblMensaje.setText(texto); lblMensaje.getStyleClass().setAll("message-label", clase); }

    public record RegistroHistorial(String fecha, String tipo, String prioridad, String asunto, String estado, String emisor) {}
}
