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
package org.xsystem.bpmn2.modelimpl.activities;

import java.util.Map;
import java.util.LinkedHashMap;
import org.xsystem.bpmn2.model.activities.Rendering;
import org.xsystem.bpmn2.model.activities.UserTask;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;


/**
 *
 * @author Andrey Timofeev
 */
public class UserTaskImpl extends TaskImpl implements UserTask{
    String implementation  = "##unspecified";
    Rendering rendering;
    
    
    @Override
    public String TypeName(){
        return "userTask";
    }
    
    public UserTaskImpl(DefinitionsImpl definitions){
        super(definitions);
    }

    public String getImplementation() {
        return implementation;
    }

    public void setImplementation(String implementation) {
        this.implementation = implementation;
    }
    
    public Rendering getRendering(){
       return this.rendering;   
    }
    
    public void  setRendering(Rendering rendering){
       this.rendering=rendering;   
    }

    
}
