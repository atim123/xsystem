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
import org.xsystem.bpmn2.model.activities.ReceiveTask;
import org.xsystem.bpmn2.model.activities.Rendering;
import org.xsystem.bpmn2.model.activities.ScriptTask;
import org.xsystem.bpmn2.model.activities.SendTask;
import org.xsystem.bpmn2.model.activities.ServiceTask;
import org.xsystem.bpmn2.model.activities.UserTask;
import org.xsystem.bpmn2.model.common.FlowNode;
import org.xsystem.bpmn2.model.common.FormalExpression;
import org.xsystem.bpmn2.model.events.EndEvent;
import org.xsystem.bpmn2.model.events.IntermediateCatchEvent;
import org.xsystem.bpmn2.model.events.IntermediateThrowEvent;
import org.xsystem.bpmn2.model.events.MessageEventDefinition;
import org.xsystem.bpmn2.model.events.StartEvent;
import org.xsystem.bpmn2.model.events.TerminateEventDefinition;
import org.xsystem.bpmn2.model.events.TimerEventDefinition;
import org.xsystem.bpmn2.model.gateways.EventBasedGateway;
import org.xsystem.bpmn2.model.gateways.ExclusiveGateway;
import org.xsystem.bpmn2.model.gateways.ParallelGateway;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.activities.ReceiveTaskImpl;
import org.xsystem.bpmn2.modelimpl.activities.RenderingImpl;
import org.xsystem.bpmn2.modelimpl.activities.ScriptTaskImpl;
import org.xsystem.bpmn2.modelimpl.activities.SendTaskImpl;
import org.xsystem.bpmn2.modelimpl.activities.ServiceTaskImpl;
import org.xsystem.bpmn2.modelimpl.activities.UserTaskImpl;
import org.xsystem.bpmn2.modelimpl.common.FormalExpressionImpl;
import org.xsystem.bpmn2.modelimpl.events.EndEventImpl;
import org.xsystem.bpmn2.modelimpl.events.IntermediateCatchEventImpl;
import org.xsystem.bpmn2.modelimpl.events.IntermediateThrowEventImpl;
import org.xsystem.bpmn2.modelimpl.events.MessageEventDefinitionImpl;
import org.xsystem.bpmn2.modelimpl.events.StartEventImpl;
import org.xsystem.bpmn2.modelimpl.events.TerminateEventDefinitionImpl;
import org.xsystem.bpmn2.modelimpl.events.TimerEventDefinitionImpl;
import org.xsystem.bpmn2.modelimpl.gateways.EventBasedGatewayImpl;
import org.xsystem.bpmn2.modelimpl.gateways.ExclusiveGatewayImpl;
import org.xsystem.bpmn2.modelimpl.gateways.ParallelGatewayImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;
import org.xsystem.utils.Auxilary;

/**
 *
 * @author Andrey Timofeev
 */
public class FlowNodeBuilder {

    public static FlowNode buildFlowNode(DefinitionsImpl definitions, Map<String, Object> modelData) {

        String type = (String) modelData.get("type");
        switch (type) {
            case "StartEvent": {
                StartEvent startEvent = new StartEventImpl(definitions);
                String message = (String) modelData.get("message");
                if (!Auxilary.isEmptyOrNull(message)) {
                    MessageEventDefinition messageEventDefinition = new MessageEventDefinitionImpl(definitions);
                    Reference messageRef = definitions.createReference(message);
                    messageEventDefinition.setMessageRef(messageRef);
                    startEvent.getEventDefinitions().add(messageEventDefinition);
                }
                return startEvent;
            }
            case "EndEvent": {
                EndEvent endEvent = new EndEventImpl(definitions);
                return endEvent;
            }
            case "TerminateEvent": {
                EndEvent endEvent = new EndEventImpl(definitions);
                TerminateEventDefinition terminateEventDefinition = new TerminateEventDefinitionImpl(definitions);
                endEvent.getEventDefinitions().add(terminateEventDefinition);
                return endEvent;
            }

            case "TimerEvent": {
                IntermediateCatchEvent intermediateCatchEvent = new IntermediateCatchEventImpl(definitions);
                String time = (String) modelData.get("time");
                if (!Auxilary.isEmptyOrNull(time)) {
                    TimerEventDefinition timerEventDefinition = new TimerEventDefinitionImpl(definitions);
                    FormalExpression formalExpression = new FormalExpressionImpl(definitions);
                    formalExpression.setBody(time);
                    timerEventDefinition.setTimeDate(formalExpression);
                    intermediateCatchEvent.getEventDefinitions().add(timerEventDefinition);
                }
                return intermediateCatchEvent;
            }
            case "CatchMessageEvent": {
                IntermediateCatchEvent intermediateCatchEvent = new IntermediateCatchEventImpl(definitions);

                String message = (String) modelData.get("message");
                if (!Auxilary.isEmptyOrNull(message)) {
                    MessageEventDefinition messageEventDefinition = new MessageEventDefinitionImpl(definitions);
                    intermediateCatchEvent.getEventDefinitions().add(messageEventDefinition);

                    Reference messageRef = definitions.createReference(message);
                    messageEventDefinition.setMessageRef(messageRef);
                }
                return intermediateCatchEvent;
            }

            case "ThrowMessageEvent": {
                IntermediateThrowEvent throwEvent = new IntermediateThrowEventImpl(definitions);
                String message = (String) modelData.get("message");
                if (!Auxilary.isEmptyOrNull(message)) {
                    MessageEventDefinition messageEventDefinition = new MessageEventDefinitionImpl(definitions);
                    throwEvent.getEventDefinitions().add(messageEventDefinition);

                    Reference messageRef = definitions.createReference(message);
                    messageEventDefinition.setMessageRef(messageRef);
                }
                return throwEvent;
            }
            case "SendTask": {
                SendTask sendTask = new SendTaskImpl(definitions);
                String message = (String) modelData.get("message");
                if (!Auxilary.isEmptyOrNull(message)) {
                    Reference messageRef = definitions.createReference(message);
                    sendTask.setMessageRef(messageRef);
                }

                return sendTask;
            }
            case "ReceiveTask": {
                ReceiveTask receiveTask = new ReceiveTaskImpl(definitions);
                String message = (String) modelData.get("message");
                if (!Auxilary.isEmptyOrNull(message)) {
                    Reference messageRef = definitions.createReference(message);
                    receiveTask.setMessageRef(messageRef);
                }

                return receiveTask;
            }
            case "ServiceTask":{
                ServiceTask serviceTask=new ServiceTaskImpl(definitions);
                String operation= (String) modelData.get("operation");
                if (!Auxilary.isEmptyOrNull(operation)) {
                    Reference operationRef = definitions.createReference(operation);
                    serviceTask.setOperationRef(operationRef);
                }
                return serviceTask;
            }
            case "UserTask": {
                UserTask userTask = new UserTaskImpl(definitions);
                String formKey = (String) modelData.get("formKey");
                if (formKey != null) {
                    Rendering rendering = new RenderingImpl(definitions);
                    rendering.setFormKey(formKey);
                    userTask.setRendering(rendering);
                }
                return userTask;
            }
            case "ScriptTask": {
                ScriptTask scriptTask = new ScriptTaskImpl(definitions);
                String script = (String) modelData.get("script");
                scriptTask.setScript(script);
                return scriptTask;
            }
            case "ExclusiveGateway": {
                ExclusiveGateway exclusiveGateway = new ExclusiveGatewayImpl(definitions);
                return exclusiveGateway;
            }
            case "ParallelGateway": {
                ParallelGateway parallelGateway = new ParallelGatewayImpl(definitions);
                return parallelGateway;
            }
            case "EventGateway": {
                EventBasedGateway eventBasedGateway = new EventBasedGatewayImpl(definitions);
                return eventBasedGateway;
            }

        }
        throw new Error("Type not implemented -" + type);
    }

}
