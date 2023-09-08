package com.mspdevs.mspfxmaven.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionMySQL {
    private static final String DRIVER = "com.mysql.jdbc.Driver"; 
    private static final String URL = "jdbc:mysql://localhost:3306/mercadito";
    private static final String USER = "usuarioMercadito";
    private static final String CLAVE = "mercadito";
    protected Connection con = null;
    
    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }
    
    public void conectar() throws Exception {
        try {
            Connection c = DriverManager.getConnection(URL, USER, CLAVE);
            setCon(c);
        } catch (Exception e) {
            throw e;
        }
    }

    public void cerrarConexion() throws SQLException {
        if (con != null) {
            if(!con.isClosed()) {
                con.close();
            }
        }
    }
}
