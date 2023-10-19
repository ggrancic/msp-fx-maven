package com.mspdevs.mspfxmaven.model.DAO;

import com.mspdevs.mspfxmaven.model.ConexionMySQL;
import com.mspdevs.mspfxmaven.model.Producto;
import com.mspdevs.mspfxmaven.model.Proveedor;
import com.mspdevs.mspfxmaven.model.Rubro;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class ProductoDAOImpl extends ConexionMySQL implements ProductoDAO {
    @Override
    public ObservableList<Producto> listarTodos() throws Exception {
        ObservableList<Producto> listaTodoEnUno = null;
        try {
            this.conectar();
            PreparedStatement st = this.con.prepareStatement(
                    "SELECT p.*, r.*, pr.*, pe.nombre AS proveedor_nombre " +
                            "FROM productos p " +
                            "JOIN rubros r ON p.id_rubro = r.id_rubro " +
                            "JOIN proveedores pr ON p.id_proveedor = pr.id_proveedor " +
                            "JOIN personas pe ON pr.id_persona = pe.id_persona"
            );

            listaTodoEnUno = FXCollections.observableArrayList();
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(rs.getInt("p.id_producto"));
                producto.setNombre(rs.getString("p.nombre"));
                producto.setPrecioVenta(rs.getDouble("p.precio_venta"));
                producto.setCodigoBarra(rs.getString("p.codigo_barra"));
                producto.setCantidadMinima(rs.getInt("p.cantidad_minima"));
                producto.setCantidadDisponible(rs.getInt("p.cantidad_disponible"));
                producto.setPrecioLista(rs.getDouble("p.precio_lista"));

                Rubro rubro = new Rubro();
                rubro.setIdRubro(rs.getInt("r.id_rubro"));
                rubro.setNombre(rs.getString("r.nombre"));

                Proveedor proveedor = new Proveedor();
                proveedor.setIdProveedor(rs.getInt("pr.id_proveedor"));
                proveedor.setNombre(rs.getString("proveedor_nombre")); // Obtiene el nombre del proveedor

                Producto todo = new Producto(producto, rubro, proveedor);
                listaTodoEnUno.add(todo);
            }
            /*
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
                listaTodoEnUno.add(todo);*/

            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace(); // Muestra detalles de la excepción en la consola
            throw e; // Relanza la excepción para que se propague
        } finally {
            this.cerrarConexion();
        }
        return listaTodoEnUno;
    }

    @Override
    public void insertar(Producto producto) throws Exception {
        try {
            this.conectar();
            String consultaSiExiste = "SELECT nombre FROM productos WHERE nombre = ?";
            PreparedStatement miSt = this.con.prepareStatement(consultaSiExiste);
            miSt.setString(1, producto.getNombre());
            ResultSet result = miSt.executeQuery();

            // Inicializo el id...
            int idProductoFK = 0;

            if (result.next()) {
                idProductoFK = result.getInt("id_producto");
                miSt.close();
            } else {
                PreparedStatement st = this.con.prepareStatement("INSERT INTO productos (nombre, precio_venta, codigo_barra, cantidad_disponible, cantidad_minima" +
                        ", id_rubro, id_proveedor) VALUES (?, ?, ?, ?, ?, ?, ?)");
                st.setString(1, producto.getNombre());
                st.setDouble(2, producto.getPrecioVenta());
                st.setString(3, producto.getCodigoBarra());
                st.setInt(4, producto.getCantidadDisponible());
                st.setInt(5, producto.getCantidadMinima());
                st.setInt(6, producto.getIdRubroFK());
                st.setInt(7, producto.getIdProveedorFK());
                st.executeUpdate();
            }
        } catch (Exception e) {
            throw e;
        } finally {
            this.cerrarConexion();
        }
    }

    @Override
    public void eliminar(Producto producto) throws Exception {
        try {
            this.conectar();
            PreparedStatement st = this.con.prepareStatement("DELETE FROM productos WHERE id_producto = ?");
            st.setInt(1, producto.getIdProducto());
            st.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            this.cerrarConexion();
        }
    }

    @Override
    public void modificar(Producto producto) throws Exception {
        try {
            this.conectar();
            // Aquí realizas la actualización del producto en la base de datos
            PreparedStatement statement = con.prepareStatement("UPDATE productos SET nombre = ?, precio_venta = ?, codigo_barra = ?, " +
                    "cantidad_disponible = ?, cantidad_minima = ?, id_rubro = ?, id_proveedor = ? WHERE id_producto = ?");
            statement.setString(1, producto.getNombre());
            statement.setDouble(2, producto.getPrecioVenta());
            statement.setString(3, producto.getCodigoBarra());
            statement.setInt(4, producto.getCantidadDisponible());
            statement.setInt(5, producto.getCantidadMinima());
            statement.setInt(6, producto.getIdRubroFK());
            statement.setInt(7, producto.getIdProveedorFK());
            statement.setInt(8, producto.getIdProducto());

            int filasAfectadas = statement.executeUpdate();

            if (filasAfectadas != 1) {
                throw new Exception("No se pudo modificar el producto.");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            this.cerrarConexion();
        }
    }







    // Nuevos

    public Producto obtenerProductoPorNombre(String nombreProducto) throws Exception {
        Producto producto = null;
        try {
            this.conectar();
            PreparedStatement st = this.con.prepareStatement("SELECT * FROM productos WHERE nombre = ?");
            st.setString(1, nombreProducto);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                producto = new Producto();
                producto.setIdProducto(rs.getInt("id_producto"));
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecioLista(rs.getDouble("precio_lista"));
                producto.setPrecioVenta(rs.getDouble("precio_venta"));
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            this.cerrarConexion();
        }
        return producto;
    }

    public Producto obtenerProductoPorCodigoBarra(String codigoBarraProducto) throws Exception {
        Producto producto = null;
        try {
            this.conectar();
            PreparedStatement st = this.con.prepareStatement("SELECT * FROM productos WHERE codigo_barra = ?");
            st.setString(1, codigoBarraProducto);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                producto = new Producto();
                producto.setIdProducto(rs.getInt("id_producto"));
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecioLista(rs.getDouble("precio_lista"));
                producto.setPrecioVenta(rs.getDouble("precio_venta"));
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            this.cerrarConexion();
        }
        return producto;
    }



    public int obtenerCantidadDisponiblePorId(int idProducto) throws Exception {
        int cantidadDisponible = 0;

        try {
            this.conectar();
            PreparedStatement consulta = this.con.prepareStatement(
                    "SELECT cantidad_disponible FROM productos WHERE id_producto = ?"
            );
            consulta.setInt(1, idProducto);
            ResultSet resultado = consulta.executeQuery();

            if (resultado.next()) {
                cantidadDisponible = resultado.getInt("cantidad_disponible");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            this.cerrarConexion();
        }
        return cantidadDisponible;
    }



    public void actualizarProducto(int idProducto, double nuevoPrecioDeLista, double nuevoPrecioDeVenta, int nuevaCantidadDisponible, int nuevoProveedorId) throws Exception {
        try {
            this.conectar();

            PreparedStatement st = this.con.prepareStatement(
                    "UPDATE productos SET precio_lista = ?, precio_venta = ?, cantidad_disponible = ?, id_proveedor = ? WHERE id_producto = ?"
            );
            st.setDouble(1, nuevoPrecioDeLista);
            st.setDouble(2, nuevoPrecioDeVenta);
            st.setInt(3, nuevaCantidadDisponible);
            st.setInt(4, nuevoProveedorId);  // Actualiza el ID del proveedor
            st.setInt(5, idProducto);

            int filasAfectadas = st.executeUpdate();
            if (filasAfectadas != 1) {
                throw new Exception("No se pudo actualizar el producto con ID: " + idProducto);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            this.cerrarConexion();
        }
    }

}
