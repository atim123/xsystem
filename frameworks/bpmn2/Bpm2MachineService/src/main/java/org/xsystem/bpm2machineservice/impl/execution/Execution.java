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
import org.xsystem.bpmn2.model.common.FlowNode;
import org.xsystem.bpmn2.model.system.Reference;


/**
 *
 * @author Andrey Timofeev
 */
public abstract class Execution {
    protected final ExecutionService owner;
    public Execution(ExecutionService owner){
        this.owner=owner;
    }

    protected void  moveToken(Token token){
        Reference<FlowNode> ref = owner.getFlowNode(token);
        FlowNode flowNode=ref.resolvedReference();
        List<Reference<FlowNode>> outgoing = flowNode.getOutgoingNode();
        Reference<FlowNode> newref = outgoing.get(0);
        owner.takeToken(token, newref);
    }
    
    public abstract void execute(Token token);
    
    public abstract void complite(Token token, Map<String, Object> contex);
}
