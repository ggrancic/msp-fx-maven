package com.mspdevs.mspfxmaven.model.DAO;

import com.mspdevs.mspfxmaven.model.Cliente;
import com.mspdevs.mspfxmaven.model.Compra;

public interface CompraDAO extends CRUD<Compra>{
    int insertarCompra(Compra compra) throws Exception;
}
