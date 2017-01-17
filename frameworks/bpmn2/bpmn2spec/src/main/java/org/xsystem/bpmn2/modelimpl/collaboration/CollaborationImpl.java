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
package org.xsystem.bpmn2.modelimpl.collaboration;

import java.util.ArrayList;
import java.util.List;
import org.xsystem.bpmn2.model.choreography.Choreography;
import org.xsystem.bpmn2.model.collaboration.Collaboration;
import org.xsystem.bpmn2.model.collaboration.MessageFlow;
import org.xsystem.bpmn2.model.collaboration.MessageFlowAssociation;
import org.xsystem.bpmn2.model.collaboration.Participant;
import org.xsystem.bpmn2.model.collaboration.ParticipantAssociations;
import org.xsystem.bpmn2.model.common.Artifact;
import org.xsystem.bpmn2.model.common.CorrelationKey;
import org.xsystem.bpmn2.model.conversation.ConversationAssociation;
import org.xsystem.bpmn2.model.conversation.ConversationLink;
import org.xsystem.bpmn2.model.conversation.ConversationNode;
import org.xsystem.bpmn2.model.infrastructure.Definitions;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.foundation.RootElementImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;


/**
 *
 * @author Andrey Timofeev
 */
public class CollaborationImpl extends RootElementImpl implements Collaboration  {

    String name;
    List<Reference<Choreography>> choreographyRef=new ArrayList();
    List<CorrelationKey> correlationKey=new ArrayList();
    List<ConversationAssociation> conversationAssociations=new ArrayList();
    List<ConversationNode> conversations=new ArrayList();
    List<ConversationLink> conversationLinks=new ArrayList();
    List<Artifact> artifacts=new ArrayList();
    List<Participant> participants=new ArrayList();
    List<ParticipantAssociations> participantAssociations=new ArrayList();
    List<MessageFlow> messageFlow=new ArrayList();
    List<MessageFlowAssociation> messageFlowAssociation=new ArrayList();
    
    Boolean isClosed;
    // collaboration
    
    @Override
    public String TypeName(){
        return "collaboration";
    }
    
    public CollaborationImpl(DefinitionsImpl definitions) {
        super(definitions);
    }
    
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name=name;
    }

    @Override
    public List<Reference<Choreography>> getCchoreographyRef() {
       return choreographyRef;
    }

    @Override
    public List<CorrelationKey> getCorrelationKey() {
        return correlationKey;
    }

    @Override
    public List<ConversationAssociation> getConversationAssociations() {
       return conversationAssociations;
    }

    @Override
    public List<ConversationNode> getConversations() {
        return conversations;
    }

    @Override
    public List<ConversationLink> getConversationLinks() {
        return conversationLinks;
    }

    @Override
    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    @Override
    public List<Participant> getParticipants() {
        return participants;
    }

    @Override
    public List<ParticipantAssociations> getParticipantAssociations() {
       return participantAssociations;
    }

    @Override
    public List<MessageFlow> getMessageFlow() {
        return messageFlow;
    }

    @Override
    public List<MessageFlowAssociation> getMessageFlowAssociation() {
       return messageFlowAssociation;
    }

    @Override
    public Boolean getIsClosed() {
        return isClosed;
     }

    @Override
    public void setIsClosed(Boolean isClosed) {
        this.isClosed=isClosed;
     }

    
}
