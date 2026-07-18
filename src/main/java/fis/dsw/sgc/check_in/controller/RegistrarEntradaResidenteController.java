package fis.dsw.sgc.check_in.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class RegistrarEntradaResidenteController {

    @FXML private TextField txtIdentificacion;
    @FXML private TextField txtNombreResidente;
    @FXML private TextField txtDepartamento;
    @FXML private TextField txtMotivo;
    @FXML private Label lblMensaje;
    @FXML private Button btnBuscar;
    @FXML private Button btnRegistrar;

    @FXML private TableView<IngresoResidenteFila> tablaIngresos;
    @FXML private TableColumn<IngresoResidenteFila, String> colHora;
    @FXML private TableColumn<IngresoResidenteFila, String> colNombre;
    @FXML private TableColumn<IngresoResidenteFila, String> colDepto;
    @FXML private TableColumn<IngresoResidenteFila, String> colMotivo;

    private final ObservableList<IngresoResidenteFila> ingresos = FXCollections.observableArrayList();
    private static final DateTimeFormatter HORA_FORMATO = DateTimeFormatter.ofPattern("HH:mm");

    // Simula la validación contra el módulo GRA hasta que exista el servicio real.
    private final Map<String, String[]> residentesDemo = new HashMap<>();

    @FXML
    public void initialize() {
        residentesDemo.put("1712345678", new String[]{"María Fernanda Cárdenas", "Depto. 302"});
        residentesDemo.put("1798765432", new String[]{"Jorge Andrés Salazar", "Depto. 101"});

        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDepto.setCellValueFactory(new PropertyValueFactory<>("departamento"));
        colMotivo.setCellValueFactory(new PropertyValueFactory<>("motivo"));
        tablaIngresos.setItems(ingresos);
    }

    @FXML
    void buscarResidente(ActionEvent event) {
        String identificacion = txtIdentificacion.getText() == null ? "" : txtIdentificacion.getText().trim();
        if (identificacion.isEmpty()) {
            mostrarError("Ingrese la identificación del residente para buscarlo.");
            return;
        }

        String[] datos = residentesDemo.get(identificacion);
        if (datos == null) {
            txtNombreResidente.clear();
            txtDepartamento.clear();
            mostrarError("No se encontró un residente registrado con esa identificación.");
            return;
        }

        txtNombreResidente.setText(datos[0]);
        txtDepartamento.setText(datos[1]);
        mostrarInfo("Residente encontrado. Verifique los datos antes de registrar el ingreso.");
    }

    @FXML
    void registrarEntrada(ActionEvent event) {
        if (txtNombreResidente.getText().isEmpty() || txtDepartamento.getText().isEmpty()) {
            mostrarError("Debe buscar y verificar un residente antes de registrar la entrada.");
            return;
        }

        String motivo = txtMotivo.getText() == null || txtMotivo.getText().isBlank()
                ? "Ingreso regular"
                : txtMotivo.getText().trim();

        ingresos.add(0, new IngresoResidenteFila(
                LocalTime.now().format(HORA_FORMATO),
                txtNombreResidente.getText(),
                txtDepartamento.getText(),
                motivo
        ));

        mostrarExito("Entrada registrada correctamente para " + txtNombreResidente.getText() + ".");
        limpiarFormulario(null);
    }

    @FXML
    void limpiarFormulario(ActionEvent event) {
        txtIdentificacion.clear();
        txtNombreResidente.clear();
        txtDepartamento.clear();
        txtMotivo.clear();
    }

    private void mostrarInfo(String mensaje) {
        lblMensaje.setText(mensaje);
        lblMensaje.getStyleClass().setAll("message-label", "message-info");
    }

    private void mostrarExito(String mensaje) {
        lblMensaje.setText(mensaje);
        lblMensaje.getStyleClass().setAll("message-label", "message-success");
    }

    private void mostrarError(String mensaje) {
        lblMensaje.setText(mensaje);
        lblMensaje.getStyleClass().setAll("message-label", "message-error");
    }

    public static class IngresoResidenteFila {
        private final String hora;
        private final String nombre;
        private final String departamento;
        private final String motivo;

        public IngresoResidenteFila(String hora, String nombre, String departamento, String motivo) {
            this.hora = hora;
            this.nombre = nombre;
            this.departamento = departamento;
            this.motivo = motivo;
        }

        public String getHora() { return hora; }
        public String getNombre() { return nombre; }
        public String getDepartamento() { return departamento; }
        public String getMotivo() { return motivo; }
    }
}
