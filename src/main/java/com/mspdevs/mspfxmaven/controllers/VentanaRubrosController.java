package com.mspdevs.mspfxmaven.controllers;

import com.mspdevs.mspfxmaven.model.DAO.RubroDAOImpl;
import com.mspdevs.mspfxmaven.model.Rubro;
import com.mspdevs.mspfxmaven.utils.Alerta;
import com.mspdevs.mspfxmaven.utils.FormatoTexto;
import com.mspdevs.mspfxmaven.utils.ManejoDeBotones;
import com.mspdevs.mspfxmaven.utils.ManejoDeEntrada;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.input.KeyEvent;


public class VentanaRubrosController implements Initializable {
    Alerta msj = new Alerta();

    // Declarar una lista de respaldo para todos los empleados originales
    private ObservableList<Rubro> todosLosRubros;

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
    private TextField campoNombre;

    @FXML
    private TableView<Rubro> tablaRubros;

    @FXML
    private TableColumn<Rubro, Integer> colId;

    @FXML
    private TableColumn<Rubro, String> colNom;

    @FXML
    void agregarRubro(ActionEvent event) {
        String nombreIngresado = FormatoTexto.formatearTexto(this.campoNombre.getText());
        if (nombreIngresado.isEmpty()) {
            msj.mostrarError("Error", "", "Debe ingresar el nombre del rubro");
        } else {
            Rubro r = new Rubro();
            RubroDAOImpl dao = new RubroDAOImpl();
            r.setNombre(nombreIngresado);
            try {
                dao.insertar(r);
                completarTabla();
                vaciarCampos();
                campoNombre.requestFocus();
                msj.mostrarAlertaInforme("Operación exitosa", "", "Se ha agregado el rubro correctamente.");
            } catch (Exception e) {
                msj.mostrarError("Error", "", "El rubro ya existe.");
            }
        }
    }

    @FXML
    void accionBotonEliminar(ActionEvent event) {
        Rubro r = this.tablaRubros.getSelectionModel().getSelectedItem();
        if (r == null) {
            msj.mostrarError("Error", "", "Debe seleccionar un elemento de la lista");
        } else {
            boolean confirmacion = msj.mostrarConfirmacion("Confirmar Eliminación", "",
                    "¿Está seguro de que desea eliminar este rubro?");
            if (confirmacion) { // Si se confirma la eliminación
                try {
                    RubroDAOImpl dao = new RubroDAOImpl();
                    dao.eliminar(r);
                    completarTabla();
                    vaciarCampos();
                    campoNombre.requestFocus();
                    manejador.configurarBotones(false);
                    msj.mostrarAlertaInforme("Operacion exitosa", "", "El rubro se ha eliminado");
                } catch (Exception e) {
                    msj.mostrarError("Error", "", "No se pudo eliminar el rubro");
                }
            }
        }
    }
    
     @FXML
    void accionBtnModificar(ActionEvent event) {
        // Obtiene el rubro seleccionado en la tabla
        Rubro r = this.tablaRubros.getSelectionModel().getSelectedItem();
        
        if (r == null) {
            // Muestra un mensaje de error si no se selecciona ningún elemento en la tabla
            msj.mostrarError("Error", "", "Debe seleccionar un rubro de la lista para modificar.");
            return;
        }

         String nombreIngresado = FormatoTexto.formatearTexto(this.campoNombre.getText());
        
        if (nombreIngresado.isEmpty()) {
            msj.mostrarError("Error", "", "Debe ingresar el nombre del rubro.");
            return;
        }
        
        r.setNombre(nombreIngresado);
         boolean confirmacion = msj.mostrarConfirmacion("Confirmar Modificación", "",
                 "¿Está seguro de que desea modificar este rubro?");
         if (confirmacion) { // Si se confirma la eliminación

             try {
                 RubroDAOImpl dao = new RubroDAOImpl();
                 dao.modificar(r);
                 completarTabla();
                 vaciarCampos();
                 campoNombre.requestFocus();
                 manejador.configurarBotones(false);
                 msj.mostrarAlertaInforme("Operación exitosa", "", "El rubro se ha modificado");
             } catch (Exception e) {
                 msj.mostrarError("Error", "", "No se pudo modificar el rubro");
             }
         }
    }

    @FXML
    void accionBtnLimpiar(ActionEvent event) {
        vaciarCampos();
        manejador.configurarBotones(false);
        tablaRubros.getSelectionModel().clearSelection();
        campoNombre.requestFocus();
    }

    @FXML
    void filtrarRubros(KeyEvent event) {
        // Obtener el texto ingresado en el campo de búsqueda
        String filtro = campoBuscar.getText().toLowerCase();

        if (filtro.isEmpty()) {
            // Si el campo de búsqueda está vacío, mostrar todos los empleados originales
            tablaRubros.setItems(todosLosRubros);
        } else {
            // Filtrar la lista de todos los empleados originales y mostrar los resultados
            ObservableList<Rubro> rubrosFiltrados = todosLosRubros.filtered(empleado
                    -> empleado.getNombre().toLowerCase().startsWith(filtro)
            );
            tablaRubros.setItems(rubrosFiltrados);
        }
    }

    public void completarTabla() {
        RubroDAOImpl rubro = new RubroDAOImpl();
        ObservableList<Rubro> rubros = null;

        try {
            rubros = rubro.listarTodos();
        } catch (Exception e) {
            msj.mostrarError("Error", "", "Se ha producido un error recuperando los datos de la BD");
        }

        this.colId.setCellValueFactory(new PropertyValueFactory<>("idRubro"));
        this.colNom.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.tablaRubros.setItems(rubros);
        
        tablaRubros.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                manejador.configurarBotones(true);
                // Llena los campos de entrada con los datos del proveedor seleccionado
                campoNombre.setText(newValue.getNombre());
            }
        });
    }
    
    public void vaciarCampos() {
        campoNombre.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        double colIdCustomWidth = tablaRubros.getWidth() * 0.01;
        colId.setMinWidth(colIdCustomWidth);

        double colNomCustomWidth = tablaRubros.getWidth() * 0.45;
        colNom.setMinWidth(colNomCustomWidth);

        completarTabla();

        // Establecer el enfoque en campoNombre después de que la ventana se haya mostrado completamente
        Platform.runLater(() -> campoNombre.requestFocus());

        todosLosRubros = tablaRubros.getItems();

        // Instancia el ManejadorBotones en la inicialización del controlador
        manejador = new ManejoDeBotones(btnModificar, btnEliminar, btnAgregar);
        // Para deshabilitar "Modificar" y "Eliminar" y habilitar "Agregar"
        manejador.configurarBotones(false);

        // Asigna el TextFormatter al campoNombre
        campoNombre.setTextFormatter(ManejoDeEntrada.soloLetrasEspacioAcento());
    }
}