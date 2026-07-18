package fis.dsw.sgc.finanzas.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

// Controlador de la vista Validar pago
public class ValidarPagoController {

    @FXML private TextField txtIdDeuda;
    @FXML private TextField txtIdPago;
    @FXML private TextField txtCedula;
    @FXML private Label lblMensaje;

    @FXML
    public void initialize() {
        setMensaje("Ingrese el ID de la deuda y del pago a validar.", "message-info");
    }

    @FXML
    void validar(ActionEvent event) {
        String deuda = t(txtIdDeuda);
        String pago = t(txtIdPago);
        String cedula = t(txtCedula);

        if (deuda.isEmpty()) {
            setMensaje("Debe ingresar el ID de la deuda.", "message-error");
            return;
        }
        if (pago.isEmpty()) {
            setMensaje("Debe ingresar el ID del pago.", "message-error");
            return;
        }
        if (cedula.isEmpty()) {
            setMensaje("Ingrese la cédula del residente.", "message-error");
            return;
        }
        if (!cedula.matches("\\d{5,13}")) {
            setMensaje("Cédula inválida. Ingrese solo dígitos (5 a 13).", "message-error");
            return;
        }

        // IDs de prueba mientras no haya base de datos
        if (deuda.equalsIgnoreCase("DEU-404") || pago.equalsIgnoreCase("PAG-404")) {
            setMensaje("No existe una deuda o pago con el identificador proporcionado.", "message-error");
            return;
        }
        if (deuda.equalsIgnoreCase("DEU-PAGADA") || pago.toUpperCase().endsWith("-OK")) {
            setMensaje("Esta deuda ya ha sido pagada.", "message-error");
            return;
        }

        setMensaje("Pago " + pago + " validado exitosamente. Deuda " + deuda + " → PAGADA.",
                "message-success");
    }

    @FXML
    void rechazar(ActionEvent event) {
        String deuda = t(txtIdDeuda);
        String pago = t(txtIdPago);

        if (pago.isEmpty()) {
            setMensaje("Indique el ID del pago a rechazar.", "message-error");
            return;
        }
        if (deuda.isEmpty()) {
            setMensaje("Indique también el ID de la deuda asociada.", "message-error");
            return;
        }

        setMensaje("Pago " + pago + " rechazado. La deuda " + deuda + " permanece EN PROCESO.",
                "message-error");
    }

    @FXML
    void limpiar(ActionEvent event) {
        txtIdDeuda.clear();
        txtIdPago.clear();
        txtCedula.clear();
        setMensaje("Formulario listo para una nueva validación.", "message-info");
    }

    private void setMensaje(String texto, String estilo) {
        lblMensaje.getStyleClass().removeAll("message-info", "message-success", "message-error");
        if (!lblMensaje.getStyleClass().contains("message-label")) {
            lblMensaje.getStyleClass().add("message-label");
        }
        lblMensaje.getStyleClass().add(estilo);
        lblMensaje.setText(texto);
    }

    private static String t(TextField f) {
        return f.getText() == null ? "" : f.getText().trim();
    }
}
