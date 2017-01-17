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
import org.xsystem.bpmn2.model.data.DataOutput;
import org.xsystem.bpmn2.model.data.DataOutputAssociation;
import org.xsystem.bpmn2.model.events.CatchEvent;
import org.xsystem.bpmn2.model.events.EventDefinition;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;


/**
 *
 * @author Andrey Timofeev
 */
public class CatchEventImpl extends EventImpl implements CatchEvent{
    List<EventDefinition> eventDefinitions=new ArrayList();
    List<DataOutput>      dataOutputs=new ArrayList();
    List<DataOutputAssociation>  dataOutputAssociations=new ArrayList();
    
    @Override
    public String TypeName(){
        return "catchEvent";
    }
    
    public CatchEventImpl(DefinitionsImpl definitions){
        super(definitions);
    }
    
    @Override
    public List<EventDefinition> getEventDefinitions(){
        return eventDefinitions;
    }
    
    @Override
    public List<DataOutput>      getDataOutputs(){
        return dataOutputs;
    }
    
    @Override
    public List<DataOutputAssociation>    getDataOutputAssociations(){
        return dataOutputAssociations;
    }

    
}
