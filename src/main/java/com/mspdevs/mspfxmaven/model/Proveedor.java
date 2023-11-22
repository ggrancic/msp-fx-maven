package com.mspdevs.mspfxmaven.model;

public class Proveedor extends Persona{
    private int idProveedor;
    private Persona persona;
    public Proveedor() { this.persona = new Persona();
    }

    // Getters y setters
    public int getIdProveedor() {
        return idProveedor;
    }
    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }
    public Persona getPersona() { return persona; }
    public void setPersona(Persona persona) { this.persona = persona; }
}
