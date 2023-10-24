package com.mspdevs.mspfxmaven.model.DAO;

import com.mspdevs.mspfxmaven.model.ConexionMySQL;
import com.mspdevs.mspfxmaven.model.Venta;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class VentaDAOImpl extends ConexionMySQL implements VentaDAO{
    @Override
    public ObservableList<Venta> listarTodos() throws Exception {
        return null;
    }

    @Override
    public void insertar(Venta venta) throws Exception {

    }

    @Override
    public void eliminar(Venta venta) throws Exception {

    }

    @Override
    public void modificar(Venta venta) throws Exception {

    }

    @Override
    public int insertarVenta(Venta venta) throws Exception {
        int idVentaGenerada = -1; // Valor predeterminado en caso de error

        try {
            this.conectar();
            // Inserta la venta en la tabla "factura_ventas"
            PreparedStatement st = this.con.prepareStatement(
                    "INSERT INTO factura_ventas (numero, tipo, fechaDeEmision, subtotal, iva, total, cliente, empleado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            st.setString(1, venta.getNumeroFactura());
            st.setString(2, venta.getTipo());
            st.setDate(3, java.sql.Date.valueOf(String.valueOf(venta.getFechaEmision())));
            st.setDouble(4, venta.getSubtotal());
            st.setDouble(5, venta.getIva());
            st.setDouble(6, venta.getTotal());
            st.setInt(7, venta.getIdClienteFK());
            st.setInt(8, venta.getIdEmpleadoFK());

            int filasAfectadas = st.executeUpdate();
            if (filasAfectadas == 0) {
                throw new Exception("La inserción de la venta falló, no se insertaron filas.");
            }

            // Obtener el ID de la venta generada
            ResultSet generatedKeys = st.getGeneratedKeys();
            if (generatedKeys.next()) {
                idVentaGenerada = generatedKeys.getInt(1);
            } else {
                throw new Exception("No se pudo obtener el ID de la venta generada.");
            }

            // Resto de tu código para insertar en "detalle_ventas y otras operaciones.

        } catch (Exception e) {
            throw e;
        } finally {
            this.cerrarConexion();
        }

        return idVentaGenerada; // Devuelve el ID de la venta generada
    }




    public String obtenerUltimoNumeroFactura() throws Exception {
        String ultimoNumeroFactura = "0";

        try {
            this.conectar();
            PreparedStatement consulta = this.con.prepareStatement(
                    "SELECT MAX(numero) FROM factura_ventas"
            );
            ResultSet resultado = consulta.executeQuery();

            if (resultado.next()) {
                ultimoNumeroFactura = resultado.getString(1);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            this.cerrarConexion();
        }

        return ultimoNumeroFactura;
    }
}
