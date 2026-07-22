package fis.dsw.sgc.finanzas.controller;

import fis.dsw.sgc.finanzas.dto.DetalleGastoDTO;
import fis.dsw.sgc.finanzas.dto.ReporteGastosDTO;
import fis.dsw.sgc.finanzas.service.IReportesService;
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
import java.util.ResourceBundle;

public class GenerarReporteGastosController implements Initializable {

    // 5. Inyección de dependencias
    private final IReportesService reportesService;

    public GenerarReporteGastosController(IReportesService reportesService) {
        this.reportesService = reportesService;
    }

    @FXML private Button btnBuscar;
    @FXML private Button btnGuardar;
    @FXML private Button btnLimpiar;

    // 1. Parametrizando correctamente las columnas con tu DetalleGastoDTO
    @FXML private TableColumn<DetalleGastoDTO, String> colDescripcion;
    @FXML private TableColumn<DetalleGastoDTO, String> colMotivo;
    @FXML private TableColumn<DetalleGastoDTO, Double> colValor;

    @FXML private DatePicker dpFechaFin;
    @FXML private DatePicker dpFechaInicio;

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
            mostrarAlerta("Error de validación", "Por favor, seleccione ambas fechas.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // 4. Llamada al Service utilizando el nombre del caso de uso
            ReporteGastosDTO reporte = reportesService.generarReporteGastos(fechaInicio, fechaFin);

            // 1. Llenar la tabla con la lista de Detalles que envía el Service
            tbReporteGastos.getItems().setAll(reporte.detalles);

            // 2. Colocar la información en los txt respectivos
            txtTotalAgua.setText(String.format("%.2f", reporte.totalAgua));
            txtTotalLuz.setText(String.format("%.2f", reporte.totalLuz));
            txtTotalTelefono.setText(String.format("%.2f", reporte.totalTelefono));
            txtTotalInternet.setText(String.format("%.2f", reporte.totalInternet));
            txtTotalSueldos.setText(String.format("%.2f", reporte.totalSueldos));
            txtTotalOtros.setText(String.format("%.2f", reporte.totalOtros));

            // Calculo extra para el total de servicios basicos agrupado
            double totalServiciosBasicos = reporte.totalAgua + reporte.totalLuz + reporte.totalTelefono + reporte.totalInternet;
            txtTotalServiciosBasicos.setText(String.format("%.2f", totalServiciosBasicos));

            txtTotalGastos.setText(String.format("%.2f", reporte.totalGeneral));

            // Cumpliendo con el caso de uso: emitir mensaje
            mostrarAlerta("Éxito", "Reporte generado correctamente", Alert.AlertType.INFORMATION);

        } catch (Exception e) { // Atrapa la FechasInvalidasException o cualquier otra del Service
            mostrarAlerta("Error en la consulta", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void descargarReporte(ActionEvent event) {
        // 7. Descargar reportes en dos archivos CSV básicos
        if (tbReporteGastos.getItems().isEmpty()) {
            mostrarAlerta("Sin datos", "No hay un reporte generado para descargar.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // CSV Detalles
            File archivoDetalles = new File("reporte_gastos_detalles.csv");
            try (PrintWriter writer = new PrintWriter(archivoDetalles)) {
                writer.println("Motivo,Descripcion,Valor");
                for (DetalleGastoDTO detalle : tbReporteGastos.getItems()) {
                    writer.printf("%s,%s,%.2f\n", detalle.motivo, detalle.descripcion, detalle.valor);
                }
            }

            // CSV Totales
            File archivoTotales = new File("reporte_gastos_totales.csv");
            try (PrintWriter writer = new PrintWriter(archivoTotales)) {
                writer.println("Categoria,Total");
                writer.println("Agua," + txtTotalAgua.getText());
                writer.println("Luz," + txtTotalLuz.getText());
                writer.println("Telefono," + txtTotalTelefono.getText());
                writer.println("Internet," + txtTotalInternet.getText());
                writer.println("Servicios Basicos," + txtTotalServiciosBasicos.getText());
                writer.println("Sueldos," + txtTotalSueldos.getText());
                writer.println("Otros," + txtTotalOtros.getText());
                writer.println("Total General," + txtTotalGastos.getText());
            }

            mostrarAlerta("Descarga Exitosa", "Archivos CSV generados correctamente en la raíz del proyecto.", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            mostrarAlerta("Error de exportación", "Hubo un problema al crear los archivos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void limpiarReporte(ActionEvent event) {
        // 8. Limpia la tabla y deja vacíos los textFields y DatePickers
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
        // Mapeo seguro de columnas hacia las propiedades del DTO (Evita problemas al no tener getters explícitos)
        colMotivo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().motivo));
        colDescripcion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().descripcion));
        colValor.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().valor));

        // 6. Carga de los iconos que enviaste
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
    }

    // Método auxiliar para no repetir código de generación de alertas
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}