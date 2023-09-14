package com.mspdevs.mspfxmaven.model.DAO;

import com.mspdevs.mspfxmaven.model.ConexionMySQL;
import com.mspdevs.mspfxmaven.model.Producto;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAOImpl extends ConexionMySQL implements ProductoDAO {
    @Override
    public ObservableList<Producto> listarTodos() throws Exception {
        return null;
    }

    @Override
    public void insertar(Producto producto) throws Exception {

    }

    @Override
    public void eliminar(Producto producto) throws Exception {

    }

    @Override
    public void modificar(Producto producto) throws Exception {

    }
}
