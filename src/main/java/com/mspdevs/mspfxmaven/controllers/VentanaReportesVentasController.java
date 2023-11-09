package com.mspdevs.mspfxmaven.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.mspdevs.mspfxmaven.model.Cliente;
import com.mspdevs.mspfxmaven.model.Compra;
import com.mspdevs.mspfxmaven.model.Venta;
import com.mspdevs.mspfxmaven.model.DAO.CompraDAOImpl;
import com.mspdevs.mspfxmaven.model.DAO.VentaDAOImpl;
import com.mspdevs.mspfxmaven.utils.Alerta;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    private TableColumn<Venta, Cliente> columnaCliente;

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
    private TableView<Venta> tablaVentas;

    @FXML
    private GridPane gPane;

    @FXML
    private Label label20reg;
    
    
    // ----- METODOS -----
    
    
    public void completarTablaConPaginacion(int inicio, int elementosPorPagina, Date fechaInicio, Date fechaFin) {
        VentaDAOImpl venta = new VentaDAOImpl();
        ObservableList<Venta> ventas = null;

        try {
            ventas = venta.listarConLimitYFecha(inicio, elementosPorPagina, fechaInicio, fechaFin);
        } catch (Exception e) {
            msj.mostrarError("Error", "", "Se ha producido un error recuperando los datos de la BD");
            e.printStackTrace();
        }

        this.columnaNroFac.setCellValueFactory(new PropertyValueFactory<>("numeroFactura"));
        this.columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fechaEmision"));
        this.columnaTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        this.columnaCliente.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getCliente().getRazonSocial()));
        this.columnaSubTot.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        this.columnaIVA.setCellValueFactory(new PropertyValueFactory<>("iva"));
        this.columnaTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        this.tablaVentas.setItems(ventas);

    }
    
    
    public int obtenerTotalRegistros(Date fechaInicio, Date fechaFin) {
    	CompraDAOImpl compra = new CompraDAOImpl();
        int total = 0;
        
        
    	fechaInicio = Date.valueOf(campoFechaDeInicio.getValue().format(formatoMySql));
    	fechaFin = Date.valueOf(campoFechaDeFin.getValue().format(formatoMySql));
        
		try {
			total = compra.obtenerCantidadDeCompras(fechaInicio, fechaFin);
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return total;
    }
    
    
    public int obtenerTotalPaginas() {
    	fechaInicio = Date.valueOf(campoFechaDeInicio.getValue().format(formatoMySql));
    	fechaFin = Date.valueOf(campoFechaDeFin.getValue().format(formatoMySql));
    	int cantidadDePaginas = (int) Math.ceil((double) obtenerTotalRegistros(fechaInicio, fechaFin) / registrosPorPagina);
    	return cantidadDePaginas;
    }
    
    
    @FXML
    void accionBtnSiguiente(ActionEvent event) {
    	if (currentPageIndex < (total - 1) ) {
    		btnAnterior.setDisable(false);
    		currentPageIndex++;
    		System.out.println(currentPageIndex);
    		System.out.println(total);
    		offsets = currentPageIndex * registrosPorPagina;
        	completarTablaConPaginacion(offsets, registrosPorPagina, fechaInicio, fechaFin);
        	if (currentPageIndex == total - 1) {
        		btnSiguiente.setDisable(true);
        	}
    	}
    	
    }
    
    @FXML
    void accionBtnAnterior(ActionEvent event) {
    	
    	if (currentPageIndex > 0) {
    		btnSiguiente.setDisable(false);
    		currentPageIndex--;
    		offsets = currentPageIndex * registrosPorPagina;
    		completarTablaConPaginacion(offsets, registrosPorPagina, fechaInicio, fechaFin); 		
    		if (currentPageIndex == 0) {
    			btnAnterior.setDisable(true);
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
        	fechaInicio = Date.valueOf(campoFechaDeInicio.getValue());
        	fechaFin = Date.valueOf(campoFechaDeFin.getValue().format(formatoMySql));
        	
        	label20reg.setText("Mostrando resultados DESDE " + fechaInicio + " HASTA " + fechaFin);
        	
        	
        	
        	tablaVentas.getItems().clear();
        	completarTablaConPaginacion(0, 20, fechaInicio, fechaFin);
    	}
    }
    
    
    @FXML
    void abrirModalDetalle(MouseEvent event) throws IOException {
    	if (event.getClickCount() == 2) {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mspdevs/mspfxmaven/views/ModalDetalleVenta.fxml"));
            Parent root = loader.load();
            
            ModalDetalleVentasController dvc = new ModalDetalleVentasController();
            
            dvc = loader.getController();
            
            Venta ventaSeleccionada = tablaVentas.getSelectionModel().getSelectedItem();
            dvc.completarTabla(ventaSeleccionada.getId_factura_ventas());
            dvc.setLabelNroFactura(ventaSeleccionada.getNumeroFactura());
            
            
            Scene scene = new Scene(root);
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.initStyle(StageStyle.UNDECORATED);
            newStage.initOwner( ((Node)event.getSource()).getScene().getWindow() );
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.showAndWait();
    	}
    }
    

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		completarTablaConPaginacion(0, 20, fechaInicio, fechaFin);
		btnAnterior.setDisable(true);
	    btnSiguiente.setDisable(true);
		
	}
}
