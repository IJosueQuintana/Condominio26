package fis.dsw.sgc.comunicacion.controller;

import fis.dsw.sgc.comunicacion.dto.HistorialDTO;
import fis.dsw.sgc.comunicacion.exception.ComunicacionException;
import fis.dsw.sgc.comunicacion.service.IComunicacionService;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.Objects;

import java.time.LocalDate;

public class ConsultarHistorialController {
    @FXML private DatePicker dpFechaDesde,dpFechaHasta;
    @FXML private ComboBox<String> cmbTipo,cmbEstado;
    @FXML private TextField txtBusqueda;
    @FXML private Label lblMensaje,lblTotalResultados,lblDetalleSeleccionado;
    @FXML private TableView<HistorialDTO> tblHistorial;
    @FXML private TableColumn<HistorialDTO,String> colFecha,colTipo,colPrioridad,colAsunto,colEstado,colEmisor;

    private final ObservableList<HistorialDTO> datos=FXCollections.observableArrayList();
    private IComunicacionService service;

    @FXML public void initialize() {
        cmbTipo.setItems(FXCollections.observableArrayList(
                "Todos","Mensaje","Anuncio","Notificación","Alerta","Boletín"));
        cmbEstado.setItems(FXCollections.observableArrayList(
                "Todos","Enviado","Publicado","Leída","Fallida","Cancelada","Eliminada"));
        cmbTipo.getSelectionModel().selectFirst();
        cmbEstado.getSelectionModel().selectFirst();
        dpFechaDesde.setValue(LocalDate.now().minusMonths(1));
        dpFechaHasta.setValue(LocalDate.now());

        colFecha.setCellValueFactory(d->new ReadOnlyStringWrapper(d.getValue().fecha()));
        colTipo.setCellValueFactory(d->new ReadOnlyStringWrapper(d.getValue().tipo()));
        colPrioridad.setCellValueFactory(d->new ReadOnlyStringWrapper(d.getValue().prioridad()));
        colAsunto.setCellValueFactory(d->new ReadOnlyStringWrapper(d.getValue().asunto()));
        colEstado.setCellValueFactory(d->new ReadOnlyStringWrapper(d.getValue().estado()));
        colEmisor.setCellValueFactory(d->new ReadOnlyStringWrapper(d.getValue().emisor()));
        tblHistorial.setItems(datos);
        tblHistorial.setPlaceholder(new Label("No existen registros para los filtros."));
        tblHistorial.getSelectionModel().selectedItemProperty()
                .addListener((o,a,n)->detalle(n));

    }
    public void setComunicacionService(
            IComunicacionService service
    ) {
        this.service = Objects.requireNonNull(
                service,
                "El servicio de Comunicación no puede ser nulo."
        );

        cargarHistorial();
    }
    @FXML private void buscar() {
        if (!fechas()) return;
        cargarHistorial();
        mensaje("Búsqueda realizada.","message-info");
    }

    @FXML private void limpiarFiltros() {
        dpFechaDesde.setValue(LocalDate.now().minusMonths(1));
        dpFechaHasta.setValue(LocalDate.now());
        cmbTipo.getSelectionModel().selectFirst();
        cmbEstado.getSelectionModel().selectFirst();
        txtBusqueda.clear();
        tblHistorial.getSelectionModel().clearSelection();
        detalle(null);
        cargarHistorial();
        mensaje("Filtros restablecidos.","message-info");
    }

    @FXML private void verDetalle() {
        HistorialDTO d=tblHistorial.getSelectionModel().getSelectedItem();
        if (d==null) { mensaje("Seleccione un registro.","message-error"); return; }
        detalle(d);
        mensaje("Detalle cargado para “"+d.asunto()+"”.","message-info");
    }

    @FXML private void actualizarHistorial() {
        cargarHistorial();
        mensaje("Historial actualizado.","message-success");
    }

    public void cargarHistorial() {
        if (service==null||!fechas()) return;
        try {
            datos.setAll(service.buscarHistorial(
                    dpFechaDesde.getValue(),dpFechaHasta.getValue(),
                    cmbTipo.getValue(),cmbEstado.getValue(),txtBusqueda.getText()));
            lblTotalResultados.setText(datos.size()+" resultado(s)");
        } catch (ComunicacionException e) { mensaje(e.getMessage(),"message-error"); }
    }

    private boolean fechas() {
        if (dpFechaDesde.getValue()==null||dpFechaHasta.getValue()==null) {
            mensaje("Seleccione el rango completo.","message-error"); return false;
        }
        if (dpFechaHasta.getValue().isBefore(dpFechaDesde.getValue())) {
            mensaje("La fecha hasta no puede ser anterior.","message-error"); return false;
        }
        return true;
    }

    private void detalle(HistorialDTO d) {
        if (d==null) {
            lblDetalleSeleccionado.setText("Seleccione un registro para consultar su detalle.");
            return;
        }
        String det=d.detalle()==null||d.detalle().isBlank()?"Sin detalle adicional.":d.detalle();
        lblDetalleSeleccionado.setText(d.fecha()+" · "+d.tipo()+" · "+d.emisor()+"\n"+det);
    }

    private void mensaje(String t,String c) { lblMensaje.setText(t); lblMensaje.getStyleClass().setAll("message-label",c); }
}
