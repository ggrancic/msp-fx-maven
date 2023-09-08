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
                e.getEsAdmin(rs.getInt("esAdmin"));
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
