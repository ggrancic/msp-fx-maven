package com.mspdevs.mspfxmaven.controllers;



import com.mspdevs.mspfxmaven.model.*;
import com.mspdevs.mspfxmaven.model.DAO.*;
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

public class ModalNuevoClienteController implements Initializable {
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
    private TextField campoCuil;

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
        Cliente cliente = obtenerValoresDeCampos();

        // Verifica si algún campo de texto está vacío
        if (cliente.getNombre().isEmpty() || cliente.getApellido().isEmpty() || cliente.getProvincia().isEmpty() ||
                cliente.getLocalidad().isEmpty() || cliente.getCalle().isEmpty() || cliente.getCuil().isEmpty() ||
                cliente.getMail().isEmpty() || cliente.getDni().isEmpty() || cliente.getTelefono().isEmpty()) {
            // Mostrar mensaje de error si falta ingresar datos
            msj.mostrarError("Error", "", "Falta ingresar datos.");
        } else {
            // Realiza las validaciones con ValidacionDeEntrada
            if (ValidacionDeEntrada.validarCuil(cliente.getCuil()) &&
                    ValidacionDeEntrada.validarEmail(cliente.getMail()) &&
                    ValidacionDeEntrada.validarDNI(cliente.getDni()) &&
                    ValidacionDeEntrada.validarTelefono(cliente.getTelefono())) {

                try {
                    ClienteDAOImpl dao = new ClienteDAOImpl();
                    dao.insertar(cliente);
                    msj.mostrarAlertaInforme("Operación exitosa", "", "Se ha agregado el cliente correctamente.");
                    cerrarVentanaModal(event);
                } catch (Exception e) {
                    msj.mostrarError("Error", "", "No se pudo agregar el cliente a la BD");
                }
            }
        }*/


        // Comprueba si se ha seleccionado un Toggle
        if (!personaRB.isSelected() && !empresaRB.isSelected()) {
            msj.mostrarError("Error", "", "Debe seleccionar una opción: Persona o Empresa.");
        } else {
            // Comprueba si se seleccionó personaRadio
            if (personaRB.isSelected()) {
                // Obtiene los valores de los campos en un objeto Cliente
                Cliente cliente = obtenerValoresDeCampos();

                // Realiza las validaciones
                if (cliente.getDni().isEmpty() || cliente.getNombre().isEmpty() || cliente.getApellido().isEmpty() ||
                        cliente.getProvincia().isEmpty() || cliente.getLocalidad().isEmpty() ||
                        cliente.getCuil().isEmpty() || cliente.getMail().isEmpty() || cliente.getTelefono().isEmpty()) {
                    // Muestra mensaje de error si falta ingresar datos
                    msj.mostrarError("Error", "", "Falta ingresar datos obligatorios.");
                } else {
                    // Ajustar la Razón Social si está vacía
                    if (cliente.getRazonSocial() == null) {
                        cliente.setRazonSocial(cliente.getNombre() + " " + cliente.getApellido());
                    } else {
                        cliente.setRazonSocial(cliente.getRazonSocial());
                    }
                    // Realizar las validaciones específicas para personas
                    if (ValidacionDeEntrada.validarEmail(cliente.getMail()) &&
                            ValidacionDeEntrada.validarDNI(cliente.getDni()) &&
                            ValidacionDeEntrada.validarCuil(cliente.getCuil()) &&
                            ValidacionDeEntrada.validarTelefono(cliente.getTelefono())) {
                        try {
                            ClienteDAOImpl dao = new ClienteDAOImpl();
                            dao.insertar(cliente);
                            vaciarCampos();
                            msj.mostrarAlertaInforme("Operación exitosa", "", "Se ha agregado el cliente correctamente.");
                        } catch (Exception e) {
                            msj.mostrarError("Error", "", "No se pudo agregar el cliente.");
                            e.printStackTrace();
                        }
                    }
                }
            }
            // Realiza las validaciones
            if (empresaRB.isSelected()) {
                // Obtener los valores de los campos en un objeto Cliente
                Cliente cliente = obtenerValoresDeCamposEmpresa();

                if (cliente.getRazonSocial().isEmpty() || cliente.getProvincia().isEmpty() || cliente.getLocalidad().isEmpty() ||
                        cliente.getCuil().isEmpty() || cliente.getMail().isEmpty() || cliente.getTelefono().isEmpty()) {
                    // Muestra mensaje de error si falta ingresar datos
                    msj.mostrarError("Error", "", "Falta ingresar datos obligatorios.");
                } else {
                    if (ValidacionDeEntrada.validarEmail(cliente.getMail()) &&
                            ValidacionDeEntrada.validarCuil(cliente.getCuil()) &&
                            ValidacionDeEntrada.validarTelefono(cliente.getTelefono())) {
                        try {
                            ClienteDAOImpl dao = new ClienteDAOImpl();
                            dao.insertar(cliente);
                            vaciarCampos();
                            msj.mostrarAlertaInforme("Operación exitosa", "", "Se ha agregado el cliente correctamente.");
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
                "¿Está seguro de que no quiere agregar un nuevo cliente?");
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
                campoCuil.setText(persona.getCuil());
                campoNombre.setText(persona.getNombre());
                campoApellido.setText(persona.getApellido());
                campoRazonSocial.setText(persona.getRazonSocial());
                campoProvincia.setValue(persona.getProvincia());
                campoLocalidad.setText(persona.getLocalidad());
                campoCalle.setText(persona.getCalle());
                campoTelefono.setText(persona.getTelefono());
                campoEmail.setText(persona.getMail());
                return;
            } else {
                campoCuil.requestFocus();
            }
        }*/
    }

    @FXML
    void autoCompletarCamposPorCuil(ActionEvent event) {
        /*
        PersonaDAOImpl p = new PersonaDAOImpl();
        ObservableList<Persona> personas = null;
        try {
            personas = p.listarTodos();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Persona persona : personas) {
            if (persona.getCuil().equals(campoCuil.getText())) {
                campoRazonSocial.setText(persona.getRazonSocial());
                campoProvincia.setValue(persona.getProvincia());
                campoLocalidad.setText(persona.getLocalidad());
                campoCalle.setText(persona.getCalle());
                campoTelefono.setText(persona.getTelefono());
                campoEmail.setText(persona.getMail());
                return;
            }
        }
        campoRazonSocial.requestFocus();*/
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Llama al método para deshabilitar todos los campos de texto al inicializar
        deshabilitarCamposTexto();

        // Carga la lista de provincias desde la clase ProvinciasArgentinas
        campoProvincia.setItems(ProvinciasArgentinas.getProvincias());

        campoProvincia.setValue("Chaco");
        // Establecer el enfoque en campoNombre después de que la ventana se haya mostrado completamente
        //Platform.runLater(() -> campoDni.requestFocus());

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
            if (newValue == personaRB) {
                // Habilita campos relacionados con personas
                campoDni.setDisable(false);
                campoCuil.setDisable(false);
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
                // Habilita campos relacionados con empresas
                campoDni.setDisable(true);
                campoCuil.setDisable(false);
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
                campoCuil.requestFocus();
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

    private void deshabilitarCamposTexto() {
        campoDni.setDisable(true);
        campoCuil.setDisable(true);
        campoNombre.setDisable(true);
        campoApellido.setDisable(true);
        campoRazonSocial.setDisable(true);
        campoEmail.setDisable(true);
        campoCalle.setDisable(true);
        campoTelefono.setDisable(true);
        campoProvincia.setDisable(true);
        campoLocalidad.setDisable(true);
    }

    private Cliente obtenerValoresDeCampos() {
        String nombreIngresado = FormatoTexto.formatearTexto(this.campoNombre.getText());
        String apellidoIngresado = FormatoTexto.formatearTexto(this.campoApellido.getText());
        String provinciaIngresada = this.campoProvincia.getSelectionModel().getSelectedItem();
        String localidadIngresada = FormatoTexto.formatearTexto(this.campoLocalidad.getText());
        String calleIngresada = FormatoTexto.formatearTexto(this.campoCalle.getText());
        String cuitIngresado = this.campoCuil.getText();
        String dniIngresado = this.campoDni.getText();
        String telefonoIngresado = this.campoTelefono.getText();
        String emailIngresado = this.campoEmail.getText();
        if (emailIngresado != null && !emailIngresado.isEmpty()) {
            emailIngresado.toLowerCase();
            // Realiza las operaciones con lowerCaseText
        } else {
            emailIngresado = "";
        }

        Cliente cliente = new Cliente();
        cliente.setNombre(nombreIngresado);
        cliente.setApellido(apellidoIngresado);
        cliente.setProvincia(provinciaIngresada);
        cliente.setLocalidad(localidadIngresada);
        cliente.setCalle(calleIngresada);
        cliente.setCuil(cuitIngresado);
        cliente.setDni(dniIngresado);
        cliente.setTelefono(telefonoIngresado);
        cliente.setMail(emailIngresado);

        return cliente;
    }

    private Cliente obtenerValoresDeCamposEmpresa() {
        String razonSocialIngresada = FormatoTexto.formatearTexto(campoRazonSocial.getText());
        String provinciaIngresada = this.campoProvincia.getSelectionModel().getSelectedItem();
        String localidadIngresada = FormatoTexto.formatearTexto(this.campoLocalidad.getText());
        String calleIngresada = FormatoTexto.formatearTexto(this.campoCalle.getText());
        String cuilIngresado = this.campoCuil.getText();
        String emailIngresado = this.campoEmail.getText().toLowerCase();
        String telefonoIngresado = this.campoTelefono.getText();

        Cliente cliente = new Cliente();
        //cliente.setNombre(nombreIngresado);
        //cliente.setApellido(apellidoIngresado);
        cliente.setProvincia(provinciaIngresada);
        cliente.setLocalidad(localidadIngresada);
        cliente.setCalle(calleIngresada);
        cliente.setCuil(cuilIngresado);
        //cliente.setDni(dniIngresado);
        cliente.setRazonSocial(razonSocialIngresada);
        cliente.setMail(emailIngresado);
        cliente.setTelefono(telefonoIngresado);

        return cliente;
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
        campoCuil.setText("");
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
}
