package fis.dsw.sgc.reservas.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

import fis.dsw.sgc.reservas.dto.EspacioReservableDTO;
import fis.dsw.sgc.reservas.model.Reserva;
import fis.dsw.sgc.reservas.service.IServicioReservas;
import fis.dsw.sgc.reservas.service.ServicioReservasImpl;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AnadirReservaController {

    // TODO(GRB): reemplazar por el id del usuario autenticado (SesionUsuario).
    private static final int ID_RESIDENTE_ACTUAL = 2; // 'Carlos Residente' en seed.sql

    private final IServicioReservas servicioReservas = new ServicioReservasImpl();

    // Mapa nombre-espacio -> datos del espacio (para obtener id, costo, etc.)
    private final Map<String, EspacioReservableDTO> espaciosPorNombre = new LinkedHashMap<>();

    @FXML
    private ChoiceBox<String> cbTipoEspacio;

    @FXML
    private javafx.scene.control.TextField txtFecha;

    @FXML
    private javafx.scene.control.TextField txtHoraInicio;

    @FXML
    private javafx.scene.control.TextField txtHoraFin;

    @FXML
    private TableView<HorarioFila> tablaHorarios;

    @FXML
    private TableColumn<HorarioFila, String> colHora;

    @FXML
    private TableColumn<HorarioFila, String> colLunes;

    @FXML
    private TableColumn<HorarioFila, String> colMartes;

    @FXML
    private TableColumn<HorarioFila, String> colMiercoles;

    @FXML
    private TableColumn<HorarioFila, String> colJueves;

    @FXML
    private TableColumn<HorarioFila, String> colViernes;

    @FXML
    private TableColumn<HorarioFila, String> colSabado;

    @FXML
    private TableColumn<HorarioFila, String> colDomingo;

    @FXML
    private VBox panelError;

    @FXML
    private VBox panelPrincipal;

    private ObservableList<HorarioFila> datosHorario = FXCollections.observableArrayList();
    
    private boolean seleccionandoInicio = true;
    private TableColumn<HorarioFila, String> columnaBloqueada = null;
    private TableColumn<HorarioFila, String> columnaSeleccion = null;
    private int indiceInicio = -1;
    private int indiceFin = -1;

    @FXML
    public void initialize() {
        // Cargar los espacios reservables reales desde la BD.
        espaciosPorNombre.clear();
        for (EspacioReservableDTO e : servicioReservas.listarEspaciosDisponibles()) {
            espaciosPorNombre.put(e.getNombre(), e);
            cbTipoEspacio.getItems().add(e.getNombre());
        }

        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colLunes.setCellValueFactory(new PropertyValueFactory<>("lunes"));
        colMartes.setCellValueFactory(new PropertyValueFactory<>("martes"));
        colMiercoles.setCellValueFactory(new PropertyValueFactory<>("miercoles"));
        colJueves.setCellValueFactory(new PropertyValueFactory<>("jueves"));
        colViernes.setCellValueFactory(new PropertyValueFactory<>("viernes"));
        colSabado.setCellValueFactory(new PropertyValueFactory<>("sabado"));
        colDomingo.setCellValueFactory(new PropertyValueFactory<>("domingo"));

        javafx.util.Callback<TableColumn<HorarioFila, String>, TableCell<HorarioFila, String>> cellFactory = column -> {
            TableCell<HorarioFila, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setAlignment(javafx.geometry.Pos.CENTER);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                        setBackground(Background.EMPTY);
                        setDisable(false);
                    } else {
                        setText(item);
                        boolean isOtherColumn = (columnaBloqueada != null && getTableColumn() != columnaBloqueada);
                        
                        if (isOtherColumn) {
                            setBackground(new Background(new BackgroundFill(Color.web("#f3f4f6"), CornerRadii.EMPTY, Insets.EMPTY)));
                            setStyle("-fx-text-fill: #d1d5db; -fx-cursor: default; -fx-font-size: 11px;");
                            setDisable(true);
                        } else {
                            setDisable(false);
                            boolean isSelected = false;
                            if (columnaSeleccion != null && getTableColumn() == columnaSeleccion) {
                                int rIndex = getIndex();
                                if (indiceFin == -1 && rIndex == indiceInicio) {
                                    isSelected = true;
                                } else if (indiceFin != -1) {
                                    int minRow = Math.min(indiceInicio, indiceFin);
                                    int maxRow = Math.max(indiceInicio, indiceFin);
                                    if (rIndex >= minRow && rIndex <= maxRow) {
                                        isSelected = true;
                                    }
                                }
                            }

                            if (item.equals("Reservado")) {
                                setBackground(new Background(new BackgroundFill(Color.web("#ffebee"), CornerRadii.EMPTY, Insets.EMPTY)));
                                setStyle("-fx-text-fill: #c62828; -fx-font-weight: bold; -fx-cursor: default; -fx-font-size: 11px;");
                            } else if (item.equals("Mantenimiento")) {
                                setBackground(new Background(new BackgroundFill(Color.web("#fff8e1"), CornerRadii.EMPTY, Insets.EMPTY)));
                                setStyle("-fx-text-fill: #f57f17; -fx-font-weight: bold; -fx-cursor: default; -fx-font-size: 11px;");
                            } else {
                                if (isSelected) {
                                    setBackground(new Background(new BackgroundFill(Color.web("#e0f2fe"), CornerRadii.EMPTY, Insets.EMPTY)));
                                    setStyle("-fx-text-fill: #0284c7; -fx-font-weight: bold; -fx-cursor: hand; -fx-font-size: 12px;");
                                } else {
                                    setBackground(new Background(new BackgroundFill(Color.web("#f9fafb"), CornerRadii.EMPTY, Insets.EMPTY)));
                                    setStyle("-fx-text-fill: #9ca3af; -fx-cursor: hand; -fx-font-size: 12px;");
                                }
                            }
                        }
                    }
                }
            };

            cell.setOnMouseClicked(event -> {
                if (cell.isEmpty() || cell.getItem() == null || cell.isDisabled()) return;
                
                String estado = cell.getItem();
                if (estado.equals("Reservado") || estado.equals("Mantenimiento")) {
                    return; 
                }

                HorarioFila fila = cell.getTableRow().getItem();
                if (fila == null) return;

                if (seleccionandoInicio) {
                    columnaBloqueada = cell.getTableColumn();
                    columnaSeleccion = cell.getTableColumn();
                    indiceInicio = cell.getIndex();
                    indiceFin = -1;
                    
                    int colIndex = cell.getTableView().getColumns().indexOf(columnaBloqueada);
                    
                    java.time.LocalDate today = java.time.LocalDate.now();
                    java.time.LocalDate monday = today.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
                    java.time.LocalDate clickedDate = monday.plusWeeks(semanaOffset).plusDays(colIndex - 1);
                    
                    txtFecha.setText(clickedDate.toString());
                    String horaInicio = fila.getHora().split(" - ")[0];
                    txtHoraInicio.setText(horaInicio);
                    txtHoraFin.clear();
                    
                    seleccionandoInicio = false;
                } else {
                    if (cell.getTableColumn() != columnaBloqueada) {
                        return;
                    }
                    indiceFin = cell.getIndex();
                    
                    int minRow = Math.min(indiceInicio, indiceFin);
                    int maxRow = Math.max(indiceInicio, indiceFin);
                    
                    boolean ocupado = false;
                    for (int i = minRow; i <= maxRow; i++) {
                        String estadoCelda = columnaBloqueada.getCellData(i);
                        if ("Reservado".equals(estadoCelda) || "Mantenimiento".equals(estadoCelda)) {
                            ocupado = true;
                            break;
                        }
                    }

                    if (ocupado) {
                        if (panelPrincipal != null) {
                            panelPrincipal.setVisible(false);
                            panelPrincipal.setManaged(false);
                        }
                        panelError.setVisible(true);
                        panelError.setManaged(true);
                        
                        limpiarSeleccion();
                        return;
                    }
                    
                    String horaInicio = tablaHorarios.getItems().get(minRow).getHora().split(" - ")[0];
                    String horaFin = tablaHorarios.getItems().get(maxRow).getHora().split(" - ")[1];
                    
                    txtHoraInicio.setText(horaInicio);
                    txtHoraFin.setText(horaFin);
                    
                    seleccionandoInicio = true;
                    columnaBloqueada = null; // Liberar columnas para nueva selección
                }
                tablaHorarios.refresh();
            });

            return cell;
        };

        colLunes.setCellFactory(cellFactory);
        colMartes.setCellFactory(cellFactory);
        colMiercoles.setCellFactory(cellFactory);
        colJueves.setCellFactory(cellFactory);
        colViernes.setCellFactory(cellFactory);
        colSabado.setCellFactory(cellFactory);
        colDomingo.setCellFactory(cellFactory);

        // Generar horas base desde 10:00 a 02:00
        generarHorasBase();

        // Al cambiar de espacio, se recargan los datos falsos y se limpia la selección
        cbTipoEspacio.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cargarDisponibilidad(newVal);
                limpiarSeleccion();
            }
        });

        // Seleccionar el primero por defecto
        cbTipoEspacio.getSelectionModel().selectFirst();
    }

    // ... (rest of imports remain)

    @FXML
    private javafx.scene.control.Button btnSemanaAnterior;

    @FXML
    private javafx.scene.control.Button btnSemanaSiguiente;

    @FXML
    private javafx.scene.control.Label lblSemana;

    private int semanaOffset = 0;

    private void generarHorasBase() {
        datosHorario.clear();
        String[] horas = {
            "08:00 - 10:00", "10:00 - 12:00", "12:00 - 14:00", "14:00 - 16:00",
            "16:00 - 18:00", "18:00 - 20:00", "20:00 - 22:00", "22:00 - 00:00",
            "00:00 - 02:00"
        };
        for (String h : horas) {
            datosHorario.add(new HorarioFila(h));
        }
        tablaHorarios.setItems(datosHorario);
        actualizarEtiquetaSemana();
    }

    @FXML
    void anteriorSemana(javafx.event.ActionEvent event) {
        if (semanaOffset > 0) {
            semanaOffset--;
            actualizarEtiquetaSemana();
            if (cbTipoEspacio.getValue() != null) {
                cargarDisponibilidad(cbTipoEspacio.getValue());
            }
            limpiarSeleccion();
        }
    }

    @FXML
    void siguienteSemana(javafx.event.ActionEvent event) {
        if (semanaOffset < 1) {
            semanaOffset++;
            actualizarEtiquetaSemana();
            if (cbTipoEspacio.getValue() != null) {
                cargarDisponibilidad(cbTipoEspacio.getValue());
            }
            limpiarSeleccion();
        }
    }

    private void actualizarEtiquetaSemana() {
        if (semanaOffset == 0) {
            lblSemana.setText("Semana Actual");
            btnSemanaAnterior.setDisable(true);
            btnSemanaSiguiente.setDisable(false);
        } else if (semanaOffset == 1) {
            lblSemana.setText("Siguiente Semana");
            btnSemanaAnterior.setDisable(false);
            btnSemanaSiguiente.setDisable(true);
        }
    }

    private void limpiarSeleccion() {
        seleccionandoInicio = true;
        columnaBloqueada = null;
        columnaSeleccion = null;
        indiceInicio = -1;
        indiceFin = -1;
        if (txtFecha != null) txtFecha.clear();
        if (txtHoraInicio != null) txtHoraInicio.clear();
        if (txtHoraFin != null) txtHoraFin.clear();
        if (tablaHorarios != null) tablaHorarios.refresh();
    }

    @FXML
    void cerrarPanelError(javafx.event.ActionEvent event) {
        if (panelError != null) {
            panelError.setVisible(false);
            panelError.setManaged(false);
        }
        if (panelPrincipal != null) {
            panelPrincipal.setVisible(true);
            panelPrincipal.setManaged(true);
        }
    }

    /**
     * Marca en la grilla semanal los bloques ya ocupados por reservas ACTIVAS
     * reales del espacio seleccionado, consultando la BD dia por dia.
     */
    private void cargarDisponibilidad(String espacioNombre) {
        // Limpiar todo primero
        for (HorarioFila fila : datosHorario) {
            fila.limpiar();
        }

        EspacioReservableDTO espacio = espaciosPorNombre.get(espacioNombre);
        if (espacio != null) {
            LocalDate hoy = LocalDate.now();
            LocalDate lunes = hoy.with(java.time.temporal.TemporalAdjusters
                    .previousOrSame(java.time.DayOfWeek.MONDAY)).plusWeeks(semanaOffset);

            for (int dia = 0; dia < 7; dia++) {
                LocalDate fecha = lunes.plusDays(dia);
                List<Reserva> reservas = servicioReservas
                        .listarReservasPorEspacioYFecha(espacio.getIdEspacioComun(), fecha.toString());
                for (Reserva r : reservas) {
                    marcarBloquesReservados(dia, r.getHoraInicio(), r.getHoraFin());
                }
            }
        }

        // Forzar refresco de la tabla
        tablaHorarios.refresh();
    }

    /** Marca como "Reservado" los slots de 2h que se solapan con [inicio, fin). */
    private void marcarBloquesReservados(int diaColumna, String horaInicio, String horaFin) {
        int inicioRes = aMinutos(horaInicio);
        int finRes = aMinutos(horaFin);
        if (inicioRes < 0 || finRes < 0) return;

        for (HorarioFila fila : datosHorario) {
            String[] rango = fila.getHora().split(" - ");
            if (rango.length != 2) continue;
            int slotInicio = aMinutos(rango[0]);
            int slotFin = aMinutos(rango[1]);
            if (slotFin <= slotInicio) slotFin += 24 * 60; // slot que cruza medianoche
            // Solapamiento de intervalos
            if (inicioRes < slotFin && slotInicio < finRes) {
                setDia(fila, diaColumna, "Reservado");
            }
        }
    }

    /** Convierte "HH:MM" (o "HH:MM:SS") a minutos desde medianoche; -1 si invalido. */
    private int aMinutos(String hora) {
        if (hora == null) return -1;
        try {
            String[] p = hora.trim().split(":");
            return Integer.parseInt(p[0]) * 60 + Integer.parseInt(p[1]);
        } catch (Exception e) {
            return -1;
        }
    }

    /** Asigna un valor a la columna de dia correspondiente (0=Lunes .. 6=Domingo). */
    private void setDia(HorarioFila fila, int diaColumna, String valor) {
        switch (diaColumna) {
            case 0: fila.setLunes(valor); break;
            case 1: fila.setMartes(valor); break;
            case 2: fila.setMiercoles(valor); break;
            case 3: fila.setJueves(valor); break;
            case 4: fila.setViernes(valor); break;
            case 5: fila.setSabado(valor); break;
            case 6: fila.setDomingo(valor); break;
            default: break;
        }
    }

    /**
     * Crea la reserva seleccionada en la grilla usando el servicio (que valida
     * mora y solapamiento y registra la deuda en Finanzas si corresponde).
     */
    @FXML
    void reservar(javafx.event.ActionEvent event) {
        String espacioNombre = cbTipoEspacio.getValue();
        String fecha = txtFecha.getText();
        String horaInicio = txtHoraInicio.getText();
        String horaFin = txtHoraFin.getText();

        EspacioReservableDTO espacio = espaciosPorNombre.get(espacioNombre);
        if (espacio == null || fecha == null || fecha.isEmpty()
                || horaInicio == null || horaInicio.isEmpty()
                || horaFin == null || horaFin.isEmpty()) {
            mostrarError();
            return;
        }

        boolean ok = servicioReservas.crearReserva(ID_RESIDENTE_ACTUAL,
                espacio.getIdEspacioComun(), fecha, horaInicio, horaFin);

        if (ok) {
            // Recargar disponibilidad para reflejar la nueva reserva.
            cargarDisponibilidad(espacioNombre);
            limpiarSeleccion();
        } else {
            // Rechazada por mora o solapamiento (ver consola del servicio).
            mostrarError();
        }
    }

    private void mostrarError() {
        if (panelPrincipal != null) {
            panelPrincipal.setVisible(false);
            panelPrincipal.setManaged(false);
        }
        if (panelError != null) {
            panelError.setVisible(true);
            panelError.setManaged(true);
        }
        limpiarSeleccion();
    }

    // --- INNER CLASS PARA LA FILA DEL HORARIO ---
    public static class HorarioFila {
        private String hora;
        private String lunes = "Disponible";
        private String martes = "Disponible";
        private String miercoles = "Disponible";
        private String jueves = "Disponible";
        private String viernes = "Disponible";
        private String sabado = "Disponible";
        private String domingo = "Disponible";

        public HorarioFila(String hora) {
            this.hora = hora;
        }

        public void limpiar() {
            lunes = "Disponible";
            martes = "Disponible";
            miercoles = "Disponible";
            jueves = "Disponible";
            viernes = "Disponible";
            sabado = "Disponible";
            domingo = "Disponible";
        }

        // Getters
        public String getHora() { return hora; }
        public String getLunes() { return lunes; }
        public String getMartes() { return martes; }
        public String getMiercoles() { return miercoles; }
        public String getJueves() { return jueves; }
        public String getViernes() { return viernes; }
        public String getSabado() { return sabado; }
        public String getDomingo() { return domingo; }

        // Setters
        public void setHora(String hora) { this.hora = hora; }
        public void setLunes(String lunes) { this.lunes = lunes; }
        public void setMartes(String martes) { this.martes = martes; }
        public void setMiercoles(String miercoles) { this.miercoles = miercoles; }
        public void setJueves(String jueves) { this.jueves = jueves; }
        public void setViernes(String viernes) { this.viernes = viernes; }
        public void setSabado(String sabado) { this.sabado = sabado; }
        public void setDomingo(String domingo) { this.domingo = domingo; }
    }
}
