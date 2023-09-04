package com.mspdevs.mspfxmaven.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Rubro1 { // Clase Rubro1 que representa un modelo para datos de rubros en JavaFX
    // Propiedades de cadena para el ID y el nombre del rubro
    private final StringProperty id;
    private final StringProperty nombre;


    /*public Rubro1()
    {
        id = new SimpleStringProperty(this, "id");
        nombre = new SimpleStringProperty(this, "name");
    }

    public Rubro1(StringProperty id, StringProperty nombre) {
        this.id = id;
        this.nombre = nombre;
    }*/

    // Constructor predeterminado
    public Rubro1()
    { // Inicializa las propiedades de cadena con un enlace bidireccional al objeto Rubro1
        id = new SimpleStringProperty(this, "id");
        nombre  = new SimpleStringProperty(this, "name");
    }

    // Método para obtener la propiedad del ID
    public StringProperty idProperty() {
        return id;
    }

    // Método para obtener el valor del ID
    public String getId() {
        return id.get();
    }

    // Método para establecer el valor del ID
    public void setId(String newId) {
        id.set(newId);
    }

    // Método para obtener la propiedad del nombre
    public StringProperty nameProperty() {
        return nombre;
    }

    // Método para obtener el valor del nombre
    public String getName() {
        return nombre.get();
    }

    // Método para establecer el valor del nombre
    public void setName(String newName) {
        nombre.set(newName);
    }

}
