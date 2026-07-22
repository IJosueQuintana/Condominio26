package fis.dsw.sgc.reservas.controller;

import fis.dsw.sgc.reservas.dto.EspacioReservableDTO;
import fis.dsw.sgc.reservas.model.EstadoReserva;
import fis.dsw.sgc.reservas.model.Reserva;
import fis.dsw.sgc.reservas.service.IServicioReservas;
import fis.dsw.sgc.reservas.service.ServicioReservasImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Pantalla de auditoria de reservas (rol administrador/presidente).
 * Lista TODAS las reservas de la BD, permite filtrar por residente, espacio y
 * rango de fechas, cancelar reservas activas y registrar observaciones de
 * auditoria sobre reservas finalizadas.
 *
 * NOTAS DE INTEGRACION (pendientes):
 *   - El id del auditor en sesion deberia venir de GRB (SesionUsuario). Hoy se
 *     usa un id de demostracion como autor de las observaciones.
 *   - La creacion de multas NO es responsabilidad del modulo de Reservas
 *     (segun acuerdo con GRA). La UI de multa queda cableada como solicitud a
 *     Finanzas, pero la generacion real de la multa la realiza ese modulo.
 */
public class AuditarReservasController {

    // TODO(GRB): reemplazar por el id del usuario auditor autenticado.
    private static final int ID_AUDITOR_ACTUAL = 1; // 'Andrea Administradora' en seed.sql

    @FXML private VBox panelPrincipal;
    @FXML private VBox panelObservacion;
    @FXML private TextArea txtObservacion;
    @FXML private TextField txtBusquedaResidente;
    @FXML private ChoiceBox<String> cbFiltroEspacio;
    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;

    @FXML private TableView<Reserva> tablaReservas;
    @FXML private TableColumn<Reserva, String> colResidente;
    @FXML private TableColumn<Reserva, String> colEspacio;
    @FXML private TableColumn<Reserva, String> colFecha;
    @FXML private TableColumn<Reserva, String> colHoraInicio;
    @FXML private TableColumn<Reserva, String> colHoraFin;
    @FXML private TableColumn<Reserva, String> colEstado;
    @FXML private TableColumn<Reserva, Void> colOpciones;

    @FXML private Button btnAnadirMulta;
    @FXML private ChoiceBox<String> cbMotivoMulta;

    private final IServicioReservas servicioReservas = new ServicioReservasImpl();

    private boolean aplicandoMulta = false;
    private Reserva reservaSeleccionadaParaObservacion = null;
    private final Set<Integer> reservasConObservacion = new HashSet<>();
    private final ObservableList<Reserva> reservasMasterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // 1. Filtro de espacios: "Todos" + nombres reales de la BD
        cbFiltroEspacio.getItems().add("Todos");
        for (EspacioReservableDTO e : servicioReservas.listarEspaciosDisponibles()) {
            cbFiltroEspacio.getItems().add(e.getNombre());
        }
        cbFiltroEspacio.setValue("Todos");

        // 2. Columnas (todas basadas en texto, consistentes con el modelo)
        colResidente.setCellValueFactory(new PropertyValueFactory<>("nombreResidente"));
        colEspacio.setCellValueFactory(new PropertyValueFactory<>("nombreEspacio"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaReserva"));
        colHoraInicio.setCellValueFactory(new PropertyValueFactory<>("horaInicio"));
        colHoraFin.setCellValueFactory(new PropertyValueFactory<>("horaFin"));
        colEstado.setCellValueFactory(cd -> new SimpleStringProperty(
                cd.getValue().getEstado() != null ? cd.getValue().getEstado().name() : ""));

        configurarColumnaEstado();
        configurarColumnaOpciones();

        // 3. Cargar todas las reservas desde la BD
        cargarReservas();

        // 4. Filtrado combinado (residente + espacio + rango de fechas)
        FilteredList<Reserva> filteredData = new FilteredList<>(reservasMasterData, b -> true);
        javafx.beans.value.ChangeListener<Object> filterChangeListener =
                (obs, oldV, newV) -> filteredData.setPredicate(this::coincideConFiltros);

        txtBusquedaResidente.textProperty().addListener(filterChangeListener);
        cbFiltroEspacio.valueProperty().addListener(filterChangeListener);
        dpFechaInicio.valueProperty().addListener(filterChangeListener);
        dpFechaFin.valueProperty().addListener(filterChangeListener);

        SortedList<Reserva> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tablaReservas.comparatorProperty());
        tablaReservas.setItems(sortedData);

        // 5. Motivos de multa (solicitud a Finanzas)
        cbMotivoMulta.getItems().addAll("Dañar los espacios", "No show");
    }

    private void cargarReservas() {
        reservasMasterData.clear();
        List<Reserva> reservas = servicioReservas.listarTodasLasReservas();
        reservasMasterData.addAll(reservas);
        reservasConObservacion.clear();
        for (Reserva r : reservas) {
            if (!r.getObservaciones().isEmpty()) {
                reservasConObservacion.add(r.getId());
            }
        }
        tablaReservas.refresh();
    }

    private boolean coincideConFiltros(Reserva reserva) {
        // Residente
        String busqueda = txtBusquedaResidente.getText();
        boolean matchResidente = true;
        if (busqueda != null && !busqueda.isEmpty()) {
            String filtro = busqueda.toLowerCase();
            matchResidente = reserva.getNombreResidente() != null
                    && reserva.getNombreResidente().toLowerCase().contains(filtro);
        }

        // Espacio
        String espacioSel = cbFiltroEspacio.getValue();
        boolean matchEspacio = true;
        if (espacioSel != null && !"Todos".equals(espacioSel)) {
            matchEspacio = espacioSel.equals(reserva.getNombreEspacio());
        }

        // Rango de fechas (fechaReserva llega como "YYYY-MM-DD")
        boolean matchFecha = true;
        LocalDate fechaReserva = parseFecha(reserva.getFechaReserva());
        if (fechaReserva != null) {
            LocalDate inicio = dpFechaInicio.getValue();
            LocalDate fin = dpFechaFin.getValue();
            if (fin == null) fin = LocalDate.now();
            if (inicio != null) {
                matchFecha = !fechaReserva.isBefore(inicio) && !fechaReserva.isAfter(fin);
            } else {
                matchFecha = !fechaReserva.isAfter(fin);
            }
        }

        return matchResidente && matchEspacio && matchFecha;
    }

    private LocalDate parseFecha(String fecha) {
        if (fecha == null || fecha.isEmpty()) return null;
        try {
            // Toma solo la parte de fecha por si viniera con hora.
            return LocalDate.parse(fecha.length() >= 10 ? fecha.substring(0, 10) : fecha);
        } catch (Exception ex) {
            return null;
        }
    }

    private void configurarColumnaEstado() {
        colEstado.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setAlignment(Pos.CENTER);
                if (empty || item == null || item.isEmpty()) {
                    setText(null);
                    setStyle("");
                    setBackground(Background.EMPTY);
                    return;
                }
                setText(item.substring(0, 1).toUpperCase() + item.substring(1).toLowerCase());
                if ("ACTIVA".equalsIgnoreCase(item)) {
                    setBackground(new Background(new BackgroundFill(Color.web("#f3f4f6"), CornerRadii.EMPTY, Insets.EMPTY)));
                    setStyle("-fx-text-fill: #4b5563; -fx-font-weight: bold; -fx-font-size: 12px;");
                } else if ("FINALIZADA".equalsIgnoreCase(item)) {
                    setBackground(new Background(new BackgroundFill(Color.web("#ecfdf5"), CornerRadii.EMPTY, Insets.EMPTY)));
                    setStyle("-fx-text-fill: #059669; -fx-font-weight: bold; -fx-font-size: 12px;");
                } else if ("CANCELADA".equalsIgnoreCase(item)) {
                    setBackground(new Background(new BackgroundFill(Color.web("#ffebee"), CornerRadii.EMPTY, Insets.EMPTY)));
                    setStyle("-fx-text-fill: #c62828; -fx-font-weight: bold; -fx-font-size: 12px;");
                } else {
                    setBackground(Background.EMPTY);
                    setStyle("");
                }
            }
        });
    }

    private void configurarColumnaOpciones() {
        colOpciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnAccion = new Button();

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }
                Reserva reserva = getTableView().getItems().get(getIndex());
                EstadoReserva estado = reserva.getEstado();
                HBox box = new HBox(10);
                box.setAlignment(Pos.CENTER);
                btnAccion.setPrefWidth(150);
                btnAccion.getStyleClass().removeAll("btn-accion-tabla", "btn-accion-cancelar", "btn-accion-observacion");
                btnAccion.getStyleClass().add("btn-accion-tabla");

                if (estado == EstadoReserva.ACTIVA) {
                    btnAccion.setText("Cancelar Reserva");
                    btnAccion.getStyleClass().add("btn-accion-cancelar");
                    btnAccion.setDisable(false);
                    btnAccion.setStyle("");
                    btnAccion.setOnAction(e -> cancelarReserva(reserva));
                    box.getChildren().add(btnAccion);
                    setGraphic(box);
                } else if (estado == EstadoReserva.FINALIZADA) {
                    btnAccion.setText("Agregar Observación");
                    btnAccion.getStyleClass().add("btn-accion-observacion");
                    if (reservasConObservacion.contains(reserva.getId())) {
                        btnAccion.setDisable(true);
                        btnAccion.setStyle("-fx-background-color: #e5e7eb; -fx-text-fill: #9ca3af; -fx-opacity: 1;");
                    } else {
                        btnAccion.setDisable(false);
                        btnAccion.setStyle("");
                        btnAccion.setOnAction(e -> abrirPanelObservacion(reserva));
                    }
                    box.getChildren().add(btnAccion);
                    setGraphic(box);
                } else {
                    setGraphic(null);
                }
            }
        });
    }

    private void cancelarReserva(Reserva reserva) {
        boolean ok = servicioReservas.cancelarReserva(reserva.getId(),
                "Cancelada por auditoria");
        if (ok) {
            cargarReservas();
        }
    }

    @FXML
    void toggleMulta(ActionEvent event) {
        aplicandoMulta = !aplicandoMulta;
        if (aplicandoMulta) {
            btnAnadirMulta.setStyle("");
            btnAnadirMulta.getStyleClass().clear();
            btnAnadirMulta.getStyleClass().addAll("button", "danger-button");
            btnAnadirMulta.setText("Quitar Multa");
            cbMotivoMulta.setVisible(true);
            cbMotivoMulta.setManaged(true);
        } else {
            btnAnadirMulta.setStyle("");
            btnAnadirMulta.getStyleClass().clear();
            btnAnadirMulta.getStyleClass().addAll("button", "secondary-button");
            btnAnadirMulta.setText("Añadir Multa");
            cbMotivoMulta.setVisible(false);
            cbMotivoMulta.setManaged(false);
            cbMotivoMulta.setValue(null);
        }
    }

    private void abrirPanelObservacion(Reserva reserva) {
        reservaSeleccionadaParaObservacion = reserva;
        txtObservacion.clear();
        aplicandoMulta = false;
        btnAnadirMulta.setStyle("");
        btnAnadirMulta.getStyleClass().clear();
        btnAnadirMulta.getStyleClass().addAll("button", "secondary-button");
        btnAnadirMulta.setText("Añadir Multa");
        cbMotivoMulta.setVisible(false);
        cbMotivoMulta.setManaged(false);
        cbMotivoMulta.setValue(null);

        panelPrincipal.setVisible(false);
        panelPrincipal.setManaged(false);
        panelObservacion.setVisible(true);
        panelObservacion.setManaged(true);
    }

    @FXML
    void cancelarObservacion(ActionEvent event) {
        reservaSeleccionadaParaObservacion = null;
        panelObservacion.setVisible(false);
        panelObservacion.setManaged(false);
        panelPrincipal.setVisible(true);
        panelPrincipal.setManaged(true);
    }

    @FXML
    void enviarObservacion(ActionEvent event) {
        if (reservaSeleccionadaParaObservacion != null) {
            String texto = txtObservacion.getText();
            servicioReservas.registrarObservacion(
                    reservaSeleccionadaParaObservacion.getId(),
                    ID_AUDITOR_ACTUAL,
                    texto);
            reservasConObservacion.add(reservaSeleccionadaParaObservacion.getId());

            // Solicitud de multa a Finanzas (GRA). La creacion de la multa es
            // responsabilidad del modulo de Finanzas; aqui solo se deja la
            // intencion cableada. Cuando Main inyecte la fachada, este bloque
            // deberia delegar en IFachadaParaReservas.registrarDeuda(...).
            if (aplicandoMulta && cbMotivoMulta.getValue() != null) {
                System.out.println("[Reservas] Solicitud de multa a Finanzas -> motivo: "
                        + cbMotivoMulta.getValue()
                        + " (reserva " + reservaSeleccionadaParaObservacion.getId() + ")");
            }
        }
        cancelarObservacion(event);
        cargarReservas();
    }
}
