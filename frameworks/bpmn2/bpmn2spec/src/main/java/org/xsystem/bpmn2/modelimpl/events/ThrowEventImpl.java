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
package org.xsystem.bpmn2.modelimpl.events;

import java.util.ArrayList;
import java.util.List;
import org.xsystem.bpmn2.model.data.DataInput;
import org.xsystem.bpmn2.model.data.DataInputAssociation;
import org.xsystem.bpmn2.model.data.InputSet;
import org.xsystem.bpmn2.model.events.EventDefinition;
import org.xsystem.bpmn2.model.events.ThrowEvent;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;

/**
 *
 * @author Andrey Timofeev
 */
public class ThrowEventImpl extends EventImpl implements ThrowEvent {
    List<EventDefinition> eventDefinitions=new ArrayList();
    List<DataInput>      dataInputs=new ArrayList();
    List<DataInputAssociation> dataInputAssociations=new ArrayList();
    InputSet  inputSet;
    
    
    @Override
    public String TypeName(){
        return "throwEvent";
    }
    
    public ThrowEventImpl(DefinitionsImpl definitions) {
        super(definitions);
    }
    
    @Override
    public List<EventDefinition> getEventDefinitions(){
       return eventDefinitions;
    }
    
    @Override
    public List<DataInput>      getDataInputs(){
       return dataInputs;
    }
    
    @Override
    public List<DataInputAssociation>      getDataInputAssociations(){
        return dataInputAssociations;
    }
    
    @Override
    public InputSet  getInputSet(){
        return this.inputSet;
    }
    
    
    @Override
    public void setInputSet(InputSet inputSet){
        this.inputSet=inputSet;
    }

    
}
