package com.mspdevs.mspfxmaven.controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.mspdevs.mspfxmaven.model.DetalleVenta;
import com.mspdevs.mspfxmaven.model.Producto;
import com.mspdevs.mspfxmaven.model.DAO.DetalleVentaDAOImpl;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class ModalDetalleVentasController {
    @FXML
    private TableColumn<DetalleVenta, Integer> colCantidad;

    @FXML
    private TableColumn<DetalleVenta, Producto> colCodBarra;

    @FXML
    private TableColumn<DetalleVenta, Producto> colNom;

    @FXML
    private TableColumn<DetalleVenta, Producto> colPU;

    @FXML
    private TableColumn<DetalleVenta, Double> colTotal;

    @FXML
    private TableView<DetalleVenta> tblDetalle;
    
    @FXML
    private Button btnAtras;
    
    @FXML
    private Label labelDetalle;
    
    
    // ----- METODOS -----
    
    void completarTabla(int idFactura) {
    	DetalleVentaDAOImpl detalle = new DetalleVentaDAOImpl();
		ObservableList<DetalleVenta> listaDetalles = null;
		
		try {
			listaDetalles= detalle.buscarPorId(idFactura);
			System.out.println(idFactura);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (listaDetalles != null) {
			
			this.colCodBarra.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getProducto().getCodigoBarra()));
	        this.colNom.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getProducto().getNombre()));
	        this.colPU.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getProducto().getPrecioVenta()));
	        this.colCantidad.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getCantidad()));
	        this.colTotal.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getMonto()));
			
			tblDetalle.setItems(listaDetalles);
		} else {
			System.out.println("No se cargaron datos");
		}
    }
    
    void setLabelNroFactura(String nroFactura) {
    	labelDetalle.setText("DETALLE DE VENTA NUMERO " + nroFactura);
    }
    
	
	@FXML
    void accionBtnAtras(ActionEvent event) {
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
