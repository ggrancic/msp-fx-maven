package com.mspdevs.mspfxmaven.controllers;

import com.mspdevs.mspfxmaven.model.DAO.PersonaDAOImpl;
import com.mspdevs.mspfxmaven.model.DAO.ProductoDAOImpl;
import com.mspdevs.mspfxmaven.model.DAO.ProveedorDAOImpl;
import com.mspdevs.mspfxmaven.model.DAO.RubroDAOImpl;
import com.mspdevs.mspfxmaven.model.Persona;
import com.mspdevs.mspfxmaven.model.Producto;
import com.mspdevs.mspfxmaven.model.Proveedor;
import com.mspdevs.mspfxmaven.model.Rubro;
import com.mspdevs.mspfxmaven.utils.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.SearchableComboBox;

public class ModalNuevoProveedorController implements Initializable {
    Alerta msj = new Alerta();

    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnCancelar;

    @FXML
    private TextField campoApellido;

    @FXML
    private TextField campoCalle;

    @FXML
    private TextField campoCuit;

    @FXML
    private TextField campoDni;

    @FXML
    private TextField campoEmail;

    @FXML
    private TextField campoLocalidad;

    @FXML
    private TextField campoNombre;

    @FXML
    private SearchableComboBox<String> campoProvincia;

    @FXML
    private TextField campoRazonSocial;

    @FXML
    private TextField campoTelefono;

    @FXML
    private VBox contenedor;

    @FXML
    private RadioButton empresaRB;

    @FXML
    private RadioButton personaRB;

    @FXML
    private ToggleGroup seleccionarTipo;


    @FXML
    void accionBotonAgregar(ActionEvent event) {
        /*
        // Obtener los valores de los campos en un objeto Proveedor
        Proveedor proveedor = obtenerValoresDeCampos();

        // Verifica si algún campo de texto está vacío
        if (proveedor.getNombre().isEmpty() || proveedor.getApellido().isEmpty() || proveedor.getProvincia().isEmpty() ||
                proveedor.getLocalidad().isEmpty() || proveedor.getCalle().isEmpty() || proveedor.getCuit().isEmpty() ||
                proveedor.getDni().isEmpty() || proveedor.getTelefono().isEmpty()) {
            // Mostrar mensaje de error si falta ingresar datos
            msj.mostrarError("Error", "", "Falta ingresar datos.");
        } else {
            // Realiza las validaciones con ValidacionDeEntrada
            if (ValidacionDeEntrada.validarCuil(proveedor.getCuit()) &&
                    ValidacionDeEntrada.validarDNI(proveedor.getDni()) &&
                    ValidacionDeEntrada.validarTelefono(proveedor.getTelefono())) {

                try {
                    ProveedorDAOImpl dao = new ProveedorDAOImpl();
                    dao.insertar(proveedor);
                    msj.mostrarAlertaInforme("Operación exitosa", "", "Se ha agregado el proveedor correctamente.");
                    cerrarVentanaModal(event);
                } catch (Exception e) {
                    msj.mostrarError("Error", "", "No se pudo agregar el proveedor a la BD");
                }
            }
        }*/

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
                    if (proveedor.getRazonSocial() == null) {
                        proveedor.setRazonSocial(proveedor.getNombre() + " " + proveedor.getApellido());
                    } else {
                        proveedor.setRazonSocial(proveedor.getRazonSocial());
                    }
                    // Realiza las validaciones
                    if (ValidacionDeEntrada.validarEmail(proveedor.getMail()) &&
                            ValidacionDeEntrada.validarCuil(proveedor.getCuit()) &&
                            ValidacionDeEntrada.validarDNI(proveedor.getDni()) &&
                            ValidacionDeEntrada.validarTelefono(proveedor.getTelefono())) {
                        try {
                            ProveedorDAOImpl dao = new ProveedorDAOImpl();
                            dao.insertar(proveedor);
                            vaciarCampos();
                            msj.mostrarAlertaInforme("Operación exitosa", "", "Se ha agregado el proveedor correctamente.");
                        } catch (Exception e) {
                            msj.mostrarError("Error", "", "No se pudo agregar el proveedor.");
                            e.printStackTrace();
                        }
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
                    if (ValidacionDeEntrada.validarEmail(proveedor.getMail()) &&
                            ValidacionDeEntrada.validarCuil(proveedor.getCuit()) &&
                            ValidacionDeEntrada.validarTelefono(proveedor.getTelefono())) {
                        try {
                            ProveedorDAOImpl dao = new ProveedorDAOImpl();
                            dao.insertar(proveedor);
                            vaciarCampos();
                            msj.mostrarAlertaInforme("Operación exitosa", "", "Se ha agregado el proveedor correctamente.");
                        } catch (Exception e) {
                            msj.mostrarError("Error", "", "No se pudo agregar el cliente.");
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @FXML
    void accionBotonCancelar(ActionEvent event) {
        // Crea un cuadro de diálogo de confirmación
        boolean confirmado = msj.mostrarConfirmacion("Confirmación", "",
                "¿Está seguro de que no desea agregar un nuevo proveedor?");
        if (confirmado) {
            cerrarVentanaModal(event);
        }
    }

    @FXML
    void autoCompletarCampos(ActionEvent event) {
        /*
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
                campoProvincia.setValue(persona.getProvincia());
                campoLocalidad.setText(persona.getLocalidad());
                campoCalle.setText(persona.getCalle());
                campoTelefono.setText(persona.getTelefono());
                campoEmail.setText(persona.getMail());
                campoCuit.requestFocus();
                return;
            } else {
                campoCuit.requestFocus();
            }
        }*/
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Llama al método para deshabilitar todos los campos de texto al inicializar
        deshabilitarCamposTexto();

        campoProvincia.setValue("Chaco");
        // Carga la lista de provincias desde la clase ProvinciasArgentinas
        campoProvincia.setItems(ProvinciasArgentinas.getProvincias());
        // Establecer el enfoque en campoNombre después de que la ventana se haya mostrado completamente
        //Platform.runLater(() -> campoDni.requestFocus());

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
                campoProvincia.setDisable(false);
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
                campoProvincia.setDisable(false);
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

    private void cerrarVentanaModal(ActionEvent event) {
        // Obtén la referencia a la ventana actual
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        // Cierra la ventana
        stage.close();
    }

    public void vaciarCampos() {
        // Limpia los campos de entrada y foucs en nombre
        campoDni.setText("");
        campoNombre.setText("");
        campoApellido.setText("");
        campoRazonSocial.setText("");
        campoProvincia.getSelectionModel().clearSelection();
        campoLocalidad.setText("");
        campoCalle.setText("");
        campoEmail.setText("");
        campoTelefono.setText("");
        //buscarCampo.setText("");
        campoCuit.setText("");
        //campoNombre.requestFocus();
        campoProvincia.setValue("Chaco");
        // Deselecciona los Toggle después del guardado
        personaRB.setSelected(false);
        empresaRB.setSelected(false);
        personaRB.setDisable(false);
        empresaRB.setDisable(false);
        // Deshabilita todos los campos de texto
        deshabilitarCamposTexto();
    }

    private Proveedor obtenerValoresDeCampos() {
        String nombreIngresado = FormatoTexto.formatearTexto(this.campoNombre.getText());
        String apellidoIngresado = FormatoTexto.formatearTexto(this.campoApellido.getText());
        String provinciaIngresada = this.campoProvincia.getSelectionModel().getSelectedItem();
        String localidadIngresada = FormatoTexto.formatearTexto(this.campoLocalidad.getText());
        String calleIngresada = FormatoTexto.formatearTexto(this.campoCalle.getText());
        String cuitIngresado = this.campoCuit.getText();
        String dniIngresado = this.campoDni.getText();
        String telefonoIngresado = this.campoTelefono.getText();
        String emailIngresado = this.campoEmail.getText();
        if (emailIngresado != null && !emailIngresado.isEmpty()) {
            emailIngresado.toLowerCase();
            // Realiza las operaciones con lowerCaseText
        } else {
            emailIngresado = "";
        }

        Proveedor proveedor = new Proveedor();
        proveedor.setNombre(nombreIngresado);
        proveedor.setApellido(apellidoIngresado);
        proveedor.setProvincia(provinciaIngresada);
        proveedor.setLocalidad(localidadIngresada);
        proveedor.setCalle(calleIngresada);
        proveedor.setCuit(cuitIngresado);
        proveedor.setDni(dniIngresado);
        proveedor.setTelefono(telefonoIngresado);
        proveedor.setMail(emailIngresado);

        return proveedor;
    }

    private Proveedor obtenerValoresDeCamposEmpresa() {
        String razonSocialIngresada = FormatoTexto.formatearTexto(campoRazonSocial.getText());
        String provinciaIngresada = this.campoProvincia.getSelectionModel().getSelectedItem();
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
        campoProvincia.setDisable(true);
        campoLocalidad.setDisable(true);
    }
}