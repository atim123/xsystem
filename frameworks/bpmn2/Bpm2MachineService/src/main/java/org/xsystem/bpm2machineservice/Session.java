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
package org.xsystem.bpm2machineservice;

import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.xsystem.sql2.page.SQL;
import org.xsystem.system.sql.JDBCTransationManager2;
import org.xsystem.utils.MBeanUtil;

/**
 *
 * @author Andrey Timofeev
 */
public class Session {

    static ThreadLocal<Session> self = new ThreadLocal();

    static String dsName = "java:/comp/env/jdbc/rfhir";

/*    Set result = conn.queryMBeans(null, 
"Catalina:type=DataSource,path=/appdb,host=localhost,class=javax.sql.DataSource");
*/
   static void tt() {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
       ObjectName listenerRouteQuery = MBeanUtil.createObjectName("*:*,class=javax.sql.DataSource");
        //("*:*,type=routes,name=*Listener*");

        Set<ObjectInstance> ret=mbs.queryMBeans(listenerRouteQuery,null);
        System.out.println(ret);
    }

    
    static Connection getConnection() {
      //  tt();
        try {
            InitialContext cxt = new InitialContext();

            DataSource ds = (DataSource) cxt.lookup(dsName);
            Connection ret = ds.getConnection();
            return ret;
        } catch (NamingException | SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    private Session() {

    }
    JDBCTransationManager2 tm = null;
    String definitionsKey = "";

    public static Session create() {
        Session ret = new Session();
        if (!JDBCTransationManager2.isConnection()) {
            ret.tm = new JDBCTransationManager2(() -> getConnection());
            ret.tm.begin();
        }
        Connection connection=JDBCTransationManager2.getConnection();
        Map row=SQL.row(connection,"select guid from bpmn2.v_cash where id='definitions'");
        ret.definitionsKey=(String)row.get("guid");
        
        self.set(ret);

        return ret;
    }

    public static String getDefinitionsKey(){
       Session session= self.get();
       return session.definitionsKey;
    }
    
    public void close() {
        try {
            if (tm != null) {
                try {
                    tm.commit();
                } finally {
                    tm.close();
                }
            }
        } finally {
           self.set(null);
        }
    }

    public void abort() {
       try {
            if (tm != null) {
                try {
                    tm.rollback();
                } finally {
                    tm.close();
                }
            }
        } finally {
           self.set(null);
        }
    }
}
