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
package org.xsystem.bpmn2.modelimpl.process;

import java.util.ArrayList;
import java.util.List;
import org.xsystem.bpmn2.model.activities.ResourceRole;
import org.xsystem.bpmn2.model.common.CorrelationSubscription;
import org.xsystem.bpmn2.model.common.FlowElement;
import org.xsystem.bpmn2.model.data.Property;
import org.xsystem.bpmn2.model.process.LaneSet;
import org.xsystem.bpmn2.modelimpl.common.CallableElementImpl;
import org.xsystem.bpmn2.model.process.Process;
import org.xsystem.bpmn2.model.process.ProcessType;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;


/**
 *
 * @author Andrey Timofeev
 */
public class ProcessImpl extends CallableElementImpl implements Process{
    List<FlowElement> flowElements=new ArrayList();// [0..*]
    List<CorrelationSubscription> correlationSubscriptions=new ArrayList();
    ProcessType processType=ProcessType.None;
    List<LaneSet> laneSets=new ArrayList();
    boolean isExecutable;// [0..1]
    boolean isClosed = false;
    String state = "None";
    
    List<Property> properties=new ArrayList();//: Property [0..*]
    List<ResourceRole> resources=new ArrayList();
    
    @Override
     public String TypeName(){
        return "process";
    }
    
    public ProcessImpl(DefinitionsImpl definitions){
        super(definitions);
    }
    
    @Override
    public List<FlowElement> getFlowElements() {
        return flowElements;
    }

    @Override
    public boolean getIsClosed() {
        return isClosed;
    }

    @Override
    public void setIsClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }

    @Override
    public boolean getIsExecutable() {
        return isExecutable;
    }

    @Override
    public void setIsExecutable(boolean isExecutable) {
        this.isExecutable = isExecutable;
    }

    @Override
    public ProcessType getProcessType() {
        return processType;
    }

    @Override
    public void setProcessType(ProcessType processType) {
        this.processType = processType;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public void setState(String state) {
        this.state = state;
    }

    @Override
    public List<Property> getProperties() {
        return properties;
    }
    
    public List<CorrelationSubscription> getCorrelationSubscriptions(){
       return correlationSubscriptions;
    }

    @Override
    public List<LaneSet> getLaneSets() {
        return laneSets;
    }
    
    public List<ResourceRole> getResources(){
        return this.resources;
    }

    
}
