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
package org.xsystem.sql2.http.impl;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.xsystem.sql2.http.ConnectionManager;


/**
 *
 * @author Andrey Timofeev
 */
public class JNDIConnectionManager implements ConnectionManager{
    @Override
    public Connection create(String dsName) {
        try {
            InitialContext cxt = new InitialContext();

            DataSource ds = (DataSource) cxt.lookup(dsName);
            Connection ret = ds.getConnection();
            return ret;
        } catch (NamingException | SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

}
