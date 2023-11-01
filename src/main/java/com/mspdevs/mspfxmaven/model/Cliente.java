package com.mspdevs.mspfxmaven.model;

public class Cliente extends Persona {
    private int idCliente;
    private Persona persona; // Agregar un campo para la persona relacionada

    // Getters y setters de idProveedor y cuit
    public int getIdCliente() {
        return idCliente;
    }
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
    public Persona getPersona() { return persona; }
    public void setPersona(Persona persona) { this.persona = persona; }
}
