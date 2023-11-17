package com.mspdevs.mspfxmaven;

import com.mspdevs.mspfxmaven.controllers.LoginMSPController;
import com.mspdevs.mspfxmaven.controllers.VentanaPrincipalController;
import com.mspdevs.mspfxmaven.utils.Alerta;
import com.mspdevs.mspfxmaven.utils.PantallaInicioUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class Main extends Application {
    Alerta msj = new Alerta();
    private static Scene scene;
    public static Stage primaryStage; // Agrega una referencia al Stage principal

    @Override
    public void start(Stage primarystage) throws IOException {
        Main.primaryStage = primarystage; // Guarda una referencia al Stage principal
        PantallaInicioUtil.mostrarPantallaInicio(primaryStage);
        // Agrega un manejador al evento de cierre de la ventana
        primarystage.setOnCloseRequest(e -> {
            e.consume();
            // Crea un cuadro de diálogo de confirmación
            boolean confirmado = msj.mostrarConfirmacion("Confirmación", "",
                    "¿Estás seguro de que deseas cerrar la aplicación?");
            if (confirmado) {
                // Si el usuario elige "Sí", cierra la ventana y, por lo tanto, la aplicación
                primarystage.close();
            }
        });
    }



    // ...

    public static void main(String[] args) {
        launch();
    }

    private LoginMSPController loginController; // Referencia a loginController

    // ...

}