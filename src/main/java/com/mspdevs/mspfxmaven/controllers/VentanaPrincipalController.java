package com.mspdevs.mspfxmaven.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


public class VentanaPrincipalController implements Initializable {
    
    @FXML
    private BorderPane bpane;
    
    @FXML
    private Button btnClientes;
    
    @FXML
    private Button btnInventario;
    
    @FXML
    private Button btnProveedores;

    @FXML
    void abrirVentanaCliente(MouseEvent event) throws IOException {
        BorderPane centro = FXMLLoader.load(getClass().getResource("/com/mspdevs/mspfxmaven/views/VentanaCliente.fxml"));    
        bpane.setCenter(centro);
    }
    
    @FXML
    void abrirVentanaInventario(MouseEvent event) throws IOException {
        BorderPane centro = FXMLLoader.load(getClass().getResource("/com/mspdevs/mspfxmaven/views/VentanaProductos.fxml"));
        bpane.setCenter(centro);
    }
    
    @FXML
    void abrirVentanaProveedores(MouseEvent event) throws IOException {
        BorderPane centro = FXMLLoader.load(getClass().getResource("/com/mspdevs/mspfxmaven/views/VentanaProveedores.fxml"));
        bpane.setCenter(centro);
    }
    
    @FXML
    void abrirVentanaUsuarios(MouseEvent event) throws IOException {
        BorderPane centro = FXMLLoader.load(getClass().getResource("/com/mspdevs/mspfxmaven/views/VentanaEmpleados.fxml"));
        bpane.setCenter(centro);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Acá se inicializa todo lo referido a los elementos del fxml.
        
        
        
    }
    
}
