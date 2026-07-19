package fis.dsw.sgc.finanzas.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

// Controlador de la vista Generar rendición de cuentas
public class GenerarRendicionCuentasController {

    @FXML private DatePicker dpInicio;
    @FXML private DatePicker dpFin;
    @FXML private TextArea txtObservaciones;
    @FXML private Label lblMensaje;
    @FXML private TableView<FilaMonto> tablaGastos;
    @FXML private TableColumn<FilaMonto, String> colGastoCat;
    @FXML private TableColumn<FilaMonto, String> colGastoMonto;
    @FXML private TableView<FilaMonto> tablaIngresos;
    @FXML private TableColumn<FilaMonto, String> colIngCat;
    @FXML private TableColumn<FilaMonto, String> colIngMonto;
    @FXML private Label lblTotales;

    private final ObservableList<FilaMonto> gastos = FXCollections.observableArrayList();
    private final ObservableList<FilaMonto> ingresos = FXCollections.observableArrayList();
    private final Set<String> periodosGenerados = new HashSet<>();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_LOCAL_DATE;

    @FXML
    public void initialize() {
        LocalDate hoy = LocalDate.now();
        dpInicio.setValue(hoy.withDayOfMonth(1));
        dpFin.setValue(hoy);

        colGastoCat.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colGastoMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
        colIngCat.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colIngMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
        tablaGastos.setItems(gastos);
        tablaIngresos.setItems(ingresos);
        tablaGastos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tablaIngresos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        setMensaje("Seleccione el periodo, escriba observaciones y pulse Generar reporte.", "message-info");
    }

    @FXML
    void generar(ActionEvent event) {
        LocalDate ini = dpInicio.getValue();
        LocalDate fin = dpFin.getValue();
        String obs = txtObservaciones.getText() == null ? "" : txtObservaciones.getText().trim();

        if (ini == null || fin == null) {
            setMensaje("La fecha de inicio y la fecha de fin son obligatorias.", "message-error");
            return;
        }
        if (fin.isBefore(ini)) {
            setMensaje("La fecha de fin debe ser mayor o igual a la fecha de inicio para la consulta.",
                    "message-error");
            return;
        }

        // Demo de periodo ya generado (enero 2026)
        String clave = ini.format(FMT) + "|" + fin.format(FMT);
        if (periodosGenerados.contains(clave)
                || (ini.equals(LocalDate.of(2026, 1, 1)) && fin.equals(LocalDate.of(2026, 1, 31)))) {
            setMensaje(
                    "Ya se generó un reporte de rendición de cuentas para las fechas especificadas; puede consultarlo en cualquier momento.",
                    "message-error");
            return;
        }

        if (!obs.isEmpty() && !obs.matches("[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚüÜ .,;:!?\\-\\n\\r]+")) {
            setMensaje("Formato de observaciones no válido.", "message-error");
            return;
        }

        double agua = 320.00;
        double luz = 410.50;
        double telefono = 80.00;
        double internet = 95.00;
        double sueldos = 1500.00;
        double otros = 200.00;
        double servBasicos = agua + luz + telefono + internet;
        double totalGastos = servBasicos + sueldos + otros;

        double multas = 120.00;
        double alicuotas = 1800.00;
        double reservas = 350.00;
        double totalIngresos = multas + alicuotas + reservas;
        double balance = totalIngresos - totalGastos;

        gastos.setAll(
                new FilaMonto("SERVICIOS BÁSICOS (Agua)", "$" + f(agua)),
                new FilaMonto("SERVICIOS BÁSICOS (Luz)", "$" + f(luz)),
                new FilaMonto("SERVICIOS BÁSICOS (Teléfono)", "$" + f(telefono)),
                new FilaMonto("SERVICIOS BÁSICOS (Internet)", "$" + f(internet)),
                new FilaMonto("SUELDOS", "$" + f(sueldos)),
                new FilaMonto("OTROS", "$" + f(otros)),
                new FilaMonto("TOTAL GASTOS", "$" + f(totalGastos))
        );
        ingresos.setAll(
                new FilaMonto("MULTAS", "$" + f(multas)),
                new FilaMonto("ALÍCUOTAS", "$" + f(alicuotas)),
                new FilaMonto("RESERVAS", "$" + f(reservas)),
                new FilaMonto("TOTAL INGRESOS", "$" + f(totalIngresos))
        );

        String obsLine = obs.isEmpty() ? "(sin observaciones)" : obs;
        lblTotales.setText(
                "Periodo: " + ini.format(FMT) + " a " + fin.format(FMT)
                        + " | Balance (ingresos menos gastos): $" + f(balance)
                        + " | Observaciones: " + obsLine);

        periodosGenerados.add(clave);
        setMensaje(
                "Reporte de rendición de cuentas generado exitosamente, disponible para la consulta de los residentes.",
                "message-success");
    }

    @FXML
    void limpiar(ActionEvent event) {
        LocalDate hoy = LocalDate.now();
        dpInicio.setValue(hoy.withDayOfMonth(1));
        dpFin.setValue(hoy);
        txtObservaciones.clear();
        gastos.clear();
        ingresos.clear();
        lblTotales.setText("Genere un reporte para ver totales y balance.");
        setMensaje("Formulario listo. Seleccione un nuevo periodo.", "message-info");
    }

    private void setMensaje(String texto, String estilo) {
        lblMensaje.getStyleClass().removeAll("message-info", "message-success", "message-error");
        if (!lblMensaje.getStyleClass().contains("message-label")) {
            lblMensaje.getStyleClass().add("message-label");
        }
        lblMensaje.getStyleClass().add(estilo);
        lblMensaje.setText(texto);
    }

    private static String f(double v) {
        return String.format(java.util.Locale.US, "%.2f", v);
    }

    public static class FilaMonto {
        private final String categoria;
        private final String monto;

        public FilaMonto(String categoria, String monto) {
            this.categoria = categoria;
            this.monto = monto;
        }

        public String getCategoria() {
            return categoria;
        }

        public String getMonto() {
            return monto;
        }
    }
}
