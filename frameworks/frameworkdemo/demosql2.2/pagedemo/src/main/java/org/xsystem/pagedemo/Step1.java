/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.pagedemo;

import java.sql.Connection;
import java.sql.SQLException;
import org.w3c.dom.Document;
import org.xsystem.sql2.page.PAGE;

import org.xsystem.utils.XMLUtil;

/**
 *
 * @author Andrey Timofeev
 */
public class Step1 {

    public static void deleteAllPersons() throws SQLException {
        DemoConnectionManager demoConnectionManager=new DemoConnectionManager();
        try (Connection connection = demoConnectionManager.create("")) {
            Document xml = XMLUtil.getDocumentFromResourcesE("step1.xml");
            PAGE.dml(connection, xml, "destroyallpersons", PAGE.params());
            System.out.println("Deletes all persons");

        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        try {
            deleteAllPersons();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // TODO code application logic here
    }

}
