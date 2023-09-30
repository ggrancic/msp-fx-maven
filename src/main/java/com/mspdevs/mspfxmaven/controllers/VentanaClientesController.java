package com.mspdevs.mspfxmaven.controllers;

import com.mspdevs.mspfxmaven.model.Cliente;
import com.mspdevs.mspfxmaven.model.DAO.ClienteDAOImpl;
import com.mspdevs.mspfxmaven.model.DAO.ProveedorDAOImpl;
import com.mspdevs.mspfxmaven.model.DAO.RubroDAOImpl;
import com.mspdevs.mspfxmaven.model.Persona;
import com.mspdevs.mspfxmaven.model.Proveedor;
import com.mspdevs.mspfxmaven.model.Rubro;
import com.mspdevs.mspfxmaven.utils.Alerta;
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
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class VentanaClientesController implements Initializable {
    Alerta msj = new Alerta();

    private ObservableList<Cliente> todosLosClientes;

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
    private TextField campoLocalidad;

    @FXML
    private TextField campoNombre;

    @FXML
    private TextField campoProvincia;

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
                msj.mostrarAlertaInforme("Operación exitosa", "", "Se ha agregado el cliente correctamente.");
            } catch (Exception e) {
                e.printStackTrace(); // Imprime el stack trace de la excepción para depuración
                msj.mostrarError("Error", "", "Ocurrió un error al agregar el cliente.");
            }
        }

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
        // Deselecciona la fila en la tabla
        tablaClientes.getSelectionModel().clearSelection(); //
    }

    @FXML
    void accionBotonModificar(ActionEvent event) {
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
        c.setMail(emailIngresado);
        c.setTelefono(telefonoIngresado);
        try {
            ClienteDAOImpl dao = new ClienteDAOImpl();
            dao.modificar(c);
            completarTabla();
            vaciarCampos();
            msj.mostrarAlertaInforme("Operación exitosa", "", "El cliente se ha modificado");
        } catch (Exception e) {
            msj.mostrarError("Error", "", "No se pudo modificar el elemento en la BD");
        }
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


    public void completarTabla() {
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
                // Llena los campos de entrada con los datos del proveedor seleccionado
                campoNombre.setText(newValue.getNombre());
                campoApellido.setText(newValue.getApellido());
                campoProvincia.setText(newValue.getProvincia());
                campoLocalidad.setText(newValue.getLocalidad());
                campoCalle.setText(newValue.getCalle());
                //campoCuit.setText(newValue.getCuit());
                campoEmail.setText(newValue.getMail());
                campoTelefono.setText(newValue.getTelefono());

            }
        });
    }






    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /*double colIdCustomWidth = tablaProveedores.getWidth() * 0.01;
        colId.setMinWidth(colIdCustomWidth);

        double colNomCustomWidth = tablaProveedores.getWidth() * 0.7;
        colNom.setMinWidth(colNomCustomWidth);*/

        // Llamado a completarTabla al inicializar el controlador
        completarTabla();

        todosLosClientes = tablaClientes.getItems();
    }

    public void vaciarCampos() {
        // Limpiar los campos de entrada y foucs en nombre
        campoNombre.setText("");
        campoApellido.setText("");
        campoProvincia.setText("");
        campoLocalidad.setText("");
        campoCalle.setText("");
        campoEmail.setText("");
        campoTelefono.setText("");
        campoBuscar.setText("");
        campoCuil.setText("");
        campoNombre.requestFocus();
    }
}
