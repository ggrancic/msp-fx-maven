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
            PreparedStatement st = this.con.prepareStatement("SELECT cl.*, p.* FROM clientes cl JOIN personas p ON cl.idpersona = p.id_persona");
            // Se inicializa la lista donde se almacenarán los resultados
            lista = FXCollections.observableArrayList();
            ResultSet rs = st.executeQuery(); // Ejecuta la consulta SQL
            while (rs.next()) {  // Recorre los resultados
                // se crea una instancia de cliente para cada fila de resultados
                Cliente cliente = new Cliente();
                cliente.setIdCliente(rs.getInt("id_cliente"));
                cliente.setCuil(rs.getString("p.cuil"));
                cliente.setCuit(rs.getString("p.cuit"));
                cliente.setRazonSocial(rs.getString("p.razon_social"));
                cliente.setIdPersona(rs.getInt("id_persona"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellido(rs.getString("apellido"));
                cliente.setProvincia(rs.getString("provincia"));
                cliente.setLocalidad(rs.getString("localidad"));
                cliente.setCalle(rs.getString("calle"));
                cliente.setDni(rs.getString("dni"));
                cliente.setMail(rs.getString("mail"));
                cliente.setTelefono(rs.getString("telefono"));

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
            String consultaSiExiste = "SELECT id_persona FROM personas WHERE dni = ? OR cuit = ?";
            PreparedStatement miSt = this.con.prepareStatement(consultaSiExiste);
            miSt.setString(1, cliente.getDni());
            miSt.setString(2, cliente.getCuit());
            ResultSet result = miSt.executeQuery();

            // Inicializo el id de la persona...
            int idPersonaFK = 0;

            if (result.next()) {
                idPersonaFK = result.getInt("id_persona");
                miSt.close();
            } else {
                // Este es el caso de que no existe la persona en la db. Por lo tanto, tengo que tomar los datos
                // que vienen del formulario y dar de alta a la persona en la db.
                // Primero, insertamos los datos en la tabla "Personas"
                String queryPersonas = "INSERT INTO personas (nombre, apellido, provincia, localidad, calle, dni, mail, telefono) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stPersonas = this.con.prepareStatement(queryPersonas);
                stPersonas.setString(1, cliente.getNombre());
                stPersonas.setString(2, cliente.getApellido());
                stPersonas.setString(3, cliente.getProvincia());
                stPersonas.setString(4, cliente.getLocalidad());
                stPersonas.setString(5, cliente.getCalle());
                stPersonas.setString(6, cliente.getDni());
                stPersonas.setString(7, cliente.getMail());
                stPersonas.setString(8, cliente.getTelefono());
                stPersonas.executeUpdate();
                stPersonas.close();

                PreparedStatement stGetId = this.con.prepareStatement("SELECT LAST_INSERT_ID()");
                ResultSet rs = stGetId.executeQuery();
                if (rs.next()) {
                    idPersonaFK = rs.getInt(1);
                }
                rs.close();
                stGetId.close();    
            }
            
            String queryUpdate = "UPDATE personas SET razon_social = ?, cuil = ?, cuit = ? WHERE id_persona = ?";
            PreparedStatement stUpdate = this.con.prepareStatement(queryUpdate);
            stUpdate.setString(1, cliente.getRazonSocial());
            stUpdate.setString(2, cliente.getCuil());
            stUpdate.setString(3, cliente.getCuit());
            stUpdate.setInt(4, idPersonaFK);
            stUpdate.executeUpdate();
            stUpdate.close();
            
            
            // Por ultimo, insertamos los datos en la tabla "Clientes" usando el ID de la persona (clave foranea)
            String queryClientes = "INSERT INTO clientes (idpersona, id_cliente) VALUES (?, ?)";
            PreparedStatement stClientes = this.con.prepareStatement(queryClientes);
            stClientes.setInt(1, idPersonaFK);
            stClientes.setInt(2, cliente.getIdCliente());
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
            int idcliente = cliente.getIdCliente();

            // Utiliza una sentencia SQL que elimine registros de ambas tablas en una sola consulta
            PreparedStatement stEliminar = con.prepareStatement(
                    "DELETE FROM clientes WHERE id_cliente = ?"
            );
            stEliminar.setInt(1, idcliente);
            stEliminar.executeUpdate();
            stEliminar.close();

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
                            "SET nombre = ?, apellido = ?, provincia = ?, localidad = ?, calle = ?, dni = ?, cuil = ?, razon_social = ?, mail = ?, telefono = ? " +
                            "WHERE id_persona = ?"
            );
            stModificarPersona.setString(1, cliente.getNombre());
            stModificarPersona.setString(2, cliente.getApellido());
            stModificarPersona.setString(3, cliente.getProvincia());
            stModificarPersona.setString(4, cliente.getLocalidad());
            stModificarPersona.setString(5, cliente.getCalle());
            stModificarPersona.setString(6, cliente.getDni());
            stModificarPersona.setString(7, cliente.getCuil());
            stModificarPersona.setString(8, cliente.getRazonSocial());
            stModificarPersona.setString(9, cliente.getMail());
            stModificarPersona.setString(10, cliente.getTelefono());
            stModificarPersona.setInt(11, idPersona);
            stModificarPersona.executeUpdate();
            stModificarPersona.close();

            // Confirmamos la transacción (hacemos los cambios permanentes)
            con.commit();
        } catch (Exception e) {
            // Si ocurre un error, revertimos la transacción
            con.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            // Restablecemos la configuración de la conexión y la cerramos
            con.setAutoCommit(true);
            this.cerrarConexion();
        }
    }

    public int obtenerPorRazonSocial(String razonSocial) throws Exception {
        int idCliente = -1; // Valor predeterminado si no se encuentra el cliente
        try {
            this.conectar();
            PreparedStatement st = this.con.prepareStatement(
                    "SELECT c.id_cliente FROM clientes c " +
                            "INNER JOIN personas per ON c.idpersona = per.id_persona " +
                            "WHERE per.razon_social = ?");
            st.setString(1, razonSocial);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                idCliente = rs.getInt("id_cliente");
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            throw e;
        } finally {
            this.cerrarConexion();
        }
        return idCliente;
    }
}
