/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.system.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;


/**
 *
 * @author Andrey Timofeev
 * Это конечно отстой но мне нужна распределеная транзакция но 90% база то одна
 */
public class JDBCTransationManager2 {
    static ThreadLocal<JDBCTransationManager2> self=new ThreadLocal();
    Connection connection = null;
    boolean saveAutoCommit;
    Supplier<Connection> transactionFactory;

    public JDBCTransationManager2(Supplier<Connection> transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    public void close() {
        if (connection != null) {
            try {
                connection.setAutoCommit(saveAutoCommit);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        self.set(null);
    }

    public void begin() {
        connection = transactionFactory.get();
        try {
            saveAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            self.set(this);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public static boolean isConnection(){
        JDBCTransationManager2 tm= self.get();
        if (tm==null){
            return false; 
        }
        Connection cn=tm.connection;
        if (cn==null){
            return false;
        }
        return true;
    }
    
    public static Connection getConnection(){
       JDBCTransationManager2 tm= self.get();
       Connection cn=tm.connection;
       return cn;
    }
    
    
    public void commit(){
       try {
            connection.commit();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } 
    }
    
    public void rollback() {
        if (connection == null) {
            return;
        }
        try {
            connection.rollback();
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
    }
}
