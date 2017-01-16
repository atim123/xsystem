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
package org.xsystem.bpmn2.model.conversation;
import java.util.List;
import org.xsystem.bpmn2.model.collaboration.InteractionNode;
import org.xsystem.bpmn2.model.collaboration.MessageFlow;
import org.xsystem.bpmn2.model.collaboration.Participant;
import org.xsystem.bpmn2.model.common.CorrelationKey;
import org.xsystem.bpmn2.model.foundation.BaseElement;
import org.xsystem.bpmn2.model.system.Reference;


/**
 *
 * @author Andrey Timofeev
 */
public interface ConversationNode extends BaseElement ,InteractionNode{

    public String getName();

    public void setName(String name);

    //  participantRefs: Participant[2..*]
    List<Reference<Participant>> getParticipantRefs();

    // messageFlowRefs: MessageFlow [0..*] 
    List<Reference<MessageFlow>> getMessageFlowRefs();

    // correlationKeys:CorrelationKey [0..*]  
    List<CorrelationKey> getCorrelationKeys();
}
