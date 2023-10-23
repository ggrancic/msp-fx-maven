package com.mspdevs.mspfxmaven.controllers;

import com.mspdevs.mspfxmaven.utils.Alerta;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class ModalFinalizarVentaController implements Initializable {
    Alerta msj = new Alerta();
    @FXML
    private Button btnImprimirComprobante;

    @FXML
    private Button btnSalir;

    @FXML
    void accionImprimirComprobante(ActionEvent event) {

    }

    @FXML
    void accionSalir(ActionEvent event) {
        // Crea un cuadro de diálogo de confirmación
        boolean confirmado = msj.mostrarConfirmacion("Confirmación", "",
                "¿Está seguro de que no desea imprimir el comprobante?");
        if (confirmado) {
            cerrarVentanaModal(event);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private void cerrarVentanaModal(ActionEvent event) {
        // Obtén la referencia a la ventana actual
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        // Cierra la ventana
        stage.close();
    }
}
