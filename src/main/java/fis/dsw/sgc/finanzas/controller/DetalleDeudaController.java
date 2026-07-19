package fis.dsw.sgc.finanzas.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// Ventana de mas opciones sobre una deuda
public class DetalleDeudaController {

    @FXML private Label lblId;
    @FXML private Label lblMotivo;
    @FXML private Label lblValor;
    @FXML private Label lblFecha;
    @FXML private Label lblEstado;
    @FXML private Label lblDescripcion;
    @FXML private DatePicker dpNuevaFecha;
    @FXML private Button btnValidar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Label lblMensaje;

    private ConsultarDeudasController.DeudaFila deuda;
    private Runnable alCerrar;
    private String resultadoAccion = "";
    private String estadoActualizado;
    private String fechaActualizada;

    public void setDeuda(ConsultarDeudasController.DeudaFila deuda, Runnable alCerrar) {
        this.deuda = deuda;
        this.alCerrar = alCerrar;
        if (deuda == null) {
            return;
        }
        lblId.setText("ID: " + deuda.getIdDeuda());
        lblMotivo.setText("Motivo: " + deuda.getMotivo());
        lblValor.setText("Valor: " + deuda.getValor());
        lblFecha.setText("Fecha máx.: " + deuda.getFechaMaximaPago());
        lblEstado.setText("Estado: " + deuda.getEstado());
        lblDescripcion.setText("Descripción: " + deuda.getDescripcion());
        btnValidar.setVisible("EN PROCESO".equalsIgnoreCase(deuda.getEstado()));
        btnValidar.setManaged(btnValidar.isVisible());
        setMensaje("Elija una acción sobre esta deuda.", "message-info");
    }

    public String getResultadoAccion() {
        return resultadoAccion;
    }

    public String getEstadoActualizado() {
        return estadoActualizado;
    }

    public String getFechaActualizada() {
        return fechaActualizada;
    }

    @FXML
    void validarPago(ActionEvent event) {
        if (deuda == null) {
            return;
        }
        if (!"EN PROCESO".equalsIgnoreCase(deuda.getEstado())) {
            setMensaje("Solo se valida si la deuda está EN PROCESO.", "message-error");
            return;
        }
        // Se marca pagada sin pedir id de pago
        estadoActualizado = "PAGADA";
        resultadoAccion = "VALIDADA";
        setMensaje("Pago registrado exitosamente.", "message-success");
        notificarYCerrar(event);
    }

    @FXML
    void modificarFecha(ActionEvent event) {
        LocalDate nueva = dpNuevaFecha.getValue();
        if (nueva == null) {
            setMensaje("Seleccione la nueva fecha máxima de pago.", "message-error");
            return;
        }
        if (!nueva.isAfter(LocalDate.now())) {
            setMensaje("La nueva fecha máxima de pago debe ser mayor a la fecha actual.", "message-error");
            return;
        }
        if (deuda != null && deuda.getFechaMaximaPago() != null) {
            try {
                LocalDate actual = LocalDate.parse(deuda.getFechaMaximaPago());
                if (!nueva.isAfter(actual)) {
                    setMensaje(
                            "La nueva fecha máxima de pago debe ser mayor a la fecha de pago actual de la deuda.",
                            "message-error");
                    return;
                }
            } catch (Exception ignored) {
                // Si el formato demo no parsea, solo se valida contra hoy
            }
        }
        fechaActualizada = nueva.format(DateTimeFormatter.ISO_LOCAL_DATE);
        resultadoAccion = "FECHA";
        setMensaje("Fecha máxima de pago modificada con éxito.", "message-success");
        notificarYCerrar(event);
    }

    @FXML
    void eliminar(ActionEvent event) {
        resultadoAccion = "ELIMINADA";
        setMensaje("Deuda Eliminada Exitosamente", "message-success");
        notificarYCerrar(event);
    }

    @FXML
    void cerrar(ActionEvent event) {
        cerrarVentana(event);
    }

    private void notificarYCerrar(ActionEvent event) {
        if (alCerrar != null) {
            alCerrar.run();
        }
        cerrarVentana(event);
    }

    private void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void setMensaje(String texto, String estilo) {
        lblMensaje.getStyleClass().removeAll("message-info", "message-success", "message-error");
        if (!lblMensaje.getStyleClass().contains("message-label")) {
            lblMensaje.getStyleClass().add("message-label");
        }
        lblMensaje.getStyleClass().add(estilo);
        lblMensaje.setText(texto);
    }
}
