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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xsystem.sql2.dml.ParamsHelper;
import org.xsystem.utils.Auxilary;
import org.xsystem.utils.XMLUtil;


/**
 *
 * @author Andrey Timofeev
 */
public class PAGE implements ParamsHelper{
    
    public static Map<String, Object> params(Object... args) {
        return Auxilary.newMap(args);
    }
    
    public static void prepare(SqlAction action, Document xml, String path) {
        Element elem = getNode(path, xml);
        if (elem==null){
            throw new Error("Can't find sql action path=["+path+"]");
        }
        buildContext(action, elem);
        Element sqlElement = (Element) XMLUtil.getNoge("./sql", elem);
        if (sqlElement==null){
           throw new RuntimeException("in ["+path+"] expected <sql> teg" ); 
        }
        String elsql = XMLUtil.getContentText(sqlElement);
        String strnamed = elem.getAttribute("namedparameters");
        if (!Auxilary.isEmptyOrNull(strnamed)) {
            boolean v = strnamed.equalsIgnoreCase("true");
            action.setNamedparameters(v);
        }
        boolean singleRow=true;
        String singleRowStr=elem.getAttribute("singlerow");
        if (!Auxilary.isEmptyOrNull(singleRowStr)){
            singleRow = strnamed.equalsIgnoreCase("true");
        }
        action.setSingleRow(singleRow);
        elsql = elsql.trim();
        action.setSql(elsql);
        Element skipElement=(Element) XMLUtil.getNoge("./skip", elem);
        if (skipElement!=null){
           String elskip = XMLUtil.getContentText(skipElement); 
           action.setSkip(elskip);
        } 
        Element totalElement=(Element) XMLUtil.getNoge("./total", elem);
        if (totalElement!=null){
            String eltotal = XMLUtil.getContentText(totalElement); 
            action.setTotal(eltotal);
        }
    }
    
    public static Object dml(Connection connection, Document xml, String path, Map<String, Object> params) {
        SqlDmlAction action = new SqlDmlAction();
        prepare(action, xml, path);
        return action.exec(connection, params);
    }
    
    
    public static Map<String, Object> row(Connection connection, Document xml, String path, Map<String, Object> params) {
        SqlRowAction action = new SqlRowAction();
        prepare(action, xml, path);

        return (Map) action.exec(connection, params);
    }
    
    public static Object rowValue(Connection connection, Document xml, String path, Map<String, Object> params, String rowName) {
        Map<String, Object> retrow = row(connection, xml, path, params);
        if (retrow != null) {
            return retrow.get(rowName);
        }
        return null;
    }
    
    
     public static Stream<Map<String, Object>> stream(Connection connection, Document xml, String path,Long skip, Integer total, Map<String, Object> params) {
        SqlStreamAction action = new SqlStreamAction();
        prepare(action, xml, path);
        return  action.execQuery(connection,skip,total, params);
    }

    public static Stream<Map<String, Object>> stream(Connection connection, Document xml, String path, Map<String, Object> params) {
        SqlStreamAction action = new SqlStreamAction();
        prepare(action, xml, path);
        return  action.execQuery(connection,null,null, params);
    } 
     
    public static List<Map<String, Object>> list(Connection connection, Document xml, String path,Long skip, Integer total, Map<String, Object> params) {
        return stream(connection, xml, path,skip,total, params)
                .collect(Collectors.toList());
    }

    public static List<Map<String, Object>> list(Connection connection, Document xml, String path, Map<String, Object> params) {
        return stream(connection, xml, path,null,null, params)
                .collect(Collectors.toList());
    }
    
    //------------Private--------------------------------------------------------
    static void buildContext(SqlAction action, Element elem) {//throws XPathExpressionException {
        buildGlobalContext(action, elem);
        buildLocalContext(action, elem);
        buildParams(action, elem);
    }

    static void buildGlobalContext(SqlAction action, Element elem) { //throws XPathExpressionException {
        Element root = elem.getOwnerDocument().getDocumentElement();

        Element definitionsElem = (Element) XMLUtil.getNoge("./definitions", root);
        if (definitionsElem != null) {
            buildLocalContext(action,definitionsElem);
        }
    }

    static void buildLocalContext(SqlAction action, Element elem) {

        NodeList rowList = XMLUtil.getNogeList("./define", elem);
        for (int i = 0; i < rowList.getLength(); i++) {
            ContextDefinition contextDefinition = new ContextDefinition();

            Element item = (Element) rowList.item(i);
            String name = item.getAttribute("name");
            String ifEl = item.getAttribute("if");
            String value = XMLUtil.getContentText(item).trim();

            contextDefinition.setName(name);

            if (!Auxilary.isEmptyOrNull(ifEl)) {
                contextDefinition.setIfExpr(ifEl);
            }

            

            if (!Auxilary.isEmptyOrNull(value)) {
                contextDefinition.setValue(value);
            }
            action.getDefinitions().add(contextDefinition);
        }
    }

    static void buildParams(SqlAction action, Element elem) {

        NodeList rowList = XMLUtil.getNogeList("./parameter", elem);

        // Object[] sqlPrm = new Object[rowList.getLength()];
        for (int i = 0; i < rowList.getLength(); i++) {
            Element item = (Element) rowList.item(i);
            String name = item.getAttribute("name");
            String inParam=item.getAttribute("in");
            String outParam=item.getAttribute("out");
            String type = item.getAttribute("type");
            
            String objecttype=item.getAttribute("objecttype");
            String arraytype =item.getAttribute("arraytype");
            
            String ifEl = item.getAttribute("if");
            String value = item.getAttribute("value");

            ContextDefinition contextDefinition = new ContextDefinition();

            contextDefinition.setName(name);

            if (!Auxilary.isEmptyOrNull(arraytype)&&Auxilary.isEmptyOrNull(type)) {
                type="ARRAY";
                contextDefinition.setObjectType(arraytype);
            }
         
            if (!Auxilary.isEmptyOrNull(objecttype)){
                contextDefinition.setObjectType(objecttype);
                if (Auxilary.isEmptyOrNull(type)){
                    type="STRUCT";
                }
            }
            
            if (!Auxilary.isEmptyOrNull(ifEl)) {
                contextDefinition.setIfExpr(ifEl);
            }

            if (!Auxilary.isEmptyOrNull(type)) {
                contextDefinition.setType(type);
            }

            if (!Auxilary.isEmptyOrNull(value)) {
                contextDefinition.setValue(value);
            }
            boolean in;
            boolean out;
                  
            if (Auxilary.isEmptyOrNull(inParam)&&Auxilary.isEmptyOrNull(outParam)){
                in=true;
                out=false;
            } else if (Auxilary.isEmptyOrNull(inParam)&&!Auxilary.isEmptyOrNull(outParam)){
                out=Boolean.parseBoolean(outParam);
                in=true;
                if (out){
                  in=false;  
                }
            } else if (!Auxilary.isEmptyOrNull(inParam)&&Auxilary.isEmptyOrNull(outParam)){
                in=Boolean.parseBoolean(inParam);
                out=true;
                if (in){
                  out=false;  
                }
            } else {
                in=Boolean.parseBoolean(inParam);
                out=Boolean.parseBoolean(outParam);
            }
            
            contextDefinition.setIn(in);
            contextDefinition.setOut(out);
         
            action.getParams().add(contextDefinition);
        }
    }

    
    
    
    static Element getNode(String path, Document xml) {
        Element root = xml.getDocumentElement();
        Element elem = (Element) XMLUtil.getNoge(path, root);
        return elem;
    }
}
