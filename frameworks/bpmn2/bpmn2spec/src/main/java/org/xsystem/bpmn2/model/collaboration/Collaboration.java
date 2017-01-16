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
package org.xsystem.bpmn2.model.collaboration;

import java.util.List;
import org.xsystem.bpmn2.model.choreography.Choreography;
import org.xsystem.bpmn2.model.common.Artifact;
import org.xsystem.bpmn2.model.common.CorrelationKey;
import org.xsystem.bpmn2.model.conversation.ConversationAssociation;
import org.xsystem.bpmn2.model.conversation.ConversationLink;
import org.xsystem.bpmn2.model.conversation.ConversationNode;
import org.xsystem.bpmn2.model.foundation.RootElement;
import org.xsystem.bpmn2.model.system.Reference;


/**
 *
 * @author Andrey Timofeev
 */
public interface Collaboration extends RootElement{
    public String getName();
    public void setName(String name);
    
    //choreographyRef: Choreography [0..*]
     public List<Reference<Choreography>> getCchoreographyRef(); 
    
    //correlationKeys:CorrelationKey [0..*]
    public List<CorrelationKey> getCorrelationKey();// [0..*]
    
    //conversationAssociations: ConversationAssociation [0..*]
    public List<ConversationAssociation> getConversationAssociations();
    
    //conversations:ConversationNode [0..*]
    public List<ConversationNode> getConversations();
    
    //conversationLinks:ConversationLink [0..*]
    public List<ConversationLink> getConversationLinks();
    
   //artifacts: Artifact [0..*]
    
    public List<Artifact> getArtifacts();
    
    //participants: Participant [0..*]
    public List<Participant> getParticipants();
    
    
    //participantAssociations:ParticipantAssociations [0..*]
    public List<ParticipantAssociations> getParticipantAssociations();
    
    //messageFlow: Message Flow [0..*]
    public List<MessageFlow> getMessageFlow();
    
    //messageFlowAssociations:Message Flow Association [0..*]
    public List<MessageFlowAssociation> getMessageFlowAssociation();
    //IsClosed
    
    public Boolean getIsClosed();
    public void setIsClosed(Boolean isClosed);
}
