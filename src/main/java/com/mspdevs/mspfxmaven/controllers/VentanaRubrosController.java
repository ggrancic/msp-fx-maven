package com.mspdevs.mspfxmaven.controllers;

import com.mspdevs.mspfxmaven.model.DAO.RubroDAOImpl;
import com.mspdevs.mspfxmaven.model.Rubro;
import com.mspdevs.mspfxmaven.utils.Alerta;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class VentanaRubrosController implements Initializable {

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
        Stage myCurrentStage = (Stage) tablaRubros.getScene().getWindow();
        if (nombreIngresado.isEmpty()) {
            msj.mostrarError("Error", "", "Debe ingresar el nombre del rubro");
        } else {
            Rubro r = new Rubro();
            RubroDAOImpl dao = new RubroDAOImpl();
            r.setNombre(nombreIngresado);
            try {
                dao.insertar(r);
                completarTabla();
                campoNombre.clear();
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
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        double colIdCustomWidth = tablaRubros.getWidth() * 0.01;
        colId.setMinWidth(colIdCustomWidth);

        double colNomCustomWidth = tablaRubros.getWidth() * 0.7;
        colNom.setMinWidth(colNomCustomWidth);

        completarTabla();

    }
}
