package com.mspdevs.mspfxmaven.model.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.mspdevs.mspfxmaven.model.ConexionMySQL;
import com.mspdevs.mspfxmaven.model.Persona;
import com.mspdevs.mspfxmaven.model.Proveedor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PersonaDAOImpl extends ConexionMySQL implements PersonaDAO<Persona> {

    @Override
    public ObservableList<Persona> listarTodos() throws Exception {
        ObservableList<Persona> lista = null;

        try {
            this.conectar();
            PreparedStatement ps = this.con.prepareStatement("SELECT p.* FROM personas p");
            lista = FXCollections.observableArrayList();

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Persona persona = new Persona();
                persona.setIdPersona(rs.getInt("id_persona"));
                persona.setNombre(rs.getString("nombre"));
                persona.setApellido(rs.getString("apellido"));
                persona.setProvincia(rs.getString("provincia"));
                persona.setLocalidad(rs.getString("localidad"));
                persona.setCalle(rs.getString("calle"));
                persona.setDni(rs.getString("dni"));
                persona.setMail(rs.getString("mail"));
                persona.setTelefono(rs.getString("telefono"));

                lista.add(persona);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.cerrarConexion();
        }
        return lista;
    }

}