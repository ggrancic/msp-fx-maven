package com.mspdevs.mspfxmaven.controllers;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.UnaryOperator;

import com.mspdevs.mspfxmaven.model.Compra;
import com.mspdevs.mspfxmaven.model.DAO.*;
import com.mspdevs.mspfxmaven.model.DetalleCompra;
import com.mspdevs.mspfxmaven.model.Producto;
import com.mspdevs.mspfxmaven.model.Proveedor;
import com.mspdevs.mspfxmaven.utils.Alerta;
import com.mspdevs.mspfxmaven.utils.ManejoDeEntrada;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;
import org.controlsfx.control.SearchableComboBox;


public class VentanaComprasController implements Initializable {
    Alerta msj = new Alerta();

    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnCrearProducto;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnNuevoProveedor;

    @FXML
    private ToggleGroup busquedaProducto;

    @FXML
    private Spinner<Integer> campoCantidad;

    @FXML
    private DatePicker campoFecha;

    @FXML
    private TextField campoGanancia;

    @FXML
    private TextField campoIva;

    @FXML
    private TextField campoNombre;

    @FXML
    private TextField campoNumFactura;

    @FXML
    private TextField campoPrecioLista;

    @FXML
    private TextField campoPrecioVenta;

    @FXML
    private TextField campoSubtotal;

    @FXML
    private TextField campoTotal;

    @FXML
    private RadioButton codigoBarraRadio;

    @FXML
    private TableColumn<Producto, Integer> colCantidad;

    @FXML
    private TableColumn<Producto, Integer> colId;

    @FXML
    private TableColumn<Producto, String> colNom;

    @FXML
    private TableColumn<Producto, Double> colPL;

    @FXML
    private TableColumn<Producto, Double> colPV;

    @FXML
    private RadioButton nombreRadio;

    @FXML
    private SearchableComboBox<String> productoBox;

    @FXML
    private SearchableComboBox<String> proveedorBox;

    @FXML
    private TableView<Producto> tblDetalle;

    @FXML
    private ComboBox<String> tipoFacturaBox;

    private double totalPrecioLista = 0.0; // Variable para el precio total de lista
    private double subtotalTotal = 0.0; // Variable para rastrear el subtotal total
    private double ivaTotal = 0.0; // Variable para rastrear el IVA total
    private Date fechaMySQL; // Declarar fechaMySQL como variable miembro
    private ObservableList<Producto> todosLosProductos;
    private double subtotal = 0.0;
    private double iva = 0.0;
    private double total = 0.0;
    boolean productoEncontrado = false; // Variable para verificar si se encontró el producto

    @FXML
    void accionAgregarALista(ActionEvent event) throws Exception {
        // Recopila los datos de los campos
        String nombre = campoNombre.getText();
        String precioLista = campoPrecioLista.getText();
        String precioVenta = campoPrecioVenta.getText();
        String cantidad = String.valueOf(campoCantidad.getValue());
        String ganancia = campoGanancia.getText();

        // Validación: verifica si los campos requeridos están vacíos
        if (nombre.isEmpty() || precioVenta.isEmpty() || precioLista.isEmpty() || cantidad == "0" || cantidad.isEmpty() || ganancia.isEmpty()) {
            msj.mostrarAlertaInforme("Error", "", "Debe completar todos los campos.");
            return;
        }

        // Validación: verifica que el precio de lista y el precio de venta no sean 0.00
        double precioListaDoubleValidacion = Double.parseDouble(precioLista);
        double precioVentaDoubleValidacion = Double.parseDouble(precioVenta);
        if (precioListaDoubleValidacion <= 0 || precioVentaDoubleValidacion <= 0) {
            msj.mostrarAlertaInforme("Error", "", "El precio de lista y el precio de venta deben ser mayores que 0.00.");
            return;
        }

        // Validación: verifica que el precio de venta sea mayor que el precio de lista
        if (precioVentaDoubleValidacion <= precioListaDoubleValidacion) {
            msj.mostrarAlertaInforme("Error", "", "El precio de venta debe ser mayor que el precio de lista.");
            return;
        }

        // Deshabilita los campos
        habilitarCampos(false);


        // Obtiene el ID del producto desde la propiedad userData
        int productoId = (int) btnAgregar.getUserData(); // Accede al ID desde el botón Agregar

        System.out.println("el id de producto es: " + productoId);

        // Crea un nuevo objeto Producto
        Producto producto = new Producto();
        producto.setIdProducto(productoId); // Asigna el ID del producto
        producto.setNombre(nombre);
        producto.setPrecioLista(Double.parseDouble(String.valueOf(precioLista)));
        producto.setPrecioVenta(Double.parseDouble(precioVenta));
        producto.setCantidadDisponible(Integer.parseInt(cantidad));

        // Verifica si el producto ya existe en la tabla
        if (esProductoExistente(producto)) {
            // Muestra un mensaje de error y vacía los campos
            msj.mostrarAlertaInforme("Error", "", "El producto ya está en la lista.");
            campoNombre.clear();
            campoPrecioLista.clear();
            campoPrecioVenta.clear();
            campoCantidad.getValueFactory().setValue(1);
            campoGanancia.setText("0");
        } else {
            //todosLosProductos.add(producto);  // Agrega el producto a la lista observable
            // Agrega el producto a la TableView
            tblDetalle.getItems().add(producto);

            // Habilitar el botón "btnGuardar" después de agregar un producto
            btnGuardar.setDisable(false);

            // Limpia los campos después de agregar el producto a la lista
            campoNombre.clear();
            campoPrecioLista.clear();
            campoPrecioVenta.clear();
            campoCantidad.getValueFactory().setValue(1);
            campoGanancia.setText("0");

            // Calcula el monto total del producto (precio de lista * cantidad)
            double precioListaDouble = Double.parseDouble(precioLista);
            int cantidadInt = Integer.parseInt(cantidad);
            double montoTotalProducto = precioListaDouble * cantidadInt;

            // Agrega el monto total del producto al campo "campoTotal"
            totalPrecioLista += montoTotalProducto;

            // Calcula el precio de lista sin IVA (monto total / 1.21)
            double precioListaSinIva = montoTotalProducto / 1.21;

            // Calcula el IVA (precio total - precio de lista sin IVA)
            double iva = montoTotalProducto - precioListaSinIva;

            // Suma el subtotal y el IVA total
            subtotalTotal += precioListaSinIva;
            ivaTotal += iva;

            // Formatea los valores para mostrarlos en los campos
            // Define el formato para dos decimales
            DecimalFormat formatoDosDecimales = new DecimalFormat("#,##0.00");


            // Llama a la función para actualizar el resumen
            actualizarResumen();
            System.out.println("Cantidad de productos seleccionados: " + todosLosProductos.size());
        }
    }

    @FXML
    void accionCrearProducto(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mspdevs/mspfxmaven/views/ModalNuevoProducto.fxml"));
        Parent root = loader.load();

        ModalNuevoProductoController modalNuevoProductoController = loader.getController();
        modalNuevoProductoController.obtenerProveedor(proveedorBox.getValue());

        Scene scene = new Scene(root);
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.initStyle(StageStyle.UNDECORATED);
        newStage.initOwner(((Node)event.getSource()).getScene().getWindow() );
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.showAndWait();

        // Deseleccionar los elementos del RadioButton
        //busquedaProducto.selectToggle(null);

        // Vaciar el ComboBox
        productoBox.setItems(FXCollections.observableArrayList());
        campoNombre.clear();
        campoPrecioLista.clear();
        campoPrecioVenta.clear();
        campoCantidad.getValueFactory().setValue(1);
        campoGanancia.setText("0");
        campoGanancia.setDisable(true);
    }

    @FXML
    void accionGuardarCompra(ActionEvent event) throws Exception {
        System.out.println("Cantidad de productos seleccionados: " + todosLosProductos.size());

        String subtotalText = campoSubtotal.getText();
        String totalSinIvaText = campoIva.getText();
        String totalText = campoTotal.getText();
        String numero = campoNumFactura.getText();

        LocalDate fechaSeleccionada = campoFecha.getValue();
        if (fechaSeleccionada != null) {
            // Formatea la fecha en el formato de MySQL 'yyyy-MM-dd' y asignarla a la variable de miembro
            DateTimeFormatter formatoMySQL = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            fechaMySQL = java.sql.Date.valueOf(fechaSeleccionada);
            System.out.println("Fecha de Compra (formato MySQL): " + fechaMySQL);
        } else {
            System.err.println("Fecha no seleccionada");
        }

        String tipo = tipoFacturaBox.getValue();
        String ProveedorNombreSeleccionado = proveedorBox.getSelectionModel().getSelectedItem();

        ProveedorDAOImpl proveedorDAO = new ProveedorDAOImpl();
        int ProveedorId = proveedorDAO.obtenerPorNombre(ProveedorNombreSeleccionado);

        // Asegurarse de que todos los campos requeridos se hayan completado
        if (numero.isEmpty() || tipo == null) {
            msj.mostrarError("Error", "", "Por favor, complete todos los campos obligatorios.");
            return;
        }

        // Elimina los puntos como separadores de miles
        subtotalText = subtotalText.replace(".", "");
        totalSinIvaText = totalSinIvaText.replace(".", "");
        totalText = totalText.replace(".", "");

        // Reemplaza las comas por puntos
        subtotalText = subtotalText.replace(",", ".");
        totalSinIvaText = totalSinIvaText.replace(",", ".");
        totalText = totalText.replace(",", ".");

        // Convierte los valores a tipos numéricos
        double subtotal = Double.parseDouble(subtotalText);
        double totalSinIva = Double.parseDouble(totalSinIvaText);
        double total = Double.parseDouble(totalText);

        // Luego, inserta la compra en la base de datos
        CompraDAOImpl compraDAO = new CompraDAOImpl();
        ProductoDAOImpl productoDAO = new ProductoDAOImpl();
        DetalleCompraDAOImpl detalleCompraDAO = new DetalleCompraDAOImpl();

        // Crea un objeto Compra con los datos recopilados
        Compra compra = new Compra();
        compra.setFecha(fechaMySQL);
        compra.setNumeroFactura(numero);
        compra.setSubtotal(subtotal);
        compra.setTotalSinIva(totalSinIva);
        compra.setTotal(total);
        compra.setTipo(tipo);
        compra.setIdProveedorFK(ProveedorId);

        String nuevoProveedorSeleccionado = proveedorBox.getSelectionModel().getSelectedItem();

        // Obtiene el ID del nuevo proveedor basado en su nombre
        int nuevoProveedorId = proveedorDAO.obtenerPorNombre(nuevoProveedorSeleccionado);

        System.out.println("id del proveedor: " + nuevoProveedorId);

        try {
            for (Producto producto : todosLosProductos) {
                int idProducto = producto.getIdProducto();
                double nuevoPrecioDeLista = producto.getPrecioLista();
                double nuevoPrecioDeVenta = producto.getPrecioVenta();

                // Obtiene la cantidad disponible anterior
                int cantidadDisponibleAnterior = productoDAO.obtenerCantidadDisponiblePorId(idProducto);

                // Calcula la nueva cantidad disponible (cantidad anterior + cantidad nueva)
                int nuevaCantidadDisponible = cantidadDisponibleAnterior + producto.getCantidadDisponible();

                // Actualiza el producto en la base de datos con la nueva cantidad disponible y el nuevo proveedor
                productoDAO.actualizarProducto(idProducto, nuevoPrecioDeLista, nuevoPrecioDeVenta, nuevaCantidadDisponible, nuevoProveedorId);

                msj.mostrarAlertaInforme("Operación exitosa", "", "Se ha modificado el producto y el proveedor.");
            }
        } catch (Exception e){

        }

        // Realiza la inserción de la compra y obtener el ID de la compra generada
        int idCompraGenerada = compraDAO.insertarCompra(compra);

        // Luego, usa idCompraGenerada para insertar en la tabla "detalle_compras"
        try {
            //DetalleCompraDAOImpl detalleCompraDAO = new DetalleCompraDAOImpl();
            for (Producto producto : todosLosProductos) {
                int idProducto = producto.getIdProducto();
                int cantidad = producto.getCantidadDisponible();
                double precio = producto.getPrecioLista();

                // Inserta el detalle de compra
                DetalleCompra detalleCompra = new DetalleCompra();
                detalleCompra.setCantidad(cantidad);
                detalleCompra.setPrecio(precio);
                detalleCompra.getFacturaCompra().setId_factura_compras(idCompraGenerada); // Usar el ID de la compra generada
                detalleCompra.getProducto().setIdProducto(idProducto);

                detalleCompraDAO.insertar(detalleCompra);
            }

            msj.mostrarAlertaInforme("Operación exitosa", "", "Se han agregado los detalles de compra.");
        } catch (Exception e){
            // Manejo de errores
        }
        vaciarCampos();
        try {
            // Limpia la lista de clientes
            productoBox.getItems().clear();
        } catch (Exception ex) {
            // Manejar cualquier excepción que pueda ocurrir al limpiar
            ex.printStackTrace(); // O usa otro método de manejo de errores
        }
        btnGuardar.setDisable(true);
    }

    @FXML
    void accionNuevoProveedor(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mspdevs/mspfxmaven/views/ModalNuevoProveedor.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.initStyle(StageStyle.UNDECORATED);
        newStage.initOwner(((Node)event.getSource()).getScene().getWindow() );
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.showAndWait();

        actualizarComboBoxProveedores();
    }

    @FXML
    void buscarPorCodigo(ActionEvent event) {

    }

    @FXML
    void buscarPorNombre(ActionEvent event) {

    }

    public void initialize(URL location, ResourceBundle resources) {
        // Configura la TableView para usar la lista observable
        todosLosProductos = tblDetalle.getItems();

        // Para eliminar productos de la TableView y de la lista observable
        tblDetalle.setRowFactory(tv -> {
            TableRow<Producto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    // Obtiene el producto de la fila seleccionada
                    Producto producto = row.getItem();

                    // Crea un cuadro de diálogo de confirmación
                    boolean confirmado = msj.mostrarConfirmacion("Confirmación", "", "¿Está seguro de que desea quitar este producto de la tabla?");

                    if (confirmado) {
                        // Si el usuario confirma, elimina el producto de la lista observable y actualiza el TableView
                        todosLosProductos.remove(producto);

                        // Actualiza el resumen
                        actualizarResumen();
                    }
                }
            });
            return row;
        });

        productoBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                // Obtiene el nombre o código de barras seleccionado
                String nombreSeleccionado = newValue;

                // Habilita los campos después de cargar el producto
                habilitarCampos(true);
                campoNombre.setEditable(false);

                // Verifica el Toggle seleccionado
                Toggle selectedToggle = busquedaProducto.getSelectedToggle();

                if (selectedToggle == nombreRadio) {
                    // Realiza la búsqueda por nombre
                    productoEncontrado = mostrarDetallesProductoPorNombre(nombreSeleccionado);
                    // Borra el valor seleccionado en el SearchableComboBox
                    productoBox.getSelectionModel().clearSelection();
                } else if (selectedToggle == codigoBarraRadio) {
                    // Realiza la búsqueda por código de barras
                    productoEncontrado = mostrarDetallesProductoPorCodigoBarra(nombreSeleccionado);
                    // Borra el valor seleccionado en el SearchableComboBox
                    productoBox.getSelectionModel().clearSelection();
                }


                if (!productoEncontrado) {
                    // Manejar la situación en la que no se encontró el producto
                    msj.mostrarAlertaInforme("Error", "", "Producto no encontrado");
                }
            }
        });

        // Crea un listener que verifica los campos antes de habilitar el botón
        //btnAgregarProducto.setDisable(true); // Inicialmente, el botón está deshabilitado
        btnAgregar.setDisable(true);
        btnGuardar.setDisable(true);
        btnCrearProducto.setDisable(true);

        // Agrega un listener para el campo "campoNumFactura"
        campoNumFactura.textProperty().addListener((observable, oldValue, newValue) -> {
            //alidarCamposYHabilitarBoton(btnAgregarProducto);
            validarCamposYHabilitarBoton(btnAgregar);
            validarCamposYHabilitarBoton(btnCrearProducto);
            //validarCamposYHabilitarBoton(btnGuardar);
        });

        // Agrega un listener para el ComboBox "tipoFacturaBox"
        tipoFacturaBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            //validarCamposYHabilitarBoton(btnAgregarProducto);
            validarCamposYHabilitarBoton(btnAgregar);
            validarCamposYHabilitarBoton(btnCrearProducto);
            //validarCamposYHabilitarBoton(btnGuardar);
        });

        // Agrega un listener para el SearchableComboBox "proveedorBox"
        proveedorBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            //validarCamposYHabilitarBoton(btnAgregarProducto);
            validarCamposYHabilitarBoton(btnAgregar);
            validarCamposYHabilitarBoton(btnCrearProducto);
            //validarCamposYHabilitarBoton(btnGuardar);
        });

        // Personaliza el formato del DatePicker a "año/mes/día"
        String pattern = "yyyy/MM/dd";
        campoFecha.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });

        tipoFacturaBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String tipoFactura = newValue;
                if ("A".equals(tipoFactura)) {
                    // Tipo de factura A
                    actualizarResumen(); // Llama a la función para actualizar el resumen
                } else if ("B".equals(tipoFactura) || "C".equals(tipoFactura)) {
                    // Tipo de factura B o C
                    actualizarResumen(); // Llama a la función para actualizar el resumen
                }
            }
        });

        completarTablaProductos();
        habilitarCampos(false);

        ProveedorDAOImpl proveedorDAO = new ProveedorDAOImpl();
        ProductoDAOImpl productoDAO = new ProductoDAOImpl();

        // Obteniene la lista de proveedores desde la base de datos
        List<Proveedor> proveedores = null;
        try {
            proveedores = proveedorDAO.listarTodos();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Cargar los nombres en los ComboBox
        for (Proveedor proveedor : proveedores) {
            proveedorBox.getItems().add(proveedor.getRazonSocial());
        }

        // Configura el ToggleGroup para los radio buttons
        nombreRadio.setToggleGroup(busquedaProducto);
        codigoBarraRadio.setToggleGroup(busquedaProducto);

        // Obteniene la lista de productos desde la base de datos
        List<Producto> productos = null;
        try {
            productos = productoDAO.listarTodos();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Agrega un Listener para el ToggleGroup para detectar cuál radio button está seleccionado
        List<Producto> finalProductos = productos;
        busquedaProducto.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            //productoBox.getItems().clear(); // Limpia el productoBox

            if (newToggle == nombreRadio) {
                // Habilita la búsqueda por nombre
                productoBox.getItems().setAll(cargarNombresProductos());
            } else if (newToggle == codigoBarraRadio) {
                // Habilita la búsqueda por código de barras
                productoBox.getItems().setAll(cargarCodigosBarrasProductos());
            }
        });

        // Obteniene la fecha actual
        Date fechaActual = new Date();
        // Formatea la fecha como una cadena (por ejemplo, "yyyy-MM-dd")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fechaComoCadena = dateFormat.format(fechaActual);
        // Establece la fecha actual en el campoFecha
        campoFecha.setValue(LocalDate.parse(fechaComoCadena));

        // Crea una lista de valores para el ComboBox
        tipoFacturaBox.getItems().addAll("A", "B", "C");

        // Agrega un listener al campo de ganancia
        campoGanancia.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !campoPrecioLista.getText().isEmpty()) {
                calcularPrecioVenta();
            }
        });

        // Agrega un listener al campo de precio de lista
        campoPrecioLista.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !campoGanancia.getText().isEmpty()) {
                calcularPrecioVenta();
            }
        });

        // Crea una SpinnerValueFactory para manejar los valores del Spinner
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999, 1);

        // Asigna la SpinnerValueFactory al Spinner
        campoCantidad.setValueFactory(valueFactory);

        // Establece el valor inicial en 0
        campoCantidad.getValueFactory().setValue(1);
        //campoPrecioVenta.setText("0");
        campoGanancia.setText("0");

        campoCantidad.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) {
                campoCantidad.getValueFactory().setValue(1);
            }
        });

        campoNumFactura.setTextFormatter(ManejoDeEntrada.soloNumerosFactura());
        campoPrecioLista.setTextFormatter(ManejoDeEntrada.soloNumerosDecimales());
        campoPrecioVenta.setTextFormatter(ManejoDeEntrada.soloNumerosDecimales());
        campoGanancia.setTextFormatter(ManejoDeEntrada.soloCantidadGanancia());
        campoCantidad.getEditor().setTextFormatter(ManejoDeEntrada.soloCantidad());

        // Agrega un listener para escuchar los cambios en el valor del Spinner
        campoCantidad.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue > 999) {
                campoCantidad.getValueFactory().setValue(999);
            }
        });
    }

    // Esta función verifica si todos los campos requeridos tienen datos y habilita los botones
    private void validarCamposYHabilitarBoton(Button boton) {
        String numeroFactura = campoNumFactura.getText();
        String tipoFactura = tipoFacturaBox.getSelectionModel().getSelectedItem();
        String proveedor = proveedorBox.getValue();

        boolean camposValidos = !numeroFactura.isEmpty() && numeroFactura.length() == 12 && tipoFactura != null && proveedor != null;
        boton.setDisable(!camposValidos);
    }

    private boolean mostrarDetallesProductoPorNombre(String nombreProducto) {
        // Llama a tu método de DAO para obtener los detalles del producto por su nombre
        ProductoDAOImpl productoDAO = new ProductoDAOImpl();
        Producto productoSeleccionado = null;

        try {
            productoSeleccionado = productoDAO.obtenerProductoPorNombre(nombreProducto);
        } catch (Exception e) {
            e.printStackTrace();
            // Manejo de errores si es necesario
        }

        if (productoSeleccionado != null) {
            // Rellena los campos con los detalles del producto
            campoNombre.setText(productoSeleccionado.getNombre());
            campoPrecioLista.setText(Double.toString(productoSeleccionado.getPrecioLista()));
            campoPrecioVenta.setText(Double.toString(productoSeleccionado.getPrecioVenta()));
            // Asigna el ID del producto a la propiedad userData de un elemento apropiado
            btnAgregar.setUserData(productoSeleccionado.getIdProducto()); // Asigna el ID al botón Agregar
            return true; // Producto encontrado
        } else {
            msj.mostrarError("Error", "", "Producto no encontrado");
            return false; // Producto no encontrado
        }
    }

    private boolean mostrarDetallesProductoPorCodigoBarra(String codigoBarra) {
        // Llama a tu método de DAO para obtener los detalles del producto por su código de barras
        ProductoDAOImpl productoDAO = new ProductoDAOImpl();
        Producto productoSeleccionado = null;

        try {
            productoSeleccionado = productoDAO.obtenerProductoPorCodigoBarra(codigoBarra);
        } catch (Exception e) {
            e.printStackTrace();
            // Manejo de errores si es necesario
        }

        if (productoSeleccionado != null) {
            // Rellena los campos con los detalles del producto
            campoNombre.setText(productoSeleccionado.getNombre());
            campoPrecioLista.setText(Double.toString(productoSeleccionado.getPrecioLista()));
            campoPrecioVenta.setText(Double.toString(productoSeleccionado.getPrecioVenta()));
            // Asigna el ID del producto a la propiedad userData de un elemento apropiado
            btnAgregar.setUserData(productoSeleccionado.getIdProducto()); // Asigna el ID al botón Agregar
            return true; // Producto encontrado
        } else {
            msj.mostrarError("Error", "", "Producto no encontrado");
            return false; // Producto no encontrado
        }
    }

    private ObservableList<String> cargarNombresProductos() {
        ObservableList<String> nombres = FXCollections.observableArrayList();
        ProductoDAOImpl productoDAO = new ProductoDAOImpl();

        try {
            // Usa el ProductoDAOImpl para obtener los nombres de productos desde la base de datos
            List<Producto> productos = productoDAO.listarTodos();
            for (Producto producto : productos) {
                nombres.add(producto.getNombre());
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Manejo de errores si es necesario
        }

        return nombres;
    }

    private ObservableList<String> cargarCodigosBarrasProductos() {
        ObservableList<String> codigosBarras = FXCollections.observableArrayList();
        ProductoDAOImpl productoDAO = new ProductoDAOImpl();

        try {
            // Usa el ProductoDAOImpl para obtener los códigos de barras de productos desde la base de datos
            List<Producto> productos = productoDAO.listarTodos();
            for (Producto producto : productos) {
                codigosBarras.add(producto.getCodigoBarra());
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Manejo de errores si es necesario
        }

        return codigosBarras;
    }

    private void calcularPrecioVenta() {
        // Obtiene los valores actuales de precio de lista y ganancia
        double precioLista = Double.parseDouble(campoPrecioLista.getText());
        double ganancia = Double.parseDouble(campoGanancia.getText());

        // Calcula el precio de venta
        double precioVenta = precioLista + (precioLista * ganancia / 100);

        // Actualiza el campo de precio de venta
        campoPrecioVenta.setText(String.valueOf(precioVenta));
    }

    public void vaciarCampos() {
        // Limpia los campos de entrada
        campoNumFactura.setText("");
        tipoFacturaBox.setValue("Seleccionar");
        proveedorBox.getSelectionModel().select(null); // Esto debería borrar la selección
        campoSubtotal.setText("");
        campoIva.setText("");
        campoTotal.setText("");
        subtotalTotal = 0.0;
        ivaTotal = 0.0;
        totalPrecioLista = 0.0;
        // Limpia la TableView
        tblDetalle.getItems().clear();
        codigoBarraRadio.setSelected(false);
        nombreRadio.setSelected(false);
        productoBox.setValue("");
    }

    private void habilitarCampos(boolean habilitar) {
        campoNombre.setDisable(!habilitar);
        campoPrecioLista.setDisable(!habilitar);
        campoGanancia.setDisable(!habilitar);
        campoPrecioVenta.setDisable(!habilitar);
        campoCantidad.setDisable(!habilitar);
    }

    public void completarTablaProductos() {
        ProductoDAOImpl productoDAO = new ProductoDAOImpl();
        ObservableList<Producto> productos = null;

        try {
            // Recupera la lista de productos desde la base de datos.
            productos = productoDAO.listarTodos();
        } catch (Exception e) {
            e.printStackTrace();
            msj.mostrarError("Error", "", "Se ha producido un error recuperando los datos de la BD");
        }

        // Configura las celdas de la tabla para mostrar los datos de Producto
        // Utilizando PropertyValueFactory para enlazar las propiedades de la clase Producto con las columnas de la tabla
        this.colId.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        this.colNom.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.colPL.setCellValueFactory(new PropertyValueFactory<>("precioLista"));
        this.colPV.setCellValueFactory(new PropertyValueFactory<>("precioVenta"));
        this.colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidadDisponible"));

        this.colPL.setCellFactory(col -> new PrecioListaCell());
        this.colPV.setCellFactory(col -> new PrecioVentaCell());
    }

    public class PrecioListaCell extends TableCell<Producto, Double> {
        private final DecimalFormat formatoDosDecimales = new DecimalFormat("#,##0.00");

        @Override
        protected void updateItem(Double precioLista, boolean empty) {
            super.updateItem(precioLista, empty);

            if (empty || precioLista == null) {
                // Si la celda está vacía o el precio de lista es nulo, no se muestra ningún texto.
                setText(null);
            } else {
                // Formatea el precio de lista con dos decimales y lo muestra en la celda.
                String precioListaFormateado = formatoDosDecimales.format(precioLista);
                setText(precioListaFormateado);
            }
        }
    }

    public class PrecioVentaCell extends TableCell<Producto, Double> {
        private final DecimalFormat formatoDosDecimales = new DecimalFormat("#,##0.00");

        @Override
        protected void updateItem(Double precioVenta, boolean empty) {
            super.updateItem(precioVenta, empty);

            if (empty || precioVenta == null) {
                // Si la celda está vacía o el precio de venta es nulo, no se muestra ningún texto.
                setText(null);
            } else {
                // Formatea el precio de venta con dos decimales y lo muestra en la celda.
                String precioVentaFormateado = formatoDosDecimales.format(precioVenta);
                setText(precioVentaFormateado);
            }
        }
    }

    private boolean esProductoExistente(Producto productoNuevo) {
        for (Producto productoExistente : tblDetalle.getItems()) {
            // Comprueba si un producto con el mismo ID ya existe en la tabla.
            if (productoExistente.getIdProducto() == productoNuevo.getIdProducto()) {
                return true; // El producto ya existe en la tabla.
            }
        }
        return false; // El producto no existe en la tabla.
    }

    private void actualizarResumen() {
        // Inicializa variables para el subtotal, IVA y total
        subtotal = 0.0;
        iva = 0.0;
        total = 0.0;

        // Itera a través de todos los productos en la lista
        for (Producto producto : todosLosProductos) {
            double precioListaDouble = producto.getPrecioLista();
            int cantidadInt = producto.getCantidadDisponible();
            double montoTotalProducto = precioListaDouble * cantidadInt;

            // Calcula el precio de lista sin IVA (monto total / 1.21)
            double precioListaSinIva = montoTotalProducto / 1.21;

            // Calcula el IVA (precio total - precio de lista sin IVA)
            double ivaProducto = montoTotalProducto - precioListaSinIva;

            // Suma el subtotal y el IVA total
            subtotal += precioListaSinIva;
            iva += ivaProducto;

            // Agrega el monto total del producto al campo "total"
            total += montoTotalProducto;
        }

        // Define el formato para dos decimales
        DecimalFormat formatoDosDecimales = new DecimalFormat("#,##0.00");

        // Obtén el tipo de factura seleccionado
        String tipoFactura = tipoFacturaBox.getValue();

        if ("A".equals(tipoFactura)) {
            // Tipo de factura A
            String subtotalFormateado = formatoDosDecimales.format(subtotal);
            String ivaFormateado = formatoDosDecimales.format(iva);
            String totalFormateado = formatoDosDecimales.format(total);

            // Actualiza los campos de subtotal, IVA y total
            campoSubtotal.setText(subtotalFormateado);
            campoIva.setText(ivaFormateado);
            campoTotal.setText(totalFormateado);
        } else if ("B".equals(tipoFactura) || "C".equals(tipoFactura)) {
            // Tipo de factura B o C
            campoSubtotal.setText(formatoDosDecimales.format(total));

            // Establece el campo IVA en "0.00"
            campoIva.setText("0.00");

            // Actualiza el campo total
            campoTotal.setText(formatoDosDecimales.format(total));
        }
        // Habilita o deshabilita el botón de guardar según el valor de totalPrecioLista
        btnGuardar.setDisable(total == 0.0);
    }

    private void actualizarComboBoxProveedores() {
        ProveedorDAOImpl proveedorDAO = new ProveedorDAOImpl();

        List<Proveedor> proveedores = null;
        try {
            proveedores = proveedorDAO.listarTodos();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            if (!proveedorBox.getItems().isEmpty()) {
                proveedorBox.getItems().clear();
            }
        } catch (IndexOutOfBoundsException e) {

        }
        // Luego, agrega los nombres de proveedores nuevamente
        for (Proveedor proveedor : proveedores) {
            proveedorBox.getItems().add(proveedor.getRazonSocial());
        }
        // Asegúrate de que el ComboBox esté seleccionando el primer elemento
        if (!proveedorBox.getItems().isEmpty()) {
            proveedorBox.getSelectionModel().select(0);
        }
    }
}