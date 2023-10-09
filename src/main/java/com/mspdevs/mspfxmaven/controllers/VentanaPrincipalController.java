package com.mspdevs.mspfxmaven.controllers;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;


public class VentanaPrincipalController implements Initializable {

    private LoginMSPController loginController;
    
    @FXML
    private HBox hbox ;
    
    @FXML
    private BorderPane bpane;

    @FXML
    private Label usuarioLogueado;

    @FXML
    private Button btnCerrar;
    
    @FXML
    private Button btnClientes;
    
    @FXML
    private Button btnInventario;
    
    @FXML
    private Button btnProveedores;

    @FXML
    private Button btnRubros;

    @FXML
    private Label time;

    @FXML
    private Label fecha;
    
    @FXML
    private Button btnCompra;

    private volatile boolean stop = false;

    @FXML
    void abrirVentanaCliente(MouseEvent event) throws IOException {
        GridPane centro = FXMLLoader.load(getClass().getResource("/com/mspdevs/mspfxmaven/views/VentanaCliente.fxml"));    
        bpane.setCenter(centro);
    }
    
    @FXML
    void abrirVentanaInventario(MouseEvent event) throws IOException {
        GridPane centro = FXMLLoader.load(getClass().getResource("/com/mspdevs/mspfxmaven/views/VentanaProductos.fxml"));
        bpane.setCenter(centro);
    }
    
    @FXML
    void abrirVentanaProveedores(MouseEvent event) throws IOException {
        GridPane centro = FXMLLoader.load(getClass().getResource("/com/mspdevs/mspfxmaven/views/VentanaProveedores.fxml"));
        bpane.setCenter(centro);
    }
    
    @FXML
    void abrirVentanaUsuarios(MouseEvent event) throws IOException {
        GridPane centro = FXMLLoader.load(getClass().getResource("/com/mspdevs/mspfxmaven/views/VentanaEmpleados.fxml"));
        bpane.setCenter(centro);
    }

    @FXML
    void abrirVentanaRubros(MouseEvent event) throws IOException {
        GridPane centro = FXMLLoader.load(getClass().getResource("/com/mspdevs/mspfxmaven/views/VentanaRubros.fxml"));
        bpane.setCenter(centro);
    }
    
    @FXML
    void abrirVentanaCompras(ActionEvent event) throws IOException {
    	BorderPane centro = FXMLLoader.load(getClass().getResource("/com/mspdevs/mspfxmaven/views/VentanaCompras.fxml"));
    	bpane.setCenter(centro);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Acá se inicializa todo lo referido a los elementos del fxml.
        Timenow();

        //usuarioLogueado.setText(loginController.getNombreUsuarioLogueado());
    }

    private void Timenow(){
        Thread thread = new Thread(() -> {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            while(!stop){
                try{
                    Thread.sleep(1000);
                }catch(Exception e){
                    System.out.println(e);
                }
                final String timenow = sdf.format(new Date());
                Platform.runLater(() -> {
                    time.setText(timenow); // This is the label
                });
            }
        });
        thread.start();

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e ->
                fecha.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        ),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    public void irAPantallaLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mspdevs/mspfxmaven/views/LoginMSP.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();

            // Obtener el Stage actual y cerrarlo
            Stage currentStage = (Stage) btnCerrar.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void cerrarAplicacion(ActionEvent event) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar");
        confirmacion.setHeaderText("¿Desea salir o cerrar sesión?");
        ButtonType salirButton = new ButtonType("Salir");
        ButtonType noButton = new ButtonType("No salir");
        ButtonType cerrarSesionButton = new ButtonType("Cerrar sesión");

        confirmacion.getButtonTypes().setAll(salirButton, noButton, cerrarSesionButton);

        Stage stage = (Stage) confirmacion.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == salirButton) {
                // Cerrar la aplicación
                stop = true; // Detener el hilo de actualización del tiempo
                Stage currentStage = (Stage) btnCerrar.getScene().getWindow();
                currentStage.close();
            } else if (response == noButton) {
                // No hacer nada, simplemente cerrar la ventana de confirmación
            } else if (response == cerrarSesionButton) {
                // Cerrar sesión y volver a la ventana de inicio de sesión
                stop = true; // Detener el hilo de actualización del tiempo
                irAPantallaLogin(event);
            }
        });
    }


    // ... otros métodos ...

    // Método para obtener una referencia al controlador de inicio de sesión
    private LoginMSPController obtenerControladorLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mspdevs/mspfxmaven/views/LoginMSP.fxml"));
            Parent root = loader.load();
            LoginMSPController loginController = loader.getController();
            return loginController;
        } catch (IOException e) {
            System.out.println("Error al obtener el controlador de inicio de sesión: " + e.getMessage());
            return null;
        }
    }
}
