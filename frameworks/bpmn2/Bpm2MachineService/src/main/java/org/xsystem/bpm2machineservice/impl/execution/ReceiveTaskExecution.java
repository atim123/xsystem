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
import org.xsystem.bpmn2.model.activities.ReceiveTask;
import org.xsystem.bpmn2.model.common.FlowNode;
import org.xsystem.bpmn2.model.system.Reference;

import org.xsystem.bpm2machineservice.impl.ExecutionService;
import org.xsystem.bpm2machineservice.impl.Token;

/**
 *
 * @author Andrey Timofeev
 */
public class ReceiveTaskExecution extends Execution{

    public ReceiveTaskExecution(ExecutionService owner) {
        super(owner);
    }

    @Override
    public void execute(Token token) {
        String tokenId=token.getId();
        
        owner.createBoundaryEvents(token);
        
        Reference<FlowNode> ref = owner.getFlowNode(token);
        ReceiveTask receiveTask = (ReceiveTask) ref.resolvedReference();
        String messageId=receiveTask.getMessageRef().resolvedReference().getId();
        owner.toWaitsToken(tokenId,messageId);

    }

    @Override
    public void complite(Token token, Map<String, Object> messageData) {
        Reference<FlowNode> ref = owner.getFlowNode(token);
        ReceiveTask receiveTask = (ReceiveTask) ref.resolvedReference();
        
        List associationList= receiveTask.getDataOutputAssociations();
       
        String processInstanceId = token.getProcess();
        Map processContext= owner.getProcessContext(processInstanceId,null);
        AssociationsResolver resolver= new AssociationsResolver(messageData,processContext,owner.getScriptingService());
        resolver.resolve(associationList);
        owner.setProcessContext(processInstanceId,processContext);//!!!
        
        List<Reference<FlowNode>> outgoing = receiveTask.getOutgoingNode();
        Reference<FlowNode> newref = outgoing.get(0);
        
        MessageReceiver receiver= new MessageReceiver(owner);
        Token retToken=receiver.complete(token);
        
        owner.takeToken(retToken, newref);
        
    }
    
}
