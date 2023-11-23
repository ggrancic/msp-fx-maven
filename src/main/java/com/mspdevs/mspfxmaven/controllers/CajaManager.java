package com.mspdevs.mspfxmaven.controllers;

import com.mspdevs.mspfxmaven.model.Caja;

public class CajaManager {
	
	private static CajaManager instancia;

    private Caja caja;

    private CajaManager() {
        // Constructor privado para evitar instanciación directa
        this.caja = new Caja();
    }

    public static CajaManager getInstance() {
        if (instancia == null) {
            synchronized (CajaManager.class) {
                if (instancia == null) {
                    instancia = new CajaManager();
                }
            }
        }
        return instancia;
    }

    public Caja getCaja() {
        return caja;
    }

    // Otros métodos para actualizar los datos de la caja, como agregar ingresos, egresos, etc.
    
    public void agregarIngresos(double monto) {
        caja.setIngresos(caja.getIngresos() + monto);
    }

    public void agregarEgresos(double monto) {
        caja.setEgresos(caja.getEgresos() + monto);
    }

    // Puedes agregar más métodos según tus necesidades

    // ...

    // Método para realizar el cierre de caja
    public void cerrarCaja() {
        // Lógica para cerrar la caja, por ejemplo, guardar los datos en una base de datos
        // También puedes reiniciar los valores de ingresos, egresos, etc. según tus necesidades
    }
}
