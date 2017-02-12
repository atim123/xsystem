/* 
 * Copyright (C) 2017 Andrey Timofeev
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.xsystem.bpmneditor.format.parser;

import java.util.Map;
import org.xsystem.bpmn2.model.collaboration.Collaboration;
import org.xsystem.bpmn2.model.collaboration.MessageFlow;
import org.xsystem.bpmn2.model.common.FormalExpression;
import org.xsystem.bpmn2.model.common.SequenceFlow;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.collaboration.MessageFlowImpl;
import org.xsystem.bpmn2.modelimpl.common.FormalExpressionImpl;
import org.xsystem.bpmn2.modelimpl.common.SequenceFlowImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;
import org.xsystem.bpmneditor.format.GoParser2;
import org.xsystem.utils.Auxilary;

/**
 *
 * @author Andrey Timofeev
 */
public class FlowElementParser extends AbstractParser {

    public FlowElementParser(GoParser2 owner) {
        super(owner);
    }

    org.xsystem.bpmn2.model.process.Process getProcess(String nodeId) {
        DefinitionsImpl definitions = owner.getDefinitions();

        org.xsystem.bpmn2.model.process.Process ret = definitions.getRootElements().stream()
                .filter(rootElements -> rootElements instanceof org.xsystem.bpmn2.model.process.Process)
                .map(rootElements -> (org.xsystem.bpmn2.model.process.Process) rootElements)
                .filter(process
                        -> process.getFlowElements().stream()
                        .filter(flowElements -> nodeId.equals(flowElements.getId()))
                        .findFirst().isPresent()
                )
                .findFirst().get();
        return ret;

    }

    void makeMessageFlow(Map<String, Object> modelData) {
        String from = (String) modelData.get("from");
        String to = (String) modelData.get("to");
        String message = (String) modelData.get("message");
        String id = modelData.get("key").toString();
        String name = (String) modelData.get("text");
        DefinitionsImpl definitions = owner.getDefinitions();
        MessageFlow messageFlow = new MessageFlowImpl(definitions);

        messageFlow.setId(id);
        messageFlow.setName(name);
        
        if (!Auxilary.isEmptyOrNull(from)) {
            Reference refFrom = definitions.createReference(from);
            messageFlow.setSourceRef(refFrom);
        }

        if (!Auxilary.isEmptyOrNull(to)) {
            Reference refTo = definitions.createReference(to);
            messageFlow.setTargetRef(refTo);
        }

        if (!Auxilary.isEmptyOrNull(message)) {
            Reference refMessge = definitions.createReference(message);
            messageFlow.setMessageRef(refMessge);
        }
        
        Collaboration collaboration=owner.getCollaboration();
        collaboration.getMessageFlow().add(messageFlow);
    }

    ;
    
    void makeSequenceFlow(Map<String, Object> modelData) {
        String from = (String) modelData.get("from");
        String to = (String) modelData.get("to");
        String id = modelData.get("key").toString();
        String name = (String) modelData.get("text");
        
        String fromPort=(String) modelData.get("fromPort");
        if (!Auxilary.isEmptyOrNull(fromPort)){
            from=fromPort;
        }
        
        org.xsystem.bpmn2.model.process.Process proc = getProcess(from);
        if (proc != getProcess(to)) {
            throw new Error("Bad Sequence id->" + id + " Diferent proc from-> " + from + " to->" + to);
        }
        DefinitionsImpl definitions = owner.getDefinitions();
        SequenceFlow sequenceFlow = new SequenceFlowImpl(definitions);

        sequenceFlow.setId(id);
        sequenceFlow.setName(name);

        Reference refFrom = definitions.createReference(from);
        Reference refTo = definitions.createReference(to);

        sequenceFlow.setSourceRef(refFrom);
        sequenceFlow.setTargetRef(refTo);

        String condition = (String) modelData.get("condition");
        if (!Auxilary.isEmptyOrNull(condition)) {
            FormalExpression conditionExpression = new FormalExpressionImpl(definitions);
            conditionExpression.setBody(condition);
            sequenceFlow.setConditionExpression(conditionExpression);
        }

        proc.getFlowElements().add(sequenceFlow);
    }

    @Override
    public void parse(Map<String, Object> modelData) {
        String type = (String) modelData.get("type");

        switch (type) {
            case "SequenceFlow": {
                makeSequenceFlow(modelData);
                return;
            }
            case "MessageFlow":
                makeMessageFlow(modelData);
                return;
        }
        System.out.println("Type not implemented -" + type);

    }

}
// "type": "SequenceFlow"
