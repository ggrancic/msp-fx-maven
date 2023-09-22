package com.mspdevs.mspfxmaven.model.DAO;

import com.mspdevs.mspfxmaven.model.ConexionMySQL;
import com.mspdevs.mspfxmaven.model.Empleado;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EmpleadoDAOImpl extends ConexionMySQL implements EmpleadoDAO {

    @Override
    public ObservableList<Empleado> listarTodos() throws Exception {
        ObservableList<Empleado> lista = null;
        try {
            this.conectar();
            String myQuery = "SELECT em.* , per.* FROM empleados em JOIN personas per "
                    + "ON em.idpersona = per.id_persona";
            PreparedStatement st = this.con.prepareStatement(myQuery);
            lista = FXCollections.observableArrayList();
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Empleado e = new Empleado();
                e.setId_empleado(rs.getInt("id_empleado"));
                e.setNombre(rs.getString("nombre"));
                e.setApellido(rs.getString("apellido"));
                e.setProvincia(rs.getString("provincia"));
                e.setLocalidad(rs.getString("localidad"));
                e.setCalle(rs.getString("calle"));
                e.setDni(rs.getString("dni"));
                e.setMail(rs.getString("mail"));
                e.setTelefono(rs.getString("telefono"));
                e.setNombre_usuario(rs.getString("nombre_usuario"));
                e.setClave(rs.getString("clave"));
                e.setEsAdmin(rs.getString("esAdmin"));
                e.setIdPersona(rs.getInt("idPersona"));
                lista.add(e);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            throw e;
        } finally {
            this.cerrarConexion();
        }
        return lista;
    }

    @Override
    public void insertar(Empleado empleado) throws Exception {
        try {
            this.conectar();
            String queryPersonas = "INSERT INTO personas (nombre, apellido, provincia, localidad, calle, dni, mail, telefono)"
                    + " VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement stPersonas = this.con.prepareStatement(queryPersonas);
            stPersonas.setString(1, empleado.getNombre());
            stPersonas.setString(2, empleado.getApellido());
            stPersonas.setString(3, empleado.getProvincia());
            stPersonas.setString(4, empleado.getLocalidad());
            stPersonas.setString(5, empleado.getCalle());
            stPersonas.setString(6, empleado.getDni());
            stPersonas.setString(7, empleado.getMail());
            stPersonas.setString(8, empleado.getTelefono());
            stPersonas.executeUpdate();
            stPersonas.close();

            PreparedStatement stGetId = this.con.prepareStatement("SELECT LAST_INSERT_ID()");
            ResultSet rs = stGetId.executeQuery();
            int idPersonaFK = 0;
            if (rs.next()) {
                idPersonaFK = rs.getInt(1);
            }
            rs.close();
            stGetId.close();

            String queryEmpleados = "INSERT INTO empleados (nombre_usuario, clave, esAdmin, idpersona) VALUES (?,?,?,?)";
            PreparedStatement stEmpleados = this.con.prepareStatement(queryEmpleados);
            stEmpleados.setString(1, empleado.getNombre_usuario());
            stEmpleados.setString(2, empleado.getClave());
            stEmpleados.setString(3, empleado.getEsAdmin());
            stEmpleados.setInt(4, idPersonaFK);
            stEmpleados.executeUpdate();
            stEmpleados.close();
        } catch (Exception e) {
            throw e;
        } finally {
            this.cerrarConexion();
        }
    }

    @Override
    public void eliminar(Empleado empleado) throws Exception {
        try {
            this.conectar();
            PreparedStatement st = this.con.prepareStatement("DELETE FROM empleados WHERE id_empleado = ?");
            st.setInt(1, empleado.getId_empleado());
            st.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            this.cerrarConexion();
        }
    }

    @Override
    public void modificar(Empleado empleado) throws Exception {
        try {
            this.conectar();
            System.out.println(empleado.getIdPersona());
            // Comenzamos una transacción para asegurar la integridad de los datos
            con.setAutoCommit(false);
            
            String queryPersonas = "UPDATE personas SET nombre = ?, apellido = ?, provincia = ?, localidad = ?, calle = ?, dni = ?, mail = ?, telefono = ?"
                    + " WHERE id_persona = ?";
            PreparedStatement stMP = this.con.prepareStatement(queryPersonas);
            
            stMP.setString(1, empleado.getNombre());
            stMP.setString(2, empleado.getApellido());
            stMP.setString(3, empleado.getProvincia());
            stMP.setString(4, empleado.getLocalidad());
            stMP.setString(5, empleado.getCalle());
            stMP.setString(6, empleado.getDni());
            stMP.setString(7, empleado.getMail());
            stMP.setString(8, empleado.getTelefono());
            stMP.setInt(9, empleado.getIdPersona());
            
            stMP.executeUpdate();
            stMP.close();
            
            String queryEmpleado = "UPDATE empleados SET nombre_usuario = ?, clave = ?, esAdmin = ? WHERE idpersona = ?";
            PreparedStatement stEm = this.con.prepareStatement(queryEmpleado);
            
            stEm.setString(1, empleado.getNombre_usuario());
            stEm.setString(2, empleado.getClave());
            stEm.setString(3, empleado.getEsAdmin());
            stEm.setInt(4, empleado.getIdPersona());
            
            stEm.executeUpdate();
            stEm.close();
            
            // Confirmamos la transacción (hacemos los cambios permanentes)
            con.commit();
            
        } catch (Exception e) {
            con.rollback();
            //throw e;
            e.printStackTrace();
        } finally {
            con.setAutoCommit(true);
            this.cerrarConexion();
        }
    }
}
