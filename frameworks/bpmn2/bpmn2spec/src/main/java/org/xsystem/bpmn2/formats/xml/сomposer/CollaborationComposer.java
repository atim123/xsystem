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
import org.xsystem.bpmn2.model.collaboration.Collaboration;
import org.xsystem.bpmn2.model.collaboration.MessageFlow;
import org.xsystem.bpmn2.model.collaboration.Participant;
import org.xsystem.bpmn2.model.collaboration.ParticipantMultiplicity;
import org.xsystem.bpmn2.model.common.CorrelationKey;
import org.xsystem.bpmn2.model.common.CorrelationProperty;
import org.xsystem.bpmn2.model.conversation.ConversationNode;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.utils.XMLUtil;

/**
 *
 * @author Andrey Timofeev
 */
public class CollaborationComposer extends BaseElementComposer {

    void makeParticipant(Element root, Participant participant) {
        Element ret = makeBaseElement(root, "participant", participant);
        Reference<org.xsystem.bpmn2.model.process.Process> procRef = participant.getProcessRef();

        String processRef = ComposerFactory.getRefId(procRef);
        ComposerFactory.setAttr(ret, "processRef", processRef);
        ParticipantMultiplicity participantMultiplicity = participant.getParticipantMultiplicity();
        if (participantMultiplicity != null) {
            Element elem=makeBaseElement(ret, "participantMultiplicity",participantMultiplicity);
            XMLUtil.setIntegerAttr(elem,"maximum",participantMultiplicity.getMaximum());
            XMLUtil.setIntegerAttr(elem,"minimum",participantMultiplicity.getMinimum());
        }
        
    }

    ;
   
    /*
    <participant id="_2" name="Пациент" processRef="OrderingOnkogematologicheskyResearch">
      <participantMultiplicity maximum="1" minimum="0"/>
    </participant>
    */
    public void makeMessageFlow(Element root,MessageFlow messageFlow) {
        Element ret = makeBaseElement(root, "messageFlow", messageFlow);
        ComposerFactory.setAttr(ret,"name",messageFlow.getName());
        ComposerFactory.setAttr(ret,"messageRef",messageFlow.getMessageRef());
        ComposerFactory.setAttr(ret,"sourceRef",messageFlow.getSourceRef());
        ComposerFactory.setAttr(ret,"targetRef",messageFlow.getTargetRef());
    }
    
    public void makeCorrelationKey(Element root,CorrelationKey correlationKey){
        Element ret = makeBaseElement(root, "correlationKey",correlationKey);
        String name=correlationKey.getName();
        ComposerFactory.setAttr(ret,"name",name);
        List<Reference<CorrelationProperty>> correlationPropertyRef= correlationKey.getCorrelationProperty();
        correlationPropertyRef.stream().forEach(action -> {
            ComposerFactory.setRefBody(ret,"correlationPropertyRef",action);
        });
        
    }
    
    
    public void makeConversationNode(Element root,ConversationNode conversationNode) {
        Element conversationNodeElem = makeBaseElement(root, "conversation", conversationNode);
        
        List<Reference<MessageFlow>> messageFlows= conversationNode.getMessageFlowRefs();
        
        messageFlows.stream().forEach(action -> {
            ComposerFactory.setRefBody(conversationNodeElem,"messageFlowRef",action);
        });
        
        List<CorrelationKey> correlationKeys= conversationNode.getCorrelationKeys();
        correlationKeys.stream().forEach(action -> {
            makeCorrelationKey(conversationNodeElem,action);
        });
    }
    /*
    <conversation id="conversationOrderHandling">
            <messageFlowRef>_6-6389</messageFlowRef>
            <correlationKey id="correlOrder" name="Order Correlation Key">
                <correlationPropertyRef>propOrderID</correlationPropertyRef>
            </correlationKey>
     </conversation>
    */
    
    @Override
    public Element composer(Element root, Object src) {
        Collaboration collaboration = (Collaboration) src;

        Element ret = makeBaseElement(root, "collaboration", collaboration);

        List<Participant> participants = collaboration.getParticipants();
        //participant
        participants.stream().forEach(action -> {
            makeParticipant(ret, action);
        });

        List<MessageFlow> messageFlows= collaboration.getMessageFlow();
        
        messageFlows.stream().forEach(action -> {
            makeMessageFlow(ret, action);
        });
        
        List<CorrelationKey> correlationKeys= collaboration.getCorrelationKey();
        
        correlationKeys.forEach(action->{
            makeCorrelationKey(ret,action);
        });
        
        
        List<ConversationNode> conversationNodes= collaboration.getConversations();
        //conversation
        conversationNodes.forEach(action -> {
            makeConversationNode(ret, action);
        });
        
        System.out.println(conversationNodes);
        
        return ret;

    }

}
