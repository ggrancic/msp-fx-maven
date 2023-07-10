package com.mspdevs.mspfxmaven;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import org.kordamp.bootstrapfx.BootstrapFX;

/**
 * JavaFX App
 */
public class Main extends Application {

    private static Scene scene;

    @Override
    public void start(Stage primarystage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/mspdevs/mspfxmaven/views/LoginMSP.fxml"));
        Scene scene = new Scene(root, 800, 600);
        primarystage.setScene(scene);
        primarystage.setTitle("Market Sales Pro - Version 1.0");
        primarystage.setResizable(false);
        primarystage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}