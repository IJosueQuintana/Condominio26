package fis.dsw.sgc.reservas.controller;

import fis.dsw.sgc.reservas.model.Reserva;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.time.LocalDate;
import java.time.ZoneId;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
// ... imports will be merged correctly with standard Java imports

public class AuditarReservasController {

    @FXML
    private VBox panelPrincipal;

    @FXML
    private VBox panelObservacion;

    @FXML
    private TextArea txtObservacion;

    @FXML
    private TextField txtBusquedaResidente;

    @FXML
    private ChoiceBox<String> cbFiltroEspacio;

    @FXML
    private DatePicker dpFechaInicio;

    @FXML
    private DatePicker dpFechaFin;

    @FXML
    private TableView<Reserva> tablaReservas;

    @FXML
    private TableColumn<Reserva, String> colResidente;

    @FXML
    private TableColumn<Reserva, String> colEspacio;

    @FXML
    private TableColumn<Reserva, Date> colFecha;

    @FXML
    private TableColumn<Reserva, Time> colHoraInicio;

    @FXML
    private TableColumn<Reserva, Time> colHoraFin;

    @FXML
    private TableColumn<Reserva, String> colEstado;

    @FXML
    private TableColumn<Reserva, Void> colOpciones;

    @FXML
    private Button btnAnadirMulta;

    @FXML
    private ChoiceBox<String> cbMotivoMulta;

    private boolean aplicandoMulta = false;
    private Reserva reservaSeleccionadaParaObservacion = null;
    private Set<Reserva> reservasConObservacion = new HashSet<>();
    private ObservableList<Reserva> reservasMasterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Inicializar ChoiceBox de Espacios
        cbFiltroEspacio.getItems().addAll("Todos", "Cancha Sintética", "Salón de Eventos", "Área BBQ", "Gimnasio", "Piscina");
        cbFiltroEspacio.setValue("Todos");

        // 1. Configurar las columnas
        colResidente.setCellValueFactory(new PropertyValueFactory<>("nombreResidente"));
        colEspacio.setCellValueFactory(new PropertyValueFactory<>("idReserva")); 
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaReserva"));
        colHoraInicio.setCellValueFactory(new PropertyValueFactory<>("horaInicio"));
        colHoraFin.setCellValueFactory(new PropertyValueFactory<>("horaFin"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Formatear la Fecha (dd/MM/yyyy) y centrar
        colFecha.setCellFactory(column -> new TableCell<>() {
            private final java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("dd/MM/yyyy");
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(format.format(item));
                    setAlignment(Pos.CENTER);
                }
            }
        });

        // Formatear la Hora (HH:mm) y centrar
        javafx.util.Callback<TableColumn<Reserva, Time>, TableCell<Reserva, Time>> timeCellFactory = column -> new TableCell<>() {
            private final java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("HH:mm");
            @Override
            protected void updateItem(Time item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(format.format(item));
                    setAlignment(Pos.CENTER);
                }
            }
        };
        colHoraInicio.setCellFactory(timeCellFactory);
        colHoraFin.setCellFactory(timeCellFactory);

        // 2. Configurar la columna de estado para pintar la celda completa
        colEstado.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setAlignment(Pos.CENTER);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                    setBackground(Background.EMPTY);
                } else {
                    setGraphic(null);
                    String estadoFormateado = item.substring(0, 1).toUpperCase() + item.substring(1).toLowerCase();
                    setText(estadoFormateado);
                    
                    if ("ACTIVA".equalsIgnoreCase(item)) {
                        setBackground(new Background(new BackgroundFill(Color.web("#f3f4f6"), CornerRadii.EMPTY, Insets.EMPTY)));
                        setStyle("-fx-text-fill: #4b5563; -fx-font-weight: bold; -fx-font-size: 12px;");
                    } else if ("FINALIZADA".equalsIgnoreCase(item) || "COMPLETADA".equalsIgnoreCase(item)) {
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
            }
        });

        // 3. Configurar la columna de opciones
        colOpciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnAccion = new Button();

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Reserva reserva = getTableView().getItems().get(getIndex());
                    HBox box = new HBox(10);
                    box.setAlignment(Pos.CENTER);
                    
                    btnAccion.setPrefWidth(150);
                    btnAccion.getStyleClass().removeAll("btn-accion-tabla", "btn-accion-cancelar", "btn-accion-observacion");
                    btnAccion.getStyleClass().add("btn-accion-tabla");
                    
                    if ("ACTIVA".equalsIgnoreCase(reserva.getEstado())) {
                        btnAccion.setText("Cancelar Reserva");
                        btnAccion.getStyleClass().add("btn-accion-cancelar");
                        btnAccion.setOnAction(e -> System.out.println("Cancelar reserva " + reserva.getIdReserva()));
                        box.getChildren().add(btnAccion);
                        setGraphic(box);
                    } else if ("FINALIZADA".equalsIgnoreCase(reserva.getEstado())) {
                        btnAccion.setText("Agregar Observación");
                        btnAccion.getStyleClass().add("btn-accion-observacion");
                        
                        if (reservasConObservacion.contains(reserva)) {
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
            }
        });

        // 4. Crear datos ficticios
        Reserva activa = new Reserva();
        activa.setNombreResidente("Juan Pérez");
        activa.setIdReserva("Cancha Sintética");
        activa.setFechaReserva(new Date());
        activa.setHoraInicio(Time.valueOf("10:00:00"));
        activa.setHoraFin(Time.valueOf("12:00:00"));
        activa.setEstado("ACTIVA");

        Reserva completada = new Reserva();
        completada.setNombreResidente("María López");
        completada.setIdReserva("Salón de Eventos");
        
        // Fecha en el pasado para prueba de filtros
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.DAY_OF_MONTH, -5);
        completada.setFechaReserva(cal.getTime());
        
        completada.setHoraInicio(Time.valueOf("14:00:00"));
        completada.setHoraFin(Time.valueOf("18:00:00"));
        completada.setEstado("FINALIZADA");

        Reserva cancelada = new Reserva();
        cancelada.setNombreResidente("Carlos Ruiz");
        cancelada.setIdReserva("Área BBQ");
        cancelada.setFechaReserva(new Date());
        cancelada.setHoraInicio(Time.valueOf("19:00:00"));
        cancelada.setHoraFin(Time.valueOf("22:00:00"));
        cancelada.setEstado("CANCELADA");

        reservasMasterData.addAll(activa, completada, cancelada);

    // 5. Lógica de Filtrado Múltiple
        FilteredList<Reserva> filteredData = new FilteredList<>(reservasMasterData, b -> true);

        // Listener común para todos los filtros
        javafx.beans.value.ChangeListener<Object> filterChangeListener = (observable, oldValue, newValue) -> {
            filteredData.setPredicate(reserva -> {
                // 5.1 Filtro por Residente
                String searchString = txtBusquedaResidente.getText();
                boolean matchResidente = true;
                if (searchString != null && !searchString.isEmpty()) {
                    String lowerCaseFilter = searchString.toLowerCase();
                    matchResidente = reserva.getNombreResidente() != null && reserva.getNombreResidente().toLowerCase().contains(lowerCaseFilter);
                }

                // 5.2 Filtro por Espacio
                String espacioSeleccionado = cbFiltroEspacio.getValue();
                boolean matchEspacio = true;
                if (espacioSeleccionado != null && !"Todos".equals(espacioSeleccionado)) {
                    matchEspacio = reserva.getIdReserva() != null && reserva.getIdReserva().equals(espacioSeleccionado);
                }

                // 5.3 Filtro por Rango de Fechas
                boolean matchFecha = true;
                if (reserva.getFechaReserva() != null) {
                    LocalDate fechaReserva = reserva.getFechaReserva().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate fechaInicio = dpFechaInicio.getValue();
                    LocalDate fechaFin = dpFechaFin.getValue();

                    // Si no hay fecha fin, asumimos "hasta el presente" (hoy)
                    if (fechaFin == null) {
                        fechaFin = LocalDate.now();
                    }

                    if (fechaInicio != null) {
                        matchFecha = !fechaReserva.isBefore(fechaInicio) && !fechaReserva.isAfter(fechaFin);
                    } else {
                        // Si no hay fecha inicio, solo limitamos por el fin (hasta hoy)
                        matchFecha = !fechaReserva.isAfter(fechaFin);
                    }
                }

                return matchResidente && matchEspacio && matchFecha;
            });
        };

        txtBusquedaResidente.textProperty().addListener(filterChangeListener);
        cbFiltroEspacio.valueProperty().addListener(filterChangeListener);
        dpFechaInicio.valueProperty().addListener(filterChangeListener);
        dpFechaFin.valueProperty().addListener(filterChangeListener);

        SortedList<Reserva> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tablaReservas.comparatorProperty());
        tablaReservas.setItems(sortedData);

        // Inicializar Motivos de Multa
        cbMotivoMulta.getItems().addAll("Dañar los espacios", "No show");
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
            String observacion = txtObservacion.getText();
            System.out.println("Observación de auditoría guardada para " + reservaSeleccionadaParaObservacion.getIdReserva() + ": " + observacion);
            reservasConObservacion.add(reservaSeleccionadaParaObservacion);
            // Aquí iría la llamada al servicio: servicioReservas.registrarObservacion(...)
            if (aplicandoMulta && cbMotivoMulta.getValue() != null) {
                System.out.println(">>> Multa aplicada: " + cbMotivoMulta.getValue());
            }
        }
        cancelarObservacion(event);
        tablaReservas.refresh();
    }
}
