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
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
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
    private TextField campoLocalidad;

    @FXML
    private TextField campoNombre;

    @FXML
    private SearchableComboBox<String> campoProvincia;

    @FXML
    private TextField campoTelefono;

    @FXML
    private TextField campoEmail;

    @FXML
    private VBox contenedor;

    @FXML
    void accionBotonAgregar(ActionEvent event) {
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
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        campoProvincia.setValue("Chaco");
        // Carga la lista de provincias desde la clase ProvinciasArgentinas
        campoProvincia.setItems(ProvinciasArgentinas.getProvincias());
        // Establecer el enfoque en campoNombre después de que la ventana se haya mostrado completamente
        Platform.runLater(() -> campoDni.requestFocus());

        // Aplica el TextFormatter a los campos
        campoNombre.setTextFormatter(ManejoDeEntrada.soloLetrasEspacioAcento());
        campoApellido.setTextFormatter(ManejoDeEntrada.soloLetrasEspacioAcento());
        campoCalle.setTextFormatter(ManejoDeEntrada.soloLetrasNumEspAcento());
        campoTelefono.setTextFormatter(ManejoDeEntrada.soloTelefono());
        //campoProvincia.setTextFormatter(ManejoDeEntrada.soloLetrasEspacioAcento());
        campoLocalidad.setTextFormatter(ManejoDeEntrada.soloLetrasEspacioAcento());
        campoDni.setTextFormatter(ManejoDeEntrada.soloDni());
        campoCuit.setTextFormatter(ManejoDeEntrada.soloNumerosEnteros());
        campoEmail.setTextFormatter(ManejoDeEntrada.soloEmail());

    }

    private void cerrarVentanaModal(ActionEvent event) {
        // Obtén la referencia a la ventana actual
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        // Cierra la ventana
        stage.close();
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
}