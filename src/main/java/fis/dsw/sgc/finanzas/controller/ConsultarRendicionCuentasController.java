package fis.dsw.sgc.finanzas.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import java.time.LocalDate;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

public class ConsultarRendicionCuentasController {

    private static final String MSG_INICIAL = "Seleccione el periodo de inicio y fin y pulse Consultar.";
    private static final String PLACEHOLDER_VACIO = "Sin resultados. Seleccione un rango de fechas.";

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
        // Inicialización de columnas de la tabla
        colMotivo.setCellValueFactory(new PropertyValueFactory<>("motivo"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        tablaResultados.setItems(filas);
        tablaResultados.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        // Placeholder para tabla vacía
        placeholderTabla = new Label(PLACEHOLDER_VACIO);
        placeholderTabla.getStyleClass().add("module-subtitle");
        placeholderTabla.setWrapText(true);
        placeholderTabla.setAlignment(Pos.CENTER);
        tablaResultados.setPlaceholder(placeholderTabla);

        // Inicialización de fechas por defecto
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

        // Validaciones de fecha
        if (ini == null || fin == null) {
            setMensaje("Fecha de inicio y fecha de fin son obligatorias.", "message-error");
            return;
        }
        if (fin.isBefore(ini)) {
            setMensaje("La fecha fin debe ser mayor o igual a la fecha de inicio.", "message-error");
            return;
        }

        // --- VALORES QUEMADOS SEGÚN SOLICITUD ---
        filas.addAll(
                new RegistroFila("INGRESO", "$1800.00", "2026-07-05", "PAGADO", "Recaudación de alícuotas del mes"),
                new RegistroFila("GASTO", "$320.00", "2026-07-10", "PROCESADO", "Pago de servicio de agua potable"),
                new RegistroFila("INGRESO", "$120.00", "2026-07-12", "PAGADO", "Multas por retraso en asamblea"),
                new RegistroFila("GASTO", "$410.50", "2026-07-15", "PROCESADO", "Pago de servicio de luz eléctrica"),
                new RegistroFila("INGRESO", "$350.00", "2026-07-16", "PAGADO", "Reserva de áreas comunales (salón)")
        );

        setMensaje("Consulta generada exitosamente. Se encontraron " + filas.size() + " registros.", "message-success");

        boxImprimir.setVisible(true);
        boxImprimir.setManaged(true);

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
        setMensaje("Imprimiendo reporte...", "message-success");

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

    // Clase interna para manejar las filas de la tabla
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

        public String getMotivo() { return motivo; }
        public String getValor() { return valor; }
        public String getFecha() { return fecha; }
        public String getEstado() { return estado; }
        public String getDescripcion() { return descripcion; }
    }
}