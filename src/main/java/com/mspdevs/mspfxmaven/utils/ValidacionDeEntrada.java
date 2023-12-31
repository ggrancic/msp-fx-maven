package com.mspdevs.mspfxmaven.utils;

import javafx.scene.control.ComboBox;
import com.mspdevs.mspfxmaven.utils.Alerta;

import java.util.regex.Pattern;

public class ValidacionDeEntrada {
    static Alerta msj = new Alerta();

    public static boolean validarEmail(String email) {
        // Validar el formato del correo electrónico
        // (Puedes utilizar una expresión regular o alguna otra lógica)
        if (email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return true;
        } else {
            msj.mostrarError("Error", "", "El formato del correo electrónico es inválido.");
            return false;
        }
    }

    public static boolean validarCuil(String cuil) {
        // Validar que el CUIL tenga una longitud de 10 o 11 dígitos
        if (cuil.length() == 10 || cuil.length() == 11) {
            if (cuil.matches("\\d{10,11}")) {
                return true;
            } else {
                msj.mostrarError("Error", "", "El CUIL ingresado no es válido.");
                return false;
            }
        } else {
            msj.mostrarError("Error", "", "El CUIL debe tener 10 o 11 dígitos.");
            return false;
        }
    }

    public static boolean validarDNI(String dni) {
        // Validar que el DNI tenga una longitud de 7 o 8 dígitos
        if (dni.length() == 7 || dni.length() == 8) {
            if (dni.matches("\\d{7,8}")) {
                return true;
            } else {
                msj.mostrarError("Error", "", "El DNI ingresado no es válido.");
                return false;
            }
        } else {
            msj.mostrarError("Error", "", "El DNI debe tener 7 u 8 dígitos.");
            return false;
        }
    }

    public static boolean validarCodigoDeBarra(String codigo) {
        // Validar que el código de barra tenga exactamente 13 caracteres
        if (codigo.length() == 13) {
            return true;
        } else {
            msj.mostrarError("Error", "", "El codigo de barra ingresado no es válido.");
            return false;
        }
    }

    public static boolean validarNumeroFactura(String numFactura) {
        // Validar que el código de barra tenga exactamente 13 caracteres
        if (numFactura.length() == 12) {
            return true;
        } else {
            msj.mostrarError("Error", "", "El número de factura debe tener 12 dígitos.");
            return false;
        }
    }

    public static boolean validarCantidad(String cantidadIngresada) {
        // Verificar si el campo está vacío
        if (cantidadIngresada.isEmpty()) {
            msj.mostrarError("Error", "", "La cantidad no debe estar vacia.");
            return false;
        }


        // Contar el número de dígitos en el texto
        int contadorDigitos = 0;
        for (char c : cantidadIngresada.toCharArray()) {
            if (Character.isDigit(c)) {
                contadorDigitos++;
            }
        }

        // Verificar si hay entre 1 y 3 dígitos
        if (contadorDigitos >= 1 && contadorDigitos <= 3) {
            return true;
        } else {
            msj.mostrarError("Error", "", "La cantidad debe tener entre 1 y 3 números..");
            return false;
        }
    }

    public static boolean validarGanancia(String gananciaIngresada) {
        // Verificar si el campo está vacío
        if (gananciaIngresada.isEmpty()) {
            msj.mostrarError("Error", "", "La ganancia no debe estar vacia.");
            return false;
        }

        // Contar el número de dígitos en el texto
        int contadorDigitos = 0;
        for (char c : gananciaIngresada.toCharArray()) {
            if (Character.isDigit(c)) {
                contadorDigitos++;
            }
        }

        // Verificar si hay entre 1 y 3 dígitos
        if (contadorDigitos >= 1 && contadorDigitos <= 3) {
            return true;
        } else {
            msj.mostrarError("Error", "", "La ganancia debe tener entre 1 y 3 números.");
            return false;
        }
    }

    public static boolean validarSeleccionTipoProveedorYNumeroFactura(String numeroFactura, String tipo, String proveedor) {
        if (numeroFactura == null || numeroFactura.isEmpty()) {
            msj.mostrarError("Error", "", "Debe indicar el número de factura");
            return false;
        }
        if (numeroFactura.length() != 12) {
            msj.mostrarError("Error", "", "El número de factura debe tener exactamente 12 dígitos.");
            return false;
        }
        if (tipo == null || tipo.isEmpty()) {
            msj.mostrarError("Error", "", "Debe seleccionar un tipo de factura");
            return false;
        }
        if (proveedor == null || proveedor.isEmpty()) {
            msj.mostrarError("Error", "", "Debe seleccionar un proveedor");
            return false;
        }
        return true;
    }

    public static boolean validarSeleccionComboBox(ComboBox<?> comboBox, String mensajeError) {
        // Verificar si se ha seleccionado un elemento en el ComboBox
        if (comboBox.getSelectionModel().getSelectedItem() != null) {
            return true;
        } else {
            msj.mostrarError("Error", "", mensajeError); // Muestra el mensaje de error personalizado
            return false;
        }
    }

    public static boolean validarTelefono(String telefono) {
        // Validar que el teléfono tenga una longitud de 10 dígitos
        if (telefono.length() == 10 && telefono.matches("\\d{10}")) {
            return true;
        } else {
            msj.mostrarError("Error", "", "El teléfono debe tener 10 dígitos numéricos.");
            return false;
        }
    }



    public static boolean validarPrecioVenta(String precioVentaTexto) {
        // Verificar si el texto es nulo o está vacío
        if (precioVentaTexto == null || precioVentaTexto.isEmpty()) {
            msj.mostrarError("Error", "", "Debe ingresar un precio de venta.");
            return false;
        }

        // Verificar si el texto contiene solo números y, opcionalmente, un punto
        if (!precioVentaTexto.matches("^\\d+(\\.\\d+)?$")) {
            msj.mostrarError("Error", "", "El precio de venta debe ser un número válido.");
            return false;
        }

        return true;
    }
}


