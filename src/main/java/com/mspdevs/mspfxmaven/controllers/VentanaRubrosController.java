package com.mspdevs.mspfxmaven.controllers;

import com.mspdevs.mspfxmaven.model.ConexionMySQL;
import com.mspdevs.mspfxmaven.model.Rubro;
import com.mspdevs.mspfxmaven.model.Rubro1;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

import javax.swing.*;
import java.sql.*;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.TableRow;

public class VentanaRubrosController extends ConexionMySQL implements Initializable {
    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnLimpiar;

    @FXML
    private Button btnModificar;

    @FXML
    private TextField nombreTxt;

    @FXML
    private TableColumn<Rubro1, String> IDColumna;

    @FXML
    private TableColumn<Rubro1, String> NOMBREColumna;

    @FXML
    private TableView<Rubro1> Tabla;

    Connection con;
    PreparedStatement pst;

    int myIndex;
    int id;

    private TreeMap<String, Rubro> listaHabitacion = null;


    @FXML
    void agregarRubro(ActionEvent event) {

        // Crea una instancia de la clase ConexionMySQL para gestionar la conexión a la base de datos
        ConexionMySQL conexionMySQL = new ConexionMySQL();

        // Establece la conexión a la base de datos
        conexionMySQL.conectar();
        Connection conexion = conexionMySQL.getCon();

        String nombreIngresado = nombreTxt.getText();

        /*if (nombre.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Campo Vacío");
            alert.setContentText("El campo de nombre no puede estar vacío.");
            alert.showAndWait();
            return; // Sale del método si el campo está vacío
        }*/

        if (!esNombreValido(nombreIngresado)) {
            mostrarAlerta("Advertencia", "Campo Vacío", "El campo de nombre no puede estar vacío.");
            Tabla.getSelectionModel().clearSelection();
            return; // Sale del método si el campo está vacío
        }

        // Toma el valor ingresado en "nombreIngresado", convierte la primera letra en mayúscula
        // y el resto en minúscula para formatear el nombre de manera consistente
        String nombre = nombreIngresado.substring(0, 1).toUpperCase() + nombreIngresado.substring(1).toLowerCase();

        try
        {
            // Consulta SQL para verificar si el nombre ya existe
            String consultaExistencia = "SELECT COUNT(*) FROM rubros WHERE nombre = ?";
            PreparedStatement statementExistencia = conexion.prepareStatement(consultaExistencia);
            statementExistencia.setString(1, nombre);

            ResultSet rsExistencia = statementExistencia.executeQuery();
            rsExistencia.next();
            int cantidadRegistros = rsExistencia.getInt(1);

            if (cantidadRegistros > 0) {
                // Si ya existe un registro con el mismo nombre, muestra una alerta de advertencia
                mostrarAlerta("Advertencia", "Nombre duplicado", "El nombre ya existe en la base de datos.");
                return;
            }
            // Prepara la consulta
            String consulta = "insert into rubros(nombre)values(?)";
            PreparedStatement statement = conexion.prepareStatement(consulta);
            statement.setString(1, nombre); // Ejemplo de parámetro

            // Ejecuta la consulta
            statement.executeUpdate();

            // Cierra los recursos
            statement.close();
            conexion.close();

            /*pst = con.prepareStatement("insert into rubros(nombre)values(?)");
            pst.setString(1, nombre);
            pst.executeUpdate();*/

            // Muestra una alerta después de agregar el registro
            mostrarAlerta("Advertencia", "Rubro agregado", "El rubro se añadio con exito.");

            tabla();

            nombreTxt.setText("");
            nombreTxt.requestFocus();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(VentanaRubrosController.class.getName()).log(Level.SEVERE, null, ex);
        }

        /*String nombreRubro = nombreTxt.getText();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/mercadito", "usuarioMercadito", "mercadito");
            pst = con.prepareStatement("insert into rubros(nombre) values (?)");
            pst.setString(1, nombreRubro);

            int status = pst.executeUpdate();


            if(status==1) {
                JOptionPane.showMessageDialog(null, "Guardado con exito");
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo guardar");
            }

            tabla();
            nombreTxt.setText("");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VentanaRubrosController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/


        //Boton para agregar una habitacion
        /*Object[] oResult = _validar();
        boolean ok = (boolean) oResult[0];
        if (ok) {
            Rubro h = (Rubro) oResult[1];
            Rubros cnx = new Rubros();
            if (cnx.conectar()) {
                boolean okNuevo = cnx.isIngresar(h);
                if (okNuevo) {
                    Rubro hBlando = new Rubro();
                    JOptionPane.showMessageDialog(null, "Agregado con exito", "Ok", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No se registro el rubro", "Error", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "ERROR", "Error", JOptionPane.WARNING_MESSAGE);
            }
        }*/

    }

    /*
    @FXML
    void agregarRubro(MouseEvent event) {
        //Boton para agregar una habitacion
        Object[] oResult = _validar();
        boolean ok = (boolean) oResult[0];
        if (ok) {
            Rubro h = (Rubro) oResult[1];
            Rubros cnx = new Rubros();
            if (cnx.conectar()) {
                boolean okNuevo = cnx.isIngresar(h);
                if (okNuevo) {
                    Rubro hBlando = new Rubro();
                } else {
                    JOptionPane.showMessageDialog(null, "No se registro el rubro", "Error", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "ERROR", "Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }*/

    @FXML
    void eliminarRubro(MouseEvent event) {
        // Obtiene el índice de la fila seleccionada en la tabla "Tabla"
        myIndex = Tabla.getSelectionModel().getSelectedIndex();

        /*if (myIndex == -1) {
            // Si no se seleccionó ningún registro, muestra un mensaje de advertencia
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("Ningún registro seleccionado");
            alert.setContentText("Por favor, seleccione un registro para eliminar.");
            alert.showAndWait();
            return; // Sale del método si no hay selección
        }*/

        // Verifica si hay una selección válida antes de continuar
        if (!esRegistroValido()) {
            return; // Sale del método si no hay selección válida
        }

        // Obtiene el ID del registro seleccionado en la tabla
        id = Integer.parseInt(String.valueOf(Tabla.getItems().get(myIndex).getId()));

        try
        {
            // Prepara la consulta SQL para eliminar el registro
            pst = con.prepareStatement("delete from rubros where id_rubro = ? ");
            pst.setInt(1, id);
            pst.executeUpdate();

            // Muestra una alerta después de eliminar el registro
            mostrarAlerta("Advertencia", "Rubro eliminado", "El rubro se elimino con exito.");
            tabla();
            nombreTxt.setText("");
            nombreTxt.requestFocus();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(VentanaRubrosController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    void limpiarRubro(MouseEvent event) {
        // Vacia el campo de Nombre y hace focus en el mismo
        nombreTxt.setText("");
        nombreTxt.requestFocus();
    }

    @FXML
    void modificarRubro(MouseEvent event) {

        // Obtiene el índice de la fila seleccionada en la tabla "Tabla"
        myIndex = Tabla.getSelectionModel().getSelectedIndex();

        /*if (myIndex == -1) {
            // Si no se seleccionó ningún registro, muestra un mensaje de advertencia
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("Ningún registro seleccionado");
            alert.setContentText("Por favor, seleccione un registro para modificar.");
            alert.showAndWait();
            return; // Sale del método si no hay selección
        }

        id = Integer.parseInt(String.valueOf(Tabla.getItems().get(myIndex).getId()));

        String nombreIngresado = nombreTxt.getText();*/

        if (!esRegistroValido()) {
            return; // Sale del método si no hay selección válida
        }

        String nombreIngresado = nombreTxt.getText();

        if (!esNombreValido(nombreIngresado)) {
            mostrarAlerta("Advertencia", "Campo Vacío", "El campo de nombre no puede estar vacío.");
            Tabla.getSelectionModel().clearSelection();
            return; // Sale del método si el campo está vacío
        }

        // Toma el valor ingresado en "nombreIngresado", convierte la primera letra en mayúscula
        // y el resto en minúscula para formatear el nombre de manera consistente
        String nombre = nombreIngresado.substring(0, 1).toUpperCase() + nombreIngresado.substring(1).toLowerCase();

        try
        {
            pst = con.prepareStatement("update rubros set nombre = ? where id_rubro = ? ");
            pst.setInt(2, id);
            pst.setString(1, nombre);

            pst.executeUpdate();

            // Muestra una alerta después de modificar el registro
            mostrarAlerta("Advertencia", "Rubro modificado", "El rubro se modifico con exito.");

            tabla();
            nombreTxt.setText("");
            nombreTxt.requestFocus();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(VentanaRubrosController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }



    public void Connect()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/mercadito","usuarioMercadito","mercadito");
        } catch (ClassNotFoundException ex) {

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void tabla() // Método para cargar datos desde la base de datos y mostrarlos en una tabla
    {
        //ConexionMySQL conexionMySQL = new ConexionMySQL();
        //conexionMySQL.getCon();

        // Establece la conexión con la base de datos
        Connect();

        // Crea una lista observable para almacenar los datos de los rubros
        ObservableList<Rubro1> students = FXCollections.observableArrayList();
        try
        {
            // Prepara una consulta SQL para obtener los datos (id, nombre) de los rubros
            pst = con.prepareStatement("select id_rubro,nombre from rubros");
            ResultSet rs = pst.executeQuery();
            {
                while (rs.next()) // Procesa los resultados de la consulta
                {
                    Rubro1 st = new Rubro1();
                    st.setId(rs.getString("id_rubro"));
                    st.setName(rs.getString("nombre"));
                    students.add(st);
                }
            }
            // Asigna los datos a la tabla en la interfaz de usuario
            Tabla.setItems(students);
            IDColumna.setCellValueFactory(f -> f.getValue().idProperty());
            NOMBREColumna.setCellValueFactory(f -> f.getValue().nameProperty());
        }

        catch (SQLException ex)
        { // Maneja cualquier excepción SQL que pueda ocurrir
            Logger.getLogger(VentanaRubrosController.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Configura un manejador de eventos para las filas de la tabla
        Tabla.setRowFactory( tv -> {
            TableRow<Rubro1> myRow = new TableRow<>();
            myRow.setOnMouseClicked (event ->
            {
                if (event.getClickCount() == 1 && (!myRow.isEmpty()))
                { // Cuando se hace clic en una fila, actualiza la variable myIndex y el campo de texto nombreTxt
                    myIndex =  Tabla.getSelectionModel().getSelectedIndex();

                    id = Integer.parseInt(String.valueOf(Tabla.getItems().get(myIndex).getId()));
                    nombreTxt.setText(Tabla.getItems().get(myIndex).getName());

                }
            });
            return myRow;
        });


    }



    @Override
    public void initialize(URL location, ResourceBundle resources) { // Método llamado automáticamente al inicializar la vista
        tabla(); // Llama al método "tabla()" para cargar datos en la tabla al inicio
    }




    // METODOS


    // Comprueba si hay un registro seleccionado en la tabla
    // Si no hay selección, muestra una alerta de advertencia y devuelve falso
    private boolean esRegistroValido() {
        if (myIndex == -1) {
            mostrarAlerta("Advertencia", "Ningún registro seleccionado", "Por favor, seleccione un registro.");
            return false;
        }
        return true;
    }

    private boolean esNombreValido(String nombre) { // Comprueba si el nombre ingresado no está vacío
        return !nombre.isEmpty();
    }

    // Muestra una alerta de advertencia con el título, encabezado y contenido que se especifique
    private void mostrarAlerta(String titulo, String encabezado, String contenido) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);
        alert.showAndWait();
    }




    /*public static void main(String[] args) {
        ConexionMySQL conexionMySQL = new ConexionMySQL(); // Crea una instancia de ConexionMySQL

        if (conexionMySQL.conectar()) { // Llama al método conectar
            Connection conexion = conexionMySQL.getCon(); // Obtiene la conexión
            // Realiza operaciones con la conexión, como consultas SQL, aquí
        } else {
            System.out.println("No se pudo establecer la conexión.");
        }
    }*/




    /*
    // Metodos de validacion de textfield.
    private boolean _validarCamposEnBlanco(String nombre) {

        boolean ok2 = true;

        if (nombre.isEmpty()) {
            ok2 = false;
        }
        return ok2;
    }*/

    /*
    //Validacion
    private Object[] _validar() {
        boolean ok = false;
        Rubro h = new Rubro();
        String nombre = nombreTxt.getText().trim();
        boolean test = _validarCamposEnBlanco(nombre);

        if (test) {
            h.setNombre(nombreTxt.getText().trim());

        } else {
            JOptionPane.showMessageDialog(null, "No se permiten campos vacios", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        Object[] miObjeto = new Object[2];
        miObjeto[0] = ok;
        miObjeto[1] = h;
        return miObjeto;

    }
    */
}
