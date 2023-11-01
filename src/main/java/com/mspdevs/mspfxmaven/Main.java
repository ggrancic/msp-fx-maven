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
        /*
        Parent root = FXMLLoader.load(getClass().getResource("/com/mspdevs/mspfxmaven/views/LoginMSP.fxml"));
        Scene scene = new Scene(root, 800, 600);
        primarystage.setScene(scene);
        primarystage.setTitle("Market Sales Pro - Version 1.1");
        primarystage.setResizable(false);
        // Agrega un manejador al evento de cierre de la ventana
        primarystage.setOnCloseRequest(e -> {
            e.consume(); // Evita que la ventana se cierre de inmediato
            // Crea un cuadro de diálogo de confirmación
            boolean confirmado = msj.mostrarConfirmacion("Confirmación", "",
                    "¿Estás seguro de que deseas cerrar la aplicación?");
            if (confirmado) {
                // Si el usuario elige "Sí", cierra la ventana y, por lo tanto, la aplicación
                primarystage.close();
            }*/

            /*
            // Crea un cuadro de diálogo de confirmación
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Cierre");
            alert.setHeaderText("¿Estás seguro de que deseas cerrar la aplicación?");
            //alert.setContentText("");

            // Configura los botones en el cuadro de diálogo
            ButtonType buttonTypeYes = new ButtonType("Sí");
            ButtonType buttonTypeNo = new ButtonType("No");
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

            // Muestra el cuadro de diálogo y espera a que el usuario seleccione una opción
            alert.showAndWait().ifPresent(response -> {
                if (response == buttonTypeYes) {
                    // Si el usuario elige "Sí", cierra la ventana y, por lo tanto, la aplicación
                    primarystage.close();
                }
            });*/
        /*
        });
        primarystage.show();

        // Cargar y establecer la imagen de icono
        Image icon = new Image(getClass().getResourceAsStream("/com/mspdevs/mspfxmaven/imgs/carrito-de-compras.png"));
        primarystage.getIcons().add(icon);

        // Establece la referencia al Stage principal
        primaryStage = primarystage;*/
    }



    // ...

    public static void main(String[] args) {
        launch();
    }

    private LoginMSPController loginController; // Referencia a loginController

    // ...

}