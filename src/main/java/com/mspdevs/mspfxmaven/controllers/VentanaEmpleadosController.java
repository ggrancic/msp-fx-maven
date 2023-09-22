package com.mspdevs.mspfxmaven.controllers;

import com.mspdevs.mspfxmaven.model.DAO.EmpleadoDAOImpl;
import com.mspdevs.mspfxmaven.model.Empleado;
import com.mspdevs.mspfxmaven.utils.Alerta;
import com.mspdevs.mspfxmaven.utils.VentanaCambiarContraseña;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;


public class VentanaEmpleadosController implements Initializable {

    Alerta msj = new Alerta();
    
    // Declarar una lista de respaldo para todos los empleados originales
    private ObservableList<Empleado> todosLosEmpleados;
    
    @FXML
    private TextField campoApellido;

    @FXML
    private TextField campoCalle;

    @FXML
    private TextField campoClave;

    @FXML
    private TextField campoDNI;

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

    @FXML
    private ComboBox<String> comboAdmin;
    
    @FXML
    private Button btnClave;
    
    @FXML
    private TextField campoBusqueda;

    @FXML
    void accionBotonAgregar(ActionEvent event) {
        String nombreIngresado = this.campoNombre.getText();
        String apellidoIngresado = this.campoApellido.getText();
        String calleIngresada = this.campoCalle.getText();
        String telefonoIngresado = this.campoTelefono.getText();
        String provinciaIngresada = this.campoProvincia.getText();
        String localidadIngresada = this.campoLocalidad.getText();
        String emailIngresado = this.campoEmail.getText();
        String rolIngresado = this.convertirValorCombo(comboAdmin.getValue());
        String dniIngresado = this.campoDNI.getText();

        if (nombreIngresado.isEmpty() || apellidoIngresado.isEmpty() || calleIngresada.isEmpty()
        || telefonoIngresado.isEmpty() || provinciaIngresada.isEmpty() || localidadIngresada.isEmpty()
        || emailIngresado.isEmpty() || dniIngresado.isEmpty() || rolIngresado == null) {
            msj.mostrarError("Error", "", "Debe completar todos los campos");
        } else {
            Empleado em = new Empleado();
            EmpleadoDAOImpl dao = new EmpleadoDAOImpl();

            em.setNombre_usuario(dniIngresado);
            em.setNombre(nombreIngresado);
            em.setApellido(apellidoIngresado);
            em.setCalle(calleIngresada);
            em.setTelefono(telefonoIngresado);
            em.setProvincia(provinciaIngresada);
            em.setLocalidad(localidadIngresada);
            em.setClave(dniIngresado);
            em.setMail(emailIngresado);
            em.setEsAdmin(rolIngresado);
            em.setDni(dniIngresado);

            try {
                dao.insertar(em);
                msj.mostrarAlertaInforme("Operación exitosa", "", "Se ha agregado el empleado correctamente.");
                completarTabla();
            } catch (Exception e) {
                msj.mostrarError("Error", "", "No se pudo agrega el empleado en la BD");
            }
        }
    }

    @FXML
    void accionBtnEliminar(ActionEvent event) {
        Empleado em = this.tblEmpleados.getSelectionModel().getSelectedItem();
        if (em == null) {
            msj.mostrarError("Error", "", "Debe seleccionar un elemento de la lista");
        } else {
            try {
                EmpleadoDAOImpl dao = new EmpleadoDAOImpl();
                dao.eliminar(em);
                completarTabla();
                msj.mostrarAlertaInforme("Operacion exitosa", "", "El empleado se ha eliminado");
            } catch (Exception e) {
                msj.mostrarError("Error", "", "No se pudo eliminar el elemento de la BD");
            }
        }
    }
    
    @FXML
    void accionBtnModificar(ActionEvent event) {
        // Obtiene el rubro seleccionado en la tabla
        Empleado empl = this.tblEmpleados.getSelectionModel().getSelectedItem();
        
        if (empl == null) {
            // Muestra un mensaje de error si no se selecciona ningún elemento en la tabla
            msj.mostrarError("Error", "", "Debe seleccionar un empleado de la lista para modificar.");
        } else {
            String nombreIngresado = this.campoNombre.getText();
            String apellidoIngresado = this.campoApellido.getText();
            String calleIngresada = this.campoCalle.getText();
            String telefonoIngresado = this.campoTelefono.getText();
            String provinciaIngresada = this.campoProvincia.getText();
            String localidadIngresada = this.campoLocalidad.getText();
            String emailIngresado = this.campoEmail.getText();
            String rolIngresado = this.convertirValorCombo(comboAdmin.getValue());
            String dniIngresado = this.campoDNI.getText();

            if (nombreIngresado.isEmpty() || apellidoIngresado.isEmpty() || calleIngresada.isEmpty()
            || telefonoIngresado.isEmpty() || provinciaIngresada.isEmpty() || localidadIngresada.isEmpty()
            || emailIngresado.isEmpty() || dniIngresado.isEmpty() || rolIngresado == null) {
                msj.mostrarError("Error", "", "Debe completar todos los campos");
            } else {
                empl.setNombre_usuario(dniIngresado);
                empl.setNombre(nombreIngresado);
                empl.setApellido(apellidoIngresado);
                empl.setCalle(calleIngresada);
                empl.setTelefono(telefonoIngresado);
                empl.setProvincia(provinciaIngresada);
                empl.setLocalidad(localidadIngresada);
                empl.setMail(emailIngresado);
                empl.setEsAdmin(rolIngresado);
                empl.setDni(dniIngresado);

                try {
                    EmpleadoDAOImpl dao = new EmpleadoDAOImpl();
                    dao.modificar(empl);
                    completarTabla();
                    vaciarCampos();
                    msj.mostrarAlertaInforme("Operación exitosa", "", "El empleado se ha modificado");
                } catch (Exception e) {
                    msj.mostrarError("Error", "", "No se pudo modificar el elemento en la BD");
                }
                }     
        }
         
    }

    @FXML
    void accionCambiarClave(ActionEvent event) {
        // Obtiene el rubro seleccionado en la tabla
        Empleado em = this.tblEmpleados.getSelectionModel().getSelectedItem();
        
        if (em == null) {
            // Muestra un mensaje de error si no se selecciona ningún elemento en la tabla
            msj.mostrarError("Error", "", "Debe seleccionar un empleado de la lista para cambiar la clave.");
        } else {
            VentanaCambiarContraseña vc = new VentanaCambiarContraseña();
            vc.showAndWait();
            em.setClave(vc.getNuevaPw());
        }
        
    }
    
    @FXML
    void accionBtnLimpiar(ActionEvent event) {
        vaciarCampos();
        tblEmpleados.getSelectionModel().clearSelection();
    }
    
    @FXML
    void filtrarEmpleados(KeyEvent event) {
        // Obtener el texto ingresado en el campo de búsqueda
        String filtro = campoBusqueda.getText().toLowerCase();

        if (filtro.isEmpty()) {
            // Si el campo de búsqueda está vacío, mostrar todos los empleados originales
            tblEmpleados.setItems(todosLosEmpleados);
        } else {
            // Filtrar la lista de todos los empleados originales y mostrar los resultados
            ObservableList<Empleado> empleadosFiltrados = todosLosEmpleados.filtered(empleado
                    -> empleado.getNombre().toLowerCase().startsWith(filtro)
            );
            tblEmpleados.setItems(empleadosFiltrados);
        }
    }
    
    
    void vaciarCampos() {
        campoNombre.clear();
        campoApellido.clear();
        campoCalle.clear();
        campoLocalidad.clear();
        campoProvincia.clear();
        campoTelefono.clear();
        campoEmail.clear();
        campoDNI.clear();
        comboAdmin.setValue(null);
        comboAdmin.setPromptText("Seleccionar");
    }

    public void completarTabla() {
        EmpleadoDAOImpl emple = new EmpleadoDAOImpl();
        ObservableList<Empleado> empleados = null;

        try {
            empleados = emple.listarTodos();
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
        
        // Configura un listener para la selección de fila en la tabla
        tblEmpleados.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Llena los campos de entrada con los datos del proveedor seleccionado
                campoNombre.setText(newValue.getNombre());
                campoApellido.setText(newValue.getApellido());
                campoProvincia.setText(newValue.getProvincia());
                campoLocalidad.setText(newValue.getLocalidad());
                campoCalle.setText(newValue.getCalle());
                campoEmail.setText(newValue.getMail());
                campoTelefono.setText(newValue.getTelefono());
                campoDNI.setText(newValue.getDni());
                comboAdmin.setValue(reconvertirValorCombo(newValue.getEsAdmin()));
                }
        });
    }
    
    
    public String convertirValorCombo (String valor) {
        String nuevoValor = "";
        if (valor == "Si") {
            nuevoValor = "S";
        } else {
            nuevoValor = "N";
        }
        return nuevoValor;
    }
    
    public String reconvertirValorCombo (String valor) {
        String nuevoValor = "";
        if (valor.equals("S")) {
            nuevoValor = "Si";
        } else {
            nuevoValor = "No";
        }
        
        return nuevoValor;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        completarTabla();
        ObservableList<String> itemsComboAdmin = FXCollections.observableArrayList("Si","No");
        this.comboAdmin.setItems(itemsComboAdmin);
        todosLosEmpleados = tblEmpleados.getItems();
    }
}
