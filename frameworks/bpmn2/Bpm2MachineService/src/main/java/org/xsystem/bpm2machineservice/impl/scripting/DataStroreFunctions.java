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
package org.xsystem.bpm2machineservice.impl.scripting;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;
import org.xsystem.bpm2machineservice.impl.ExecutionService;
import org.xsystem.sql2.page.PAGE;
import org.xsystem.system.sql.JDBCTransationManager2;
import org.xsystem.utils.XMLUtil;

/**
 *
 * @author Andrey Timofeev
 */
public class DataStroreFunctions {
    protected final ExecutionService owner;

    Map<String, String> sqlStmts = new HashMap();

    public DataStroreFunctions(ExecutionService owner) {
        this.owner = owner;
    }

    public Object create(String id, Map params) {
        Connection con = getConnection();
        Document xml = getDocument(id);
        Object ret = PAGE.dml(con, xml, "create", params);
        return ret;
    }

    public Object update(String id, Map params) {
        Connection con = getConnection();
        Document xml = getDocument(id);
        Object ret = PAGE.dml(con, xml, "update", params);
        return ret;
        
    }

    public Object get(String id, Map params) {
        Connection con = getConnection();
        Document xml = getDocument(id);
        Object ret = PAGE.row(con, xml, "get", params);
       return ret;
    }

    public void delete(String id, Map params) {
        Connection con = getConnection();
        Document xml = getDocument(id);
        Object ret = PAGE.dml(con, xml, "delete", params);
    }

    Connection getConnection() {
        return JDBCTransationManager2.getConnection();
    }

    Document getDocument(String id) {
        String curStmt = sqlStmts.get(id);
        if (curStmt == null) {
            curStmt = owner.getDatastore(id);
            sqlStmts.put(id, curStmt);
        }
        Document ret = XMLUtil.getDocumentE(curStmt);
        return ret;
    }
}
