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
package org.xsystem.bpmn2.modelimpl.common;

import java.util.ArrayList;
import java.util.List;
import org.xsystem.bpmn2.model.common.FlowElement;
import org.xsystem.bpmn2.model.common.FlowElementsContainer;
import org.xsystem.bpmn2.model.process.LaneSet;
import org.xsystem.bpmn2.modelimpl.foundation.BaseElementImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;


/**
 *
 * @author Andrey Timofeev
 */
public class FlowElementsContainerImpl extends BaseElementImpl implements FlowElementsContainer{
    List<FlowElement> flowElements=new ArrayList();// [0..*]
    List<LaneSet> laneSets= new ArrayList();
    
    
    @Override
    public String TypeName(){
        return "FlowElementsContainer";
    }
    
    public FlowElementsContainerImpl(DefinitionsImpl definitions){
        super(definitions);
    }
    
    public List<FlowElement> getFlowElements() {
        return flowElements;
    }

    public List<LaneSet> getLaneSets(){
        return laneSets;
    }
}
