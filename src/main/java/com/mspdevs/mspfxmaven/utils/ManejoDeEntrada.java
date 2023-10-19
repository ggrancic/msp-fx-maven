package com.mspdevs.mspfxmaven.utils;

import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class ManejoDeEntrada {
    // Patrón para letras, espacios y acentos
    private static final Pattern letrasEspacioAcento = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]*$");
    // Operador para filtrar caracteres ingresados
    private static final UnaryOperator<TextFormatter.Change> letrasEspacioAcentoFiltro = change -> {
        if (letrasEspacioAcento.matcher(change.getControlNewText()).matches()) {
            if (change.getControlNewText().length() <= 45) {
                return change;
            }
        }
        return null;
    };
    public static TextFormatter<String> soloLetrasEspacioAcento() {
        return new TextFormatter<>(letrasEspacioAcentoFiltro);
    }


    // Patrón para letras, números, espacios y acentos
    private static final Pattern letrasNumEspAcento = Pattern.compile("^[a-zA-Z0-9\\sáéíóúÁÉÍÓÚüÜñÑ]*$");
    // Operador para filtrar caracteres ingresados
    private static final UnaryOperator<TextFormatter.Change> letrasNumEspAcentoFiltro = change -> {
        if (letrasNumEspAcento.matcher(change.getControlNewText()).matches()) {
            if (change.getControlNewText().length() <= 50) {
                return change;
            }
        }
        return null;
    };
    public static TextFormatter<String> soloLetrasNumEspAcento() {
        return new TextFormatter<>(letrasNumEspAcentoFiltro);
    }

    // Patrón para codigo de barras
    /*private static final Pattern codigoBarra = Pattern.compile("^[a-zA-Z0-9ñÑ]*$");
    // Operador para filtrar caracteres ingresados
    private static final UnaryOperator<TextFormatter.Change> codigoBarraFiltro = change -> {
        if (codigoBarra.matcher(change.getControlNewText()).matches()) {
            if (change.getControlNewText().length() <= 13) { // Cambiar límite según necesidades
                return change;
            }
        }
        return null;
    };
    public static TextFormatter<String> soloCodigoBarra() {
        return new TextFormatter<>(codigoBarraFiltro);
    }*/

    // Patrón para email
    private static final Pattern email = Pattern.compile("^[a-zA-Z0-9ñ@\\-_.]+$");
    // Operador para filtrar caracteres ingresados
    private static final UnaryOperator<TextFormatter.Change> emailFiltro = change -> {
        if (change.getControlNewText().isEmpty()) {
            return change;
        }
        if (email.matcher(change.getControlNewText()).matches()) {
            if (change.getControlNewText().length() <= 255) { // Cambiar límite según necesidades
                return change;
            }
        }
        return null;
    };
    public static TextFormatter<String> soloEmail() {
        return new TextFormatter<>(emailFiltro);
    }

    // Patrón para números decimales
    private static final Pattern numerosDecimales = Pattern.compile("\\d{0,6}(\\.\\d{0,2})?");

    // Operador para filtrar caracteres ingresados
    private static final UnaryOperator<TextFormatter.Change> numerosDecimalesFiltro = change -> {
        if (numerosDecimales.matcher(change.getControlNewText()).matches()) {
            return change;
        } else {
            return null;
        }
    };
    public static TextFormatter<String> soloNumerosDecimales() {
        return new TextFormatter<>(numerosDecimalesFiltro);
    }

    // Define un patrón común para números enteros
    private static final Pattern numerosEnteros= Pattern.compile("\\d*");
    // Crea un operador común para aplicar la restricción de formato
    private static final UnaryOperator<TextFormatter.Change> numerosEnterosFiltro = change -> {
        if (numerosEnteros.matcher(change.getControlNewText()).matches()) {
            if (change.getControlNewText().length() <= 11) { // Cambiar límite según necesidades
                return change;
            }
        }
        return null;
    };
    private static final UnaryOperator<TextFormatter.Change> numerosTelefono = change -> {
        if (numerosEnteros.matcher(change.getControlNewText()).matches()) {
            if (change.getControlNewText().length() <= 10) { // Cambiar límite según necesidades
                return change;
            }
        }
        return null;
    };
    private static final UnaryOperator<TextFormatter.Change> numerosDni = change -> {
        if (numerosEnteros.matcher(change.getControlNewText()).matches()) {
            if (change.getControlNewText().length() <= 8) { // Cambiar límite según necesidades
                return change;
            }
        }
        return null;
    };
    private static final UnaryOperator<TextFormatter.Change> numerosCodigoBarras = change -> {
        if (numerosEnteros.matcher(change.getControlNewText()).matches()) {
            if (change.getControlNewText().length() <= 13) { // Cambiar límite según necesidades
                return change;
            }
        }
        return null;
    };
    private static final UnaryOperator<TextFormatter.Change> numerosFactura = change -> {
        if (numerosEnteros.matcher(change.getControlNewText()).matches()) {
            if (change.getControlNewText().length() <= 12) { // Cambiar límite según necesidades
                return change;
            }
        }
        return null;
    };
    private static final UnaryOperator<TextFormatter.Change> numerosCantidadGanancia = change -> {
        if (numerosEnteros.matcher(change.getControlNewText()).matches()) {
            if (change.getControlNewText().length() <= 3) { // Cambiar límite según necesidades
                return change;
            }
        }
        return null;
    };

    // Crea un filtro que permita solo dígitos y una longitud máxima de 3
    static UnaryOperator<TextFormatter.Change> filtroCantidad = change -> {
        String newText = change.getControlNewText();
        if (newText.matches("\\d{0,3}")) {
            return change;
        }
        return null;
    };

    // Aplica el filtro al TextFormatter
    public static TextFormatter<String> soloCantidad() { return new TextFormatter<>(filtroCantidad);}


    // Método para obtener un TextFormatter para números enteros
    public static TextFormatter<String> soloNumerosEnteros() {
        return new TextFormatter<>(numerosEnterosFiltro);
    }
    public static TextFormatter<String> soloDni() {
        return new TextFormatter<>(numerosDni);
    }
    public static TextFormatter<String> soloTelefono() {
        return new TextFormatter<>(numerosTelefono);
    }
    public static TextFormatter<String> soloCodigoBarras() {
        return new TextFormatter<>(numerosCodigoBarras);
    }
    public static TextFormatter<String> soloNumerosFactura() {
        return new TextFormatter<>(numerosFactura);
    }
    public static TextFormatter<String> soloCantidadGanancia() {
        return new TextFormatter<>(numerosCantidadGanancia);
    }
}
