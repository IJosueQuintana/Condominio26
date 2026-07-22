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
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.LocalDate;
import java.util.Locale;

public class ConsultarRendicionCuentasController {

    private static final String MSG_INICIAL = "Seleccione el periodo de inicio y fin y pulse Consultar.";
    private static final String PLACEHOLDER_VACIO = "Sin resultados. Seleccione un rango de fechas.";

    private final IReportesService reportesService;

    public ConsultarRendicionCuentasController() {
        this(new ReportesServiceImpl(new ReportesDAOImpl(), new GestionUsuariosServiceImpl()));
    }

    public ConsultarRendicionCuentasController(IReportesService reportesService) {
        this.reportesService = reportesService;
    }

    @FXML private DatePicker dpInicio;
    @FXML private DatePicker dpFin;
    @FXML private Label lblMensaje;
    @FXML private TableView<RegistroFila> tablaResultados;
    @FXML private TableColumn<RegistroFila, String> colMotivo;
    @FXML private TableColumn<RegistroFila, String> colValor;
    @FXML private TableColumn<RegistroFila, String> colFecha;
    @FXML private TableColumn<RegistroFila, String> colEstado;
    @FXML private TableColumn<RegistroFila, String> colDescripcion;
    @FXML private HBox boxImprimir;
    @FXML private Label lblIconoImprimir;
    @FXML private Button btnImprimir;

    private final ObservableList<RegistroFila> filas = FXCollections.observableArrayList();
    private Label placeholderTabla;

    @FXML
    public void initialize() {
        colMotivo.setCellValueFactory(new PropertyValueFactory<>("motivo"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        tablaResultados.setItems(filas);
        tablaResultados.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        placeholderTabla = new Label(PLACEHOLDER_VACIO);
        placeholderTabla.getStyleClass().add("module-subtitle");
        placeholderTabla.setWrapText(true);
        placeholderTabla.setAlignment(Pos.CENTER);
        tablaResultados.setPlaceholder(placeholderTabla);

        LocalDate hoy = LocalDate.now();
        dpInicio.setValue(hoy.withDayOfMonth(1));
        dpFin.setValue(hoy);

        setMensaje(MSG_INICIAL, "message-info");

        FontIcon iconPrint = new FontIcon("fa-file-excel-o");
        iconPrint.getStyleClass().add("optionsIcon");
        lblIconoImprimir.setGraphic(iconPrint);
        lblIconoImprimir.setText(null);
    }

    @FXML
    void consultar(ActionEvent event) {
        LocalDate ini = dpInicio.getValue();
        LocalDate fin = dpFin.getValue();
        filas.clear();

        if (ini == null || fin == null) {
            setMensaje("Fecha de inicio y fecha de fin son obligatorias.", "message-error");
            return;
        }
        if (fin.isBefore(ini)) {
            setMensaje("La fecha fin debe ser mayor o igual a la fecha de inicio.", "message-error");
            return;
        }

        try {
            ReporteRendicionDTO reporte = reportesService.consultarReporteRendicionCuentas(ini, fin);
            cargarReporte(reporte);
            setMensaje("Consulta generada exitosamente. Se encontraron " + filas.size() + " registros.", "message-success");
            boxImprimir.setVisible(true);
            boxImprimir.setManaged(true);
        } catch (RuntimeException ex) {
            setMensaje(ex.getMessage(), "message-error");
            ocultarBotonImprimir();
        }
    }

    @FXML
    void limpiar(ActionEvent event) {
        LocalDate hoy = LocalDate.now();
        dpInicio.setValue(hoy.withDayOfMonth(1));
        dpFin.setValue(hoy);
        filas.clear();
        setMensaje(MSG_INICIAL, "message-info");
        ocultarBotonImprimir();
    }

    @FXML
    void imprimirReporte(ActionEvent event) {
        setMensaje("Reporte listo para descarga.", "message-success");
    }

    private void cargarReporte(ReporteRendicionDTO reporte) {
        String periodo = reporte.fechaInicio + " a " + reporte.fechaFin;
        filas.addAll(
                new RegistroFila("SERVICIOS BASICOS", f(reporte.totalServiciosBasicos), periodo, "GENERADO", "Total de gastos en servicios básicos"),
                new RegistroFila("SUELDOS", f(reporte.totalSueldosGastos), periodo, "GENERADO", "Total de gastos en sueldos"),
                new RegistroFila("OTROS GASTOS", f(reporte.totalOtrosGastos), periodo, "GENERADO", "Total de otros gastos"),
                new RegistroFila("TOTAL GASTOS", f(reporte.totalGastosGeneral), periodo, "GENERADO", "Total general de gastos"),
                new RegistroFila("MULTAS", f(reporte.totalMultas), periodo, "GENERADO", "Total de ingresos por multas"),
                new RegistroFila("ALICUOTAS", f(reporte.totalAlicuotas), periodo, "GENERADO", "Total de ingresos por alícuotas"),
                new RegistroFila("RESERVAS", f(reporte.totalReservas), periodo, "GENERADO", "Total de ingresos por reservas"),
                new RegistroFila("TOTAL INGRESOS", f(reporte.totalIngresosGeneral), periodo, "GENERADO", "Total general de ingresos"),
                new RegistroFila("BALANCE NETO", f(reporte.balanceNeto), periodo, "GENERADO", reporte.observaciones == null ? "" : reporte.observaciones)
        );
    }

    private void ocultarBotonImprimir() {
        boxImprimir.setVisible(false);
        boxImprimir.setManaged(false);
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

    public static class RegistroFila {
        private final String motivo;
        private final String valor;
        private final String fecha;
        private final String estado;
        private final String descripcion;

        public RegistroFila(String motivo, String valor, String fecha, String estado, String descripcion) {
            this.motivo = motivo;
            this.valor = valor;
            this.fecha = fecha;
            this.estado = estado;
            this.descripcion = descripcion;
        }

        public String getMotivo() {
            return motivo;
        }

        public String getValor() {
            return valor;
        }

        public String getFecha() {
            return fecha;
        }

        public String getEstado() {
            return estado;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }
}
