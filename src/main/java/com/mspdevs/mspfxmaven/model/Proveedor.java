package com.mspdevs.mspfxmaven.model;

public class Proveedor extends Persona{
    private int idProveedor;
    private String cuit;

    private Persona persona; // Agregar un campo para la persona relacionada

    /*public Proveedor(int idPersona, String nombre, String apellido, String provincia, String localidad, String calle, String dni, String mail, String telefono) {
        super(idPersona, nombre, apellido, provincia, localidad, calle, dni, mail, telefono);
    }

    public Proveedor() {
        super();
    }*/


    // Getters y setters de idProveedor y cuit
    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public Persona getPersona() { return persona; }

    public void setPersona(Persona persona) { this.persona = persona; }
}
