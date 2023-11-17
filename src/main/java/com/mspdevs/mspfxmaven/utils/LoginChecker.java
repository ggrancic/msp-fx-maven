package com.mspdevs.mspfxmaven.utils;

import com.mspdevs.mspfxmaven.model.Empleado;
import com.mspdevs.mspfxmaven.model.DAO.EmpleadoDAOImpl;

import javafx.collections.ObservableList;

/**
 * Clase encargada de manejar la logica de login.
 */
public class LoginChecker {
	private boolean estado;
	private String username;
	private String password;
	private Empleado empleado;
	
	
	public LoginChecker(String username, String password) {
		this.username = username;
		this.password = password;
		this.estado = false;
	}

	public boolean getEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	/**
	 * Trae la lista de empleados de BD para luego checkear si existe. En el caso que exista
	 * pone el estado en {@code true} y hace la instancia de {@code Empleado} correspondiente.
	 */
	public void autenticarEmpleado() {
		EmpleadoDAOImpl empleados = new EmpleadoDAOImpl();
        ObservableList<Empleado> listaEmpleados = null;

		try {
			listaEmpleados = empleados.listarTodos();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        for (Empleado empleado : listaEmpleados) {
			if (this.username.equals(empleado.getNombre_usuario()) && this.password.equals(empleado.getClave())) {
				try {
					this.empleado = empleado;
					this.estado = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
