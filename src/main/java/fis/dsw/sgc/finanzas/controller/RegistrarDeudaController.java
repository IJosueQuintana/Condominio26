package fis.dsw.sgc.finanzas.controller;

import fis.dsw.sgc.finanzas.dto.NuevaDeudaDTO;
import fis.dsw.sgc.finanzas.service.DeudaServiceImpl;
import fis.dsw.sgc.finanzas.service.IDeudaService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

// Controlador de la vista Registrar deuda
public class RegistrarDeudaController {

    @FXML private TextField txtCedula;
    @FXML private ComboBox<String> cmbMotivo;
    @FXML private DatePicker dpFechaMax;
    @FXML private TextField txtValor;
    @FXML private TextArea txtDescripcion;
    @FXML private Label lblMensaje;

    private final IDeudaService deudaService = new DeudaServiceImpl();

    @FXML
    public void initialize() {
        cmbMotivo.setItems(FXCollections.observableArrayList("ALICUOTA", "MULTA", "RESERVA"));
        cmbMotivo.getSelectionModel().selectFirst();
        dpFechaMax.setValue(LocalDate.now().plusDays(30));
        setMensaje("Complete los datos de la deuda y pulse Registrar deuda.", "message-info");
    }

    @FXML
    void registrar(ActionEvent event) {
        String cedula = texto(txtCedula);
        String motivo = cmbMotivo.getValue() == null ? "" : cmbMotivo.getValue();
        LocalDate fecha = dpFechaMax.getValue();
        String valor = texto(txtValor);
        String desc = txtDescripcion.getText() == null ? "" : txtDescripcion.getText().trim();

        if (cedula.isEmpty()) {
            setMensaje("Debe ingresar la cédula del residente.", "message-error");
            return;
        }
        if (!cedula.matches("\\d+")) {
            setMensaje("La cédula solo debe contener números.", "message-error");
            return;
        }
        if (fecha == null) {
            setMensaje("Seleccione la fecha máxima de pago.", "message-error");
            return;
        }
        if (valor.isEmpty() || !valor.matches("\\d+(\\.\\d{1,2})?")) {
            setMensaje("Valor inválido. Use un número con hasta 2 decimales.", "message-error");
            return;
        }
        if (desc.isEmpty()) {
            setMensaje("Ingrese la descripción de la deuda.", "message-error");
            return;
        }

        try {
            NuevaDeudaDTO nuevaDeuda = new NuevaDeudaDTO(cedula, motivo, fecha, desc, Double.parseDouble(valor));
            deudaService.registrarDeuda(nuevaDeuda);
            String motivoTxt = motivo.equalsIgnoreCase("ALICUOTA") ? "alícuota"
                    : motivo.equalsIgnoreCase("MULTA") ? "multa" : "reserva";
            setMensaje(
                    "Deuda por motivo de " + motivoTxt + " con el valor de " + valor
                            + " registrada exitosamente para el residente.",
                    "message-success");
        } catch (RuntimeException ex) {
            setMensaje(ex.getMessage(), "message-error");
        }
    }

    @FXML
    void limpiar(ActionEvent event) {
        txtCedula.clear();
        txtValor.clear();
        txtDescripcion.clear();
        cmbMotivo.getSelectionModel().selectFirst();
        dpFechaMax.setValue(LocalDate.now().plusDays(30));
        setMensaje("Formulario listo para una nueva deuda.", "message-info");
    }

    private void setMensaje(String texto, String estilo) {
        lblMensaje.getStyleClass().removeAll("message-info", "message-success", "message-error");
        if (!lblMensaje.getStyleClass().contains("message-label")) {
            lblMensaje.getStyleClass().add("message-label");
        }
        lblMensaje.getStyleClass().add(estilo);
        lblMensaje.setText(texto);
    }

    private static String texto(TextField field) {
        return field.getText() == null ? "" : field.getText().trim();
    }
}
