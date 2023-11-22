package com.mspdevs.mspfxmaven.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.mspdevs.mspfxmaven.model.Caja;
import com.mspdevs.mspfxmaven.utils.Alerta;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModalRetiroController implements Initializable {
	
	Alerta msj = new Alerta();
	
	private Caja currentCaja;
	
	private double totalEnCaja;
	
	private double dineroRetirado;

	@FXML
    private Button btnAceptar;

    @FXML
    private Button btnCancelar;

    @FXML
    private TextField campoMontoRet;
    
    
    public void setTotalCaja(Caja caja) {
		this.currentCaja = caja;
	}
    
    @FXML
    void accionBtnAceptar(ActionEvent event) throws IOException {
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mspdevs/mspfxmaven/views/ModalCaja.fxml"));
        Parent root = loader.load();
        ModalCajaController mcc = loader.getController();
    	
    	if (Double.parseDouble(campoMontoRet.getText()) > currentCaja.getMontoFinal()) {
    		msj.mostrarError("ERROR", null, "Cantidad ingresada mayor a la que hay en caja.");
    	} else {
    		currentCaja.setEgresos(Double.parseDouble(campoMontoRet.getText()));
    		Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
    	}
    }
    
    @FXML
    void accionBtnCancelar(ActionEvent event) {
    	Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
    
    
    
	
}
