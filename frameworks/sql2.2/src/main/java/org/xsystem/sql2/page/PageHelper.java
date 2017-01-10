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
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.w3c.dom.Document;

/**
 *
 * @author Andrey Timofeev
 */
public interface PageHelper {
    public Connection getConnection();
    public Document   getDocument();
    
    public default Object dml(String path, Map<String, Object> params) {
        Connection con=getConnection();
        Document   xml=getDocument();
        Object ret= PAGE.dml(con, xml,path,params);
        return ret;
    }
    
    public default Map<String, Object> row(String path, Map<String, Object> params) {
        Connection con=getConnection();
        Document   xml=getDocument();
        Map<String, Object> ret= PAGE.row(con, xml,path,params);
        return ret;
    }
    
   public default Object rowValue(String path, Map<String, Object> params,String rowName) {
        Connection con=getConnection();
        Document   xml=getDocument();
        Object  ret= PAGE.rowValue(con, xml,path,params,rowName);
        return ret;
    }
  
   public default Stream<Map<String, Object>> stream(String path, Map<String, Object> params){
       Connection con=getConnection();
        Document   xml=getDocument();
        Stream<Map<String, Object>> ret= PAGE.stream(con, xml,path,params);
        return ret;
   }
           
   public default List<Map<String, Object>> list(String path, Map<String, Object> params){
       Connection con=getConnection();
        Document   xml=getDocument();
        List<Map<String, Object>> ret= PAGE.list(con, xml,path,params);
        return ret;
   }
}
