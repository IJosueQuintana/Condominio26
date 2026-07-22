package fis.dsw.sgc.comunicacion.controller;

import fis.dsw.sgc.comunicacion.dto.NotificacionDTO;
import fis.dsw.sgc.comunicacion.exception.ComunicacionException;
import fis.dsw.sgc.comunicacion.service.IComunicacionService;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.Objects;

public class GestionarNotificacionesController {
    @FXML private ComboBox<String> cmbTipo,cmbEstado;
    @FXML private TextField txtBusqueda;
    @FXML private Label lblMensaje,lblResumen;
    @FXML private TableView<NotificacionDTO> tblNotificaciones;
    @FXML private TableColumn<NotificacionDTO,String> colFecha,colTipo,colDestinatario,colAsunto,colEstado;
    @FXML private Button btnMarcarLeida,btnEliminar;

    private final ObservableList<NotificacionDTO> datos=FXCollections.observableArrayList();
    private IComunicacionService service;

    @FXML public void initialize() {
        cmbTipo.setItems(FXCollections.observableArrayList(
                "Todas","Mensaje","Anuncio","Alerta","Recordatorio","Sistema"));
        cmbEstado.setItems(FXCollections.observableArrayList(
                "Todos","Pendiente","Enviada","Leída","Fallida","Eliminada"));
        cmbTipo.getSelectionModel().selectFirst();
        cmbEstado.getSelectionModel().selectFirst();

        colFecha.setCellValueFactory(d->new ReadOnlyStringWrapper(d.getValue().fecha()));
        colTipo.setCellValueFactory(d->new ReadOnlyStringWrapper(d.getValue().tipo()));
        colDestinatario.setCellValueFactory(d->new ReadOnlyStringWrapper(d.getValue().destinatario()));
        colAsunto.setCellValueFactory(d->new ReadOnlyStringWrapper(d.getValue().asunto()));
        colEstado.setCellValueFactory(d->new ReadOnlyStringWrapper(d.getValue().estado()));
        tblNotificaciones.setItems(datos);
        tblNotificaciones.setPlaceholder(new Label("No existen notificaciones."));

    }
    public void setComunicacionService(
            IComunicacionService service
    ) {
        this.service = Objects.requireNonNull(
                service,
                "El servicio de Comunicación no puede ser nulo."
        );

        cargarNotificaciones();
    }

    @FXML private void aplicarFiltros() {
        cargarNotificaciones();
        mensaje("Filtros aplicados.","message-info");
    }

    @FXML private void limpiarFiltros() {
        cmbTipo.getSelectionModel().selectFirst();
        cmbEstado.getSelectionModel().selectFirst();
        txtBusqueda.clear();
        cargarNotificaciones();
        mensaje("Filtros restablecidos.","message-info");
    }

    @FXML private void marcarLeida() {
        NotificacionDTO d=seleccionada();
        if (d==null||service==null) return;
        try {
            service.marcarNotificacionLeida(d.id());
            cargarNotificaciones();
            mensaje("Notificación marcada como leída.","message-success");
        } catch (ComunicacionException e) { mensaje(e.getMessage(),"message-error"); }
    }

    @FXML private void eliminar() {
        NotificacionDTO d=seleccionada();
        if (d==null||service==null) return;
        try {
            service.eliminarNotificacion(d.id());
            cargarNotificaciones();
            mensaje("Notificación eliminada de manera lógica.","message-success");
        } catch (ComunicacionException e) { mensaje(e.getMessage(),"message-error"); }
    }

    @FXML private void actualizarNotificaciones() {
        cargarNotificaciones();
        mensaje("Notificaciones actualizadas.","message-success");
    }

    @FXML private void seleccionarNotificacion() {
        NotificacionDTO d=tblNotificaciones.getSelectionModel().getSelectedItem();
        if (d!=null) mensaje("Seleccionada: “"+d.asunto()+"” · "+d.estado()+".","message-info");
    }

    public void cargarNotificaciones() {
        if (service==null) return;
        try {
            datos.setAll(service.buscarNotificaciones(
                    cmbTipo.getValue(),cmbEstado.getValue(),txtBusqueda.getText()));
            resumen();
        } catch (ComunicacionException e) { mensaje(e.getMessage(),"message-error"); }
    }

    private NotificacionDTO seleccionada() {
        NotificacionDTO d=tblNotificaciones.getSelectionModel().getSelectedItem();
        if (d==null) mensaje("Seleccione una notificación.","message-error");
        return d;
    }

    private void resumen() {
        long pendientes=datos.stream().filter(d->"Pendiente".equalsIgnoreCase(d.estado())).count();
        long leidas=datos.stream().filter(d->"Leída".equalsIgnoreCase(d.estado())).count();
        lblResumen.setText(datos.size()+" total · "+pendientes+" pendiente(s) · "+leidas+" leída(s)");
    }

    private void mensaje(String t,String c) { lblMensaje.setText(t); lblMensaje.getStyleClass().setAll("message-label",c); }
}
