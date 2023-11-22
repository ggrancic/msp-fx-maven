package com.mspdevs.mspfxmaven.model;


import java.time.LocalDateTime;
import java.util.Date;

/**
 * Esta clase se encarga de mapear la tabla {@code cajas}
 * de la base de datos
 */

public class Caja {
	
	private int idCaja;
	private LocalDateTime fechaHoraApertura;
	private LocalDateTime fechaHoraCierre;
	private double montoInicial;
	private double montoFinal;
	private double ingresos;
	private double egresos;
	private Empleado responsable;
	private boolean status;

	public Caja() {
		this.responsable = new Empleado();
	}

	public int getIdCaja() {
		return idCaja;
	}

	public void setIdCaja(int idCaja) {
		this.idCaja = idCaja;
	}

	public LocalDateTime getFechaHoraApertura() {
		return fechaHoraApertura;
	}

	public void setFechaHoraApertura(LocalDateTime fechaHoraApertura) {
		this.fechaHoraApertura = fechaHoraApertura;
	}

	public LocalDateTime getFechaHoraCierre() {
		return fechaHoraCierre;
	}

	public void setFechaHoraCierre(LocalDateTime fechaHoraCierre) {
		this.fechaHoraCierre = fechaHoraCierre;
	}

	public double getMontoInicial() {
		return montoInicial;
	}

	public void setMontoInicial(double montoInicial) {
		this.montoInicial = montoInicial;
	}

	public double getMontoFinal() {
		return montoFinal;
	}

	public void setMontoFinal(double montoFinal) {
		this.montoFinal = montoFinal;
	}

	public double getIngresos() {
		return ingresos;
	}

	public void setIngresos(double ingresos) {
		this.ingresos += ingresos;
	}

	public double getEgresos() {
		return egresos;
	}

	public void setEgresos(double egresos) {
		this.egresos += egresos;
	}

	public Empleado getResponsable() {
		return responsable;
	}

	public void setResponsable(Empleado responsable) {
		this.responsable = responsable;
	}
	
	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Caja{" +
				"montoInicial=" + montoInicial +
				'}';
	}
}
