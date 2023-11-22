package com.mspdevs.mspfxmaven.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Alerta {
    public void mostrarError(String titulo, String encabezado, String contenido) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    public void mostrarAlertaInforme(String titulo, String encabezado, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    public boolean mostrarConfirmacion(String titulo, String encabezado, String contenido) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);

        // Crear botones personalizados para confirmación
        ButtonType botonAceptar = new ButtonType("Si", ButtonBar.ButtonData.OK_DONE);
        ButtonType botonCancelar = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        // Agregar los botones a la ventana de confirmación
        alert.getButtonTypes().setAll(botonAceptar, botonCancelar);

        // Mostrar la ventana y esperar a que se cierre
        Optional<ButtonType> resultado = alert.showAndWait();

        // Comprobar qué botón se presionó y devolver un valor booleano
        if (resultado.isPresent() && resultado.get() == botonAceptar) {
            return true; // Se presionó el botón "Aceptar"
        } else {
            return false; // Se presionó el botón "Cancelar" o se cerró la ventana
        }
    }

    public boolean mostrarConfirmacionDosBotones(String titulo, String encabezado, String contenido) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);

        // Crear botones personalizados para confirmación
        ButtonType salirButton = new ButtonType("Salir");
        ButtonType cerrarSesionButton = new ButtonType("Cerrar Sesión");

        alert.getButtonTypes().setAll(salirButton, cerrarSesionButton);

        // Mostrar la ventana y esperar a que se cierre
        Optional<ButtonType> resultado = alert.showAndWait();

        // Comprobar qué botón se presionó y devolver un valor booleano
        if (resultado.isPresent() && resultado.get() == salirButton) {
            return true; // Se presionó el botón "Salir"
        } else {
            return false; // Se presionó el botón "Cerrar Sesión" o se cerró la ventana
        }
    }
}