package fis.dsw.sgc.finanzas.controller;

import fis.dsw.sgc.finanzas.service.IReportesService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GenerarCertificadoNoDeudorController {

    // 5. Inyección de dependencias
    private final IReportesService reportesService;

    public GenerarCertificadoNoDeudorController(IReportesService reportesService) {
        this.reportesService = reportesService;
    }

    // 1. Opciones de interfaz mapeadas desde la vista FXML
    @FXML private DatePicker dpInicio;
    @FXML private DatePicker dpFin;
    @FXML private TextArea txtObservaciones;
    @FXML private Label lblMensaje;
    @FXML private Label lblResumen;

    @FXML private HBox boxImprimir;
    @FXML private Label lblIconoImprimir;
    @FXML private Button btnImprimir;

    private static final DateTimeFormatter FMT_DDMMYYYY = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // 6. Mantiene el initialize cargando iconos y estado inicial
    @FXML
    public void initialize() {
        LocalDate hoy = LocalDate.now();
        dpInicio.setValue(hoy.withDayOfMonth(1));
        dpFin.setValue(hoy);
        dpInicio.setPromptText("Fecha de inicio");
        dpFin.setPromptText("Fecha de fin");
        setMensaje("Seleccione el periodo y pulse Generar certificado.", "message-info");

        FontIcon iconPrint = new FontIcon("fa-file-excel-o");
        iconPrint.getStyleClass().add("optionsIcon");
        lblIconoImprimir.setGraphic(iconPrint);
        lblIconoImprimir.setText(null);
    }

    // 4. Ejecución del caso de uso "generarCertificadoDeNoDeudor"
    @FXML
    void generar(ActionEvent event) {
        LocalDate fechaInicio = dpInicio.getValue();
        LocalDate fechaFin = dpFin.getValue();
        LocalDate fechaActual = LocalDate.now();

        // Validaciones del Caso de Uso según la especificación
        if (fechaInicio == null || fechaFin == null) {
            setMensaje("Fechas con formato invalido. Ambas fechas deben tener el formato \"DD/MM/YYYY\"", "message-error");
            return;
        }

        // Escenario Alterno 2: La fecha de inicio debe ser menor que la fecha actual
        if (!fechaInicio.isBefore(fechaActual)) {
            setMensaje("La fecha de inicio tiene que ser menor que la fecha actual", "message-error");
            return;
        }

        // Escenario Alterno 3: La fecha de inicio debe ser menor que la fecha de fin
        if (!fechaInicio.isBefore(fechaFin)) {
            setMensaje("La fecha de fin tiene que ser mayor que la fecha de inicio", "message-error");
            return;
        }

        // Escenario Alterno 4: La fecha de fin debe ser menor o igual a la fecha actual
        if (fechaFin.isAfter(fechaActual)) {
            setMensaje("La fecha de fin tiene que ser menor o igual a la fecha actual.", "message-error");
            return;
        }

        try {
            // Nota: En caso de no existir un DTO específico en la interfaz, el Service procesa/valida los datos.
            // Para la verificación de deudas/pagos, se consulta al servicio:
            reportesService.generarReportedePagosRealizados(fechaInicio, fechaFin);

            String strInicio = fechaInicio.format(FMT_DDMMYYYY);
            String strFin = fechaFin.format(FMT_DDMMYYYY);

            // 7. Texto formal asignado al resumen del certificado
            String textoCertificado = String.format(
                    "El sistema de gestión del condominio [Nombre del Condominio] certifica que el residente [Nombre y apellido] con cédula [0123456789] no presenta deudas pendientes entre las fechas %s y %s.",
                    strInicio,
                    strFin
            );

            // 2. Colocar datos en sus labels / txt Respectivos
            lblResumen.setText(textoCertificado);

            setMensaje("Certificado generado exitosamente", "message-success");

            if (boxImprimir != null) {
                boxImprimir.setVisible(true);
                boxImprimir.setManaged(true);
            }

        } catch (Exception e) {
            // Escenario Alternativo 5 o excepciones personalizadas producidas por el Service
            setMensaje("El Residente no se encuentra al día o ocurrió un inconveniente: " + e.getMessage(), "message-error");
        }
    }

    // 7. Generación de archivos CSV con detalles y resumen/totales
    @FXML
    void descargarReporte(ActionEvent event) {
        if (dpInicio.getValue() == null || dpFin.getValue() == null) {
            setMensaje("No se ha generado ningún certificado para descargar.", "message-error");
            return;
        }

        String strInicio = dpInicio.getValue().format(FMT_DDMMYYYY);
        String strFin = dpFin.getValue().format(FMT_DDMMYYYY);
        String obs = txtObservaciones.getText() == null ? "" : txtObservaciones.getText().trim();

        try {
            // Archivo CSV con los Detalles / Certificación
            File archivoDetalles = new File("certificado_no_deudor_detalles.csv");
            try (PrintWriter writer = new PrintWriter(archivoDetalles)) {
                writer.println("CONDOMINIO,RESIDENTE,CEDULA,FECHA_INICIO,FECHA_FIN,ESTADO");
                writer.printf("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n",
                        "Nombre del Condominio",
                        "Nombre y apellido",
                        "0123456789",
                        strInicio,
                        strFin,
                        "SIN DEUDAS PENDIENTES");
            }

            // Archivo CSV con los Totales y Observaciones
            File archivoTotales = new File("certificado_no_deudor_totales.csv");
            try (PrintWriter writer = new PrintWriter(archivoTotales)) {
                writer.println("CONCEPTO,VALOR");
                writer.println("Total Deudas Pendientes,0.00");
                writer.println("Total En Mora,0.00");
                writer.println("Observaciones,\"" + obs.replace("\"", "\"\"") + "\"");
                writer.println("Estado General,AL DIA");
            }

            setMensaje("Certificado descargado exitosamente en archivos CSV.", "message-success");

        } catch (Exception e) {
            setMensaje("Error al descargar el certificado: " + e.getMessage(), "message-error");
        }
    }

    // 8. Limpiar solo deja vacía la tabla/resumen y los textfields/fieles de entrada
    @FXML
    void limpiar(ActionEvent event) {
        dpInicio.setValue(null);
        dpFin.setValue(null);
        txtObservaciones.clear();
        lblResumen.setText("el sistema de gestión del condominio [Nombre del Condominio] certifica que el residente [Nombre y apellido] con cédula [0123456789] no presenta deudas pendientes entre las fechas [inicio] [fin].");
        setMensaje("Seleccione el periodo y pulse Generar certificado.", "message-info");
        ocultarBotonImprimir();
    }

    private void ocultarBotonImprimir() {
        if (boxImprimir != null) {
            boxImprimir.setVisible(false);
            boxImprimir.setManaged(false);
        }
    }

    private void setMensaje(String texto, String estilo) {
        lblMensaje.getStyleClass().removeAll("message-info", "message-success", "message-error");
        if (!lblMensaje.getStyleClass().contains("message-label")) {
            lblMensaje.getStyleClass().add("message-label");
        }
        lblMensaje.getStyleClass().add(estilo);
        lblMensaje.setText(texto);
    }
}