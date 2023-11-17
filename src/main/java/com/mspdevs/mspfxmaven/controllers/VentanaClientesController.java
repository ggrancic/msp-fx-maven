package com.mspdevs.mspfxmaven.controllers;

import com.mspdevs.mspfxmaven.model.Cliente;
import com.mspdevs.mspfxmaven.model.DAO.ClienteDAOImpl;
import com.mspdevs.mspfxmaven.model.DAO.PersonaDAOImpl;
import com.mspdevs.mspfxmaven.model.DAO.ProveedorDAOImpl;
import com.mspdevs.mspfxmaven.model.Persona;
import com.mspdevs.mspfxmaven.model.Proveedor;
import com.mspdevs.mspfxmaven.utils.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;

import org.controlsfx.control.SearchableComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class VentanaClientesController implements Initializable {
    Alerta msj = new Alerta();

    private ObservableList<Cliente> todosLosClientes;

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
    private TextField campoBuscar;

    @FXML
    private TextField campoCalle;

    @FXML
    private TextField campoEmail;

    @FXML
    private TextField campoCuil;

    @FXML
    private TextField campoDni;

    @FXML
    private TextField campoRazonSocial;

    @FXML
    private TextField campoLocalidad;

    @FXML
    private TextField campoNombre;

    @FXML
    private SearchableComboBox<String> comboProvincia;

    @FXML
    private TextField campoTelefono;

    @FXML
    private TableColumn<Persona, String> colApel;

    @FXML
    private TableColumn<Persona, String> colCalle;

    @FXML
    private TableColumn<Persona, String> colRazonSocial;

    @FXML
    private TableColumn<Cliente, String> colCuil;

    @FXML
    private TableColumn<Cliente, String> colDni;

    @FXML
    private TableColumn<Persona, String> colEmail;

    @FXML
    private TableColumn<Cliente, Integer> colId;

    @FXML
    private TableColumn<Persona, String> colLoc;

    @FXML
    private TableColumn<Persona, String> colNom;

    @FXML
    private TableColumn<Persona, String> colProv;

    @FXML
    private TableColumn<Persona, String> colTele;

    @FXML
    private TableView<Cliente> tablaClientes;

    @FXML
    private ToggleGroup seleccionarTipo;

    @FXML
    private RadioButton empresaRadio;

    @FXML
    private RadioButton personaRadio;

    @FXML
    void accionBotonAgregar(ActionEvent event) {
    	
    	boolean clienteRepetido = false;

        if (!personaRadio.isSelected() && !empresaRadio.isSelected()) {
            msj.mostrarError("Error", "", "Debe seleccionar una opción: Persona o Empresa.");
        } else {

            if (personaRadio.isSelected()) {

                Cliente cliente = obtenerValoresDeCampos();

                if (cliente.getDni().isEmpty() || cliente.getNombre().isEmpty() || cliente.getApellido().isEmpty() ||
                        cliente.getProvincia().isEmpty() || cliente.getLocalidad().isEmpty() ||
                        cliente.getCuil().isEmpty() || cliente.getMail().isEmpty() || cliente.getTelefono().isEmpty()) {
                	
                    msj.mostrarError("Error", "", "Falta ingresar datos obligatorios.");
                    
                    
                } else {
                	
                    // Ajusta la Razón Social si está vacía (nombre + apellido)
            		if (cliente.getRazonSocial().isEmpty()) {
                        cliente.setRazonSocial(cliente.getNombre() + " " + cliente.getApellido());
                    }            
                    
                    for (Cliente clienteEnTabla : tablaClientes.getItems()) {
                		if (clienteEnTabla.getCuit().equals(cliente.getCuit())) {
                			clienteRepetido = true;
                			break;
                		}
                	}
                    
                    if (!clienteRepetido) {
                    	if (ValidacionDeEntrada.validarEmail(cliente.getMail()) &&
                                ValidacionDeEntrada.validarDNI(cliente.getDni()) &&
                                ValidacionDeEntrada.validarCuil(cliente.getCuil()) &&
                                ValidacionDeEntrada.validarTelefono(cliente.getTelefono())) {
                            try {
                                ClienteDAOImpl dao = new ClienteDAOImpl();
                                dao.insertar(cliente);
                                completarTabla();
                                vaciarCampos();
                                manejador.configurarBotones(false);
                                msj.mostrarAlertaInforme("Operación exitosa", "", "Se ha agregado el cliente correctamente.");
                            } catch (Exception e) {
                                msj.mostrarError("Error", "", "No se pudo agregar el cliente.");
                            }
                        }
                    } else {
                    	msj.mostrarError("Error", "", "Ya existe el cliente");
                    }
                    
                    }
                    
                    
            }
            // Realiza las validaciones
            if (empresaRadio.isSelected()) {
                // Obtener los valores de los campos en un objeto Cliente
                Cliente cliente = obtenerValoresDeCamposEmpresa();

                if (cliente.getRazonSocial().isEmpty() || cliente.getProvincia().isEmpty() || cliente.getLocalidad().isEmpty() ||
                        cliente.getCuil().isEmpty() || cliente.getMail().isEmpty() || cliente.getTelefono().isEmpty()) {
                    // Muestra un mensaje de error si falta ingresar datos
                    msj.mostrarError("Error", "", "Falta ingresar datos obligatorios.");
                } else {
                	
                	for (Cliente clienteEnTabla : tablaClientes.getItems()) {
                		if (clienteEnTabla.getCuit() != null) {
                			if (clienteEnTabla.getCuit().equals(cliente.getCuit())) {
                    			clienteRepetido = true;
                    			break;
                    		}
                		}		
                	}
                	
                	
                    if (!clienteRepetido) {
                    	if (ValidacionDeEntrada.validarEmail(cliente.getMail()) &&
                                ValidacionDeEntrada.validarCuil(cliente.getCuil()) &&
                                ValidacionDeEntrada.validarTelefono(cliente.getTelefono())) {
                            try {
                                ClienteDAOImpl dao = new ClienteDAOImpl();
                                dao.insertar(cliente);
                                completarTabla();
                                vaciarCampos();
                                manejador.configurarBotones(false);
                                msj.mostrarAlertaInforme("Operación exitosa", "", "Se ha agregado el cliente correctamente.");
                            } catch (Exception e) {
                                msj.mostrarError("Error", "", "No se pudo agregar el cliente.");
                                e.printStackTrace();
                            }
                        }
                    } else {
                    	msj.mostrarError("Error", "", "Ya existe el cliente");
                    }
                }
            }
        }
    }

    @FXML
    void accionBotonEliminar(ActionEvent event) {
        // Obtiene el cliente seleccionado en la tabla
        Cliente c = this.tablaClientes.getSelectionModel().getSelectedItem();
        if (c == null) {
            // Muestra un mensaje de error si no se selecciona ningún elemento en la tabla
            msj.mostrarError("Error", "", "Debe seleccionar un cliente de la lista");
        } else {
            boolean confirmacion = msj.mostrarConfirmacion("Confirmar Eliminación", "",
                    "¿Está seguro de que desea eliminar este cliente?");
            if (confirmacion) { // Si se confirma la eliminación
                try {
                    ClienteDAOImpl dao = new ClienteDAOImpl();
                    dao.eliminar(c);
                    completarTabla();
                    vaciarCampos();
                    campoDni.requestFocus();
                    // Para habilitar "Modificar" y "Eliminar" y deshabilitar "Agregar"
                    manejador.configurarBotones(false);
                    msj.mostrarAlertaInforme("Operacion exitosa", "", "El cliente se ha eliminado");
                } catch (Exception e) {
                    msj.mostrarError("Error", "", "No se pudo eliminar el cliente");
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
        tablaClientes.getSelectionModel().clearSelection();
        campoDni.requestFocus(); // Focus en dni
    }

    @FXML
    void accionBotonModificar(ActionEvent event) {
        // Obtiene el proveedor seleccionado en la tabla
        Cliente c = this.tablaClientes.getSelectionModel().getSelectedItem();

        // Comprueba si se seleccionó personaRadio
        if (personaRadio.isSelected()) {
            // Obtiene los valores de los campos en un objeto Cliente
            Cliente cliente = obtenerValoresDeCampos();

            // Realiza las validaciones
            if (cliente.getDni().isEmpty() || cliente.getCuil().isEmpty() || cliente.getNombre().isEmpty() || cliente.getApellido().isEmpty() ||
                    cliente.getProvincia().isEmpty() || cliente.getLocalidad().isEmpty() ||
                    cliente.getCuil().isEmpty() || cliente.getMail().isEmpty() || cliente.getTelefono().isEmpty()) {
                // Muestra mensaje de error si falta ingresar datos
                msj.mostrarError("Error", "", "Falta ingresar datos obligatorios.");
            } else {
                // Ajusta la Razón Social si está vacía (nombre + apellido)
                if (cliente.getRazonSocial().isEmpty()) {
                    cliente.setRazonSocial(cliente.getNombre() + " " + cliente.getApellido());
                }
                // Realiza las validaciones
                if (ValidacionDeEntrada.validarEmail(cliente.getMail()) &&
                        ValidacionDeEntrada.validarDNI(cliente.getDni()) &&
                        ValidacionDeEntrada.validarCuil(cliente.getCuil()) &&
                        ValidacionDeEntrada.validarTelefono(cliente.getTelefono())) {
                    boolean confirmacion = msj.mostrarConfirmacion("Confirmar Modificación", "",
                            "¿Está seguro de que desea modificar este cliente?");
                    if (confirmacion) { // Si se confirma la eliminación
                        try {
                            // Actualiza los valores del proveedor
                            c.setNombre(cliente.getNombre());
                            c.setApellido(cliente.getApellido());
                            c.setRazonSocial(cliente.getRazonSocial());
                            c.setProvincia(cliente.getProvincia());
                            c.setLocalidad(cliente.getLocalidad());
                            c.setCalle(cliente.getCalle());
                            c.setCuil(cliente.getCuil());
                            c.setDni(cliente.getDni());
                            c.setMail(cliente.getMail());
                            c.setTelefono(cliente.getTelefono());

                            ClienteDAOImpl dao = new ClienteDAOImpl();
                            dao.modificar(c);
                            completarTabla();
                            vaciarCampos();
                            manejador.configurarBotones(false);
                            msj.mostrarAlertaInforme("Operación exitosa", "", "Se ha modificado el cliente correctamente.");
                        } catch (Exception e) {
                            msj.mostrarError("Error", "", "No se pudo modificar el cliente.");
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        // Comprueba si se seleccionó empresaRadio
        else if (empresaRadio.isSelected()) {
            // Obtiene los valores de los campos en un objeto Cliente
            Cliente cliente = obtenerValoresDeCamposEmpresa();

            // Realiza las validaciones específicas para empresas
            if (cliente.getRazonSocial().isEmpty() || cliente.getProvincia().isEmpty() || cliente.getLocalidad().isEmpty() ||
                    cliente.getCuil().isEmpty() || cliente.getMail().isEmpty() || cliente.getTelefono().isEmpty()) {
                // Muestra un mensaje de error si falta ingresar datos
                msj.mostrarError("Error", "", "Falta ingresar datos obligatorios.");
            } else {
                if (ValidacionDeEntrada.validarEmail(cliente.getMail()) &&
                        ValidacionDeEntrada.validarCuil(cliente.getCuil()) &&
                        ValidacionDeEntrada.validarTelefono(cliente.getTelefono())) {
                    boolean confirmacion = msj.mostrarConfirmacion("Confirmar Modificación", "",
                            "¿Está seguro de que desea modificar este cliente?");
                    if (confirmacion) { // Si se confirma la eliminación
                        try {
                            // Actualiza los valores del proveedor
                            c.setRazonSocial(cliente.getRazonSocial());
                            c.setProvincia(cliente.getProvincia());
                            c.setLocalidad(cliente.getLocalidad());
                            c.setCalle(cliente.getCalle());
                            c.setCuil(cliente.getCuil());
                            c.setMail(cliente.getMail());
                            c.setTelefono(cliente.getTelefono());
                            ClienteDAOImpl dao = new ClienteDAOImpl();
                            dao.modificar(c);
                            completarTabla();
                            vaciarCampos();
                            manejador.configurarBotones(false);
                            msj.mostrarAlertaInforme("Operación exitosa", "", "Se ha modificado el cliente correctamente.");
                        } catch (Exception e) {
                            msj.mostrarError("Error", "", "No se pudo modificar el cliente.");
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
    
    
    @FXML
    void autoCompletarPorDni(ActionEvent event) {
    	PersonaDAOImpl p = new PersonaDAOImpl();
        ObservableList<Persona> personas = null;
        try {
            personas = p.listarTodos();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Persona persona : personas) {
        	if (persona.getDni() != null) {
        		if (persona.getDni().equals(campoDni.getText())) {
                	campoNombre.setText(persona.getNombre());
                    campoApellido.setText(persona.getApellido());
                    campoCuil.setText(persona.getCuil());
                    campoRazonSocial.setText(persona.getRazonSocial());
                    comboProvincia.setValue(persona.getProvincia());
                    campoLocalidad.setText(persona.getLocalidad());
                    campoCalle.setText(persona.getCalle());
                    campoTelefono.setText(persona.getTelefono());
                    campoEmail.setText(persona.getMail());
                    campoCuil.requestFocus();
                    empresaRadio.setDisable(true);
                    return;
                }
        	}            
        }
    }
    
    @FXML
    void autocompletarPorCuit(ActionEvent event) {
    	
    	if (!(personaRadio.isSelected())) {
    		PersonaDAOImpl p = new PersonaDAOImpl();
            ObservableList<Persona> personas = null;
            try {
                personas = p.listarTodos();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (Persona persona : personas) {
            	if(persona.getCuit() != null) {
            		if (persona.getCuit().equals(campoCuil.getText())) {
                        campoCuil.setText(persona.getCuit());
                        campoRazonSocial.setText(persona.getRazonSocial());
                        comboProvincia.setValue(persona.getProvincia());
                        campoLocalidad.setText(persona.getLocalidad());
                        campoCalle.setText(persona.getCalle());
                        campoTelefono.setText(persona.getTelefono());
                        campoEmail.setText(persona.getMail());
                        personaRadio.setDisable(true);
                        return;
                    }
            	}
                
            }
    	}
    }
    
    

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Llama al método para deshabilitar todos los campos de texto al inicializar
        deshabilitarCamposTexto();
        // Carga la lista de provincias desde la clase ProvinciasArgentinas
        comboProvincia.setItems(ProvinciasArgentinas.getProvincias());
        comboProvincia.setValue("Chaco");
        // Llamado a completarTabla al inicializar el controlador
        completarTabla();

        todosLosClientes = tablaClientes.getItems();

        // Instancia el ManejadorBotones en la inicialización del controlador
        manejador = new ManejoDeBotones(btnModificar, btnEliminar, btnAgregar);
        // Para deshabilitar "Modificar" y "Eliminar" y habilitar "Agregar"
        manejador.configurarBotones(false);

        campoDni.setTextFormatter(ManejoDeEntrada.soloDni());
        campoNombre.setTextFormatter(ManejoDeEntrada.soloLetrasEspacioAcento());
        campoApellido.setTextFormatter(ManejoDeEntrada.soloLetrasEspacioAcento());
        campoRazonSocial.setTextFormatter(ManejoDeEntrada.soloLetrasEspacioAcento());
        campoCalle.setTextFormatter(ManejoDeEntrada.soloLetrasNumEspAcento());
        campoTelefono.setTextFormatter(ManejoDeEntrada.soloTelefono());
        campoLocalidad.setTextFormatter(ManejoDeEntrada.soloLetrasEspacioAcento());
        campoEmail.setTextFormatter(ManejoDeEntrada.soloEmail());
        campoCuil.setTextFormatter(ManejoDeEntrada.soloNumerosEnteros());

        // Manejo de eventos para habilitar o deshabilitar campos de texto
        seleccionarTipo.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == personaRadio) {
                // Habilita campos relacionados con personas
                campoDni.setDisable(false);
                campoCuil.setDisable(false);
                campoNombre.setDisable(false);
                campoApellido.setDisable(false);
                campoRazonSocial.setDisable(false);
                campoEmail.setDisable(false);
                campoCalle.setDisable(false);
                campoTelefono.setDisable(false);
                comboProvincia.setDisable(false);
                campoLocalidad.setDisable(false);
                // Focus en Dni
                campoDni.requestFocus();
//                vaciarSoloTexto();
            } else if (newValue == empresaRadio) {
                // Habilita campos relacionados con empresas
                campoDni.setDisable(true);
                campoCuil.setDisable(false);
                campoNombre.setDisable(true);
                campoApellido.setDisable(true);
                campoRazonSocial.setDisable(false);
                campoEmail.setDisable(false);
                campoCalle.setDisable(false);
                campoTelefono.setDisable(false);
                comboProvincia.setDisable(false);
                campoLocalidad.setDisable(false);
                //Vacio algunos campos
                campoDni.setText("");
                campoNombre.setText("");
                campoApellido.setText("");
                // Focus en el campo Cuil
                campoCuil.requestFocus();
//                vaciarSoloTexto();
            }
        });
    }

    public void completarTabla() {
        campoNombre.requestFocus();

        // Crea una instancia del DAO de Proveedor
        ClienteDAOImpl cliente = new ClienteDAOImpl();
        ObservableList<Cliente> clientes = null;

        try {
            // Se intenta obtener la lista de proveedores desde la base de datos
            clientes = cliente.listarTodos();
        } catch (Exception e) {
            // Maneja cualquier excepción que ocurra durante la obtención de datos
            msj.mostrarError("Error", "", "Se ha producido un error recuperando los datos de la BD");
        }

        // Configura las celdas de la tabla para mostrar los datos de Proveedor y Persona
        // Utilizando PropertyValueFactory para enlazar las propiedades de la clase Cliente con las columnas de la tabla
        this.colId.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        this.colNom.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.colApel.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        this.colRazonSocial.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getRazonSocial()));
        this.colProv.setCellValueFactory(new PropertyValueFactory<>("provincia"));
        this.colLoc.setCellValueFactory(new PropertyValueFactory<>("localidad"));
        this.colCalle.setCellValueFactory(new PropertyValueFactory<>("calle"));
        this.colEmail.setCellValueFactory(new PropertyValueFactory<>("mail"));
        this.colTele.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        this.colCuil.setCellValueFactory(new PropertyValueFactory<>("cuil"));
        this.colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        // Establece los datos de la tabla con la lista de proveedores
        this.tablaClientes.setItems(clientes);

        // Configura un listener para la selección de fila en la tabla
        tablaClientes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                manejador.configurarBotones(true);
                // Llena los campos de entrada con los datos del proveedor seleccionado
                campoNombre.setText(newValue.getNombre());
                campoApellido.setText(newValue.getApellido());
                campoRazonSocial.setText(newValue.getRazonSocial());
                comboProvincia.getSelectionModel().select(newValue.getProvincia());
                campoLocalidad.setText(newValue.getLocalidad());
                campoCalle.setText(newValue.getCalle());
                campoCuil.setText(newValue.getCuil());
                campoEmail.setText(newValue.getMail());
                campoTelefono.setText(newValue.getTelefono());
                campoDni.setText(newValue.getDni());

                // Habilita o deshabilita el Toggle basado en si el cliente tiene DNI
                if (newValue.getDni() != null && !newValue.getDni().isEmpty()) {
                    // El cliente tiene un DNI, habilita "Persona" y deshabilita "Empresa"
                    personaRadio.setDisable(false);
                    empresaRadio.setDisable(true);
                    seleccionarTipo.selectToggle(personaRadio);
                } else {
                    // El cliente no tiene DNI, habilita "Empresa" y deshabilita "Persona"
                    empresaRadio.setDisable(false);
                    personaRadio.setDisable(true);
                    seleccionarTipo.selectToggle(empresaRadio);
                }
            }
        });
        colId.setSortType(TableColumn.SortType.ASCENDING);
        tablaClientes.getSortOrder().add(colId);
        tablaClientes.sort();
    }

    public void vaciarCampos() {
        // Limpia los campos de entrada y foucs en nombre
        campoDni.setText("");
        campoNombre.setText("");
        campoApellido.setText("");
        campoRazonSocial.setText("");
        comboProvincia.getSelectionModel().clearSelection();
        campoLocalidad.setText("");
        campoCalle.setText("");
        campoEmail.setText("");
        campoTelefono.setText("");
        campoBuscar.setText("");
        campoCuil.setText("");
        //campoNombre.requestFocus();
        comboProvincia.setValue("Chaco");
        // Deselecciona los Toggle después del guardado
        personaRadio.setSelected(false);
        empresaRadio.setSelected(false);
        personaRadio.setDisable(false);
        empresaRadio.setDisable(false);
        // Deshabilita todos los campos de texto
        deshabilitarCamposTexto();
    }
    
    public void vaciarSoloTexto() {
    	campoDni.setText("");
        campoNombre.setText("");
        campoApellido.setText("");
        campoRazonSocial.setText("");
        comboProvincia.getSelectionModel().clearSelection();
        campoLocalidad.setText("");
        campoCalle.setText("");
        campoEmail.setText("");
        campoTelefono.setText("");
        campoBuscar.setText("");
        campoCuil.setText("");
        comboProvincia.setValue("Chaco");
    }

    @FXML
    void filtrarClientes(KeyEvent event) {
        // Obtiene el texto ingresado en el campo de búsqueda
        String filtro = campoBuscar.getText().toLowerCase();

        if (filtro.isEmpty()) {
            // Si el campo de búsqueda está vacío, muestra todos los clientes originales
            tablaClientes.setItems(todosLosClientes);
        } else {
            // Filtra la lista de todos los clientes originales y mostrar los resultados
            ObservableList<Cliente> clientesFiltrados = todosLosClientes.filtered(cliente ->
                    (cliente.getNombre() != null && cliente.getNombre().toLowerCase().contains(filtro)) || // Busca en el nombre
                            (cliente.getRazonSocial() != null && cliente.getRazonSocial().toLowerCase().contains(filtro)) || // Busca en la razón social
                            (cliente.getCuil() != null && cliente.getCuil().toLowerCase().contains(filtro)) || // Busca en el cuil
                            (cliente.getDni() != null && cliente.getDni().toLowerCase().contains(filtro)) // Busca en el dni
            );
            tablaClientes.setItems(clientesFiltrados);
        }
    }

    private Cliente obtenerValoresDeCampos() {
        String nombreIngresado = FormatoTexto.formatearTexto(this.campoNombre.getText());
        String apellidoIngresado = FormatoTexto.formatearTexto(this.campoApellido.getText());
        String provinciaIngresada = this.comboProvincia.getSelectionModel().getSelectedItem();
        String localidadIngresada = FormatoTexto.formatearTexto(this.campoLocalidad.getText());
        String calleIngresada = FormatoTexto.formatearTexto(this.campoCalle.getText());
        String cuitIngresado = this.campoCuil.getText();
        String dniIngresado = campoDni.getText();
        String razonSocialIngresada = campoRazonSocial.getText();
        String emailIngresado = this.campoEmail.getText().toLowerCase();
        String telefonoIngresado = this.campoTelefono.getText();

        Cliente cliente = new Cliente();
        cliente.setNombre(nombreIngresado);
        cliente.setApellido(apellidoIngresado);
        cliente.setProvincia(provinciaIngresada);
        cliente.setLocalidad(localidadIngresada);
        cliente.setCalle(calleIngresada);
        cliente.setCuil(cuitIngresado);
        cliente.setCuit(cuitIngresado);
        cliente.setDni(dniIngresado);
        cliente.setRazonSocial(razonSocialIngresada);
        cliente.setMail(emailIngresado);
        cliente.setTelefono(telefonoIngresado);

        return cliente;
    }

    private Cliente obtenerValoresDeCamposEmpresa() {
        String razonSocialIngresada = FormatoTexto.formatearTexto(campoRazonSocial.getText());
        String provinciaIngresada = this.comboProvincia.getSelectionModel().getSelectedItem();
        String localidadIngresada = FormatoTexto.formatearTexto(this.campoLocalidad.getText());
        String calleIngresada = FormatoTexto.formatearTexto(this.campoCalle.getText());
        String cuilIngresado = this.campoCuil.getText();
        String emailIngresado = this.campoEmail.getText().toLowerCase();
        String telefonoIngresado = this.campoTelefono.getText();

        Cliente cliente = new Cliente();
        cliente.setProvincia(provinciaIngresada);
        cliente.setLocalidad(localidadIngresada);
        cliente.setCalle(calleIngresada);
        cliente.setCuil(cuilIngresado);
        cliente.setCuit(cuilIngresado);
        cliente.setRazonSocial(razonSocialIngresada);
        cliente.setMail(emailIngresado);
        cliente.setTelefono(telefonoIngresado);

        return cliente;
    }

    @FXML
    void porEmpresa(ActionEvent event) {
    }

    @FXML
    void porPersona(ActionEvent event) {
    }

    // Método para deshabilitar todos los campos de texto
    private void deshabilitarCamposTexto() {
        campoDni.setDisable(true);
        campoCuil.setDisable(true);
        campoNombre.setDisable(true);
        campoApellido.setDisable(true);
        campoRazonSocial.setDisable(true);
        campoEmail.setDisable(true);
        campoCalle.setDisable(true);
        campoTelefono.setDisable(true);
        comboProvincia.setDisable(true);
        campoLocalidad.setDisable(true);
    }
}
