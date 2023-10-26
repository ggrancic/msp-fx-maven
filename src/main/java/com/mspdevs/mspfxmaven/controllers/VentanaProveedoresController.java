package com.mspdevs.mspfxmaven.controllers;

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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    void accionBotonAgregar(ActionEvent event) {
        // Obtener los valores de los campos en un objeto Proveedor
        Proveedor proveedor = obtenerValoresDeCampos();

        // Verifica si algún campo de texto está vacío
        if (proveedor.getDni().isEmpty() || proveedor.getNombre().isEmpty() || proveedor.getApellido().isEmpty() || proveedor.getProvincia().isEmpty() ||
                proveedor.getLocalidad().isEmpty() || proveedor.getCalle().isEmpty() || proveedor.getCuit().isEmpty() ||
                proveedor.getMail().isEmpty() || proveedor.getTelefono().isEmpty()) {
            // Mostrar mensaje de error si falta ingresar datos
            msj.mostrarError("Error", "", "Falta ingresar datos.");
        } else {
            // Realiza las validaciones con ValidacionDeEntrada
            if (ValidacionDeEntrada.validarEmail(proveedor.getMail()) &&
                    ValidacionDeEntrada.validarCuil(proveedor.getCuit()) &&
                    ValidacionDeEntrada.validarTelefono(proveedor.getTelefono())) {
                try {
                    ProveedorDAOImpl dao = new ProveedorDAOImpl();
                    dao.insertar(proveedor);
                    completarTabla();
                    vaciarCampos();
                    campoDni.requestFocus();
                    manejador.configurarBotones(false);
                    msj.mostrarAlertaInforme("Operación exitosa", "", "Se ha agregado el proveedor correctamente.");
                } catch (Exception e) {
                    msj.mostrarError("Error", "", "No se pudo agregar el proveedor a la BD");
                }
            }
        }
        /*
        // Obtiene los valores ingresados en los campos de texto
        String nombreIngresado = FormatoTexto.formatearTexto(this.campoNombre.getText());
        String apellidoIngresado = FormatoTexto.formatearTexto(this.campoApellido.getText());
        String provinciaIngresada = FormatoTexto.formatearTexto(this.campoProvincia.getText());
        String localidadIngresada = FormatoTexto.formatearTexto(this.campoLocalidad.getText());
        String calleIngresada = FormatoTexto.formatearTexto(this.campoCalle.getText());
        String cuitIngresado = this.campoCuit.getText();
        String dniIngresado = cuitIngresado.substring(2, 10);
        String emailIngresado = this.campoEmail.getText();
        String telefonoIngresado = this.campoTelefono.getText();

        // Verifica si algún campo de texto está vacío
        if (nombreIngresado.isEmpty() || apellidoIngresado.isEmpty() || provinciaIngresada.isEmpty() ||
                localidadIngresada.isEmpty() || calleIngresada.isEmpty() || cuitIngresado.isEmpty() ||
                emailIngresado.isEmpty() || telefonoIngresado.isEmpty()) {
            // Mostrar mensaje de error si falta ingresar datos
            msj.mostrarError("Error", "", "Falta ingresar datos.");
        } else {
            // Realiza las validaciones con ValidacionDeEntrada
            if (ValidacionDeEntrada.validarEmail(emailIngresado) &&
                    ValidacionDeEntrada.validarCuil(cuitIngresado) &&
                    ValidacionDeEntrada.validarTelefono(telefonoIngresado)) {
                // Realiza la operación de agregar un proveedor si todas las validaciones son validas
                Proveedor p = new Proveedor();
                ProveedorDAOImpl dao = new ProveedorDAOImpl();
                p.setNombre(nombreIngresado);
                p.setApellido(apellidoIngresado);
                p.setProvincia(provinciaIngresada);
                p.setLocalidad(localidadIngresada);
                p.setCalle(calleIngresada);
                p.setCuit(cuitIngresado);
                p.setDni(dniIngresado);
                p.setMail(emailIngresado);
                p.setTelefono(telefonoIngresado);
                try {
                    dao.insertar(p);
                    completarTabla();
                    vaciarCampos();
                    campoNombre.requestFocus();
                    msj.mostrarAlertaInforme("Operación exitosa", "", "Se ha agregado el proveedor correctamente.");
                } catch (Exception e) {
                    e.printStackTrace(); // Imprime el stack trace de la excepción para depuración
                    msj.mostrarError("Error", "", "Ocurrió un error al agregar el proveedor.");
                }
            }
        }*/
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

        if (p == null) {
            // Muestra un mensaje de error si no se selecciona ningún elemento en la tabla
            msj.mostrarError("Error", "", "Debe seleccionar un rubro de la lista para modificar.");
            return;
        }

        // Obtiene los valores de los campos
        Proveedor proveedor = obtenerValoresDeCampos();

        // Verifica si algún campo de texto está vacío
        if (proveedor.getDni().isEmpty() || proveedor.getNombre().isEmpty() || proveedor.getApellido().isEmpty() || proveedor.getProvincia().isEmpty() ||
                proveedor.getLocalidad().isEmpty() || proveedor.getCalle().isEmpty() || proveedor.getCuit().isEmpty() ||
                proveedor.getMail().isEmpty() || proveedor.getTelefono().isEmpty()) {
            // Mostrar mensaje de error si falta ingresar datos
            msj.mostrarError("Error", "", "Falta ingresar datos.");
        } else {
            // Realiza las validaciones con ValidacionDeEntrada
            if (ValidacionDeEntrada.validarEmail(proveedor.getMail()) &&
                    ValidacionDeEntrada.validarDNI(proveedor.getDni()) &&
                    ValidacionDeEntrada.validarCuil(proveedor.getCuit()) &&
                    ValidacionDeEntrada.validarTelefono(proveedor.getTelefono())) {
                // Mostrar una ventana de confirmación
                boolean confirmacion = msj.mostrarConfirmacion("Confirmar Modificación", "",
                        "¿Está seguro de que desea modificar este proveedor?");
                if (confirmacion) { // Si se confirma la modificación
                    try {// Actualiza los valores del proveedor
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
                        campoDni.requestFocus();
                        manejador.configurarBotones(false);
                        msj.mostrarAlertaInforme("Operación exitosa", "", "El proveedor se ha modificado");
                    } catch (Exception e) {
                        msj.mostrarError("Error", "", "No se pudo modificar el elemento en la BD");
                    }
                }
            }
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
                //campoRazonSocial.setText(proveedor);
                comboProvincia.setValue(persona.getProvincia());
                campoLocalidad.setText(persona.getLocalidad());
                campoCalle.setText(persona.getCalle());
                campoTelefono.setText(persona.getTelefono());
                campoEmail.setText(persona.getMail());
                campoCuit.requestFocus();
                return;
            } else {
                campoCuit.requestFocus();
            }
        }
    }

    @FXML
    void filtrarProveedores(KeyEvent event) {
        // Obtener el texto ingresado en el campo de búsqueda
        String filtro = buscarCampo.getText().toLowerCase();

        if (filtro.isEmpty()) {
            // Si el campo de búsqueda está vacío, mostrar todos los empleados originales
            tablaProveedores.setItems(todosLosProveedores);
        } else {
            // Filtrar la lista de todos los empleados originales y mostrar los resultados
            ObservableList<Proveedor> rubrosFiltrados = todosLosProveedores.filtered(proveedor
                    -> proveedor.getNombre().toLowerCase().startsWith(filtro)
            );
            tablaProveedores.setItems(rubrosFiltrados);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboProvincia.setValue("Chaco");
        // Llamado a completarTabla al inicializar el controlador
        completarTabla();

        // Carga la lista de provincias desde la clase ProvinciasArgentinas
        comboProvincia.setItems(ProvinciasArgentinas.getProvincias());

        todosLosProveedores = tablaProveedores.getItems();

        // Establecer el enfoque en campoNombre después de que la ventana se haya mostrado completamente
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
    }
    public void completarTabla() {
        // Crear una instancia del DAO de Proveedor
        ProveedorDAOImpl proveedor = new ProveedorDAOImpl();
        ObservableList<Proveedor> proveedores = null;

        try {
            // se intenta obtener la lista de proveedores desde la base de datos
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
                campoDni.setText(newValue.getDni());
                campoEmail.setText(newValue.getMail());
                campoTelefono.setText(newValue.getTelefono());
            }
        });
    }

    public void vaciarCampos() {
        // Limpiar los campos de entrada y foucs en nombre
        campoNombre.setText("");
        campoApellido.setText("");
        campoRazonSocial.setText("");
        comboProvincia.getSelectionModel().clearSelection();
        campoLocalidad.setText("");
        campoCalle.setText("");
        campoEmail.setText("");
        campoTelefono.setText("");
        campoCuit.setText("");
        campoDni.setText("");
        buscarCampo.setText("");
        campoNombre.requestFocus();
        comboProvincia.setValue("Chaco");
    }

    private Proveedor obtenerValoresDeCampos() {
        String nombreIngresado = FormatoTexto.formatearTexto(this.campoNombre.getText());
        String apellidoIngresado = FormatoTexto.formatearTexto(this.campoApellido.getText());
        String razonSocialIngresada = FormatoTexto.formatearTexto(this.campoRazonSocial.getText());
        String provinciaIngresada = this.comboProvincia.getSelectionModel().getSelectedItem();
        String localidadIngresada = FormatoTexto.formatearTexto(this.campoLocalidad.getText());
        String calleIngresada = FormatoTexto.formatearTexto(this.campoCalle.getText());
        String cuitIngresado = this.campoCuit.getText();
        String dniIngresado = this.campoDni.getText();
        String emailIngresado = this.campoEmail.getText().toLowerCase();;
        if (emailIngresado != null && !emailIngresado.isEmpty()) {
            emailIngresado.toLowerCase();
            // Realiza las operaciones con lowerCaseText
        } else {
            emailIngresado = "";
        }
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
}


