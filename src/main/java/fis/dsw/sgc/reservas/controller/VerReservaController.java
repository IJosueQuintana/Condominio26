package fis.dsw.sgc.reservas.controller;

import fis.dsw.sgc.reservas.model.Reserva;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.sql.Time;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class VerReservaController {

    @FXML
    private VBox panelPrincipal;

    @FXML
    private VBox panelObservacion;

    @FXML
    private TextArea txtObservacion;

    @FXML
    private TableView<Reserva> tablaReservas;

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

    private Reserva reservaSeleccionadaParaObservacion = null;
    private Set<Reserva> reservasConObservacion = new HashSet<>();

    @FXML
    public void initialize() {
        // 1. Configurar las columnas
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

        // 3. Configurar la columna de opciones con CellFactory
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
                    box.setAlignment(Pos.CENTER); // Centramos los botones en la celda
                    
                    // Ancho fijo para que todos los botones midan exactamente lo mismo
                    btnAccion.setPrefWidth(150);
                    
                    // Limpiar clases previas
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
                        // CANCELADA no tiene opciones
                        setGraphic(null);
                    }
                }
            }
        });

        // 4. Crear datos ficticios (Dummy data)
        ObservableList<Reserva> reservasDummy = FXCollections.observableArrayList();
        
        Reserva activa = new Reserva();
        activa.setIdReserva("Cancha Sintética");
        activa.setFechaReserva(new Date());
        activa.setHoraInicio(Time.valueOf("10:00:00"));
        activa.setHoraFin(Time.valueOf("12:00:00"));
        activa.setEstado("ACTIVA");

        Reserva completada = new Reserva();
        completada.setIdReserva("Salón de Eventos");
        completada.setFechaReserva(new Date());
        completada.setHoraInicio(Time.valueOf("14:00:00"));
        completada.setHoraFin(Time.valueOf("18:00:00"));
        completada.setEstado("FINALIZADA");

        Reserva cancelada = new Reserva();
        cancelada.setIdReserva("Área BBQ");
        cancelada.setFechaReserva(new Date());
        cancelada.setHoraInicio(Time.valueOf("19:00:00"));
        cancelada.setHoraFin(Time.valueOf("22:00:00"));
        cancelada.setEstado("CANCELADA");

        reservasDummy.addAll(activa, completada, cancelada);

        // 4. Cargar en la tabla
        tablaReservas.setItems(reservasDummy);
    }

    private void abrirPanelObservacion(Reserva reserva) {
        reservaSeleccionadaParaObservacion = reserva;
        txtObservacion.clear();
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
            System.out.println("Observación guardada para " + reservaSeleccionadaParaObservacion.getIdReserva() + ": " + observacion);
            reservasConObservacion.add(reservaSeleccionadaParaObservacion);
            // Aquí iría la llamada al servicio: servicioReservas.registrarObservacion(...)
        }
        // Cerrar panel y volver a la tabla
        cancelarObservacion(event);
        tablaReservas.refresh();
    }
}
