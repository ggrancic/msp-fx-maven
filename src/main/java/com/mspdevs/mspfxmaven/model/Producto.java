package com.mspdevs.mspfxmaven.model;

public class Producto {
    private int idProducto;
    private String nombre;
    private double precioVenta;
    private double precioLista;
    private String codigoBarra;
    private int cantidadDisponible;
    private int cantidadMinima;
    int idRubroFK;
    int idProveedorFK;
    private String proveedorNombre;
    private String rubroNombre;

    private int cantidadVendida;
    private double totalVendido;



    //Agregado recien
    public Producto(Producto producto, Rubro rubro, Proveedor proveedor) {
        this.idProducto = producto.getIdProducto();
        this.nombre = producto.getNombre();
        this.precioVenta = producto.getPrecioVenta();
        this.codigoBarra = producto.getCodigoBarra();
        this.cantidadDisponible = producto.getCantidadDisponible();
        this.cantidadMinima = producto.getCantidadMinima();
        this.idRubroFK = rubro.getIdRubro();
        this.idProveedorFK = proveedor.getIdProveedor();
        this.proveedorNombre = proveedor.getNombre();
        this.rubroNombre = rubro.getNombre();

        // Agregado para ventas
        this.totalVendido = producto.getTotalVendido();
        //this.precioLista = producto.getPrecioLista();
    }

    //Agregado recien
    public Producto() {
    }
    public int getIdProducto() {
        return idProducto;
    }
    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public int getCantidadDisponible() {
        return cantidadDisponible;
    }
    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }
    public int getCantidadMinima() {
        return cantidadMinima;
    }
    public void setCantidadMinima(int cantidadMinima) {
        this.cantidadMinima = cantidadMinima;
    }
    public double getPrecioVenta() {
        return precioVenta;
    }
    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }
    public String getCodigoBarra() {
        return codigoBarra;
    }
    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }
    public int getIdRubroFK() {
        return idRubroFK;
    }
    public void setIdRubroFK(int idRubroFK) {
        this.idRubroFK = idRubroFK;
    }
    public int getIdProveedorFK() {
        return idProveedorFK;
    }
    public void setIdProveedorFK(int idProveedorFK) {
        this.idProveedorFK = idProveedorFK;
    }
    public String getProveedorNombre() {
        return proveedorNombre;
    }
    public void setProveedorNombre(String proveedorNombre) {
        this.proveedorNombre = proveedorNombre;
    }
    public String getRubroNombre() {
        return rubroNombre;
    }
    public void setRubroNombre(String rubroNombre) {
        this.rubroNombre = rubroNombre;
    }




    public int getCantidadVendida() {
        return cantidadVendida;
    }
    public void setCantidadVendida(int cantidadVendida) {
        this.cantidadVendida = cantidadVendida;
    }
    public double getTotalVendido() {
        return totalVendido;
    }
    public void setTotalVendido(double totalVendido) {
        this.totalVendido= totalVendido;
    }

    public double getPrecioLista() {
        return precioLista;
    }
    public void setPrecioLista(double precioLista) {
        this.precioLista = precioLista;
    }

}
