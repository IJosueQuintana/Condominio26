package fis.dsw.sgc.finanzas.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// Controlador de la vista Generar Certificado de No Deudor

public class GenerarCertificadoNoDeudorController {

    @FXML private DatePicker dpInicio;
    @FXML private DatePicker dpFin;
    @FXML private TextArea txtObservaciones;
    @FXML private Label lblMensaje;
    @FXML private Label lblResumen;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_LOCAL_DATE;

    @FXML
    public void initialize() {
        LocalDate hoy = LocalDate.now();
        dpInicio.setValue(hoy.withDayOfMonth(1));
        dpFin.setValue(hoy);
        dpInicio.setPromptText("Fecha de inicio");
        dpFin.setPromptText("Fecha de fin");
        setMensaje("Seleccione el periodo y pulse Generar certificado.", "message-info");
    }

    @FXML
    void generar(ActionEvent event) {
        LocalDate ini = dpInicio.getValue();
        LocalDate fin = dpFin.getValue();
        String obs = txtObservaciones.getText() == null ? "" : txtObservaciones.getText().trim();

        if (ini == null || fin == null) {
            setMensaje("Fecha de inicio y fecha de fin son obligatorias.", "message-error");
            return;
        }
        if (fin.isBefore(ini)) {
            setMensaje("La fecha fin debe ser mayor o igual a la fecha inicio.", "message-error");
            return;
        }

        // Valores de ejemplo hasta conectar con el estado del residente
        double agua = 320.00;
        double luz = 410.50;
        double telefono = 80.00;
        double internet = 95.00;
        double sueldos = 1500.00;
        double otros = 200.00;
        double totalGastos = agua + luz + telefono + internet + sueldos + otros;

        double multas = 120.00;
        double alicuotas = 1800.00;
        double reservas = 350.00;
        double totalIngresos = multas + alicuotas + reservas;
        double balance = totalIngresos - totalGastos;

        String periodo = ini.format(FMT) + " → " + fin.format(FMT);
        String obsLine = obs.isEmpty() ? "(sin observaciones)" : obs;

        lblResumen.setText(
                "Periodo: " + periodo + "\n"
                        + "Observaciones: " + obsLine + "\n\n"
                        + "GASTOS\n"
                        + "  Agua: $" + f(agua) + " | Luz: $" + f(luz)
                        + " | Teléfono: $" + f(telefono) + " | Internet: $" + f(internet) + "\n"
                        + "  Sueldos: $" + f(sueldos) + " | Otros: $" + f(otros) + "\n"
                        + "  TOTAL GASTOS: $" + f(totalGastos) + "\n\n"
                        + "INGRESOS\n"
                        + "  Multas: $" + f(multas) + " | Alícuotas: $" + f(alicuotas)
                        + " | Reservas: $" + f(reservas) + "\n"
                        + "  TOTAL INGRESOS: $" + f(totalIngresos) + "\n\n"
                        + "BALANCE (ingresos − gastos): $" + f(balance)
        );

        setMensaje(
                "Certificado de No Deudor generado exitosamente. Disponible para consulta del residente.",
                "message-success"
        );
    }

    @FXML
    void limpiar(ActionEvent event) {
        LocalDate hoy = LocalDate.now();
        dpInicio.setValue(hoy.withDayOfMonth(1));
        dpFin.setValue(hoy);
        txtObservaciones.clear();
        lblResumen.setText("Genere un Certificado de no Deudor para conocer el estado financiero del residente.");
        setMensaje("Certificado listo. Seleccione un nuevo periodo.", "message-info");
    }

    private void setMensaje(String texto, String estilo) {
        lblMensaje.getStyleClass().removeAll("message-info", "message-success", "message-error");
        if (!lblMensaje.getStyleClass().contains("message-label")) {
            lblMensaje.getStyleClass().add("message-label");
        }
        lblMensaje.getStyleClass().add(estilo);
        lblMensaje.setText(texto);
    }

    private static String f(double v) {
        return String.format(java.util.Locale.US, "%.2f", v);
    }
}
