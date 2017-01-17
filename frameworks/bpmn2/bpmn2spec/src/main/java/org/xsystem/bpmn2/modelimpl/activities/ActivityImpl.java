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
import java.util.ArrayList;
import java.util.List;
import org.xsystem.bpmn2.model.activities.Activity;
import org.xsystem.bpmn2.model.activities.LoopCharacteristics;
import org.xsystem.bpmn2.model.activities.ResourceRole;
import org.xsystem.bpmn2.model.common.SequenceFlow;
import org.xsystem.bpmn2.model.data.DataInputAssociation;
import org.xsystem.bpmn2.model.data.DataOutputAssociation;
import org.xsystem.bpmn2.model.data.InputOutputSpecification;
import org.xsystem.bpmn2.model.data.Property;
import org.xsystem.bpmn2.modelimpl.common.FlowNodeImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;

/**
 *
 * @author Andrey Timofeev
 */
public class ActivityImpl extends FlowNodeImpl implements Activity{
    List<DataOutputAssociation>dataOutputAssociations= new ArrayList();// [0..*]
    List<DataInputAssociation>datainputAssociations= new ArrayList();// [0..*]
    
    InputOutputSpecification ioSpecification;//: Input OutputSpecification [0..1]
    List<Property> properties=new ArrayList();//: Property [0..*]
    LoopCharacteristics loopCharacteristics;
    List<ResourceRole> resources=new ArrayList();
    
    @Override
    public String TypeName(){
        return "Activity";
    }
    
    public ActivityImpl(DefinitionsImpl definitions){
        super(definitions);
    }
    
    @Override
    public List<DataOutputAssociation> getDataOutputAssociations() {
        return dataOutputAssociations;
    }

    @Override
    public List<DataInputAssociation> getDatainputAssociations() {
        return datainputAssociations;
    }

    @Override
    public InputOutputSpecification getIoSpecification() {
        return ioSpecification;
    }

    @Override
    public void setIoSpecification(InputOutputSpecification ioSpecification) {
        this.ioSpecification = ioSpecification;
    }
    
    @Override
    public  LoopCharacteristics getLoopCharacteristics(){
        return this.loopCharacteristics;
    };
    
    @Override
    public  void  setLoopCharacteristics(LoopCharacteristics loopCharacteristics){
        this.loopCharacteristics=loopCharacteristics;
    }
    
    @Override
    public List<ResourceRole> getResources(){
        return this.resources;
    }

    
}
