package fis.dsw.sgc.finanzas.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

// Controlador de la vista Registrar pago externo
public class pagarDeudaController {

    @FXML private TextField txtIdDeuda;
    @FXML private TextField txtCedula;
    @FXML private TextField txtMonto;
    @FXML private ComboBox<String> cmbMetodo;
    @FXML private TextField txtReferencia;
    @FXML private Button btnRegistrar;
    @FXML private Button btnLimpiar;
    @FXML private Label lblMensaje;

    @FXML
    public void initialize() {
        cmbMetodo.setItems(FXCollections.observableArrayList(
                "EFECTIVO",
                "TRANSFERENCIA",
                "TARJETA"
        ));
        cmbMetodo.getSelectionModel().selectFirst();
        cmbMetodo.valueProperty().addListener((obs, oldV, newV) -> actualizarReferencia(newV));
        actualizarReferencia(cmbMetodo.getValue());
        setMensaje("Ingrese el ID de la deuda y complete los datos del pago.", "message-info");
    }

    @FXML
    void registrar(ActionEvent event) {
        String idDeuda = texto(txtIdDeuda);
        String cedula = texto(txtCedula);
        String monto = texto(txtMonto);
        String metodo = cmbMetodo.getValue() == null ? "" : cmbMetodo.getValue();
        String ref = texto(txtReferencia);

        if (idDeuda.isEmpty()) {
            setMensaje("Debe ingresar el ID de la deuda.", "message-error");
            return;
        }

        // IDs de prueba mientras no haya base de datos
        if (idDeuda.equalsIgnoreCase("DEU-404") || idDeuda.equalsIgnoreCase("NO-EXISTE")) {
            setMensaje("No existe una deuda con el identificador proporcionado.", "message-error");
            return;
        }
        if (idDeuda.equalsIgnoreCase("DEU-PAGADA") || idDeuda.toUpperCase().endsWith("-PAGADA")) {
            setMensaje("Esta deuda ya ha sido pagada.", "message-error");
            return;
        }

        if (cedula.isEmpty()) {
            setMensaje("Debe ingresar la cédula del residente.", "message-error");
            return;
        }
        if (!cedula.matches("\\d{5,13}")) {
            setMensaje("Cédula inválida. Ingrese solo dígitos (5 a 13).", "message-error");
            return;
        }
        if (monto.isEmpty()) {
            setMensaje("Debe ingresar el monto del pago.", "message-error");
            return;
        }
        if (!monto.matches("\\d+(\\.\\d{1,2})?")) {
            setMensaje("Monto inválido. Use un número con hasta 2 decimales.", "message-error");
            return;
        }
        if (("TRANSFERENCIA".equals(metodo) || "TARJETA".equals(metodo)) && ref.isEmpty()) {
            setMensaje("Para " + metodo.toLowerCase() + " indique el número de comprobante (referencia).",
                    "message-error");
            return;
        }

        setMensaje("Pago registrado exitosamente. Deuda " + idDeuda + " → estado PAGADA.",
                "message-success");
    }

    @FXML
    void limpiar(ActionEvent event) {
        txtIdDeuda.clear();
        txtCedula.clear();
        txtMonto.clear();
        txtReferencia.clear();
        cmbMetodo.getSelectionModel().selectFirst();
        actualizarReferencia(cmbMetodo.getValue());
        setMensaje("Formulario listo para un nuevo registro.", "message-info");
    }

    private void actualizarReferencia(String metodo) {
        boolean efectivo = "EFECTIVO".equals(metodo);
        txtReferencia.setDisable(efectivo);
        if (efectivo) {
            txtReferencia.clear();
            txtReferencia.setPromptText("No aplica en efectivo");
        } else {
            txtReferencia.setPromptText("Nº comprobante (transferencia / tarjeta)");
        }
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
