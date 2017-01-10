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
package org.xsystem.sql2.http;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xsystem.sql2.page.PAGE;


/**
 *
 * @author Andrey Timofeev
 */
public interface Executer {
  public Object execute(Map params) throws RuntimeException;
   
     
   public void parse(Element element);
   
   
   
   public static Object dml(String path, Map<String, Object> params) {
      Enviroment env= Enviroment.getEnviroment();
      Connection connection=env.getConnection();
      Document document=env.getDocument();
      Object ret=PAGE.dml(connection, document, path, params);
      return ret;
   }
   
   public static Map<String, Object> row(String path, Map<String, Object> params) {
      Enviroment env= Enviroment.getEnviroment();
      Connection connection=env.getConnection();
      Document document=env.getDocument();
      Map<String, Object> ret=PAGE.row(connection, document, path, params);
      return ret;
   }
   
   public static Object rowValue(String path, Map<String, Object> params,String rowName) {
      Enviroment env= Enviroment.getEnviroment();
      Connection connection=env.getConnection();
      Document document=env.getDocument();
      Object ret=PAGE.rowValue(connection, document, path, params,rowName);
      return ret;
   }
   
   public static Stream<Map<String, Object>> stream(String path, Map<String, Object> params) {
      Enviroment env= Enviroment.getEnviroment();
      Connection connection=env.getConnection();
      Document document=env.getDocument();
      Stream<Map<String, Object>> ret=PAGE.stream(connection, document, path, params);
      return ret;
   }
   
   public static List<Map<String, Object>> list(String path,Long skip, Integer total, Map<String, Object> params) {
      Enviroment env= Enviroment.getEnviroment();
      Connection connection=env.getConnection();
      Document document=env.getDocument();
      List<Map<String, Object>> ret=PAGE.list(connection, document,path,skip,total, params);
      return ret;
   }
   
   public static List<Map<String, Object>> list(String path, Map<String, Object> params) {
      Enviroment env= Enviroment.getEnviroment();
      Connection connection=env.getConnection();
      Document document=env.getDocument();
      List<Map<String, Object>> ret=PAGE.list(connection, document, path, params);
      return ret;
   }  
}
