package fis.dsw.sgc.inmuebles.controller;

import fis.dsw.sgc.inmuebles.dao.IInmuebleDAO;
import fis.dsw.sgc.inmuebles.dao.InmuebleDAOMySQL;
import fis.dsw.sgc.inmuebles.dto.OpcionComboDTO;
import fis.dsw.sgc.inmuebles.model.Inmueble;
import fis.dsw.sgc.inmuebles.service.IInmueblesService;
import fis.dsw.sgc.inmuebles.service.InmueblesServiceImpl;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class RegistrarInmuebleController {

    @FXML private TextField txtCodigo;
    @FXML private ComboBox<OpcionComboDTO> cmbEdificio;
    @FXML private ComboBox<OpcionComboDTO> cmbTipoInmueble;
    @FXML private ComboBox<OpcionComboDTO> cmbPropietario;
    @FXML private TextField txtPiso;
    @FXML private TextField txtNumero;
    @FXML private TextField txtAreaM2;
    @FXML private TextField txtHabitaciones;
    @FXML private TextField txtBanos;
    @FXML private TextArea txtDescripcion;
    @FXML private CheckBox chkAlquiler;
    @FXML private CheckBox chkVenta;
    @FXML private Label lblMensaje;

    private final IInmuebleDAO inmuebleDAO = new InmuebleDAOMySQL();
    private final IInmueblesService inmueblesService = new InmueblesServiceImpl(inmuebleDAO);

    private static final OpcionComboDTO SIN_PROPIETARIO = new OpcionComboDTO(0, "Sin propietario");

    @FXML
    public void initialize() {
        cmbEdificio.setItems(FXCollections.observableArrayList(inmueblesService.obtenerEdificios()));
        cmbTipoInmueble.setItems(FXCollections.observableArrayList(inmueblesService.obtenerTiposInmueble()));

        cmbPropietario.getItems().add(SIN_PROPIETARIO);
        cmbPropietario.getItems().addAll(inmueblesService.obtenerPropietarios());
        cmbPropietario.setValue(SIN_PROPIETARIO);

        setMensaje("Complete los datos del inmueble y pulse Registrar.", "message-info");
    }

    @FXML
    void registrar(ActionEvent event) {
        try {
            Inmueble inmueble = new Inmueble();
            inmueble.setCodigo(texto(txtCodigo.getText()));

            if (cmbEdificio.getValue() != null) {
                inmueble.setIdEdificio(cmbEdificio.getValue().getId());
            }
            if (cmbTipoInmueble.getValue() != null) {
                inmueble.setIdTipoInmueble(cmbTipoInmueble.getValue().getId());
            }

            inmueble.setPiso(parseEntero(txtPiso.getText()));
            inmueble.setNumero(texto(txtNumero.getText()));
            inmueble.setAreaM2(parseDecimal(txtAreaM2.getText()));
            inmueble.setNumeroHabitaciones(parseEntero(txtHabitaciones.getText()));
            inmueble.setNumeroBanos(parseEntero(txtBanos.getText()));
            inmueble.setDescripcion(texto(txtDescripcion.getText()));
            inmueble.setDisponibleAlquiler(chkAlquiler.isSelected());
            inmueble.setDisponibleVenta(chkVenta.isSelected());
            inmueble.setEstado("DISPONIBLE");

            int idNuevoInmueble = inmueblesService.registrarInmueble(inmueble);

            OpcionComboDTO propietarioSeleccionado = cmbPropietario.getValue();
            if (propietarioSeleccionado != null && propietarioSeleccionado.getId() != 0) {
                inmueblesService.asignarPropietario(idNuevoInmueble, propietarioSeleccionado.getId());
            }

            setMensaje("Inmueble '" + inmueble.getCodigo() + "' registrado exitosamente.", "message-success");
            limpiar(event);

        } catch (IllegalArgumentException e) {
            setMensaje(e.getMessage(), "message-error");
        } catch (Exception e) {
            setMensaje("Ocurrió un error al registrar el inmueble. Intente nuevamente.", "message-error");
            System.err.println("Error al registrar inmueble: " + e.getMessage());
        }
    }

    @FXML
    void limpiar(ActionEvent event) {
        txtCodigo.clear();
        cmbEdificio.getSelectionModel().clearSelection();
        cmbTipoInmueble.getSelectionModel().clearSelection();
        txtPiso.clear();
        txtNumero.clear();
        txtAreaM2.clear();
        txtHabitaciones.clear();
        txtBanos.clear();
        txtDescripcion.clear();
        chkAlquiler.setSelected(false);
        chkVenta.setSelected(false);
        cmbPropietario.setValue(SIN_PROPIETARIO);
    }

    private String texto(String valor) {
        return valor == null ? "" : valor.trim();
    }

    private Integer parseEntero(String valor) {
        try {
            return (valor == null || valor.isBlank()) ? null : Integer.valueOf(valor.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Double parseDecimal(String valor) {
        try {
            return (valor == null || valor.isBlank()) ? null : Double.valueOf(valor.trim());
        } catch (NumberFormatException e) {
            return null;
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
}