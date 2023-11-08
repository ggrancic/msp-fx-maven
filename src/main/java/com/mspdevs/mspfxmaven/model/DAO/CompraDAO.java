package com.mspdevs.mspfxmaven.model.DAO;

import java.sql.Date;

import com.mspdevs.mspfxmaven.model.Compra;

import javafx.collections.ObservableList;

public interface CompraDAO extends CRUD<Compra>{
    int insertarCompra(Compra compra) throws Exception;
    ObservableList<Compra> listarConLimit(int inicio, int elementosPorPagina) throws Exception;
    ObservableList<Compra> listarConLimitYFecha(int inicio, int fin, Date fechaInicio, Date fechaFin) throws Exception;
}