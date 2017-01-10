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

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.List;
import java.util.Map;
import org.xsystem.sql2.dml.DmlCommand;


/**
 *
 * @author Andrey Timofeev
 */
public abstract class AbstactNativeHelper {
    protected DmlCommand dmlCommand;
    
    public AbstactNativeHelper(DmlCommand dmlCommand){
       this.dmlCommand= dmlCommand;
    }
    
    public abstract Struct createStructure(Connection con, String baseTypeName, Map elements) throws SQLException;
    public abstract Map createMap(Connection con,Struct struct,String baseTypeName) throws SQLException;
    
    public abstract List createList(Connection con, Array array,String baseTypeName) throws SQLException;
    public abstract Array createNamedArray(Connection con, String baseTypeName, List elements) throws SQLException;

    public abstract Object getOTHER(Connection con,Object retType);
    public abstract void    setJSONPARAM(PreparedStatement ps,int pram,Object value) throws SQLException;
}
