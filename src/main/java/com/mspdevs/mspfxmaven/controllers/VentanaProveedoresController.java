package com.mspdevs.mspfxmaven.controllers;

import com.mspdevs.mspfxmaven.model.Cliente;
import com.mspdevs.mspfxmaven.model.DAO.ClienteDAOImpl;
import com.mspdevs.mspfxmaven.model.DAO.PersonaDAOImpl;
import com.mspdevs.mspfxmaven.model.DAO.ProveedorDAOImpl;
import com.mspdevs.mspfxmaven.model.Persona;
import com.mspdevs.mspfxmaven.model.Proveedor;
import com.mspdevs.mspfxmaven.utils.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import org.controlsfx.control.SearchableComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class VentanaProveedoresController implements Initializable  {
    Alerta msj = new Alerta();

    private ObservableList<Proveedor> todosLosProveedores;

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
    private TextField buscarCampo;

    @FXML
    private TextField campoDni;

    @FXML
    private TextField campoApellido;

    @FXML
    private TextField campoCalle;

    @FXML
    private TextField campoCuit;

    @FXML
    private TextField campoEmail;

    @FXML
    private TextField campoLocalidad;

    @FXML
    private TextField campoNombre;

    @FXML
    private TextField campoRazonSocial;

    @FXML
    private SearchableComboBox<String> comboProvincia;

    @FXML
    private TextField campoTelefono;

    @FXML
    private TableColumn<Persona, String> colApel;

    @FXML
    private TableColumn<Persona, String> colCalle;

    @FXML
    private TableColumn<Proveedor, String> colRazonS;

    @FXML
    private TableColumn<Proveedor, String> colCuit;

    @FXML
    private TableColumn<Persona, String> colDni;

    @FXML
    private TableColumn<Persona, String> colEmail;

    @FXML
    private TableColumn<Proveedor, Integer> colId;

    @FXML
    private TableColumn<Persona, String> colLoca;

    @FXML
    private TableColumn<Persona, String> colNom;

    @FXML
    private TableColumn<Persona, String> colProv;

    @FXML
    private TableColumn<Persona, String> colTele;

    @FXML
    private TableView<Proveedor> tablaProveedores;

    @FXML
    private RadioButton empresaRB;

    @FXML
    private RadioButton personaRB;

    @FXML
    private ToggleGroup seleccionarTipo;

    @FXML
    void accionBotonAgregar(ActionEvent event) {
    	
    	boolean proveedorRepetido = false;
    	
        // Comprueba si se ha seleccionado un Toggle
        if (!personaRB.isSelected() && !empresaRB.isSelected()) {
            msj.mostrarError("Error", "", "Debe seleccionar una opción: Persona o Empresa.");
        } else {
            // Comprueba si se seleccionó personaRadio
            if (personaRB.isSelected()) {
                // Obtener los valores de los campos en un objeto Cliente
                Proveedor proveedor = obtenerValoresDeCampos();

                // Realiza las validaciones
                if (proveedor.getDni().isEmpty() || proveedor.getNombre().isEmpty() || proveedor.getApellido().isEmpty() ||
                        proveedor.getProvincia().isEmpty() || proveedor.getLocalidad().isEmpty() ||
                        proveedor.getCuit().isEmpty() || proveedor.getMail().isEmpty() || proveedor.getTelefono().isEmpty()) {
                    // Mostrar mensaje de error si falta ingresar datos
                    msj.mostrarError("Error", "", "Falta ingresar datos obligatorios.");
                } else {
                    // Ajusta la Razón Social si está vacía (nombre + apellido)
                    if (proveedor.getRazonSocial().isEmpty()) {
                        proveedor.setRazonSocial(proveedor.getNombre() + " " + proveedor.getApellido());
                    }
                    
                    
                    for (Proveedor proveedorEnTabla : tablaProveedores.getItems()) {
						if (proveedorEnTabla.getCuit().equals(proveedor.getCuit())) {
							proveedorRepetido = true;
							break;
						}
					}
                    
                    if (!proveedorRepetido) {
                        if (ValidacionDeEntrada.validarEmail(proveedor.getMail()) &&
                                ValidacionDeEntrada.validarCuil(proveedor.getCuit()) &&
                                ValidacionDeEntrada.validarDNI(proveedor.getDni()) &&
                                ValidacionDeEntrada.validarTelefono(proveedor.getTelefono())) {
                            try {
                                ProveedorDAOImpl dao = new ProveedorDAOImpl();
                                dao.insertar(proveedor);
                                completarTabla();
                                vaciarCampos();
                                manejador.configurarBotones(false);
                                msj.mostrarAlertaInforme("Operación exitosa", "", "Se ha agregado el proveedor correctamente.");
                            } catch (Exception e) {
                                msj.mostrarError("Error", "", "No se pudo agregar el proveedor.");
                                e.printStackTrace();
                            }
                        }
                    } else {
                    	msj.mostrarError("Error", "", "Ya existe el proveedor");
                    }
                }
            }
            // Realiza las validaciones
            if (empresaRB.isSelected()) {
                // Obtener los valores de los campos en un objeto Cliente
                Proveedor proveedor = obtenerValoresDeCamposEmpresa();

                if (proveedor.getRazonSocial().isEmpty() || proveedor.getProvincia().isEmpty() || proveedor.getLocalidad().isEmpty() ||
                        proveedor.getCuit().isEmpty() || proveedor.getMail().isEmpty() || proveedor.getTelefono().isEmpty()) {
                    // Muestra un mensaje de error si falta ingresar datos
                    msj.mostrarError("Error", "", "Falta ingresar datos obligatorios.");
                } else {
                	
                	for (Proveedor proveedorEnTabla : tablaProveedores.getItems()) {
						if (proveedorEnTabla.getCuit().equals(proveedor.getCuit())) {
							proveedorRepetido = true;
							break;
						}
					}
                	
                	if (!proveedorRepetido) {
                		if (ValidacionDeEntrada.validarEmail(proveedor.getMail()) &&
                                ValidacionDeEntrada.validarCuil(proveedor.getCuit()) &&
                                ValidacionDeEntrada.validarTelefono(proveedor.getTelefono())) {
                            try {
                                ProveedorDAOImpl dao = new ProveedorDAOImpl();
                                dao.insertar(proveedor);
                                completarTabla();
                                vaciarCampos();
                                manejador.configurarBotones(false);
                                msj.mostrarAlertaInforme("Operación exitosa", "", "Se ha agregado el proveedor correctamente.");
                            } catch (Exception e) {
                                msj.mostrarError("Error", "", "No se pudo agregar el proveedor.");
                                e.printStackTrace();
                            }
                        }
                	} else {
                		msj.mostrarError("Error", "", "Ya existe el proveedor");
                	}
                }
            }
        }
    }

    @FXML
    void accionBotonEliminar(ActionEvent event) {
        // Obtiene el proveedor seleccionado en la tabla
        Proveedor p = this.tablaProveedores.getSelectionModel().getSelectedItem();
        if (p == null) {
            // Muestra un mensaje de error si no se selecciona ningún elemento en la tabla
            msj.mostrarError("Error", "", "Debe seleccionar un elemento de la lista");
        } else {
            // Mostrar una ventana de confirmación
            boolean confirmacion = msj.mostrarConfirmacion("Confirmar Eliminación", "",
                    "¿Está seguro de que desea eliminar este proveedor?");
            if (confirmacion) { // Si se confirma la eliminación
                try {
                    ProveedorDAOImpl dao = new ProveedorDAOImpl();
                    dao.eliminar(p);
                    completarTabla();
                    vaciarCampos();
                    campoDni.requestFocus();
                    manejador.configurarBotones(false);
                    msj.mostrarAlertaInforme("Operacion exitosa", "", "El proveedor se ha eliminado");
                } catch (Exception e) {
                    msj.mostrarError("Error", "", "No se pudo eliminar el elemento de la BD");
                }
            }
        }
    }

    @FXML
    void accionBotonLimpiar(ActionEvent event) {
        vaciarCampos(); // Limpia los campos de texto
        manejador.configurarBotones(false); // Deshabilita "Modificar" y "Eliminar", y habilita "Agregar"
        tablaProveedores.getSelectionModel().clearSelection(); // Deselecciona la fila en la tabla
        campoDni.requestFocus(); // Focus en dni
    }

    @FXML
    void accionBotonModificar(ActionEvent event) {
        // Obtiene el proveedor seleccionado en la tabla
        Proveedor p = this.tablaProveedores.getSelectionModel().getSelectedItem();

        // Comprobar si se seleccionó personaRadio
        if (personaRB.isSelected()) {
            // Obtener los valores de los campos en un objeto Cliente
            Proveedor proveedor = obtenerValoresDeCampos();

            // Realizar las validaciones específicas para personas
            if (proveedor.getDni().isEmpty() || proveedor.getCuit().isEmpty() || proveedor.getNombre().isEmpty() || proveedor.getApellido().isEmpty() ||
                    proveedor.getProvincia().isEmpty() || proveedor.getLocalidad().isEmpty() || proveedor.getMail().isEmpty() || proveedor.getTelefono().isEmpty()) {
                // Mostrar mensaje de error si falta ingresar datos
                msj.mostrarError("Error", "", "Falta ingresar datos obligatorios.");
            } else {
                // Ajustar la Razón Social si está vacía
                if (proveedor.getRazonSocial().isEmpty()) {
                    proveedor.setRazonSocial(proveedor.getNombre() + " " + proveedor.getApellido());
                }
                // Realizar las validaciones específicas para personas
                if (ValidacionDeEntrada.validarEmail(proveedor.getMail()) &&
                        ValidacionDeEntrada.validarCuil(proveedor.getCuit()) &&
                        ValidacionDeEntrada.validarDNI(proveedor.getDni()) &&
                        ValidacionDeEntrada.validarTelefono(proveedor.getTelefono())) {
                    boolean confirmacion = msj.mostrarConfirmacion("Confirmar Modificación", "",
                            "¿Está seguro de que desea modificar este proveedor?");
                    if (confirmacion) { // Si se confirma la eliminación
                        try {
                            // Actualiza los valores del proveedor
                            p.setNombre(proveedor.getNombre());
                            p.setApellido(proveedor.getApellido());
                            p.setRazonSocial(proveedor.getRazonSocial());
                            p.setProvincia(proveedor.getProvincia());
                            p.setLocalidad(proveedor.getLocalidad());
                            p.setCalle(proveedor.getCalle());
                            p.setCuit(proveedor.getCuit());
                            p.setDni(proveedor.getDni());
                            p.setMail(proveedor.getMail());
                            p.setTelefono(proveedor.getTelefono());

                            ProveedorDAOImpl dao = new ProveedorDAOImpl();
                            dao.modificar(p);
                            completarTabla();
                            vaciarCampos();
                            manejador.configurarBotones(false);
                            msj.mostrarAlertaInforme("Operación exitosa", "", "Se ha modificado el proveedor correctamente.");
                        } catch (Exception e) {
                            msj.mostrarError("Error", "", "No se pudo modificar el proveedor.");
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        // Comprueba si se seleccionó empresaRadio
        else if (empresaRB.isSelected()) {
            // Obtener los valores de los campos en un objeto Cliente
            Proveedor proveedor = obtenerValoresDeCamposEmpresa();

            // Realiza las validaciones
            if (proveedor.getRazonSocial().isEmpty() || proveedor.getProvincia().isEmpty() || proveedor.getLocalidad().isEmpty() ||
                    proveedor.getCuit().isEmpty() || proveedor.getMail().isEmpty() || proveedor.getTelefono().isEmpty()) {
                // Mostrar mensaje de error si falta ingresar datos
                msj.mostrarError("Error", "", "Falta ingresar datos obligatorios.");
            } else {
                // Ajustar la Razón Social si está vacía
                if (proveedor.getRazonSocial().isEmpty()) {
                    proveedor.setRazonSocial(proveedor.getNombre() + " " + proveedor.getApellido());
                }
                if (ValidacionDeEntrada.validarEmail(proveedor.getMail()) &&
                        ValidacionDeEntrada.validarCuil(proveedor.getCuit()) &&
                        ValidacionDeEntrada.validarTelefono(proveedor.getTelefono())) {
                    boolean confirmacion = msj.mostrarConfirmacion("Confirmar Modificación", "",
                            "¿Está seguro de que desea modificar este proveedor?");
                    if (confirmacion) { // Si se confirma la eliminación
                        try {
                            // Actualiza los valores del proveedor
                            p.setRazonSocial(proveedor.getRazonSocial());
                            p.setProvincia(proveedor.getProvincia());
                            p.setLocalidad(proveedor.getLocalidad());
                            p.setCalle(proveedor.getCalle());
                            p.setCuit(proveedor.getCuit());
                            p.setMail(proveedor.getMail());
                            p.setTelefono(proveedor.getTelefono());

                            ProveedorDAOImpl dao = new ProveedorDAOImpl();
                            dao.modificar(p);
                            completarTabla();
                            vaciarCampos();
                            manejador.configurarBotones(false);
                            msj.mostrarAlertaInforme("Operación exitosa", "", "Se ha modificado el proveedor correctamente.");
                        } catch (Exception e) {
                            msj.mostrarError("Error", "", "No se pudo modificar el proveedor.");
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
                    campoCuit.setText(persona.getCuil());
                    campoRazonSocial.setText(persona.getRazonSocial());
                    comboProvincia.setValue(persona.getProvincia());
                    campoLocalidad.setText(persona.getLocalidad());
                    campoCalle.setText(persona.getCalle());
                    campoTelefono.setText(persona.getTelefono());
                    campoEmail.setText(persona.getMail());
                    campoCuit.requestFocus();
                    empresaRB.setDisable(true);
                    return;
                }
        	}            
        }
    }
    
    @FXML
    void autocompletarPorCuit(ActionEvent event) {
    	
    	if (!(personaRB.isSelected())) {
    		PersonaDAOImpl p = new PersonaDAOImpl();
            ObservableList<Persona> personas = null;
            try {
                personas = p.listarTodos();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (Persona persona : personas) {
            	if(persona.getCuit() != null) {
            		if (persona.getCuit().equals(campoCuit.getText())) {
                        campoCuit.setText(persona.getCuit());
                        campoRazonSocial.setText(persona.getRazonSocial());
                        comboProvincia.setValue(persona.getProvincia());
                        campoLocalidad.setText(persona.getLocalidad());
                        campoCalle.setText(persona.getCalle());
                        campoTelefono.setText(persona.getTelefono());
                        campoEmail.setText(persona.getMail());
                        personaRB.setDisable(true);
                        return;
                    }
            	}
                
            }
    	}
    	
    	
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
        campoCuit.setText("");
        comboProvincia.setValue("Chaco");
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

        todosLosProveedores = tablaProveedores.getItems();

        // Establece el enfoque en campoNombre después de que la ventana se haya mostrado completamente
        Platform.runLater(() -> campoDni.requestFocus());

        // Instancia el ManejadorBotones en la inicialización del controlador
        manejador = new ManejoDeBotones(btnModificar, btnEliminar, btnAgregar);
        // Para deshabilitar "Modificar" y "Eliminar" y habilitar "Agregar"
        manejador.configurarBotones(false);

        campoNombre.setTextFormatter(ManejoDeEntrada.soloLetrasEspacioAcento());
        campoApellido.setTextFormatter(ManejoDeEntrada.soloLetrasEspacioAcento());
        campoRazonSocial.setTextFormatter(ManejoDeEntrada.soloLetrasEspacioAcento());
        campoCalle.setTextFormatter(ManejoDeEntrada.soloLetrasNumEspAcento());
        campoTelefono.setTextFormatter(ManejoDeEntrada.soloTelefono());
        //campoProvincia.setTextFormatter(ManejoDeEntrada.soloLetrasEspacioAcento());
        campoLocalidad.setTextFormatter(ManejoDeEntrada.soloLetrasEspacioAcento());
        campoEmail.setTextFormatter(ManejoDeEntrada.soloEmail());
        campoCuit.setTextFormatter(ManejoDeEntrada.soloNumerosEnteros());
        campoDni.setTextFormatter(ManejoDeEntrada.soloDni());

        // Manejo de eventos para habilitar o deshabilitar campos de texto
        seleccionarTipo.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == personaRB) {
                // Habilitar campos relacionados con personas
                campoDni.setDisable(false);
                campoCuit.setDisable(false);
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
            } else if (newValue == empresaRB) {
                // Habilitar campos relacionados con empresas
                campoDni.setDisable(true);
                campoCuit.setDisable(false);
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
                campoCuit.requestFocus();
            }
        });
    }
    public void completarTabla() {
        // Crea una instancia del DAO de Proveedor
        ProveedorDAOImpl proveedor = new ProveedorDAOImpl();
        ObservableList<Proveedor> proveedores = null;

        try {
            // Se intenta obtener la lista de proveedores desde la base de datos
            proveedores = proveedor.listarTodos();
        } catch (Exception e) {
            // Maneja cualquier excepción que ocurra durante la obtención de datos
            msj.mostrarError("Error", "", "Se ha producido un error recuperando los datos de la BD");
        }

        // Configura las celdas de la tabla para mostrar los datos de Proveedor y Persona
        // Utilizando PropertyValueFactory para enlazar las propiedades de la clase Proveedor con las columnas de la tabla
        this.colId.setCellValueFactory(new PropertyValueFactory<>("idProveedor"));
        this.colCuit.setCellValueFactory(new PropertyValueFactory<>("cuit"));
        this.colNom.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.colApel.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        this.colRazonS.setCellValueFactory(new PropertyValueFactory<>("razonSocial"));
        this.colProv.setCellValueFactory(new PropertyValueFactory<>("provincia"));
        this.colLoca.setCellValueFactory(new PropertyValueFactory<>("localidad"));
        this.colCalle.setCellValueFactory(new PropertyValueFactory<>("calle"));
        this.colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        this.colEmail.setCellValueFactory(new PropertyValueFactory<>("mail"));
        this.colTele.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        // Establece los datos de la tabla con la lista de proveedores
        this.tablaProveedores.setItems(proveedores);

        // Configura un listener para la selección de fila en la tabla
        tablaProveedores.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                manejador.configurarBotones(true);
                // Llena los campos de entrada con los datos del proveedor seleccionado
                campoNombre.setText(newValue.getNombre());
                campoApellido.setText(newValue.getApellido());
                campoRazonSocial.setText(newValue.getRazonSocial());
                comboProvincia.getSelectionModel().select(newValue.getProvincia());
                campoLocalidad.setText(newValue.getLocalidad());
                campoCalle.setText(newValue.getCalle());
                campoCuit.setText(newValue.getCuit());
                campoEmail.setText(newValue.getMail());
                campoTelefono.setText(newValue.getTelefono());
                campoDni.setText(newValue.getDni());

                // Habilita o deshabilita el Toggle basado en si el cliente tiene DNI
                if (newValue.getDni() != null && !newValue.getDni().isEmpty()) {
                    // El cliente tiene un DNI, habilita "Persona" y deshabilita "Empresa"
                    personaRB.setDisable(false);
                    empresaRB.setDisable(true);
                    seleccionarTipo.selectToggle(personaRB);
                } else {
                    // El cliente no tiene DNI, habilita "Empresa" y deshabilita "Persona"
                    empresaRB.setDisable(false);
                    personaRB.setDisable(true);
                    seleccionarTipo.selectToggle(empresaRB);
                }
            }
        });
        colId.setSortType(TableColumn.SortType.ASCENDING);
        tablaProveedores.getSortOrder().add(colId);
        tablaProveedores.sort();
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
        buscarCampo.setText("");
        campoCuit.setText("");
        //campoNombre.requestFocus();
        comboProvincia.setValue("Chaco");
        // Deselecciona los Toggle después del guardado
        personaRB.setSelected(false);
        empresaRB.setSelected(false);
        personaRB.setDisable(false);
        empresaRB.setDisable(false);
        // Deshabilita todos los campos de texto
        deshabilitarCamposTexto();
    }

    @FXML
    void filtrarProveedores(KeyEvent event) {
        // Obteniene el texto ingresado en el campo de búsqueda
        String filtro = buscarCampo.getText().toLowerCase();

        if (filtro.isEmpty()) {
            // Si el campo de búsqueda está vacío, muestra todos los clientes originales
            tablaProveedores.setItems(todosLosProveedores);
        } else {
            // Filtra la lista de todos los clientes originales y mostrar los resultados
            ObservableList<Proveedor> proveedoresFiltrados = todosLosProveedores.filtered(proveedor ->
                    (proveedor.getNombre() != null && proveedor.getNombre().toLowerCase().contains(filtro)) || // Busca en el nombre
                            (proveedor.getRazonSocial() != null && proveedor.getRazonSocial().toLowerCase().contains(filtro)) || // Busca en la razón social
                            (proveedor.getCuit() != null && proveedor.getCuit().toLowerCase().contains(filtro)) || // Busca en el cuil
                            (proveedor.getDni() != null && proveedor.getDni().toLowerCase().contains(filtro)) // Busca en el dni
            );
            tablaProveedores.setItems(proveedoresFiltrados);
        }
    }

    private Proveedor obtenerValoresDeCampos() {
        String nombreIngresado = FormatoTexto.formatearTexto(this.campoNombre.getText());
        String apellidoIngresado = FormatoTexto.formatearTexto(this.campoApellido.getText());
        String provinciaIngresada = this.comboProvincia.getSelectionModel().getSelectedItem();
        String localidadIngresada = FormatoTexto.formatearTexto(this.campoLocalidad.getText());
        String calleIngresada = FormatoTexto.formatearTexto(this.campoCalle.getText());
        String cuitIngresado = this.campoCuit.getText();
        String dniIngresado = campoDni.getText();
        String razonSocialIngresada = FormatoTexto.formatearTexto(campoRazonSocial.getText());
        String emailIngresado = this.campoEmail.getText().toLowerCase();
        String telefonoIngresado = this.campoTelefono.getText();

        Proveedor proveedor = new Proveedor();
        proveedor.setNombre(nombreIngresado);
        proveedor.setApellido(apellidoIngresado);
        proveedor.setRazonSocial(razonSocialIngresada);
        proveedor.setProvincia(provinciaIngresada);
        proveedor.setLocalidad(localidadIngresada);
        proveedor.setCalle(calleIngresada);
        proveedor.setCuit(cuitIngresado);
        proveedor.setDni(dniIngresado);
        proveedor.setMail(emailIngresado);
        proveedor.setTelefono(telefonoIngresado);

        return proveedor;
    }

    private Proveedor obtenerValoresDeCamposEmpresa() {
        String razonSocialIngresada = FormatoTexto.formatearTexto(campoRazonSocial.getText());
        String provinciaIngresada = this.comboProvincia.getSelectionModel().getSelectedItem();
        String localidadIngresada = FormatoTexto.formatearTexto(this.campoLocalidad.getText());
        String calleIngresada = FormatoTexto.formatearTexto(this.campoCalle.getText());
        String cuitIngresado = this.campoCuit.getText();
        String emailIngresado = this.campoEmail.getText().toLowerCase();
        String telefonoIngresado = this.campoTelefono.getText();

        Proveedor proveedor = new Proveedor();
        //cliente.setNombre(nombreIngresado);
        //cliente.setApellido(apellidoIngresado);
        proveedor.setProvincia(provinciaIngresada);
        proveedor.setLocalidad(localidadIngresada);
        proveedor.setCalle(calleIngresada);
        proveedor.setCuit(cuitIngresado);
        //cliente.setDni(dniIngresado);
        proveedor.setRazonSocial(razonSocialIngresada);
        proveedor.setMail(emailIngresado);
        proveedor.setTelefono(telefonoIngresado);

        return proveedor;
    }

    private void deshabilitarCamposTexto() {
        campoDni.setDisable(true);
        campoCuit.setDisable(true);
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