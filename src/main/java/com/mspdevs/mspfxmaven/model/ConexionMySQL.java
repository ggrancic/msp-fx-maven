package com.mspdevs.mspfxmaven.model;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionMySQL {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/mercadito";
    private static final String USER = "usuarioMercadito";
    private static final String CLAVE = "mercadito";
    private Connection con = null;

    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }

    public void conectar() {
        try {
            Connection c = DriverManager.getConnection(URL, USER, CLAVE);
            setCon(c);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*public boolean conectar(){
        boolean ok = false;
        setCon(null);
        try {
            Class.forName(DRIVER);//Cargar el Driver de MySQL para establecer la conexion
            Connection conexion = (Connection) DriverManager.getConnection(URL, USER, CLAVE); //Conectar por medio de DriverManager
            if(conexion!=null){
                setCon(conexion);
                ok = true;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }finally{
            return ok;
        }
    }*/



}
