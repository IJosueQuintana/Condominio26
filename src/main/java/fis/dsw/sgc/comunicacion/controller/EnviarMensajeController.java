package fis.dsw.sgc.comunicacion.controller;

import fis.dsw.sgc.comunicacion.dto.EnviarComunicacionDTO;
import fis.dsw.sgc.comunicacion.dto.MensajeResumenDTO;
import fis.dsw.sgc.comunicacion.exception.ComunicacionException;
import fis.dsw.sgc.comunicacion.service.IComunicacionService;
import fis.dsw.sgc.comunicacion.util.ComunicacionCatalogos;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.Objects;
import java.util.LinkedHashMap;
import java.util.Map;


public class EnviarMensajeController {
    private static final int LIMITE_ASUNTO=120, LIMITE_CONTENIDO=2000;
    private static final String RES="Mensaje a residentes", TRA="Comunicado a trabajadores",
            GLO="Mensaje global", URG="Mensaje urgente", ALE="Alerta de emergencia",
            BOL="Boletín informativo";
    private static final Map<String,String> DESTINATARIOS=mapa();

    @FXML private ComboBox<String> cmbTipoMensaje,cmbPrioridad;
    @FXML private TextField txtAsunto;
    @FXML private TextArea txtContenido;
    @FXML private Label lblDestinatarios,lblContadorAsunto,lblContadorCaracteres,lblMensaje;
    @FXML private TableView<MensajeResumenDTO> tblMensajesRecientes;
    @FXML private TableColumn<MensajeResumenDTO,String> colFecha,colTipo,colPrioridad,colAsunto,colEstado;
    @FXML private Button btnEnviar;

    private final ObservableList<MensajeResumenDTO> datos=FXCollections.observableArrayList();
    private IComunicacionService service;

    @FXML public void initialize() {
        cmbTipoMensaje.setItems(FXCollections.observableArrayList(RES,TRA,GLO,URG,ALE,BOL));
        cmbPrioridad.setItems(FXCollections.observableArrayList("Baja","Normal","Alta","Urgente"));
        cmbTipoMensaje.getSelectionModel().select(RES);
        cmbPrioridad.getSelectionModel().select("Normal");

        txtAsunto.setTextFormatter(new TextFormatter<String>(c->
                c.getControlNewText().length()<=LIMITE_ASUNTO?c:null));
        txtContenido.setTextFormatter(new TextFormatter<String>(c->
                c.getControlNewText().length()<=LIMITE_CONTENIDO?c:null));

        colFecha.setCellValueFactory(d->new ReadOnlyStringWrapper(d.getValue().fecha()));
        colTipo.setCellValueFactory(d->new ReadOnlyStringWrapper(d.getValue().tipo()));
        colPrioridad.setCellValueFactory(d->new ReadOnlyStringWrapper(d.getValue().prioridad()));
        colAsunto.setCellValueFactory(d->new ReadOnlyStringWrapper(d.getValue().asunto()));
        colEstado.setCellValueFactory(d->new ReadOnlyStringWrapper(d.getValue().estado()));
        tblMensajesRecientes.setItems(datos);
        tblMensajesRecientes.setPlaceholder(new Label("No existen comunicaciones recientes."));

        cmbTipoMensaje.valueProperty().addListener((o,a,n)->{
            actualizarDestinatarios();
            if (URG.equals(n)||ALE.equals(n)) cmbPrioridad.getSelectionModel().select("Urgente");
            else if (BOL.equals(n)) cmbPrioridad.getSelectionModel().select("Baja");
            else cmbPrioridad.getSelectionModel().select("Normal");
        });
        txtAsunto.textProperty().addListener((o,a,n)->actualizarContadores());
        txtContenido.textProperty().addListener((o,a,n)->actualizarContadores());

        actualizarDestinatarios();
        actualizarContadores();

    }
    public void setComunicacionService(
            IComunicacionService service
    ) {
        this.service = Objects.requireNonNull(
                service,
                "El servicio de Comunicación no puede ser nulo."
        );

        cargarMensajes();
    }
    @FXML private void enviarMensaje() {
        if (!validar()||service==null) return;
        btnEnviar.setDisable(true);
        try {
            long id=service.enviarMensaje(new EnviarComunicacionDTO(
                    service.obtenerIdEmisorActual(),
                    null,
                    ComunicacionCatalogos.codigoTipoMensaje(cmbTipoMensaje.getValue()),
                    ComunicacionCatalogos.codigoPrioridad(cmbPrioridad.getValue()),
                    txtAsunto.getText().trim(),
                    txtContenido.getText().trim()));
            cargarMensajes();
            limpiarCampos();
            mensaje("Mensaje enviado correctamente. Registro #"+id+".","message-success");
        } catch (ComunicacionException e) {
            mensaje(e.getMessage(),"message-error");
        } finally {
            btnEnviar.setDisable(false);
        }
    }

    @FXML private void limpiarFormulario() {
        cmbTipoMensaje.getSelectionModel().select(RES);
        cmbPrioridad.getSelectionModel().select("Normal");
        limpiarCampos();
        mensaje("Formulario limpiado.","message-info");
    }

    @FXML private void cancelar() {
        limpiarFormulario();
        mensaje("Operación cancelada.","message-info");
    }

    @FXML private void seleccionarMensaje() {
        MensajeResumenDTO d=tblMensajesRecientes.getSelectionModel().getSelectedItem();
        if (d!=null) mensaje("Seleccionado: “"+d.asunto()+"” · "+d.estado()+".","message-info");
    }

    public void cargarMensajes() {
        if (service==null) return;
        try { datos.setAll(service.obtenerMensajesRecientes(25)); }
        catch (ComunicacionException e) { mensaje(e.getMessage(),"message-error"); }
    }

    private boolean validar() {
        if (cmbTipoMensaje.getValue()==null) return error("Seleccione el tipo.",cmbTipoMensaje);
        if (cmbPrioridad.getValue()==null) return error("Seleccione la prioridad.",cmbPrioridad);
        if (txtAsunto.getText()==null||txtAsunto.getText().trim().length()<5)
            return error("Ingrese un asunto de al menos 5 caracteres.",txtAsunto);
        if (txtContenido.getText()==null||txtContenido.getText().trim().length()<10)
            return error("Ingrese un contenido de al menos 10 caracteres.",txtContenido);
        return true;
    }

    private boolean error(String m,Control c) { mensaje(m,"message-error"); c.requestFocus(); return false; }
    private void limpiarCampos() { txtAsunto.clear(); txtContenido.clear(); actualizarContadores(); }
    private void actualizarDestinatarios() {
        lblDestinatarios.setText(DESTINATARIOS.getOrDefault(cmbTipoMensaje.getValue(),"Destinatarios por definir."));
    }
    private void actualizarContadores() {
        lblContadorAsunto.setText((txtAsunto.getText()==null?0:txtAsunto.getText().length())+" / "+LIMITE_ASUNTO);
        lblContadorCaracteres.setText((txtContenido.getText()==null?0:txtContenido.getText().length())+" / "+LIMITE_CONTENIDO);
    }
    private void mensaje(String t,String c) { lblMensaje.setText(t); lblMensaje.getStyleClass().setAll("message-label",c); }

    private static Map<String,String> mapa() {
        Map<String,String> m=new LinkedHashMap<>();
        m.put(RES,"Destinatarios: residentes y propietarios activos.");
        m.put(TRA,"Destinatarios: administración, seguridad y presidencia.");
        m.put(GLO,"Destinatarios: todos los usuarios activos.");
        m.put(URG,"Destinatarios: todos los usuarios activos con prioridad urgente.");
        m.put(ALE,"Destinatarios: toda la comunidad ante una emergencia.");
        m.put(BOL,"Destinatarios: toda la comunidad del condominio.");
        return Map.copyOf(m);
    }
}
