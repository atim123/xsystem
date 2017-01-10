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

import java.io.File;
import java.sql.Connection;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.w3c.dom.Document;


/**
 *
 * @author Andrey Timofeev
 */
public interface Enviroment {
    
    public static String FILEPRFX="file://";
    
    
    public static Enviroment getEnviroment(){
       return PageServlet2.getEnviroment();
    }
    
    public static boolean isFile(String path) {
        if (path != null) {
            return path.startsWith(FILEPRFX);
        }
        return false;
    }
    
    public HttpServletRequest getHttpRequest();
    
    public Connection getConnection();
    
    public Document  getDocument();
    
    public File getFile(String fname);
    
    public Map<String, String> getObjectNames();
    
}
