package fis.dsw.sgc.comunicacion.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class GestionarNotificacionesController {
    @FXML private ComboBox<String> cmbTipo;
    @FXML private ComboBox<String> cmbEstado;
    @FXML private TextField txtBusqueda;
    @FXML private Label lblMensaje;
    @FXML private Label lblResumen;
    @FXML private TableView<NotificacionItem> tblNotificaciones;
    @FXML private TableColumn<NotificacionItem, String> colFecha;
    @FXML private TableColumn<NotificacionItem, String> colTipo;
    @FXML private TableColumn<NotificacionItem, String> colDestinatario;
    @FXML private TableColumn<NotificacionItem, String> colAsunto;
    @FXML private TableColumn<NotificacionItem, String> colEstado;

    private final ObservableList<NotificacionItem> notificaciones = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        cmbTipo.setItems(FXCollections.observableArrayList("Todas", "Mensaje", "Anuncio", "Alerta", "Recordatorio", "Sistema"));
        cmbEstado.setItems(FXCollections.observableArrayList("Todos", "Pendiente", "Enviada", "Leída", "Fallida", "Eliminada"));
        cmbTipo.getSelectionModel().selectFirst();
        cmbEstado.getSelectionModel().selectFirst();

        colFecha.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().fecha()));
        colTipo.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().tipo()));
        colDestinatario.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().destinatario()));
        colAsunto.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().asunto()));
        colEstado.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().estado()));
        tblNotificaciones.setItems(notificaciones);
        tblNotificaciones.setPlaceholder(new Label("No existen notificaciones para mostrar."));
        cargarNotificaciones();
    }

    @FXML private void aplicarFiltros() {
        // Integración pendiente: notificacionService.buscar(filtros).
        actualizarResumen();
        mostrarMensaje("Filtros aplicados.", "message-info");
    }

    @FXML private void limpiarFiltros() {
        cmbTipo.getSelectionModel().selectFirst();
        cmbEstado.getSelectionModel().selectFirst();
        txtBusqueda.clear();
        tblNotificaciones.getSelectionModel().clearSelection();
        cargarNotificaciones();
        mostrarMensaje("Filtros restablecidos.", "message-info");
    }

    @FXML private void marcarLeida() {
        NotificacionItem n = seleccionada();
        if (n == null) return;
        reemplazar(n, new NotificacionItem(n.fecha(), n.tipo(), n.destinatario(), n.asunto(), "Leída"));
        mostrarMensaje("Notificación marcada como leída.", "message-success");
    }

    @FXML private void eliminar() {
        NotificacionItem n = seleccionada();
        if (n == null) return;
        notificaciones.remove(n);
        actualizarResumen();
        mostrarMensaje("Notificación eliminada de la vista. La eliminación definitiva se conectará al servicio.", "message-info");
    }

    @FXML private void actualizarNotificaciones() {
        cargarNotificaciones();
        mostrarMensaje("Notificaciones actualizadas.", "message-success");
    }

    @FXML private void seleccionarNotificacion() {
        NotificacionItem n = tblNotificaciones.getSelectionModel().getSelectedItem();
        if (n != null) mostrarMensaje("Seleccionada: “" + n.asunto() + "”.", "message-info");
    }

    public void cargarNotificaciones() {
        notificaciones.clear();
        actualizarResumen();
        // Integración pendiente: notificacionService.obtenerNotificaciones().
    }

    private NotificacionItem seleccionada() {
        NotificacionItem n = tblNotificaciones.getSelectionModel().getSelectedItem();
        if (n == null) mostrarMensaje("Seleccione una notificación.", "message-error");
        return n;
    }

    private void reemplazar(NotificacionItem anterior, NotificacionItem nueva) {
        int indice = notificaciones.indexOf(anterior);
        if (indice >= 0) notificaciones.set(indice, nueva);
        tblNotificaciones.getSelectionModel().select(nueva);
        actualizarResumen();
    }

    private void actualizarResumen() {
        long pendientes = notificaciones.stream().filter(n -> "Pendiente".equals(n.estado())).count();
        lblResumen.setText(notificaciones.size() + " notificación(es) · " + pendientes + " pendiente(s)");
    }
    private void mostrarMensaje(String texto, String clase) { lblMensaje.setText(texto); lblMensaje.getStyleClass().setAll("message-label", clase); }

    public record NotificacionItem(String fecha, String tipo, String destinatario, String asunto, String estado) {}
}
