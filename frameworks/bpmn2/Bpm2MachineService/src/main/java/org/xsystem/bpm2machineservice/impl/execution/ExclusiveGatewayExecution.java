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
import java.util.stream.Collectors;
import org.xsystem.bpm2machineservice.impl.ExecutionService;
import org.xsystem.bpm2machineservice.impl.Token;
import org.xsystem.bpmn2.model.common.FlowNode;
import org.xsystem.bpmn2.model.common.FormalExpression;
import org.xsystem.bpmn2.model.gateways.ExclusiveGateway;
import org.xsystem.bpmn2.model.system.Reference;

/**
 *
 * @author Andrey Timofeev
 */
public class ExclusiveGatewayExecution extends Execution{

    public ExclusiveGatewayExecution(ExecutionService owner) {
        super(owner);
    }

    @Override
    public void execute(Token token) {
        Reference<FlowNode> ref = owner.getFlowNode(token);
        ExclusiveGateway gateway = (ExclusiveGateway) ref.resolvedReference();

        String processInstanceId = token.getProcess();
        Map<String, Object> evalCtx = owner.getProcessContext(processInstanceId,null);
                

        List<Reference<FlowNode>> outgoing = gateway.getOutgoing().stream()
                .map(refSequenceFlow -> refSequenceFlow.resolvedReference())
                .filter(sequenceFlow -> sequenceFlow.getConditionExpression() != null)
                .filter(sequenceFlow -> {
                    FormalExpression expression = (FormalExpression) sequenceFlow.getConditionExpression();
                    // TO DO ANY SCRINTING
                    String script = expression.getBody();
                    boolean ret =owner.getScriptingService().mvelEval(script, evalCtx, Boolean.class);

                    return ret;
                })
                .map(sequenceFlow -> sequenceFlow.getTargetRef())
                .collect(Collectors.toList());
        // find defualt transition
        if (outgoing.isEmpty()) {
            outgoing = gateway.getOutgoing().stream()
                    .map(refSequenceFlow -> refSequenceFlow.resolvedReference())
                    .filter(sequenceFlow -> sequenceFlow.getConditionExpression() == null)
                    .map(sequenceFlow -> sequenceFlow.getTargetRef())
                    .collect(Collectors.toList());
        }
        if (outgoing.size() != 1) {
            throw new RuntimeException("Bad conditions ExclusiveGateway " + gateway.getId());
        }
        Reference<FlowNode> newref = outgoing.get(0);
        owner.takeToken(token, newref);
    }

    @Override
    public void complite(Token token, Map<String, Object> contex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
