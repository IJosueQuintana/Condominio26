package fis.dsw.sgc.check_in.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class RegistrarEntradaExternaController {

    @FXML private TextField txtNombreVisitante;
    @FXML private TextField txtIdentificacionVisitante;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtDestino;
    @FXML private TextField txtMotivo;
    @FXML private CheckBox chkRequiereParqueadero;
    @FXML private VBox panelParqueadero;
    @FXML private TextField txtPlaca;
    @FXML private ChoiceBox<String> cbParqueadero;
    @FXML private Label lblMensaje;
    @FXML private Button btnRegistrar;

    @FXML private TableView<IngresoExternoFila> tablaIngresos;
    @FXML private TableColumn<IngresoExternoFila, String> colHora;
    @FXML private TableColumn<IngresoExternoFila, String> colNombre;
    @FXML private TableColumn<IngresoExternoFila, String> colDestino;
    @FXML private TableColumn<IngresoExternoFila, String> colMotivo;
    @FXML private TableColumn<IngresoExternoFila, String> colParqueadero;

    private final ObservableList<IngresoExternoFila> ingresos = FXCollections.observableArrayList();
    private static final DateTimeFormatter HORA_FORMATO = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    public void initialize() {
        cbParqueadero.setItems(FXCollections.observableArrayList("P-01", "P-02", "P-03", "P-04", "P-05"));

        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDestino.setCellValueFactory(new PropertyValueFactory<>("destino"));
        colMotivo.setCellValueFactory(new PropertyValueFactory<>("motivo"));
        colParqueadero.setCellValueFactory(new PropertyValueFactory<>("parqueadero"));
        tablaIngresos.setItems(ingresos);
    }

    @FXML
    void toggleParqueadero(ActionEvent event) {
        boolean requiere = chkRequiereParqueadero.isSelected();
        panelParqueadero.setVisible(requiere);
        panelParqueadero.setManaged(requiere);
        if (!requiere) {
            txtPlaca.clear();
            cbParqueadero.getSelectionModel().clearSelection();
        }
    }

    @FXML
    void registrarEntradaExterna(ActionEvent event) {
        if (txtNombreVisitante.getText().isBlank() || txtIdentificacionVisitante.getText().isBlank()
                || txtDestino.getText().isBlank() || txtMotivo.getText().isBlank()) {
            mostrarError("Complete nombre, identificación, destino y motivo del visitante.");
            return;
        }

        String parqueaderoAsignado = "-";
        if (chkRequiereParqueadero.isSelected()) {
            if (txtPlaca.getText().isBlank() || cbParqueadero.getValue() == null) {
                mostrarError("Indique la placa y seleccione un parqueadero disponible.");
                return;
            }
            parqueaderoAsignado = cbParqueadero.getValue() + " (" + txtPlaca.getText().trim() + ")";
        }

        ingresos.add(0, new IngresoExternoFila(
                LocalTime.now().format(HORA_FORMATO),
                txtNombreVisitante.getText().trim(),
                txtDestino.getText().trim(),
                txtMotivo.getText().trim(),
                parqueaderoAsignado
        ));

        mostrarExito("Entrada externa registrada. Se notificó al residente relacionado.");
        limpiarFormulario(null);
    }

    @FXML
    void limpiarFormulario(ActionEvent event) {
        txtNombreVisitante.clear();
        txtIdentificacionVisitante.clear();
        txtTelefono.clear();
        txtDestino.clear();
        txtMotivo.clear();
        txtPlaca.clear();
        cbParqueadero.getSelectionModel().clearSelection();
        chkRequiereParqueadero.setSelected(false);
        panelParqueadero.setVisible(false);
        panelParqueadero.setManaged(false);
    }

    private void mostrarExito(String mensaje) {
        lblMensaje.setText(mensaje);
        lblMensaje.getStyleClass().setAll("message-label", "message-success");
    }

    private void mostrarError(String mensaje) {
        lblMensaje.setText(mensaje);
        lblMensaje.getStyleClass().setAll("message-label", "message-error");
    }

    public static class IngresoExternoFila {
        private final String hora;
        private final String nombre;
        private final String destino;
        private final String motivo;
        private final String parqueadero;

        public IngresoExternoFila(String hora, String nombre, String destino, String motivo, String parqueadero) {
            this.hora = hora;
            this.nombre = nombre;
            this.destino = destino;
            this.motivo = motivo;
            this.parqueadero = parqueadero;
        }

        public String getHora() { return hora; }
        public String getNombre() { return nombre; }
        public String getDestino() { return destino; }
        public String getMotivo() { return motivo; }
        public String getParqueadero() { return parqueadero; }
    }
}
