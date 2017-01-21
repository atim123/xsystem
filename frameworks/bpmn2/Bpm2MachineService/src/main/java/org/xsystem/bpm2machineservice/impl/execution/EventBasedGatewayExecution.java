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
import java.util.List;
import java.util.Map;
import org.xsystem.bpm2machineservice.impl.ExecutionService;
import org.xsystem.bpm2machineservice.impl.Token;
import org.xsystem.bpmn2.model.activities.ReceiveTask;
import org.xsystem.bpmn2.model.common.FlowNode;
import org.xsystem.bpmn2.model.events.IntermediateCatchEvent;
import org.xsystem.bpmn2.model.gateways.EventBasedGateway;
import org.xsystem.bpmn2.model.system.Reference;

/**
 *
 * @author Andrey Timofeev
 */
public class EventBasedGatewayExecution extends Execution{

    public EventBasedGatewayExecution(ExecutionService owner) {
        super(owner);
    }

    @Override
    public void execute(Token token) {
   
       Reference<FlowNode> ref = owner.getFlowNode(token); 
        EventBasedGateway gateway =(EventBasedGateway) ref.resolvedReference();
        List<Reference<FlowNode>> outgoing = gateway.getOutgoingNode();
        
        token.setIsactive(false);
        owner.saveToken(token);
        final Token rootToken = token;
        outgoing.stream()
                .map(refe->refe.resolvedReference())
                .filter(  flowNode->{return (  flowNode instanceof IntermediateCatchEvent)
                                     ||(flowNode instanceof ReceiveTask );})
                .forEach(flowNode->{
                      Token newToken = owner.newToken(rootToken,flowNode.getReference());
                        owner.takeToken(newToken,flowNode.getReference());         
                });
   
    }

    @Override
    public void complite(Token token, Map<String, Object> contex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

