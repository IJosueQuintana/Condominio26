package fis.dsw.sgc.finanzas.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

// Ventana emergente que simula el pago con tarjeta
public class SimularPagoTarjetaController {

    @FXML private Label lblDeuda;
    @FXML private TextField txtNumero;
    @FXML private TextField txtTitular;
    @FXML private TextField txtVence;
    @FXML private PasswordField txtCvv;
    @FXML private Label lblMensaje;

    private boolean aceptado;
    private boolean rechazado;

    public void setIdDeuda(String idDeuda) {
        lblDeuda.setText("Deuda: " + (idDeuda == null ? "" : idDeuda));
    }

    public boolean isPagoAceptado() {
        return aceptado;
    }

    public boolean isPagoRechazado() {
        return rechazado;
    }

    @FXML
    void aceptar(ActionEvent event) {
        String num = texto(txtNumero);
        String tit = texto(txtTitular);
        String ven = texto(txtVence);
        String cvv = txtCvv.getText() == null ? "" : txtCvv.getText().trim();

        if (!num.matches("\\d{13,19}")) {
            setMensaje("Número de tarjeta inválido.", "message-error");
            return;
        }
        if (tit.isEmpty()) {
            setMensaje("Ingrese el nombre del titular.", "message-error");
            return;
        }
        if (!ven.matches("\\d{2}/\\d{2}")) {
            setMensaje("Vencimiento inválido. Use MM/AA.", "message-error");
            return;
        }
        if (!cvv.matches("\\d{3,4}")) {
            setMensaje("CVV inválido.", "message-error");
            return;
        }

        aceptado = true;
        rechazado = false;
        cerrar(event);
    }

    @FXML
    void rechazar(ActionEvent event) {
        aceptado = false;
        rechazado = true;
        cerrar(event);
    }

    @FXML
    void cancelar(ActionEvent event) {
        aceptado = false;
        rechazado = false;
        cerrar(event);
    }

    private void cerrar(ActionEvent event) {
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

    private static String texto(TextField field) {
        return field.getText() == null ? "" : field.getText().trim();
    }
}
