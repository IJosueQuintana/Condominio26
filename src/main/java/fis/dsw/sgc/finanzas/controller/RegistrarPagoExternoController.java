package fis.dsw.sgc.finanzas.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class RegistrarPagoExternoController implements Initializable {

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnLimpiar;

    @FXML
    private ComboBox<String> cmbMotivo;

    @FXML
    private ComboBox<String> cmbServiciosBasicos;

    @FXML
    private TextArea cmpDescripcion;

    @FXML
    private DatePicker dpFechaGasto;

    @FXML
    private Label lblIconoDescripcion;

    @FXML
    private Label lblIconoFecha;

    @FXML
    private Label lblIconoMotivo;

    @FXML
    private Label lblIconoServicioBasico;

    @FXML
    private Label lblIconoTipoGasto;

    @FXML
    private Label lblIconoTitulo;

    @FXML
    private Label lblMensajeEstado;


    @FXML
    private Label lblIconoValor;

    @FXML
    private Label lblIconoValorDidactico;

    @FXML
    private HBox opcionesServicioBasico;

    @FXML
    private TextField txtValor;

    private String[] motivosGastos = {"SERVICIO BÁSICO", "SUELDOS", "OTROS"};
    private String[] serviciosBasicos = {"AGUA", "LUZ", "TELÉFONO", "INTERNET"};
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmbMotivo.getItems().addAll(motivosGastos);
        cmbServiciosBasicos.getItems().addAll(serviciosBasicos);

        opcionesServicioBasico.setVisible(false);
        opcionesServicioBasico.setManaged(false);

        FontIcon icon = new FontIcon("fa-university");
        icon.getStyleClass().add("titleIcon");
        lblIconoTitulo.setGraphic(icon);
        lblIconoTitulo.setText(null);

        FontIcon icon2 = new FontIcon("fa-calendar");
        icon2.getStyleClass().add("formIcon");
        lblIconoFecha.setText(null);
        lblIconoFecha.setGraphic(icon2);

        FontIcon icon3 = new FontIcon("fa-coffee");
        icon3.getStyleClass().add("formIcon");
        lblIconoMotivo.setText(null);
        lblIconoMotivo.setGraphic(icon3);

        FontIcon icon4 = new FontIcon("fa-usd");
        icon4.getStyleClass().add("formIcon");
        lblIconoValor.setText(null);
        lblIconoValor.setGraphic(icon4);

        FontIcon icon5 = new FontIcon("fa-shower");
        icon5.getStyleClass().add("formIcon");

        lblIconoServicioBasico.setText(null);
        lblIconoServicioBasico.setGraphic(icon5);

        FontIcon icon6 = new FontIcon("fa-user-o");
        icon6.getStyleClass().add("playableIcon");

        FontIcon icon7 = new FontIcon("fa-tint");
        icon7.getStyleClass().add("playableIcon");

        FontIcon icon8 = new FontIcon("fa-suitcase");
        icon8.getStyleClass().add("playableIcon");

        FontIcon icon9 = new FontIcon("fa-angellist");
        icon9.getStyleClass().add("playableIcon");

        FontIcon icon10 = new FontIcon("fa-warning");
        icon10.getStyleClass().add("playableIcon");

        FontIcon icon11 = new FontIcon("fa-window-close");
        icon11.getStyleClass().add("playableIcon");

        cmbMotivo.valueProperty().addListener((obs, old, nuevo) -> {
            if (Objects.equals(nuevo, "SERVICIO BÁSICO")) {
                cmpDescripcion.setEditable(false);
                opcionesServicioBasico.setVisible(true);
                opcionesServicioBasico.setManaged(true);
                lblIconoTipoGasto.setText(null);
                lblIconoTipoGasto.setGraphic(icon7);

            }

            if (Objects.equals(nuevo, "SUELDOS")) {
                lblIconoTipoGasto.setText(null);
                lblIconoTipoGasto.setGraphic(icon6);

            }

            if(Objects.equals(nuevo, "OTROS")){
                lblIconoTipoGasto.setText(null);
                lblIconoTipoGasto.setGraphic(icon8);
            }
        });

        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*(\\.\\d{0,2})?")) {
                return change;
            }
            return null;
        });

        txtValor.setTextFormatter(formatter);

        txtValor.textProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue.isBlank()) {
                return;
            }

            try {
                double valor = Double.parseDouble(newValue);

                if (valor < 100) {
                    lblIconoValorDidactico.setText(null);
                    lblIconoValorDidactico.setGraphic(icon9);
                    btnGuardar.setDisable(false);
                } else {
                    // Hacer otra cosa
                    lblIconoValorDidactico.setText(null);
                    lblIconoValorDidactico.setGraphic(icon10);
                    btnGuardar.setDisable(false);
                }

            } catch (NumberFormatException e) {
                lblMensajeEstado.setText("El valor tiene que ser un número ");
                btnGuardar.setDisable(true);
                lblIconoValorDidactico.setText(null);
                lblIconoValorDidactico.setGraphic(icon11);
                lblMensajeEstado.getStyleClass().setAll("message-error");
            }
        });

    }
}
