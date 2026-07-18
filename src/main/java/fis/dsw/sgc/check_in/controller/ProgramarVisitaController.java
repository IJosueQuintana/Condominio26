package fis.dsw.sgc.check_in.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProgramarVisitaController {

    @FXML private TextField txtNombreVisitante;
    @FXML private TextField txtIdentificacionVisitante;
    @FXML private TextField txtPlaca;
    @FXML private TextField txtUnidad;
    @FXML private TextField txtMotivo;
    @FXML private DatePicker dpFecha;
    @FXML private TextField txtHora;
    @FXML private Label lblMensaje;

    @FXML private TableView<VisitaProgramadaFila> tablaVisitas;
    @FXML private TableColumn<VisitaProgramadaFila, String> colVisitante;
    @FXML private TableColumn<VisitaProgramadaFila, String> colFecha;
    @FXML private TableColumn<VisitaProgramadaFila, String> colHora;
    @FXML private TableColumn<VisitaProgramadaFila, String> colMotivo;
    @FXML private TableColumn<VisitaProgramadaFila, String> colEstado;
    @FXML private TableColumn<VisitaProgramadaFila, Void> colOpciones;

    private final ObservableList<VisitaProgramadaFila> visitas = FXCollections.observableArrayList();
    private static final DateTimeFormatter FECHA_FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        colVisitante.setCellValueFactory(new PropertyValueFactory<>("visitante"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colMotivo.setCellValueFactory(new PropertyValueFactory<>("motivo"));

        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colEstado.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String estado, boolean empty) {
                super.updateItem(estado, empty);
                if (empty || estado == null) {
                    setGraphic(null);
                    return;
                }
                Label badge = new Label(estado);
                badge.getStyleClass().addAll("badge-estado",
                        estado.equals("Programada") ? "badge-programada" : "badge-cancelada");
                setGraphic(badge);
            }
        });

        colOpciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnCancelar = new Button("Cancelar");
            {
                btnCancelar.getStyleClass().addAll("btn-accion-tabla", "btn-accion-cancelar");
                btnCancelar.setOnAction(e -> {
                    VisitaProgramadaFila fila = getTableView().getItems().get(getIndex());
                    fila.setEstado("Cancelada");
                    getTableView().refresh();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }
                VisitaProgramadaFila fila = getTableView().getItems().get(getIndex());
                setGraphic("Cancelada".equals(fila.getEstado()) ? null : btnCancelar);
            }
        });

        tablaVisitas.setItems(visitas);
    }

    @FXML
    void programarVisita(ActionEvent event) {
        if (txtNombreVisitante.getText().isBlank() || txtUnidad.getText().isBlank()
                || txtMotivo.getText().isBlank() || dpFecha.getValue() == null || txtHora.getText().isBlank()) {
            mostrarError("Complete visitante, unidad, motivo, fecha y hora de la visita.");
            return;
        }

        LocalDate fecha = dpFecha.getValue();
        if (fecha.isBefore(LocalDate.now())) {
            mostrarError("La fecha seleccionada no es válida para programar la visita.");
            return;
        }

        visitas.add(0, new VisitaProgramadaFila(
                txtNombreVisitante.getText().trim(),
                fecha.format(FECHA_FORMATO),
                txtHora.getText().trim(),
                txtMotivo.getText().trim(),
                "Programada"
        ));

        mostrarExito("Visita programada correctamente. El personal de seguridad podrá verificarla al ingreso.");
        limpiarFormulario(null);
    }

    @FXML
    void limpiarFormulario(ActionEvent event) {
        txtNombreVisitante.clear();
        txtIdentificacionVisitante.clear();
        txtPlaca.clear();
        txtUnidad.clear();
        txtMotivo.clear();
        dpFecha.setValue(null);
        txtHora.clear();
    }

    private void mostrarExito(String mensaje) {
        lblMensaje.setText(mensaje);
        lblMensaje.getStyleClass().setAll("message-label", "message-success");
    }

    private void mostrarError(String mensaje) {
        lblMensaje.setText(mensaje);
        lblMensaje.getStyleClass().setAll("message-label", "message-error");
    }

    public static class VisitaProgramadaFila {
        private final String visitante;
        private final String fecha;
        private final String hora;
        private final String motivo;
        private String estado;

        public VisitaProgramadaFila(String visitante, String fecha, String hora, String motivo, String estado) {
            this.visitante = visitante;
            this.fecha = fecha;
            this.hora = hora;
            this.motivo = motivo;
            this.estado = estado;
        }

        public String getVisitante() { return visitante; }
        public String getFecha() { return fecha; }
        public String getHora() { return hora; }
        public String getMotivo() { return motivo; }
        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
    }
}
