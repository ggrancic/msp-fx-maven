package com.mspdevs.mspfxmaven.controllers;

import com.mspdevs.mspfxmaven.model.ConexionMySQL;
import com.mspdevs.mspfxmaven.model.Producto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class VentanaProductosController {
    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnLimpiar;

    @FXML
    private Button btnModificar;

    @FXML
    private TextField campoBuscar;

    @FXML
    private TextField campoCantDisp;

    @FXML
    private TextField campoCantMin;

    @FXML
    private TextField campoLista;

    @FXML
    private TextField campoNombre;

    @FXML
    private TextField campoVenta;

    @FXML
    private TableColumn<?, ?> colCantDisponible;

    @FXML
    private TableColumn<?, ?> colCantMinima;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colNom;

    @FXML
    private TableColumn<?, ?> colPrecioLista;

    @FXML
    private TableColumn<?, ?> colPrecioVenta;

    @FXML
    private TableColumn<?, ?> colProveedor;

    @FXML
    private TableColumn<?, ?> colRubro;

    @FXML
    private ComboBox<?> proveedorBox;

    @FXML
    private ComboBox<?> rubroBox;

    @FXML
    private TableView<Producto> tablaProducto;

    @FXML
    void accionBotonAgregar(ActionEvent event) {

    }

    @FXML
    void accionBotonEliminar(ActionEvent event) {

    }

    @FXML
    void accionBotonLimpiar(ActionEvent event) {

    }

    @FXML
    void accionBotonModificar(ActionEvent event) {

    }

    @FXML
    void buscarProductoFiltro(KeyEvent event) {

    }


    @FXML
    void initialize() {

    }

    /*
    // Método para cargar los rubros desde la base de datos al ComboBox
    private void cargarRubrosDesdeBD() {
        // Crear una lista observable para almacenar los nombres de los rubros
        ObservableList<String> rubros = FXCollections.observableArrayList();

        try {
            // Establecer la conexión con la base de datos
            ConexionMySQL conexionMySQL = new ConexionMySQL();
            //conexionMySQL.conectar();
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
    }*/
}
