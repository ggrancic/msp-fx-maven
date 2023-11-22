package com.mspdevs.mspfxmaven.model.DAO;

import com.mspdevs.mspfxmaven.model.Compra;
import com.mspdevs.mspfxmaven.model.ConexionMySQL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class CompraDAOImpl extends ConexionMySQL implements CompraDAO{
    @Override
    public ObservableList<Compra> listarTodos() throws Exception {
        ObservableList<Compra> listaTodoEnUno = null;
        try {
            this.conectar();
            PreparedStatement st = this.con.prepareStatement(
                    "SELECT c.*, pe.nombre AS proveedor_nombre, pr.id_proveedor " +
                            "FROM factura_compras c " +
                            "JOIN proveedores pr ON c.proveedor = pr.id_proveedor " +
                            "JOIN personas pe ON pr.id_persona = pe.id_persona"
            );

            listaTodoEnUno = FXCollections.observableArrayList();
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Compra compra = new Compra();
                compra.setId_factura_compras(rs.getInt("c.id_factura_compras"));
                compra.setFecha(rs.getDate("c.fecha"));
                compra.setNumeroFactura(rs.getString("c.numero"));
                compra.setTipo(rs.getString("c.tipo"));
                compra.setSubtotal(rs.getDouble("c.subtotal"));
                compra.setTotalSinIva(rs.getDouble("c.totalSinIva"));
                compra.setTotal(rs.getDouble("c.total"));
                compra.setIdProveedorFK(rs.getInt("pr.id_proveedor"));
                compra.setProveedorNombre(rs.getString("proveedor_nombre"));

                listaTodoEnUno.add(compra);
            }

            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            this.cerrarConexion();
        }
        return listaTodoEnUno;
    }

    @Override
    public int insertarCompra(Compra compra) throws Exception {
        int idCompraGenerada = -1; // Valor predeterminado en caso de error

        try {
            this.conectar();
            PreparedStatement st = this.con.prepareStatement(
                    "INSERT INTO factura_compras (fecha, numero, tipo, subtotal, totalSinIva, total, proveedor) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            st.setDate(1, java.sql.Date.valueOf(String.valueOf(compra.getFecha())));
            st.setString(2, compra.getNumeroFactura());
            st.setString(3, compra.getTipo());
            st.setDouble(4, compra.getSubtotal());
            st.setDouble(5, compra.getTotalSinIva());
            st.setDouble(6, compra.getTotal());
            st.setInt(7, compra.getIdProveedorFK());

            int filasAfectadas = st.executeUpdate();
            if (filasAfectadas == 0) {
                throw new Exception("La inserción de la compra falló, no se insertaron filas.");
            }

            // Obtener el ID de la compra generada
            ResultSet generatedKeys = st.getGeneratedKeys();
            if (generatedKeys.next()) {
                idCompraGenerada = generatedKeys.getInt(1);
            } else {
                throw new Exception("No se pudo obtener el ID de la compra generada.");
            }

            // Resto de tu código para insertar en "detalle_compras" y otras operaciones.

        } catch (Exception e) {
            throw e;
        } finally {
            this.cerrarConexion();
        }

        return idCompraGenerada; // Devuelve el ID de la compra generada
    }

    @Override
    public void eliminar(Compra compra) throws Exception {
        // Nada por el momento
    }

    @Override
    public void modificar(Compra compra) throws Exception {
        // No se va a modificar
    }

	@Override
	public void insertar(Compra t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ObservableList<Compra> listarConLimit(int inicio, int elementosPorPagina) throws Exception {
	    ObservableList<Compra> listaTodoEnUno = null;
        try {
            this.conectar();
            PreparedStatement st = this.con.prepareStatement(
                    "SELECT c.*, pe.nombre AS proveedor_nombre, pr.id_proveedor " +
                            "FROM factura_compras c " +
                            "JOIN proveedores pr ON c.proveedor = pr.id_proveedor " +
                            "JOIN personas pe ON pr.id_persona = pe.id_persona ORDER BY c.id_factura_compras DESC LIMIT ?,?"
            );
            
            st.setInt(1, inicio);
            st.setInt(2, elementosPorPagina);

            listaTodoEnUno = FXCollections.observableArrayList();
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Compra compra = new Compra();
                compra.setId_factura_compras(rs.getInt("c.id_factura_compras"));
                compra.setFecha(rs.getDate("c.fecha"));
                compra.setNumeroFactura(rs.getString("c.numero"));
                compra.setTipo(rs.getString("c.tipo"));
                compra.setSubtotal(rs.getDouble("c.subtotal"));
                compra.setTotalSinIva(rs.getDouble("c.totalSinIva"));
                compra.setTotal(rs.getDouble("c.total"));
                compra.setIdProveedorFK(rs.getInt("pr.id_proveedor"));
                compra.setProveedorNombre(rs.getString("proveedor_nombre"));

                listaTodoEnUno.add(compra);
            }

            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            this.cerrarConexion();
        }
        return listaTodoEnUno;
	}
	
	public int obtenerCantidadDeCompras(Date fechaInicio, Date fechaFin) throws SQLException {
		int total = 0;
		
		try {
			this.conectar();
			PreparedStatement st = this.con.prepareStatement("SELECT COUNT(*) as total_registros "
					+ "FROM factura_compras c "
					+ "JOIN proveedores pr ON c.proveedor = pr.id_proveedor "
					+ "JOIN personas pe ON pr.id_persona = pe.id_persona "
					+ "WHERE c.fecha BETWEEN ? AND ? "
					+ "ORDER BY c.id_factura_compras DESC");
			
			st.setDate(1, fechaInicio);
			st.setDate(2, fechaFin);
			ResultSet rs = st.executeQuery();
			
			if(rs.next()) {
				total = rs.getInt(1);
			}
			
			rs.close();
            st.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.cerrarConexion();
		}
		
		return total;
	}

	@Override
	public ObservableList<Compra> listarConLimitYFecha(int inicio, int elementosPorPagina, Date fechaInicio, Date fechaFin) throws Exception {
		
		ObservableList<Compra> lista = null;
		
		try {
			this.conectar();
			String sql = "SELECT c.*, persona.razon_social, pr.id_proveedor " +
                    "FROM factura_compras c " +
                    "JOIN proveedores pr ON c.proveedor = pr.id_proveedor " +
                    "JOIN personas persona ON pr.id_persona = persona.id_persona ";
			
			 if (fechaInicio != null && fechaFin != null) {
		            sql += "WHERE c.fecha BETWEEN ? AND ? ";
		            
		        }
		        
		        sql += "ORDER BY c.id_factura_compras DESC LIMIT ?,?";
		        
		        PreparedStatement st = this.con.prepareStatement(sql);
		        
		        int parameterIndex = 1;
		        
		        if (fechaInicio != null && fechaFin != null) {
		            st.setDate(parameterIndex++, new java.sql.Date(fechaInicio.getTime()));
		            st.setDate(parameterIndex++, new java.sql.Date(fechaFin.getTime()));
		        }
		        
		        st.setInt(parameterIndex++, inicio);
		        st.setInt(parameterIndex++, elementosPorPagina);
		        
		        lista = FXCollections.observableArrayList();
		        
		        ResultSet rs = st.executeQuery();
		        while (rs.next()) {
		            Compra compra = new Compra();
		            compra.setId_factura_compras(rs.getInt("c.id_factura_compras"));
		            compra.setFecha(rs.getDate("c.fecha"));
		            compra.setNumeroFactura(rs.getString("c.numero"));
		            compra.setTipo(rs.getString("c.tipo"));
		            compra.setSubtotal(rs.getDouble("c.subtotal"));
		            compra.setTotalSinIva(rs.getDouble("c.totalSinIva"));
		            compra.setTotal(rs.getDouble("c.total"));
                    compra.getProveedor().setRazonSocial(rs.getString("persona.razon_social"));

		            lista.add(compra);
		        }
		        
		        rs.close();
		        st.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			this.cerrarConexion();
		}
		
		return lista;
	}
}
