package fis.dsw.sgc.finanzas.controller;

import fis.dsw.sgc.finanzas.dto.CuotaDTO;
import fis.dsw.sgc.finanzas.exception.DeudaNoExisteException;
import fis.dsw.sgc.finanzas.exception.NoSePuedeDiferirException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import fis.dsw.sgc.finanzas.service.IDeudaService;

import java.util.regex.Pattern;

// Controlador de la vista Solicitar pago en cuotas (caso de uso solicitarPagoEnCuotas, GRA)
public class SolicitarPagoEnCuotasController {

    private static final Pattern MESES_VALIDO = Pattern.compile("\\d+");
    private static final int MESES_MINIMOS = 3;
    private static final int MESES_MAXIMOS = 11;

    // Conexión Controller -> Service: el service llega inyectado por constructor (no se instancia aquí)
    private final IDeudaService deudaService;

    // Inyección de dependencias: quien cargue este controlador (FXMLLoader + setController) debe pasar el service
    public SolicitarPagoEnCuotasController(IDeudaService deudaService) {
        this.deudaService = deudaService;
    }

    @FXML private TextField txtIdDeuda;
    @FXML private TextField txtNumeroMeses;
    @FXML private Button btnCalcular;
    @FXML private Button btnLimpiar;
    @FXML private Button btnSolicitar;
    @FXML private Label lblMensaje;

    @FXML private TableView<CuotaDTO> tablaCuotas;
    @FXML private TableColumn<CuotaDTO, String> colCuota;
    @FXML private TableColumn<CuotaDTO, String> colFechaMax;
    @FXML private TableColumn<CuotaDTO, String> colValor;

    private final ObservableList<CuotaDTO> cuotas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colCuota.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCuota()));
        colFechaMax.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFechaMaximaPago()));
        colValor.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getValor()));
        tablaCuotas.setItems(cuotas);
        tablaCuotas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        Label placeholderTabla = new Label("Aún no se ha solicitado el diferimiento de una deuda.");
        placeholderTabla.getStyleClass().add("module-subtitle");
        placeholderTabla.setWrapText(true);
        placeholderTabla.setAlignment(Pos.CENTER);
        placeholderTabla.setMaxWidth(420);
        tablaCuotas.setPlaceholder(placeholderTabla);

        btnSolicitar.setDisable(true);
        setMensaje("Ingrese el ID de la deuda y el número de meses a diferirla.", "message-info");
    }

    @FXML
    void calcular(ActionEvent event) {
        cuotas.clear();
        btnSolicitar.setDisable(true);

        String idDeuda = texto(txtIdDeuda);
        String meses = texto(txtNumeroMeses);

        if (idDeuda.isEmpty()) {
            setMensaje("Debe ingresar el ID de la deuda que desea diferir.", "message-error");
            return;
        }
        if (meses.isEmpty() || !MESES_VALIDO.matcher(meses).matches()) {
            setMensaje("El número de meses ingresado no es válido, ingrese un número entero.", "message-error");
            return;
        }

        int numeroMeses = Integer.parseInt(meses);
        if (numeroMeses <= MESES_MINIMOS - 1 || numeroMeses >= MESES_MAXIMOS + 1) {
            setMensaje("El numero de meses a diferir la deuda debe ser de almenos 3 y como máximo 12", "message-error");
            return;
        }

        try {
            // Llamado al Service: envía el id de la deuda y el número de meses, recibe de vuelta las cuotas generadas.
            // La validación de mora y el cálculo de cuotas ocurren dentro del Service, no aquí.
            cuotas.setAll(deudaService.solicitarPagoEnCuotas(Integer.valueOf(idDeuda), numeroMeses));
        } catch (NumberFormatException e) {
            // El Service espera un id numérico; si el texto ingresado no lo es, se avisa al usuario
            setMensaje("El ID de la deuda ingresado no es válido, ingrese un número entero.", "message-error");
            return;
        } catch (DeudaNoExisteException e) {
            // El Service lanza esta excepción cuando el ID de deuda ingresado no existe
            setMensaje(e.getMessage(), "message-error");
            return;
        } catch (NoSePuedeDiferirException e) {
            // El Service lanza esta excepción cuando la deuda está en mora o el número de meses no cumple la regla de negocio
            setMensaje(e.getMessage(), "message-error");
            return;
        }

        btnSolicitar.setDisable(false);
        setMensaje("", "message-info");
    }

    @FXML
    void solicitar(ActionEvent event) {
        setMensaje("Deuda diferida exitosamente", "message-success");
    }

    @FXML
    void limpiar(ActionEvent event) {
        txtIdDeuda.clear();
        txtNumeroMeses.clear();
        cuotas.clear();
        btnSolicitar.setDisable(true);
        setMensaje("Ingrese el ID de la deuda y el número de meses a diferirla.", "message-info");
    }

    private String texto(TextField campo) {
        return campo.getText() == null ? "" : campo.getText().trim();
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