package com.mspdevs.mspfxmaven.model.DAO;

import com.mspdevs.mspfxmaven.model.Cliente;
import com.mspdevs.mspfxmaven.model.ConexionMySQL;
import com.mspdevs.mspfxmaven.model.Empleado;
import com.mspdevs.mspfxmaven.model.Proveedor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ClienteDAOImpl extends ConexionMySQL implements ClienteDAO {

    @Override
    public ObservableList<Cliente> listarTodos() throws Exception {
        ObservableList<Cliente> lista = null;
        try {
            this.conectar();
            // Se crea una consulta SQL que combina las tablas "clientes" y "personas" mediante un INNER JOIN
            PreparedStatement st = this.con.prepareStatement("SELECT cl.id_cliente, p.* FROM clientes cl JOIN personas p ON cl.idpersona = p.id_persona");
            // Se inicializa la lista donde se almacenarán los resultados
            lista = FXCollections.observableArrayList();
            ResultSet rs = st.executeQuery(); // Ejecuta la consulta SQL
            while (rs.next()) {  // Recorre los resultados
                // se crea una instancia de cliente para cada fila de resultados
                Cliente cliente = new Cliente();
                cliente.setIdCliente(rs.getInt("id_cliente"));
                //proveedor.setCuit(rs.getString("cuit"));
                cliente.setIdPersona(rs.getInt("id_persona"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellido(rs.getString("apellido"));
                cliente.setProvincia(rs.getString("provincia"));
                cliente.setLocalidad(rs.getString("localidad"));
                cliente.setCalle(rs.getString("calle"));
                //proveedor.setDni(rs.getString("dni"));
                cliente.setMail(rs.getString("mail"));
                cliente.setTelefono(rs.getString("telefono"));

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

                lista.add(cliente); // Agrega el proveedor a la lista
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
    public void insertar(Cliente cliente) throws Exception {
        try {
            this.conectar();

            // Primero, insertamos los datos en la tabla "Personas"
            PreparedStatement stPersonas = this.con.prepareStatement("INSERT INTO personas (nombre, apellido, provincia, localidad, calle, mail, telefono) VALUES (?, ?, ?, ?, ?, ?, ?)");
            stPersonas.setString(1, cliente.getNombre());
            stPersonas.setString(2, cliente.getApellido());
            stPersonas.setString(3, cliente.getProvincia());
            stPersonas.setString(4, cliente.getLocalidad());
            stPersonas.setString(5, cliente.getCalle());
            //stPersonas.setString(6, proveedor.getDni());
            stPersonas.setString(6, cliente.getMail());
            stPersonas.setString(7, cliente.getTelefono());
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

            // Por ultimo, insertamos los datos en la tabla "Clientes" usando el ID de la persona (clave foranea)
            PreparedStatement stClientes = this.con.prepareStatement("INSERT INTO clientes (idpersona, id_cliente) VALUES (?, ?)");
            stClientes.setInt(1, idPersona);
            stClientes.setInt(2, cliente.getIdCliente());
            //stProveedores.setString(3, proveedor.getCuit());
            stClientes.executeUpdate();
            stClientes.close();

        } catch (Exception e) {
            throw e;
        } finally {
            this.cerrarConexion();
        }

    }

    @Override
    public void eliminar(Cliente cliente) throws Exception {
        try {
            this.conectar();
            // Comenzamos una transacción para asegurar la integridad de los datos
            con.setAutoCommit(false);

            // Obtenemos el id_persona asociado al proveedor que se va a eliminar
            int idPersona = cliente.getIdPersona();

            // Utiliza una sentencia SQL que elimine registros de ambas tablas en una sola consulta
            PreparedStatement stEliminar = con.prepareStatement(
                    "DELETE clientes, personas " +
                            "FROM clientes " +
                            "INNER JOIN personas ON clientes.idpersona = personas.id_persona " +
                            "WHERE clientes.idpersona = ?"
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
    public void modificar(Cliente cliente) throws Exception {
        try {
            this.conectar();
            // Comenzamos una transacción para asegurar la integridad de los datos
            con.setAutoCommit(false);

            // Obtenemos el id_persona asociado al cliente que se va a modificar
            int idPersona = cliente.getIdPersona();

            // Modificamos primero el registro correspondiente en la tabla "personas"
            PreparedStatement stModificarPersona = con.prepareStatement(
                    "UPDATE personas " +
                            "SET nombre = ?, apellido = ?, provincia = ?, localidad = ?, calle = ?, mail = ?, telefono = ? " +
                            "WHERE id_persona = ?"
            );
            stModificarPersona.setString(1, cliente.getNombre());
            stModificarPersona.setString(2, cliente.getApellido());
            stModificarPersona.setString(3, cliente.getProvincia());
            stModificarPersona.setString(4, cliente.getLocalidad());
            stModificarPersona.setString(5, cliente.getCalle());
            //stModificarPersona.setString(6, cliente.getDni());
            stModificarPersona.setString(6, cliente.getMail());
            stModificarPersona.setString(7, cliente.getTelefono());
            stModificarPersona.setInt(8, idPersona);
            stModificarPersona.executeUpdate();
            stModificarPersona.close();

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
