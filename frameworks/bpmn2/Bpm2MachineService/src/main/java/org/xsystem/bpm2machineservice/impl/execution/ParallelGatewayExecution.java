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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.xsystem.bpm2machineservice.impl.ExecutionService;
import org.xsystem.bpm2machineservice.impl.Token;
import org.xsystem.bpmn2.model.common.FlowNode;
import org.xsystem.bpmn2.model.gateways.ParallelGateway;
import org.xsystem.bpmn2.model.system.Reference;

/**
 *
 * @author Andrey Timofeev
 */
public class ParallelGatewayExecution extends Execution{

    public ParallelGatewayExecution(ExecutionService owner) {
        super(owner);
    }

    @Override
    public void execute(Token token) {
        Reference<FlowNode> ref = owner.getFlowNode(token); 
        ParallelGateway gateway =(ParallelGateway) ref.resolvedReference();
        List<Reference<FlowNode>> outgoing = gateway.getOutgoingNode();
        
        if (token.getParent()!=null) {
            Token parent = owner.getParentToken(token);
            if (owner.getFlowNode(parent).resolvedReference() instanceof ParallelGateway) {
                owner.destroyToken(token);
                if (! owner.getChildList(parent).isEmpty()) {
                    return; //Ждем
                } else {
                    parent.setIsactive(true);
                            
                    parent.setActivity(gateway.getId());
                            
                    owner.saveToken(parent); //????
                    // идем по ветки fork
                    token = parent;
                }
            }
        }
        if (outgoing.size() == 1) {
            Reference<FlowNode> newref = outgoing.get(0);
            owner.takeToken(token, newref);
          } else if (outgoing.size() > 1) {
            token.setIsactive(false);

            owner.saveToken(token);
            final Token rootToken = token;
            //Поcтроить  список новых токенов
            outgoing.stream()
                    .forEach(element -> {
                        Token newToken = owner.newToken(rootToken, element);
                        owner.takeToken(newToken,element);
                    });
        } else {
            // error
        } 
        
    }

    @Override
    public void complite(Token token, Map<String, Object> contex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
