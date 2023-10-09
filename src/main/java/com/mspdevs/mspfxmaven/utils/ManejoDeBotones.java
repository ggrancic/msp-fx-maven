package com.mspdevs.mspfxmaven.utils;

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

public class ManejoDeBotones {
    private Button btnModificar;
    private Button btnEliminar;
    private Button btnAgregar;

    public ManejoDeBotones(Button btnModificar, Button btnEliminar, Button btnAgregar) {
        this.btnModificar = btnModificar;
        this.btnEliminar = btnEliminar;
        this.btnAgregar = btnAgregar;
    }

    public void configurarBotones(boolean habilitarModificarEliminar) {
        btnModificar.setDisable(!habilitarModificarEliminar);
        btnEliminar.setDisable(!habilitarModificarEliminar);
        btnAgregar.setDisable(habilitarModificarEliminar);
    }
}
