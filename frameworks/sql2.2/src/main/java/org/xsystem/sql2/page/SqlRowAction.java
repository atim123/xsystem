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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.xsystem.sql2.dml.DmlCommand;
import org.xsystem.sql2.dml.DmlParams;
import org.xsystem.sql2.page.mvel.PageMVELFactory;

/**
 *
 * @author Andrey Timofeev
 */
public class SqlRowAction extends SqlAction{
    @Override
    public Object exec(Connection connection, Map<String, Object> params) {
        Map<String, Object> context = new LinkedHashMap();
        context.putAll(params);
        PageMVELFactory elFactory = new PageMVELFactory(context, connection);
        buildDefinitions(elFactory, context);
        String sql=buildSql(elFactory);
        
        DmlCommand dml=new DmlCommand();
        dml.setNamedParams(this.isNamedparameters());
        dml.setUpperTag(this.getUpperTag());
        
        
        List<DmlParams> dmlParams= buildDmlParams(elFactory);
        Map<String, Object> prms =buildParams(elFactory,params);
        Object ret=dml.row(connection, sql, dmlParams, prms);
 
        return ret;
    }

    @Override
    public Stream<Map<String, Object>> execQuery(Connection connection, Long skip, Integer total, Map<String, Object> params) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
