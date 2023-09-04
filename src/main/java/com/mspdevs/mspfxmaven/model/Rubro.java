package com.mspdevs.mspfxmaven.model;

public class Rubro {
    private int id_rubro;
    private String nombre;

    // Constructor
    public Rubro(){
        setId_rubro(0);
        setNombre("");
    }

    // Métodos getter y setter para la ID del rubro
    public int getId_rubro() {
        return id_rubro;
    }

    public void setId_rubro(int id_rubro) {
        this.id_rubro = id_rubro;
    }

    // Métodos getter y setter para el nombre del rubro
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public Object[] getInfo() {

        Object[] oDato = {getId_rubro(),
                getNombre()};
        return oDato;
    }

    public boolean isValidar() {
        boolean ok = false;
        ok = true;
        return ok;
    }
}
