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
package org.xsystem.sql2.page;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.sql.Types;
import org.xsystem.sql2.dml.DmlCommand;
import org.xsystem.sql2.dml.DmlParams;


/**
 *
 * @author Andrey Timofeev
 */
public class SQL {
   public static Object[] arglist(Object... args) {
        return args;
    }
    
     public static Object exec(Connection connection, String sql){
        Object ret= exec(connection,sql,arglist());
        return ret;
     }
    
     public static Object exec(Connection connection, String sql, Object[] params){
         return exec(connection,sql,params,true);
     }
     
     public static List list(Connection connection, String sql, Object[] params){
        Object ret= exec(connection,sql,params,false);
        return (List)ret;
     }
     
     public static List list(Connection connection, String sql){
        return list(connection,sql,arglist());
    }
    
     public static Map row(Connection connection, String sql){
         List lst=list(connection,sql);
         if (lst.isEmpty()){
             return null;
         }
         Map ret=(Map)lst.get(0);
         return ret;
     } 
     
    public static Object exec(Connection connection, String sql, Object[] params,boolean singleRow){
       DmlCommand dmlCommand=new DmlCommand();
       List<DmlParams> paramsSpec = new ArrayList();
       Map paramsValue=new LinkedHashMap();
       for (int i=0;i<params.length;i++){
           String paramName="p"+i;
           DmlParams dmlParams=new DmlParams();
           dmlParams.setIn(true);
           dmlParams.setName(paramName);
           dmlParams.setJdbcType(Types.OTHER);
           paramsValue.put(paramName,params[i]);
           paramsSpec.add(dmlParams);
       }
       
       Object ret=dmlCommand.execute(connection,sql,paramsSpec,paramsValue,singleRow);
       return ret;
    } 
}
