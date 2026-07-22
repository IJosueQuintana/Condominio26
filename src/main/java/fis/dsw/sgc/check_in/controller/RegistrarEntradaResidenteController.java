package fis.dsw.sgc.check_in.controller;

import fis.dsw.sgc.check_in.dto.RegistroEntradaDTO;
import fis.dsw.sgc.check_in.exception.CheckInException;
import fis.dsw.sgc.check_in.model.RegistroEntradaResidente;
import fis.dsw.sgc.check_in.service.ICheckInService;

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

import java.util.List;

public class RegistrarEntradaResidenteController {

    @FXML private TextField txtIdentificacion;
    @FXML private TextField txtNombreResidente;
    @FXML private TextField txtApellidoResidente;
    @FXML private TextField txtDepartamento;
    @FXML private Label lblMensaje;
    @FXML private Button btnBuscar;
    @FXML private Button btnRegistrar;

    @FXML private TableView<RegistroEntradaDTO> tablaIngresos;
    @FXML private TableColumn<RegistroEntradaDTO, String> colHora;
    @FXML private TableColumn<RegistroEntradaDTO, String> colNombre;
    @FXML private TableColumn<RegistroEntradaDTO, String> colDepto;

    private ICheckInService checkInService;
    private final ObservableList<RegistroEntradaDTO> ingresos = FXCollections.observableArrayList();

    public RegistrarEntradaResidenteController() {
        this(new fis.dsw.sgc.check_in.service.CheckInServiceImpl());
    }

    // Constructor para Inyección de Dependencias (Estilo Grupo A)
    public RegistrarEntradaResidenteController(ICheckInService checkInService) {
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
        colDepto.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDestino()));
        tablaIngresos.setItems(ingresos);

        cargarUltimosIngresos();
    }

    private void cargarUltimosIngresos() {
        if (checkInService != null) {
            List<RegistroEntradaDTO> lista = checkInService.obtenerHistorialDTO(null, "RESIDENTE", null);
            ingresos.setAll(lista);
        }
    }

    @FXML
    void buscarResidente(ActionEvent event) {
        String identificacion = txtIdentificacion.getText() == null ? "" : txtIdentificacion.getText().trim();
        if (identificacion.isEmpty()) {
            mostrarError("Ingrese la identificación del residente para buscarlo.");
            return;
        }

        if (checkInService == null) {
            mostrarError("El servicio de Check-In no ha sido inyectado.");
            return;
        }

        String[] datos = checkInService.buscarDatosResidentePorCedula(identificacion);
        if (datos == null) {
            txtNombreResidente.clear();
            txtApellidoResidente.clear();
            txtDepartamento.clear();
            mostrarError("No se encontró un residente activo registrado con la cédula " + identificacion);
            return;
        }

        txtNombreResidente.setText(datos[0]);
        txtApellidoResidente.setText(datos[1]);
        txtDepartamento.setText(datos[2]);
        mostrarInfo("Residente verificado. Haga clic en 'Registrar Entrada' para confirmar.");
    }

    @FXML
    void registrarEntrada(ActionEvent event) {
        String identificacion = txtIdentificacion.getText() == null ? "" : txtIdentificacion.getText().trim();
        if (identificacion.isEmpty() || txtNombreResidente.getText().isEmpty()) {
            mostrarError("Debe buscar y verificar un residente activo antes de registrar la entrada.");
            return;
        }

        try {
            RegistroEntradaResidente entrada = checkInService.registrarEntradaResidente(identificacion);
            mostrarExito("Entrada registrada con éxito para " + entrada.getNombres() + " " + entrada.getApellidos() + ".");
            limpiarFormulario(null);
            cargarUltimosIngresos();
        } catch (CheckInException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    void limpiarFormulario(ActionEvent event) {
        txtIdentificacion.clear();
        txtNombreResidente.clear();
        txtApellidoResidente.clear();
        txtDepartamento.clear();
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