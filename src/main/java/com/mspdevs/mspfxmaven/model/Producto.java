package com.mspdevs.mspfxmaven.model;

import javafx.beans.property.*;

public class Producto {
    private final StringProperty id;
    private final StringProperty nombre;
    private final DoubleProperty precioVenta;
    private final DoubleProperty precioLista;
    private final IntegerProperty cantidadDisponible;
    private final IntegerProperty cantidadMinima;

    // Constructor predeterminado
    public Producto() {
        id = new SimpleStringProperty(this, "id");
        nombre = new SimpleStringProperty(this, "nombre");
        precioVenta = new SimpleDoubleProperty(this, "precioVenta");
        precioLista = new SimpleDoubleProperty(this, "precioLista");
        cantidadDisponible = new SimpleIntegerProperty(this, "cantidadDisponible");
        cantidadMinima = new SimpleIntegerProperty(this, "cantidadMinima");
    }

    // Métodos para obtener y establecer la propiedad del ID
    public StringProperty idProperty() {
        return id;
    }

    public String getId() {
        return id.get();
    }

    public void setId(String newId) {
        id.set(newId);
    }

    // Métodos para obtener y establecer la propiedad del nombre
    public StringProperty nombreProperty() {
        return nombre;
    }

    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String newNombre) {
        nombre.set(newNombre);
    }

    // Métodos para obtener y establecer la propiedad del precio de venta
    public DoubleProperty precioVentaProperty() {
        return precioVenta;
    }

    public double getPrecioVenta() {
        return precioVenta.get();
    }

    public void setPrecioVenta(double newPrecioVenta) {
        precioVenta.set(newPrecioVenta);
    }

    // Métodos para obtener y establecer la propiedad del precio de lista
    public DoubleProperty precioListaProperty() {
        return precioLista;
    }

    public double getPrecioLista() {
        return precioLista.get();
    }

    public void setPrecioLista(double newPrecioLista) {
        precioLista.set(newPrecioLista);
    }

    // Métodos para obtener y establecer la propiedad de la cantidad disponible
    public IntegerProperty cantidadDisponibleProperty() {
        return cantidadDisponible;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible.get();
    }

    public void setCantidadDisponible(int newCantidadDisponible) {
        cantidadDisponible.set(newCantidadDisponible);
    }

    // Métodos para obtener y establecer la propiedad de la cantidad mínima
    public IntegerProperty cantidadMinimaProperty() {
        return cantidadMinima;
    }

    public int getCantidadMinima() {
        return cantidadMinima.get();
    }

    public void setCantidadMinima(int newCantidadMinima) {
        cantidadMinima.set(newCantidadMinima);
    }
}
