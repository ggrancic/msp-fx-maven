package com.mspdevs.mspfxmaven.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProvinciasArgentinas {
    public static ObservableList<String> getProvincias() {
        ObservableList<String> provinciasArgentinas = FXCollections.observableArrayList(
                "Buenos Aires",
                "Catamarca",
                "Chaco",
                "Chubut",
                "Córdoba",
                "Corrientes",
                "Entre Ríos",
                "Formosa",
                "Jujuy",
                "La Pampa",
                "La Rioja",
                "Mendoza",
                "Misiones",
                "Neuquén",
                "Río Negro",
                "Salta",
                "San Juan",
                "San Luis",
                "Santa Cruz",
                "Santa Fe",
                "Santiago del Estero",
                "Tierra del Fuego",
                "Tucumán"
        );

        return provinciasArgentinas;
    }
}
