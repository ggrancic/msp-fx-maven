package com.mspdevs.mspfxmaven.model;

import java.util.Date;

public class Venta {
    private int id_factura_ventas;
    private String numeroFactura;
    private String tipo;
    private Date fechaEmision;
    private double subtotal;
    private double iva;
    private double total;
    private Cliente cliente;
    private Empleado empleado;

    public Venta() {
    	this.cliente = new Cliente();
    	this.empleado = new Empleado();
    }

    public int getId_factura_ventas() {
        return id_factura_ventas;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public String getTipo() {
        return tipo;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getIva() {
        return iva;
    }

    public double getTotal() {
        return total;
    }

    public void setId_factura_ventas(int id_factura_ventas) {
        this.id_factura_ventas = id_factura_ventas;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public void setTotal(double total) {
        this.total = total;
    }

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

}
