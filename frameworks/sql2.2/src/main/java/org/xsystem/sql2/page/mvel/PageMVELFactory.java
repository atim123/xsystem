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
package org.xsystem.sql2.page.mvel;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.Map;
import org.mvel2.MVEL;
import org.mvel2.templates.TemplateRuntime;
import org.xsystem.sql2.page.functions.PageFunctions;
import org.xsystem.sql2.page.functions.SqlFunctions;

/**
 *
 * @author Andrey Timofeev
 */
public class PageMVELFactory {
    Map actionContext=new LinkedHashMap();
    Connection connection;
    
    public PageMVELFactory(Map actionContext, Connection connection){
        this.connection = connection;
        this.actionContext.putAll(actionContext);
        SqlFunctions sqlFunctions = new SqlFunctions(connection);
        PageFunctions pageFunction = new PageFunctions(actionContext);
        this.actionContext.put("SQL",sqlFunctions);
        this.actionContext.put("PAGE",pageFunction);
       
    }
    
    public void setObject(String name,Object value){
        actionContext.put(name, value);
    }
    
    public Object getValue(String mvel) {
      Object ret=   MVEL.eval(mvel,actionContext);
      return ret;
    }
    
    public <T> T getValue(String mvel,Class<T> toType) {
      Object ret=   MVEL.eval(mvel,actionContext,toType);
      return(T) ret;
    }
    
    public String resolveTemplate(String template){
       String ret= (String)TemplateRuntime.eval(template,actionContext);
       return ret;
    }
}
