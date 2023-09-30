package com.mspdevs.mspfxmaven.controllers;

import com.mspdevs.mspfxmaven.model.DAO.RubroDAO;
import com.mspdevs.mspfxmaven.model.DAO.RubroDAOImpl;
import com.mspdevs.mspfxmaven.model.Rubro;
import com.mspdevs.mspfxmaven.utils.Alerta;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class VentanaRubrosController implements Initializable {
    Alerta msj = new Alerta();

    // Declarar una lista de respaldo para todos los empleados originales
    private ObservableList<Rubro> todosLosRubros;

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
        String nombreIngresado = this.campoNombre.getText();
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
                msj.mostrarAlertaInforme("Operación exitosa", "", "Se ha agregado el rubro correctamente.");
            } catch (Exception e) {
                msj.mostrarError("Error", "", "No se pudo agrega el rubro en la BD");
            }
        }
    }

    @FXML
    void accionBotonEliminar(ActionEvent event) {
        Rubro r = this.tablaRubros.getSelectionModel().getSelectedItem();
        if (r == null) {
            msj.mostrarError("Error", "", "Debe seleccionar un elemento de la lista");
        } else {
            try {
                RubroDAOImpl dao = new RubroDAOImpl();
                dao.eliminar(r);
                completarTabla();
                msj.mostrarAlertaInforme("Operacion exitosa", "", "El rubro se ha eliminado");
            } catch (Exception e) {
                msj.mostrarError("Error", "", "No se pudo eliminar el elemento de la BD");
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

        String nombreIngresado = this.campoNombre.getText();

        if (nombreIngresado.isEmpty()) {
            msj.mostrarError("Error", "", "Debe ingresar el nombre del rubro.");
            return;
        }

        r.setNombre(nombreIngresado);


        try {
            RubroDAOImpl dao = new RubroDAOImpl();
            dao.modificar(r);
            completarTabla();
            vaciarCampos();
            msj.mostrarAlertaInforme("Operación exitosa", "", "El rubro se ha modificado");
        } catch (Exception e) {
            msj.mostrarError("Error", "", "No se pudo modificar el elemento en la BD");
        }
    }

    @FXML
    void accionBtnLimpiar(ActionEvent event) {
        vaciarCampos();
        tablaRubros.getSelectionModel().clearSelection();
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

        double colNomCustomWidth = tablaRubros.getWidth() * 0.7;
        colNom.setMinWidth(colNomCustomWidth);

        completarTabla();

        todosLosRubros = tablaRubros.getItems();
    }

}

