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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.application.Platform;
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
    private TableColumn<Cliente, String> colCuil;

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
    void accionBotonAgregar(ActionEvent event) {
        // Obtener los valores de los campos en un objeto Proveedor
        Cliente cliente = obtenerValoresDeCampos();

        // Verifica si algún campo de texto está vacío
        if (cliente.getDni().isEmpty() || cliente.getNombre().isEmpty() || cliente.getApellido().isEmpty() || cliente.getProvincia().isEmpty() ||
                cliente.getLocalidad().isEmpty() || cliente.getCalle().isEmpty() || cliente.getCuil().isEmpty() ||
                cliente.getMail().isEmpty() || cliente.getTelefono().isEmpty()) {
            // Mostrar mensaje de error si falta ingresar datos
            msj.mostrarError("Error", "", "Falta ingresar datos.");
        } else {
            // Realiza las validaciones con ValidacionDeEntrada
            if (ValidacionDeEntrada.validarEmail(cliente.getMail()) &&
                    ValidacionDeEntrada.validarCuil(cliente.getCuil()) &&
                    ValidacionDeEntrada.validarTelefono(cliente.getTelefono())) {
                try {
                    ClienteDAOImpl dao = new ClienteDAOImpl();
                    dao.insertar(cliente);
                    completarTabla();
                    vaciarCampos();
                    campoNombre.requestFocus();
                    manejador.configurarBotones(false);
                    msj.mostrarAlertaInforme("Operación exitosa", "", "Se ha agregado el cliente correctamente.");
                } catch (Exception e) {
                    msj.mostrarError("Error", "", "No se pudo agregar el cliente.");
                }
            }
        }
        /*
        // Obtiene los valores ingresados en los campos de texto
        String nombreIngresado = this.campoNombre.getText();
        String apellidoIngresado = this.campoApellido.getText();
        String provinciaIngresada = this.campoProvincia.getText();
        String localidadIngresada = this.campoLocalidad.getText();
        String calleIngresada = this.campoCalle.getText();
        String emailIngresado = this.campoEmail.getText();
        String telefonoIngresado = this.campoTelefono.getText();
        String cuilIngresado = this.campoCuil.getText();

        // Verificar si algún campo de texto está vacío
        if (nombreIngresado.isEmpty() || apellidoIngresado.isEmpty() || provinciaIngresada.isEmpty() || localidadIngresada.isEmpty() || calleIngresada.isEmpty() || emailIngresado.isEmpty() || telefonoIngresado.isEmpty()) {
            // Muestra un mensaje de error si falta ingresar datos en algún campo
            msj.mostrarError("Error", "", "Falta ingresar datos.");
        } else {
            // Realiza la operación de agregar un proveedor si todos los campos están completos
            String dniIngresado = cuilIngresado.substring(2, 10);
            Cliente c = new Cliente();
            ClienteDAOImpl dao = new ClienteDAOImpl();
            c.setNombre(nombreIngresado);
            c.setApellido(apellidoIngresado);
            c.setProvincia(provinciaIngresada);
            c.setLocalidad(localidadIngresada);
            c.setCalle(calleIngresada);
            c.setMail(emailIngresado);
            c.setTelefono(telefonoIngresado);
            c.setCuil(cuilIngresado);
            c.setDni(dniIngresado);
            try {
                dao.insertar(c);
                completarTabla();
                vaciarCampos();
                campoNombre.requestFocus();
                msj.mostrarAlertaInforme("Operación exitosa", "", "Se ha agregado el cliente correctamente.");
            } catch (Exception e) {
                e.printStackTrace(); // Imprime el stack trace de la excepción para depuración
                msj.mostrarError("Error", "", "Ocurrió un error al agregar el cliente.");
            }
        }*/
    }

    @FXML
    void accionBotonEliminar(ActionEvent event) {
        // Obtiene el cliente seleccionado en la tabla
        Cliente c = this.tablaClientes.getSelectionModel().getSelectedItem();
        if (c == null) {
            // Muestra un mensaje de error si no se selecciona ningún elemento en la tabla
            msj.mostrarError("Error", "", "Debe seleccionar un elemento de la lista");
        } else {
            try {
                ClienteDAOImpl dao = new ClienteDAOImpl();
                dao.eliminar(c);
                completarTabla();
                vaciarCampos();
                campoDni.requestFocus();
                // Para habilitar "Modificar" y "Eliminar" y deshabilitar "Agregar"
                manejador.configurarBotones(true);
                msj.mostrarAlertaInforme("Operacion exitosa", "", "El cliente se ha eliminado");
            } catch (Exception e) {
                msj.mostrarError("Error", "", "No se pudo eliminar el elemento de la BD");
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
        // Obtiene los valores de los campos
        Cliente cliente = obtenerValoresDeCampos();

        // Verifica si algún campo de texto está vacío
        if (cliente.getDni().isEmpty() || cliente.getNombre().isEmpty() || cliente.getApellido().isEmpty() || cliente.getProvincia().isEmpty() ||
                cliente.getLocalidad().isEmpty() || cliente.getCalle().isEmpty() || cliente.getCuil().isEmpty() ||
                cliente.getMail().isEmpty() || cliente.getTelefono().isEmpty()) {
            // Mostrar mensaje de error si falta ingresar datos
            msj.mostrarError("Error", "", "Falta ingresar datos.");
        } else {
            // Realiza las validaciones con ValidacionDeEntrada
            // Realiza las validaciones con ValidacionDeEntrada
            if (ValidacionDeEntrada.validarEmail(cliente.getMail()) &&
                    ValidacionDeEntrada.validarDNI(cliente.getDni()) &&
                    ValidacionDeEntrada.validarCuil(cliente.getCuil()) &&
                    ValidacionDeEntrada.validarTelefono(cliente.getTelefono())) {
                try {
                    // Actualiza los valores del proveedor
                    c.setNombre(cliente.getNombre());
                    c.setApellido(cliente.getApellido());
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
                    campoDni.requestFocus();
                    // Para habilitar "Modificar" y "Eliminar" y deshabilitar "Agregar"
                    manejador.configurarBotones(false);
                    msj.mostrarAlertaInforme("Operación exitosa", "", "El cliente se ha modificado");
                } catch (Exception e) {
                    msj.mostrarError("Error", "", "No se pudo agregar el cliente.");
                }
            }
        }
        /*
        // Obtiene el proveedor seleccionado en la tabla
        Cliente c = this.tablaClientes.getSelectionModel().getSelectedItem();

        if (c == null) {
            // Muestra un mensaje de error si no se selecciona ningún elemento en la tabla
            msj.mostrarError("Error", "", "Debe seleccionar un cliente de la lista para modificar.");
            return;
        }
        String nombreIngresado = this.campoNombre.getText().trim();
        String apellidoIngresado = this.campoApellido.getText();
        String provinciaIngresada = this.campoProvincia.getText();
        String localidadIngresada = this.campoLocalidad.getText();
        String calleIngresada = this.campoCalle.getText();
        String cuilIngresado = this.campoCuil.getText();
        String dniIngresado = cuilIngresado.substring(2, 10);
        String emailIngresado = this.campoEmail.getText();
        String telefonoIngresado = this.campoTelefono.getText();

        if (nombreIngresado.isEmpty()) {
            msj.mostrarError("Error", "", "Debe ingresar el nombre del proveedor.");
            return;
        }
        // Actualiza el nombre del rubro seleccionado con el contenido del campoNombre
        c.setNombre(nombreIngresado);
        c.setApellido(apellidoIngresado);
        c.setProvincia(provinciaIngresada);
        c.setLocalidad(localidadIngresada);
        c.setCalle(calleIngresada);
        c.setCuil(cuilIngresado);
        c.setDni(dniIngresado);
        c.setMail(emailIngresado);
        c.setTelefono(telefonoIngresado);
        try {
            ClienteDAOImpl dao = new ClienteDAOImpl();
            dao.modificar(c);
            completarTabla();
            vaciarCampos();
            campoNombre.requestFocus();
            // Para habilitar "Modificar" y "Eliminar" y deshabilitar "Agregar"
            manejador.configurarBotones(false);
            msj.mostrarAlertaInforme("Operación exitosa", "", "El cliente se ha modificado");
        } catch (Exception e) {
            msj.mostrarError("Error", "", "No se pudo modificar el elemento en la BD");
        }*/
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Carga la lista de provincias desde la clase ProvinciasArgentinas
        comboProvincia.setItems(ProvinciasArgentinas.getProvincias());
        comboProvincia.setValue("Chaco");
        // Llamado a completarTabla al inicializar el controlador
        completarTabla();

        todosLosClientes = tablaClientes.getItems();

        // Establecer el enfoque en campoNombre después de que la ventana se haya mostrado completamente
        Platform.runLater(() -> campoDni.requestFocus());

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
        campoCuil.setTextFormatter(ManejoDeEntrada.soloNumerosEnteros());
    }

    public void completarTabla() {
        campoNombre.requestFocus();

        // Crear una instancia del DAO de Proveedor
        ClienteDAOImpl cliente = new ClienteDAOImpl();
        ObservableList<Cliente> clientes = null;

        try {
            // se intenta obtener la lista de proveedores desde la base de datos
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
        this.colProv.setCellValueFactory(new PropertyValueFactory<>("provincia"));
        this.colLoc.setCellValueFactory(new PropertyValueFactory<>("localidad"));
        this.colCalle.setCellValueFactory(new PropertyValueFactory<>("calle"));
        this.colEmail.setCellValueFactory(new PropertyValueFactory<>("mail"));
        this.colTele.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        this.colCuil.setCellValueFactory(new PropertyValueFactory<>("cuil"));
        // Establece los datos de la tabla con la lista de proveedores
        this.tablaClientes.setItems(clientes);

        // Configura un listener para la selección de fila en la tabla
        tablaClientes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                manejador.configurarBotones(true);
                // Llena los campos de entrada con los datos del proveedor seleccionado
                campoNombre.setText(newValue.getNombre());
                campoApellido.setText(newValue.getApellido());
                comboProvincia.getSelectionModel().select(newValue.getProvincia());
                campoLocalidad.setText(newValue.getLocalidad());
                campoCalle.setText(newValue.getCalle());
                campoCuil.setText(newValue.getCuil());
                campoEmail.setText(newValue.getMail());
                campoTelefono.setText(newValue.getTelefono());
                campoDni.setText(newValue.getDni());
            }
        });
    }

    public void vaciarCampos() {
        // Limpiar los campos de entrada y foucs en nombre
        campoDni.setText("");
        campoNombre.setText("");
        campoApellido.setText("");
        comboProvincia.getSelectionModel().clearSelection();
        campoLocalidad.setText("");
        campoCalle.setText("");
        campoEmail.setText("");
        campoTelefono.setText("");
        campoBuscar.setText("");
        campoCuil.setText("");
        campoNombre.requestFocus();
        comboProvincia.setValue("Chaco");
    }

    @FXML
    void filtrarClientes(KeyEvent event) {
        // Obtener el texto ingresado en el campo de búsqueda
        String filtro = campoBuscar.getText().toLowerCase();

        if (filtro.isEmpty()) {
            // Si el campo de búsqueda está vacío, mostrar todos los empleados originales
            tablaClientes.setItems(todosLosClientes);
        } else {
            // Filtrar la lista de todos los empleados originales y mostrar los resultados
            ObservableList<Cliente> rubrosFiltrados = todosLosClientes.filtered(cliente
                    -> cliente.getNombre().toLowerCase().startsWith(filtro)
            );
            tablaClientes.setItems(rubrosFiltrados);
        }
    }

    @FXML
    void autoCompletarCampos(ActionEvent event) {
        PersonaDAOImpl p = new PersonaDAOImpl();
        ObservableList<Persona> personas = null;
        try {
            personas = p.listarTodos();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Persona persona : personas) {
            if (persona.getDni().equals(campoDni.getText())) {
                campoNombre.setText(persona.getNombre());
                campoApellido.setText(persona.getApellido());
                comboProvincia.setValue(persona.getProvincia());
                campoLocalidad.setText(persona.getLocalidad());
                campoCalle.setText(persona.getCalle());
                campoTelefono.setText(persona.getTelefono());
                campoEmail.setText(persona.getMail());
                campoNombre.requestFocus();
                return;
            } else {
                campoNombre.requestFocus();
            }
        }
    }



    private Cliente obtenerValoresDeCampos() {
        String nombreIngresado = FormatoTexto.formatearTexto(this.campoNombre.getText());
        String apellidoIngresado = FormatoTexto.formatearTexto(this.campoApellido.getText());
        String provinciaIngresada = this.comboProvincia.getSelectionModel().getSelectedItem();
        String localidadIngresada = FormatoTexto.formatearTexto(this.campoLocalidad.getText());
        String calleIngresada = FormatoTexto.formatearTexto(this.campoCalle.getText());
        String cuilIngresado = this.campoCuil.getText();
        String dniIngresado = campoDni.getText();
        String emailIngresado = this.campoEmail.getText().toLowerCase();
        String telefonoIngresado = this.campoTelefono.getText();

        Cliente cliente = new Cliente();
        cliente.setNombre(nombreIngresado);
        cliente.setApellido(apellidoIngresado);
        cliente.setProvincia(provinciaIngresada);
        cliente.setLocalidad(localidadIngresada);
        cliente.setCalle(calleIngresada);
        cliente.setCuil(cuilIngresado);
        cliente.setDni(dniIngresado);
        cliente.setMail(emailIngresado);
        cliente.setTelefono(telefonoIngresado);

        return cliente;
    }
}
