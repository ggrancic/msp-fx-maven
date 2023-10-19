package com.mspdevs.mspfxmaven.model.DAO;

import com.mspdevs.mspfxmaven.model.ConexionMySQL;
import com.mspdevs.mspfxmaven.model.DetalleCompra;
import javafx.collections.ObservableList;
import com.mspdevs.mspfxmaven.model.ConexionMySQL;
import com.mspdevs.mspfxmaven.model.Producto;
import com.mspdevs.mspfxmaven.model.Proveedor;
import com.mspdevs.mspfxmaven.model.Rubro;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class DetalleCompraDAOImpl extends ConexionMySQL implements DetalleCompraDAO{
    @Override
    public ObservableList<DetalleCompra> listarTodos() throws Exception {
        return null;
    }

    @Override
    public void insertar(DetalleCompra detalleCompra) throws Exception {
        try {
            this.conectar();
            PreparedStatement st = this.con.prepareStatement(
                    "INSERT INTO detalle_compra (cantidad, precio, id_factura_compras, id_producto) VALUES (?, ?, ?, ?)"
            );
            st.setInt(1, detalleCompra.getCantidad());
            st.setDouble(2, detalleCompra.getPrecio());
            st.setInt(3, detalleCompra.getIdFacturaCompra());
            st.setInt(4, detalleCompra.getIdProducto());
            st.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            this.cerrarConexion();
        }

    }

    @Override
    public void eliminar(DetalleCompra detalleCompra) throws Exception {

    }

    @Override
    public void modificar(DetalleCompra detalleCompra) throws Exception {

    }
}
