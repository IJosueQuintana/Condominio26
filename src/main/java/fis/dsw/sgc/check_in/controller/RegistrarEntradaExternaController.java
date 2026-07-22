package fis.dsw.sgc.check_in.controller;

import fis.dsw.sgc.check_in.dto.RegistroEntradaDTO;
import fis.dsw.sgc.check_in.exception.CheckInException;
import fis.dsw.sgc.check_in.model.VisitaProgramada;
import fis.dsw.sgc.check_in.service.ICheckInService;
import fis.dsw.sgc.check_in.service.IProgramVisitaService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @FXML private CheckBox chkVisitaProgramada;
    @FXML private VBox panelVisitaProgramada;
    @FXML private ComboBox<String> cmbVisitasProgramadas;

    @FXML private TableView<RegistroEntradaDTO> tablaIngresos;
    @FXML private TableColumn<RegistroEntradaDTO, String> colHora;
    @FXML private TableColumn<RegistroEntradaDTO, String> colNombre;
    @FXML private TableColumn<RegistroEntradaDTO, String> colDestino;
    @FXML private TableColumn<RegistroEntradaDTO, String> colInfoAdicional;
    @FXML private TableColumn<RegistroEntradaDTO, String> colParqueadero;

    private ICheckInService checkInService;
    private IProgramVisitaService visitaProgramadaService;
    private Map<String, VisitaProgramada> mapaVisitasProgramadas = new HashMap<>();
    private Map<String, Integer> mapaResidentes = new HashMap<>();
    private final ObservableList<RegistroEntradaDTO> ingresos = FXCollections.observableArrayList();

    public RegistrarEntradaExternaController() {
        this(new fis.dsw.sgc.check_in.service.CheckInServiceImpl(), new fis.dsw.sgc.check_in.service.ProgramVisitaService());
    }

    public RegistrarEntradaExternaController(ICheckInService checkInService, IProgramVisitaService visitaProgramadaService) {
        this.visitaProgramadaService = visitaProgramadaService;
        this.checkInService = checkInService;
    }

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
        cargarResidentes();

        // Listener para el ComboBox de visitas programadas
        cmbVisitasProgramadas.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && mapaVisitasProgramadas.containsKey(newVal)) {
                VisitaProgramada visita = mapaVisitasProgramadas.get(newVal);

                // 1. Autocompletar datos personales
                txtIdentificacionVisitante.setText(visita.getCedulaVisita());
                txtNombreVisitante.setText(visita.getNombresVisita() + " " + visita.getApellidosVisita());
                txtInfoAdicional.setText(visita.getMotivoVisita());

                // 2. Autocompletar Residente / Destino
                String nombreResidente = "Visita Externa / Sin Residente";
                if (visita.getIdResidente() != null && mapaResidentes != null) {
                    for (Map.Entry<String, Integer> entry : mapaResidentes.entrySet()) {
                        if (entry.getValue().equals(visita.getIdResidente())) {
                            nombreResidente = entry.getKey();
                            break;
                        }
                    }
                }
                txtDestino.setText(nombreResidente);

                // 3. Autocompletar Parqueadero y autoseleccionar el primer espacio libre
                if (visita.getPlaca() != null && !visita.getPlaca().trim().isEmpty() && !visita.getPlaca().equalsIgnoreCase("N/A")) {
                    chkRequiereParqueadero.setSelected(true);
                    toggleParqueadero(null); // Despliega panel y carga parqueaderos
                    txtPlaca.setText(visita.getPlaca());

                    if (!cbParqueadero.getItems().isEmpty()) {
                        cbParqueadero.getSelectionModel().selectFirst();
                    }
                } else {
                    chkRequiereParqueadero.setSelected(false);
                    toggleParqueadero(null);
                    txtPlaca.clear();
                }
            }
        });
    }

    private void cargarResidentes() {
        if (visitaProgramadaService != null) {
            mapaResidentes = visitaProgramadaService.obtenerResidentes();
        }
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
            // 1. Registrar la entrada externa
            checkInService.registrarEntradaExterna(nombre, "", cedula, destino, info, parq, placa, numParq);

            // 2. Si el registro proviene de una visita programada, cambiar estado a REALIZADA
            if (chkVisitaProgramada != null && chkVisitaProgramada.isSelected() && cmbVisitasProgramadas.getValue() != null) {
                String textoSeleccionado = cmbVisitasProgramadas.getValue();
                VisitaProgramada visitaProgramada = mapaVisitasProgramadas.get(textoSeleccionado);

                if (visitaProgramada != null) {
                    boolean marcada = visitaProgramadaService.marcarVisitaProgRealizada(visitaProgramada.getIdVisita());
                    if (!marcada) {
                        System.err.println("Advertencia: No se pudo cambiar el estado de la visita programada #" + visitaProgramada.getIdVisita() + " a REALIZADA.");
                    }
                }
            }

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

        if (chkVisitaProgramada != null) {
            chkVisitaProgramada.setSelected(false);
        }
        if (panelVisitaProgramada != null) {
            panelVisitaProgramada.setVisible(false);
            panelVisitaProgramada.setManaged(false);
        }
        if (cmbVisitasProgramadas != null) {
            cmbVisitasProgramadas.getSelectionModel().clearSelection();
        }
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

    @FXML
    void toggleVisitaProgramada(ActionEvent event) {
        boolean isSelected = chkVisitaProgramada.isSelected();
        panelVisitaProgramada.setVisible(isSelected);
        panelVisitaProgramada.setManaged(isSelected);

        if (isSelected) {
            cargarVisitasEnComboBox();
        } else {
            cmbVisitasProgramadas.getSelectionModel().clearSelection();
            limpiarFormulario(null);
        }
    }

    private void cargarVisitasEnComboBox() {
        mapaVisitasProgramadas.clear();
        List<VisitaProgramada> lista = visitaProgramadaService.obtenerVisitasProgramadas();

        for (VisitaProgramada visita : lista) {
            String estadoStr = visita.getEstado() != null ? visita.getEstado().toString() : "";
            if ("PROGRAMADA".equalsIgnoreCase(estadoStr)) {
                String textoMostrar = String.format("%s %s - C.C: %s - Fecha: %s %s",
                        visita.getNombresVisita(),
                        visita.getApellidosVisita(),
                        visita.getCedulaVisita(),
                        visita.getFechaProgramada(),
                        visita.getHoraProgramada());

                mapaVisitasProgramadas.put(textoMostrar, visita);
            }
        }
        ObservableList<String> opciones = FXCollections.observableArrayList(mapaVisitasProgramadas.keySet());
        cmbVisitasProgramadas.setItems(opciones);
    }
}