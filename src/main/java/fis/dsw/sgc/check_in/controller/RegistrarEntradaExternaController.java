package fis.dsw.sgc.check_in.controller;

import fis.dsw.sgc.check_in.dto.RegistroEntradaDTO;
import fis.dsw.sgc.check_in.exception.CheckInException;
import fis.dsw.sgc.check_in.service.ICheckInService;

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

import java.util.List;

public class RegistrarEntradaExternaController {

    @FXML private TextField txtIdentificacionVisitante;
    @FXML private TextField txtNombreVisitante;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtDestino;
    @FXML private TextField txtInfoAdicional;
    @FXML private CheckBox chkRequiereParqueadero;
    @FXML private VBox panelParqueadero;
    @FXML private TextField txtPlaca;
    @FXML private ChoiceBox<String> cbParqueadero;
    @FXML private Label lblMensaje;
    @FXML private Button btnRegistrar;

    @FXML private TableView<RegistroEntradaDTO> tablaIngresos;
    @FXML private TableColumn<RegistroEntradaDTO, String> colHora;
    @FXML private TableColumn<RegistroEntradaDTO, String> colNombre;
    @FXML private TableColumn<RegistroEntradaDTO, String> colDestino;
    @FXML private TableColumn<RegistroEntradaDTO, String> colInfoAdicional;
    @FXML private TableColumn<RegistroEntradaDTO, String> colParqueadero;

    private ICheckInService checkInService;
    private final ObservableList<RegistroEntradaDTO> ingresos = FXCollections.observableArrayList();

    public RegistrarEntradaExternaController() {
        this(new fis.dsw.sgc.check_in.service.CheckInServiceImpl());
    }

    public RegistrarEntradaExternaController(ICheckInService checkInService) {
        this.checkInService = checkInService;
    }

    /** Setter para DI manual por mainWindowController tras FXMLLoader */
    public void setCheckInService(ICheckInService checkInService) {
        this.checkInService = checkInService;
    }

    public ICheckInService getCheckInService() {
        return checkInService;
    }

    @FXML
    public void initialize() {
        colHora.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getHora()));
        colNombre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPersona()));
        colDestino.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDestino()));
        colInfoAdicional.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getObservaciones()));
        colParqueadero.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getParqueadero()));
        tablaIngresos.setItems(ingresos);

        cargarParqueaderosDisponibles();
        cargarTablaIngresos();
    }

    private void cargarParqueaderosDisponibles() {
        if (checkInService != null) {
            List<String> libres = checkInService.obtenerParqueaderosDisponibles("VISITA");
            if (libres.isEmpty()) {
                libres = checkInService.obtenerParqueaderosDisponibles(null);
            }
            cbParqueadero.setItems(FXCollections.observableArrayList(libres));
        }
    }

    private void cargarTablaIngresos() {
        if (checkInService != null) {
            List<RegistroEntradaDTO> lista = checkInService.obtenerHistorialDTO(null, "EXTERNA", null);
            ingresos.setAll(lista);
        }
    }


    @FXML
    void toggleParqueadero(ActionEvent event) {
        boolean requiere = chkRequiereParqueadero.isSelected();
        panelParqueadero.setVisible(requiere);
        panelParqueadero.setManaged(requiere);
        if (!requiere) {
            txtPlaca.clear();
            cbParqueadero.getSelectionModel().clearSelection();
        } else {
            cargarParqueaderosDisponibles();
        }
    }

    @FXML
    void registrarEntradaExterna(ActionEvent event) {
        String cedula = txtIdentificacionVisitante.getText() == null ? "" : txtIdentificacionVisitante.getText().trim();
        String nombre = txtNombreVisitante.getText() == null ? "" : txtNombreVisitante.getText().trim();
        String destino = txtDestino.getText() == null ? "" : txtDestino.getText().trim();
        String info = txtInfoAdicional.getText() == null ? "" : txtInfoAdicional.getText().trim();
        boolean parq = chkRequiereParqueadero.isSelected();
        String placa = txtPlaca.getText() == null ? "" : txtPlaca.getText().trim();
        String numParq = cbParqueadero.getValue();

        if (nombre.isBlank() || cedula.isBlank() || destino.isBlank()) {
            mostrarError("Nombre, identificación y destino/residente son campos obligatorios.");
            return;
        }

        if (parq && (placa.isBlank() || numParq == null)) {
            mostrarError("Para asignar parqueadero debe indicar la placa del vehículo y seleccionar un puesto libre.");
            return;
        }

        try {
            checkInService.registrarEntradaExterna(nombre, "", cedula, destino, info, parq, placa, numParq);
            mostrarExito("Entrada registrada correctamente. Se actualizó el historial.");
            limpiarFormulario(null);
            cargarTablaIngresos();
        } catch (CheckInException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    void limpiarFormulario(ActionEvent event) {
        txtNombreVisitante.clear();
        txtIdentificacionVisitante.clear();
        txtTelefono.clear();
        txtDestino.clear();
        txtInfoAdicional.clear();
        txtPlaca.clear();
        cbParqueadero.getSelectionModel().clearSelection();
        chkRequiereParqueadero.setSelected(false);
        panelParqueadero.setVisible(false);
        panelParqueadero.setManaged(false);
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
}