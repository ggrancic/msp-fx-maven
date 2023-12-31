package com.mspdevs.mspfxmaven.model.DAO;

import com.mspdevs.mspfxmaven.model.ConexionMySQL;
import com.mspdevs.mspfxmaven.model.Rubro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RubroDAOImpl extends ConexionMySQL implements RubroDAO {

    @Override
    public ObservableList<Rubro> listarTodos() throws Exception {
        ObservableList<Rubro> lista = null;
        try {
            this.conectar();
            PreparedStatement st = this.con.prepareStatement("SELECT * FROM rubros");
            lista = FXCollections.observableArrayList();
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Rubro r = new Rubro();
                r.setIdRubro(rs.getInt("id_rubro"));
                r.setNombre(rs.getString("nombre"));
                lista.add(r);
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
    public void insertar(Rubro rubro) throws Exception {
        try {
            this.conectar();
            String consultaSiExiste = "SELECT nombre FROM rubros WHERE nombre = ?";
            PreparedStatement miSt = this.con.prepareStatement(consultaSiExiste);
            miSt.setString(1, rubro.getNombre());
            ResultSet result = miSt.executeQuery();
            // Inicializo el id...
            int idRubroFK = 0;

            if (result.next()) {
                idRubroFK = result.getInt("id_rubro");
                miSt.close();
            } else {
                PreparedStatement st = this.con.prepareStatement("INSERT INTO rubros (nombre) VALUES (?)");
                st.setString(1, rubro.getNombre());
                st.executeUpdate();
            }

        } catch (Exception e) {
            throw e;
        } finally {
            this.cerrarConexion();
        }
    }

    @Override
    public void eliminar(Rubro rubro) throws Exception {
        try {
            this.conectar();
            PreparedStatement st = this.con.prepareStatement("DELETE FROM rubros WHERE id_rubro = ?");
            st.setInt(1, rubro.getIdRubro());
            st.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            this.cerrarConexion();
        }
    }

    @Override
    public void modificar(Rubro rubro) throws Exception {
        try {
            this.conectar();
            PreparedStatement st = this.con.prepareStatement("UPDATE rubros SET nombre = ? WHERE id_rubro = ?");
            st.setString(1, rubro.getNombre());
            st.setInt(2, rubro.getIdRubro());
            st.executeUpdate();
        } catch (Exception e) {
            //throw e;
            e.printStackTrace();
        } finally {
            this.cerrarConexion();
        }
    }

    // Método para obtener un rubro por nombre
    public Rubro obtenerPorNombre(String nombre) throws Exception {
        Rubro rubro = null;
        try {
            this.conectar();
            PreparedStatement st = this.con.prepareStatement("SELECT * FROM rubros WHERE nombre = ?");
            st.setString(1, nombre);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                rubro = new Rubro();
                rubro.setIdRubro(rs.getInt("id_rubro"));
                rubro.setNombre(rs.getString("nombre"));
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            throw e;
        } finally {
            this.cerrarConexion();
        }
        return rubro;
    }

}
