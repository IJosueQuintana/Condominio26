package fis.dsw.sgc.finanzas.controller;

import fis.dsw.sgc.finanzas.dto.EntidadBancariaDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.Locale;
import java.util.regex.Pattern;

// Controlador de la vista Configuración financiera (valor de alícuotas + entidades bancarias)
public class ConfiguracionFinancieraController {

    private static final Pattern VALOR_VALIDO = Pattern.compile("\\d+(\\.\\d{1,2})?");
    private static final Pattern CEDULA_VALIDA = Pattern.compile("\\d{5,13}");
    private static final Pattern CORREO_VALIDO = Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    @FXML private Label lblValorActualAlicuota;
    @FXML private TextField txtNuevoValorAlicuota;
    @FXML private Button btnGuardarAlicuota;
    @FXML private Label lblMensajeAlicuota;

    @FXML private TextField txtNombreEntidad;
    @FXML private TextField txtNumeroCuenta;
    @FXML private TextField txtCedulaTitular;
    @FXML private ComboBox<String> cbTipoCuenta;
    @FXML private TextField txtCorreoTitular;
    @FXML private Button btnRegistrarEntidad;
    @FXML private Button btnLimpiarEntidad;
    @FXML private Label lblMensajeEntidad;

    @FXML private TableView<EntidadBancariaDTO> tablaEntidades;
    @FXML private TableColumn<EntidadBancariaDTO, String> colNombreEntidad;
    @FXML private TableColumn<EntidadBancariaDTO, String> colNumeroCuenta;
    @FXML private TableColumn<EntidadBancariaDTO, String> colTipoCuenta;
    @FXML private TableColumn<EntidadBancariaDTO, String> colCedulaTitular;
    @FXML private TableColumn<EntidadBancariaDTO, String> colCorreoTitular;

    private final ObservableList<EntidadBancariaDTO> entidades = FXCollections.observableArrayList();
    private double valorActualAlicuota = 45.00;

    @FXML
    public void initialize() {
        cbTipoCuenta.setItems(FXCollections.observableArrayList("AHORROS", "CORRIENTE"));

        colNombreEntidad.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        colNumeroCuenta.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNumeroCuenta()));
        colTipoCuenta.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTipo()));
        colCedulaTitular.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCedulaTitular()));
        colCorreoTitular.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmailTitular()));
        tablaEntidades.setItems(entidades);
        tablaEntidades.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        Label placeholder = new Label("Aún no se han registrado entidades bancarias.");
        placeholder.getStyleClass().add("module-subtitle");
        placeholder.setWrapText(true);
        placeholder.setAlignment(Pos.CENTER);
        placeholder.setMaxWidth(420);
        tablaEntidades.setPlaceholder(placeholder);

        actualizarValorActual();
    }

    // ==================== Valor mensual de alícuotas ====================

    @FXML
    void guardarValorAlicuota(ActionEvent event) {
        String texto = txtNuevoValorAlicuota.getText() == null ? "" : txtNuevoValorAlicuota.getText().trim();

        if (texto.isEmpty() || !VALOR_VALIDO.matcher(texto).matches() || Double.parseDouble(texto) <= 0) {
            setMensaje(lblMensajeAlicuota,
                    "El valor mensual esperado de alicuotas ingresado no es válido, asegúrese que haya ingresado un número mayor a 0 con hasta 2 cifras decimales",
                    "message-error");
            return;
        }

        valorActualAlicuota = Double.parseDouble(texto);
        actualizarValorActual();
        txtNuevoValorAlicuota.clear();
        setMensaje(lblMensajeAlicuota, "El valor mensual esperado de alicuotas se registró correctamente", "message-success");
    }

    private void actualizarValorActual() {
        lblValorActualAlicuota.setText(String.format(Locale.US, "$%.2f", valorActualAlicuota));
    }

    // ==================== Entidades bancarias ====================

    @FXML
    void registrarEntidadBancaria(ActionEvent event) {
        String nombreEntidad = textoDe(txtNombreEntidad);
        String numeroCuenta = textoDe(txtNumeroCuenta);
        String cedulaTitular = textoDe(txtCedulaTitular);
        String tipoCuenta = cbTipoCuenta.getValue();
        String correoTitular = textoDe(txtCorreoTitular);

        if (nombreEntidad.isEmpty() || numeroCuenta.isEmpty() || cedulaTitular.isEmpty()
                || tipoCuenta == null || correoTitular.isEmpty()) {
            setMensaje(lblMensajeEntidad, "La información de la entidad bancaria no es válida", "message-error");
            return;
        }
        if (!CEDULA_VALIDA.matcher(cedulaTitular).matches() || !CORREO_VALIDO.matcher(correoTitular).matches()) {
            setMensaje(lblMensajeEntidad, "La información de la entidad bancaria no es válida", "message-error");
            return;
        }

        boolean yaExiste = entidades.stream()
                .anyMatch(dto -> dto.getNumeroCuenta().equalsIgnoreCase(numeroCuenta));
        if (yaExiste) {
            setMensaje(lblMensajeEntidad, "Ya existe una entidad bancaria registrada con el número de cuenta ingresado", "message-error");
            return;
        }

        // TODO: reemplazar por servicioFinanzas.registrarEntidadBancaria(dto) cuando exista el Service/DAO
        entidades.add(new EntidadBancariaDTO(nombreEntidad, numeroCuenta, cedulaTitular, tipoCuenta, correoTitular));
        setMensaje(lblMensajeEntidad, "Entidad bancaria registrada correctamente", "message-success");
        limpiarFormularioEntidad(null);
    }

    @FXML
    void limpiarFormularioEntidad(ActionEvent event) {
        txtNombreEntidad.clear();
        txtNumeroCuenta.clear();
        txtCedulaTitular.clear();
        txtCorreoTitular.clear();
        cbTipoCuenta.setValue(null);
    }

    private String textoDe(TextField campo) {
        return campo.getText() == null ? "" : campo.getText().trim();
    }

    private void setMensaje(Label label, String texto, String estilo) {
        label.getStyleClass().removeAll("message-info", "message-success", "message-error");
        if (!label.getStyleClass().contains("message-label")) {
            label.getStyleClass().add("message-label");
        }
        label.getStyleClass().add(estilo);
        label.setText(texto);
    }
}
