package com.mspdevs.mspfxmaven.model.DAO;

import com.mspdevs.mspfxmaven.model.*;
import javafx.collections.ObservableList;
import com.mspdevs.mspfxmaven.model.ConexionMySQL;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DetalleCompraDAOImpl extends ConexionMySQL implements DetalleCompraDAO{
    @Override
    public ObservableList<DetalleCompra> listarTodos() throws Exception {
		ObservableList<DetalleCompra> listaDetalles = null;

		try {

			this.conectar();
			listaDetalles = FXCollections.observableArrayList();

			PreparedStatement st = this.con.prepareStatement("SELECT detalle.id_detalle_compra, producto.nombre, producto.precio_venta, producto.codigo_barra, detalle.cantidad, detalle.precio, producto.precio_lista "
					+ "FROM mercadito.detalle_compra detalle "
					+ "INNER JOIN mercadito.productos producto ON detalle.id_producto = producto.id_producto "
					+ "INNER JOIN mercadito.factura_compras factura ON detalle.id_factura_compras = factura.id_factura_compras");

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				DetalleCompra detalle = new DetalleCompra();

				detalle.setId(rs.getInt("detalle.id_detalle_compra"));
				detalle.getProducto().setNombre(rs.getString("producto.nombre"));
				detalle.getProducto().setPrecioLista(rs.getDouble("producto.precio_lista"));
				detalle.getProducto().setPrecioVenta(rs.getDouble("producto.precio_venta"));
				detalle.getProducto().setCodigoBarra(rs.getString("producto.codigo_barra"));
				detalle.setCantidad(rs.getInt("detalle.cantidad"));
				detalle.setPrecio(rs.getDouble("detalle.precio"));

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
    public void insertar(DetalleCompra detalleCompra) throws Exception {
        try {
            this.conectar();
            PreparedStatement st = this.con.prepareStatement(
                    "INSERT INTO detalle_compra (cantidad, precio, id_factura_compras, id_producto) VALUES (?, ?, ?, ?)"
            );
            st.setInt(1, detalleCompra.getCantidad());
            st.setDouble(2, detalleCompra.getPrecio());
            st.setInt(3, detalleCompra.getFacturaCompra().getId_factura_compras());
            st.setInt(4, detalleCompra.getProducto().getIdProducto());
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
    
    public ObservableList<DetalleCompra> buscarPorId (int idFacturaCompra) throws SQLException {
    	
    	ObservableList<DetalleCompra> listaDetalles = null;
    		
    	try {
    		this.conectar();
    		
    		listaDetalles = FXCollections.observableArrayList();
    		
    		PreparedStatement st = this.con.prepareStatement("SELECT detalle.id_detalle_compra, detalle.cantidad, detalle.precio, producto.nombre, "
    				+ "producto.precio_lista,producto.precio_venta, fac.id_factura_compras, fac.numero, fac.fecha "
    				+ "FROM detalle_compra detalle "
    				+ "INNER JOIN productos producto on detalle.id_producto = producto.id_producto "
    				+ "INNER JOIN factura_compras fac on detalle.id_factura_compras = fac.id_factura_compras "
    				+ "WHERE fac.id_factura_compras = ?");
    		
    		st.setInt(1, idFacturaCompra);
    		ResultSet rs = st.executeQuery();
    		
    		while (rs.next()) {
    			DetalleCompra detalle = new DetalleCompra();
    			detalle.setId(rs.getInt("detalle.id_detalle_compra"));
    			detalle.setCantidad(rs.getInt("detalle.cantidad"));
    			detalle.getProducto().setPrecioLista(rs.getDouble("producto.precio_lista"));
    			detalle.getProducto().setPrecioVenta(rs.getDouble("producto.precio_venta"));
    			detalle.getProducto().setNombre(rs.getString("producto.nombre"));
    			detalle.setPrecio(rs.getDouble("detalle.precio"));
    			detalle.getFacturaCompra().setId_factura_compras(rs.getInt("fac.id_factura_compras"));
    			detalle.getFacturaCompra().setNumeroFactura(rs.getString("fac.numero"));
    			detalle.getFacturaCompra().setFecha(rs.getDate("fac.fecha"));
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
