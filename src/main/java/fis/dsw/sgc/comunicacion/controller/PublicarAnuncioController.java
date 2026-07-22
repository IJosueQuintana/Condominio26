package fis.dsw.sgc.comunicacion.controller;

import fis.dsw.sgc.comunicacion.dto.AnuncioResumenDTO;
import fis.dsw.sgc.comunicacion.dto.PublicarAnuncioDTO;
import fis.dsw.sgc.comunicacion.exception.ComunicacionException;
import fis.dsw.sgc.comunicacion.service.IComunicacionService;
import fis.dsw.sgc.comunicacion.util.ComunicacionCatalogos;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.Objects;

import java.time.LocalDate;

public class PublicarAnuncioController {
    private static final int LIMITE_TITULO=120,LIMITE_CONTENIDO=2000;

    @FXML private ComboBox<String> cmbTipoAnuncio,cmbPrioridad;
    @FXML private DatePicker dpFechaPublicacion,dpFechaExpiracion;
    @FXML private TextField txtTitulo;
    @FXML private TextArea txtContenido;
    @FXML private Label lblContadorTitulo,lblContadorContenido,lblMensaje;
    @FXML private TableView<AnuncioResumenDTO> tblAnunciosRecientes;
    @FXML private TableColumn<AnuncioResumenDTO,String> colFecha,colTipo,colPrioridad,colTitulo,colVigencia,colEstado;
    @FXML private Button btnPublicar;

    private final ObservableList<AnuncioResumenDTO> datos=FXCollections.observableArrayList();
    private IComunicacionService service;

    @FXML public void initialize() {
        cmbTipoAnuncio.setItems(FXCollections.observableArrayList(
                "Anuncio general","Aviso de mantenimiento","Boletín informativo","Alerta de emergencia"));
        cmbPrioridad.setItems(FXCollections.observableArrayList("Baja","Normal","Alta","Urgente"));
        cmbTipoAnuncio.getSelectionModel().selectFirst();
        cmbPrioridad.getSelectionModel().select("Normal");
        dpFechaPublicacion.setValue(LocalDate.now());
        dpFechaExpiracion.setValue(LocalDate.now().plusDays(7));

        txtTitulo.setTextFormatter(new TextFormatter<String>(c->
                c.getControlNewText().length()<=LIMITE_TITULO?c:null));
        txtContenido.setTextFormatter(new TextFormatter<String>(c->
                c.getControlNewText().length()<=LIMITE_CONTENIDO?c:null));
        txtTitulo.textProperty().addListener((o,a,n)->contadores());
        txtContenido.textProperty().addListener((o,a,n)->contadores());

        colFecha.setCellValueFactory(d->new ReadOnlyStringWrapper(d.getValue().fecha()));
        colTipo.setCellValueFactory(d->new ReadOnlyStringWrapper(d.getValue().tipo()));
        colPrioridad.setCellValueFactory(d->new ReadOnlyStringWrapper(d.getValue().prioridad()));
        colTitulo.setCellValueFactory(d->new ReadOnlyStringWrapper(d.getValue().titulo()));
        colVigencia.setCellValueFactory(d->new ReadOnlyStringWrapper(d.getValue().vigencia()));
        colEstado.setCellValueFactory(d->new ReadOnlyStringWrapper(d.getValue().estado()));
        tblAnunciosRecientes.setItems(datos);
        tblAnunciosRecientes.setPlaceholder(new Label("No existen anuncios recientes."));
        contadores();

    }
    public void setComunicacionService(
            IComunicacionService service
    ) {
        this.service = Objects.requireNonNull(
                service,
                "El servicio de Comunicación no puede ser nulo."
        );

        cargarAnuncios();
    }

    @FXML private void publicarAnuncio() {
        if (!validar()||service==null) return;
        btnPublicar.setDisable(true);
        try {
            long id=service.publicarAnuncio(new PublicarAnuncioDTO(
                    service.obtenerIdEmisorActual(),
                    ComunicacionCatalogos.codigoTipoAnuncio(cmbTipoAnuncio.getValue()),
                    ComunicacionCatalogos.codigoPrioridad(cmbPrioridad.getValue()),
                    txtTitulo.getText().trim(),txtContenido.getText().trim(),
                    dpFechaPublicacion.getValue(),dpFechaExpiracion.getValue()));
            cargarAnuncios();
            limpiarRedaccion();
            mensaje("Anuncio publicado correctamente. Registro #"+id+".","message-success");
        } catch (ComunicacionException e) {
            mensaje(e.getMessage(),"message-error");
        } finally {
            btnPublicar.setDisable(false);
        }
    }

    @FXML private void limpiarFormulario() {
        cmbTipoAnuncio.getSelectionModel().selectFirst();
        cmbPrioridad.getSelectionModel().select("Normal");
        dpFechaPublicacion.setValue(LocalDate.now());
        dpFechaExpiracion.setValue(LocalDate.now().plusDays(7));
        limpiarRedaccion();
        mensaje("Formulario limpiado.","message-info");
    }

    @FXML private void cancelar() { limpiarFormulario(); mensaje("Operación cancelada.","message-info"); }

    @FXML private void seleccionarAnuncio() {
        AnuncioResumenDTO d=tblAnunciosRecientes.getSelectionModel().getSelectedItem();
        if (d!=null) mensaje("Seleccionado: “"+d.titulo()+"” · "+d.estado()+".","message-info");
    }

    public void cargarAnuncios() {
        if (service==null) return;
        try { datos.setAll(service.obtenerAnunciosRecientes(25)); }
        catch (ComunicacionException e) { mensaje(e.getMessage(),"message-error"); }
    }

    private boolean validar() {
        if (cmbTipoAnuncio.getValue()==null) return error("Seleccione el tipo.",cmbTipoAnuncio);
        if (cmbPrioridad.getValue()==null) return error("Seleccione la prioridad.",cmbPrioridad);
        if (dpFechaPublicacion.getValue()==null) return error("Seleccione la publicación.",dpFechaPublicacion);
        if (dpFechaExpiracion.getValue()==null) return error("Seleccione la expiración.",dpFechaExpiracion);
        if (dpFechaExpiracion.getValue().isBefore(dpFechaPublicacion.getValue()))
            return error("La expiración no puede ser anterior.",dpFechaExpiracion);
        if (txtTitulo.getText()==null||txtTitulo.getText().trim().length()<5)
            return error("Ingrese un título de al menos 5 caracteres.",txtTitulo);
        if (txtContenido.getText()==null||txtContenido.getText().trim().length()<10)
            return error("Ingrese un contenido de al menos 10 caracteres.",txtContenido);
        return true;
    }

    private boolean error(String m,Control c) { mensaje(m,"message-error"); c.requestFocus(); return false; }
    private void limpiarRedaccion() { txtTitulo.clear(); txtContenido.clear(); contadores(); }
    private void contadores() {
        lblContadorTitulo.setText((txtTitulo.getText()==null?0:txtTitulo.getText().length())+" / "+LIMITE_TITULO);
        lblContadorContenido.setText((txtContenido.getText()==null?0:txtContenido.getText().length())+" / "+LIMITE_CONTENIDO);
    }
    private void mensaje(String t,String c) { lblMensaje.setText(t); lblMensaje.getStyleClass().setAll("message-label",c); }
}
