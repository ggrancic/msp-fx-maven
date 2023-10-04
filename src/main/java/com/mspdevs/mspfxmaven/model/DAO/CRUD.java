package com.mspdevs.mspfxmaven.model.DAO;

import javafx.collections.ObservableList;

public interface CRUD<T> {

    ObservableList<T> listarTodos() throws Exception;
    void insertar(T t) throws Exception;
    void eliminar(T t) throws Exception;
    void modificar(T t) throws Exception;
    
}
