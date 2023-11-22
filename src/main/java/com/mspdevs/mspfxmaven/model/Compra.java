package com.mspdevs.mspfxmaven.model;

import java.util.Date;

public class Compra {
    private int id_factura_compras;
    private Date fecha;
    private String numeroFactura;
    private String tipo;
    private double subtotal;
    private double totalSinIva;
    private double total;
    private Proveedor proveedor;
    
    public Compra() {
    	this.proveedor = new Proveedor();
    }

    public int getId_factura_compras() {
        return id_factura_compras;
    }

    public Date getFecha() {
        return fecha;
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

    public double getTotalSinIva() {
        return totalSinIva;
    }

    public double getTotal() {
        return total;
    }

    public void setId_factura_compras(int id_factura_compras) {
        this.id_factura_compras = id_factura_compras;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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

    public void setTotalSinIva(double totalSinIva) {
        this.totalSinIva = totalSinIva;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getIdProveedorFK() {
        return proveedor.getIdProveedor();
    }
    public void setIdProveedorFK(int idProveedorFK) {
        this.proveedor.setIdProveedor(idProveedorFK);
    }
    public String getProveedorNombre() {
        return proveedor.getNombre();
    }
    public void setProveedorNombre(String proveedorNombre) {
        this.proveedor.setNombre(proveedorNombre);
    }
}
