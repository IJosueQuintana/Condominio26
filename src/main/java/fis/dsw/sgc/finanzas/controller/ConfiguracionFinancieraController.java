package fis.dsw.sgc.finanzas.controller;

import fis.dsw.sgc.finanzas.dto.EntidadBancariaDTO;
import fis.dsw.sgc.finanzas.service.ConfiguracionFinancieraService;
import fis.dsw.sgc.finanzas.service.IConfiguracionFinancieraService;
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
    private static final Pattern CEDULA_VALIDA = Pattern.compile("\\d{10}");
    private static final Pattern NOMBRE_ENTIDAD_VALIDO = Pattern.compile("[a-zA-ZñÑ ]{1,100}");
    private static final Pattern NUMERO_CUENTA_VALIDO = Pattern.compile("\\d+");
    private static final Pattern CORREO_VALIDO = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

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

    // Conexión Controller -> Service: por aquí se accede a la lógica de negocio real (Service -> DAO)
    private final IConfiguracionFinancieraService configuracionFinancieraService = new ConfiguracionFinancieraService();
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
                    "El valor mensual esperado de alicuotas ingresado no es válido, asegúrese que haya ingresado un número mayor a 0 con 2 decimales",
                    "message-error");
            return;
        }

        // Llamado al Service: envía el valor validado y recibe de vuelta el valor ya registrado
        valorActualAlicuota = configuracionFinancieraService.definirValorMensualDeAlicuotas(Double.parseDouble(texto));
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

        if (nombreEntidad.isEmpty()) {
            setMensaje(lblMensajeEntidad, "Debe ingresar el nombre de la entidad bancaria", "message-error");
            return;
        }
        if (!NOMBRE_ENTIDAD_VALIDO.matcher(nombreEntidad).matches()) {
            setMensaje(lblMensajeEntidad,
                    "El nombre de la entidad bancaria no es válido, ingrese hasta 100 caracteres usando solo letras",
                    "message-error");
            return;
        }
        if (numeroCuenta.isEmpty()) {
            setMensaje(lblMensajeEntidad, "Debe ingresar el número de cuenta de la entidad bancaria", "message-error");
            return;
        }
        if (!NUMERO_CUENTA_VALIDO.matcher(numeroCuenta).matches()) {
            setMensaje(lblMensajeEntidad, "El número de cuenta no es válido, ingrese solo números", "message-error");
            return;
        }
        if (cedulaTitular.isEmpty()) {
            setMensaje(lblMensajeEntidad, "Debe ingresar la cédula del titular de la cuenta", "message-error");
            return;
        }
        if (!CEDULA_VALIDA.matcher(cedulaTitular).matches()) {
            setMensaje(lblMensajeEntidad, "La cédula del titular no es válida, ingrese 10 dígitos", "message-error");
            return;
        }
        if (tipoCuenta == null) {
            setMensaje(lblMensajeEntidad, "Debe seleccionar el tipo de cuenta", "message-error");
            return;
        }
        if (correoTitular.isEmpty()) {
            setMensaje(lblMensajeEntidad, "Debe ingresar el correo electrónico del titular de la cuenta", "message-error");
            return;
        }
        if (!CORREO_VALIDO.matcher(correoTitular).matches()) {
            setMensaje(lblMensajeEntidad, "El correo electrónico del titular no es válido", "message-error");
            return;
        }

        boolean yaExiste = entidades.stream()
                .anyMatch(dto -> dto.getNumeroCuenta().equalsIgnoreCase(numeroCuenta));
        if (yaExiste) {
            setMensaje(lblMensajeEntidad, "Ya existe una entidad bancaria registrada con el número de cuenta ingresado", "message-error");
            return;
        }

        // Llamado al Service: envía el DTO armado con los datos del formulario y recibe de vuelta el DTO ya registrado
        EntidadBancariaDTO nuevaEntidad = new EntidadBancariaDTO(nombreEntidad, numeroCuenta, cedulaTitular, tipoCuenta, correoTitular);
        entidades.add(configuracionFinancieraService.registrarEntidadBancaria(nuevaEntidad));
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