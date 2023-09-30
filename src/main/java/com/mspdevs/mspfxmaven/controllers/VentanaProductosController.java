package com.mspdevs.mspfxmaven.controllers;

import com.mspdevs.mspfxmaven.model.DAO.ProductoDAOImpl;
import com.mspdevs.mspfxmaven.model.DAO.ProveedorDAOImpl;
import com.mspdevs.mspfxmaven.model.DAO.RubroDAOImpl;
import com.mspdevs.mspfxmaven.model.Producto;
import com.mspdevs.mspfxmaven.model.Proveedor;
import com.mspdevs.mspfxmaven.model.Rubro;
import com.mspdevs.mspfxmaven.utils.Alerta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;


public class VentanaProductosController {

    Alerta msj = new Alerta();
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
    private TableColumn<Producto, Integer> colCantDisponible;

    @FXML
    private TableColumn<Producto, Integer> colCantMinima;

    @FXML
    private TableColumn<Producto, Integer> colId;

    @FXML
    private TableColumn<Producto, String> colNom;

    @FXML
    private TableColumn<Producto, Double> colPrecioLista;

    @FXML
    private TableColumn<Producto, Double> colPrecioVenta;

    @FXML
    private TableColumn<Proveedor, String> colProveedor;

    @FXML
    private TableColumn<Rubro, String> colRubro;

    @FXML
    private ComboBox<String> proveedorBox;

    @FXML
    private ComboBox<String> rubroBox;

    @FXML
    private TableView<Producto> tablaProducto;

    RubroDAOImpl rubroDAO = new RubroDAOImpl();

    @FXML
    void accionBotonAgregar(ActionEvent event) {
        // Obtiene el nombre seleccionado del ComboBox de rubros
        String rubroSeleccionado = this.rubroBox.getSelectionModel().getSelectedItem();

        // Obtiene el nombre seleccionado del ComboBox de proveedores
        String proveedorSeleccionado = this.proveedorBox.getSelectionModel().getSelectedItem();

        //int idRubro = rubroDAO.listarTodos(rubroSeleccionado);
        //int idProveedor = proveedorDAO.obtenerIdPorNombre(proveedorSeleccionado);

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
        completarTablaProductos();

        // Llama al método para obtener los nombres de proveedores desde ProveedorDAOImpl
        ObservableList<String> nombresProveedores = obtenerNombresProveedoresDesdeDAO();

        // Configura el ComboBox con los nombres de proveedores
        proveedorBox.setItems(nombresProveedores);

        // Llama al método para obtener los nombres de rubros desde RubroDAOImpl
        ObservableList<String> nombresRubros = obtenerNombresRubrosDesdeDAO();

        // Configura el ComboBox con los nombres de rubros
        rubroBox.setItems(nombresRubros);

    }




    public void completarTablaProductos() {
        ProductoDAOImpl productoDAO = new ProductoDAOImpl();
        ObservableList<Producto> productos = null;

        try {
            productos = productoDAO.listarTodos();
        } catch (Exception e) {
            msj.mostrarError("Error", "", "Se ha producido un error recuperando los datos de la BD");
        }

        // Configura las celdas de la tabla para mostrar los datos de Producto
        // Utilizando PropertyValueFactory para enlazar las propiedades de la clase Producto con las columnas de la tabla
        this.colId.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        this.colNom.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.colPrecioVenta.setCellValueFactory(new PropertyValueFactory<>("precioVenta"));
        this.colPrecioLista.setCellValueFactory(new PropertyValueFactory<>("precioLista"));
        this.colCantMinima.setCellValueFactory(new PropertyValueFactory<>("cantidadMinima"));
        this.colCantDisponible.setCellValueFactory(new PropertyValueFactory<>("cantidadDisponible"));
        //this.colProveedor.setCellValueFactory(new PropertyValueFactory<>(""));

        // Establece los datos de la tabla con la lista de productos
        this.tablaProducto.setItems(productos);

        /*

        // Configura ComboBox de proveedores y rubros
        ObservableList<Producto> proveedores = null;
        ObservableList<Producto> rubros = null;

        ProductoDAOImpl productoDAO1 = new ProductoDAOImpl();
        ObservableList<Producto> productos1 = null;

        try {
            proveedores = productoDAO1.listarTodos();
            rubros = productoDAO1.listarTodos();
        } catch (Exception e) {
            msj.mostrarError("Error", "", "Se ha producido un error recuperando los datos de la BD");
        }

        // Configura ComboBox de proveedores
        proveedorBox.setItems(proveedores);

        // Configura ComboBox de rubros
        rubroBox.setItems(rubros);*/
    }









    private ObservableList<String> obtenerNombresRubrosDesdeDAO() {
        RubroDAOImpl rubroDAO = new RubroDAOImpl();
        ObservableList<String> nombresRubros = FXCollections.observableArrayList();

        try {
            // Obtén la lista de rubros con IDs y nombres desde el DAO
            ObservableList<Rubro> rubros = rubroDAO.listarTodos();

            // Extrae solo los nombres de los rubros y agrégalos a la lista
            for (Rubro rubro : rubros) {
                nombresRubros.add(rubro.getNombre());

            }
        } catch (Exception e) {
            // Manejar la excepción
        }

        return nombresRubros;
    }

    private ObservableList<String> obtenerNombresProveedoresDesdeDAO() {
        ProveedorDAOImpl proveedorDAO = new ProveedorDAOImpl();
        ObservableList<String> nombresProveedores = FXCollections.observableArrayList();

        try {
            // Obtén la lista de proveedores con nombres desde el DAO
            ObservableList<Proveedor> proveedores = proveedorDAO.listarTodos();

            // Extrae solo los nombres de los proveedores y agrégalos a la lista
            for (Proveedor proveedor : proveedores) {
                nombresProveedores.add(proveedor.getNombre());
            }
        } catch (Exception e) {
            // Manejar la excepción
        }

        return nombresProveedores;
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
