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
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.xsystem.sql2.convertor.JdbcTypeMapping;
import org.xsystem.sql2.dml.DataAccessException;
import org.xsystem.sql2.dml.DmlCommand;
import org.xsystem.sql2.dml.DmlParams;
import org.xsystem.sql2.page.mvel.PageMVELFactory;
import org.xsystem.utils.Auxilary;


/**
 *
 * @author Andrey Timofeev
 */
public abstract class SqlAction {
   private final List<ContextDefinition> definitions = new ArrayList();

    private final List<ContextDefinition> params = new ArrayList();

    private String elsql;
    private boolean namedparameters = false;
    private Boolean upperTag;
    private Boolean singleRow = true;
    private String skipEval = null;
    private String totalEval = null;

    public String getSkip() {
        return skipEval;
    }

    public void setSkip(String skip) {
        this.skipEval = skip;
    }
    
    
    public String getTotal() {
        return totalEval;
    }

    public void setTotal(String total) {
        this.totalEval = total;
    }
    
    public Boolean getSingleRow() {
        return singleRow;
    }

    public void setSingleRow(Boolean singleRow) {
        this.singleRow = singleRow;
    }

    public Boolean getUpperTag() {
        return upperTag;
    }

    public void setUpperTag(Boolean upperTag) {
        this.upperTag = upperTag;
    }

    public abstract Object exec(Connection connection, Map<String, Object> params);

    public abstract Stream<Map<String, Object>> execQuery(Connection connection, Long skip, Integer total, Map<String, Object> params);

    public String getSql() {
        return elsql;
    }

    public void setSql(String sql) {
        this.elsql = sql.trim();
    }

    public void setNamedparameters(boolean namedparameters) {
        this.namedparameters = namedparameters;
    }

    public boolean isNamedparameters() {
        return namedparameters;
    }

    public List<ContextDefinition> getDefinitions() {
        return definitions;
    }

    public List<ContextDefinition> getParams() {
        return params;
    }

    String buildSql(PageMVELFactory mvel) {
        String sql = mvel.resolveTemplate(elsql);

        sql = sql.trim();
        return sql;
    }

    Long buidSkip(PageMVELFactory mvel, Map<String, Object> context){
        String elSkip=this.getSkip();
        if (!Auxilary.isEmptyOrNull(elSkip)){
           Long ret= mvel.getValue(elSkip, Long.class);
           return ret;
        }
        return null;
    }
    
    Integer buidTotal(PageMVELFactory mvel, Map<String, Object> context){
        String elTotatal=this.getTotal();
        if (!Auxilary.isEmptyOrNull(elTotatal)){
           Integer ret= mvel.getValue(elTotatal, Integer.class);
           return ret;
        }
        return null;
    }
    
    
    void buildDefinitions(PageMVELFactory mvel, Map<String, Object> context) {
        definitions.forEach((ContextDefinition definition) -> {
            String name = definition.getName();
            String ifEl = definition.getIfExpr();
            String elvalue = definition.getValue();
            boolean set = true;
            if (!Auxilary.isEmptyOrNull(ifEl)) {
                set = mvel.getValue(ifEl, Boolean.class);
            }
            if (set) {
                Object value = mvel.getValue(elvalue);
                mvel.setObject(name, value);
            }
        });
    }

    public List<DmlParams> buildDmlParams(PageMVELFactory mvel) {
        List<DmlParams> ret = new ArrayList();
        params.stream().forEach((paramDefinition) -> {
            String ifEl = paramDefinition.getIfExpr();
            boolean set = true;
            if (!Auxilary.isEmptyOrNull(ifEl)) {
                set = mvel.getValue(ifEl, Boolean.class);
            }
            if (set) {
                String type = paramDefinition.getType();
                String name = paramDefinition.getName();
                String objectType = paramDefinition.getObjectType();

                boolean isIn = paramDefinition.isIn();
                boolean isOut = paramDefinition.isOut();
                int jdbcType = JdbcTypeMapping.JDBCTypeTranslate(type);
                DmlParams dmlParams = new DmlParams();
                dmlParams.setName(name);
                dmlParams.setJdbcType(jdbcType);
                dmlParams.setObjectType(objectType);
                dmlParams.setIn(isIn);
                dmlParams.setOut(isOut);
                ret.add(dmlParams);
            }
        });
        return ret;
    }

    public Map<String, Object> buildParams(PageMVELFactory mvel, Map<String, Object> prms) {
        Map ret = new LinkedHashMap();
        params.stream().forEach((paramDefinition) -> {
            boolean isIn = paramDefinition.isIn();
            if (isIn) {
                String ifEl = paramDefinition.getIfExpr();
                boolean set = true;
                if (!Auxilary.isEmptyOrNull(ifEl)) {
                    set = mvel.getValue(ifEl, Boolean.class);
                }

                if (set) {
                    String paramName = paramDefinition.getName();
                    String elValue = paramDefinition.getValue();
                    if (elValue != null) {
                        Object value = mvel.getValue(elValue);
                        ret.put(paramName, value);
                    } else {
                        Object value = prms.get(paramName);
                        ret.put(paramName, value);
                    }
                }
            }
        });
        return ret;
    } 
}
