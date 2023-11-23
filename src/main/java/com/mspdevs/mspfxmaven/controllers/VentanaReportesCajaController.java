package com.mspdevs.mspfxmaven.controllers;

import com.mspdevs.mspfxmaven.model.Caja;
import com.mspdevs.mspfxmaven.model.DAO.CajaDAOImpl;
import com.mspdevs.mspfxmaven.model.DAO.CompraDAOImpl;
import com.mspdevs.mspfxmaven.model.Empleado;
import com.mspdevs.mspfxmaven.model.Venta;
import com.mspdevs.mspfxmaven.utils.Alerta;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class VentanaReportesCajaController implements Initializable {

    Alerta msj = new Alerta();

    Date fInicio = null;

    Date fFin = null;

    int registrosPorPagina = 20;

    int currentPageIndex;

    int total;

    int offsets;


    @FXML
    private Button btnAnterior;

    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnSiguiente;

    @FXML
    private DatePicker campoFechaDeFin;

    @FXML
    private DatePicker campoFechaDeInicio;

    @FXML
    private TableColumn<Caja, Double> colEgresos;

    @FXML
    private TableColumn<Caja, String> colFHApertura;

    @FXML
    private TableColumn<Caja, String> colFHCierre;

    @FXML
    private TableColumn<Caja, Double> colIngresos;

    @FXML
    private TableColumn<Caja, Double> colMF;

    @FXML
    private TableColumn<Caja, Double> colMI;

    @FXML
    private TableColumn<Caja, String> colResp;

    @FXML
    private GridPane gPane;

    @FXML
    private Label label20reg;

    @FXML
    private TableView<Caja> tablaCajas;


    // ----- Metodos custom -----

    public void completarTablaConPaginacion(int inicio, int elementosPorPagina, Date fechaInicio, Date fechaFin) {
        CajaDAOImpl caja = new CajaDAOImpl();
        ObservableList<Caja> cajas = null;

        try {
            cajas = caja.listarConLimitYFecha(inicio, elementosPorPagina, fechaInicio, fechaFin);
        } catch (Exception e) {
            msj.mostrarError("Error", "", "Se ha producido un error recuperando los datos de la BD");
            e.printStackTrace();
        }

        this.colFHApertura.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFechaHoraApertura().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        this.colFHCierre.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFechaHoraCierre().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        this.colMI.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMontoInicial()));
        this.colIngresos.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getIngresos()));
        this.colEgresos.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getEgresos()));
        this.colMF.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMontoFinal()));
        this.colResp.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getResponsable().getNombre_usuario()));

        this.tablaCajas.setItems(cajas);

    }

    public int obtenerTotalRegistros(Date fechaInicio, Date fechaFin) {
        CajaDAOImpl cajadao = new CajaDAOImpl();
        int total = 0;


        fechaInicio = Date.valueOf(campoFechaDeInicio.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        fechaFin = Date.valueOf(campoFechaDeFin.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        try {
            total = cajadao.obtenerCantidadDeCompras(fechaInicio, fechaFin);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public int obtenerTotalPaginas() {
        fInicio = Date.valueOf(campoFechaDeInicio.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        fFin = Date.valueOf(campoFechaDeFin.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        int cantidadDePaginas = (int) Math.ceil((double) obtenerTotalRegistros(fInicio, fFin) / registrosPorPagina);
        return cantidadDePaginas;
    }




    @FXML
    void abrirModalDetalle(MouseEvent event) {

    }

    @FXML
    void accionBtnAnterior(ActionEvent event) {
        if (currentPageIndex > 0) {
            btnSiguiente.setDisable(false);
            currentPageIndex--;
            offsets = currentPageIndex * registrosPorPagina;
            completarTablaConPaginacion(offsets, registrosPorPagina, fInicio, fFin);
            if (currentPageIndex == 0) {
                btnAnterior.setDisable(true);
            }
        }
    }

    @FXML
    void accionBtnSiguiente(ActionEvent event) {
        if (currentPageIndex < (total - 1) ) {
            btnAnterior.setDisable(false);
            currentPageIndex++;
            System.out.println(currentPageIndex);
            System.out.println(total);
            offsets = currentPageIndex * registrosPorPagina;
            completarTablaConPaginacion(offsets, registrosPorPagina, fInicio, fFin);
            if (currentPageIndex == total - 1) {
                btnSiguiente.setDisable(true);
            }
        }
    }

    @FXML
    void accionBuscarPorFecha(ActionEvent event) {
        if (campoFechaDeInicio.getValue() == null || campoFechaDeFin.getValue() == null) {
            msj.mostrarError("Error", "", "Debe ingresar la fecha de inicio y la fecha de fin.");

        } else if (campoFechaDeInicio.getValue().isAfter(campoFechaDeFin.getValue())) {
            msj.mostrarError("Error", "", "La fecha de INICIO debe ser anterior a la fecha de FIN.");
            Platform.runLater(() -> campoFechaDeInicio.requestFocus());

        } else if (campoFechaDeFin.getValue().isBefore(campoFechaDeInicio.getValue())) {
            msj.mostrarError("Error", "", "La fecha de FIN debe ser posterior a la fecha de INICIO.");
        } else {

            total = obtenerTotalPaginas();

            if (total > 1) {
                btnSiguiente.setDisable(false);
            }

            btnAnterior.setDisable(true);

            DateTimeFormatter formatoMySql = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            fInicio = Date.valueOf(campoFechaDeInicio.getValue());
            fFin = Date.valueOf(campoFechaDeFin.getValue().format(formatoMySql));

            label20reg.setText("Mostrando resultados DESDE " + fInicio + " HASTA " + fFin);



            tablaCajas.getItems().clear();
            completarTablaConPaginacion(0, 20, fInicio, fFin);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        completarTablaConPaginacion(0, 20, fInicio, fFin);
        btnAnterior.setDisable(true);
        btnSiguiente.setDisable(true);
    }
}
