package fis.dsw.sgc.inmuebles.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    private TableView<InmuebleFila> tablaInmuebles;

    @FXML
    private TableColumn<InmuebleFila, String> colCodigo;

    @FXML
    private TableColumn<InmuebleFila, String> colDireccion;

    @FXML
    private TableColumn<InmuebleFila, String> colPropietario;

    @FXML
    private TableColumn<InmuebleFila, String> colEstado;

    @FXML
    public void initialize() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colPropietario.setCellValueFactory(new PropertyValueFactory<>("propietario"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Datos de ejemplo, solo para confirmar visualmente que la vista carga bien.
        // TODO: reemplazar por datos reales desde inmuebles.service cuando exista.
        ObservableList<InmuebleFila> datosDePrueba = FXCollections.observableArrayList(
                new InmuebleFila("A-101", "Torre A, Piso 1", "Erick Salazar", "Al día"),
                new InmuebleFila("B-204", "Torre B, Piso 2", "María Pérez", "Deudor"),
                new InmuebleFila("C-305", "Torre C, Piso 3", "Jorge Ramírez", "Al día")
        );
        tablaInmuebles.setItems(datosDePrueba);
    }

    @FXML
    void buscar(ActionEvent event) {
        // TODO: conectar con inmuebles.service para filtrar por código/dirección real.
        System.out.println("Buscando inmueble: " + txtBuscar.getText());
    }

    /** Fila simple solo para probar la tabla. Muévela a inmuebles.model/dto cuando integren datos reales. */
    public static class InmuebleFila {
        private final String codigo;
        private final String direccion;
        private final String propietario;
        private final String estado;

        public InmuebleFila(String codigo, String direccion, String propietario, String estado) {
            this.codigo = codigo;
            this.direccion = direccion;
            this.propietario = propietario;
            this.estado = estado;
        }

        public String getCodigo() {
            return codigo;
        }

        public String getDireccion() {
            return direccion;
        }

        public String getPropietario() {
            return propietario;
        }

        public String getEstado() {
            return estado;
        }
    }
}