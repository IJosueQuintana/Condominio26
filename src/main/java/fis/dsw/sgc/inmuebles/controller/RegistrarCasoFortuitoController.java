package fis.dsw.sgc.inmuebles.controller;

import fis.dsw.sgc.inmuebles.dao.IInmuebleDAO;
import fis.dsw.sgc.inmuebles.dao.InmuebleDAOMySQL;
import fis.dsw.sgc.inmuebles.dto.CasoFortuitoDTO;
import fis.dsw.sgc.inmuebles.model.Inmueble;
import fis.dsw.sgc.inmuebles.service.IInmueblesService;
import fis.dsw.sgc.inmuebles.service.InmueblesServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class RegistrarCasoFortuitoController {

    @FXML private TextField txtBuscarCodigo;
    @FXML private Label lblInmuebleInfo;
    @FXML private TextArea txtDescripcion;
    @FXML private Label lblMensaje;

    @FXML private TableView<CasoFortuitoDTO> tablaCasos;
    @FXML private TableColumn<CasoFortuitoDTO, String> colFecha;
    @FXML private TableColumn<CasoFortuitoDTO, String> colDescripcion;
    @FXML private TableColumn<CasoFortuitoDTO, String> colEstado;

    private final IInmuebleDAO inmuebleDAO = new InmuebleDAOMySQL();
    private final IInmueblesService inmueblesService = new InmueblesServiceImpl(inmuebleDAO);

    private Inmueble inmuebleActual;

    @FXML
    public void initialize() {
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

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
            inmuebleActual = null;
            lblInmuebleInfo.setText(" ");
            tablaCasos.setItems(FXCollections.observableArrayList());
            habilitarFormulario(false);
            setMensaje("No se encontró ningún inmueble con el código '" + codigo + "'.", "message-error");
            return;
        }

        lblInmuebleInfo.setText("Inmueble encontrado: " + inmuebleActual.getCodigo()
                + (inmuebleActual.getDescripcion() != null && !inmuebleActual.getDescripcion().isBlank()
                    ? " — " + inmuebleActual.getDescripcion() : ""));

        cargarHistorial();
        habilitarFormulario(true);
        setMensaje("Describa el incidente y pulse Registrar caso.", "message-info");
    }

    @FXML
    void registrar(ActionEvent event) {
        if (inmuebleActual == null) {
            setMensaje("Primero busque un inmueble por código.", "message-error");
            return;
        }

        try {
            String descripcion = txtDescripcion.getText();
            inmueblesService.registrarCasoFortuito(inmuebleActual.getIdInmueble(), descripcion);

            txtDescripcion.clear();
            cargarHistorial();
            setMensaje("Caso fortuito registrado exitosamente para el inmueble '"
                    + inmuebleActual.getCodigo() + "'.", "message-success");

        } catch (IllegalArgumentException e) {
            setMensaje(e.getMessage(), "message-error");
        } catch (Exception e) {
            setMensaje("Ocurrió un error al registrar el caso fortuito. Intente nuevamente.", "message-error");
            System.err.println("Error al registrar caso fortuito: " + e.getMessage());
        }
    }

    private void cargarHistorial() {
        List<CasoFortuitoDTO> casos = inmueblesService.listarCasosFortuitos(inmuebleActual.getIdInmueble());
        ObservableList<CasoFortuitoDTO> filas = FXCollections.observableArrayList(casos);
        tablaCasos.setItems(filas);
    }

    private void habilitarFormulario(boolean habilitar) {
        txtDescripcion.setDisable(!habilitar);
    }

    private String texto(String valor) {
        return valor == null ? "" : valor.trim();
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