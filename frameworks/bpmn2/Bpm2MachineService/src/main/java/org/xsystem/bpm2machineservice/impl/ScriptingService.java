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
package org.xsystem.bpm2machineservice.impl;

import java.util.Calendar;
import java.util.Map;
import org.apache.commons.jxpath.ClassFunctions;
import org.apache.commons.jxpath.FunctionLibrary;
import org.apache.commons.jxpath.JXPathContext;
import org.mvel2.MVEL;
import org.mvel2.integration.impl.MapVariableResolverFactory;
import org.xsystem.bpm2machineservice.impl.scripting.CalendarHelper;
import org.xsystem.bpm2machineservice.impl.scripting.DataStroreFunctions;
import org.xsystem.utils.Auxilary;

/**
 *
 * @author Andrey Timofeev
 */
public class ScriptingService {
    
    ExecutionService owner;
    public ScriptingService(ExecutionService owner){
       this.owner= owner;
    }
    
    MapVariableResolverFactory buildResolverFactory(){
        MapVariableResolverFactory externalCtx=new MapVariableResolverFactory(); 
        
        DataStroreFunctions dataStore=new DataStroreFunctions(owner);
        externalCtx.createVariable("DataStore",dataStore);
        externalCtx.createVariable("Calendar",Calendar.class);
        externalCtx.createVariable("CalendarHelper",CalendarHelper.class);
        return externalCtx;
    }
    
    public   <T extends Object> T mvelEval(String expression, Map<String, Object> vars, Class<T> toType) {
        MapVariableResolverFactory externalCtx=buildResolverFactory();
        return  MVEL.eval(expression, vars,externalCtx,toType);
    }
    
    public void mvelEval(String expression, Map<String, Object> vars){
        MapVariableResolverFactory externalCtx=buildResolverFactory();
        Object ret=MVEL.eval(expression, vars,externalCtx);
        Auxilary.XXX(ret);
    }
    
    public JXPathContext makeJXPathContext(Map<String, Object> data){
        JXPathContext context = JXPathContext.newContext(data);
        FunctionLibrary lib = new FunctionLibrary();
        lib.addFunctions(new ClassFunctions(CalendarHelper.class,"calendarhelper"));
        lib.addFunctions(new ClassFunctions(Calendar.class,"calendar"));
        
        context.setFunctions(lib);
        
        return context;
    }
}
