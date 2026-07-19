package fis.dsw.sgc.finanzas.controller;

import fis.dsw.sgc.core.util.NavigationUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

// Controlador de la vista Consultar deudas
public class ConsultarDeudasController {

    private static final String MSG_INICIAL =
            "Ingrese la cédula del residente y pulse Consultar.";
    private static final String PLACEHOLDER_VACIO =
            "Sin resultados. Ingrese una cédula y pulse Consultar.";
    private static final String PLACEHOLDER_SIN_DEUDAS =
            "El residente no tiene deudas pendientes.";

    @FXML private ComboBox<String> cmbRol;
    @FXML private TextField txtCedula;
    @FXML private Button btnConsultar;
    @FXML private Button btnLimpiar;
    @FXML private Label lblMensaje;
    @FXML private TableView<DeudaFila> tablaDeudas;
    @FXML private TableColumn<DeudaFila, String> colId;
    @FXML private TableColumn<DeudaFila, String> colMotivo;
    @FXML private TableColumn<DeudaFila, String> colValor;
    @FXML private TableColumn<DeudaFila, String> colFechaMax;
    @FXML private TableColumn<DeudaFila, String> colEstado;
    @FXML private TableColumn<DeudaFila, String> colDescripcion;
    @FXML private TableColumn<DeudaFila, Void> colOpciones;

    private final ObservableList<DeudaFila> filas = FXCollections.observableArrayList();
    private Label placeholderTabla;

    @FXML
    public void initialize() {
        cmbRol.setItems(FXCollections.observableArrayList("RESIDENTE", "PRESIDENTE"));
        cmbRol.getSelectionModel().select("PRESIDENTE");
        cmbRol.valueProperty().addListener((o, a, b) -> actualizarColumnaOpciones());

        colId.setCellValueFactory(new PropertyValueFactory<>("idDeuda"));
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

        configurarColumnaOpciones();
        actualizarColumnaOpciones();
        setMensaje(MSG_INICIAL, "message-info");
    }

    private void configurarColumnaOpciones() {
        colOpciones.setCellFactory(new Callback<>() {
            @Override
            public TableCell<DeudaFila, Void> call(TableColumn<DeudaFila, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Más opciones");

                    {
                        btn.getStyleClass().add("secondary-button");
                        btn.setOnAction(e -> {
                            DeudaFila fila = getTableView().getItems().get(getIndex());
                            abrirDetalle(e, fila);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || !esPresidente()) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        });
    }

    private void actualizarColumnaOpciones() {
        colOpciones.setVisible(esPresidente());
        tablaDeudas.refresh();
    }

    private boolean esPresidente() {
        return cmbRol.getValue() != null && cmbRol.getValue().equalsIgnoreCase("PRESIDENTE");
    }

    private void abrirDetalle(ActionEvent event, DeudaFila fila) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/finanzas/fxml/detalleDeuda.fxml"));
            Parent root = loader.load();
            DetalleDeudaController ctrl = loader.getController();
            ctrl.setDeuda(fila, () -> {
                if ("ELIMINADA".equals(ctrl.getResultadoAccion())) {
                    filas.remove(fila);
                } else if (ctrl.getEstadoActualizado() != null) {
                    fila.setEstado(ctrl.getEstadoActualizado());
                }
                if (ctrl.getFechaActualizada() != null) {
                    fila.setFechaMaximaPago(ctrl.getFechaActualizada());
                }
                tablaDeudas.refresh();
            });
            NavigationUtil.openNewWindow(event, root, "Detalle de deuda");
        } catch (Exception ex) {
            setMensaje("No se pudo abrir el detalle de la deuda.", "message-error");
        }
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
        if (!cedula.matches("\\d{10}")) {
            setMensaje("Cédula inválida. Ingrese 10 dígitos.", "message-error");
            return;
        }

        if ("0000000000".equals(cedula)) {
            setPlaceholder(PLACEHOLDER_SIN_DEUDAS);
            setMensaje("El residente no tiene deudas.", "message-info");
            return;
        }

        if ("9999999999".equals(cedula)) {
            setMensaje("No existe un cliente con el número de cédula proporcionado.", "message-error");
            return;
        }

        // Datos de ejemplo hasta conectar con el servicio
        filas.addAll(
                new DeudaFila("DEU-001", "ALICUOTA", "$45.00", "2026-07-31", "PENDIENTE",
                        "Alícuota del mes de julio"),
                new DeudaFila("DEU-002", "MULTA", "$20.00", "2026-07-20", "MORA",
                        "Retraso en el pago de una reserva"),
                new DeudaFila("DEU-003", "RESERVA", "$15.00", "2026-08-05", "EN PROCESO",
                        "Uso del salón comunal")
        );
        setMensaje("Deudas del residente.", "message-success");
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
        private final String idDeuda;
        private final String motivo;
        private final String valor;
        private String fechaMaximaPago;
        private String estado;
        private final String descripcion;

        public DeudaFila(String idDeuda, String motivo, String valor, String fechaMaximaPago,
                         String estado, String descripcion) {
            this.idDeuda = idDeuda;
            this.motivo = motivo;
            this.valor = valor;
            this.fechaMaximaPago = fechaMaximaPago;
            this.estado = estado;
            this.descripcion = descripcion;
        }

        public String getIdDeuda() {
            return idDeuda;
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

        public void setFechaMaximaPago(String fechaMaximaPago) {
            this.fechaMaximaPago = fechaMaximaPago;
        }

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }
}
