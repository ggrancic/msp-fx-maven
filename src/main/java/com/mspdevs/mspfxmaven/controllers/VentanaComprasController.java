package com.mspdevs.mspfxmaven.controllers;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class VentanaComprasController implements Initializable {

    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnModificar;

    @FXML
    private TableColumn<?, ?> colCod;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colNom;

    @FXML
    private TableColumn<?, ?> colPL;

    @FXML
    private TableColumn<?, ?> colPV;

    @FXML
    private TableView<?> tblDetalle;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

}
