package com.mspdevs.mspfxmaven.model.DAO;

import com.mspdevs.mspfxmaven.model.ConexionMySQL;
import com.mspdevs.mspfxmaven.model.Proveedor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProveedorDAOImpl extends ConexionMySQL implements ProveedorDAO {
    @Override
    public ObservableList<Proveedor> listarTodos() throws Exception {
        ObservableList<Proveedor> lista = null;
        try {
            this.conectar();
            // Se crea una consulta SQL que combina las tablas "proveedores" y "personas" mediante un INNER JOIN
            PreparedStatement st = this.con.prepareStatement("SELECT pr.*, p.* FROM proveedores pr JOIN personas p ON pr.id_persona = p.id_persona");
            // Se inicializa la lista donde se almacenarán los resultados
            lista = FXCollections.observableArrayList();
            ResultSet rs = st.executeQuery(); // Ejecuta la consulta SQL
            while (rs.next()) {  // Recorre los resultados
                // se crea una instancia de Proveedor para cada fila de resultados
                Proveedor proveedor = new Proveedor();
                proveedor.setIdProveedor(rs.getInt("id_proveedor"));
                proveedor.setCuit(rs.getString("cuit"));
                proveedor.setRazonSocial(rs.getString("razon_social"));
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
            String consultaSiExiste = "SELECT id_persona FROM personas WHERE dni = ?";
            PreparedStatement miSt = this.con.prepareStatement(consultaSiExiste);
            miSt.setString(1, proveedor.getDni());
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
                String queryPersonas = "INSERT INTO personas (nombre, apellido, provincia, localidad, calle, dni, cuit, razon_social, mail, telefono) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stPersonas = this.con.prepareStatement(queryPersonas);
                stPersonas.setString(1, proveedor.getNombre());
                stPersonas.setString(2, proveedor.getApellido());
                stPersonas.setString(3, proveedor.getProvincia());
                stPersonas.setString(4, proveedor.getLocalidad());
                stPersonas.setString(5, proveedor.getCalle());
                stPersonas.setString(6, proveedor.getDni());
                stPersonas.setString(7, proveedor.getCuit());
                stPersonas.setString(8, proveedor.getRazonSocial());
                stPersonas.setString(9, proveedor.getMail());
                stPersonas.setString(10, proveedor.getTelefono());
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
            // Por ultimo, insertamos los datos en la tabla "Proveedores" usando el ID de la persona (clave foranea)
            String queryProveedores = "INSERT INTO proveedores (id_persona, id_proveedor) VALUES (?, ?)";
            PreparedStatement stProveedores = this.con.prepareStatement(queryProveedores);
            stProveedores.setInt(1, idPersonaFK);
            stProveedores.setInt(2, proveedor.getIdProveedor());
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
            con.commit();*/

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
                            "SET nombre = ?, apellido = ?, provincia = ?, localidad = ?, calle = ?, dni = ?, cuit = ?, razon_social = ?, mail = ?, telefono = ? " +
                            "WHERE id_persona = ?"
            );
            stModificarPersona.setString(1, proveedor.getNombre());
            stModificarPersona.setString(2, proveedor.getApellido());
            stModificarPersona.setString(3, proveedor.getProvincia());
            stModificarPersona.setString(4, proveedor.getLocalidad());
            stModificarPersona.setString(5, proveedor.getCalle());
            stModificarPersona.setString(6, proveedor.getDni());
            stModificarPersona.setString(7, proveedor.getCuit());
            stModificarPersona.setString(8, proveedor.getRazonSocial());
            stModificarPersona.setString(9, proveedor.getMail());
            stModificarPersona.setString(10, proveedor.getTelefono());
            stModificarPersona.setInt(11, idPersona);
            stModificarPersona.executeUpdate();
            stModificarPersona.close();
            /*
            // Luego, modificamos el registro correspondiente en la tabla "proveedores"
            PreparedStatement stModificarProveedor = con.prepareStatement(
                    "UPDATE proveedores " +
                            "WHERE id_persona = ?"
            );
            stModificarProveedor.setInt(1, idPersona);
            stModificarProveedor.executeUpdate();
            stModificarProveedor.close();*/
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

    // Método para obtener un proveedor por nombre
    public int obtenerPorNombre(String nombre) throws Exception {
        int idProveedor = -1; // Valor predeterminado si no se encuentra el proveedor
        try {
            this.conectar();
            //PreparedStatement st = this.con.prepareStatement("SELECT * FROM proveedores WHERE nombre = ?");
            PreparedStatement st = this.con.prepareStatement(
                    "SELECT p.id_proveedor FROM proveedores p " +
                            "INNER JOIN personas per ON p.id_persona = per.id_persona " +
                            "WHERE per.razon_social = ?");
            st.setString(1, nombre);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                idProveedor = rs.getInt("id_proveedor");
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            throw e;
        } finally {
            this.cerrarConexion();
        }
        return idProveedor;
    }
}
