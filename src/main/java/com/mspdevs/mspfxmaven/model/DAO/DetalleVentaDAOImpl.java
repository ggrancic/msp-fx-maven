package com.mspdevs.mspfxmaven.model.DAO;

import com.mspdevs.mspfxmaven.model.ConexionMySQL;
import com.mspdevs.mspfxmaven.model.DetalleVenta;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;

public class DetalleVentaDAOImpl extends ConexionMySQL implements DetalleVentaDAO{
    @Override
    public ObservableList<DetalleVenta> listarTodos() throws Exception {
        return null;
    }

    @Override
    public void insertar(DetalleVenta detalleVenta) throws Exception {
        try {
            this.conectar();
            PreparedStatement st = this.con.prepareStatement(
                    "INSERT INTO detalle_venta (cantidad, id_producto, id_factura_ventas) VALUES (?, ?, ?)"
            );
            st.setInt(1, detalleVenta.getCantidad());
            st.setInt(2, detalleVenta.getIdProducto());
            st.setInt(3, detalleVenta.getIdFacturaVenta());
            st.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            this.cerrarConexion();
        }
    }

    @Override
    public void eliminar(DetalleVenta detalleVenta) throws Exception {

    }

    @Override
    public void modificar(DetalleVenta detalleVenta) throws Exception {

    }
}
