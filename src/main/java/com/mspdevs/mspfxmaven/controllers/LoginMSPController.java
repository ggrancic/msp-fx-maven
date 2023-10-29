package com.mspdevs.mspfxmaven.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.mspdevs.mspfxmaven.model.DAO.EmpleadoDAOImpl;
import com.mspdevs.mspfxmaven.model.Empleado;
import com.mspdevs.mspfxmaven.utils.Alerta;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.mspdevs.mspfxmaven.model.ConexionMySQL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 */
public class LoginMSPController implements Initializable {
    private VentanaPrincipalController ventanaPrincipalController;

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
    void verificarLogin(MouseEvent event) {
        String usuarioIngresado = campoUser.getText();
        String claveIngresada = campoClave.getText();
        String nombreObtenido;
        String rol;
        Empleado empleadoAutenticado = null;
        boolean ok = false;

        EmpleadoDAOImpl empleados = new EmpleadoDAOImpl();
        ObservableList<Empleado> listaEmpleados = null;
        try {
            listaEmpleados = empleados.listarTodos();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Empleado empleado : listaEmpleados) {
            if (usuarioIngresado.equals(empleado.getNombre_usuario()) && claveIngresada.equals(empleado.getClave())) {
                try {
                    empleadoAutenticado = empleado;
                    ok = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (ok) {
            try {
                irAPantallaPcpal("/com/mspdevs/mspfxmaven/views/VentanaPrincipal.fxml", event, empleadoAutenticado);
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

        VentanaPrincipalController ventanaPrincipalController = loader.getController(); // Usa la misma instancia
        ventanaPrincipalController.mostrarUsuario(empleado.getNombre() + " " + empleado.getApellido(), empleado.getId_empleado());

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
}
