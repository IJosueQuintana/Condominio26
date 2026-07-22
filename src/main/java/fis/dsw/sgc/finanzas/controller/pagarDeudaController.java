package fis.dsw.sgc.finanzas.controller;

import fis.dsw.sgc.core.util.NavigationUtil;
import fis.dsw.sgc.finanzas.dto.PagoTarjetaDTO;
import fis.dsw.sgc.finanzas.service.IPagoService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

// Controlador de la vista Pagar deuda
public class pagarDeudaController {

    @FXML private TextField txtIdDeuda;
    @FXML private Label lblValor;
    @FXML private Label lblEstado;
    @FXML private ComboBox<String> cmbMetodo;
    @FXML private Label lblDatosBancarios;
    @FXML private TextField txtComprobante;
    @FXML private Button btnConsultar;
    @FXML private Button btnPagar;
    @FXML private Button btnLimpiar;
    @FXML private Label lblMensaje;

    private boolean deudaCargada;
    private String estadoActual = "";
    private final IPagoService pagoService;

    public pagarDeudaController(IPagoService pagoService) {
        this.pagoService = pagoService;
    }

    @FXML
    public void initialize() {
        cmbMetodo.setItems(FXCollections.observableArrayList(
                "EFECTIVO",
                "TRANSFERENCIA",
                "TARJETA"
        ));
        cmbMetodo.getSelectionModel().selectFirst();
        cmbMetodo.valueProperty().addListener((obs, oldV, newV) -> actualizarMetodo(newV));
        actualizarMetodo(cmbMetodo.getValue());
        setMensaje("Ingrese el ID de la deuda y pulse Consultar deuda.", "message-info");
    }

    @FXML
    void consultarDeuda(ActionEvent event) {
        String idDeuda = texto(txtIdDeuda);
        if (idDeuda.isEmpty()) {
            setMensaje("Debe ingresar el ID de la deuda.", "message-error");
            return;
        }
        if (idDeuda.equalsIgnoreCase("DEU-404") || idDeuda.equalsIgnoreCase("NO-EXISTE")) {
            deudaCargada = false;
            lblValor.setText("(sin datos)");
            lblEstado.setText("(sin datos)");
            setMensaje("No existe una deuda con el identificador proporcionado.", "message-error");
            return;
        }
        if (idDeuda.equalsIgnoreCase("DEU-PAGADA") || idDeuda.toUpperCase().endsWith("-PAGADA")) {
            deudaCargada = true;
            lblValor.setText("$0.00");
            lblEstado.setText("PAGADA");
            estadoActual = "PAGADA";
            setMensaje("Esta deuda ya ha sido pagada.", "message-error");
            return;
        }

        // Datos de ejemplo hasta conectar el servicio
        deudaCargada = true;
        lblValor.setText("$45.00");
        lblEstado.setText("PENDIENTE");
        estadoActual = "PENDIENTE";
        setMensaje("Deuda encontrada. Elija el método de pago y pulse Pagar.", "message-success");
    }


    @FXML
    void pagar(ActionEvent event) {
        String idDeuda = texto(txtIdDeuda);
        String metodo = cmbMetodo.getValue() == null ? "" : cmbMetodo.getValue();

        if (idDeuda.isEmpty()) {
            setMensaje("Debe ingresar el ID de la deuda.", "message-error");
            return;
        }
        if (!deudaCargada) {
            setMensaje("Consulte la deuda antes de pagar.", "message-error");
            return;
        }
        if ("PAGADA".equalsIgnoreCase(estadoActual)) {
            setMensaje("Esta deuda ya ha sido pagada.", "message-error");
            return;
        }

        Integer idNumerico = idDeudaNumerico(idDeuda);

        if ("EFECTIVO".equals(metodo) || "TRANSFERENCIA".equals(metodo)) {
            if ("TRANSFERENCIA".equals(metodo)) {
                String comp = texto(txtComprobante);
                if (comp.isEmpty()) {
                    setMensaje("Ingrese el comprobante de depósito.", "message-error");
                    return;
                }
                lblDatosBancarios.setText(
                        "Banco Pichincha | Cuenta 2201234567 | Condominio Los Sauces | RUC 1799999999001");
            }
            try {
                pagoService.pagarDeuda(idNumerico, metodo);
                lblEstado.setText("EN PROCESO");
                estadoActual = "EN PROCESO";
                if ("EFECTIVO".equals(metodo)) {
                    setMensaje("Acérquese a las oficinas de contabilidad para efectuar el pago.", "message-success");
                } else {
                    setMensaje(
                            "Se revisará el depósito y se actualizará el estado de su deuda en las próximas horas.",
                            "message-success");
                }
            } catch (RuntimeException ex) {
                setMensaje(ex.getMessage(), "message-error");
            }
            return;
        }

        if ("TARJETA".equals(metodo)) {
            SimularPagoTarjetaController ctrl;
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/finanzas/fxml/simularPagoTarjeta.fxml"));
                ctrl = new SimularPagoTarjetaController();
                loader.setController(ctrl);
                Parent root = loader.load();
                ctrl.setIdDeuda(idDeuda);
                NavigationUtil.openNewWindow(event, root, "Sistema externo de pago");
            } catch (Exception ex) {
                setMensaje("No se pudo abrir la ventana de pago con tarjeta.", "message-error");
                return;
            }
            if (ctrl.isPagoRechazado()) {
                setMensaje("El pago con tarjeta fue rechazado. Intente de nuevo.", "message-error");
                return;
            }
            if (ctrl.isPagoAceptado()) {
                try {
                    PagoTarjetaDTO pago = new PagoTarjetaDTO(
                            idNumerico,
                            ctrl.getNumeroTarjeta(),
                            ctrl.getFechaVencimiento(),
                            ctrl.getNombreTitular(),
                            ctrl.getCcv());
                    pagoService.pagarDeudaTarjeta(pago);
                    lblEstado.setText("PAGADA");
                    estadoActual = "PAGADA";
                    setMensaje("Deuda cancelada exitosamente.", "message-success");
                } catch (RuntimeException ex) {
                    setMensaje(ex.getMessage(), "message-error");
                }
            }
        }
    }

    private Integer idDeudaNumerico(String idDeuda) {
        try {
            return Integer.valueOf(idDeuda.replaceAll("\\D", ""));
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    @FXML
    void limpiar(ActionEvent event) {
        txtIdDeuda.clear();
        txtComprobante.clear();
        lblValor.setText("(consulte la deuda)");
        lblEstado.setText("(consulte la deuda)");
        deudaCargada = false;
        estadoActual = "";
        cmbMetodo.getSelectionModel().selectFirst();
        actualizarMetodo(cmbMetodo.getValue());
        setMensaje("Formulario listo para un nuevo pago.", "message-info");
    }

    private void actualizarMetodo(String metodo) {
        boolean transferencia = "TRANSFERENCIA".equals(metodo);
        txtComprobante.setDisable(!transferencia);
        if (!transferencia) {
            txtComprobante.clear();
        }
        if (transferencia) {
            lblDatosBancarios.setText(
                    "Banco Pichincha | Cuenta 2201234567 | Condominio Los Sauces | RUC 1799999999001");
            txtComprobante.setPromptText("Número de comprobante de depósito");
        } else if ("TARJETA".equals(metodo)) {
            lblDatosBancarios.setText("Al pagar se abrirá la ventana del sistema externo.");
            txtComprobante.setPromptText("No aplica con tarjeta");
        } else {
            lblDatosBancarios.setText("No aplica en efectivo.");
            txtComprobante.setPromptText("No aplica en efectivo");
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

    private static String texto(TextField field) {
        return field.getText() == null ? "" : field.getText().trim();
    }
}
