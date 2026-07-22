package fis.dsw.sgc.finanzas.controller;

import fis.dsw.sgc.administracion.service.GestionUsuariosServiceImpl;
import fis.dsw.sgc.finanzas.dao.ReportesDAOImpl;
import fis.dsw.sgc.finanzas.dto.DetalleGastoDTO;
import fis.dsw.sgc.finanzas.dto.ReporteGastosDTO;
import fis.dsw.sgc.finanzas.service.IReportesService;
import fis.dsw.sgc.finanzas.service.ReportesServiceImpl;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class GenerarReporteGastosController implements Initializable {

    private final IReportesService reportesService;

    public GenerarReporteGastosController() {
        this(new ReportesServiceImpl(new ReportesDAOImpl(), new GestionUsuariosServiceImpl()));
    }

    public GenerarReporteGastosController(IReportesService reportesService) {
        this.reportesService = reportesService;
    }

    @FXML private Button btnBuscar;
    @FXML private Button btnGuardar;
    @FXML private Button btnLimpiar;
    @FXML private TableColumn<DetalleGastoDTO, String> colMotivo;
    @FXML private TableColumn<DetalleGastoDTO, String> colDescripcion;
    @FXML private TableColumn<DetalleGastoDTO, LocalDate> colFecha;
    @FXML private TableColumn<DetalleGastoDTO, Double> colValor;
    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;
    @FXML private Label lblIconoAgua;
    @FXML private Label lblIconoFechas;
    @FXML private Label lblIconoFechas1;
    @FXML private Label lblIconoGastos;
    @FXML private Label lblIconoGuardar;
    @FXML private Label lblIconoInternet;
    @FXML private Label lblIconoLimpiar;
    @FXML private Label lblIconoLuz;
    @FXML private Label lblIconoTelefono;
    @FXML private Label lblTotalGastos;
    @FXML private Label lblTotalOtros;
    @FXML private Label lblTotalServiciosBasicos;
    @FXML private Label lblTotalSueldos;
    @FXML private TableView<DetalleGastoDTO> tbReporteGastos;
    @FXML private TextField txtTotalAgua;
    @FXML private TextField txtTotalGastos;
    @FXML private TextField txtTotalInternet;
    @FXML private TextField txtTotalLuz;
    @FXML private TextField txtTotalOtros;
    @FXML private TextField txtTotalServiciosBasicos;
    @FXML private TextField txtTotalSueldos;
    @FXML private TextField txtTotalTelefono;

    @FXML
    void consultarGastos(ActionEvent event) {
        LocalDate fechaInicio = dpFechaInicio.getValue();
        LocalDate fechaFin = dpFechaFin.getValue();

        if (fechaInicio == null || fechaFin == null) {
            mostrarAlerta("Formato de fechas incorrecto", "Ingrese la fecha de inicio y la fecha de fin.", Alert.AlertType.WARNING);
            return;
        }

        LocalDate fechaActual = LocalDate.now();

        if (!fechaInicio.isBefore(fechaActual)) {
            mostrarAlerta("Error en fechas", "La fecha de inicio tiene que ser menor que la fecha actual.", Alert.AlertType.WARNING);
            return;
        }

        if (!fechaInicio.isBefore(fechaFin)) {
            mostrarAlerta("Error en fechas", "La fecha de fin tiene que ser mayor que la fecha de inicio.", Alert.AlertType.WARNING);
            return;
        }

        if (fechaFin.isAfter(fechaActual)) {
            mostrarAlerta("Error en fechas", "La fecha de fin tiene que ser menor o igual a la fecha actual.", Alert.AlertType.WARNING);
            return;
        }

        try {
            ReporteGastosDTO reporte = reportesService.generarReporteGastos(fechaInicio, fechaFin);
            mostrarReporte(reporte);
            mostrarAlerta("Éxito", "Reporte generado correctamente.", Alert.AlertType.INFORMATION);
        } catch (RuntimeException ex) {
            mostrarAlerta("Error al generar reporte", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void descargarReporte(ActionEvent event) {
        if (tbReporteGastos.getItems().isEmpty()) {
            mostrarAlerta("Sin datos", "No existen registros en la tabla para descargar.", Alert.AlertType.WARNING);
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            File archivoDetalles = new File("reporte_gastos_detalles.csv");
            try (PrintWriter writer = new PrintWriter(archivoDetalles)) {
                writer.println("MOTIVO,DESCRIPCION,FECHA,VALOR");
                for (DetalleGastoDTO detalle : tbReporteGastos.getItems()) {
                    String fecha = detalle.fecha == null ? "" : detalle.fecha.format(formatter);
                    writer.printf(Locale.US, "%s,%s,%s,%.2f%n",
                            textoSeguro(detalle.motivo),
                            textoSeguro(detalle.descripcion),
                            fecha,
                            detalle.valor == null ? 0.0 : detalle.valor);
                }
            }

            File archivoTotales = new File("reporte_gastos_totales.csv");
            try (PrintWriter writer = new PrintWriter(archivoTotales)) {
                writer.println("CONCEPTO,VALOR");
                writer.println("Total Agua," + txtTotalAgua.getText());
                writer.println("Total Luz," + txtTotalLuz.getText());
                writer.println("Total Telefono," + txtTotalTelefono.getText());
                writer.println("Total Internet," + txtTotalInternet.getText());
                writer.println("Total Servicios Basicos," + txtTotalServiciosBasicos.getText());
                writer.println("Total Sueldos," + txtTotalSueldos.getText());
                writer.println("Total Otros," + txtTotalOtros.getText());
                writer.println("Total Gastos Generales," + txtTotalGastos.getText());
            }

            mostrarAlerta("Descarga completada", "Los archivos CSV fueron descargados con éxito.", Alert.AlertType.INFORMATION);
        } catch (Exception ex) {
            mostrarAlerta("Error de exportación", "Error al crear los archivos de descarga: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void limpiarReporte(ActionEvent event) {
        tbReporteGastos.getItems().clear();
        txtTotalAgua.clear();
        txtTotalLuz.clear();
        txtTotalTelefono.clear();
        txtTotalInternet.clear();
        txtTotalServiciosBasicos.clear();
        txtTotalSueldos.clear();
        txtTotalOtros.clear();
        txtTotalGastos.clear();
        dpFechaInicio.setValue(null);
        dpFechaFin.setValue(null);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colMotivo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().motivo));
        colDescripcion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().descripcion));
        colFecha.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().fecha));
        colValor.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().valor));

        FontIcon icon = new FontIcon("fa-external-link");
        icon.getStyleClass().add("titleIcon");
        lblIconoGastos.setGraphic(icon);
        lblIconoGastos.setText(null);

        FontIcon icon2 = new FontIcon("fa-calendar-minus-o");
        icon2.getStyleClass().add("totalsIcon");
        lblIconoFechas.setText(null);
        lblIconoFechas.setGraphic(icon2);

        FontIcon icon3 = new FontIcon("fa-calendar-plus-o");
        icon3.getStyleClass().add("totalsIcon");
        lblIconoFechas1.setText(null);
        lblIconoFechas1.setGraphic(icon3);

        FontIcon icon4 = new FontIcon("fa-phone");
        icon4.getStyleClass().add("totalsIcon");
        lblIconoTelefono.setText(null);
        lblIconoTelefono.setGraphic(icon4);

        FontIcon icon5 = new FontIcon("fa-tint");
        icon5.getStyleClass().add("totalsIcon");
        lblIconoAgua.setText(null);
        lblIconoAgua.setGraphic(icon5);

        FontIcon icon6 = new FontIcon("fa-plug");
        icon6.getStyleClass().add("totalsIcon");
        lblIconoLuz.setText(null);
        lblIconoLuz.setGraphic(icon6);

        FontIcon icon7 = new FontIcon("fa-wifi");
        icon7.getStyleClass().add("totalsIcon");
        lblIconoInternet.setText(null);
        lblIconoInternet.setGraphic(icon7);

        FontIcon icon8 = new FontIcon("fa-users");
        icon8.getStyleClass().add("totalsIcon");
        lblTotalSueldos.setText(null);
        lblTotalSueldos.setGraphic(icon8);

        FontIcon icon9 = new FontIcon("fa-wrench");
        icon9.getStyleClass().add("totalsIcon");
        lblTotalOtros.setText(null);
        lblTotalOtros.setGraphic(icon9);

        FontIcon icon10 = new FontIcon("fa-shower");
        icon10.getStyleClass().add("totalsIcon");
        lblTotalServiciosBasicos.setText(null);
        lblTotalServiciosBasicos.setGraphic(icon10);

        FontIcon icon11 = new FontIcon("fa-usd");
        icon11.getStyleClass().add("totalsIcon");
        lblTotalGastos.setText(null);
        lblTotalGastos.setGraphic(icon11);

        FontIcon icon12 = new FontIcon("fa-file-excel-o");
        icon12.getStyleClass().add("optionsIcon");
        lblIconoGuardar.setGraphic(icon12);
        lblIconoGuardar.setText(null);

        FontIcon icon13 = new FontIcon("fa-history");
        icon13.getStyleClass().add("optionsIcon");
        lblIconoLimpiar.setGraphic(icon13);
        lblIconoLimpiar.setText(null);

        FontIcon icon14 = new FontIcon("fa-search");
        icon14.getStyleClass().add("optionsIcon");
        icon14.getStyleClass().add("optionsIcon--white");
        btnBuscar.setGraphic(icon14);
    }

    private void mostrarReporte(ReporteGastosDTO reporte) {
        tbReporteGastos.getItems().clear();
        if (reporte.detalles != null) {
            tbReporteGastos.getItems().setAll(reporte.detalles);
        }
        txtTotalAgua.setText(formatoMoneda(reporte.totalAgua));
        txtTotalLuz.setText(formatoMoneda(reporte.totalLuz));
        txtTotalTelefono.setText(formatoMoneda(reporte.totalTelefono));
        txtTotalInternet.setText(formatoMoneda(reporte.totalInternet));
        txtTotalSueldos.setText(formatoMoneda(reporte.totalSueldos));
        txtTotalOtros.setText(formatoMoneda(reporte.totalOtros));
        txtTotalServiciosBasicos.setText(formatoMoneda(reporte.totalAgua + reporte.totalLuz + reporte.totalTelefono + reporte.totalInternet));
        txtTotalGastos.setText(formatoMoneda(reporte.totalGeneral));
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
