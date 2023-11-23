package com.mspdevs.mspfxmaven.controllers;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

import com.mspdevs.mspfxmaven.utils.Alerta;
import com.mspdevs.mspfxmaven.utils.PantallaInicioUtil;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import com.mspdevs.mspfxmaven.Main;
import com.mspdevs.mspfxmaven.model.Caja;
import com.mspdevs.mspfxmaven.model.Empleado;
import com.mspdevs.mspfxmaven.model.DAO.EmpleadoDAOImpl;



public class VentanaPrincipalController implements Initializable {
    Alerta msj = new Alerta();

    private LoginMSPController loginController;

    private Empleado empleadoLogueado;


    @FXML
    private HBox hbox ;

    @FXML
    private BorderPane bpane;

    @FXML
    private Label usuarioLogueado;

    @FXML
    private Button btnCerrar;

    @FXML
    private Button btnClientes;

    @FXML
    private Button btnInventario;

    @FXML
    private Button btnProveedores;

    @FXML
    private Button btnRubros;

    @FXML
    private Label time;

    @FXML
    private Label fecha;

    @FXML
    private Button btnCompra;

    @FXML
    private Button btnCompras;

    @FXML
    private Button btnVentas;

    @FXML
    private Button btnReportes;

    @FXML
    private Button btnUsuarios;

    @FXML
    private ToolBar botonera;

    @FXML
    private Button btnCajas;

    private volatile boolean stop = false;

    private String nombreEmpleado;

    private int idEmpleado;

    private double ingresos;

    private Caja cajaActual;

    public Caja getCajaActual() {
        return cajaActual;
    }

    public void setCajaActual(Caja cajaActual) {
        this.cajaActual = cajaActual;
    }

    public double getIngresos() {
        return ingresos;
    }

    public void acumularIngresos(double newingresos) {
        this.ingresos = newingresos;
    }



    @FXML
    void abrirVentanaCliente(MouseEvent event) throws IOException {
        GridPane centro = FXMLLoader.load(getClass().getResource("/com/mspdevs/mspfxmaven/views/VentanaCliente.fxml"));
        bpane.setCenter(centro);
    }

    @FXML
    void abrirVentanaInventario(MouseEvent event) throws IOException {
        GridPane centro = FXMLLoader.load(getClass().getResource("/com/mspdevs/mspfxmaven/views/VentanaProductos.fxml"));
        bpane.setCenter(centro);
    }

    @FXML
    void abrirVentanaProveedores(MouseEvent event) throws IOException {
        GridPane centro = FXMLLoader.load(getClass().getResource("/com/mspdevs/mspfxmaven/views/VentanaProveedores.fxml"));
        bpane.setCenter(centro);
    }

    @FXML
    void abrirVentanaUsuarios(MouseEvent event) throws IOException {
        GridPane centro = FXMLLoader.load(getClass().getResource("/com/mspdevs/mspfxmaven/views/VentanaEmpleados.fxml"));
        bpane.setCenter(centro);
    }

    @FXML
    void abrirVentanaRubros(MouseEvent event) throws IOException {
        GridPane centro = FXMLLoader.load(getClass().getResource("/com/mspdevs/mspfxmaven/views/VentanaRubros.fxml"));
        bpane.setCenter(centro);
    }

    @FXML
    void abrirVentanaCompras(ActionEvent event) throws IOException {
        GridPane centro = FXMLLoader.load(getClass().getResource("/com/mspdevs/mspfxmaven/views/VentanaAlternativaCompras.fxml"));
        bpane.setCenter(centro);
    }

    @FXML
    void abrirVentanaVentas(ActionEvent event) throws IOException {

        if (cajaActual != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mspdevs/mspfxmaven/views/VentanaAlternativaVentas.fxml"));
            Parent root = loader.load();
            VentanaVentasAlternativaController ventasAlternativaController = loader.getController();

            String nombreUsuario = usuarioLogueado.getText();

            EmpleadoDAOImpl empleadoDAO = new EmpleadoDAOImpl();

            ventasAlternativaController.setUsuario(idEmpleado);
            ventasAlternativaController.setearCaja(cajaActual);

            bpane.setCenter(root);
        } else {
            msj.mostrarError("Error", "", "Debe abrir una nueva caja antes de iniciar la venta.");
        }


    }

    @FXML
    void abrirReportes(ActionEvent event) throws IOException {
        GridPane centro = FXMLLoader.load(getClass().getResource("/com/mspdevs/mspfxmaven/views/VentanaReportesBotones.fxml"));
        bpane.setCenter(centro);
    }


    @FXML
    void accionBtnCaja(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mspdevs/mspfxmaven/views/ModalCaja.fxml"));
        Parent root = loader.load();
        ModalCajaController mcc = loader.getController();

        mcc.setResponsableActual(empleadoLogueado);



        if (cajaActual != null) {
            mcc.bloquearApertura(cajaActual);
            mcc.setCajaNueva(cajaActual);

        } else {
            mcc.ponerLabelCC();
        }

        Scene scene = new Scene(root);
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.initStyle(StageStyle.UNDECORATED);
        newStage.initOwner( ((Node)event.getSource()).getScene().getWindow() );
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.showAndWait();

        if (cajaActual == null) {
            cajaActual = mcc.getCajaNueva();
        }

        boolean estadoCaja = mcc.isCajaCerrada();
        System.out.println(estadoCaja);

        if (estadoCaja) {
            cajaActual = null;
        }

    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Acá se inicializa todo lo referido a los elementos del fxml.
        Timenow();

        // BACKUP DE BD
        //usuarioLogueado.setText(loginController.getNombreUsuarioLogueado());


    }




    private void Timenow(){
        Thread thread = new Thread(() -> {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            while(!stop){
                try{
                    Thread.sleep(1000);
                }catch(Exception e){
                    System.out.println(e);
                }
                final String timenow = sdf.format(new Date());
                Platform.runLater(() -> {
                    time.setText(timenow); // This is the label
                });
            }
        });
        thread.start();

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e ->
                fecha.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        ),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    public void irAPantallaLogin(ActionEvent event) {
        PantallaInicioUtil.mostrarPantallaInicio(Main.primaryStage);
        // Cierra la ventana actual si es necesario
        // Agrega un manejador al evento de cierre de la ventana
        Stage currentStage = (Stage) btnCerrar.getScene().getWindow();
        currentStage.close();
        currentStage.setOnCloseRequest(e -> {
            e.consume(); // Evita que la ventana se cierre de inmediato
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar");
            confirmacion.setHeaderText("¿Desea salir o cerrar sesión?");
            ButtonType salirButton = new ButtonType("Salir");
            ButtonType noButton = new ButtonType("No salir");
            ButtonType cerrarSesionButton = new ButtonType("Cerrar sesión");

            confirmacion.getButtonTypes().setAll(salirButton, noButton, cerrarSesionButton);

            Stage stage = (Stage) confirmacion.getDialogPane().getScene().getWindow();
            stage.setAlwaysOnTop(true);

            confirmacion.showAndWait().ifPresent(response -> {
                if (response == salirButton) {
                    // Cerrar la aplicación
                    stop = true; // Detener el hilo de actualización del tiempo
                    currentStage.close();
                } else if (response == noButton) {
                    // No hacer nada, simplemente cerrar la ventana de confirmación
                } else if (response == cerrarSesionButton) {
                    // Cerrar sesión y volver a la ventana de inicio de sesión
                    stop = true; // Detener el hilo de actualización del tiempo
                    // Lógica para crear el backup de la base de datos
                    //realizarBackupBaseDeDatos();
                    PantallaInicioUtil.mostrarPantallaInicio(Main.primaryStage);
                }
            });
        });
        // Cierra la ventana actual si es necesario
        currentStage.close();
    }

    public void irAPantallaLogin(Stage stage) {
        PantallaInicioUtil.mostrarPantallaInicio(Main.primaryStage);
        // Cierra la ventana actual si es necesario
        // Agrega un manejador al evento de cierre de la ventana
        Stage currentStage = (Stage) btnCerrar.getScene().getWindow();
        currentStage.close();
        currentStage.setOnCloseRequest(e -> {
            e.consume(); // Evita que la ventana se cierre de inmediato
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar");
            confirmacion.setHeaderText("¿Desea salir o cerrar sesión?");
            ButtonType salirButton = new ButtonType("Salir");
            ButtonType noButton = new ButtonType("No salir");
            ButtonType cerrarSesionButton = new ButtonType("Cerrar sesión");

            confirmacion.getButtonTypes().setAll(salirButton, noButton, cerrarSesionButton);

            //Stage stage = (Stage) confirmacion.getDialogPane().getScene().getWindow();
            stage.setAlwaysOnTop(true);

            confirmacion.showAndWait().ifPresent(response -> {
                if (response == salirButton) {
                    // Cerrar la aplicación
                    stop = true; // Detener el hilo de actualización del tiempo
                    currentStage.close();
                } else if (response == noButton) {
                    // No hacer nada, simplemente cerrar la ventana de confirmación
                } else if (response == cerrarSesionButton) {
                    // Cerrar sesión y volver a la ventana de inicio de sesión
                    stop = true; // Detener el hilo de actualización del tiempo
                    // Lógica para crear el backup de la base de datos
                    //realizarBackupBaseDeDatos();
                    PantallaInicioUtil.mostrarPantallaInicio(Main.primaryStage);
                }
            });
        });

        // Cierra la ventana actual si es necesario
        currentStage.close();
    }

    @FXML
    void cerrarAplicacion(ActionEvent event) {

        if (cajaActual != null) {
            msj.mostrarError("ATENCION", "", "Tiene un cierre de caja pendiente.");

        } else {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar");
            confirmacion.setHeaderText("¿Desea salir o cerrar sesión?");
            ButtonType salirButton = new ButtonType("Salir");
            ButtonType noButton = new ButtonType("No salir");
            ButtonType cerrarSesionButton = new ButtonType("Cerrar sesión");

            confirmacion.getButtonTypes().setAll(salirButton, noButton, cerrarSesionButton);

            Stage stage = (Stage) confirmacion.getDialogPane().getScene().getWindow();
            stage.setAlwaysOnTop(true);

            confirmacion.showAndWait().ifPresent(response -> {
                if (response == salirButton) {
                    // Cerrar la aplicación
                    stop = true; // Detener el hilo de actualización del tiempo
                    Stage currentStage = (Stage) btnCerrar.getScene().getWindow();
                    currentStage.close();
                } else if (response == noButton) {
                    // No hacer nada, simplemente cerrar la ventana de confirmación
                } else if (response == cerrarSesionButton) {
                    // Cerrar sesión y volver a la ventana de inicio de sesión
                    stop = true; // Detener el hilo de actualización del tiempo
                    // Lógica para crear el backup de la base de datos
                    //realizarBackupBaseDeDatos();
                    irAPantallaLogin(event);
                }
            });
        }


    }

    // ... otros métodos ...

    // Método para obtener una referencia al controlador de inicio de sesión
    private LoginMSPController obtenerControladorLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mspdevs/mspfxmaven/views/LoginMSP.fxml"));
            Parent root = loader.load();
            LoginMSPController loginController = loader.getController();
            return loginController;
        } catch (IOException e) {
            System.out.println("Error al obtener el controlador de inicio de sesión: " + e.getMessage());
            return null;
        }
    }

    // Método para crear un backup de la base de datos
    private void realizarBackupBaseDeDatos() {
        String dbUser = "usuarioMercadito";
        String dbPassword = "mercadito";
        String dbName = "mercadito";
        String rutaBackup = "F:\\Backup\\backup_mercadito.sql";

        try {
            String rutaMysqldump = "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump";
            String[] command = {
                    rutaMysqldump,
                    "--user=" + dbUser,
                    "--password=" + dbPassword,
                    dbName,
                    "--result-file=" + rutaBackup
            };

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                msj.mostrarAlertaInforme("Hola","Backup realizado con éxito", "El backup de la base de datos se ha creado correctamente.");
            } else {
                msj.mostrarAlertaInforme("Hola","Error en el backup", "No se pudo realizar el backup de la base de datos.");
            }
        } catch (IOException e) {
            msj.mostrarAlertaInforme("Hola","Error en el backup", "Error de entrada/salida: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restaura la bandera de interrupción
            msj.mostrarAlertaInforme("Hola", "Error en el backup", "La operación fue interrumpida: " + e.getMessage());
        } catch (Exception e) {
            msj.mostrarAlertaInforme("Hola","Error en el backup", "Error general: " + e.getMessage());
        }
    }

    public void obtenerEmpleado(Empleado empleadoAutenticado) {

        empleadoLogueado = empleadoAutenticado;

        nombreEmpleado = empleadoAutenticado.getNombre() + " " + empleadoAutenticado.getApellido();
        idEmpleado = empleadoAutenticado.getId_empleado();

        usuarioLogueado.setText(nombreEmpleado);

    }

    // Agrega este método para manejar el evento de cierre
    public void setCerrarEvento(Stage primaryStage) {

        primaryStage.getScene().getWindow().setOnCloseRequest(e -> {

            e.consume(); // Evita que la ventana principal se cierre de inmediato

            if (cajaActual != null) {
                msj.mostrarError("ALERTA", "", "Tiene un cierre de caja pendiente.");
            } else {
                Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
                confirmacion.setTitle("Confirmar");
                confirmacion.setHeaderText("¿Desea salir o cerrar sesión?");
                ButtonType salirButton = new ButtonType("Salir");
                ButtonType noButton = new ButtonType("No salir");
                ButtonType cerrarSesionButton = new ButtonType("Cerrar sesión");

                confirmacion.getButtonTypes().setAll(salirButton, noButton, cerrarSesionButton);

                Stage stage = (Stage) confirmacion.getDialogPane().getScene().getWindow();
                stage.setAlwaysOnTop(true);

                confirmacion.showAndWait().ifPresent(response -> {
                    if (response == salirButton) {
                        // Cerrar la aplicación
                        stop = true; // Detener el hilo de actualización del tiempo
                        primaryStage.close(); // Cierra la ventana principal
                    } else if (response == noButton) {
                        // No hacer nada, simplemente cerrar la ventana de confirmación
                    } else if (response == cerrarSesionButton) {
                        // Cerrar sesión y volver a la ventana de inicio de sesión
                        stop = true; // Detener el hilo de actualización del tiempo
                        // Lógica para crear el backup de la base de datos
                        //realizarBackupBaseDeDatos();
                        irAPantallaLogin(primaryStage);
                    }
                });
            }

        });
    }


    void habilitarSoloVentas() {
        botonera.getItems().remove(btnClientes);
        botonera.getItems().remove(btnCompra);
        botonera.getItems().remove(btnInventario);
        botonera.getItems().remove(btnReportes);
        botonera.getItems().remove(btnProveedores);
        botonera.getItems().remove(btnUsuarios);
        botonera.getItems().remove(btnRubros);
    }


}