package com.mspdevs.mspfxmaven.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ReportesComprasController {
	
	@FXML
    private Button btnVolver;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colNom;

    @FXML
    private TableColumn<?, ?> colNom1;

    @FXML
    private TableColumn<?, ?> colNom11;

    @FXML
    private TableColumn<?, ?> colNom111;

    @FXML
    private TableView<?> tablaRubros;
    
    
//    @FXML
//    void accionBtnVolver(ActionEvent event) {
//    	try {
//    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mspdevs/mspfxmaven/views/VentanaReportesBotones.fxml"));
//            Parent ventanaReportes = loader.load();
//            Scene scene = btnVolver.getScene();
//            scene.setRoot(ventanaReportes);
//    	} catch (Exception e) {
//			e.printStackTrace();
//		}
//    	
//    }
    
}
