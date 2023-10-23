package com.mspdevs.mspfxmaven.controllers;



import com.mspdevs.mspfxmaven.model.*;
import com.mspdevs.mspfxmaven.model.DAO.*;
import com.mspdevs.mspfxmaven.utils.Alerta;
import com.mspdevs.mspfxmaven.utils.FormatoTexto;
import com.mspdevs.mspfxmaven.utils.ManejoDeEntrada;
import com.mspdevs.mspfxmaven.utils.ValidacionDeEntrada;
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
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
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
    private TextField campoProvincia;

    @FXML
    private TextField campoTelefono;
    @FXML
    private VBox contenedor;

    @FXML
    void accionBotonAgregar(ActionEvent event) {
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
                campoProvincia.setText(persona.getProvincia());
                campoLocalidad.setText(persona.getLocalidad());
                campoCalle.setText(persona.getCalle());
                campoTelefono.setText(persona.getTelefono());
                campoEmail.setText(persona.getMail());
                return;
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Establecer el enfoque en campoNombre después de que la ventana se haya mostrado completamente
        Platform.runLater(() -> campoDni.requestFocus());

        // Aplica el TextFormatter a los campos
        campoNombre.setTextFormatter(ManejoDeEntrada.soloLetrasEspacioAcento());
        campoApellido.setTextFormatter(ManejoDeEntrada.soloLetrasEspacioAcento());
        campoCalle.setTextFormatter(ManejoDeEntrada.soloLetrasNumEspAcento());
        campoTelefono.setTextFormatter(ManejoDeEntrada.soloTelefono());
        campoProvincia.setTextFormatter(ManejoDeEntrada.soloLetrasEspacioAcento());
        campoLocalidad.setTextFormatter(ManejoDeEntrada.soloLetrasEspacioAcento());
        campoDni.setTextFormatter(ManejoDeEntrada.soloDni());
        campoCuil.setTextFormatter(ManejoDeEntrada.soloNumerosEnteros());
        campoEmail.setTextFormatter(ManejoDeEntrada.soloEmail());
    }

    private void cerrarVentanaModal(ActionEvent event) {
        // Obtén la referencia a la ventana actual
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        // Cierra la ventana
        stage.close();
    }

    private Cliente obtenerValoresDeCampos() {
        String nombreIngresado = FormatoTexto.formatearTexto(this.campoNombre.getText());
        String apellidoIngresado = FormatoTexto.formatearTexto(this.campoApellido.getText());
        String provinciaIngresada = FormatoTexto.formatearTexto(this.campoProvincia.getText());
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
}
