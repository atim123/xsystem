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
package org.xsystem.sql2.dml.jdbcnative;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.postgresql.util.PGobject;
import org.xsystem.http.BytesSerializer;

import org.xsystem.sql2.dml.DmlCommand;

/**
 *
 * @author Andrey Timofeev
 */
public class PostgreSQLNativeHelper extends AbstactNativeHelper {
    protected final static GsonBuilder gsonBuilder = new GsonBuilder()
            .serializeNulls()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .registerTypeHierarchyAdapter(byte[].class,new BytesSerializer());

    
    
    public PostgreSQLNativeHelper(DmlCommand dmlCommand){
        super(dmlCommand);
    }
    
    @Override
    public  Object getOTHER(Connection con,Object retType){
        if (retType instanceof PGobject){
            PGobject pgGobject=(PGobject)retType;
            String tpy=pgGobject.getType();
            if (tpy.equalsIgnoreCase("json")){
               String json= pgGobject.getValue();
               Gson gson = gsonBuilder.create();
               retType = gson.fromJson(json, Object.class);
            }
            
        }
       
        return retType;
    }
    
    public  void    setJSONPARAM(PreparedStatement ps,int idx,Object value) throws SQLException {
       Gson gson = gsonBuilder.create();
       String json = gson.toJson(value);
       PGobject jsonObject = new PGobject();
       jsonObject.setType("json");
       jsonObject.setValue(json);
       ps.setObject(idx, jsonObject);
    }
    
    @Override
    public Struct createStructure(Connection con, String baseTypeName, Map elements) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map createMap(Connection con, Struct struct, String baseTypeName) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List createList(Connection con, Array array, String baseTypeName) throws SQLException {
        Object obj=array.getArray();
        List ret=new ArrayList();
        if (obj instanceof Object[]){
           Object[] arr=(Object[]) obj;
           ret.addAll(Arrays.asList(arr));
           return ret;
        } else {
            ret.add(obj);
        }
        return ret;

    }

    @Override
    public Array createNamedArray(Connection con, String baseTypeName, List elements) throws SQLException {
        if (   baseTypeName.equalsIgnoreCase("varchar")
            || baseTypeName.equalsIgnoreCase("string")    
        ){
            String strings[] = new String[elements.size()]; 
            elements.toArray(strings);
            Array ret=con.createArrayOf("varchar", strings);
            return ret;
        } else {
          throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    
        }
    }
}
