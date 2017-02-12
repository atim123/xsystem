/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.pagedemo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xsystem.sql2.http.ConnectionManager;

/**
 *
 * @author Andrey Timofeev
 */
public class DemoConnectionManager implements ConnectionManager{

    @Override
    public Connection create(String name) {
       String url="jdbc:postgresql:////127.0.0.1:5432//postgres";
       
        String username="fhir";
        String password="fhir";
        Properties props = new Properties();
        props.setProperty("user",username);
        props.setProperty("password",password);
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url,username,password);
            return conn;
        } catch (ClassNotFoundException|SQLException ex) {
            throw new RuntimeException(ex);
        }

    }
    
}
