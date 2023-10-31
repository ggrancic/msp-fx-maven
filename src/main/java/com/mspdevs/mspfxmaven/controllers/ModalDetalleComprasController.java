package com.mspdevs.mspfxmaven.controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.mspdevs.mspfxmaven.model.DetalleCompra;
import com.mspdevs.mspfxmaven.model.Producto;
import com.mspdevs.mspfxmaven.model.DAO.DetalleCompraDAOImpl;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;


import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


public class ModalDetalleComprasController implements Initializable {
	
	
	@FXML
    private TableColumn<?, ?> colCantidad;

    @FXML
    private TableColumn<DetalleCompra, Integer> colId;

    @FXML
    private TableColumn<DetalleCompra, String> colNom;

    @FXML
    private TableColumn<?, ?> colPL;

    @FXML
    private TableColumn<?, ?> colPV;

    @FXML
    private TableColumn<DetalleCompra, Double> colTotal;

    @FXML
    private TableView<DetalleCompra> tblDetalle;
    
    @FXML
    private Button btnAtras;
    
    @FXML
    private Label labelDetalle;
    
    
    // ----- METODOS -----
    
    void completarTabla(int idFactura) {
    	DetalleCompraDAOImpl detalle = new DetalleCompraDAOImpl();
		ObservableList<DetalleCompra> listaDetalles = null;
		
		try {
			listaDetalles= detalle.buscarPorId(idFactura);
			System.out.println(idFactura);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (listaDetalles != null) {
			colId.setCellValueFactory(data-> new SimpleObjectProperty(data.getValue().getId()));
			colNom.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getProducto().getNombre()));
			colTotal.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getPrecio()));
			
			tblDetalle.setItems(listaDetalles);
		} else {
			System.out.println("No se cargaron datos");
		}
    }
    
    void setLabelNroFactura(String nroFactura) {
    	labelDetalle.setText("DETALLE DE COMPRA NUMERO " + nroFactura);
    }
    
	
	@FXML
    void accionBtnAtras(ActionEvent event) {
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
	
}
