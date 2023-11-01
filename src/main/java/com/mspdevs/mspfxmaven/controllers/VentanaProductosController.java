package com.mspdevs.mspfxmaven.controllers;

import com.mspdevs.mspfxmaven.model.DAO.*;
import com.mspdevs.mspfxmaven.model.Empleado;
import com.mspdevs.mspfxmaven.model.Producto;
import com.mspdevs.mspfxmaven.model.Proveedor;
import com.mspdevs.mspfxmaven.model.Rubro;
import com.mspdevs.mspfxmaven.utils.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import org.controlsfx.control.SearchableComboBox;

import java.util.Comparator;
import java.util.List;


public class VentanaProductosController {
    Alerta msj = new Alerta();
    // Declara una lista de respaldo para todos los productos originales
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
    private SearchableComboBox<String> proveedorBox;

    @FXML
    private SearchableComboBox<String> rubroBox;

    @FXML
    private TableView<Producto> tablaProducto;

    @FXML
    void accionBotonAgregar(ActionEvent event) throws Exception {
        String nombreIngresado = FormatoTexto.formatearTexto(this.campoNombre.getText());
        String precioVentaTexto = this.campoVenta.getText();
        String codigoBarraIngresado = this.campoCodigo.getText();
        String cantDisponibleTexto = this.campoCantDisp.getText();
        String cantMinimaTexto = this.campoCantMin.getText();
        String RubroNombreSeleccionado = rubroBox.getSelectionModel().getSelectedItem();
        String ProveedorNombreSeleccionado = proveedorBox.getSelectionModel().getSelectedItem();

        ProveedorDAOImpl proveedorDAO = new ProveedorDAOImpl();
        RubroDAOImpl rubroDAO = new RubroDAOImpl();

        // Verifica si algún campo de texto está vacío
        if (nombreIngresado.isEmpty() || precioVentaTexto.isEmpty() ||
                cantDisponibleTexto.isEmpty() || cantMinimaTexto.isEmpty()) {
            // Mostrar mensaje de error si falta ingresar datos
            msj.mostrarError("Error", "", "Falta ingresar datos.");
        } else {

                if (ValidacionDeEntrada.validarCodigoDeBarra(codigoBarraIngresado) &&
                        ValidacionDeEntrada.validarPrecioVenta(precioVentaTexto) &&
                        ValidacionDeEntrada.validarSeleccionComboBox(proveedorBox, "Debe seleccionar un proveedor.") &&
                        ValidacionDeEntrada.validarSeleccionComboBox(rubroBox, "Debe seleccionar un rubro.")) {
                    // Verificar valores numéricos
                    Double precioVentaIngresado = Double.valueOf(precioVentaTexto);
                    Integer cantDisponibleIngresado = Integer.valueOf(cantDisponibleTexto);
                    Integer cantMinimaIngresado = Integer.valueOf(cantMinimaTexto);
                    // Realizar la operación si todas las validaciones son exitosas
                    int ProveedorId = proveedorDAO.obtenerPorNombre(ProveedorNombreSeleccionado);
                    int RubroId = rubroDAO.obtenerPorNombre(RubroNombreSeleccionado).getIdRubro();

                    Producto pro = new Producto();
                    ProductoDAOImpl dao = new ProductoDAOImpl();

                    pro.setNombre(nombreIngresado);
                    pro.setPrecioVenta(precioVentaIngresado);
                    pro.setCodigoBarra(codigoBarraIngresado);
                    pro.setCantidadDisponible(cantDisponibleIngresado);
                    pro.setCantidadMinima(cantMinimaIngresado);
                    pro.setIdRubroFK(RubroId);
                    pro.setIdProveedorFK(ProveedorId);

                    System.out.println("id de rubro:" + pro.getIdRubroFK() + ", id de proveedor: " + pro.getIdProveedorFK());

                    try {
                        dao.insertar(pro);
                        msj.mostrarAlertaInforme("Operación exitosa", "", "Se ha agregado el producto correctamente.");
                        completarTablaProductos();
                        vaciarCampos();
                        campoNombre.requestFocus();
                        System.out.println("Cantidad de productos seleccionados: " + todosLosProductos.size());
                    } catch (Exception e) {
                        msj.mostrarError("Error", "", "No se pudo agregar el producto en la BD");
                        e.printStackTrace();
                    }
                }
            }
    }


    @FXML
    void accionBotonEliminar(ActionEvent event) {
        Producto pro = this.tablaProducto.getSelectionModel().getSelectedItem();
        if (pro == null) {
            msj.mostrarError("Error", "", "Debe seleccionar un producto de la lista para eliminar");
        } else {
            boolean confirmacion = msj.mostrarConfirmacion("Confirmar Eliminación", "",
                    "¿Está seguro de que desea eliminar este producto?");
            if (confirmacion) { // Si se confirma la eliminación
                try {
                    ProductoDAOImpl dao = new ProductoDAOImpl();
                    dao.eliminar(pro);
                    completarTablaProductos();
                    manejador.configurarBotones(false);
                    campoNombre.requestFocus();
                    msj.mostrarAlertaInforme("Operacion exitosa", "", "El producto se ha eliminado");
                    vaciarCampos();
                } catch (Exception e) {
                    msj.mostrarError("Error", "", "No se puede eliminar. El producto corresponde a una venta/compra.");
                }
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
        System.out.println("Cantidad de productos seleccionados: " + todosLosProductos.size());
    }

    @FXML
    void accionBotonModificar(ActionEvent event) throws Exception {
        // Obtiene el proveedor seleccionado en la tabla
        Producto pro = this.tablaProducto.getSelectionModel().getSelectedItem();

        if (pro == null) {
            // Muestra un mensaje de error si no se selecciona ningún elemento en la tabla
            msj.mostrarError("Error", "", "Debe seleccionar un producto de la lista para modificar.");
            return;
        }
        String nombreIngresado = FormatoTexto.formatearTexto(this.campoNombre.getText());
        String precioVentaTexto = this.campoVenta.getText();
        String codigoBarraIngresado = this.campoCodigo.getText().toUpperCase();
        String cantDisponibleTexto = this.campoCantDisp.getText();
        String cantMinimaTexto = this.campoCantMin.getText();
        String RubroNombreSeleccionado = rubroBox.getSelectionModel().getSelectedItem();
        String ProveedorNombreSeleccionado = proveedorBox.getSelectionModel().getSelectedItem();
        ProveedorDAOImpl proveedorDAO = new ProveedorDAOImpl();
        RubroDAOImpl rubroDAO = new RubroDAOImpl();

        // Verifica si algún campo de texto está vacío
        if (nombreIngresado.isEmpty() || precioVentaTexto.isEmpty() ||
                cantDisponibleTexto.isEmpty() || cantMinimaTexto.isEmpty()) {
            // Mostrar mensaje de error si falta ingresar datos
            msj.mostrarError("Error", "", "Falta ingresar datos.");
        } else {
            if (ValidacionDeEntrada.validarCodigoDeBarra(codigoBarraIngresado) &&
                    ValidacionDeEntrada.validarPrecioVenta(precioVentaTexto) &&
                    ValidacionDeEntrada.validarSeleccionComboBox(proveedorBox, "Debe seleccionar un proveedor.") &&
                    ValidacionDeEntrada.validarSeleccionComboBox(rubroBox, "Debe seleccionar un rubro.")) {
                // Verificar valores numéricos
                Double precioVentaIngresado = Double.valueOf(precioVentaTexto);
                Integer cantDisponibleIngresado = Integer.valueOf(cantDisponibleTexto);
                Integer cantMinimaIngresado = Integer.valueOf(cantMinimaTexto);
                // Realizar la operación si todas las validaciones son exitosas
                int ProveedorId = proveedorDAO.obtenerPorNombre(ProveedorNombreSeleccionado);
                int RubroId = rubroDAO.obtenerPorNombre(RubroNombreSeleccionado).getIdRubro();

                pro.setNombre(nombreIngresado);
                pro.setPrecioVenta(precioVentaIngresado);
                pro.setCodigoBarra(codigoBarraIngresado);
                pro.setCantidadDisponible(cantDisponibleIngresado);
                pro.setCantidadMinima(cantMinimaIngresado);
                pro.setIdRubroFK(RubroId);
                pro.setIdProveedorFK(ProveedorId);
                boolean confirmacion = msj.mostrarConfirmacion("Confirmar Modificación", "",
                        "¿Está seguro de que desea modificar este producto?");
                if (confirmacion) { // Si se confirma la eliminación
                    try {
                        ProductoDAOImpl dao = new ProductoDAOImpl();
                        dao.modificar(pro);
                        //completarTablaProductos();
                        tablaProducto.refresh();
                        completarTablaProductos();
                        vaciarCampos();
                        campoNombre.requestFocus();
                        manejador.configurarBotones(false);
                        msj.mostrarAlertaInforme("Operación exitosa", "", "El producto se ha modificado");
                    } catch (Exception e) {
                        msj.mostrarError("Error", "", "No se pudo modificar el producto.");
                        e.printStackTrace();
                        //System.out.println("id de rubro:" + pro.getIdRubroFK() + ", id de proveedor: " + pro.getIdProveedorFK());
                    }
                }
            }
        }
    }

    @FXML
    void buscarProductoFiltro(KeyEvent event) {
        // Obtener el texto ingresado en el campo de búsqueda
        String filtro = campoBuscar.getText().toLowerCase();

        if (filtro.isEmpty()) {
            // Si el campo de búsqueda está vacío, mostrar todos los productos originales
            tablaProducto.setItems(todosLosProductos);
        } else {
            // Filtrar la lista de todos los productos originales y mostrar los resultados
            ObservableList<Producto> productosFiltrados = todosLosProductos.filtered(producto
                    -> producto.getNombre().toLowerCase().startsWith(filtro) ||
                    producto.getCodigoBarra().toLowerCase().startsWith(filtro)
            );
            tablaProducto.setItems(productosFiltrados);
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
            proveedorBox.getItems().add(proveedor.getRazonSocial());
        }

        todosLosProductos = tablaProducto.getItems();

        // Instancia el ManejadorBotones en la inicialización del controlador
        manejador = new ManejoDeBotones(btnModificar, btnEliminar, btnAgregar);
        // Para deshabilitar "Modificar" y "Eliminar" y habilitar "Agregar"
        manejador.configurarBotones(false);

        // Aplica el TextFormatter a los campos
        campoNombre.setTextFormatter(ManejoDeEntrada.soloLetrasNumEspAcento());
        campoVenta.setTextFormatter(ManejoDeEntrada.soloNumerosDecimales());
        campoCodigo.setTextFormatter(ManejoDeEntrada.soloCodigoBarras());
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
        this.colProveedor.setCellValueFactory(new PropertyValueFactory<>("proveedorRazonSocial"));
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
                proveedorBox.getSelectionModel().select(newValue.getProveedorRazonSocial());
            }
        });

        colId.setSortType(TableColumn.SortType.ASCENDING);
        tablaProducto.getSortOrder().add(colId);
        tablaProducto.sort();
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