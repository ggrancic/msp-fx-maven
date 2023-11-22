package com.mspdevs.mspfxmaven.model.DAO;

import com.mspdevs.mspfxmaven.model.Compra;
import com.mspdevs.mspfxmaven.model.ConexionMySQL;
import com.mspdevs.mspfxmaven.model.Venta;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class VentaDAOImpl extends ConexionMySQL implements VentaDAO{
    @Override
    public ObservableList<Venta> listarTodos() throws Exception {
        return null;
    }

    @Override
    public void insertar(Venta venta) throws Exception {

    }

    @Override
    public void eliminar(Venta venta) throws Exception {

    }

    @Override
    public void modificar(Venta venta) throws Exception {

    }

    @Override
    public int insertarVenta(Venta venta) throws Exception {
        int idVentaGenerada = -1; // Valor predeterminado en caso de error

        try {
            this.conectar();

            PreparedStatement st = this.con.prepareStatement(
                    "INSERT INTO factura_ventas (numero, tipo, fechaDeEmision, subtotal, iva, total, cliente, empleado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            st.setString(1, venta.getNumeroFactura());
            st.setString(2, venta.getTipo());
            st.setDate(3, java.sql.Date.valueOf(String.valueOf(venta.getFechaEmision())));
            st.setDouble(4, venta.getSubtotal());
            st.setDouble(5, venta.getIva());
            st.setDouble(6, venta.getTotal());
            st.setInt(7, venta.getCliente().getIdCliente());
            st.setInt(8, venta.getEmpleado().getId_empleado());

            int filasAfectadas = st.executeUpdate();
            if (filasAfectadas == 0) {
                throw new Exception("La inserción de la venta falló, no se insertaron filas.");
            }

            // Obtener el ID de la venta generada
            ResultSet generatedKeys = st.getGeneratedKeys();
            if (generatedKeys.next()) {
                idVentaGenerada = generatedKeys.getInt(1);
            } else {
                throw new Exception("No se pudo obtener el ID de la venta generada.");
            }

        } catch (Exception e) {
            
            e.printStackTrace();
        } finally {
            this.cerrarConexion();
        }

        return idVentaGenerada;
    }

    public String obtenerUltimoNumeroFactura() throws Exception {
        String ultimoNumeroFactura = "0";

        try {
            this.conectar();
            PreparedStatement consulta = this.con.prepareStatement(
                    "SELECT MAX(numero) FROM factura_ventas"
            );
            ResultSet resultado = consulta.executeQuery();

            if (resultado.next()) {
                ultimoNumeroFactura = resultado.getString(1);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            this.cerrarConexion();
        }

        return ultimoNumeroFactura;
    }
    
    public ObservableList<Venta> listarConLimitYFecha(int inicio, int elementosPorPagina, Date fechaInicio, Date fechaFin) throws Exception {
    	
    	ObservableList<Venta> lista = null;
		
		try {
			this.conectar();
			String sql = "SELECT fv.id_factura_ventas,fv.numero, fv.tipo, fv.fechaDeEmision, " +
                    "fv.subtotal, fv.iva, fv.total, persona.razon_social, cliente.id_cliente  " +
                    "FROM factura_ventas fv " +
                    "JOIN clientes cliente ON fv.cliente = cliente.id_cliente " +
                    "JOIN personas persona ON cliente.idpersona = persona.id_persona ";
			
			 if (fechaInicio != null && fechaFin != null) {
		            sql += "WHERE fv.fechaDeEmision BETWEEN ? AND ? ";
		            
		        }
		        
		        sql += "ORDER BY fv.id_factura_ventas DESC LIMIT ?,?";
		        
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
		        	Venta venta = new Venta();
		        	venta.setId_factura_ventas(rs.getInt("fv.id_factura_ventas"));
		        	venta.setFechaEmision(rs.getDate("fv.fechaDeEmision"));
		        	venta.setNumeroFactura(rs.getString("fv.numero"));
		        	venta.setTipo(rs.getString("fv.tipo"));
		        	venta.setSubtotal(rs.getDouble("fv.subtotal"));
		        	venta.setIva(rs.getDouble("fv.iva"));
		        	venta.setTotal(rs.getDouble("fv.total"));
		        	venta.getCliente().setRazonSocial(rs.getString("persona.razon_social"));
		        	
		        	lista.add(venta);
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
