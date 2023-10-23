package com.mspdevs.mspfxmaven.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.mspdevs.mspfxmaven.model.Empleado;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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

    @FXML
    private TextField campoUser;
    @FXML
    private PasswordField campoClave;
    @FXML
    private Button btnLogin;

    private Parent root;

// --------- METODOS ---------

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Acá se inicializa todo lo referido a los elementos del fxml.

    }

    // Acá voy a poner todo lo referido a las consultas sql.

    public Empleado buscarEmpleado(String user, String pass) {
        Empleado empleado = null;
        try {
            ConexionMySQL c = new ConexionMySQL();
            c.conectar();
            PreparedStatement st = (PreparedStatement) c.getCon().prepareStatement("SELECT id_empleado, nombre_usuario, clave, nombre, apellido " +
                    "FROM mercadito.empleados em JOIN mercadito.personas per ON em.idpersona = per.id_persona WHERE nombre_usuario=? AND clave=?");
            st.setString(1, user);
            st.setString(2, pass);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                empleado = new Empleado();
                empleado.setId_empleado(rs.getInt("id_empleado"));
                empleado.setNombre_usuario(rs.getString("nombre_usuario"));
                empleado.setClave(rs.getString("clave"));
                empleado.setNombre(rs.getString("nombre"));
                empleado.setApellido(rs.getString("apellido"));
            }
            st.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return empleado;
    }

    /*
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

    }*/

    @FXML
    void verificarLogin(MouseEvent event) {
        String usuarioIngresado = campoUser.getText();
        String claveIngresada = campoClave.getText();

        Empleado empleado = buscarEmpleado(usuarioIngresado, claveIngresada);

        if (empleado != null) {
            // Establece el nombre de usuario logueado
            setNombreUsuarioLogueado(usuarioIngresado);
            // Pasa el objeto Empleado a la pantalla principal
            try {
                irAPantallaPcpal("/com/mspdevs/mspfxmaven/views/VentanaPrincipal.fxml", event, empleado);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            Alert alertaDatosErroneos = new Alert(AlertType.ERROR);
            alertaDatosErroneos.setTitle("Error");
            alertaDatosErroneos.setHeaderText(null);
            alertaDatosErroneos.setContentText("Ingrese las credenciales correctas");
            alertaDatosErroneos.showAndWait();
        }

        /*
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
            // Establece el nombre de usuario logueado
            setNombreUsuarioLogueado(usuarioIngresado);
            try {
                irAPantallaPcpal("/com/mspdevs/mspfxmaven/views/VentanaPrincipal.fxml", event, usuarioIngresado);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }*/
    }

    public void irAPantallaPcpal(String url, Event evt, Empleado empleado) throws Exception {
        ((Node)(evt.getSource())).getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
        Parent root = loader.load();

        VentanaPrincipalController ventanaPrincipalController1 = loader.getController();
        ventanaPrincipalController1.mostrarUsuario(empleado.getNombre() + " " + empleado.getApellido());

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


    /*
    public void irAPantallaPcpal(String url, Event evt) throws Exception {
        ((Node)(evt.getSource())).getScene().getWindow().hide();

        Parent root = FXMLLoader.load(getClass().getResource(url));
        Scene scene = new Scene(root);
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.setMaximized(true);
        newStage.show();
    }
     */





    // Variable para almacenar el nombre de usuario
    private String nombreUsuarioLogueado;

    // ...

    // Método para obtener el nombre de usuario
    public String getNombreUsuarioLogueado() {
        return nombreUsuarioLogueado;
    }

    // Método para establecer el nombre de usuario
    public void setNombreUsuarioLogueado(String nombreUsuario) {
        this.nombreUsuarioLogueado = nombreUsuario;
    }

}
