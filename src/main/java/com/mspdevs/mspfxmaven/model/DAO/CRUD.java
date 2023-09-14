package com.mspdevs.mspfxmaven.model.DAO;

import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public interface CRUD<T> {
    // Método para listar todos los elementos de tipo T
    ObservableList<T> listarTodos() throws Exception;

    // Método para insertar un elemento de tipo T
    void insertar(T t) throws Exception;

    // Método para eliminar un elemento de tipo T
    void eliminar(T t) throws Exception;

    // Método para modificar un elemento de tipo T
    void modificar(T t) throws Exception;
}
