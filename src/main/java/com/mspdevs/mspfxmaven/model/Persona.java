package com.mspdevs.mspfxmaven.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class Persona {
    int idPersona;
    String nombre;
    String apellido;
    String provincia;
    String localidad;
    String calle;
    String dni;
    String mail;
    String telefono;
    

    /*public Persona() {
        this.idPersona = idPersona;
        this.nombre = nombre;
        this.apellido = apellido;
        this.provincia = provincia;
        this.localidad = localidad;
        this.calle = calle;
        this.dni = dni;
        this.mail = mail;
        this.telefono = telefono;

    }

    public Persona(int idPersona, String nombre, String apellido, String provincia, String localidad, String calle, String dni, String mail, String telefono) {
        this.idPersona = idPersona;
        this.nombre = nombre;
        this.apellido = apellido;
        this.provincia = provincia;
        this.localidad = localidad;
        this.calle = calle;
        this.dni = dni;
        this.mail = mail;
        this.telefono = telefono;
    }*/


    // Getters y setters de idProveedor y cuit
    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    // Constructor
    /*public Persona(int idPersona, String nombre, String apellido, String provincia, String localidad, String calle, String dni, String mail, String telefono) {
        this.idPersona = idPersona;
        this.nombre = nombre;
        this.apellido = apellido;
        this.provincia = provincia;
        this.localidad = localidad;
        this.calle = calle;
        this.dni = dni;
        this.mail = mail;
        this.telefono = telefono;
    }*/
}
