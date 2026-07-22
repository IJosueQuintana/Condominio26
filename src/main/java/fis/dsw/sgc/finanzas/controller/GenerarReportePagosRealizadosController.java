package fis.dsw.sgc.finanzas.controller;

import fis.dsw.sgc.administracion.model.Usuario;
import fis.dsw.sgc.administracion.service.GestionUsuariosServiceImpl;
import fis.dsw.sgc.core.session.SesionUsuario;
import fis.dsw.sgc.finanzas.dao.ReportesDAOImpl;
import fis.dsw.sgc.finanzas.dto.DetallePagoDTO;
import fis.dsw.sgc.finanzas.dto.ReportePagosDTO;
import fis.dsw.sgc.finanzas.service.IReportesService;
import fis.dsw.sgc.finanzas.service.ReportesServiceImpl;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class GenerarReportePagosRealizadosController implements Initializable {

    private final IReportesService reportesService;

    public GenerarReportePagosRealizadosController() {
        this(new ReportesServiceImpl(new ReportesDAOImpl(), new GestionUsuariosServiceImpl()));
    }

    public GenerarReportePagosRealizadosController(IReportesService reportesService) {
        this.reportesService = reportesService;
    }

    @FXML private Button btnGuardar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnBuscar;
    @FXML private TableColumn<DetallePagoDTO, LocalDate> colFecha;
    @FXML private TableColumn<DetallePagoDTO, String> colMotivo;
    @FXML private TableColumn<DetallePagoDTO, String> colNumeroCedulaIdentidad;
    @FXML private TableColumn<DetallePagoDTO, Double> colValor;
    @FXML private HBox dpFechaFin;
    @FXML private DatePicker dpFechaInicio;
    @FXML private Label lblIconoCedula;
    @FXML private Label lblIconoFechas;
    @FXML private Label lblIconoFechas1;
    @FXML private Label lblIconoGuardar;
    @FXML private Label lblIconoLimpiar;
    @FXML private Label lblIconoPagos;
    @FXML private Label lblIconoTotalAlicuotas;
    @FXML private Label lblIconoTotalGeneral;
    @FXML private Label lblIconoTotalMultas;
    @FXML private Label lblIconoTotalReservas;
    @FXML private TableView<DetallePagoDTO> tbReportePagos;
    @FXML private TextField txtNumeroCedulaIdentidad;
    @FXML private TextField txtTotalAlicuotas;
    @FXML private TextField txtTotalGeneral;
    @FXML private TextField txtTotalMultas;
    @FXML private TextField txtTotalReservas;

    @FXML
    void descargarReporte(ActionEvent event) {
        if (tbReportePagos.getItems().isEmpty()) {
            mostrarAlerta("Sin datos", "No existen registros en la tabla para descargar.", Alert.AlertType.WARNING);
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            File archivoDetalles = new File("reporte_pagos_detalles.csv");
            try (PrintWriter writer = new PrintWriter(archivoDetalles)) {
                writer.println("CEDULA,MOTIVO,FECHA,VALOR");
                for (DetallePagoDTO detalle : tbReportePagos.getItems()) {
                    String fecha = detalle.fecha == null ? "" : detalle.fecha.format(formatter);
                    writer.printf(Locale.US, "%s,%s,%s,%.2f%n",
                            textoSeguro(detalle.cedula),
                            textoSeguro(detalle.motivo),
                            fecha,
                            detalle.valor == null ? 0.0 : detalle.valor);
                }
            }

            File archivoTotales = new File("reporte_pagos_totales.csv");
            try (PrintWriter writer = new PrintWriter(archivoTotales)) {
                writer.println("CONCEPTO,VALOR");
                writer.println("Total Multas," + txtTotalMultas.getText());
                writer.println("Total Alicuotas," + txtTotalAlicuotas.getText());
                writer.println("Total Reservas," + txtTotalReservas.getText());
                writer.println("Total General," + txtTotalGeneral.getText());
            }

            mostrarAlerta("Descarga completada", "Los archivos CSV fueron descargados con éxito.", Alert.AlertType.INFORMATION);
        } catch (Exception ex) {
            mostrarAlerta("Error de exportación", "Error al crear los archivos de descarga: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void limpiarReporte(ActionEvent event) {
        tbReportePagos.getItems().clear();
        txtNumeroCedulaIdentidad.clear();
        txtTotalMultas.clear();
        txtTotalAlicuotas.clear();
        txtTotalReservas.clear();
        txtTotalGeneral.clear();
        dpFechaInicio.setValue(null);
        DatePicker fechaFin = obtenerDatePickerFin();
        if (fechaFin != null) {
            fechaFin.setValue(null);
        }
    }

    @FXML
    void consultarPagos(ActionEvent event) {
        LocalDate fechaInicio = dpFechaInicio.getValue();
        DatePicker datePickerFin = obtenerDatePickerFin();
        LocalDate fechaFin = datePickerFin == null ? null : datePickerFin.getValue();
        String cedula = txtNumeroCedulaIdentidad.getText() == null ? "" : txtNumeroCedulaIdentidad.getText().trim();
        Usuario usuarioActual = SesionUsuario.obtenerInstancia().getUsuarioActual();

        if (fechaInicio == null || fechaFin == null) {
            mostrarAlerta("Formato de fechas incorrecto", "Ingrese la fecha de inicio y la fecha de fin.", Alert.AlertType.WARNING);
            return;
        }
        if (!fechaInicio.isBefore(fechaFin)) {
            mostrarAlerta("Error en fechas", "La fecha de fin tiene que ser mayor que la fecha de inicio.", Alert.AlertType.WARNING);
            return;
        }
        if (fechaInicio.isAfter(LocalDate.now()) || fechaFin.isAfter(LocalDate.now())) {
            mostrarAlerta("Error en fechas", "Las fechas no pueden ser mayores que la fecha actual.", Alert.AlertType.WARNING);
            return;
        }
        boolean residente = esResidente(usuarioActual);
        if (residente) {
            cedula = usuarioActual.getCedula();
            if (cedula == null || cedula.isBlank()) {
                mostrarAlerta("Cédula no disponible", "No se pudo obtener la cédula del residente autenticado.", Alert.AlertType.ERROR);
                return;
            }
        }

        try {
            ReportePagosDTO reporte = residente
                    ? reportesService.consultarPagosEfectuados(fechaInicio, fechaFin, cedula)
                    : reportesService.generarReportedePagosRealizados(fechaInicio, fechaFin);
            mostrarReporte(reporte);
            mostrarAlerta("Éxito", "Reporte generado correctamente.", Alert.AlertType.INFORMATION);
        } catch (RuntimeException ex) {
            mostrarAlerta("Error al generar reporte", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colNumeroCedulaIdentidad.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().cedula));
        colMotivo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().motivo));
        colFecha.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().fecha));
        colValor.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().valor));

        FontIcon icon = new FontIcon("fa-chain");
        icon.getStyleClass().add("titleIcon");
        lblIconoPagos.setGraphic(icon);
        lblIconoPagos.setText(null);
        FontIcon icon2 = new FontIcon("fa-id-card");
        icon2.getStyleClass().add("optionsIcon");
        lblIconoCedula.setGraphic(icon2);
        lblIconoCedula.setText(null);
        FontIcon icon3 = new FontIcon("fa-calendar-minus-o");
        icon3.getStyleClass().add("optionsIcon");
        lblIconoFechas.setGraphic(icon3);
        lblIconoFechas.setText(null);
        FontIcon icon4 = new FontIcon("fa-calendar-plus-o");
        icon4.getStyleClass().add("optionsIcon");
        lblIconoFechas1.setGraphic(icon4);
        lblIconoFechas1.setText(null);
        FontIcon icon5 = new FontIcon("fa-bug");
        icon5.getStyleClass().add("totalsIcon");
        lblIconoTotalMultas.setGraphic(icon5);
        lblIconoTotalMultas.setText(null);
        FontIcon icon6 = new FontIcon("fa-balance-scale");
        icon6.getStyleClass().add("totalsIcon");
        lblIconoTotalAlicuotas.setGraphic(icon6);
        lblIconoTotalAlicuotas.setText(null);
        FontIcon icon7 = new FontIcon("fa-birthday-cake");
        icon7.getStyleClass().add("totalsIcon");
        lblIconoTotalReservas.setGraphic(icon7);
        lblIconoTotalReservas.setText(null);
        FontIcon icon8 = new FontIcon("fa-usd");
        icon8.getStyleClass().add("totalsIcon");
        lblIconoTotalGeneral.setGraphic(icon8);
        lblIconoTotalGeneral.setText(null);
        FontIcon icon9 = new FontIcon("fa-file-excel-o");
        icon9.getStyleClass().add("optionsIcon");
        lblIconoGuardar.setGraphic(icon9);
        lblIconoGuardar.setText(null);
        FontIcon icon10 = new FontIcon("fa-history");
        icon10.getStyleClass().add("optionsIcon");
        lblIconoLimpiar.setGraphic(icon10);
        lblIconoLimpiar.setText(null);
        FontIcon icon11 = new FontIcon("fa-search");
        icon11.getStyleClass().add("optionsIcon");
        icon11.getStyleClass().add("optionsIcon--white");
        btnBuscar.setGraphic(icon11);
    }

    private void mostrarReporte(ReportePagosDTO reporte) {
        tbReportePagos.getItems().clear();
        if (reporte.detalles != null) {
            tbReportePagos.getItems().setAll(reporte.detalles);
        }
        txtTotalMultas.setText(formatoMoneda(reporte.totalMultas));
        txtTotalAlicuotas.setText(formatoMoneda(reporte.totalAlicuotas));
        txtTotalReservas.setText(formatoMoneda(reporte.totalReservas));
        txtTotalGeneral.setText(formatoMoneda(reporte.totalGeneral));
    }

    private static boolean esResidente(Usuario usuario) {
        return usuario != null
                && usuario.getCuenta() != null
                && usuario.getCuenta().getRoles() != null
                && usuario.getCuenta().getRoles().stream()
                .anyMatch(rol -> rol.getNombre() != null && rol.getNombre().name().equals("RESIDENTE"));
    }

    private DatePicker obtenerDatePickerFin() {
        if (dpFechaFin == null) {
            return null;
        }
        for (Node node : dpFechaFin.getChildren()) {
            if (node instanceof DatePicker) {
                DatePicker picker = (DatePicker) node;
                if (picker != dpFechaInicio) {
                    return picker;
                }
            }
        }
        return null;
    }

    private static String formatoMoneda(double valor) {
        return String.format(Locale.US, "%.2f", valor);
    }

    private static String textoSeguro(String texto) {
        return texto == null ? "" : texto;
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
