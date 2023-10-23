package com.mspdevs.mspfxmaven.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class PantallaInicioUtil {
    public static void mostrarPantallaInicio(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(PantallaInicioUtil.class.getResource("/com/mspdevs/mspfxmaven/views/LoginMSP.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.setTitle("Market Sales Pro - Version 1.2");
            stage.setResizable(false);
            Image icon = new Image(PantallaInicioUtil.class.getResourceAsStream("/com/mspdevs/mspfxmaven/imgs/carrito-de-compras.png"));
            stage.getIcons().add(icon);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
