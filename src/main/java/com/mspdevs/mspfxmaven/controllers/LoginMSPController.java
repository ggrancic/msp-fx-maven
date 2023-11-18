package com.mspdevs.mspfxmaven.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.mspdevs.mspfxmaven.model.DAO.EmpleadoDAOImpl;
import com.mspdevs.mspfxmaven.model.Empleado;
import com.mspdevs.mspfxmaven.model.DAO.EmpleadoDAOImpl;
import com.mspdevs.mspfxmaven.utils.Alerta;
import com.mspdevs.mspfxmaven.utils.LoginChecker;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class LoginMSPController implements Initializable {

    // Acá van los atributos de la ventana (botones, campos, etc).
    // Esto lo genera SceneBuilder o bien se puede
    // generar programáticamente.
	
	Alerta msj = new Alerta();

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
       

    @FXML
    void accionBtnLogin(ActionEvent event) {
    	String usuarioIngresado = campoUser.getText();
        String claveIngresada = campoClave.getText();
    	
    	LoginChecker loginCheck = new LoginChecker(usuarioIngresado, claveIngresada);
    	loginCheck.autenticarEmpleado();
        
        if (loginCheck.getEstado()) {
        	try {
				irAPantallaPcpal("/com/mspdevs/mspfxmaven/views/VentanaPrincipal.fxml", event, loginCheck.getEmpleado());
			} catch (Exception e) {
				e.printStackTrace();
			}
        } else {
        	msj.mostrarError("ERROR", "", "Usuario y/o contraseña incorrectos");
        }
    }

    public void irAPantallaPcpal(String url, Event evt, Empleado empleado) throws Exception {
        ((Node)(evt.getSource())).getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
        Parent root = loader.load();


        VentanaPrincipalController ventanaPrincipalController = loader.getController();
        ventanaPrincipalController.obtenerEmpleado(empleado);
        
        if (empleado.getEsAdmin().equals("N")) {
        	ventanaPrincipalController.habilitarSoloVentas();
        }

        Scene scene = new Scene(root);
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.setMaximized(true);
        newStage.setTitle("Market Sales Pro - Version 1.1");
        Image icon = new Image(getClass().getResourceAsStream("/com/mspdevs/mspfxmaven/imgs/carrito-de-compras.png"));
        newStage.getIcons().add(icon);
        newStage.show();

        VentanaPrincipalController principalController = loader.getController();
        principalController.setCerrarEvento(newStage); // Configura el evento de cierre
    }
    
    
    public void disparar() {
    	btnLogin.fire();
    }
}
