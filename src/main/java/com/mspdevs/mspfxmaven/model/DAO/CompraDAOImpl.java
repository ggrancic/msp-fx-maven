package com.mspdevs.mspfxmaven.model.DAO;

import com.mspdevs.mspfxmaven.model.Compra;
import com.mspdevs.mspfxmaven.model.ConexionMySQL;
import com.mspdevs.mspfxmaven.model.Proveedor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CompraDAOImpl extends ConexionMySQL implements CompraDAO{
    @Override
    public ObservableList<Compra> listarTodos() throws Exception {
        ObservableList<Compra> listaTodoEnUno = null;
        try {
            this.conectar();
            PreparedStatement st = this.con.prepareStatement(
                    "SELECT c.*, pe.nombre AS proveedor_nombre " +
                            "FROM factura_compras c " +
                            "JOIN proveedores pr ON c.proveedor = pr.id_proveedor " +
                            "JOIN personas pe ON pr.id_persona = pe.id_persona"
            );

            listaTodoEnUno = FXCollections.observableArrayList();
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Compra compra = new Compra();
                compra.setId_factura_compras(rs.getInt("c.id_factura_compras"));
                compra.setFecha(rs.getDate("c.fecha"));
                compra.setNumeroFactura(rs.getString("c.numero"));
                compra.setTipo(rs.getString("c.tipo"));
                compra.setSubtotal(rs.getDouble("c.subtotal"));
                compra.setTotalSinIva(rs.getDouble("c.totalSinIva"));
                compra.setTotal(rs.getDouble("c.total"));

                Proveedor proveedor = new Proveedor();
                proveedor.setIdProveedor(rs.getInt("pr.id_proveedor"));
                proveedor.setNombre(rs.getString("proveedor_nombre")); // Obtiene el nombre del proveedor

                Compra todo = new Compra(compra, proveedor);

                listaTodoEnUno.add(todo);
            }

            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            this.cerrarConexion();
        }
        return listaTodoEnUno;
    }

    public void insertar(Compra compra) throws Exception {

    }

    @Override
    public int insertarCompra(Compra compra) throws Exception {
        int idCompraGenerada = -1; // Valor predeterminado en caso de error

        try {
            this.conectar();
            // Insertar la compra en la tabla "factura_compras"
            PreparedStatement st = this.con.prepareStatement(
                    "INSERT INTO factura_compras (fecha, numero, tipo, subtotal, totalSinIva, total, proveedor) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            st.setDate(1, java.sql.Date.valueOf(String.valueOf(compra.getFecha())));
            st.setString(2, compra.getNumeroFactura());
            st.setString(3, compra.getTipo());
            st.setDouble(4, compra.getSubtotal());
            st.setDouble(5, compra.getTotalSinIva());
            st.setDouble(6, compra.getTotal());
            st.setInt(7, compra.getIdProveedorFK());

            int filasAfectadas = st.executeUpdate();
            if (filasAfectadas == 0) {
                throw new Exception("La inserción de la compra falló, no se insertaron filas.");
            }

            // Obtener el ID de la compra generada
            ResultSet generatedKeys = st.getGeneratedKeys();
            if (generatedKeys.next()) {
                idCompraGenerada = generatedKeys.getInt(1);
            } else {
                throw new Exception("No se pudo obtener el ID de la compra generada.");
            }

            // Resto de tu código para insertar en "detalle_compras" y otras operaciones.

        } catch (Exception e) {
            throw e;
        } finally {
            this.cerrarConexion();
        }

        return idCompraGenerada; // Devuelve el ID de la compra generada



        /*
        try {
            this.conectar();

            // Verificar si la factura de compra ya existe
            String consultaSiExiste = "SELECT id_factura_compras FROM factura_compras WHERE numero = ?";
            PreparedStatement miSt = this.con.prepareStatement(consultaSiExiste);
            miSt.setString(1, compra.getNumeroFactura());
            ResultSet result = miSt.executeQuery();

            int idFacturaCompras = 0; // Inicializar el ID

            if (result.next()) {
                idFacturaCompras = result.getInt("id_factura_compras");
                miSt.close();
            } else {
                // La factura de compra no existe, insertar una nueva
                PreparedStatement st = this.con.prepareStatement(
                        "INSERT INTO factura_compras (fecha, numero, tipo, subtotal, totalSinIva, total, proveedor) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?)");

                st.setDate(1, java.sql.Date.valueOf(String.valueOf(compra.getFecha())));
                st.setString(2, compra.getNumeroFactura());
                st.setString(3, compra.getTipo());
                st.setDouble(4, compra.getSubtotal());
                st.setDouble(5, compra.getTotalSinIva());
                st.setDouble(6, compra.getTotal());
                st.setInt(7, compra.getIdProveedorFK());

                st.executeUpdate();
                // No necesitas obtener el ID generado

                // Puedes seguir con tu lógica si es necesario

            }
        } catch (Exception e) {
            throw e;
        } finally {
            this.cerrarConexion();
        }*/

        /*
        try {
            this.conectar();

            // Verificar si la factura de compra ya existe
            String consultaSiExiste = "SELECT id_factura_compras FROM factura_compras WHERE numero = ?";
            PreparedStatement miSt = this.con.prepareStatement(consultaSiExiste);
            miSt.setString(1, compra.getNumeroFactura());
            ResultSet result = miSt.executeQuery();

            int idFacturaCompras = 0; // Inicializar el ID

            if (result.next()) {
                idFacturaCompras = result.getInt("id_factura_compras");
                miSt.close();
            } else {
                // La factura de compra no existe, insertar una nueva
                PreparedStatement st = this.con.prepareStatement(
                        "INSERT INTO factura_compras (fecha, numero, tipo, subtotal, totalSinIva, total, proveedor) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?)");

                st.setDate(1, java.sql.Date.valueOf(String.valueOf(compra.getFecha())));
                st.setString(2, compra.getNumeroFactura());
                st.setString(3, compra.getTipo());
                st.setDouble(4, compra.getSubtotal());
                st.setDouble(5, compra.getTotalSinIva());
                st.setDouble(6, compra.getTotal());
                st.setInt(7, compra.getIdProveedorFK());

                st.executeUpdate();

                ResultSet generatedKeys = st.getGeneratedKeys();
                if (generatedKeys.next()) {
                    idFacturaCompras = generatedKeys.getInt(1);
                } else {
                    throw new Exception("No se pudo obtener el ID de la factura de compra generada.");
                }
            }

            // Ahora puedes hacer algo con el ID de la factura de compra (por ejemplo, almacenarlo en tu objeto compra).

        } catch (Exception e) {
            throw e;
        } finally {
            this.cerrarConexion();
        }*/

    }

    @Override
    public void eliminar(Compra compra) throws Exception {
        // Nada por el momento
    }

    @Override
    public void modificar(Compra compra) throws Exception {
        // No se va a modificar
    }
}
