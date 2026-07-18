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
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;

public class GenerarReportePagosRealizadosController implements Initializable {

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnLimpiar;

    @FXML
    private Button btnBuscar;

    @FXML
    private TableColumn<?, ?> colFecha;

    @FXML
    private TableColumn<?, ?> colMotivo;

    @FXML
    private TableColumn<?, ?> colNumeroCedulaIdentidad;

    @FXML
    private TableColumn<?, ?> colValor;

    @FXML
    private HBox dpFechaFin;

    @FXML
    private DatePicker dpFechaInicio;

    @FXML
    private Label lblIconoCedula;

    @FXML
    private Label lblIconoFechas;

    @FXML
    private Label lblIconoFechas1;

    @FXML
    private Label lblIconoGuardar;

    @FXML
    private Label lblIconoLimpiar;

    @FXML
    private Label lblIconoPagos;

    @FXML
    private Label lblIconoTotalAlicuotas;

    @FXML
    private Label lblIconoTotalGeneral;

    @FXML
    private Label lblIconoTotalMultas;

    @FXML
    private Label lblIconoTotalReservas;

    @FXML
    private TableView<?> tbReportePagos;

    @FXML
    private TextField txtNumeroCedulaIdentidad;

    @FXML
    private TextField txtTotalAlicuotas;

    @FXML
    private TextField txtTotalGeneral;

    @FXML
    private TextField txtTotalMultas;

    @FXML
    void descargarReporte(ActionEvent event) {

    }

    @FXML
    void limpiarReporte(ActionEvent event) {

    }

    @FXML
    void consultarPagos(ActionEvent event) {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FontIcon icon = new FontIcon("fa-chain");
        icon.getStyleClass().add("titleIcon");
        lblIconoPagos.setGraphic(icon);
        lblIconoPagos.setText(null);
        FontIcon icon2 = new FontIcon("fa-id-card");
        icon2.getStyleClass().add("optionsIcon");
        lblIconoCedula.setGraphic(icon2);
        lblIconoCedula.setText(null);
        FontIcon icon3 = new FontIcon("fa-calendar-minus-o");
        icon3.getStyleClass().add("optionsIcon");
        lblIconoFechas.setGraphic(icon3);
        lblIconoFechas.setText(null);
        FontIcon icon4 = new FontIcon("fa-calendar-plus-o");
        icon4.getStyleClass().add("optionsIcon");
        lblIconoFechas1.setGraphic(icon4);
        lblIconoFechas1.setText(null);
        FontIcon icon5 = new FontIcon("fa-bug");
        icon5.getStyleClass().add("totalsIcon");
        lblIconoTotalMultas.setGraphic(icon5);
        lblIconoTotalMultas.setText(null);
        FontIcon icon6 = new FontIcon("fa-balance-scale");
        icon6.getStyleClass().add("totalsIcon");
        lblIconoTotalAlicuotas.setGraphic(icon6);
        lblIconoTotalAlicuotas.setText(null);
        FontIcon icon7 = new FontIcon("fa-birthday-cake");
        icon7.getStyleClass().add("totalsIcon");
        lblIconoTotalReservas.setGraphic(icon7);
        lblIconoTotalReservas.setText(null);
        FontIcon icon8 = new FontIcon("fa-usd");
        icon8.getStyleClass().add("totalsIcon");
        lblIconoTotalGeneral.setGraphic(icon8);
        lblIconoTotalGeneral.setText(null);
        FontIcon icon9 = new FontIcon("fa-file-excel-o");
        icon9.getStyleClass().add("optionsIcon");
        lblIconoGuardar.setGraphic(icon9);
        lblIconoGuardar.setText(null);
        FontIcon icon10 = new FontIcon("fa-history");
        icon10.getStyleClass().add("optionsIcon");
        lblIconoLimpiar.setGraphic(icon10);
        lblIconoLimpiar.setText(null);

        FontIcon icon11 = new FontIcon("fa-search");
        icon11.getStyleClass().add("optionsIcon");
        icon11.getStyleClass().add("optionsIcon--white");
        btnBuscar.setGraphic(icon11);




    }
}
