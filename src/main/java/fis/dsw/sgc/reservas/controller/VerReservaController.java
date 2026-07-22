package fis.dsw.sgc.reservas.controller;

import fis.dsw.sgc.reservas.model.EstadoReserva;
import fis.dsw.sgc.reservas.model.Reserva;
import fis.dsw.sgc.reservas.service.IServicioReservas;
import fis.dsw.sgc.reservas.service.ServicioReservasImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Pantalla "Mis Reservas". Muestra las reservas del residente actual leidas
 * desde la BD (a traves de IServicioReservas) y permite cancelar reservas
 * activas o dejar una observacion en reservas finalizadas.
 *
 * NOTA DE INTEGRACION (pendiente):
 *   El id del usuario en sesion deberia venir del modulo de Administracion
 *   (GRB). Hoy SesionUsuario expone un Usuario con UUID y sin id numerico ni
 *   cedula, mientras que la BD usa id_usuario entero. Mientras GRB no exponga
 *   esa correspondencia, se usa un id de residente de demostracion.
 */
public class VerReservaController {

    // TODO(GRB): reemplazar por el id del usuario autenticado (SesionUsuario).
    private static final int ID_RESIDENTE_ACTUAL = 2; // 'Carlos Residente' en seed.sql

    @FXML private VBox panelPrincipal;
    @FXML private VBox panelObservacion;
    @FXML private TextArea txtObservacion;

    @FXML private TableView<Reserva> tablaReservas;
    @FXML private TableColumn<Reserva, String> colEspacio;
    @FXML private TableColumn<Reserva, String> colFecha;
    @FXML private TableColumn<Reserva, String> colHoraInicio;
    @FXML private TableColumn<Reserva, String> colHoraFin;
    @FXML private TableColumn<Reserva, String> colEstado;
    @FXML private TableColumn<Reserva, Void> colOpciones;

    private final IServicioReservas servicioReservas = new ServicioReservasImpl();

    private Reserva reservaSeleccionadaParaObservacion = null;
    private final Set<Integer> reservasConObservacion = new HashSet<>();
    private final ObservableList<Reserva> datos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // 1. Columnas (todas basadas en texto, consistente con el modelo Reserva)
        colEspacio.setCellValueFactory(new PropertyValueFactory<>("nombreEspacio"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaReserva"));
        colHoraInicio.setCellValueFactory(new PropertyValueFactory<>("horaInicio"));
        colHoraFin.setCellValueFactory(new PropertyValueFactory<>("horaFin"));
        colEstado.setCellValueFactory(cd -> new SimpleStringProperty(
                cd.getValue().getEstado() != null ? cd.getValue().getEstado().name() : ""));

        configurarColumnaEstado();
        configurarColumnaOpciones();

        // 2. Cargar datos reales desde la BD
        tablaReservas.setItems(datos);
        cargarReservas();
    }

    private void cargarReservas() {
        datos.clear();
        List<Reserva> reservas = servicioReservas.listarReservasPorUsuario(ID_RESIDENTE_ACTUAL);
        datos.addAll(reservas);
        // Marcar las que ya tienen observaciones para deshabilitar el boton.
        reservasConObservacion.clear();
        for (Reserva r : reservas) {
            if (!r.getObservaciones().isEmpty()) {
                reservasConObservacion.add(r.getId());
            }
        }
        tablaReservas.refresh();
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
                "Cancelada por el residente");
        if (ok) {
            cargarReservas();
        }
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
            String texto = txtObservacion.getText();
            // El autor de la observacion es el residente en sesion (ver nota de integracion).
            servicioReservas.registrarObservacion(
                    reservaSeleccionadaParaObservacion.getId(),
                    ID_RESIDENTE_ACTUAL,
                    texto);
            reservasConObservacion.add(reservaSeleccionadaParaObservacion.getId());
        }
        cancelarObservacion(event);
        cargarReservas();
    }
}
