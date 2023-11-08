package com.mspdevs.mspfxmaven.controllers;

import com.mspdevs.mspfxmaven.model.DAO.ProductoDAOImpl;
import com.mspdevs.mspfxmaven.model.DAO.ProveedorDAOImpl;
import com.mspdevs.mspfxmaven.model.DAO.RubroDAOImpl;
import com.mspdevs.mspfxmaven.model.Producto;
import com.mspdevs.mspfxmaven.model.Rubro;
import com.mspdevs.mspfxmaven.utils.Alerta;
import com.mspdevs.mspfxmaven.utils.FormatoTexto;
import com.mspdevs.mspfxmaven.utils.ManejoDeEntrada;
import com.mspdevs.mspfxmaven.utils.ValidacionDeEntrada;
import javafx.application.Platform;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.SearchableComboBox;

public class ModalNuevoProductoController implements Initializable {
    Alerta msj = new Alerta();
    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnCancelar;

    @FXML
    private Spinner<Integer> campoCantMin;

    @FXML
    private TextField campoCodigo;

    @FXML
    private TextField campoNombre;

    @FXML
    private SearchableComboBox<String> rubroBox;
    // Agregar una variable para almacenar el nombre del proveedor
    private String proveedorSeleccionado;

    private boolean seCreoNuevoProducto = false;
    private Producto nuevoProducto;
    private String nuevoNombreProducto; // Guarda el nombre del nuevo producto

    @FXML
    void accionBotonAgregar(ActionEvent event) throws Exception {
        String nombreIngresado = FormatoTexto.formatearTexto(this.campoNombre.getText());
        String codigoBarraIngresado = this.campoCodigo.getText();
        String cantMinimaTexto = String.valueOf(campoCantMin.getValue());
        String RubroNombreSeleccionado = rubroBox.getSelectionModel().getSelectedItem();
        String ProveedorNombreSeleccionado = proveedorSeleccionado;

        ProveedorDAOImpl proveedorDAO = new ProveedorDAOImpl();
        RubroDAOImpl rubroDAO = new RubroDAOImpl();

        // Verifica si algún campo de texto está vacío
        if (nombreIngresado.isEmpty() || codigoBarraIngresado.isEmpty() ||
                cantMinimaTexto.isEmpty()) {
            // Mostrar mensaje de error si falta ingresar datos
            msj.mostrarError("Error", "", "Falta ingresar datos.");
        } else {

            if (ValidacionDeEntrada.validarCodigoDeBarra(codigoBarraIngresado) &&
                    ValidacionDeEntrada.validarPrecioVenta(cantMinimaTexto) &&
                    ValidacionDeEntrada.validarSeleccionComboBox(rubroBox, "Debe seleccionar un rubro.")) {
                // Verificar valores numéricos
                Integer cantMinimaIngresado = Integer.valueOf(cantMinimaTexto);
                // Realizar la operación si todas las validaciones son exitosas
                int RubroId = rubroDAO.obtenerPorNombre(RubroNombreSeleccionado).getIdRubro();
                int ProveedorId = proveedorDAO.obtenerPorNombre(ProveedorNombreSeleccionado);

                Producto pro = new Producto();
                ProductoDAOImpl dao = new ProductoDAOImpl();

                pro.setNombre(nombreIngresado);
                pro.setCodigoBarra(codigoBarraIngresado);
                pro.setCantidadMinima(cantMinimaIngresado);
                pro.setIdRubroFK(RubroId);
                pro.setIdProveedorFK(ProveedorId);

                try {
                    dao.insertar(pro);
                    msj.mostrarAlertaInforme("Operación exitosa", "", "Se ha agregado el producto correctamente");
                    seCreoNuevoProducto = true;
                    nuevoNombreProducto = nombreIngresado; // Guarda el nombre del nuevo producto
                    cerrarVentanaModal(event);
                } catch (Exception e) {
                    msj.mostrarError("Error", "", "No se pudo agregar el producto en la BD");
                    e.printStackTrace();
                }
            }
        }

    }

    public boolean seCreoNuevoProducto() {
        return seCreoNuevoProducto;
    }

    public String getNuevoNombreProducto() {
        return nuevoNombreProducto;
    }


    @FXML
    void accionBtnCancelar(ActionEvent event) {
        // Crea un cuadro de diálogo de confirmación
        boolean confirmado = msj.mostrarConfirmacion("Confirmación", "",
                "¿Está seguro de que no quiere agregar un nuevo producto?");
        if (confirmado) {
            cerrarVentanaModal(event);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Establecer el enfoque en campoNombre después de que la ventana se haya mostrado completamente
        Platform.runLater(() -> campoCodigo.requestFocus());

        // Aplica el TextFormatter a los campos
        campoNombre.setTextFormatter(ManejoDeEntrada.soloLetrasNumEspAcento());
        campoCodigo.setTextFormatter(ManejoDeEntrada.soloCodigoBarras());
        campoCantMin.getEditor().setTextFormatter(ManejoDeEntrada.soloCantidad());

        // Crea una SpinnerValueFactory para manejar los valores del Spinner
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999, 1);

        // Asigna la SpinnerValueFactory al Spinner
        campoCantMin.setValueFactory(valueFactory);

        // Agrega un listener para escuchar los cambios en el valor del Spinner
        campoCantMin.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue > 999) {
                campoCantMin.getValueFactory().setValue(999);
            }
        });

        // Establece el valor inicial en 0
        campoCantMin.getValueFactory().setValue(1);

        campoCantMin.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) {
                campoCantMin.getValueFactory().setValue(1);
            }
        });


        RubroDAOImpl rubroDAO = new RubroDAOImpl();
        // Obteniene la lista de rubros desde la base de datos
        List<Rubro> rubros = null;
        try {
            rubros = rubroDAO.listarTodos();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Cargar los nombres en los ComboBox
        for (Rubro rubro : rubros) {
            rubroBox.getItems().add(rubro.getNombre());
        }

        // Establece el nombre del proveedor en el campo de texto
        //campoNombre.setText(nombreProveedor);
    }

    private void cerrarVentanaModal(ActionEvent event) {
        // Obtén la referencia a la ventana actual
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        // Cierra la ventana
        stage.close();
    }

    public void obtenerProveedor(String proveedorProducto) {
        proveedorSeleccionado = proveedorProducto;
    }
}
