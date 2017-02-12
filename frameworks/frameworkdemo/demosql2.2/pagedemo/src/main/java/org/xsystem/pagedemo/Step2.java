/*
 * Copyright 2017 Andrey Timofeev.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xsystem.pagedemo;

import java.sql.Connection;
import java.sql.SQLException;
import org.w3c.dom.Document;
import org.xsystem.sql2.page.PAGE;
import org.xsystem.utils.XMLUtil;
import java.util.Map;
/**
 *
 * @author Andrey Timofeev
 */
public class Step2 {
    public static void deleteAllPersons() throws SQLException {
        DemoConnectionManager demoConnectionManager=new DemoConnectionManager();
        try (Connection connection = demoConnectionManager.create("")) {
            Document xml = XMLUtil.getDocumentFromResourcesE("step1.xml");
            PAGE.dml(connection, xml, "destroyallpersons", PAGE.params());
            System.out.println("Deletes all persons");

        }

    }
    
    public static void createPerson() throws SQLException {
        DemoConnectionManager demoConnectionManager=new DemoConnectionManager();
        try (Connection connection = demoConnectionManager.create("")) {
            Document xml = XMLUtil.getDocumentFromResourcesE("step2.xml");
            Map retRow=(Map)PAGE.dml(connection, xml, "createperson", PAGE.params(
               "firstname","Andrey",
               "surname","Timofeev",
               "patname","Genrichovich",
               "email","atim@atim.ru",
               "phone","+7 916 551 56 23"
            ));
           System.out.println("Create person "+retRow);
        }

    }
    
    public static void main(String[] args)  {
        try {
            deleteAllPersons();
            createPerson();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // TODO code application logic here
    }
}
