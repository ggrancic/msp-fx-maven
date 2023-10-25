package com.mspdevs.mspfxmaven.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class VentanaReportesBotonesController implements Initializable {

    @FXML
    private BorderPane bpaneReportes;


    @FXML
    private HBox contenedorRC;



    @FXML
    void accionRC(MouseEvent event) throws IOException {
        GridPane centro = FXMLLoader.load(getClass().getResource("/com/mspdevs/mspfxmaven/views/VentanaReporteCompra.fxml"));
        bpaneReportes.setCenter(centro);
    }

    @FXML
    void accionRV(MouseEvent event) throws IOException {
        GridPane centro = FXMLLoader.load(getClass().getResource("/com/mspdevs/mspfxmaven/views/VentanaReporteVentas.fxml"));
        bpaneReportes.setCenter(centro);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }

}