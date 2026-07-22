package fis.dsw.sgc.check_in.controller;

import fis.dsw.sgc.check_in.dao.ProgramacionVisitaDAO;
import fis.dsw.sgc.check_in.model.EstadoVisita;
import fis.dsw.sgc.check_in.model.VisitaProgramada;
import fis.dsw.sgc.check_in.service.IProgramVisitaService;
import fis.dsw.sgc.check_in.service.ProgramVisitaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ProgramarVisitaController {

    // INYECCIONES DE PANELES (STACKPANE)
    @FXML private VBox panelFormulario;
    @FXML private VBox panelTabla;

    // COMPONENTES DEL FORMULARIO
    @FXML private ComboBox<String> cmbTipoVisita;
    @FXML private TextField txtNombreVisitante;
    @FXML private TextField txtApellidoVisitante;
    @FXML private TextField txtCedulaVisitante;
    @FXML private ComboBox<String> cmbResidente;
    @FXML private TextField txtMotivo;
    @FXML private DatePicker dpFecha;
    @FXML private TextField txtHora;
    @FXML private TextField txtPlaca;
    @FXML private TextField txtInfoAdicional;
    @FXML private Label lblMensaje;

    // COMPONENTES DE LA TABLA (Solo columnas visibles)
    @FXML private TableView<VisitaProgramadaFila> tablaVisitas;
    @FXML private TableColumn<VisitaProgramadaFila, String> colVisitante;
    @FXML private TableColumn<VisitaProgramadaFila, String> colResidente;
    @FXML private TableColumn<VisitaProgramadaFila, String> colFecha;
    @FXML private TableColumn<VisitaProgramadaFila, String> colHora;
    @FXML private TableColumn<VisitaProgramadaFila, String> colEstado;
    @FXML private TableColumn<VisitaProgramadaFila, Void> colOpciones;

    // ATRIBUTOS DE LA CLASE
    private IProgramVisitaService programVisitaService;
    private Map<String, Integer> mapaResidentes;
    private Map<Integer, String> mapaNombresResidentes;
    private final ObservableList<VisitaProgramadaFila> visitas = FXCollections.observableArrayList();

    public ProgramarVisitaController() {
        this(new ProgramVisitaService(new ProgramacionVisitaDAO()));
    }

    public ProgramarVisitaController(IProgramVisitaService programVisitaService) {
        this.programVisitaService = programVisitaService;
    }

    /** Setter para DI manual por mainWindowController tras FXMLLoader */
    public void setProgramVisitaService(IProgramVisitaService programVisitaService) {
        this.programVisitaService = programVisitaService;
    }

    public IProgramVisitaService getProgramVisitaService() {
        return programVisitaService;
    }

    @FXML
    public void initialize() {
        panelFormulario.setVisible(true);
        panelTabla.setVisible(false);

        cmbTipoVisita.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if ("Visita Externa".equals(newVal)) {
                cmbResidente.setDisable(true);
                cmbResidente.getSelectionModel().clearSelection();
                cmbResidente.getEditor().clear();
            } else {
                cmbResidente.setDisable(false);
            }
        });

        cargarResidentes();
        configurarColumnasTabla();
        configurarEventosTabla();
        cargarVisitasProgramadas();
    }

    // --- NAVEGACIÓN ---
    @FXML
    void abrirVentanaVisitas(ActionEvent event) {
        cargarVisitasProgramadas();
        panelFormulario.setVisible(false);
        panelTabla.setVisible(true);
    }

    @FXML
    void volverAlFormulario(ActionEvent event) {
        panelTabla.setVisible(false);
        panelFormulario.setVisible(true);
    }

    // --- LÓGICA DEL FORMULARIO ---
    public void cargarResidentes() {
        mapaResidentes = programVisitaService.obtenerResidentes();
        mapaNombresResidentes = programVisitaService.obtenerNombresResidentesPorId();
        ObservableList<String> opciones = FXCollections.observableArrayList(mapaResidentes.keySet());
        cmbResidente.getEditor().clear();
        cmbResidente.setItems(opciones);
    }

    @FXML
    void programarVisita(ActionEvent event) {
        if (!validarFormulario()) return;

        VisitaProgramada visitaProgramada = new VisitaProgramada();

        if ("Visita a Residente".equals(cmbTipoVisita.getValue())) {
            String nombreSeleccionado = cmbResidente.getValue().trim();
            Integer idResidente = mapaResidentes.get(nombreSeleccionado);
            if (idResidente == null) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "El residente escrito no existe. Seleccione uno de la lista.");
                return;
            }
            visitaProgramada.setIdResidente(idResidente);
        }

        visitaProgramada.setNombresVisita(txtNombreVisitante.getText().trim());
        visitaProgramada.setApellidosVisita(txtApellidoVisitante.getText().trim());
        visitaProgramada.setCedulaVisita(txtCedulaVisitante.getText().trim());
        visitaProgramada.setMotivoVisita(txtMotivo.getText().trim());
        visitaProgramada.setFechaProgramada(dpFecha.getValue().toString());
        visitaProgramada.setHoraProgramada(txtHora.getText().trim());
        visitaProgramada.setInformacionAdicional(txtInfoAdicional.getText().trim());
        visitaProgramada.setEstado(EstadoVisita.PROGRAMADA);
        String tipoVisita = cmbTipoVisita.getValue().equals("Visita Externa") ? "EXTERNA" : "A RESIDENTE";
        visitaProgramada.setTipoVisita(tipoVisita);
        String placa = txtPlaca.getText().trim();
        visitaProgramada.setPlaca(placa.isBlank() ? "N/A" : placa);

        if (programVisitaService.programarVisita(visitaProgramada)) {
            mostrarExito("Visita programada correctamente.");
            limpiarFormulario(null);
            cargarVisitasProgramadas();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Sistema", "No se pudo registrar la visita en la base de datos.");
        }
    }

    @FXML
    void limpiarFormulario(ActionEvent event) {
        cmbTipoVisita.getSelectionModel().clearSelection();
        txtNombreVisitante.clear();
        txtApellidoVisitante.clear();
        txtCedulaVisitante.clear();
        txtPlaca.clear();
        cmbResidente.getSelectionModel().clearSelection();
        cmbResidente.getEditor().clear();
        txtMotivo.clear();
        txtInfoAdicional.clear();
        dpFecha.setValue(null);
        txtHora.clear();

        if (event != null) lblMensaje.setText("");
    }

    private boolean validarFormulario() {
        if (cmbTipoVisita.getSelectionModel().getSelectedItem() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campo Requerido", "Por favor seleccione el Tipo de Visita.");
            return false;
        }
        if (!txtNombreVisitante.getText().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
            mostrarAlerta(Alert.AlertType.WARNING, "Formato Incorrecto", "El nombre ingresado no es válido.");
            return false;
        }
        if (!txtApellidoVisitante.getText().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
            mostrarAlerta(Alert.AlertType.WARNING, "Formato Incorrecto", "El apellido ingresado no es válido.");
            return false;
        }
        if (!txtCedulaVisitante.getText().matches("^\\d{10}$")) {
            mostrarAlerta(Alert.AlertType.WARNING, "Formato Incorrecto", "La cédula debe contener 10 dígitos numéricos.");
            return false;
        }
        if ("Visita a Residente".equals(cmbTipoVisita.getValue())) {
            String residenteIngresado = cmbResidente.getValue().trim();
            if (residenteIngresado == null || residenteIngresado.trim().isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Campo Requerido", "Debe indicar el residente asociado.");
                return false;
            }
        }
        if (txtMotivo.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campo Requerido", "El motivo de la visita es obligatorio.");
            return false;
        }
        LocalDate fecha = dpFecha.getValue();
        if (fecha == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campo Requerido", "Debe seleccionar una fecha estimada.");
            return false;
        }
        if (fecha.isBefore(LocalDate.now())) {
            mostrarAlerta(Alert.AlertType.WARNING, "Fecha Inválida", "No puede programar una visita pasada.");
            return false;
        }
        if (!txtHora.getText().matches("^([01]\\d|2[0-3]):([0-5]\\d)$")) {
            mostrarAlerta(Alert.AlertType.WARNING, "Formato Incorrecto", "La hora debe tener formato 24h (HH:mm).");
            return false;
        }
        if (!txtPlaca.getText().trim().isEmpty() && !txtPlaca.getText().trim().matches("^[A-Za-z]{3}-?\\d{3,4}$")) {
            mostrarAlerta(Alert.AlertType.WARNING, "Formato Incorrecto", "La placa debe tener un formato válido (Ej: ABC-1234).");
            return false;
        }
        return true;
    }

    // --- LÓGICA DE LA TABLA ---

    // Función para permitir abrir detalles al hacer doble clic en una fila
    private void configurarEventosTabla() {
        tablaVisitas.setRowFactory(tv -> {
            TableRow<VisitaProgramadaFila> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    VisitaProgramadaFila rowData = row.getItem();
                    mostrarDetallesVisita(rowData);
                }
            });
            return row;
        });
    }

    private void configurarColumnasTabla() {
        colVisitante.setCellValueFactory(new PropertyValueFactory<>("visitante"));
        colResidente.setCellValueFactory(new PropertyValueFactory<>("residente"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));

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
                badge.getStyleClass().add("badge-estado");
                if (estado.equalsIgnoreCase("Programada")) badge.getStyleClass().add("badge-programada");
                else if (estado.equalsIgnoreCase("Realizada")) badge.getStyleClass().add("badge-realizada");
                else if (estado.equalsIgnoreCase("Cancelada")) badge.getStyleClass().add("badge-cancelada");
                setGraphic(badge);
            }
        });

        colOpciones.setCellFactory(param -> new TableCell<>() {

            private final Button btnReprogramar = new Button("Reprogramar");
            private final Button btnCancelar = new Button("Cancelar");
            private final HBox panelBotones = new HBox(8, btnReprogramar, btnCancelar);

            {
                panelBotones.setAlignment(Pos.CENTER);
                btnReprogramar.getStyleClass().addAll("secondary-button", "action-button");
                btnCancelar.getStyleClass().addAll("secondary-button", "danger-button");


                btnReprogramar.setOnAction(event -> {
                    VisitaProgramadaFila visitaActual = getTableView().getItems().get(getIndex());
                    mostrarDialogoReprogramar(visitaActual);
                });

                btnCancelar.setOnAction(event -> {
                    VisitaProgramadaFila visitaActual = getTableView().getItems().get(getIndex());
                    confirmarCancelacionVisita(visitaActual);
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
                if (fila != null && ("CANCELADA".equalsIgnoreCase(fila.getEstado()) || "REALIZADA".equalsIgnoreCase(fila.getEstado()))) {
                    setGraphic(null);
                } else {
                    setGraphic(panelBotones);
                }
            }
        });

        tablaVisitas.setItems(visitas);
    }

    private void cargarVisitasProgramadas() {
        visitas.clear();
        List<VisitaProgramada> lista = programVisitaService.obtenerVisitasProgramadas();
        for (VisitaProgramada visita : lista) {
            String nombreResidente = "Externo";
            if (visita.getIdResidente() != null && mapaNombresResidentes != null) {
                nombreResidente = mapaNombresResidentes.getOrDefault(visita.getIdResidente(), "Externo");
            }
            VisitaProgramadaFila visitaActual = new VisitaProgramadaFila(
                    visita.getIdVisita(),
                    visita.getNombresVisita() + " " + visita.getApellidosVisita(),
                    visita.getCedulaVisita(),
                    nombreResidente,
                    visita.getTipoVisita(),
                    visita.getFechaProgramada(),
                    visita.getHoraProgramada(),
                    visita.getPlaca(),
                    visita.getMotivoVisita(),
                    visita.getEstado()
            );
            visitas.add(visitaActual);
        }
    }

    // Cuadro de diálogo de Detalles
    private void mostrarDetallesVisita(VisitaProgramadaFila visita) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalles de la Visita");
        alert.setHeaderText("Información de la visita programada");

        String contenido = String.format(
                "Nombre del Visitante: %s\n" +
                        "Cedula del Visitante: %s\n" +
                        "Placa del Vehiculo: %s\n" +
                        "Tipo de Visita: %s\n" +
                        "Motivo: %s",
                visita.getVisitante(),
                visita.getCedula(),
                visita.getPlaca(),
                visita.getTipoVisita(),
                visita.getMotivo()
        );

        alert.setContentText(contenido);
        alert.showAndWait();
    }

    private void mostrarDialogoReprogramar(VisitaProgramadaFila visitaActual) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Reprogramar Visita");
        dialog.setHeaderText("Modifique la fecha y hora para: " + visitaActual.getVisitante());

        ButtonType btnGuardar = new ButtonType("Guardar Cambios", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        DatePicker dpNuevaFecha = new DatePicker();
        TextField txtNuevaHora = new TextField();
        txtNuevaHora.setPromptText("HH:mm");

        VBox content = new VBox(10, new Label("Nueva Fecha:"), dpNuevaFecha, new Label("Nueva Hora:"), txtNuevaHora);
        dialog.getDialogPane().setContent(content);

        // Interceptar el botón "Guardar Cambios" para validar ANTES de que el diálogo intente cerrarse
        Button btGuardarNode = (Button) dialog.getDialogPane().lookupButton(btnGuardar);
        btGuardarNode.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            // Validar Fecha
            LocalDate fecha = dpNuevaFecha.getValue();
            if (fecha == null) {
                mostrarAlerta(Alert.AlertType.WARNING, "Campo Requerido", "Debe seleccionar una nueva fecha.");
                event.consume(); // Evita que se cierre el diálogo
                return;
            }
            if (fecha.isBefore(LocalDate.now())) {
                mostrarAlerta(Alert.AlertType.WARNING, "Fecha Inválida", "No puede reprogramar una visita para una fecha pasada.");
                event.consume();
                return;
            }

            String hora = txtNuevaHora.getText().trim();
            if (!hora.matches("^([01]\\d|2[0-3]):([0-5]\\d)$")) {
                mostrarAlerta(Alert.AlertType.WARNING, "Formato Incorrecto", "La hora debe tener formato válido de 24h (HH:mm). Ej: 08:30 o 14:00.");
                event.consume();
                return;
            }
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnGuardar) {
                programVisitaService.actualizarFechaHora(visitaActual.getIdVisita(), dpNuevaFecha.getValue().toString(), txtNuevaHora.getText().trim());
                cargarVisitasProgramadas();
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Visita reprogramada con éxito.");
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void confirmarCancelacionVisita(VisitaProgramadaFila visitaActual) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar cancelación");
        alert.setHeaderText(null);
        alert.setContentText("¿Seguro que quieres eliminar la visita de " + visitaActual.getVisitante() + "?");
        ButtonType btnAceptar = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelarDialog = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnAceptar, btnCancelarDialog);

        alert.showAndWait().ifPresent(type -> {
            if (type == btnAceptar) {
                programVisitaService.cancelarVisitaProgramada(visitaActual.getIdVisita());
                visitaActual.setEstado("CANCELADA");
                tablaVisitas.refresh();
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Visita cancelada con éxito.");
            }
        });
    }

    // --- ALERTAS ---
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    private void mostrarExito(String mensaje) {
        lblMensaje.setText(mensaje);
        lblMensaje.getStyleClass().setAll("message-label", "message-success");
    }

    // --- MODELO PARA LA TABLA ---
    public static class VisitaProgramadaFila {
        private final Integer idVisita;
        private final String visitante;
        private final String cedula;
        private final String residente;
        private final String tipoVisita;
        private final String fecha;
        private final String hora;
        private final String placa;
        private final String motivo;
        private String estado;

        public VisitaProgramadaFila(Integer idVisita, String visitante, String cedula, String residente, String tipoVisita, String fecha, String hora, String placa, String motivo, String estado) {
            this.idVisita = idVisita;
            this.visitante = visitante;
            this.cedula = cedula;
            this.residente = residente;
            this.tipoVisita = tipoVisita;
            this.fecha = fecha;
            this.hora = hora;
            this.placa = placa;
            this.motivo = motivo;
            this.estado = estado;
        }

        public Integer getIdVisita() { return idVisita; }
        public String getVisitante() { return visitante; }
        public String getCedula() { return cedula; }
        public String getResidente() { return residente; }
        public String getTipoVisita() { return tipoVisita; }
        public String getFecha() { return fecha; }
        public String getHora() { return hora; }
        public String getPlaca() { return placa; }
        public String getMotivo() { return motivo; }
        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
    }
}