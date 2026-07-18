package fis.dsw.sgc.finanzas.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

// Controlador de la vista Consultar deudas
public class ConsultarDeudasController {

    private static final String MSG_INICIAL =
            "Ingrese la cédula del residente y pulse Consultar.";
    private static final String PLACEHOLDER_VACIO =
            "Sin resultados. Ingrese una cédula y pulse Consultar.";
    private static final String PLACEHOLDER_SIN_DEUDAS =
            "El residente no tiene deudas pendientes.";

    @FXML private TextField txtCedula;
    @FXML private Button btnConsultar;
    @FXML private Button btnLimpiar;
    @FXML private Label lblMensaje;
    @FXML private TableView<DeudaFila> tablaDeudas;
    @FXML private TableColumn<DeudaFila, String> colMotivo;
    @FXML private TableColumn<DeudaFila, String> colValor;
    @FXML private TableColumn<DeudaFila, String> colFechaMax;
    @FXML private TableColumn<DeudaFila, String> colEstado;
    @FXML private TableColumn<DeudaFila, String> colDescripcion;

    private final ObservableList<DeudaFila> filas = FXCollections.observableArrayList();
    private Label placeholderTabla;

    @FXML
    public void initialize() {
        colMotivo.setCellValueFactory(new PropertyValueFactory<>("motivo"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colFechaMax.setCellValueFactory(new PropertyValueFactory<>("fechaMaximaPago"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        tablaDeudas.setItems(filas);
        tablaDeudas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        placeholderTabla = new Label(PLACEHOLDER_VACIO);
        placeholderTabla.getStyleClass().add("module-subtitle");
        placeholderTabla.setWrapText(true);
        placeholderTabla.setAlignment(Pos.CENTER);
        placeholderTabla.setMaxWidth(420);
        tablaDeudas.setPlaceholder(placeholderTabla);

        setMensaje(MSG_INICIAL, "message-info");
    }

    @FXML
    void consultar(ActionEvent event) {
        String cedula = txtCedula.getText() == null ? "" : txtCedula.getText().trim();
        filas.clear();
        setPlaceholder(PLACEHOLDER_VACIO);

        if (cedula.isEmpty()) {
            setMensaje("Debe ingresar el número de cédula del residente.", "message-error");
            return;
        }
        if (!cedula.matches("\\d{5,13}")) {
            setMensaje("Cédula inválida. Ingrese solo dígitos (5 a 13).", "message-error");
            return;
        }

        if ("0000000000".equals(cedula)) {
            setPlaceholder(PLACEHOLDER_SIN_DEUDAS);
            setMensaje("El residente no tiene deudas.", "message-info");
            return;
        }

        if ("9999999999".equals(cedula)) {
            setMensaje("No existe un residente con el número de cédula proporcionado.", "message-error");
            return;
        }

        // Datos de ejemplo hasta conectar con el servicio
        filas.addAll(
                new DeudaFila("ALÍCUOTA", "$45.00", "2026-07-31", "PENDIENTE", "Alícuota del mes de julio"),
                new DeudaFila("MULTA", "$20.00", "2026-07-20", "MORA", "Retraso en el pago de una reserva"),
                new DeudaFila("RESERVA", "$15.00", "2026-08-05", "EN PROCESO", "Uso del salón comunal")
        );
        setMensaje("Se encontraron " + filas.size() + " deuda(s) para la cédula " + cedula + ".",
                "message-success");
    }

    @FXML
    void limpiar(ActionEvent event) {
        txtCedula.clear();
        filas.clear();
        setPlaceholder(PLACEHOLDER_VACIO);
        setMensaje(MSG_INICIAL, "message-info");
    }

    private void setPlaceholder(String texto) {
        if (placeholderTabla != null) {
            placeholderTabla.setText(texto);
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

    // Fila de la tabla (temporal, solo para la vista)
    public static class DeudaFila {
        private final String motivo;
        private final String valor;
        private final String fechaMaximaPago;
        private final String estado;
        private final String descripcion;

        public DeudaFila(String motivo, String valor, String fechaMaximaPago, String estado, String descripcion) {
            this.motivo = motivo;
            this.valor = valor;
            this.fechaMaximaPago = fechaMaximaPago;
            this.estado = estado;
            this.descripcion = descripcion;
        }

        public String getMotivo() {
            return motivo;
        }

        public String getValor() {
            return valor;
        }

        public String getFechaMaximaPago() {
            return fechaMaximaPago;
        }

        public String getEstado() {
            return estado;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }
}
