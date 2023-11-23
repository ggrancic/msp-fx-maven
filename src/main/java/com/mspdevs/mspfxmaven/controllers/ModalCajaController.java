package com.mspdevs.mspfxmaven.controllers;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import com.mspdevs.mspfxmaven.model.Caja;
import com.mspdevs.mspfxmaven.model.Empleado;
import com.mspdevs.mspfxmaven.model.DAO.CajaDAOImpl;
import com.mspdevs.mspfxmaven.model.DAO.VentaDAOImpl;
import com.mspdevs.mspfxmaven.utils.Alerta;
import com.mspdevs.mspfxmaven.utils.ManejoDeEntrada;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.converter.NumberStringConverter;


public class ModalCajaController implements Initializable {
	
    private Caja cajaNueva;
    
    Alerta msj = new Alerta();
    
    private Double total;
    
    private Empleado responsableActual;
    
    private boolean cajaCerrada;
    
    
    

	


	


	@FXML
    private Button btnAbrir;

    @FXML
    private Button btnCerrar;
    
    @FXML
    private Button btnVolver;
    
    @FXML
    private Button btnRetiro;

    @FXML
    private TextField campoEgresos;

    @FXML
    private TextField campoIngresos;

    @FXML
    private TextField campoMF;

    @FXML
    private TextField campoMI;

    @FXML
    private VBox contenedor;
    
    @FXML
    private Label labelEstado;
	
    // --------- EVENTOS DE BOTONES ----------
	@FXML
    void accionBtnVolver(ActionEvent event) {
    	Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    void accionAbrirCaja(ActionEvent event) {
    	
    	
    	cajaNueva = new Caja();
    	
    	cajaNueva.setMontoInicial(Double.parseDouble(campoMI.getText()));
    	cajaNueva.setIngresos(0.00);
    	cajaNueva.setEgresos(0.00);
    	cajaNueva.setMontoFinal(0.00);
    	cajaNueva.setFechaHoraApertura(LocalDateTime.now());
    	cajaNueva.setStatus(true);
    	cajaNueva.setResponsable(responsableActual);
    	System.out.println(responsableActual.getNombre());
    	
    	msj.mostrarAlertaInforme("CAJA ABIERTA", "", "Caja del día abierta. Recuerde cerrarla al final de la jornada.");
    	
    	Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
        
        
    }
    
    public void bloquearApertura(Caja cajaActual) throws IOException {
    	
    	total = cajaActual.getMontoInicial() + cajaActual.getIngresos() - cajaActual.getEgresos();
    	
    	campoMI.setText(Double.toString(cajaActual.getMontoInicial()));
    	campoIngresos.setText(Double.toString(cajaActual.getIngresos()));
    	campoEgresos.setText(Double.toString(cajaActual.getEgresos()));
    	
    	btnAbrir.setDisable(true);
    	campoMI.setDisable(true);
    	
    	btnRetiro.setDisable(false);
		btnCerrar.setDisable(false);
    	
    	if ((cajaActual.getStatus())) {
    		labelEstado.setText("Estado de caja: ABIERTA");
    	}
    	
    	cajaActual.setMontoFinal(total);
    	campoMF.setText(Double.toString(cajaActual.getMontoFinal()));
    	
    }
    
    public void ponerLabelCC() {
    	labelEstado.setText("Estado de caja: CERRADA");
    }
    
    @FXML
    void accionBtnRetiro(ActionEvent event) throws IOException {
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mspdevs/mspfxmaven/views/ModalRetiro.fxml"));
        Parent root = loader.load();
        ModalRetiroController mcr = loader.getController();
        
        mcr.setTotalCaja(cajaNueva);
    	
    	Scene scene = new Scene(root);
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.initStyle(StageStyle.UNDECORATED);
        newStage.initOwner( ((Node)event.getSource()).getScene().getWindow() );
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.showAndWait();
        
        this.bloquearApertura(cajaNueva);
        
    }
    
    @FXML
    void accionCerrarCaja(ActionEvent event) {
    	this.cajaNueva.setFechaHoraCierre(LocalDateTime.now());
    	
    	boolean confirmado = msj.mostrarConfirmacion("Confirmación", "", "¿Está seguro de que desea cerrar la caja?");
    	
    	if (confirmado) {
    		CajaDAOImpl cajadao = new CajaDAOImpl();
        	
        	try {
    			cajadao.insertar(cajaNueva);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        	
        	cajaCerrada = true;
        	
        	msj.mostrarAlertaInforme("Operacion exitosa", null, "Ha cerrado la caja exitosamente");
        	
        	Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
    	}
    	
    	
    }
    
    
    
    // -------------- GETTER Y SETTER ------------
    

    public Caja getCajaNueva() {
		return cajaNueva;
	}

	public void setCajaNueva(Caja cajaNueva) {
		this.cajaNueva = cajaNueva;
	}
    
	
	public Empleado getResponsableActual() {
		return responsableActual;
	}

	public void setResponsableActual(Empleado responsableActual) {
		this.responsableActual = responsableActual;
	}
	
	
	public boolean isCajaCerrada() {
		return cajaCerrada;
	}

	public void setCajaCerrada(boolean cajaCerrada) {
		this.cajaCerrada = cajaCerrada;
	}
	
	
	// -------------- INICIALIZADOR ELEMENTOS FXML ------------
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {

    	campoMI.setTextFormatter(ManejoDeEntrada.soloNumerosDecimales());
    	
    	campoIngresos.setDisable(true);
    	campoEgresos.setDisable(true);
    	campoMF.setDisable(true);
    	btnRetiro.setDisable(true);
		btnCerrar.setDisable(true);
	}
}
