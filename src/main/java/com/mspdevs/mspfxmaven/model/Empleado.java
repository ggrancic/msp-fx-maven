package com.mspdevs.mspfxmaven.model;

public class Empleado extends Persona{
    int id_empleado;
    String nombre_usuario;
    String clave;
    String esAdmin;
    int idPersonaFK;

    public int getId_empleado() {
        return id_empleado;
    }

    public void setId_empleado(int id_empleado) {
        this.id_empleado = id_empleado;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getEsAdmin() {
        return this.esAdmin;
    }

    public void setEsAdmin(String esAdmin) {
        this.esAdmin = esAdmin;
    }

    public int getIdPersonaFK() {
        return idPersonaFK;
    }

    public void setIdPersonaFK(int idPersonaFK) {
        this.idPersonaFK = idPersonaFK;
    }
}
