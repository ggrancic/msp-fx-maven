package com.mspdevs.mspfxmaven.model.DAO;

import java.sql.PreparedStatement;
import java.time.format.DateTimeFormatter;

import com.mspdevs.mspfxmaven.model.Caja;
import com.mspdevs.mspfxmaven.model.ConexionMySQL;

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

}
