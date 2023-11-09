package com.mspdevs.mspfxmaven.model.DAO;

import com.mspdevs.mspfxmaven.model.ConexionMySQL;
import com.mspdevs.mspfxmaven.model.DetalleCompra;
import com.mspdevs.mspfxmaven.model.DetalleVenta;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DetalleVentaDAOImpl extends ConexionMySQL implements DetalleVentaDAO{
    @Override
    public ObservableList<DetalleVenta> listarTodos() throws Exception {
        
    	ObservableList<DetalleVenta> listaDetalles = null;
    	
    	try {
			
    		this.conectar();
    		listaDetalles = FXCollections.observableArrayList();
    		
    		PreparedStatement st = this.con.prepareStatement("SELECT detalle.id_venta_producto, producto.nombre, producto.precio_venta, producto.codigo_barra, detalle.cantidad, detalle.monto "
    				+ "FROM mercadito.detalle_venta detalle "
    				+ "INNER JOIN mercadito.productos producto ON detalle.id_producto = producto.id_producto "
    				+ "INNER JOIN mercadito.factura_ventas factura ON detalle.id_factura_ventas = factura.id_factura_ventas");
    		
    		ResultSet rs = st.executeQuery();
    		
    		while (rs.next()) {
    			DetalleVenta detalle = new DetalleVenta();
    			
    			detalle.setIdDetalleVenta(rs.getInt("detalle.id_venta_producto"));
    			detalle.getProducto().setNombre(rs.getString("producto.nombre"));
    			detalle.getProducto().setPrecioVenta(rs.getDouble("producto.precio_venta"));
    			detalle.getProducto().setCodigoBarra(rs.getString("producto.codigo_barra"));
    			detalle.setCantidad(rs.getInt("detalle.cantidad"));
    			detalle.setMonto(rs.getDouble("detalle.monto"));
    			
    			listaDetalles.add(detalle);
    		}
    		
    		rs.close();
    		st.close();
    		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.cerrarConexion();
		}
    	
    	return listaDetalles;
    }

    @Override
    public void insertar(DetalleVenta detalleVenta) throws Exception {
    	try {
            this.conectar();
            PreparedStatement st = this.con.prepareStatement(
                    "INSERT INTO detalle_venta (cantidad, monto, id_factura_ventas, id_producto) VALUES (?, ?, ?, ?)"
            );
            st.setInt(1, detalleVenta.getCantidad());
            st.setDouble(2, detalleVenta.getMonto());
            st.setInt(3, detalleVenta.getFacturaVenta().getId_factura_ventas());
            st.setInt(4, detalleVenta.getProducto().getIdProducto());
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
    
public ObservableList<DetalleVenta> buscarPorId (int idFacturaVenta) throws SQLException {
    	
    	ObservableList<DetalleVenta> listaDetalles = null;
    		
    	try {
    		this.conectar();
    		
    		listaDetalles = FXCollections.observableArrayList();
    		
    		PreparedStatement st = this.con.prepareStatement("SELECT detalle.id_venta_producto, producto.nombre, producto.precio_venta, producto.codigo_barra, detalle.cantidad, detalle.monto "
    				+ "FROM mercadito.detalle_venta detalle "
    				+ "INNER JOIN mercadito.productos producto ON detalle.id_producto = producto.id_producto "
    				+ "INNER JOIN mercadito.factura_ventas factura ON detalle.id_factura_ventas = factura.id_factura_ventas "
    				+ "WHERE factura.id_factura_ventas = ?");
    		
    		st.setInt(1, idFacturaVenta);
    		ResultSet rs = st.executeQuery();
    		
    		while (rs.next()) {
    			DetalleVenta detalle = new DetalleVenta();
    			detalle.setIdDetalleVenta(rs.getInt("detalle.id_venta_producto"));
    			detalle.setCantidad(rs.getInt("detalle.cantidad"));
    			detalle.getProducto().setPrecioVenta(rs.getDouble("producto.precio_venta"));
    			detalle.getProducto().setNombre(rs.getString("producto.nombre"));
    			detalle.getProducto().setCodigoBarra(rs.getString("producto.codigo_barra"));
    			detalle.setMonto(rs.getDouble("detalle.monto"));
    			listaDetalles.add(detalle);
    		}
    		
    		rs.close();
            st.close();
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
    		this.cerrarConexion();
    	}
    	
    	return listaDetalles;
    }
}
