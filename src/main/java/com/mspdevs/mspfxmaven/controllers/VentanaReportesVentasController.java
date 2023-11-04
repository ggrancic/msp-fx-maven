package com.mspdevs.mspfxmaven.controllers;

import java.net.URL;
import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.mspdevs.mspfxmaven.model.Venta;
import com.mspdevs.mspfxmaven.utils.Alerta;

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

public class VentanaReportesVentasController implements Initializable {
	
	int registrosPorPagina = 15;
	
	int pagina;
	
	int cantidadDePaginas = 0;
	
	int empezarDesde = 0;
	
	int currentPageIndex;
	
	int total;
	
	int offsets;
	
	Date fechaInicio = null;
	
	Date fechaFin = null;
	
	DateTimeFormatter formatoMySql = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	Alerta msj = new Alerta();
	
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
    private TableColumn<?, ?> columnaCliente;

    @FXML
    private TableColumn<?, ?> columnaFecha;

    @FXML
    private TableColumn<?, ?> columnaIVA;

    @FXML
    private TableColumn<?, ?> columnaNroFac;

    @FXML
    private TableColumn<?, ?> columnaSubTot;

    @FXML
    private TableColumn<?, ?> columnaTipo;

    @FXML
    private TableColumn<?, ?> columnaTotal;
    
    @FXML
    private TableView<Venta> tablaCompras;

    @FXML
    private GridPane gPane;

    @FXML
    private Label label20reg;
    
    
    // ----- METODOS -----

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		btnAnterior.setDisable(true);
	    btnSiguiente.setDisable(true);
		
	}
}
