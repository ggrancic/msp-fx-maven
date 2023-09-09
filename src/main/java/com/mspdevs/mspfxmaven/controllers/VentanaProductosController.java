package com.mspdevs.mspfxmaven.controllers;

import com.mspdevs.mspfxmaven.model.ConexionMySQL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class VentanaProductosController {
    @FXML
    private TableColumn<?, ?> CantDisponibleColumna;

    @FXML
    private TableColumn<?, ?> CantMinimaColumna;

    @FXML
    private TableColumn<?, ?> IDColumna;

    @FXML
    private TableColumn<?, ?> NombreColumna;

    @FXML
    private TableColumn<?, ?> PrecioListaColumna;

    @FXML
    private TableColumn<?, ?> PrecioVentaColumna;

    @FXML
    private TableColumn<?, ?> ProveedorColumna;

    @FXML
    private TableColumn<?, ?> RubroColumna;

    @FXML
    private TableView<?> Tabla;

    @FXML
    private Button agregarBtn;

    @FXML
    private TextField cantDisponibleTxt;

    @FXML
    private TextField cantMinimaTxt;

    @FXML
    private Button eliminarBtn;

    @FXML
    private TextField filtrarTxt;

    @FXML
    private Button limpiarBtn;

    @FXML
    private Button modificarBtn;

    @FXML
    private TextField nombreTxt;

    @FXML
    private TextField precioListaTxt;

    @FXML
    private TextField precioVentaTxt;

    @FXML
    private ComboBox<?> proveedorBox;

    @FXML
    private ComboBox<String> rubroBox;

    @FXML
    void agregarProducto(ActionEvent event) {
        // Crea una instancia de la clase ConexionMySQL para gestionar la conexión a la base de datos
        ConexionMySQL conexionMySQL = new ConexionMySQL();

        // Establece la conexión a la base de datos
        conexionMySQL.conectar();
        Connection conexion = conexionMySQL.getCon();

        String nombreIngresado = nombreTxt.getText();
        String precioVentaIngresado = precioVentaTxt.getText();
        String precioListaIngresado = precioListaTxt.getText();
        String cantidadDisponibleIngresada = cantDisponibleTxt.getText();
        String cantidadMinimaIngresada = cantMinimaTxt.getText();


        /*if (!esNombreValido(nombreIngresado)) {
            mostrarAlerta("Advertencia", "Campo Vacío", "El campo de nombre no puede estar vacío.");
            Tabla.getSelectionModel().clearSelection();
            return; // Sale del método si el campo está vacío
        }

        // Toma el valor ingresado en "nombreIngresado", convierte la primera letra en mayúscula
        // y el resto en minúscula para formatear el nombre de manera consistente
        String nombre = nombreIngresado.substring(0, 1).toUpperCase() + nombreIngresado.substring(1).toLowerCase();

        try {
            // Consulta SQL para verificar si el nombre ya existe
            String consultaExistencia = "SELECT COUNT(*) FROM rubros WHERE nombre = ?";
            PreparedStatement statementExistencia = conexion.prepareStatement(consultaExistencia);
            statementExistencia.setString(1, nombre);

            ResultSet rsExistencia = statementExistencia.executeQuery();
            rsExistencia.next();
            int cantidadRegistros = rsExistencia.getInt(1);

            if (cantidadRegistros > 0) {
                // Si ya existe un registro con el mismo nombre, muestra una alerta de advertencia
                mostrarAlerta("Advertencia", "Nombre duplicado", "El nombre ya existe en la base de datos.");
                return;
            }
            // Prepara la consulta
            String consulta = "insert into rubros(nombre)values(?)";
            PreparedStatement statement = conexion.prepareStatement(consulta);
            statement.setString(1, nombre); // Ejemplo de parámetro

            // Ejecuta la consulta
            statement.executeUpdate();

            // Cierra los recursos
            statement.close();
            conexion.close();

            /*pst = con.prepareStatement("insert into rubros(nombre)values(?)");
            pst.setString(1, nombre);
            pst.executeUpdate();*/


        /*
            // Muestra una alerta después de agregar el registro
            mostrarAlerta("Advertencia", "Rubro agregado", "El rubro se añadio con exito.");

            tabla();

            nombreTxt.setText("");
            nombreTxt.requestFocus();
        } catch (SQLException ex) {
            Logger.getLogger(VentanaRubrosController.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

    @FXML
    void eliminarProducto(ActionEvent event) {

    }

    @FXML
    void limpiarProducto(ActionEvent event) {

    }

    @FXML
    void modificarProducto(ActionEvent event) {

    }

    // Este método se ejecutará cuando se inicialice la ventana de "Productos"
    @FXML
    void initialize() {
        // Llama al método para cargar los rubros en el ComboBox
        cargarRubrosDesdeBD();
    }

    // Método para cargar los rubros desde la base de datos al ComboBox
    private void cargarRubrosDesdeBD() {
        // Crear una lista observable para almacenar los nombres de los rubros
        ObservableList<String> rubros = FXCollections.observableArrayList();

        try {
            // Establecer la conexión con la base de datos
            ConexionMySQL conexionMySQL = new ConexionMySQL();
            conexionMySQL.conectar();
            Connection conexion = conexionMySQL.getCon();

            // Preparar una consulta SQL para obtener los nombres de los rubros
            String consulta = "SELECT nombre FROM rubros";
            PreparedStatement statement = conexion.prepareStatement(consulta);

            // Ejecutar la consulta y recoger los resultados
            ResultSet resultSet = statement.executeQuery();

            // Agregar los nombres de los rubros a la lista observable
            while (resultSet.next()) {
                rubros.add(resultSet.getString("nombre"));
            }

            // Cerrar los recursos
            resultSet.close();
            statement.close();
            conexion.close();
        } catch (SQLException ex) {
            // Manejar excepciones
            ex.printStackTrace();
        }

        // Llenar el ComboBox rubroBox con los nombres de los rubros
        rubroBox.setItems(rubros);
    }
}
