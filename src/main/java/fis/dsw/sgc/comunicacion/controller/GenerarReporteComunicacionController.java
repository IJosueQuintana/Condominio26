package fis.dsw.sgc.comunicacion.controller;

import fis.dsw.sgc.comunicacion.dto.ResumenReporteDTO;
import fis.dsw.sgc.comunicacion.exception.ComunicacionException;
import fis.dsw.sgc.comunicacion.service.IComunicacionService;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.Objects;

import java.time.LocalDate;

public class GenerarReporteComunicacionController {
    @FXML private DatePicker dpFechaInicio,dpFechaFin;
    @FXML private ComboBox<String> cmbTipo,cmbFormato;
    @FXML private Label lblMensaje,lblTotalComunicaciones,lblTotalEnviadas,lblTotalFallidas;
    @FXML private TableView<ResumenReporteDTO> tblResumen;
    @FXML private TableColumn<ResumenReporteDTO,String> colTipo,colCantidad,colEnviadas,colFallidas,colTasaExito;

    private final ObservableList<ResumenReporteDTO> datos=FXCollections.observableArrayList();
    private IComunicacionService service;

    @FXML public void initialize() {
        dpFechaInicio.setValue(LocalDate.now().withDayOfMonth(1));
        dpFechaFin.setValue(LocalDate.now());
        cmbTipo.setItems(FXCollections.observableArrayList(
                "Todos","Mensajes","Anuncios","Notificaciones","Alertas","Boletines"));
        cmbFormato.setItems(FXCollections.observableArrayList(
                "Vista en pantalla","PDF","Excel"));
        cmbTipo.getSelectionModel().selectFirst();
        cmbFormato.getSelectionModel().selectFirst();

        colTipo.setCellValueFactory(d->new ReadOnlyStringWrapper(d.getValue().tipo()));
        colCantidad.setCellValueFactory(d->new ReadOnlyStringWrapper(String.valueOf(d.getValue().cantidad())));
        colEnviadas.setCellValueFactory(d->new ReadOnlyStringWrapper(String.valueOf(d.getValue().enviadas())));
        colFallidas.setCellValueFactory(d->new ReadOnlyStringWrapper(String.valueOf(d.getValue().fallidas())));
        colTasaExito.setCellValueFactory(d->new ReadOnlyStringWrapper(d.getValue().tasaExitoFormateada()));
        tblResumen.setItems(datos);
        tblResumen.setPlaceholder(new Label("No existen datos para el rango seleccionado."));
        indicadores();
    }
    public void setComunicacionService(
            IComunicacionService service
    ) {
        this.service = Objects.requireNonNull(
                service,
                "El servicio de Comunicación no puede ser nulo."
        );

        generarVistaPrevia();
    }

    @FXML private void generarVistaPrevia() {
        if (!fechas()||service==null) return;
        try {
            datos.setAll(service.generarResumen(
                    dpFechaInicio.getValue(),dpFechaFin.getValue(),cmbTipo.getValue()));
            indicadores();
            mensaje("Vista previa generada desde SQLite.","message-success");
        } catch (ComunicacionException e) { mensaje(e.getMessage(),"message-error"); }
    }

    @FXML private void generarReporte() {
        if (!fechas()) return;
        generarVistaPrevia();
        String formato=cmbFormato.getValue();
        if ("Vista en pantalla".equals(formato))
            mensaje("El reporte está disponible en pantalla.","message-success");
        else
            mensaje("Los datos están listos. La exportación "+formato+
                    " queda para la etapa de reportes.","message-info");
    }

    @FXML private void limpiarFiltros() {
        dpFechaInicio.setValue(LocalDate.now().withDayOfMonth(1));
        dpFechaFin.setValue(LocalDate.now());
        cmbTipo.getSelectionModel().selectFirst();
        cmbFormato.getSelectionModel().selectFirst();
        datos.clear();
        indicadores();
        mensaje("Filtros restablecidos.","message-info");
    }

    private boolean fechas() {
        if (dpFechaInicio.getValue()==null||dpFechaFin.getValue()==null) {
            mensaje("Seleccione el rango completo.","message-error"); return false;
        }
        if (dpFechaFin.getValue().isBefore(dpFechaInicio.getValue())) {
            mensaje("La fecha fin no puede ser anterior.","message-error"); return false;
        }
        return true;
    }

    private void indicadores() {
        lblTotalComunicaciones.setText(String.valueOf(datos.stream().mapToInt(ResumenReporteDTO::cantidad).sum()));
        lblTotalEnviadas.setText(String.valueOf(datos.stream().mapToInt(ResumenReporteDTO::enviadas).sum()));
        lblTotalFallidas.setText(String.valueOf(datos.stream().mapToInt(ResumenReporteDTO::fallidas).sum()));
    }

    private void mensaje(String t,String c) { lblMensaje.setText(t); lblMensaje.getStyleClass().setAll("message-label",c); }
}
