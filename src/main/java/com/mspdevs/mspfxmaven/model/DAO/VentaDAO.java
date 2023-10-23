package com.mspdevs.mspfxmaven.model.DAO;

import com.mspdevs.mspfxmaven.model.Compra;
import com.mspdevs.mspfxmaven.model.Venta;

public interface VentaDAO extends CRUD<Venta>{
    int insertarVenta(Venta venta) throws Exception;
}