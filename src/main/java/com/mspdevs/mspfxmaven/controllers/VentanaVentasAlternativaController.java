package com.mspdevs.mspfxmaven.controllers;

import com.mspdevs.mspfxmaven.model.*;
import com.mspdevs.mspfxmaven.model.DAO.ClienteDAOImpl;
import com.mspdevs.mspfxmaven.model.DAO.DetalleVentaDAOImpl;
import com.mspdevs.mspfxmaven.model.DAO.ProductoDAOImpl;
import com.mspdevs.mspfxmaven.model.DAO.VentaDAOImpl;
import com.mspdevs.mspfxmaven.utils.Alerta;
import com.mspdevs.mspfxmaven.utils.ManejoDeEntrada;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.controlsfx.control.SearchableComboBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class VentanaVentasAlternativaController implements Initializable {
    Alerta msj = new Alerta();

    private Caja caja;

    @FXML
    private Button btnFinalizarVenta;

    @FXML
    private Button btnNuevoCliente;

    @FXML
    private Spinner<Integer> campoCantidad;

    @FXML
    private DatePicker campoFecha;

    @FXML
    private TextField campoIva;

    @FXML
    private TextField campoNumFactura;

    @FXML
    private TextField campoSubtotal;

    @FXML
    private TextField campoTotal;

    @FXML
    private SearchableComboBox<String> clienteBox;

    @FXML
    private TableColumn<DetalleVenta, Integer> colCantidad;

    @FXML
    private TableColumn<DetalleVenta, Producto> colCodBarra;

    @FXML
    private TableColumn<DetalleVenta, Producto> colNom;

    @FXML
    private TableColumn<DetalleVenta, Producto> colPU;

    @FXML
    private TableColumn<DetalleVenta, Double> colTotal;

    @FXML
    private TableColumn<DetalleVenta, Void> colEliminar;

    @FXML
    private SearchableComboBox<String> productoBox;

    @FXML
    private TableView<DetalleVenta> tblDetalle;

    @FXML
    private ComboBox<String> tipoFacturaBox;

    @FXML
    private Button btnAgregar;

    @FXML
    private Label lblCantDisponible;

    private double totalPrecioLista = 0.0; // Variable para el precio total de lista
    private double subtotalTotal = 0.0; // Variable para rastrear el subtotal total
    private double ivaTotal = 0.0; // Variable para rastrear el IVA total

    private double subtotal = 0.0;
    private double iva = 0.0;
    private double total = 0.0;

    private Date fechaMySQL; // Declarar fechaMySQL como variable miembro

    private ObservableList<DetalleVenta> listaDetalles;

    // Define el listener como una variable miembro
    ChangeListener<String> productoBoxListener = null;

    // Variable para rastrear si el listener está habilitado
    boolean listenerHabilitado = true;

    @FXML
    private Button botonInvisible;
    private int usuario;

    boolean productoEncontrado = false;

    // Declara esta lista al principio de tu controlador
    private List<String> productosConCantidadMinima = new ArrayList<>();
    
    
    // ----------- METODOS ----------

    // Define un evento personalizado para limpiar la selección en productoBox
    public static final EventType<Event> LIMPIAR_SELECCION_EVENT_TYPE = new EventType<>(Event.ANY, "LIMPIAR_SELECCION_EVENT");

    private void limpiarSeleccionProductoBox() {
        // Lógica para limpiar la selección en productoBox
        productoBox.valueProperty().set("");
    }

    @FXML
    void accionAgregarALista(ActionEvent event) {
        // Obtiene el producto seleccionado
        String selectedValue = productoBox.getValue();

        ProductoDAOImpl productoDAO = new ProductoDAOImpl();

        if (selectedValue != null) {
            // Obtiene la cantidad del Spinner
            int cantidad = campoCantidad.getValue();

            try {
                // Obtiene la información del producto seleccionado
                Producto producto = productoDAO.obtenerProductoPorNombreOCodigoBarra(selectedValue);


                if (producto != null) {
                    // Verifica si el producto ya existe en la lista
                    boolean productoDuplicado = false;
                    for (DetalleVenta detalle : listaDetalles) {
                        if (detalle.getProducto().getIdProducto() == producto.getIdProducto()) {
                            productoDuplicado = true;
                            break;
                        }
                    }

                    if (!productoDuplicado) {
                        // Crea un detalle de venta con la información del producto y la cantidad
                        DetalleVenta detalle = new DetalleVenta();
                        detalle.setProducto(producto);
                        detalle.setCantidad(cantidad);
                        detalle.setMonto(producto.getPrecioVenta() * cantidad);

                        // Agrega el detalle a la lista
                        listaDetalles.add(detalle);

                        // Limpia la selección en productoBox y reinicia el Spinner
                        productoBox.getSelectionModel().clearSelection();
                        campoCantidad.getValueFactory().setValue(0);
                        lblCantDisponible.setText("");

                        // Actualiza la tabla y otros valores como el resumen
                        tblDetalle.setItems(listaDetalles);
                        actualizarResumen();
                    } else {
                        // Muestra un mensaje indicando que el producto ya se agregó.
                        msj.mostrarError("Advertencia", "", "Este producto ya ha sido agregado a la lista.");
                    }
                } else {
                    // Manejo si el producto no es encontrado
                    msj.mostrarError("Error", "", "Producto no encontrado");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            // Manejo si no se ha seleccionado un producto
            msj.mostrarError("Advertencia", "", "Seleccione un producto antes de agregarlo a la lista.");
        }
    }

    @FXML
    void accionGuardarVenta(ActionEvent event) throws Exception {
    	
    	// ----- INICIO ATRIBUTOS CABECERA -----
        String subtotalText = campoSubtotal.getText();
        String totalSinIvaText = campoIva.getText();
        String totalText = campoTotal.getText();
        String numeroFactura = campoNumFactura.getText();

        LocalDate fechaSeleccionada = campoFecha.getValue();
        if (fechaSeleccionada != null) {
            // Formatea la fecha en el formato de MySQL 'yyyy-MM-dd' y asignarla a la variable de miembro
            DateTimeFormatter formatoMySQL = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            fechaMySQL = java.sql.Date.valueOf(fechaSeleccionada);
        } else {
            //System.err.println("Fecha no seleccionada");
        }

        String tipo = tipoFacturaBox.getValue();
        String razonSocialSeleccionada = clienteBox.getSelectionModel().getSelectedItem();

        ClienteDAOImpl clienteDAO = new ClienteDAOImpl();
        int ClienteId = clienteDAO.obtenerPorRazonSocial(razonSocialSeleccionada);

        // Asegura de que todos los campos requeridos se hayan completado
        if (numeroFactura.isEmpty() || tipo == null) {
            msj.mostrarError("Error", "", "Por favor, complete todos los campos obligatorios.");
            return;
        }
        
        
        // ----- FIN ATRIBUTOS CABECERA ----
        
        // Luego, inserta la compra en la base de datos
        VentaDAOImpl ventaDAO = new VentaDAOImpl();
        ProductoDAOImpl productoDAO = new ProductoDAOImpl();
        DetalleVentaDAOImpl detalleVentaDAO = new DetalleVentaDAOImpl();

        // Crea un objeto Compra con los datos recopilados
        Venta venta = new Venta();
        venta.setFechaEmision(fechaMySQL);
        venta.setNumeroFactura(numeroFactura);
        venta.setSubtotal(Double.parseDouble(subtotalText.replace(",", ".")));
        venta.setIva(Double.parseDouble(totalSinIvaText.replace(",", ".")));
        venta.setTotal(Double.parseDouble(totalText.replace(",",".")));
        venta.setTipo(tipo);
        venta.getCliente().setIdCliente(ClienteId);
        venta.getEmpleado().setId_empleado(usuario);

        caja.setIngresos(Double.parseDouble(totalText.replace(",", ".")));

        String nuevaRazonSocialSeleccionada = clienteBox.getSelectionModel().getSelectedItem();

        // Obtiene el ID del nuevo proveedor basado en su nombre
        int nuevoClienteId = clienteDAO.obtenerPorRazonSocial(nuevaRazonSocialSeleccionada);
        
     // Realiza la inserción de la compra y obtener el ID de la compra generada
        int idVentaGenerada = ventaDAO.insertarVenta(venta);

        // Limpia la lista antes de procesar la venta actual
        productosConCantidadMinima.clear();

        try {
            for (DetalleVenta detalle : listaDetalles) {
                int idProducto = detalle.getProducto().getIdProducto();

                // Obtiene la cantidad disponible anterior
                int cantidadDisponibleAnterior = productoDAO.obtenerCantidadDisponiblePorId(idProducto);

                // Calcula la nueva cantidad disponible (cantidad anterior - cantidad nueva)
                int nuevaCantidadDisponible = cantidadDisponibleAnterior - detalle.getCantidad();

                // Actualiza el producto en la base de datos con la nueva cantidad disponible y el nuevo proveedor
                productoDAO.actualizarProductoPorVenta(idProducto, nuevaCantidadDisponible);

                // Verifica si la nueva cantidad disponible es menor o igual a la cantidad mínima
                int cantidadMinima = detalle.getProducto().getCantidadMinima();
                if (nuevaCantidadDisponible <= cantidadMinima) {
                    // Almacena la información formateada
                    productosConCantidadMinima.add(String.format(
                            "%s: Solo quedan %d unidades. La cantidad mínima es %d. Reponer stock.",
                            detalle.getProducto().getNombre(), nuevaCantidadDisponible, cantidadMinima)
                    );
                }

                detalle.getFacturaVenta().setId_factura_ventas(idVentaGenerada);
                detalleVentaDAO.insertar(detalle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        vaciarCampos();
        btnFinalizarVenta.setDisable(true);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mspdevs/mspfxmaven/views/ModalFinalizarVenta.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.initStyle(StageStyle.UNDECORATED);
        newStage.initOwner(((Node)event.getSource()).getScene().getWindow() );
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.showAndWait();

        // Después de imprimir el comprobante, verifica si hay productos con cantidad mínima y muestra el mensaje
        if (!productosConCantidadMinima.isEmpty()) {
            mostrarMensajeProductosConCantidadMinima();
        }

        try {
            // Restablecer el cliente seleccionado como "Consumidor Final"
            clienteBox.getSelectionModel().select("Consumidor Final");

            // Llama a tu método para obtener el próximo número de factura y establecerlo en campoNumFactura
            int nuevoNumeroFactura = obtenerProximoNumeroFactura();
            campoNumFactura.setText(String.format("%012d", nuevoNumeroFactura));
            productoBox.getSelectionModel().clearSelection();
            campoCantidad.getValueFactory().setValue(1);
            campoCantidad.requestFocus();
        } catch (Exception e) {
            e.printStackTrace();
            // Manejo de errores si es necesario
        }
    }

    @FXML
    void accionNuevoClliente(ActionEvent event) throws IOException {
        clienteBox.getSelectionModel().select(null);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mspdevs/mspfxmaven/views/ModalNuevoCliente.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.initStyle(StageStyle.UNDECORATED);
        newStage.initOwner(((Node)event.getSource()).getScene().getWindow() );
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.showAndWait();

        /*
        // Verifica si hay un proveedor seleccionado antes de borrar los elementos
        if (!clienteBox.getSelectionModel().isEmpty()) {
            clienteBox.getSelectionModel().clearSelection();
        }*/
        // Luego de agregar el proveedor, actualiza el ComboBox
        actualizarComboBoxClientes();
    }

    private void mostrarMensajeProductosConCantidadMinima() {
        // Formatea los nombres de los productos con cantidad mínima y cantidad disponible
        String productos = String.join("\n", productosConCantidadMinima);
        msj.mostrarAlertaInforme("Advertencia. Poco Stock", "", productos);
    }

    public void vaciarCampos() {
        // Limpia los campos de entrada
        campoNumFactura.setText("");
        tipoFacturaBox.setValue("Seleccionar");
        clienteBox.getSelectionModel().select(null);
        campoSubtotal.setText("");
        campoIva.setText("");
        campoTotal.setText("");
        subtotalTotal = 0.0;
        ivaTotal = 0.0;
        totalPrecioLista = 0.0;
        // Limpia la TableView
        tblDetalle.getItems().clear();
    }

    private void habilitarCampos(boolean habilitar) {
        //campoCantidad.setDisable(!habilitar);
    }

    public void completarTablaProductos() {
        DetalleVentaDAOImpl detalleDAO = new DetalleVentaDAOImpl();
        ObservableList<DetalleVenta> detalles = null;

        try {
            // Recupera la lista de productos desde la base de datos.
            detalles = detalleDAO.listarTodos();
        } catch (Exception e) {
            e.printStackTrace();
            msj.mostrarError("Error", "", "Se ha producido un error recuperando los datos de la BD");
        }

        this.colCodBarra.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getProducto().getCodigoBarra()));
        this.colNom.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getProducto().getNombre()));
        this.colPU.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getProducto().getPrecioVenta()));
        this.colCantidad.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getCantidad()));
        this.colTotal.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getMonto()));
        //this.colTotal.setCellFactory(col -> new PrecioVentaCell());

        int lastIndex = tblDetalle.getItems().size() - 1;

        if (lastIndex >= 0) {
            // Desplázate hacia el último elemento
            tblDetalle.scrollTo(lastIndex);
        }
    }


    private void actualizarResumen() {
        // Inicializa variables para el subtotal, IVA y total
        subtotal = 0.0;
        iva = 0.0;
        total = 0.0;

        // Itera a través de todos los productos en la lista
        for (DetalleVenta detalle : listaDetalles) {
        	
            // Calcula el precio de lista sin IVA (monto total / 1.21)
        	double precioVentaSinIva = detalle.getMonto() / 1.21;

            // Calcula el IVA (precio total - precio de lista sin IVA)
            double ivaProducto = detalle.getMonto() - precioVentaSinIva;

            // Suma el subtotal y el IVA total
            subtotal += precioVentaSinIva;
            iva += ivaProducto;

            // Agrega el monto total del producto al campo "total"
            total += detalle.getMonto();
        }

        // Define el formato para dos decimales
        DecimalFormat formatoDosDecimales = new DecimalFormat("0.00");

        // Obtiene el tipo de factura seleccionado
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
        btnFinalizarVenta.setDisable(total == 0.0);
    }

    private void actualizarComboBoxClientes() {
        ClienteDAOImpl clienteDAO = new ClienteDAOImpl();
        // Obteniene la lista de clientes desde la base de datos
        List<Cliente> clientes = null;
        try {
            clientes = clienteDAO.listarTodos();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            // Limpia la lista de clientes
            clienteBox.getItems().clear();
        } catch (Exception ex) {
            // Manejar cualquier excepción que pueda ocurrir al limpiar
            ex.printStackTrace(); // O usa otro método de manejo de errores
        }

        for (Cliente cliente : clientes) {
            clienteBox.getItems().add(cliente.getRazonSocial());
            clienteBox.setValue("Consumidor Final");
        }
    }

    // Función para cargar todos los productos desde la base de datos
    private ObservableList<DetalleVenta> cargarDetalleDesdeBaseDeDatos() {
        DetalleVentaDAOImpl detalle = new DetalleVentaDAOImpl();
        ObservableList<DetalleVenta> detalles = null;
        try {
            detalles = detalle.listarTodos();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detalles;
    }

    // Método para obtener el próximo número de factura
    private int obtenerProximoNumeroFactura() {
        VentaDAOImpl ventaDAO = new VentaDAOImpl();
        try {
            String ultimoNumeroFactura = ventaDAO.obtenerUltimoNumeroFactura();

            if (ultimoNumeroFactura == null) {
                // Si no se encuentra ningún número de factura, establece "000000000001"
                return 1;
            } else {
                // Incrementa en 1 el último número de factura
                return Integer.parseInt(ultimoNumeroFactura) + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1; // Valor predeterminado si hay un error
    }

    // En algún método de inicialización, cuando se abre la ventana de ventas:
    public void setUsuario(int idEmpleado) {
        this.usuario = idEmpleado;
    }
    
    private ObservableList<Producto> cargarProductosDB() {
        ProductoDAOImpl productoDAO = new ProductoDAOImpl();
        ObservableList<Producto> productos = null;
        try {
            productos = productoDAO.listarTodos();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productos;
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
            lblCantDisponible.setText(Integer.toString(productoSeleccionado.getCantidadDisponible()));
            campoCantidad.getValueFactory().setValue(productoSeleccionado.getCantidadDisponible());
            // Asigna el ID del producto a la propiedad userData de un elemento apropiado
            btnAgregar.setUserData(productoSeleccionado.getIdProducto()); // Asigna el ID al botón Agregar
            return true; // Producto encontrado
        } else {
            msj.mostrarError("Error", "", "Producto no encontrado");
            return false; // Producto no encontrado
        }
    }

    // Variable para almacenar el valor seleccionado actualmente
    String selectedValue = null;



    private boolean mostrarDetallesProductoPorCodigoBarra(String nombreProducto) {
        // Llama a tu método de DAO para obtener los detalles del producto por su nombre
        ProductoDAOImpl productoDAO = new ProductoDAOImpl();
        Producto productoSeleccionado = null;

        try {
            productoSeleccionado = productoDAO.obtenerProductoPorCodigoBarra(nombreProducto);
        } catch (Exception e) {
            e.printStackTrace();
            // Manejo de errores si es necesario
        }

        if (productoSeleccionado != null) {
            // Rellena los campos con los detalles del producto
            lblCantDisponible.setText(Integer.toString(productoSeleccionado.getCantidadDisponible()));
            campoCantidad.getValueFactory().setValue(productoSeleccionado.getCantidadDisponible());
            // Asigna el ID del producto a la propiedad userData de un elemento apropiado
            btnAgregar.setUserData(productoSeleccionado.getIdProducto()); // Asigna el ID al botón Agregar
            return true; // Producto encontrado
        } else {
            msj.mostrarError("Error", "", "Producto no encontrado");
            return false; // Producto no encontrado
        }
    }

    // Método para ajustar el valor del Spinner según la cantidad disponible
    // Método para ajustar el valor del Spinner según la cantidad disponible
    // Método para ajustar el valor del Spinner según la cantidad disponible
    private void ajustarValorSpinner() {
        ProductoDAOImpl productoDAO = new ProductoDAOImpl();
        try {
            int nuevoValor = Integer.parseInt(campoCantidad.getEditor().getText());

            // Verifica si hay un producto seleccionado
            String productoSeleccionado = productoBox.getValue();
            if (productoSeleccionado != null) {
                // Obtén la cantidad disponible actual
                int cantidadDisponible = productoDAO.obtenerCantidadDisponiblePorNombreOCodigoBarra(productoSeleccionado);

                // Ajusta el valor del editor directamente a la cantidad disponible si es mayor
                if (nuevoValor > cantidadDisponible) {
                    campoCantidad.getEditor().setText(String.valueOf(cantidadDisponible));
                }
            }
        } catch (NumberFormatException e) {
            // Manejar la excepción si el valor ingresado no es un número válido (opcional)
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setearCaja(Caja cajaDePcpal) {
        this.caja = cajaDePcpal;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	
    	// ----- INICIO METODO PARA ELIMINAR ITEM SELECCIONADO DE TABLA ---	

        /*listaDetalles = tblDetalle.getItems();

        tblDetalle.setRowFactory(tv -> {
            TableRow<DetalleVenta> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    // Obtiene el producto de la fila seleccionada
                    DetalleVenta detalle = row.getItem();

                    // Crea un cuadro de diálogo de confirmación
                    boolean confirmado = msj.mostrarConfirmacion("Confirmación", "", "¿Está seguro de que desea quitar este producto de la tabla?");

                    if (confirmado) {
                        // Si el usuario confirma, elimina el producto de la lista observable y actualiza el TableView
                        listaDetalles.remove(detalle);

                        // Actualiza el resumen
                        actualizarResumen();
                    }
                }
            });
            return row;
        });*/

        listaDetalles = tblDetalle.getItems();
        colEliminar.setCellFactory(new Callback<TableColumn<DetalleVenta, Void>, TableCell<DetalleVenta, Void>>() {
            @Override
            public TableCell<DetalleVenta, Void> call(final TableColumn<DetalleVenta, Void> param) {
                return new TableCell<DetalleVenta, Void>() {
                    private final Button btnEliminar = new Button();
                    {
                        // Configura la imagen del botón
                        Image eliminarImage = new Image(getClass().getResourceAsStream("/com/mspdevs/mspfxmaven/imgs/eliminar.png"));
                        ImageView imageView = new ImageView(eliminarImage);
                        imageView.setFitHeight(24);
                        imageView.setFitWidth(24);
                        btnEliminar.setStyle("-fx-background-color: transparent;"); // Establece el fondo transparente
                        btnEliminar.setGraphic(imageView);

                        btnEliminar.setOnAction(event -> {
                            // Obtiene el producto de la fila seleccionada
                            DetalleVenta detalle = getTableView().getItems().get(getIndex());
                            // Crea un cuadro de diálogo de confirmación
                            boolean confirmado = msj.mostrarConfirmacion("Confirmación", "", "¿Está seguro de que desea quitar este producto de la tabla?");
                            if (confirmado) {
                                // Elimina el producto de la lista observable
                                listaDetalles.remove(detalle);
                                actualizarResumen();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btnEliminar);
                        }
                    }
                };
            }
        });
        // ----- FIN METODO PARA ELIMINAR ITEM SELECCIONADO DE TABLA ----
        
        // ----- INICIO METODO PARA COMPLETAR SEARCHABLECOMBOBOX CON PRODUCTOS DE INVENTARIO -----

        ObservableList<Producto> productosDB = cargarProductosDB();

        // Crea listas para códigos de barras y nombres de productos
        List<String> codigosBarras = new ArrayList<>();
        List<String> nombres = new ArrayList<>();

        for (Producto producto : productosDB) {
            codigosBarras.add(producto.getCodigoBarra());
            nombres.add(producto.getNombre());
        }

        productoBox.getItems().addAll(codigosBarras);
        productoBox.getItems().addAll(nombres);

        // Crea un listener que verifica los campos antes de habilitar el botón
        btnFinalizarVenta.setDisable(true);
        
        // ----- FIN METODO PARA COMPLETAR SEARCHABLECOMBOBOX CON PRODUCTOS DE INVENTARIO -----

        // ----- INICIO METODO FORMATEADOR PARA DATEPICKER -----
        
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
        
     // ----- FIN METODO FORMATEADOR PARA DATEPICKER -----
        
     // ----- INICIO METODO QUE NO SE QUE HACE ----

        tipoFacturaBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                actualizarResumen(); // Llama a la función para actualizar el resumen
            }
        });
        
     // ----- FIN METODO QUE NO SE QUE HACE ----
        
        // ---- INICIO LOGICA DE COMPLETAR TABLA -----

        completarTablaProductos();

        habilitarCampos(false);

        
        ProductoDAOImpl productoDAO = new ProductoDAOImpl();

        // ------ INICIO METODO PARA TRABAJAR CON CLIENTES ------
        
        // Obtiene la lista de clientes desde la base de datos
        ClienteDAOImpl clienteDAO = new ClienteDAOImpl();
        ObservableList<Cliente> clientes = null;
        try {
            clientes = clienteDAO.listarTodos();
        } catch (Exception e) {
            e.printStackTrace();;
        }

        // Agrega "Consumidor Final" a la lista de clientes si no existe
        Cliente consumidorFinal = null;
        for (Cliente cliente : clientes) {
            if (cliente.getRazonSocial().equals("Consumidor Final")) {
                consumidorFinal = cliente;
                break;
            }
        }

        // Crea una lista de nombres de clientes
        List<String> nombresClientes = new ArrayList<>();
        for (Cliente cliente : clientes) {
            nombresClientes.add(cliente.getRazonSocial());
        }

        // Agrega todos los nombres de clientes a la lista del ComboBox
        clienteBox.getItems().addAll(nombresClientes);

        // Agrega un ChangeListener al clienteBox
        clienteBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.equals("Consumidor Final")) {
                    // Si se selecciona "Consumidor Final", establecer el tipoFacturaBox en "B"
                    tipoFacturaBox.setValue("B");
                    // Deshabilitar la selección en tipoFacturaBox
                    tipoFacturaBox.setDisable(true);
                } else {
                    // Si se selecciona cualquier otro cliente, habilitar el tipoFacturaBox y permitir la selección
                    tipoFacturaBox.setDisable(false);
                }
            }
        });

        // Si "Consumidor Final" está en la lista de clientes, seleccionarlo por defecto
        if (consumidorFinal != null) {
            clienteBox.getSelectionModel().select(consumidorFinal.getRazonSocial());
            // Establecer "B" como el valor seleccionado en tipoFacturaBox
            tipoFacturaBox.setValue("B");
            // Deshabilitar la selección en tipoFacturaBox
            tipoFacturaBox.setDisable(true);
        }
        
        // ------ FIN METODO PARA TRABAJAR CON CLIENTES ------
        
        // ------ INICIO METODO PARA SETEAR FECHA A DIA ACTUAL ------

        // Obteniene la fecha actual
        Date fechaActual = new Date();
        // Formatea la fecha como una cadena (por ejemplo, "yyyy-MM-dd")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fechaComoCadena = dateFormat.format(fechaActual);
        // Establece la fecha actual en el campoFecha
        campoFecha.setValue(LocalDate.parse(fechaComoCadena));
        
       // ------ FIN METODO PARA SETEAR FECHA A DIA ACTUAL ------

        // Crea una lista de valores para el ComboBox
        tipoFacturaBox.getItems().addAll("A", "B", "C");
        
        
        // ----- INICIO METODO PARA TRABAJAR CON SPINNER CANTIDAD ------

        // Crea una SpinnerValueFactory para manejar los valores del Spinner
        //SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999, 1);

        // Asigna la SpinnerValueFactory al Spinner
        //campoCantidad.setValueFactory(valueFactory);

        // Establece el valor inicial en 0
        //campoCantidad.getValueFactory().setValue(1);

        campoCantidad.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) {
                campoCantidad.getValueFactory().setValue(1);
            }
        });

        campoNumFactura.setTextFormatter(ManejoDeEntrada.soloNumerosFactura());
        campoCantidad.getEditor().setTextFormatter(ManejoDeEntrada.soloCantidad());

        /*
        // Agrega un listener para escuchar los cambios en el valor del Spinner
        campoCantidad.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue > 999) {
                campoCantidad.getValueFactory().setValue(999);
            }
        });*/

        campoCantidad.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                return;  // Evita validar cuando el campo está vacío
            }

            try {
                int nuevoValor = Integer.parseInt(newValue);

                // Verifica si hay un producto seleccionado
                String productoSeleccionado = productoBox.getValue();
                if (productoSeleccionado != null) {
                    // Obtén la cantidad disponible actual
                    int cantidadDisponible = productoDAO.obtenerCantidadDisponiblePorNombreOCodigoBarra(productoSeleccionado);

                    // Ajusta el valor del editor directamente a la cantidad disponible si es mayor
                    if (nuevoValor > cantidadDisponible) {
                        campoCantidad.getEditor().setText(String.valueOf(cantidadDisponible));
                    }
                }
            } catch (NumberFormatException e) {
                // Manejar la excepción si el valor ingresado no es un número válido (opcional)
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });


        /*
        campoCantidad.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) { // Cuando pierde el foco
                try {
                    if (selectedValue != null) {
                        int cantidadIngresada = Integer.parseInt(campoCantidad.getEditor().getText());
                        int cantidadDisponible = productoDAO.obtenerCantidadDisponiblePorNombreOCodigoBarra(selectedValue);

                        if (cantidadIngresada > cantidadDisponible) {
                            campoCantidad.getValueFactory().setValue(cantidadDisponible);
                        } else if (cantidadIngresada < 1) {
                            campoCantidad.getValueFactory().setValue(1);
                        }
                    }
                } catch (NumberFormatException | SQLException e) {
                    // Manejar la excepción si el valor ingresado no es un número válido (opcional)
                    e.printStackTrace(); // o realiza el manejo de errores según tus necesidades
                }
            }
        });

        campoCantidad.valueProperty().addListener((obs, oldValue, newValue) -> {
            try {
                if (newValue != null && selectedValue != null) {
                    int cantidadDisponible = productoDAO.obtenerCantidadDisponiblePorNombreOCodigoBarra(selectedValue);

                    if (newValue > cantidadDisponible) {
                        campoCantidad.getValueFactory().setValue(cantidadDisponible);
                    } else if (newValue < 1) {
                        campoCantidad.getValueFactory().setValue(1);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });*/

        /*
        campoCantidad.getEditor().focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) { // Cuando se obtiene el enfoque
                Platform.runLater(() -> {
                    // Posiciona el cursor al final del texto
                    campoCantidad.getEditor().positionCaret(campoCantidad.getEditor().getText().length());
                });
            }
        });*/

        
       // ----- FIN METODO PARA TRABAJAR CON SPINNER CANTIDAD ------

        // Registra un manejador para el evento personalizado
        productoBox.addEventHandler(LIMPIAR_SELECCION_EVENT_TYPE, event -> limpiarSeleccionProductoBox());

        botonInvisible.setVisible(false);

        botonInvisible.setOnAction(event -> {
            productoBox.getSelectionModel().clearSelection();
        });



        /*
        // Agrega el ChangeListener a productoBox
        productoBox.valueProperty().addListener((observable, oldValue, selectedValue) -> {
            if (listenerHabilitado) {
                // Aquí coloca la lógica para cargar el producto seleccionado
                String selectedProduct = selectedValue;
                // Asegura de que selectedProduct no sea nulo y procesa el producto
                if (selectedProduct != null) {
                    System.out.println("Listener activado");
                    Producto productoSeleccionado = null;

                    // Verifica si el valor seleccionado corresponde a un código de barras o a un nombre
                    for (Producto producto : productosDB) {
                        if (selectedValue.equals(producto.getCodigoBarra()) || selectedValue.equals(producto.getNombre())) {
                            productoSeleccionado = producto;
                            break; // Producto encontrado, salir del bucle
                        }
                    }

                    if (productoSeleccionado != null) {
                        // Verifica si el producto ya existe en la tabla
                        boolean productoDuplicado = false;
                        for (DetalleVenta productoEnTabla : tblDetalle.getItems()) {
                            if (productoEnTabla.getProducto().getIdProducto() == productoSeleccionado.getIdProducto()) {
                                productoDuplicado = true;
                                break; // Producto ya existe, salir del bucle
                            }
                        }

                        if (!productoDuplicado) {
                        	
                        	DetalleVenta detalle = new DetalleVenta();
                        	detalle.getProducto().setIdProducto(productoSeleccionado.getIdProducto());
                        	detalle.getProducto().setCodigoBarra(productoSeleccionado.getCodigoBarra());
                        	detalle.getProducto().setNombre(productoSeleccionado.getNombre());
                        	detalle.getProducto().setPrecioVenta(productoSeleccionado.getPrecioVenta());
                        	detalle.setCantidad(campoCantidad.getValue());
                        	
                        	double calculoMonto = productoSeleccionado.getPrecioVenta() * campoCantidad.getValue();
                        	System.out.println(calculoMonto);
                        	
                        	detalle.setMonto(calculoMonto);
                        	
                        	tblDetalle.getItems().add(detalle);
                        	
                            // Reestablece la cantidad en campoCantidad
                            campoCantidad.getValueFactory().setValue(1);

                            // Actualiza otros valores, como el subtotal, el IVA y el total
                            actualizarResumen();


                        } else {
                            // Muestra un mensaje de error o advertencia indicando que el producto ya se agregó.
                            msj.mostrarError("Error", "", "Este producto ya ha sido agregado a la tabla.");
                        }
                    } else {
                        msj.mostrarError("Error", "", "Producto no encontrado");
                    }
                }

                // Deshabilita temporalmente el listener para evitar ejecuciones adicionales
                listenerHabilitado = false;

                // Reestablece la habilitación del listener después de un breve período (puedes ajustar este tiempo)
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), e -> {
                    listenerHabilitado = true;
                    productoBox.getSelectionModel().clearSelection();
                }));
                timeline.setCycleCount(1);
                timeline.play();
            }
            // Dispara el evento personalizado para limpiar la selección en productoBox
            //Event event = new Event(productoBox, null, VentanaVentasAlternativaController.LIMPIAR_SELECCION_EVENT_TYPE);
            //productoBox.fireEvent(event);
        });*/

        // Deshabilita el botón al inicio
        btnAgregar.setDisable(true);

        productoBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (listenerHabilitado) {
                if (newValue != null) {
                    System.out.println("Listener activado");

                    // Deshabilita temporalmente el listener para evitar ejecuciones adicionales
                    listenerHabilitado = false;

                    // Obtén la cantidad disponible del producto seleccionado por nombre o código de barras
                    try {
                        int cantidadDisponible = productoDAO.obtenerCantidadDisponiblePorNombreOCodigoBarra(newValue);

                        // Verifica si la cantidad disponible es mayor que cero
                        if (cantidadDisponible <= 0) {
                            // Muestra un mensaje de advertencia
                            msj.mostrarAlertaInforme("Advertencia", "", "Producto sin stock. Seleccione otro producto.");

                            // Vacía el productoBox y establece el foco
                            productoBox.setValue("");
                            productoBox.requestFocus();

                            // Limpia las otras selecciones
                            //campoCantidad.getValueFactory().setValue(1);
                            lblCantDisponible.setText("Sin Stock");

                            // Deshabilita el botón si la cantidad disponible es menor o igual a 0
                            btnAgregar.setDisable(true);
                        } else {
                            // Ajusta el valor máximo del Spinner a la cantidad disponible
                            SpinnerValueFactory<Integer> valueFactory =
                                    new SpinnerValueFactory.IntegerSpinnerValueFactory(1, cantidadDisponible, 1);
                            campoCantidad.setValueFactory(valueFactory);

                            // Actualiza el valor del Spinner y el Label
                            campoCantidad.getValueFactory().setValue(1); // Establece el valor inicial en 1
                            lblCantDisponible.setText("CANTIDAD DISPONIBLE: " + cantidadDisponible);

                            // Habilita el botón cuando se selecciona un producto y la cantidad es mayor o igual a 1
                            btnAgregar.setDisable(false);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace(); // Manejo de errores específico según tus necesidades
                    } finally {
                        // Reestablece la habilitación del listener después de un breve período
                        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), e -> {
                            listenerHabilitado = true;
                        }));
                        timeline.setCycleCount(1);
                        timeline.play();
                    }
                } else {
                    // Si no hay un producto seleccionado, deshabilita el botón
                    btnAgregar.setDisable(true);
                }
            }
        });

        // Agrega un listener al evento ACTION del editor del Spinner para validar y ajustar el valor
        campoCantidad.getEditor().setOnAction(event -> ajustarValorSpinner());

        // Llama al método para obtener el último número de factura
        VentaDAOImpl ventaDAO = new VentaDAOImpl();
        try {
            String ultimoNumeroFactura = ventaDAO.obtenerUltimoNumeroFactura();

            if (ultimoNumeroFactura == null) {
                // Si no se encuentra ningún número de factura, establece "000000000001"
                campoNumFactura.setText("000000000001");
            } else {
                // Incrementa en 1 el último número de factura
                int nuevoNumeroFactura = Integer.parseInt(ultimoNumeroFactura) + 1;

                // Establece el nuevo número de factura en el campo "campoNumFactura"
                campoNumFactura.setText(String.format("%012d", nuevoNumeroFactura));
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Manejo de errores
        }
    }
    
}