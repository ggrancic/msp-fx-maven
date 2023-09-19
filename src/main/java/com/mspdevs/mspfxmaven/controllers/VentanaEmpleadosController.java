package com.mspdevs.mspfxmaven.controllers;

import com.mspdevs.mspfxmaven.model.DAO.EmpleadoDAOImpl;
import com.mspdevs.mspfxmaven.model.Empleado;
import com.mspdevs.mspfxmaven.utils.Alerta;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.Initializable;

public class VentanaEmpleadosController implements Initializable {

    Alerta msj = new Alerta();
    @FXML
    private TextField campoApellido;

    @FXML
    private TextField campoCalle;

    @FXML
    private TextField campoClave;

    @FXML
    private TextField campoEmail;

    @FXML
    private TextField campoLocalidad;

    @FXML
    private TextField campoNombre;

    @FXML
    private TextField campoProvincia;

    @FXML
    private TextField campoTelefono;

    @FXML
    private TableView<Empleado> tblEmpleados;

    @FXML
    private TableColumn<?, ?> colApe;

    @FXML
    private TableColumn<?, ?> colCal;

    @FXML
    private TableColumn<?, ?> colCl;

    @FXML
    private TableColumn<?, ?> colDNI;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colLoc;

    @FXML
    private TableColumn<?, ?> colNom;

    @FXML
    private TableColumn<?, ?> colProv;

    @FXML
    private TableColumn<?, ?> colTel;

    public void completarTabla() {
        EmpleadoDAOImpl em = new EmpleadoDAOImpl();
        ObservableList<Empleado> empleados = null;

        try {
            empleados = em.listarTodos();
        } catch (Exception e) {
            msj.mostrarError("Error", "", "Se ha producido un error recuperando los datos de la BD");
        }

        this.colId.setCellValueFactory(new PropertyValueFactory<>("id_empleado"));
        this.colNom.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.colApe.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        this.colProv.setCellValueFactory(new PropertyValueFactory<>("provincia"));
        this.colLoc.setCellValueFactory(new PropertyValueFactory<>("localidad"));
        this.colCal.setCellValueFactory(new PropertyValueFactory<>("calle"));
        this.colDNI.setCellValueFactory(new PropertyValueFactory<>("dni"));
        this.colEmail.setCellValueFactory(new PropertyValueFactory<>("mail"));
        this.colTel.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        this.colCl.setCellValueFactory(new PropertyValueFactory<>("clave"));
        this.tblEmpleados.setItems(empleados);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        completarTabla();

    }
}
