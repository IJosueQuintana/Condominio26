package fis.dsw.sgc.finanzas.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;

public class GenerarReporteGastosController implements Initializable {

    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnLimpiar;

    @FXML
    private TableColumn<?, ?> colDescripcion;

    @FXML
    private TableColumn<?, ?> colFecha;

    @FXML
    private TableColumn<?, ?> colMotivo;

    @FXML
    private TableColumn<?, ?> colValor;

    @FXML
    private DatePicker dpFechaFin;

    @FXML
    private DatePicker dpFechaInicio;

    @FXML
    private Label lblIconoAgua;

    @FXML
    private Label lblIconoFechas;

    @FXML
    private Label lblIconoFechas1;

    @FXML
    private Label lblIconoGastos;

    @FXML
    private Label lblIconoGuardar;

    @FXML
    private Label lblIconoInternet;

    @FXML
    private Label lblIconoLimpiar;

    @FXML
    private Label lblIconoLuz;

    @FXML
    private Label lblIconoTelefono;

    @FXML
    private Label lblTotalGastos;

    @FXML
    private Label lblTotalOtros;

    @FXML
    private Label lblTotalServiciosBasicos;

    @FXML
    private Label lblTotalSueldos;

    @FXML
    private TableView<?> tbReporteGastos;

    @FXML
    private TextField txtTotalAgua;

    @FXML
    private TextField txtTotalGastos;

    @FXML
    private TextField txtTotalInternet;

    @FXML
    private TextField txtTotalLuz;

    @FXML
    private TextField txtTotalOtros;

    @FXML
    private TextField txtTotalServiciosBasicos;

    @FXML
    private TextField txtTotalSueldos;

    @FXML
    private TextField txtTotalTelefono;

    @FXML
    void consultarGastos(ActionEvent event) {

    }

    @FXML
    void descargarReporte(ActionEvent event) {

    }

    @FXML
    void limpiarReporte(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FontIcon icon = new FontIcon("fa-external-link");
        icon.getStyleClass().add("titleIcon");
        lblIconoGastos.setGraphic(icon);
        lblIconoGastos.setText(null);

        FontIcon icon2 = new FontIcon("fa-calendar-minus-o");
        icon2.getStyleClass().add("totalsIcon");
        lblIconoFechas.setText(null);
        lblIconoFechas.setGraphic(icon2);

        FontIcon icon3 = new FontIcon("fa-calendar-plus-o");
        icon3.getStyleClass().add("totalsIcon");
        lblIconoFechas1.setText(null);
        lblIconoFechas1.setGraphic(icon3);

        FontIcon icon4 = new FontIcon("fa-phone");
        icon4.getStyleClass().add("totalsIcon");
        lblIconoTelefono.setText(null);
        lblIconoTelefono.setGraphic(icon4);

        FontIcon icon5 = new FontIcon("fa-tint");
        icon5.getStyleClass().add("totalsIcon");

        lblIconoAgua.setText(null);
        lblIconoAgua.setGraphic(icon5);

        FontIcon icon6 = new FontIcon("fa-plug");
        icon6.getStyleClass().add("totalsIcon");

        lblIconoLuz.setText(null);
        lblIconoLuz.setGraphic(icon6);

        FontIcon icon7 = new FontIcon("fa-wifi");
        icon7.getStyleClass().add("totalsIcon");

        lblIconoInternet.setText(null);
        lblIconoInternet.setGraphic(icon7);

        FontIcon icon8 = new FontIcon("fa-users");
        icon8.getStyleClass().add("totalsIcon");

        lblTotalSueldos.setText(null);
        lblTotalSueldos.setGraphic(icon8);

        FontIcon icon9 = new FontIcon("fa-wrench");
        icon9.getStyleClass().add("totalsIcon");

        lblTotalOtros.setText(null);
        lblTotalOtros.setGraphic(icon9);

        FontIcon icon10 = new FontIcon("fa-shower");
        icon10.getStyleClass().add("totalsIcon");

        lblTotalServiciosBasicos.setText(null);
        lblTotalServiciosBasicos.setGraphic(icon10);

        FontIcon icon11 = new FontIcon("fa-usd");
        icon11.getStyleClass().add("totalsIcon");

        lblTotalGastos.setText(null);
        lblTotalGastos.setGraphic(icon11);
    }
}
