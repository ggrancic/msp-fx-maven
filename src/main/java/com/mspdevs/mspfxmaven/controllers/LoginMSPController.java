package com.mspdevs.mspfxmaven.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import com.mspdevs.mspfxmaven.model.ConexionMySQL;
import java.io.IOException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 */
public class LoginMSPController implements Initializable {
    
    // Acá van los atributos de la ventana (botones, campos, etc).
    // Esto lo genera SceneBuilder o bien se puede
    // generar programáticamente.

    @FXML
    private TextField campoUser;
    @FXML
    private PasswordField campoClave;
    @FXML
    private Button btnLogin;

// --------- METODOS ---------
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Acá se inicializa todo lo referido a los elementos del fxml.
        
    }
       
    // Acá voy a poner todo lo referido a las consultas sql.
    
    public boolean buscarEmpleado(String user, String pass) {
        boolean existe = false;
        try {
            ConexionMySQL c = new ConexionMySQL();
            c.conectar();
            PreparedStatement st = (PreparedStatement) c.getCon().prepareStatement("SELECT nombre_usuario, clave FROM mercadito.empleados WHERE nombre_usuario=? AND clave=?");
            st.setString(1, user);
            st.setString(2, pass);
            ResultSet rs = st.executeQuery();
            
            if(rs.next()) {
                existe = true;
                st.close();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        return existe;
        
    }
    
    @FXML
    void verificarLogin(MouseEvent event) {
        
        // Acá manejo si lo ingresado existe en la db o no.
        
        String usuarioIngresado = campoUser.getText();
        String claveIngresada = campoClave.getText();
        
        if (!(buscarEmpleado(usuarioIngresado, claveIngresada))) {
            Alert alertaDatosErroneos = new Alert(AlertType.ERROR);
            alertaDatosErroneos.setTitle("Error");
            alertaDatosErroneos.setHeaderText(null);
            alertaDatosErroneos.setContentText("Ingrese las credenciales correctas");
            alertaDatosErroneos.showAndWait();
        } else {
            try {
                irAPantallaPcpal("/com/mspdevs/mspfxmaven/views/VentanaPrincipal.fxml", event);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
    public void irAPantallaPcpal(String url, Event evt) throws Exception {
        ((Node)(evt.getSource())).getScene().getWindow().hide();
        
        Parent root = FXMLLoader.load(getClass().getResource(url));
        Scene scene = new Scene(root);
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.setMaximized(true);
        newStage.show();
    }
    
    
    
}
