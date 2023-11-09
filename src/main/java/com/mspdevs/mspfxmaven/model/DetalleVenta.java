package com.mspdevs.mspfxmaven.model;

public class DetalleVenta {
    private int idDetalleVenta;
    private int cantidad;
    private Venta facturaVenta;
    private double monto;


	private Producto producto;

    public DetalleVenta() {
    	this.facturaVenta = new Venta();
    	this.producto = new Producto();
    }

    public int getIdDetalleVenta() {
        return idDetalleVenta;
    }

    public void setIdDetalleVenta(int idDetalleVenta) {
        this.idDetalleVenta = idDetalleVenta;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    public Venta getFacturaVenta() {
		return facturaVenta;
	}

	public void setFacturaVenta(Venta facturaVenta) {
		this.facturaVenta = facturaVenta;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}
	
}
