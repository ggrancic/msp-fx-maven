package com.mspdevs.mspfxmaven.controllers;

import com.mspdevs.mspfxmaven.model.DAO.*;
import com.mspdevs.mspfxmaven.model.Producto;
import com.mspdevs.mspfxmaven.model.Proveedor;
import com.mspdevs.mspfxmaven.model.Rubro;
import com.mspdevs.mspfxmaven.utils.Alerta;
import com.mspdevs.mspfxmaven.utils.ManejoDeBotones;
import com.mspdevs.mspfxmaven.utils.ManejoDeEntrada;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.util.List;


public class VentanaProductosController {

    Alerta msj = new Alerta();

    // Declarar una lista de respaldo para todos los productos originales
    private ObservableList<Producto> todosLosProductos;

    // Variable de instancia para el manejador de botones
    private ManejoDeBotones manejador;
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
    private TextField campoCodigo;

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
    private TableColumn<Producto, String> colCodigoBarra;

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
        String nombreIngresado = this.campoNombre.getText();
        Double precioVentaIngresado = Double.valueOf(this.campoVenta.getText());
        String codigoBarraIngresado = this.campoCodigo.getText();
        Integer cantDisponibleIngresado = Integer.valueOf(this.campoCantDisp.getText());
        Integer cantMinimaIngresado = Integer.valueOf(this.campoCantMin.getText());
        String RubroNombreSeleccionado = rubroBox.getSelectionModel().getSelectedItem();
        String ProveedorNombreSeleccionado = proveedorBox.getSelectionModel().getSelectedItem();

        ProveedorDAOImpl proveedorDAO = new ProveedorDAOImpl();
        RubroDAOImpl rubroDAO = new RubroDAOImpl();

        try {
            int ProveedorId = proveedorDAO.obtenerPorNombre(ProveedorNombreSeleccionado);
            int RubroId = rubroDAO.obtenerPorNombre(RubroNombreSeleccionado).getIdRubro();
            //msj.mostrarAlertaInforme("Operación exitosa", "", "El rubro id es " + RubroId + ", y proveedor: " + ProveedorId);
            Producto pro = new Producto();
            ProductoDAOImpl dao = new ProductoDAOImpl();

            pro.setNombre(nombreIngresado);
            pro.setPrecioVenta(precioVentaIngresado);
            pro.setCodigoBarra(codigoBarraIngresado);
            pro.setCantidadDisponible(cantDisponibleIngresado);
            pro.setCantidadMinima(cantMinimaIngresado);
            pro.setIdRubroFK(RubroId);
            pro.setIdProveedorFK(ProveedorId);
            try {
                dao.insertar(pro);
                msj.mostrarAlertaInforme("Operación exitosa", "", "Se ha agregado el producto correctamente.");
                completarTablaProductos();
                vaciarCampos();
                campoNombre.requestFocus();
            } catch (Exception e) {
                msj.mostrarError("Error", "", "No se pudo agregar el producto en la BD");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    void accionBotonEliminar(ActionEvent event) {
        Producto pro = this.tablaProducto.getSelectionModel().getSelectedItem();
        if (pro == null) {
            msj.mostrarError("Error", "", "Debe seleccionar un producto de la lista para eliminar");
        } else {
            try {
                ProductoDAOImpl dao = new ProductoDAOImpl();
                dao.eliminar(pro);
                completarTablaProductos();
                manejador.configurarBotones(false);
                campoNombre.requestFocus();
                msj.mostrarAlertaInforme("Operacion exitosa", "", "El producto se ha eliminado");
                vaciarCampos();
            } catch (Exception e) {
                msj.mostrarError("Error", "", "No se pudo eliminar el producto de la BD");
            }
        }
    }

    @FXML
    void accionBotonLimpiar(ActionEvent event) {
        // Limpia los campos de texto
        vaciarCampos();
        manejador.configurarBotones(false);
        // Deselecciona la fila en la tabla
        tablaProducto.getSelectionModel().clearSelection();
        campoNombre.requestFocus();
    }

    @FXML
    void accionBotonModificar(ActionEvent event) {
        // Obtiene el proveedor seleccionado en la tabla
        Producto pro = this.tablaProducto.getSelectionModel().getSelectedItem();

        if (pro == null) {
            // Muestra un mensaje de error si no se selecciona ningún elemento en la tabla
            msj.mostrarError("Error", "", "Debe seleccionar un producto de la lista para modificar.");
            return;
        }
        String nombreIngresado = this.campoNombre.getText().trim();
        Double precioVentaIngresado = Double.valueOf(this.campoVenta.getText());
        String codigoBarraIngresado = this.campoCodigo.getText();
        Integer cantDisponibleIngresado = Integer.valueOf(this.campoCantDisp.getText());
        Integer cantMinimaIngresado = Integer.valueOf(this.campoCantMin.getText());
        String RubroNombreSeleccionado = rubroBox.getSelectionModel().getSelectedItem();
        String ProveedorNombreSeleccionado = proveedorBox.getSelectionModel().getSelectedItem();

        ProveedorDAOImpl proveedorDAO = new ProveedorDAOImpl();
        RubroDAOImpl rubroDAO = new RubroDAOImpl();

        try {
            int ProveedorId = proveedorDAO.obtenerPorNombre(ProveedorNombreSeleccionado);
            int RubroId = rubroDAO.obtenerPorNombre(RubroNombreSeleccionado).getIdRubro();
            // Actualiza el nombre del rubro seleccionado con el contenido del campoNombre
            pro.setNombre(nombreIngresado);
            pro.setNombre(nombreIngresado);
            pro.setPrecioVenta(precioVentaIngresado);
            pro.setCodigoBarra(codigoBarraIngresado);
            pro.setCantidadDisponible(cantDisponibleIngresado);
            pro.setCantidadMinima(cantMinimaIngresado);
            pro.setIdRubroFK(RubroId);
            pro.setIdProveedorFK(ProveedorId);
            ProductoDAOImpl dao = new ProductoDAOImpl();
            dao.modificar(pro);
            completarTablaProductos();
            vaciarCampos();
            campoNombre.requestFocus();
            manejador.configurarBotones(false);
            msj.mostrarAlertaInforme("Operación exitosa", "", "El producto se ha modificado");
        } catch (Exception e) {
            msj.mostrarError("Error", "", "No se pudo modificar el producto en la BD");
        }
    }

    @FXML
    void buscarProductoFiltro(KeyEvent event) {
        // Obtener el texto ingresado en el campo de búsqueda
        String filtro = campoBuscar.getText().toLowerCase();

        if (filtro.isEmpty()) {
            // Si el campo de búsqueda está vacío, mostrar todos los empleados originales
            tablaProducto.setItems(todosLosProductos);
        } else {
            // Filtrar la lista de todos los empleados originales y mostrar los resultados
            ObservableList<Producto> empleadosFiltrados = todosLosProductos.filtered(producto
                    -> producto.getNombre().toLowerCase().startsWith(filtro)
            );
            tablaProducto.setItems(empleadosFiltrados);
        }
    }

    @FXML
    void initialize() throws Exception {
        completarTablaProductos();

        RubroDAOImpl rubroDAO = new RubroDAOImpl();
        ProveedorDAOImpl proveedorDAO = new ProveedorDAOImpl();

        // Obtener la lista de rubros y proveedores desde la base de datos
        List<Rubro> rubros = rubroDAO.listarTodos();
        List<Proveedor> proveedores = proveedorDAO.listarTodos();

        // Establecer el enfoque en campoNombre después de que la ventana se haya mostrado completamente
        Platform.runLater(() -> campoNombre.requestFocus());

        // Cargar los nombres en los ComboBox
        for (Rubro rubro : rubros) {
            rubroBox.getItems().add(rubro.getNombre());
        }
        for (Proveedor proveedor : proveedores) {
            proveedorBox.getItems().add(proveedor.getNombre());
        }

        todosLosProductos = tablaProducto.getItems();

        // Instancia el ManejadorBotones en la inicialización del controlador
        manejador = new ManejoDeBotones(btnModificar, btnEliminar, btnAgregar);
        // Para deshabilitar "Modificar" y "Eliminar" y habilitar "Agregar"
        manejador.configurarBotones(false);


        // Aplica el TextFormatter a los campos
        campoNombre.setTextFormatter(ManejoDeEntrada.soloLetrasNumEspAcento());
        campoVenta.setTextFormatter(ManejoDeEntrada.soloNumerosDecimales());
        campoCodigo.setTextFormatter(ManejoDeEntrada.soloCodigoBarra());
        campoCantDisp.setTextFormatter(ManejoDeEntrada.soloNumerosEnteros());
        campoCantMin.setTextFormatter(ManejoDeEntrada.soloNumerosEnteros());
    }

    public void completarTablaProductos() {
        ProductoDAOImpl productoDAO = new ProductoDAOImpl();
        ObservableList<Producto> productos = null;

        try {
            productos = productoDAO.listarTodos();
        } catch (Exception e) {
            e.printStackTrace();
            msj.mostrarError("Error", "", "Se ha producido un error recuperando los datos de la BD");
        }

        // Configura las celdas de la tabla para mostrar los datos de Producto
        // Utilizando PropertyValueFactory para enlazar las propiedades de la clase Producto con las columnas de la tabla
        this.colId.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        this.colNom.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.colPrecioVenta.setCellValueFactory(new PropertyValueFactory<>("precioVenta"));
        this.colCodigoBarra.setCellValueFactory(new PropertyValueFactory<>("codigoBarra"));
        this.colCantMinima.setCellValueFactory(new PropertyValueFactory<>("cantidadMinima"));
        this.colCantDisponible.setCellValueFactory(new PropertyValueFactory<>("cantidadDisponible"));
        this.colProveedor.setCellValueFactory(new PropertyValueFactory<>("proveedorNombre"));
        this.colRubro.setCellValueFactory(new PropertyValueFactory<>("rubroNombre"));

        // Establece los datos de la tabla con la lista de productos
        this.tablaProducto.setItems(productos);

        // Configura un listener para la selección de fila en la tabla
        tablaProducto.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                manejador.configurarBotones(true);
                // Llena los campos de entrada con los datos del proveedor seleccionado
                campoNombre.setText(newValue.getNombre());
                campoVenta.setText(String.valueOf(newValue.getPrecioVenta()));
                campoCodigo.setText(newValue.getCodigoBarra());
                campoCantDisp.setText(String.valueOf(newValue.getCantidadDisponible()));
                campoCantMin.setText(String.valueOf(newValue.getCantidadMinima()));

                // Selecciona automáticamente el Rubro y Proveedor en los ComboBox
                rubroBox.getSelectionModel().select(newValue.getRubroNombre());
                proveedorBox.getSelectionModel().select(newValue.getProveedorNombre());
            }
        });
    }

    public void vaciarCampos() {
        // Limpiar los campos de entrada
        campoNombre.setText("");
        campoVenta.setText("");
        campoCodigo.setText("");
        campoCantDisp.setText("");
        campoCantMin.setText("");
        // Desseleccionar elementos en los ComboBox
        rubroBox.getSelectionModel().clearSelection();
        rubroBox.setPromptText("Seleccionar");
        proveedorBox.getSelectionModel().clearSelection();
        proveedorBox.setPromptText("Seleccionar");
        // Colocar el foco en campoNombre
        campoNombre.requestFocus();
    }
}