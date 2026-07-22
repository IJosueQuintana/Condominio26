package fis.dsw.sgc.finanzas.controller;

import fis.dsw.sgc.finanzas.dao.GastoDAOImpl;
import fis.dsw.sgc.finanzas.dto.NuevoGastoDTO;
import fis.dsw.sgc.finanzas.service.GastoServiceImpl;
import fis.dsw.sgc.finanzas.service.IGastoService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class RegistrarPagoExternoController implements Initializable {

    private final IGastoService gastoService;

    public RegistrarPagoExternoController() {
        this(new GastoServiceImpl(new GastoDAOImpl()));
    }

    public RegistrarPagoExternoController(IGastoService gastoService) {
        this.gastoService = gastoService;
    }

    @FXML private Button btnGuardar;
    @FXML private Button btnLimpiar;
    @FXML private ComboBox<String> cmbMotivo;
    @FXML private ComboBox<String> cmbServiciosBasicos;
    @FXML private TextArea cmpDescripcion;
    @FXML private DatePicker dpFechaGasto;
    @FXML private Label lblIconoDescripcion;
    @FXML private Label lblIconoFecha;
    @FXML private Label lblIconoMotivo;
    @FXML private Label lblIconoServicioBasico;
    @FXML private Label lblIconoTipoGasto;
    @FXML private Label lblIconoTitulo;
    @FXML private Label lblMensajeEstado;
    @FXML private Label lblIconoValor;
    @FXML private Label lblIconoValorDidactico;
    @FXML private HBox opcionesServicioBasico;
    @FXML private TextField txtValor;

    private final String[] motivosGastos = {"SERVICIO BÁSICO", "SUELDOS", "OTROS"};
    private final String[] serviciosBasicos = {"AGUA", "LUZ", "TELÉFONO", "INTERNET"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmbMotivo.getItems().addAll(motivosGastos);
        cmbServiciosBasicos.getItems().addAll(serviciosBasicos);
        btnGuardar.setOnAction(this::guardarPagoExterno);
        btnLimpiar.setOnAction(this::limpiarFormulario);

        opcionesServicioBasico.setVisible(false);
        opcionesServicioBasico.setManaged(false);
        lblMensajeEstado.setVisible(false);

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
                cmpDescripcion.setDisable(true);
                opcionesServicioBasico.setVisible(true);
                opcionesServicioBasico.setManaged(true);
                lblIconoTipoGasto.setText(null);
                lblIconoTipoGasto.setGraphic(icon7);
                cmpDescripcion.setText(null);
            }

            if (Objects.equals(nuevo, "SUELDOS")) {
                cmpDescripcion.setDisable(false);
                lblIconoTipoGasto.setText(null);
                lblIconoTipoGasto.setGraphic(icon6);
                opcionesServicioBasico.setVisible(false);
                opcionesServicioBasico.setManaged(false);
            }

            if (Objects.equals(nuevo, "OTROS")) {
                cmpDescripcion.setDisable(false);
                lblIconoTipoGasto.setText(null);
                lblIconoTipoGasto.setGraphic(icon8);
                opcionesServicioBasico.setVisible(false);
                opcionesServicioBasico.setManaged(false);
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
                lblIconoValorDidactico.setGraphic(null);
                return;
            }

            try {
                double valor = Double.parseDouble(newValue);
                lblIconoValorDidactico.setText(null);
                lblIconoValorDidactico.setGraphic(valor < 100 ? icon9 : icon10);
                btnGuardar.setDisable(false);
            } catch (NumberFormatException e) {
                setMensaje("El valor tiene que ser un número.", "message-error");
                btnGuardar.setDisable(true);
                lblIconoValorDidactico.setText(null);
                lblIconoValorDidactico.setGraphic(icon11);
            }
        });
    }

    @FXML
    void guardarPagoExterno(ActionEvent event) {
        LocalDate fecha = dpFechaGasto.getValue();
        String motivoVista = valorCombo(cmbMotivo);
        String descripcion = obtenerDescripcion(motivoVista);
        String valorTexto = txtValor.getText() == null ? "" : txtValor.getText().trim();

        if (fecha == null) {
            setMensaje("La fecha del gasto es obligatoria.", "message-error");
            return;
        }
        if (motivoVista.isEmpty()) {
            setMensaje("El motivo del gasto es obligatorio.", "message-error");
            return;
        }
        if (descripcion.isEmpty()) {
            setMensaje("La descripción del gasto es obligatoria.", "message-error");
            return;
        }
        if (valorTexto.isEmpty()) {
            setMensaje("El valor del gasto es obligatorio.", "message-error");
            return;
        }

        try {
            Double valor = Double.valueOf(valorTexto);
            String motivo = normalizar(motivoVista);
            String descripcionDto = motivo.equals("SERVICIO BASICO") ? normalizar(descripcion) : descripcion;
            NuevoGastoDTO dto = new NuevoGastoDTO(fecha, valor, motivo, descripcionDto);
            gastoService.registrarPagosCondominio(dto);
            setMensaje("Pago externo guardado exitosamente.", "message-success");
            limpiarCampos();
        } catch (RuntimeException ex) {
            setMensaje(ex.getMessage(), "message-error");
        }
    }

    @FXML
    void limpiarFormulario(ActionEvent event) {
        limpiarCampos();
        setMensaje("Formulario listo para registrar un nuevo gasto.", "message-info");
    }

    private String obtenerDescripcion(String motivoVista) {
        if (Objects.equals(motivoVista, "SERVICIO BÁSICO")) {
            return valorCombo(cmbServiciosBasicos);
        }
        return cmpDescripcion.getText() == null ? "" : cmpDescripcion.getText().trim();
    }

    private void limpiarCampos() {
        dpFechaGasto.setValue(null);
        cmbMotivo.getSelectionModel().clearSelection();
        cmbServiciosBasicos.getSelectionModel().clearSelection();
        cmpDescripcion.clear();
        cmpDescripcion.setDisable(false);
        txtValor.clear();
        lblIconoTipoGasto.setGraphic(null);
        lblIconoValorDidactico.setGraphic(null);
        opcionesServicioBasico.setVisible(false);
        opcionesServicioBasico.setManaged(false);
    }

    private void setMensaje(String texto, String estilo) {
        lblMensajeEstado.getStyleClass().removeAll("message-info", "message-success", "message-error");
        if (!lblMensajeEstado.getStyleClass().contains("message-label")) {
            lblMensajeEstado.getStyleClass().add("message-label");
        }
        lblMensajeEstado.getStyleClass().add(estilo);
        lblMensajeEstado.setText(texto);
        lblMensajeEstado.setVisible(true);
    }

    private static String valorCombo(ComboBox<String> combo) {
        return combo.getValue() == null ? "" : combo.getValue().trim();
    }

    private static String normalizar(String texto) {
        String sinTildes = Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return sinTildes.toUpperCase().trim();
    }
}
