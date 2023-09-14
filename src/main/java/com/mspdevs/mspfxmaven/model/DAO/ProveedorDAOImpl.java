package com.mspdevs.mspfxmaven.model.DAO;

import com.mspdevs.mspfxmaven.model.ConexionMySQL;
import com.mspdevs.mspfxmaven.model.Persona;
import com.mspdevs.mspfxmaven.model.Proveedor;
import com.mspdevs.mspfxmaven.model.Rubro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAOImpl extends ConexionMySQL implements ProveedorDAO {
    @Override
    public ObservableList<Proveedor> listarTodos() throws Exception {
        ObservableList<Proveedor> lista = null;
        try {
            this.conectar();
            // Se crea una consulta SQL que combina las tablas "proveedores" y "personas" mediante un INNER JOIN
            PreparedStatement st = this.con.prepareStatement("SELECT pr.id_proveedor, pr.cuit, p.* FROM proveedores pr JOIN personas p ON pr.id_persona = p.id_persona");
            // Se inicializa la lista donde se almacenarán los resultados
            lista = FXCollections.observableArrayList();
            ResultSet rs = st.executeQuery(); // Ejecuta la consulta SQL
            while (rs.next()) {  // Recorre los resultados
                // se crea una instancia de Proveedor para cada fila de resultados
                Proveedor proveedor = new Proveedor();
                proveedor.setIdProveedor(rs.getInt("id_proveedor"));
                proveedor.setCuit(rs.getString("cuit"));
                proveedor.setIdPersona(rs.getInt("id_persona"));
                proveedor.setNombre(rs.getString("nombre"));
                proveedor.setApellido(rs.getString("apellido"));
                proveedor.setProvincia(rs.getString("provincia"));
                proveedor.setLocalidad(rs.getString("localidad"));
                proveedor.setCalle(rs.getString("calle"));
                proveedor.setDni(rs.getString("dni"));
                proveedor.setMail(rs.getString("mail"));
                proveedor.setTelefono(rs.getString("telefono"));

                /*Persona persona = new Persona();
                persona.setIdPersona(rs.getInt("id_persona"));
                persona.setNombre(rs.getString("nombre"));
                persona.setApellido(rs.getString("apellido"));
                persona.setProvincia(rs.getString("provincia"));
                persona.setLocalidad(rs.getString("localidad"));
                persona.setCalle(rs.getString("calle"));
                persona.setDni(rs.getString("dni"));
                persona.setMail(rs.getString("mail"));
                persona.setTelefono(rs.getString("telefono"));*/

                // Asignar la persona al proveedor
                //proveedor.setPersona(persona);

                lista.add(proveedor); // Agrega el proveedor a la lista
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            throw e;
        } finally {
            // cierra la conexión a la base de datos en el bloque "finally" para asegurarse de que siempre se cierre
            this.cerrarConexion();
        }
        return lista; // Devuelve la lista de proveedores
    }

    @Override
    public void insertar(Proveedor proveedor) throws Exception {
        try {
            this.conectar();

            // Primero, insertamos los datos en la tabla "Personas"
            PreparedStatement stPersonas = this.con.prepareStatement("INSERT INTO personas (nombre, apellido, provincia, localidad, calle, dni, mail, telefono) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            stPersonas.setString(1, proveedor.getNombre());
            stPersonas.setString(2, proveedor.getApellido());
            stPersonas.setString(3, proveedor.getProvincia());
            stPersonas.setString(4, proveedor.getLocalidad());
            stPersonas.setString(5, proveedor.getCalle());
            stPersonas.setString(6, proveedor.getDni());
            stPersonas.setString(7, proveedor.getMail());
            stPersonas.setString(8, proveedor.getTelefono());
            stPersonas.executeUpdate();
            stPersonas.close();

            // Luego, obtenemos el ID generado para la tabla "Personas"
            PreparedStatement stGetId = this.con.prepareStatement("SELECT LAST_INSERT_ID()");
            ResultSet rs = stGetId.executeQuery();
            int idPersona = 0;
            if (rs.next()) {
                idPersona = rs.getInt(1);
            }
            rs.close();
            stGetId.close();

            // Por ultimo, insertamos los datos en la tabla "Proveedores" usando el ID de la persona (clave foranea)
            PreparedStatement stProveedores = this.con.prepareStatement("INSERT INTO proveedores (id_persona, id_proveedor, cuit) VALUES (?, ?, ?)");
            stProveedores.setInt(1, idPersona);
            stProveedores.setInt(2, proveedor.getIdProveedor());
            stProveedores.setString(3, proveedor.getCuit());
            stProveedores.executeUpdate();
            stProveedores.close();

        } catch (Exception e) {
            throw e;
        } finally {
            this.cerrarConexion();
        }

    }

    @Override
    public void eliminar(Proveedor proveedor) throws Exception {
        try {
            this.conectar();
            // Comenzamos una transacción para asegurar la integridad de los datos
            con.setAutoCommit(false);

            // Obtenemos el id_persona asociado al proveedor que se va a eliminar
            int idPersona = proveedor.getIdPersona();

            // Utiliza una sentencia SQL que elimine registros de ambas tablas en una sola consulta
            PreparedStatement stEliminar = con.prepareStatement(
                    "DELETE proveedores, personas " +
                            "FROM proveedores " +
                            "INNER JOIN personas ON proveedores.id_persona = personas.id_persona " +
                            "WHERE proveedores.id_persona = ?"
            );
            stEliminar.setInt(1, idPersona);
            stEliminar.executeUpdate();
            stEliminar.close();

            // Confirmamos la transacción (hacemos los cambios permanentes)
            con.commit();

            /*
            // Eliminamos el registro  en la tabla "proveedores"
            PreparedStatement stEliminarProveedor = con.prepareStatement("DELETE FROM proveedores WHERE id_persona = ?");
            stEliminarProveedor.setInt(1, idPersona);
            stEliminarProveedor.executeUpdate();
            stEliminarProveedor.close();

            // Luego, eliminamos el registro en la tabla "personas"
            PreparedStatement stEliminarPersona = con.prepareStatement("DELETE FROM personas WHERE id_persona = ?");
            stEliminarPersona.setInt(1, idPersona);
            stEliminarPersona.executeUpdate();
            stEliminarPersona.close();

            // Confirmamos la transacción (hacemos los cambios permanentes)
            con.commit();*/
        } catch (Exception e) {
            // Si ocurre un error, revertimos la transacción
            con.rollback();
            throw e;
        } finally {
            // Restablecemos la configuración de la conexión y la cerramos
            con.setAutoCommit(true);
            this.cerrarConexion();
        }
    }

    @Override
    public void modificar(Proveedor proveedor) throws Exception {
        try {
            this.conectar();
            // Comenzamos una transacción para asegurar la integridad de los datos
            con.setAutoCommit(false);

            // Obtenemos el id_persona asociado al proveedor que se va a modificar
            int idPersona = proveedor.getIdPersona();

            // Modificamos primero el registro correspondiente en la tabla "personas"
            PreparedStatement stModificarPersona = con.prepareStatement(
                    "UPDATE personas " +
                            "SET nombre = ?, apellido = ?, provincia = ?, localidad = ?, calle = ?, dni = ?, mail = ?, telefono = ? " +
                            "WHERE id_persona = ?"
            );
            stModificarPersona.setString(1, proveedor.getNombre());
            stModificarPersona.setString(2, proveedor.getApellido());
            stModificarPersona.setString(3, proveedor.getProvincia());
            stModificarPersona.setString(4, proveedor.getLocalidad());
            stModificarPersona.setString(5, proveedor.getCalle());
            stModificarPersona.setString(6, proveedor.getDni());
            stModificarPersona.setString(7, proveedor.getMail());
            stModificarPersona.setString(8, proveedor.getTelefono());
            stModificarPersona.setInt(9, idPersona);
            stModificarPersona.executeUpdate();
            stModificarPersona.close();

            // Luego, modificamos el registro correspondiente en la tabla "proveedores"
            PreparedStatement stModificarProveedor = con.prepareStatement(
                    "UPDATE proveedores " +
                            "SET cuit = ? " +
                            "WHERE id_persona = ?"
            );
            stModificarProveedor.setString(1, proveedor.getCuit());
            stModificarProveedor.setInt(2, idPersona);
            stModificarProveedor.executeUpdate();
            stModificarProveedor.close();

            // Confirmamos la transacción (hacemos los cambios permanentes)
            con.commit();
        } catch (Exception e) {
            // Si ocurre un error, revertimos la transacción
            con.rollback();
            throw e;
        } finally {
            // Restablecemos la configuración de la conexión y la cerramos
            con.setAutoCommit(true);
            this.cerrarConexion();
        }
    }
}
