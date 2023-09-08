package com.mspdevs.mspfxmaven.model.DAO;

import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public interface CRUD<T> {
    ObservableList<T> listarTodos() throws Exception;
    void insertar(T t) throws Exception;
    void eliminar(T t) throws Exception;
    void modificar(T t) throws Exception;
}
