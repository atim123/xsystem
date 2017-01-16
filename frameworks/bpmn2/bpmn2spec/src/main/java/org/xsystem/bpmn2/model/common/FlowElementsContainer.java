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
package org.xsystem.bpmn2.model.common;

import java.util.List;
import java.util.stream.Collectors;
import org.xsystem.bpmn2.model.foundation.BaseElement;
import org.xsystem.bpmn2.model.gateways.ExclusiveGateway;
import org.xsystem.bpmn2.model.process.LaneSet;
import org.xsystem.bpmn2.model.system.Reference;

/**
 *
 * @author Andrey Timofeev
 */
public interface FlowElementsContainer extends BaseElement{
    public List<FlowElement> getFlowElements();

    //laneSets: LaneSet [0..*]
    public List<LaneSet> getLaneSets();
    
    public default List getFlowElements(Class typ) {

        return getFlowElements().stream().
                filter(element -> typ.isInstance(element))
                .collect(Collectors.toList());
    }

    public default void prepare() {
        List<SequenceFlow> sequenceFlows = getFlowElements(SequenceFlow.class);

        // List<FlowNode> flowNodes= getFlowElements(FlowNode.class);
        sequenceFlows.stream().forEach(sequenceFlow -> {
            Reference<SequenceFlow> baseRef = sequenceFlow.getReference();
            
            Reference<FlowNode> sourceRef = sequenceFlow.getSourceRef();
            if (sourceRef != null) {
                FlowNode srcFlowNode = sourceRef.resolvedReference();
                if (srcFlowNode.getOutgoing().stream()
                        .filter(sequenceFlowRef -> sequenceFlowRef.equals(baseRef))
                        .findFirst().isPresent()) {
                } else {
                    srcFlowNode.getOutgoing().add(baseRef);
                }
            }
            
            Reference<FlowNode> targetRef = sequenceFlow.getTargetRef();
            if (targetRef != null) {
                FlowNode targetFlowNode = targetRef.resolvedReference();
                if (targetFlowNode.getIncoming().stream()
                        .filter(sequenceFlowRef -> sequenceFlowRef.equals(baseRef))
                        .findFirst().isPresent()) {
                } else {
                    targetFlowNode.getIncoming().add(baseRef);
                }
            }        
           
   
        });
        
         List<ExclusiveGateway> exclusiveGateways= getFlowElements(ExclusiveGateway.class);
                    exclusiveGateways.forEach(exclusiveGateway->{
                        exclusiveGateway.prepare();
                    });
    }
}
