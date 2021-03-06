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
package org.xsystem.bpmn2.modelimpl.conversation;

import java.util.ArrayList;
import java.util.List;
import org.xsystem.bpmn2.model.collaboration.MessageFlow;
import org.xsystem.bpmn2.model.collaboration.Participant;
import org.xsystem.bpmn2.model.common.CorrelationKey;
import org.xsystem.bpmn2.model.conversation.ConversationNode;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.foundation.BaseElementImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;


/**
 *
 * @author Andrey Timofeev
 */
public class ConversationNodeImpl extends BaseElementImpl implements ConversationNode{
    String name; 
    List<Reference<Participant>> participantRefs=new ArrayList();
    List<Reference<MessageFlow>> messageFlowRefs=new ArrayList();
    List<CorrelationKey> correlationKeys=new ArrayList();
    
    
    public ConversationNodeImpl(DefinitionsImpl definitions){
        super(definitions);
    }
    
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name=name;
    }

    @Override
    public List<Reference<Participant>> getParticipantRefs() {
        return participantRefs;
    }

    @Override
    public List<Reference<MessageFlow>> getMessageFlowRefs() {
        return messageFlowRefs;
    }

    @Override
    public List<CorrelationKey> getCorrelationKeys() {
        return correlationKeys;
    }

    
}
