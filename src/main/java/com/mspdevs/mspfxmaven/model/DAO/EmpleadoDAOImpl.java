package com.mspdevs.mspfxmaven.model.DAO;

import com.mspdevs.mspfxmaven.model.ConexionMySQL;
import com.mspdevs.mspfxmaven.model.Empleado;
import com.mspdevs.mspfxmaven.model.Rubro;
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
            PreparedStatement st = this.con.prepareStatement("SELECT * FROM empleados");
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
            String myQuery = "INSERT INTO personas (nombre, apellido, provincia, localidad, calle, dni, mail, telefono) VALUES (?,?,?,?,?,?,?,?)" +
                    "INSERT INTO empleados (nombre_usuario, clave, esAdmin, idpersona) VALUES (?,?,?,LAST_INSERT_ID())";
            PreparedStatement st = this.con.prepareStatement(myQuery);
            st.setString(1, empleado.getNombre());
            st.setString(2, empleado.getApellido());
            st.setString(3, empleado.getProvincia());
            st.setString(4, empleado.getLocalidad());
            st.setString(5, empleado.getCalle());
            st.setString(6, empleado.getCalle());
            st.setString(6, empleado.getDni());
            st.setString(7, empleado.getMail());
            st.setString(8, empleado.getTelefono());
            st.setString(9, empleado.getNombre_usuario());
            st.setString(10, empleado.getClave());
            st.setString(11, empleado.getEsAdmin());

        } catch (Exception e) {
            throw e;
        } finally {
            this.cerrarConexion();
        }
    }

    @Override
    public void eliminar(Empleado empleado) throws Exception {

    }

    @Override
    public void modificar(Empleado empleado) throws Exception {

    }
}
