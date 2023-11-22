package com.mspdevs.mspfxmaven.model;

public class DetalleCompra extends Producto {
    private int id;
    private int cantidad;
    private double precio;
    private Compra facturaCompra;
    private Producto producto;

    public DetalleCompra() {
        this.facturaCompra = new Compra();
        this.producto = new Producto();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
		this.id = id;
	}

	public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public double getPrecio() {
        return precio;
    }
	
	public void setPrecio(double precio) {
		this.precio = precio;
	}

    public Compra getFacturaCompra() {
        return facturaCompra;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}