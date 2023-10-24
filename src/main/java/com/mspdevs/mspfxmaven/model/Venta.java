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
    int idClienteFK;
    private String clienteNombre;

    int idEmpleadoFK;

    public Venta(Venta venta, Cliente cliente) {
        this.id_factura_ventas = venta.id_factura_ventas;
        this.numeroFactura = numeroFactura;
        this.tipo = tipo;
        this.fechaEmision = fechaEmision;
        this.subtotal = subtotal;
        this.iva = iva;
        this.total = total;
        this.idClienteFK = cliente.getIdCliente();
        this.clienteNombre = cliente.getNombre();
    }

    public Venta() {
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

    public int getIdClienteFK() {
        return idClienteFK;
    }
    public void setIdClienteFK(int idClienteFK) {
        this.idClienteFK = idClienteFK;
    }
    public String getClienteNombre() {
        return clienteNombre;
    }
    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }


    public int getIdEmpleadoFK() {
        return idEmpleadoFK;
    }
    public void setIdEmpleadoFK(int idEmpleadoFK) {
        this.idEmpleadoFK = idEmpleadoFK;
    }
}
