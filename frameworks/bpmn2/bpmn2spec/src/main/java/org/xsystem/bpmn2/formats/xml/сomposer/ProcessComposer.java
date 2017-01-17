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
package org.xsystem.bpmn2.formats.xml.сomposer;

import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xsystem.bpmn2.model.common.CorrelationKey;
import org.xsystem.bpmn2.model.common.CorrelationPropertyBinding;
import org.xsystem.bpmn2.model.common.CorrelationSubscription;
import org.xsystem.bpmn2.model.common.FlowElement;
import org.xsystem.bpmn2.model.common.FlowNode;
import org.xsystem.bpmn2.model.common.FormalExpression;
import org.xsystem.bpmn2.model.process.Lane;
import org.xsystem.bpmn2.model.process.LaneSet;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.utils.XMLUtil;
/**
 *
 * @author Andrey Timofeev
 */

class ProcessComposer extends BaseElementComposer {
    
    void makeCorrelationPropertyBinding(Element root,CorrelationPropertyBinding correlationPropertyBinding){
       Element ret= ComposerFactory.makeBaseElement(root,"correlationPropertyBinding", correlationPropertyBinding);
       ComposerFactory.setAttr(ret,"correlationPropertyRef",correlationPropertyBinding.getCorrelationPropertyRef());
       FormalExpression formalExpression= correlationPropertyBinding.getDataPath();
        
       XMLUtil.createNewCDATAElement(ret,"dataPath",formalExpression.getBody());
        // CorrelationPropertyBinding
    }
    
    void makeCorrelationSubscription(Element root,CorrelationSubscription correlationSubscriptions){
        
        Reference<CorrelationKey> ref=correlationSubscriptions.getCorrelationKeyRef();
        
        ComposerFactory.setAttr(root,"correlationKeyRef",ref);
        List<CorrelationPropertyBinding>  correlationPropertyBindings=correlationSubscriptions.getCorrelationPropertyBinding();
        correlationPropertyBindings.stream().forEach(action->{
            makeCorrelationPropertyBinding(root,action);
        });
    }
    
    
    void makeLane(Element root,Lane lane){
        ComposerFactory.setAttr(root,"name",lane.getName());
        List<Reference<FlowNode>> flowNpdeRefs=lane.getFlowNodeRefs();
        flowNpdeRefs.stream().forEach(action->{
            ComposerFactory.setRefBody( root,"flowNodeRef",action);
        });
    }
    
    void makeLaneSet(Element root,LaneSet laneSet){
        ComposerFactory.setAttr(root,"name",laneSet.getName());
        List<Lane> lanes=laneSet.getLanes();
        lanes.stream().forEach(action->{
            makeLane(ComposerFactory.makeBaseElement(root,"lane",action),action);
        });
        
    }
    
    @Override
    public Element composer(Element root, Object src) {
        org.xsystem.bpmn2.model.process.Process proc = (org.xsystem.bpmn2.model.process.Process) src;
        Element ret = makeBaseElement(root, "process", proc);
        String isExecutable = "false";
        if (proc.getIsExecutable()) {
            isExecutable = "true";
        }
        String isClosed = "false";
        if (proc.getIsClosed()) {
            isClosed = "true";
        }
        String processType = proc.getProcessType().toString();
        ComposerFactory.setAttr(ret, "name", proc.getName());
        ComposerFactory.setAttr(ret, "isClosed", isClosed);
        ComposerFactory.setAttr(ret, "isExecutable", isExecutable);
       ComposerFactory.setAttr(ret, "processType", processType);

       List<LaneSet> laneSets=proc.getLaneSets();
       laneSets.stream().forEach(action->{
            makeLaneSet(ComposerFactory.makeBaseElement(ret,"laneSet",action),action);
       });
       //laneSet
       
       
        List<FlowElement> flowElements = proc.getFlowElements();
        flowElements.stream().forEach((bpmElement) -> {
            ComposerFactory.makeDiagramElement(ret, bpmElement);
        });

        List<CorrelationSubscription> correlationSubscriptions=proc.getCorrelationSubscriptions();
        correlationSubscriptions.stream().forEach(action->{
            makeCorrelationSubscription(ComposerFactory.makeBaseElement(ret,"correlationSubscription",action),action);
        });
        
        return ret;
    }
}
