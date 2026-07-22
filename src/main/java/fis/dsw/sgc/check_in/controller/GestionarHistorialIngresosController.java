package fis.dsw.sgc.check_in.controller;

import fis.dsw.sgc.check_in.dto.RegistroEntradaDTO;
import fis.dsw.sgc.check_in.model.ReporteHistorial;
import fis.dsw.sgc.check_in.service.ICheckInService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.List;

public class GestionarHistorialIngresosController {

    @FXML private VBox panelPrincipal;
    @FXML private TextField txtBusquedaPersona;
    @FXML private ChoiceBox<String> cbTipoIngreso;
    @FXML private TextField txtPlaca;
    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;
    @FXML private Button btnBuscar;

    @FXML private Label lblMensaje;
    @FXML private TableView<RegistroEntradaDTO> tablaHistorial;
    @FXML private TableColumn<RegistroEntradaDTO, String> colFecha;
    @FXML private TableColumn<RegistroEntradaDTO, String> colHora;
    @FXML private TableColumn<RegistroEntradaDTO, String> colTipo;
    @FXML private TableColumn<RegistroEntradaDTO, String> colPersona;
    @FXML private TableColumn<RegistroEntradaDTO, String> colDetalle;

    @FXML private VBox panelDetalle;
    @FXML private Label lblDetalleTipo;
    @FXML private Label lblDetallePersona;
    @FXML private Label lblDetalleFechaHora;
    @FXML private Label lblDetalleMotivo;
    @FXML private Label lblDetallePlaca;
    @FXML private Label lblDetalleInfoAdicional;

    private ICheckInService checkInService;
    private final ObservableList<RegistroEntradaDTO> historial = FXCollections.observableArrayList();

    public GestionarHistorialIngresosController() {
        this(new fis.dsw.sgc.check_in.service.CheckInServiceImpl());
    }

    public GestionarHistorialIngresosController(ICheckInService checkInService) {
        this.checkInService = checkInService;
    }

    /** Setter para DI manual por mainWindowController tras FXMLLoader */
    public void setCheckInService(ICheckInService checkInService) {
        this.checkInService = checkInService;
    }

    public ICheckInService getCheckInService() {
        return checkInService;
    }

    @FXML
    public void initialize() {
        if (cbTipoIngreso != null) {
            cbTipoIngreso.setItems(FXCollections.observableArrayList("Todos", "RESIDENTE", "VISITANTE", "EXTERNA"));
            cbTipoIngreso.getSelectionModel().selectFirst();
        }

        if (colFecha != null) colFecha.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFecha()));
        if (colHora != null) colHora.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getHora()));
        if (colTipo != null) colTipo.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTipoEntrada()));
        if (colPersona != null) colPersona.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPersona()));
        if (colDetalle != null) colDetalle.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDestino()));

        if (tablaHistorial != null) {
            tablaHistorial.setItems(historial);
            tablaHistorial.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
                if (newSel != null) {
                    mostrarDetalleIngreso(newSel);
                }
            });
        }

        buscarHistorial(null);
    }

    @FXML
    void buscarHistorial(ActionEvent event) {
        if (checkInService == null) return;

        String fechaInicio = dpFechaInicio != null && dpFechaInicio.getValue() != null ? dpFechaInicio.getValue().toString() : null;
        String fechaFin = dpFechaFin != null && dpFechaFin.getValue() != null ? dpFechaFin.getValue().toString() : null;
        String tipo = cbTipoIngreso != null ? cbTipoIngreso.getValue() : null;
        String persona = txtBusquedaPersona != null ? txtBusquedaPersona.getText() : null;
        String placa = txtPlaca != null ? txtPlaca.getText() : null;

        List<RegistroEntradaDTO> resultados = checkInService.obtenerHistorialDTOAvanzado(fechaInicio, fechaFin, tipo, persona, placa);
        historial.setAll(resultados);

        mostrarInfo("Se encontraron " + resultados.size() + " registros de ingreso.");
    }

    @FXML
    void generarReporte(ActionEvent event) {
        if (checkInService == null) return;

        String fechaInicio = dpFechaInicio != null && dpFechaInicio.getValue() != null ? dpFechaInicio.getValue().toString() : null;
        String fechaFin = dpFechaFin != null && dpFechaFin.getValue() != null ? dpFechaFin.getValue().toString() : null;
        String tipo = cbTipoIngreso != null ? cbTipoIngreso.getValue() : null;
        String persona = txtBusquedaPersona != null && !txtBusquedaPersona.getText().isBlank() ? txtBusquedaPersona.getText().trim() : null;
        String placa = txtPlaca != null && !txtPlaca.getText().isBlank() ? txtPlaca.getText().trim() : null;

        List<RegistroEntradaDTO> lista = historial;

        int total = lista.size();
        long residentes = lista.stream().filter(r -> "RESIDENTE".equalsIgnoreCase(r.getTipoEntrada())).count();
        long visitantes = lista.stream().filter(r -> "VISITANTE".equalsIgnoreCase(r.getTipoEntrada())).count();
        long externos = lista.stream().filter(r -> "EXTERNA".equalsIgnoreCase(r.getTipoEntrada())).count();
        long conVehiculo = lista.stream().filter(r -> r.getParqueadero() != null && !r.getParqueadero().isBlank() && !"-".equals(r.getParqueadero()) && !"N/A".equalsIgnoreCase(r.getParqueadero())).count();

        StringBuilder sb = new StringBuilder();
        sb.append("=========================================================================\n");
        sb.append("                 REPORTE DETALLADO DE INGRESOS (CHECK-IN)                \n");
        sb.append("=========================================================================\n\n");
        sb.append("FILTROS APLICADOS:\n");
        sb.append(" • Persona / Cédula: ").append(persona != null ? persona : "Todos").append("\n");
        sb.append(" • Tipo de Ingreso: ").append(tipo != null ? tipo : "Todos").append("\n");
        sb.append(" • Placa de Vehículo: ").append(placa != null ? placa : "Todas").append("\n");
        sb.append(" • Rango de Fechas: ").append(fechaInicio != null ? fechaInicio : "Inicio").append(" a ").append(fechaFin != null ? fechaFin : "Fin").append("\n\n");
        sb.append("RESUMEN ESTADÍSTICO:\n");
        sb.append(" • Total Registros: ").append(total).append("\n");
        sb.append(" • Residentes: ").append(residentes).append("\n");
        sb.append(" • Visitantes: ").append(visitantes).append("\n");
        sb.append(" • Externos: ").append(externos).append("\n");
        sb.append(" • Vehículos Registrados: ").append(conVehiculo).append("\n\n");
        sb.append("=========================================================================\n");
        sb.append("                    DETALLE INDIVIDUAL DE REGISTROS                      \n");
        sb.append("=========================================================================\n\n");

        if (lista.isEmpty()) {
            sb.append("No se encontraron registros de ingreso con los filtros seleccionados.\n");
        } else {
            int count = 1;
            for (RegistroEntradaDTO r : lista) {
                sb.append(String.format("%d. [%s %s] - Tipo: %s\n", count++, r.getFecha(), r.getHora(), r.getTipoEntrada()));
                sb.append("   • Persona: ").append(r.getPersona()).append(" (Cédula: ").append(r.getCedula()).append(")\n");
                sb.append("   • Destino / Unidad: ").append(r.getDestino()).append("\n");
                sb.append("   • Vehículo / Placa: ").append(r.getParqueadero() != null && !r.getParqueadero().isBlank() ? r.getParqueadero() : "Sin vehículo").append("\n");
                if (r.getObservaciones() != null && !r.getObservaciones().isBlank()) {
                    sb.append("   • Observaciones: ").append(r.getObservaciones()).append("\n");
                }
                sb.append("-------------------------------------------------------------------------\n");
            }
        }

        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Reporte Detallado de Historial de Ingresos");
        alert.setHeaderText("Reporte de Ingresos (" + total + " registros encontrados)");

        javafx.scene.control.TextArea textArea = new javafx.scene.control.TextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefWidth(650);
        textArea.setPrefHeight(400);

        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();

        mostrarExito("Reporte generado con " + total + " registros.");
    }

    private void mostrarDetalleIngreso(RegistroEntradaDTO item) {
        if (panelDetalle == null) return;

        if (lblDetalleTipo != null) lblDetalleTipo.setText("Tipo: " + item.getTipoEntrada());
        if (lblDetallePersona != null) lblDetallePersona.setText("Persona: " + item.getPersona() + " (Cédula: " + item.getCedula() + ")");
        if (lblDetalleFechaHora != null) lblDetalleFechaHora.setText("Ingreso: " + item.getFecha() + " a las " + item.getHora());
        if (lblDetalleMotivo != null) lblDetalleMotivo.setText("Destino / Unidad: " + item.getDestino());
        if (lblDetallePlaca != null) lblDetallePlaca.setText("Vehículo / Placa: " + item.getParqueadero());
        if (lblDetalleInfoAdicional != null) lblDetalleInfoAdicional.setText("Observaciones: " + (item.getObservaciones().isBlank() ? "Sin incidencias" : item.getObservaciones()));

        panelDetalle.setVisible(true);
        panelDetalle.setManaged(true);
    }

    @FXML
    void cerrarDetalle(ActionEvent event) {
        if (panelDetalle != null) {
            panelDetalle.setVisible(false);
            panelDetalle.setManaged(false);
        }
    }

    private void mostrarInfo(String mensaje) {
        if (lblMensaje != null) {
            lblMensaje.setText(mensaje);
            lblMensaje.getStyleClass().setAll("message-label", "message-info");
        }
    }

    private void mostrarExito(String mensaje) {
        if (lblMensaje != null) {
            lblMensaje.setText(mensaje);
            lblMensaje.getStyleClass().setAll("message-label", "message-success");
        }
    }
}