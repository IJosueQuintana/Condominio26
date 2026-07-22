package fis.dsw.sgc.finanzas.controller;

import fis.dsw.sgc.administracion.service.GestionUsuariosServiceImpl;
import fis.dsw.sgc.finanzas.dao.ReportesDAOImpl;
import fis.dsw.sgc.finanzas.dto.ReporteRendicionDTO;
import fis.dsw.sgc.finanzas.service.IReportesService;
import fis.dsw.sgc.finanzas.service.ReportesServiceImpl;
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
import java.util.Locale;

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

    private final IReportesService reportesService;
    private final ObservableList<FilaMonto> gastos = FXCollections.observableArrayList();
    private final ObservableList<FilaMonto> ingresos = FXCollections.observableArrayList();

    public GenerarRendicionCuentasController() {
        this(new ReportesServiceImpl(new ReportesDAOImpl(), new GestionUsuariosServiceImpl()));
    }

    public GenerarRendicionCuentasController(IReportesService reportesService) {
        this.reportesService = reportesService;
    }

    @FXML
    public void initialize() {
        ponerFechasPorDefecto();

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
        LocalDate hoy = LocalDate.now();

        if (ini == null || fin == null) {
            setMensaje("La fecha de inicio y la fecha de fin son obligatorias.", "message-error");
            return;
        }
        if (!ini.isBefore(hoy)) {
            setMensaje("La fecha de inicio tiene que ser menor que la fecha actual.", "message-error");
            return;
        }
        if (!fin.isAfter(ini)) {
            setMensaje("La fecha de fin tiene que ser mayor que la fecha de inicio.", "message-error");
            return;
        }
        if (fin.isAfter(hoy)) {
            setMensaje("La fecha de fin tiene que ser menor o igual a la fecha actual.", "message-error");
            return;
        }
        if (obs.isEmpty()) {
            setMensaje("Las observaciones son obligatorias.", "message-error");
            return;
        }
        if (!obs.matches("[a-zA-Z0-9 áéíóúÁÉÍÓÚñÑ]+")) {
            setMensaje("Formato de observaciones no válido.", "message-error");
            return;
        }

        try {
            ReporteRendicionDTO reporte = reportesService.generarReporteRendicionCuentas(ini, fin, obs);
            mostrarReporte(reporte);
            setMensaje(
                    "Reporte de rendición de cuentas generado exitosamente, disponible para la consulta de los residentes.",
                    "message-success");
        } catch (RuntimeException ex) {
            setMensaje(ex.getMessage(), "message-error");
        }
    }

    @FXML
    void limpiar(ActionEvent event) {
        ponerFechasPorDefecto();
        txtObservaciones.clear();
        gastos.clear();
        ingresos.clear();
        lblTotales.setText("Genere un reporte para ver totales y balance.");
        setMensaje("Formulario listo. Seleccione un nuevo periodo.", "message-info");
    }

    private void mostrarReporte(ReporteRendicionDTO reporte) {
        gastos.setAll(
                new FilaMonto("SERVICIOS BASICOS", f(reporte.totalServiciosBasicos)),
                new FilaMonto("SUELDOS", f(reporte.totalSueldosGastos)),
                new FilaMonto("OTROS", f(reporte.totalOtrosGastos)),
                new FilaMonto("TOTAL GASTOS", f(reporte.totalGastosGeneral))
        );
        ingresos.setAll(
                new FilaMonto("MULTAS", f(reporte.totalMultas)),
                new FilaMonto("ALICUOTAS", f(reporte.totalAlicuotas)),
                new FilaMonto("RESERVAS", f(reporte.totalReservas)),
                new FilaMonto("TOTAL INGRESOS", f(reporte.totalIngresosGeneral))
        );

        String obs = reporte.observaciones == null || reporte.observaciones.isBlank()
                ? "sin observaciones"
                : reporte.observaciones;
        lblTotales.setText(
                "Periodo: " + reporte.fechaInicio + " a " + reporte.fechaFin
                        + " | Balance ingresos menos gastos: " + f(reporte.balanceNeto)
                        + " | Observaciones: " + obs);
    }

    private void ponerFechasPorDefecto() {
        LocalDate hoy = LocalDate.now();
        LocalDate inicio = hoy.withDayOfMonth(1);
        if (!inicio.isBefore(hoy)) {
            inicio = hoy.minusMonths(1).withDayOfMonth(1);
        }
        dpInicio.setValue(inicio);
        dpFin.setValue(hoy);
    }

    private void setMensaje(String texto, String estilo) {
        lblMensaje.getStyleClass().removeAll("message-info", "message-success", "message-error");
        if (!lblMensaje.getStyleClass().contains("message-label")) {
            lblMensaje.getStyleClass().add("message-label");
        }
        lblMensaje.getStyleClass().add(estilo);
        lblMensaje.setText(texto);
    }

    private static String f(double valor) {
        return String.format(Locale.US, "$%.2f", valor);
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
