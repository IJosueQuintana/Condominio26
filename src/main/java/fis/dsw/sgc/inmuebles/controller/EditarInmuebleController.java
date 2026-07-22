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

public class EditarInmuebleController {

    @FXML private TextField txtBuscarCodigo;

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
    @FXML private ComboBox<String> cmbEstado;
    @FXML private Label lblMensaje;

    private final IInmuebleDAO inmuebleDAO = new InmuebleDAOMySQL();
    private final IInmueblesService inmueblesService = new InmueblesServiceImpl(inmuebleDAO);

    private Inmueble inmuebleActual;

    private static final OpcionComboDTO SIN_PROPIETARIO = new OpcionComboDTO(0, "Sin propietario");

    @FXML
    public void initialize() {
        cmbEdificio.setItems(FXCollections.observableArrayList(inmueblesService.obtenerEdificios()));
        cmbTipoInmueble.setItems(FXCollections.observableArrayList(inmueblesService.obtenerTiposInmueble()));
        cmbEstado.setItems(FXCollections.observableArrayList(
                "DISPONIBLE", "OCUPADO", "EN_MANTENIMIENTO", "EN_REMODELACION", "INACTIVO"));

        cmbPropietario.getItems().add(SIN_PROPIETARIO);
        cmbPropietario.getItems().addAll(inmueblesService.obtenerPropietarios());

        habilitarFormulario(false);
        setMensaje("Ingrese el código del inmueble y pulse Buscar.", "message-info");
    }

    @FXML
    void buscar(ActionEvent event) {
        String codigo = texto(txtBuscarCodigo.getText());
        if (codigo.isEmpty()) {
            setMensaje("Ingrese un código para buscar.", "message-error");
            return;
        }

        inmuebleActual = inmueblesService.buscarInmueblePorCodigo(codigo);

        if (inmuebleActual == null) {
            habilitarFormulario(false);
            limpiarFormulario();
            setMensaje("No se encontró ningún inmueble con el código '" + codigo + "'.", "message-error");
            return;
        }

        cargarFormulario(inmuebleActual);
        habilitarFormulario(true);
        setMensaje("Inmueble '" + inmuebleActual.getCodigo() + "' cargado. Edite los datos y pulse Guardar cambios.",
                "message-info");
    }

    @FXML
    void guardar(ActionEvent event) {
        if (inmuebleActual == null) {
            setMensaje("Primero busque un inmueble por código.", "message-error");
            return;
        }

        try {
            inmuebleActual.setCodigo(texto(txtCodigo.getText()));

            if (cmbEdificio.getValue() != null) {
                inmuebleActual.setIdEdificio(cmbEdificio.getValue().getId());
            } else {
                inmuebleActual.setIdEdificio(null);
            }
            if (cmbTipoInmueble.getValue() != null) {
                inmuebleActual.setIdTipoInmueble(cmbTipoInmueble.getValue().getId());
            }

            inmuebleActual.setPiso(parseEntero(txtPiso.getText()));
            inmuebleActual.setNumero(texto(txtNumero.getText()));
            inmuebleActual.setAreaM2(parseDecimal(txtAreaM2.getText()));
            inmuebleActual.setNumeroHabitaciones(parseEntero(txtHabitaciones.getText()));
            inmuebleActual.setNumeroBanos(parseEntero(txtBanos.getText()));
            inmuebleActual.setDescripcion(texto(txtDescripcion.getText()));
            inmuebleActual.setDisponibleAlquiler(chkAlquiler.isSelected());
            inmuebleActual.setDisponibleVenta(chkVenta.isSelected());
            inmuebleActual.setEstado(cmbEstado.getValue() == null ? "DISPONIBLE" : cmbEstado.getValue());

            inmueblesService.actualizarInmueble(inmuebleActual);

            OpcionComboDTO propietarioSeleccionado = cmbPropietario.getValue();
            Integer idPropietario = (propietarioSeleccionado == null || propietarioSeleccionado.getId() == 0)
                    ? null : propietarioSeleccionado.getId();
            inmueblesService.asignarPropietario(inmuebleActual.getIdInmueble(), idPropietario);

            setMensaje("Inmueble '" + inmuebleActual.getCodigo() + "' actualizado exitosamente.", "message-success");

        } catch (IllegalArgumentException e) {
            setMensaje(e.getMessage(), "message-error");
        } catch (Exception e) {
            setMensaje("Ocurrió un error al actualizar el inmueble. Intente nuevamente.", "message-error");
            System.err.println("Error al actualizar inmueble: " + e.getMessage());
        }
    }

    private void cargarFormulario(Inmueble inmueble) {
        txtCodigo.setText(inmueble.getCodigo());
        txtPiso.setText(inmueble.getPiso() == null ? "" : String.valueOf(inmueble.getPiso()));
        txtNumero.setText(inmueble.getNumero() == null ? "" : inmueble.getNumero());
        txtAreaM2.setText(inmueble.getAreaM2() == null ? "" : String.valueOf(inmueble.getAreaM2()));
        txtHabitaciones.setText(inmueble.getNumeroHabitaciones() == null ? "" : String.valueOf(inmueble.getNumeroHabitaciones()));
        txtBanos.setText(inmueble.getNumeroBanos() == null ? "" : String.valueOf(inmueble.getNumeroBanos()));
        txtDescripcion.setText(inmueble.getDescripcion() == null ? "" : inmueble.getDescripcion());
        chkAlquiler.setSelected(inmueble.isDisponibleAlquiler());
        chkVenta.setSelected(inmueble.isDisponibleVenta());
        cmbEstado.setValue(inmueble.getEstado());

        seleccionarOpcion(cmbTipoInmueble, inmueble.getIdTipoInmueble());
        if (inmueble.getIdEdificio() != null) {
            seleccionarOpcion(cmbEdificio, inmueble.getIdEdificio());
        } else {
            cmbEdificio.getSelectionModel().clearSelection();
        }

        Integer idPropietarioActual = inmueblesService.obtenerPropietarioActual(inmueble.getIdInmueble());
        if (idPropietarioActual != null) {
            seleccionarOpcion(cmbPropietario, idPropietarioActual);
        } else {
            cmbPropietario.setValue(SIN_PROPIETARIO);
        }
    }

    private void seleccionarOpcion(ComboBox<OpcionComboDTO> combo, int id) {
        for (OpcionComboDTO opcion : combo.getItems()) {
            if (opcion.getId() == id) {
                combo.setValue(opcion);
                return;
            }
        }
    }

    private void limpiarFormulario() {
        txtCodigo.clear();
        cmbEdificio.getSelectionModel().clearSelection();
        cmbTipoInmueble.getSelectionModel().clearSelection();
        cmbEstado.getSelectionModel().clearSelection();
        txtPiso.clear();
        txtNumero.clear();
        txtAreaM2.clear();
        txtHabitaciones.clear();
        txtBanos.clear();
        txtDescripcion.clear();
        chkAlquiler.setSelected(false);
        chkVenta.setSelected(false);
        cmbPropietario.setValue(SIN_PROPIETARIO);
        inmuebleActual = null;
    }

    private void habilitarFormulario(boolean habilitar) {
        txtCodigo.setDisable(!habilitar);
        cmbEdificio.setDisable(!habilitar);
        cmbTipoInmueble.setDisable(!habilitar);
        txtPiso.setDisable(!habilitar);
        txtNumero.setDisable(!habilitar);
        txtAreaM2.setDisable(!habilitar);
        txtHabitaciones.setDisable(!habilitar);
        txtBanos.setDisable(!habilitar);
        txtDescripcion.setDisable(!habilitar);
        chkAlquiler.setDisable(!habilitar);
        chkVenta.setDisable(!habilitar);
        cmbEstado.setDisable(!habilitar);
        cmbPropietario.setDisable(!habilitar);
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