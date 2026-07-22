package fis.dsw.sgc.inmuebles.controller;

import java.util.List;

import fis.dsw.sgc.inmuebles.dao.IInmuebleDAO;
import fis.dsw.sgc.inmuebles.dao.InmuebleDAOMySQL;
import fis.dsw.sgc.inmuebles.dto.InmuebleResumenDTO;
import fis.dsw.sgc.inmuebles.service.IInmueblesService;
import fis.dsw.sgc.inmuebles.service.InmueblesServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class inmueblesController {

    @FXML
    private TextField txtBuscar;

    @FXML
    private Button btnBuscar;

    @FXML
    private Label lblMensaje;

    @FXML
    private TableView<InmuebleResumenDTO> tablaInmuebles;

    @FXML
    private TableColumn<InmuebleResumenDTO, String> colCodigo;

    @FXML
    private TableColumn<InmuebleResumenDTO, String> colDireccion;

    @FXML
    private TableColumn<InmuebleResumenDTO, String> colTipo;

    @FXML
    private TableColumn<InmuebleResumenDTO, String> colPropietario;

    @FXML
    private TableColumn<InmuebleResumenDTO, String> colEstado;

    private final IInmuebleDAO inmuebleDAO = new InmuebleDAOMySQL();
    private final IInmueblesService inmueblesService = new InmueblesServiceImpl(inmuebleDAO);

    @FXML
    public void initialize() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colPropietario.setCellValueFactory(new PropertyValueFactory<>("propietario"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        cargarInmuebles("");
    }

    @FXML
    void buscar(ActionEvent event) {
        cargarInmuebles(txtBuscar.getText());
    }

    private void cargarInmuebles(String filtro) {
        try {
            List<InmuebleResumenDTO> resultado = inmueblesService.listarInmuebles(filtro);
            ObservableList<InmuebleResumenDTO> filas = FXCollections.observableArrayList(resultado);
            tablaInmuebles.setItems(filas);

            if (resultado.isEmpty()) {
                lblMensaje.setText("No se encontraron inmuebles con ese criterio de búsqueda.");
            } else {
                lblMensaje.setText(resultado.size() + " inmueble(s) encontrado(s).");
            }
        } catch (Exception e) {
            lblMensaje.setText("Ocurrió un error al consultar los inmuebles. Revisa la conexión a la base de datos.");
            System.err.println("Error al cargar inmuebles: " + e.getMessage());
        }
    }
}