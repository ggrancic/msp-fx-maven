package com.mspdevs.mspfxmaven.model;

import java.util.ArrayList;

public abstract class ConexionMySQLAbstracto extends ConexionMySQL{


    public abstract ArrayList getList(String query);

    public abstract boolean isIngresar(Object obj);

    public abstract boolean isEliminar(Object obj);

    public abstract boolean isModificar(Object obj);

    public abstract boolean isActualizar(String update);
}
