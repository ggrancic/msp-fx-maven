package com.mspdevs.mspfxmaven.controllers;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class VentanaVentasController implements Initializable{

    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnAgregar1;

    @FXML
    private Button btnModificar;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colNom;

    @FXML
    private TableColumn<?, ?> colPL;

    @FXML
    private TableColumn<?, ?> colPV;

    @FXML
    private TableColumn<?, ?> colPV1;

    @FXML
    private TableView<?> tblDetalle;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
	}

}
