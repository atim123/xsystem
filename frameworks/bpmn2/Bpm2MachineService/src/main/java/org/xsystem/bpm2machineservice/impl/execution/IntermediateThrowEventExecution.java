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
package org.xsystem.bpm2machineservice.impl.execution;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.xsystem.bpmn2.model.activities.SendTask;
import org.xsystem.bpmn2.model.common.FlowNode;
import org.xsystem.bpmn2.model.common.Message;
import org.xsystem.bpmn2.model.events.EventDefinition;
import org.xsystem.bpmn2.model.events.IntermediateThrowEvent;
import org.xsystem.bpmn2.model.events.MessageEventDefinition;
import org.xsystem.bpmn2.model.system.Reference;

import org.xsystem.bpm2machineservice.impl.ExecutionService;
import org.xsystem.bpm2machineservice.impl.Token;

/**
 *
 * @author Andrey Timofeev
 */
public class IntermediateThrowEventExecution extends Execution{

    public IntermediateThrowEventExecution(ExecutionService owner) {
        super(owner);
    }

    @Override
    public void execute(Token token) {
        String processInstanceId = token.getProcess();
        Reference<FlowNode> ref = owner.getFlowNode(token);
        IntermediateThrowEvent throwEvent = (IntermediateThrowEvent) ref.resolvedReference();
        
        Map messageData = new LinkedHashMap();
        Map processContext= owner.getProcessContext(processInstanceId,null);
        AssociationsResolver resolver= new AssociationsResolver(processContext,messageData,owner.getScriptingService());
        
        List associationList= throwEvent.getDataInputAssociations();
        resolver.resolve(associationList);
        
        moveToken(token);
        
        
        List<EventDefinition> eventDefinitions = throwEvent.getEventDefinitions();
        eventDefinitions.stream()
                .filter(eventDefinition -> eventDefinition instanceof MessageEventDefinition)
                .map(eventDefinition -> (MessageEventDefinition) eventDefinition)
                .forEach(messageEventDefinition -> {
                    Reference<Message> msgRef = messageEventDefinition.getMessageRef();
                    String mesageid = msgRef.getQName().getLocalPart();
                    owner.sendMessage(mesageid,messageData);;
                });
    }

    @Override
    public void complite(Token token, Map<String, Object> contex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
