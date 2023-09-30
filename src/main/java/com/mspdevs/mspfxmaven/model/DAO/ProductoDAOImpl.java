package com.mspdevs.mspfxmaven.model.DAO;

import com.mspdevs.mspfxmaven.model.ConexionMySQL;
import com.mspdevs.mspfxmaven.model.Producto;
import com.mspdevs.mspfxmaven.model.Proveedor;
import com.mspdevs.mspfxmaven.model.Rubro;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProductoDAOImpl extends ConexionMySQL implements ProductoDAO {
    @Override
    public ObservableList<Producto> listarTodos() throws Exception {
        ObservableList<Producto> listaTodoEnUno = null;
        try {
            this.conectar();
            PreparedStatement st = this.con.prepareStatement(
                    "SELECT p.*, r.*, pr.* " +
                            "FROM productos p " +
                            "JOIN rubros r ON p.id_rubro = r.id_rubro " +
                            "JOIN proveedores pr ON p.id_proveedor = pr.id_proveedor"
            );
            listaTodoEnUno = FXCollections.observableArrayList();
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(rs.getInt("p.id_producto"));
                producto.setNombre(rs.getString("p.nombre"));
                producto.setPrecioVenta(rs.getDouble("p.precio_venta"));
                producto.setPrecioLista(rs.getDouble("p.precio_lista"));
                producto.setCantidadMinima(rs.getInt("p.cantidad_minima"));
                producto.setCantidadDisponible(rs.getInt("p.cantidad_disponible"));

                Rubro rubro = new Rubro();
                rubro.setIdRubro(rs.getInt("r.id_rubro"));
                rubro.setNombre(rs.getString("r.nombre"));

                Proveedor proveedor = new Proveedor();
                proveedor.setIdProveedor(rs.getInt("pr.id_proveedor"));
                proveedor.setNombre(rs.getString("pr.nombre"));

                Producto todo = new Producto(producto, rubro, proveedor);
                listaTodoEnUno.add(todo);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            throw e;
        } finally {
            this.cerrarConexion();
        }
        return listaTodoEnUno;
        /*
        ObservableList<Rubro> lista = null;
        try {
            this.conectar();
            PreparedStatement st = this.con.prepareStatement("SELECT * FROM rubros");
            lista = FXCollections.observableArrayList();
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Rubro r = new Rubro();
                r.setIdRubro(rs.getInt("id_rubro"));
                r.setNombre(rs.getString("nombre"));
                lista.add(r);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            throw e;
        } finally {
            this.cerrarConexion();
        }
        return lista;*/
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
