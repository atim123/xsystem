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
import org.xsystem.bpm2machineservice.impl.ExecutionService;
import org.xsystem.bpm2machineservice.impl.Token;
import org.xsystem.bpmn2.model.common.FlowNode;
import org.xsystem.bpmn2.model.gateways.EventBasedGateway;
import org.xsystem.bpmn2.model.system.Reference;

/**
 *
 * @author Andrey Timofeev
 */
public class MessageReceiver {
    protected final ExecutionService owner;
    
    public MessageReceiver(ExecutionService owner){
        this.owner=owner;
    }
    
    public Token complete(Token token){
        Token parentToken=owner.getParentToken(token);
        Reference<FlowNode> ref = owner.getFlowNode(token);
        FlowNode receiveTask=ref.resolvedReference();
        if (parentToken!=null){
           
           
           Reference<FlowNode> refparent= owner.getFlowNode(parentToken);
           FlowNode flowNode=refparent.resolvedReference();
           if (flowNode instanceof EventBasedGateway){
           
             List<Token> deleteToken=  owner.getChildList(parentToken);
             deleteToken.stream()
                     .forEach(action->{
                        owner.destroyToken(action);
                     });
             parentToken.setActivity("true");
             parentToken.setActivity(receiveTask.getId());
             return parentToken;
           }
        }
        return token;
     }
}
