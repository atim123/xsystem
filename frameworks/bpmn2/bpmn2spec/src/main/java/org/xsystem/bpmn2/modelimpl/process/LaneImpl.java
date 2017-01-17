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
import org.xsystem.bpmn2.model.common.FlowNode;
import org.xsystem.bpmn2.model.foundation.BaseElement;
import org.xsystem.bpmn2.model.process.Lane;
import org.xsystem.bpmn2.model.process.LaneSet;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.foundation.BaseElementImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;

/**
 *
 * @author Andrey Timofeev
 */
public class LaneImpl extends BaseElementImpl implements Lane {

    String name;
    BaseElement partitionElement;
    Reference<BaseElement> partitionElementRef;
    LaneSet childLaneSet;
    List<Reference<FlowNode>> flowNodeRefs = new ArrayList();

    public LaneImpl(DefinitionsImpl definitions) {
        super(definitions);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public BaseElement getPartitionElement() {
        return partitionElement;
    }

    @Override
    public void setPartitionElement(BaseElement partitionElement) {
        this.partitionElement=partitionElement;
    }

    @Override
    public Reference<BaseElement> getPartitionElementRef() {
        return partitionElementRef;
    }

    @Override
    public void setPartitionElementRef(Reference<BaseElement> partitionElementRef) {
        this.partitionElementRef=partitionElementRef;
    }

    @Override
    public LaneSet getChildLaneSet() {
        return childLaneSet;
    }

    @Override
    public void setChildLaneSet(LaneSet childLaneSet) {
        this.childLaneSet=childLaneSet;
    }

    @Override
    public List<Reference<FlowNode>> getFlowNodeRefs() {
        return flowNodeRefs;
    }

    
}
