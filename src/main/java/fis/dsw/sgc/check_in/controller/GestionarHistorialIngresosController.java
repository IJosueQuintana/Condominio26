package fis.dsw.sgc.check_in.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class GestionarHistorialIngresosController {

    @FXML private VBox panelPrincipal;
    @FXML private VBox panelDetalle; // Este será nuestro modal flotante

    @FXML private TextField txtBusquedaPersona;
    @FXML private ChoiceBox<String> cbTipoIngreso;
    @FXML private TextField txtPlaca;
    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;
    @FXML private Label lblMensaje;

    @FXML private TableView<RegistroEntradaFila> tablaHistorial;
    @FXML private TableColumn<RegistroEntradaFila, String> colFecha;
    @FXML private TableColumn<RegistroEntradaFila, String> colHora;
    @FXML private TableColumn<RegistroEntradaFila, String> colTipo;
    @FXML private TableColumn<RegistroEntradaFila, String> colPersona;
    @FXML private TableColumn<RegistroEntradaFila, Void> colDetalle;

    @FXML private Label lblDetalleTipo;
    @FXML private Label lblDetallePersona;
    @FXML private Label lblDetalleFechaHora;
    @FXML private Label lblDetalleMotivo;
    @FXML private Label lblDetallePlaca;
    @FXML private Label lblDetalleInfoAdicional;

    private final ObservableList<RegistroEntradaFila> historialCompleto = FXCollections.observableArrayList();
    private final ObservableList<RegistroEntradaFila> historialFiltrado = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        cbTipoIngreso.setItems(FXCollections.observableArrayList("Todos", "Residente", "Externa"));
        cbTipoIngreso.getSelectionModel().selectFirst();

        cargarDatosDemo();

        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colPersona.setCellValueFactory(new PropertyValueFactory<>("persona"));

        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colTipo.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String tipo, boolean empty) {
                super.updateItem(tipo, empty);
                if (empty || tipo == null) {
                    setGraphic(null);
                    return;
                }
                Label badge = new Label(tipo);
                badge.getStyleClass().addAll("badge-estado",
                        tipo.equals("Residente") ? "badge-tipo-residente" : "badge-tipo-externa");
                setGraphic(badge);
            }
        });

        colDetalle.setCellFactory(col -> new TableCell<>() {
            private final Button btnDetalle = new Button("Ver detalle");
            {
                btnDetalle.getStyleClass().addAll("btn-accion-tabla", "btn-accion-detalle");
                btnDetalle.setOnAction(e -> mostrarDetalle(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnDetalle);
            }
        });

        tablaHistorial.setItems(historialFiltrado);
        historialFiltrado.setAll(historialCompleto);
    }

    @FXML
    void buscarHistorial(ActionEvent event) {
        if (dpFechaInicio.getValue() != null && dpFechaFin.getValue() != null
                && dpFechaInicio.getValue().isAfter(dpFechaFin.getValue())) {
            mostrarError("La fecha 'Desde' no puede ser posterior a la fecha 'Hasta'.");
            return;
        }

        String persona = txtBusquedaPersona.getText() == null ? "" : txtBusquedaPersona.getText().trim().toLowerCase();
        String placa = txtPlaca.getText() == null ? "" : txtPlaca.getText().trim().toLowerCase();
        String tipo = cbTipoIngreso.getValue();

        List<RegistroEntradaFila> resultado = new ArrayList<>();
        for (RegistroEntradaFila fila : historialCompleto) {
            boolean coincide = true;
            if (!persona.isEmpty() && !fila.getPersona().toLowerCase().contains(persona)) coincide = false;
            if (!placa.isEmpty() && !fila.getPlaca().toLowerCase().contains(placa)) coincide = false;
            if (tipo != null && !tipo.equals("Todos") && !fila.getTipo().equals(tipo)) coincide = false;

            LocalDate fechaFila = fila.getFechaComoLocalDate();
            if (dpFechaInicio.getValue() != null && fechaFila.isBefore(dpFechaInicio.getValue())) coincide = false;
            if (dpFechaFin.getValue() != null && fechaFila.isAfter(dpFechaFin.getValue())) coincide = false;

            if (coincide) resultado.add(fila);
        }

        historialFiltrado.setAll(resultado);
        if (resultado.isEmpty()) {
            mostrarError("No se encontraron registros para los criterios ingresados.");
        } else {
            mostrarInfo(resultado.size() + " registro(s) encontrado(s).");
        }
    }

    private void mostrarDetalle(RegistroEntradaFila fila) {
        lblDetalleTipo.setText("Tipo de ingreso: " + fila.getTipo());
        lblDetallePersona.setText("Persona: " + fila.getPersona());
        lblDetalleFechaHora.setText("Fecha y hora: " + fila.getFecha() + " " + fila.getHora());
        lblDetalleMotivo.setText("Motivo: " + fila.getMotivo());
        lblDetallePlaca.setText("Placa: " + (fila.getPlaca().isEmpty() ? "No registrada" : fila.getPlaca()));
        lblDetalleInfoAdicional.setText("Información Adicional: " + fila.getInfoAdicional());

        // Mostrar el modal flotante
        panelDetalle.setVisible(true);
    }

    @FXML
    void cerrarDetalle(ActionEvent event) {
        // Ocultar el modal flotante
        panelDetalle.setVisible(false);
    }

    @FXML
    void generarReporte(ActionEvent event) {
        // Acción para el nuevo botón
        mostrarInfo("Generando reporte de los ingresos en pantalla");
    }

    private void mostrarInfo(String mensaje) {
        lblMensaje.setText(mensaje);
        lblMensaje.getStyleClass().setAll("message-label", "message-info");
    }

    private void mostrarError(String mensaje) {
        lblMensaje.setText(mensaje);
        lblMensaje.getStyleClass().setAll("message-label", "message-error");
    }

    private void cargarDatosDemo() {
        historialCompleto.add(new RegistroEntradaFila("18/07/2026", "08:15", "Residente", "María Fernanda Cárdenas", "Ingreso regular", "", "Sin observaciones"));
        historialCompleto.add(new RegistroEntradaFila("18/07/2026", "09:40", "Externa", "Carlos Pérez (Proveedor)", "Entrega de paquetería", "PBX-2210", "Dejó 2 cajas en garita"));
        historialCompleto.add(new RegistroEntradaFila("17/07/2026", "19:05", "Externa", "Ana Gómez (Visita)", "Visita social", "", "Autorizado vía llamada"));
    }

    public static class RegistroEntradaFila {
        private final String fecha;
        private final String hora;
        private final String tipo;
        private final String persona;
        private final String motivo;
        private final String placa;
        private final String infoAdicional;

        public RegistroEntradaFila(String fecha, String hora, String tipo, String persona, String motivo, String placa, String infoAdicional) {
            this.fecha = fecha;
            this.hora = hora;
            this.tipo = tipo;
            this.persona = persona;
            this.motivo = motivo;
            this.placa = placa;
            this.infoAdicional = infoAdicional;
        }

        public String getFecha() { return fecha; }
        public String getHora() { return hora; }
        public String getTipo() { return tipo; }
        public String getPersona() { return persona; }
        public String getMotivo() { return motivo; }
        public String getPlaca() { return placa; }
        public String getInfoAdicional() { return infoAdicional; }

        public LocalDate getFechaComoLocalDate() {
            String[] partes = fecha.split("/");
            return LocalDate.of(Integer.parseInt(partes[2]), Integer.parseInt(partes[1]), Integer.parseInt(partes[0]));
        }
    }
}