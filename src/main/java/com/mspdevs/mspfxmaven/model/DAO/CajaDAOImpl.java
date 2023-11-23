package com.mspdevs.mspfxmaven.model.DAO;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

import com.mspdevs.mspfxmaven.model.Caja;
import com.mspdevs.mspfxmaven.model.ConexionMySQL;
import com.mspdevs.mspfxmaven.model.Venta;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CajaDAOImpl extends ConexionMySQL implements CajaDAO {

	@Override
	public ObservableList<Caja> listarTodos() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertar(Caja cajaAGuardar) throws Exception {
		// TODO Auto-generated method stub
		String fechaInFormateada = cajaAGuardar.getFechaHoraApertura().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		String fechaFinFormateada = cajaAGuardar.getFechaHoraCierre().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		try {
			this.conectar();
			PreparedStatement st = this.con.prepareStatement("INSERT INTO cajas (fechaHoraApertura, fechaHoraCierre, montoInicial, ingresos, egresos, montoFinal, responsable)"
					+ " VALUES (?,?,?,?,?,?,?)");
			st.setString(1, fechaInFormateada);
			st.setString(2, fechaFinFormateada);
			st.setDouble(3, cajaAGuardar.getMontoInicial());
			st.setDouble(4, cajaAGuardar.getIngresos());
			st.setDouble(5, cajaAGuardar.getEgresos());
			st.setDouble(6, cajaAGuardar.getMontoFinal());
			st.setInt(7, cajaAGuardar.getResponsable().getId_empleado());
			
			int filasAfectadas = st.executeUpdate();
            if (filasAfectadas == 0) {
                throw new Exception("La inserción de la venta falló, no se insertaron filas.");
            }
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.cerrarConexion();
		}
	}

	@Override
	public void eliminar(Caja t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void modificar(Caja t) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	 public ObservableList<Caja> listarConLimitYFecha(int inicio, int elementosPorPagina, Date fechaInicio, Date fechaFin) throws Exception {
	    	
	    	ObservableList<Caja> lista = null;
			
			try {
				this.conectar();
				String sql = "select * from cajas ";
				
				 if (fechaInicio != null && fechaFin != null) {
			            sql += "WHERE DATE(fechaHoraApertura) BETWEEN ? AND ? ";
			            
			        }
			        
			        sql += "ORDER BY idCaja DESC LIMIT ?,?";
			        
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
			        	Caja caja = new Caja();
			        	caja.setFechaHoraApertura(rs.getTimestamp("fechaHoraApertura").toLocalDateTime());
			        	caja.setFechaHoraCierre(rs.getTimestamp("fechaHoraCierre").toLocalDateTime());
			        	caja.setMontoFinal(rs.getDouble("montoInicial"));
			        	caja.setMontoFinal(rs.getDouble("montoFinal"));
			        	caja.setIngresos(rs.getDouble("ingresos"));
			        	caja.setEgresos(rs.getDouble("egresos"));
			        	caja.getResponsable().setNombre_usuario("responsable");
			        	
			        	
			        	lista.add(caja);
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
	 
	 public int obtenerCantidadDeCompras(Date fechaInicio, Date fechaFin) throws SQLException {
			int total = 0;
			
			try {
				this.conectar();
				PreparedStatement st = this.con.prepareStatement("select count(*) from cajas where DATE(fechaHoraApertura) between ? and ?");
				
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

}
