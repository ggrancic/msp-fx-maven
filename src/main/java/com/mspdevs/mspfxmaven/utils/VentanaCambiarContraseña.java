package com.mspdevs.mspfxmaven.utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class VentanaCambiarContraseña extends Stage {
    
    private String nuevaPw;
    

    public VentanaCambiarContraseña() {
        Alerta msj = new Alerta();
        // Configura la ventana flotante
        initStyle(StageStyle.UTILITY);
        initModality(Modality.APPLICATION_MODAL);

        // Crea el contenido de la ventana flotante
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        
        // Agrega aquí los componentes necesarios para cambiar la contraseña, como TextFields, Labels, Buttons, etc.
        
        // Etiqueta para instrucciones
        Label instruccionesLabel = new Label("Ingrese la nueva contraseña:");
        
        // Campo de contraseña
        PasswordField nuevaContraseñaField = new PasswordField();
        nuevaContraseñaField.setPromptText("Nueva Contraseña");
        
        // Botón para confirmar el cambio de contraseña
        Button confirmarButton = new Button("Confirmar");
        confirmarButton.setOnAction(e -> {
            // Aquí puedes agregar la lógica para cambiar la contraseña,
            // por ejemplo, validar la nueva contraseña y guardarla en algún lugar.
            // Puedes mostrar un mensaje de éxito o error según el resultado.
            // Luego, cierra la ventana flotante.

            
            String pwIngresada = nuevaContraseñaField.getText();
            
            if (pwIngresada.isEmpty()) {
                msj.mostrarError("Error", "", "Debe ingresar una nueva contraseña");
            } else {
                setNuevaPw(pwIngresada);
                close();
            }
            
           
        });

        root.getChildren().addAll(instruccionesLabel, nuevaContraseñaField, confirmarButton);
        
        // Crea una escena y establece el contenido
        Scene scene = new Scene(root, 300, 200);
        setScene(scene);
        setTitle("Cambiar Contraseña");
    }

    public String getNuevaPw() {
        return nuevaPw;
    }

    public void setNuevaPw(String nuevaPw) {
        this.nuevaPw = nuevaPw;
    }
}
