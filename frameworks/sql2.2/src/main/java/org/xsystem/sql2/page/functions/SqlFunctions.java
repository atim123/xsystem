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
package org.xsystem.sql2.page.functions;

import java.sql.Connection;
import java.util.Map;
import org.xsystem.sql2.page.SQL;

/**
 *
 * @author Andrey Timofeev
 */
public class SqlFunctions {
    final Connection connection;

    public  Object[] arglist(Object... args){
        if (args==null){
          Object[] ret=new Object[1];
          ret[0]=null;
          return ret;
        }
        return args; 
    }
            
    public SqlFunctions(Connection connection) {
        this.connection = connection;
    }
    
    public Object exec(String sql, Object... values) {
        Object ret=SQL.exec(connection, sql, SQL.arglist(values),false);
        return ret;    
    }
    
    
    
    public Object row(String sql) {
        Object ret=SQL.exec(connection, sql, SQL.arglist(),true);
        return ret;
    }
    
    public Object row(String sql, Object[] values) {
        Object ret=SQL.exec(connection, sql, values,true);
        return ret;
    }
    
    public Object rowValue(String sql,String fld) {
        Object ret=SQL.exec(connection, sql, SQL.arglist(),true);
        if (ret!=null){
           Map m=(Map) ret;
           return m.get(fld);
        }
        return null;
    }
    
    public Object rowValue(String sql,String fld, Object[] values) {
        Object ret=SQL.exec(connection, sql, values,true);
        if (ret!=null){
           Map m=(Map) ret;
           return m.get(fld);
        }
        return null;
    }
}
