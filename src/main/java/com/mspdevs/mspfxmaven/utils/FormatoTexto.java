package com.mspdevs.mspfxmaven.utils;

public class FormatoTexto {
    public static String formatearTexto(String texto) {
        String[] palabras = texto.split(" ");
        StringBuilder resultado = new StringBuilder();

        for (String palabra : palabras) {
            if (!palabra.isEmpty()) {
                // Convierte la primera letra de la palabra en mayúscula y el resto en minúscula
                String primeraLetraMayuscula = palabra.substring(0, 1).toUpperCase();
                String restoMinuscula = palabra.substring(1).toLowerCase();

                // Agrega la palabra formateada al resultado
                resultado.append(primeraLetraMayuscula).append(restoMinuscula).append(" ");
            }
        }
        return resultado.toString().trim(); // Elimina espacios al final
    }
}
