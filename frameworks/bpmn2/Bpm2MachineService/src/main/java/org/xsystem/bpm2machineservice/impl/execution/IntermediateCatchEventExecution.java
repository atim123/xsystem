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
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.xsystem.bpm2machineservice.impl.ExecutionService;
import org.xsystem.bpm2machineservice.impl.Token;
import org.xsystem.bpmn2.model.common.FlowNode;
import org.xsystem.bpmn2.model.common.Message;
import org.xsystem.bpmn2.model.events.EventDefinition;
import org.xsystem.bpmn2.model.events.IntermediateCatchEvent;
import org.xsystem.bpmn2.model.events.MessageEventDefinition;
import org.xsystem.bpmn2.model.gateways.EventBasedGateway;
import org.xsystem.bpmn2.model.system.Reference;

/**
 *
 * @author Andrey Timofeev
 */
public class IntermediateCatchEventExecution extends Execution{

    public IntermediateCatchEventExecution(ExecutionService owner) {
        super(owner);
    }

    @Override
    public void execute(Token token) {
         Reference<FlowNode> ref = owner.getFlowNode(token);
        IntermediateCatchEvent intermediateCatchEvent = (IntermediateCatchEvent) ref.resolvedReference();
        List<EventDefinition> eventDefinitions = intermediateCatchEvent.getEventDefinitions();
        eventDefinitions.stream()
                .filter(eventDefinition -> eventDefinition instanceof MessageEventDefinition)
                .map(eventDefinition -> (MessageEventDefinition) eventDefinition)
                .forEach(messageEventDefinition -> {
                    Reference<Message> msgRef = messageEventDefinition.getMessageRef();
                    String mesageid = msgRef.getQName().getLocalPart();
                    String tokenId = token.getId();
                    owner.toWaitsToken(tokenId, mesageid);
                });
    }

    @Override
    public void complite(Token token, Map<String, Object> messageData) {
       Reference<FlowNode> ref = owner.getFlowNode(token);
        IntermediateCatchEvent intermediateCatchEvent = (IntermediateCatchEvent) ref.resolvedReference();
        //List<EventDefinition> eventDefinitions= intermediateCatchEvent.getEventDefinitions();

        List associationList = intermediateCatchEvent.getDataOutputAssociations();

        String processInstanceId = token.getProcess();
        Map processContext = owner.getProcessContext(processInstanceId, null);
        AssociationsResolver resolver = new AssociationsResolver(messageData, processContext,owner.getScriptingService());
        resolver.resolve(associationList);
        owner.setProcessContext(processInstanceId,processContext);//!!!
        
        List<Reference<FlowNode>> outgoing = intermediateCatchEvent.getOutgoingNode();
        Reference<FlowNode> newref = outgoing.get(0);

        MessageReceiver receiver= new MessageReceiver(owner);
        Token retToken=receiver.complete(token);
        
        owner.takeToken(retToken, newref);
        
        
    }
    
}
