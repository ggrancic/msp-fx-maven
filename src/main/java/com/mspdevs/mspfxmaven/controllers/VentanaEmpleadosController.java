package com.mspdevs.mspfxmaven.controllers;

import com.mspdevs.mspfxmaven.model.Cliente;
import com.mspdevs.mspfxmaven.model.DAO.EmpleadoDAOImpl;
import com.mspdevs.mspfxmaven.model.DAO.ProveedorDAOImpl;
import com.mspdevs.mspfxmaven.model.Empleado;
import com.mspdevs.mspfxmaven.model.Proveedor;
import com.mspdevs.mspfxmaven.utils.*;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import org.controlsfx.control.SearchableComboBox;

public class VentanaEmpleadosController implements Initializable {
    Alerta msj = new Alerta();

    // Declarar una lista de respaldo para todos los empleados originales
    private ObservableList<Empleado> todosLosEmpleados;

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
    private SearchableComboBox<String> comboProvincia;

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
        // Obtiene los valores de los campos en un objeto Empleado
        Empleado empleado = obtenerValoresDeCampos();
        
        boolean clienteRepetido = false;

        // Verifica si algún campo de texto está vacío
        if (empleado.getNombre().isEmpty() || empleado.getApellido().isEmpty() || empleado.getProvincia().isEmpty() ||
                empleado.getLocalidad().isEmpty() || empleado.getCalle().isEmpty() ||
                empleado.getMail().isEmpty() || empleado.getTelefono().isEmpty()) {
            // Muestra un mensaje de error si falta ingresar datos
            msj.mostrarError("Error", "", "Falta ingresar datos.");
        } else {
        	
        	
        	for (Empleado empleadoEnTabla : tblEmpleados.getItems()) {
        		if (empleadoEnTabla.getDni().equals(empleado.getDni())) {
        			clienteRepetido = true;
        		}
        	}
        	
        	if (!clienteRepetido) {
        		if (ValidacionDeEntrada.validarEmail(empleado.getMail())  && ValidacionDeEntrada.validarSeleccionComboBox(comboAdmin, "Debe indicar si sera administrador o no.")
                        && ValidacionDeEntrada.validarTelefono(empleado.getTelefono()) && ValidacionDeEntrada.validarDNI(empleado.getDni())) {
                    try {
                        EmpleadoDAOImpl dao = new EmpleadoDAOImpl();
                        dao.insertar(empleado);
                        completarTabla();
                        vaciarCampos();
                        campoNombre.requestFocus();
                        manejador.configurarBotones(false);
                        msj.mostrarAlertaInforme("Operación exitosa", "", "Se ha agregado el empleado correctamente.");
                    } catch (Exception e) {
                        msj.mostrarError("Error", "", "No se pudo agregar el empleado.");
                    }
                }
        	} else {
        		msj.mostrarError("Error", "", "Ya existe el empleado");
        	}
            
        }
    }

    @FXML
    void accionBtnEliminar(ActionEvent event) {
        Empleado em = this.tblEmpleados.getSelectionModel().getSelectedItem();
        if (em == null) {
            msj.mostrarError("Error", "", "Debe seleccionar un empleado de la lista");
        } else {
            boolean confirmacion = msj.mostrarConfirmacion("Confirmar Eliminación", "",
                    "¿Está seguro de que desea eliminar este empleado?");
            if (confirmacion) { // Si se confirma la eliminación
                try {
                    EmpleadoDAOImpl dao = new EmpleadoDAOImpl();
                    dao.eliminar(em);
                    completarTabla();
                    vaciarCampos();
                    campoNombre.requestFocus();
                    // Para habilitar "Modificar" y "Eliminar" y deshabilitar "Agregar"
                    manejador.configurarBotones(false);
                    msj.mostrarAlertaInforme("Operacion exitosa", "", "El empleado se ha eliminado");
                } catch (Exception e) {
                    msj.mostrarError("Error", "", "No se pudo eliminar el empleado");
                }
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
        }

        // Obtiene los valores de los campos
        Empleado empleado = obtenerValoresDeCampos();

        // Verifica si algún campo de texto está vacío
        if (empleado.getNombre().isEmpty() || empleado.getApellido().isEmpty() || empleado.getProvincia().isEmpty() ||
                empleado.getLocalidad().isEmpty() || empleado.getCalle().isEmpty() ||
                empleado.getMail().isEmpty() || empleado.getTelefono().isEmpty()) {
            // Mostrar mensaje de error si falta ingresar datos
            msj.mostrarError("Error", "", "Falta ingresar datos.");
        } else {
            // Realiza las validaciones con ValidacionDeEntrada
            if (ValidacionDeEntrada.validarEmail(empleado.getMail())  &&
                    ValidacionDeEntrada.validarSeleccionComboBox(comboAdmin, "Debe indicar si sera administrador o no.")
                    && ValidacionDeEntrada.validarTelefono(empleado.getTelefono()) && ValidacionDeEntrada.validarDNI(empleado.getDni())) {
                empl.setNombre_usuario(empleado.getDni());
                empl.setNombre(empleado.getNombre());
                empl.setApellido(empleado.getApellido());
                empl.setCalle(empleado.getCalle());
                empl.setTelefono(empleado.getTelefono());
                empl.setProvincia(empleado.getProvincia());
                empl.setLocalidad(empleado.getLocalidad());
                empl.setMail(empleado.getMail());
                empl.setEsAdmin(empleado.getEsAdmin());
                empl.setDni(empleado.getDni());
                boolean confirmacion = msj.mostrarConfirmacion("Confirmar Modificación", "",
                        "¿Está seguro de que desea modificar este empleado?");
                if (confirmacion) { // Si se confirma la eliminación
                    try {
                        EmpleadoDAOImpl dao = new EmpleadoDAOImpl();
                        dao.modificar(empl);
                        completarTabla();
                        vaciarCampos();
                        campoNombre.requestFocus();
                        comboAdmin.setPromptText("Selecciona una opción");
                        // Para habilitar "Modificar" y "Eliminar" y deshabilitar "Agregar"
                        manejador.configurarBotones(false);
                        msj.mostrarAlertaInforme("Operación exitosa", "", "El empleado se ha modificado");
                    } catch (Exception e) {
                        msj.mostrarError("Error", "", "No se pudo modificar el empleado.");
                    }
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
        // Para habilitar "Modificar" y "Eliminar" y deshabilitar "Agregar"
        manejador.configurarBotones(false);
        tblEmpleados.getSelectionModel().clearSelection();
        campoDNI.requestFocus();
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
                manejador.configurarBotones(true);
                // Llena los campos de entrada con los datos del proveedor seleccionado
                campoNombre.setText(newValue.getNombre());
                campoApellido.setText(newValue.getApellido());
                comboProvincia.getSelectionModel().select(newValue.getProvincia());
                campoLocalidad.setText(newValue.getLocalidad());
                campoCalle.setText(newValue.getCalle());
                campoEmail.setText(newValue.getMail());
                campoTelefono.setText(newValue.getTelefono());
                campoDNI.setText(newValue.getDni());
                comboAdmin.setValue(reconvertirValorCombo(newValue.getEsAdmin()));
            }
        });
    }

    void vaciarCampos() {
        campoNombre.clear();
        campoApellido.clear();
        campoCalle.clear();
        campoLocalidad.clear();
        comboProvincia.getSelectionModel().clearSelection();
        campoTelefono.clear();
        campoEmail.clear();
        campoDNI.clear();
        comboAdmin.setValue(null);
        comboAdmin.setPromptText("Seleccionar");
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
        // Carga la lista de provincias desde la clase ProvinciasArgentinas
        comboProvincia.setItems(ProvinciasArgentinas.getProvincias());

        comboProvincia.setValue("Chaco");

        completarTabla();
        ObservableList<String> itemsComboAdmin = FXCollections.observableArrayList("Si","No");
        this.comboAdmin.setItems(itemsComboAdmin);
        todosLosEmpleados = tblEmpleados.getItems();

        // Establecer el enfoque en campoNombre después de que la ventana se haya mostrado completamente
        Platform.runLater(() -> campoDNI.requestFocus());

        // Instancia el ManejadorBotones en la inicialización del controlador
        manejador = new ManejoDeBotones(btnModificar, btnEliminar, btnAgregar);
        // Para deshabilitar "Modificar" y "Eliminar" y habilitar "Agregar"
        manejador.configurarBotones(false);

        campoNombre.setTextFormatter(ManejoDeEntrada.soloLetrasEspacioAcento());
        campoApellido.setTextFormatter(ManejoDeEntrada.soloLetrasEspacioAcento());
        campoCalle.setTextFormatter(ManejoDeEntrada.soloLetrasNumEspAcento());
        campoTelefono.setTextFormatter(ManejoDeEntrada.soloTelefono());
        //campoProvincia.setTextFormatter(ManejoDeEntrada.soloLetrasEspacioAcento());
        campoLocalidad.setTextFormatter(ManejoDeEntrada.soloLetrasEspacioAcento());
        campoEmail.setTextFormatter(ManejoDeEntrada.soloEmail());
        campoDNI.setTextFormatter(ManejoDeEntrada.soloDni());
    }

    private Empleado obtenerValoresDeCampos() {
        String nombreIngresado = FormatoTexto.formatearTexto(this.campoNombre.getText());
        String apellidoIngresado = FormatoTexto.formatearTexto(this.campoApellido.getText());
        String provinciaIngresada = this.comboProvincia.getSelectionModel().getSelectedItem();
        String localidadIngresada = FormatoTexto.formatearTexto(this.campoLocalidad.getText());
        String calleIngresada = FormatoTexto.formatearTexto(this.campoCalle.getText());
        String dniIngresado = this.campoDNI.getText();
        String emailIngresado = this.campoEmail.getText().toLowerCase();;
        String telefonoIngresado = this.campoTelefono.getText();
        String rolIngresado = this.convertirValorCombo(comboAdmin.getValue());

        Empleado empleado = new Empleado();
        empleado.setNombre(nombreIngresado);
        empleado.setApellido(apellidoIngresado);
        empleado.setProvincia(provinciaIngresada);
        empleado.setLocalidad(localidadIngresada);
        empleado.setCalle(calleIngresada);
        empleado.setDni(dniIngresado);
        empleado.setMail(emailIngresado);
        empleado.setTelefono(telefonoIngresado);
        empleado.setNombre_usuario(dniIngresado);
        empleado.setClave(dniIngresado);
        empleado.setEsAdmin(rolIngresado);

        return empleado;
    }
}