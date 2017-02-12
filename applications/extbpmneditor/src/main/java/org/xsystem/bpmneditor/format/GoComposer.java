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
package org.xsystem.bpmneditor.format;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;
import org.xsystem.bpmn2.model.activities.Activity;
import org.xsystem.bpmn2.model.activities.ReceiveTask;
import org.xsystem.bpmn2.model.activities.Rendering;
import org.xsystem.bpmn2.model.activities.ResourceAssignmentExpression;
import org.xsystem.bpmn2.model.activities.ResourceRole;
import org.xsystem.bpmn2.model.activities.ScriptTask;
import org.xsystem.bpmn2.model.activities.SendTask;
import org.xsystem.bpmn2.model.activities.ServiceTask;
import org.xsystem.bpmn2.model.activities.Task;
import org.xsystem.bpmn2.model.activities.UserTask;
import org.xsystem.bpmn2.model.bpmndi.BPMNDiagram;
import org.xsystem.bpmn2.model.bpmndi.BPMNEdge;
import org.xsystem.bpmn2.model.bpmndi.BPMNShape;
import org.xsystem.bpmn2.model.bpmndi.Bounds;
import org.xsystem.bpmn2.model.bpmndi.DiagramElement;
import org.xsystem.bpmn2.model.collaboration.Collaboration;
import org.xsystem.bpmn2.model.collaboration.MessageFlow;
import org.xsystem.bpmn2.model.collaboration.Participant;
import org.xsystem.bpmn2.model.common.CorrelationProperty;
import org.xsystem.bpmn2.model.common.CorrelationPropertyRetrievalExpression;
import org.xsystem.bpmn2.model.common.CorrelationSubscription;
import org.xsystem.bpmn2.model.common.Expression;
import org.xsystem.bpmn2.model.common.FlowNode;
import org.xsystem.bpmn2.model.common.FormalExpression;
import org.xsystem.bpmn2.model.common.Message;
import org.xsystem.bpmn2.model.common.SequenceFlow;
import org.xsystem.bpmn2.model.data.DataAssociation;
import org.xsystem.bpmn2.model.data.DataInputAssociation;
import org.xsystem.bpmn2.model.data.DataOutputAssociation;
import org.xsystem.bpmn2.model.events.BoundaryEvent;
import org.xsystem.bpmn2.model.events.CatchEvent;
import org.xsystem.bpmn2.model.events.EndEvent;
import org.xsystem.bpmn2.model.events.Event;
import org.xsystem.bpmn2.model.events.EventDefinition;
import org.xsystem.bpmn2.model.events.IntermediateCatchEvent;
import org.xsystem.bpmn2.model.events.IntermediateThrowEvent;
import org.xsystem.bpmn2.model.events.MessageEventDefinition;
import org.xsystem.bpmn2.model.events.StartEvent;
import org.xsystem.bpmn2.model.events.TerminateEventDefinition;
import org.xsystem.bpmn2.model.events.ThrowEvent;
import org.xsystem.bpmn2.model.events.TimerEventDefinition;
import org.xsystem.bpmn2.model.foundation.RootElement;
import org.xsystem.bpmn2.model.gateways.EventBasedGateway;
import org.xsystem.bpmn2.model.gateways.ExclusiveGateway;
import org.xsystem.bpmn2.model.gateways.Gateway;
import org.xsystem.bpmn2.model.gateways.ParallelGateway;
import org.xsystem.bpmn2.model.humaninteraction.HumanPerformer;
import org.xsystem.bpmn2.model.humaninteraction.PotentialOwner;
import org.xsystem.bpmn2.model.infrastructure.Definitions;
import org.xsystem.bpmn2.model.service.Operation;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;
import org.xsystem.utils.Auxilary;

/**
 * 
 *
 * @author Andrey Timofeev
 */
public class GoComposer {

    DefinitionsImpl definitions;
    List nodeDataArray = new ArrayList();
    List linkDataArray = new ArrayList();
    Map modelData = new LinkedHashMap();

    Map<String, DiagramElement> diagramElements = new HashMap();
    Map<String, List<BoundaryEvent>> boundaryEvents = new HashMap();

    void clear() {
        nodeDataArray.clear();
        linkDataArray.clear();
    }

    void prepareBoundaryEvent(BoundaryEvent boundaryEvent) {
        Reference<Activity> ref = boundaryEvent.getAttachedToRef();
        String key = ref.getLocalPart();
        List<BoundaryEvent> events = boundaryEvents.get(key);
        if (events == null) {
            events = new ArrayList();
            boundaryEvents.put(key, events);
        }
        events.add(boundaryEvent);
    }

    Reference<Activity> findOwner(String eventId) {

        Iterator<String> iter = boundaryEvents.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            List<BoundaryEvent> lst = boundaryEvents.get(key);
            BoundaryEvent boundaryEvent = lst.stream()
                    .filter(predicate -> predicate.getId().equals(eventId))
                    .findFirst().orElse(null);
            if (boundaryEvent != null) {
                Reference<Activity> ref = boundaryEvent.getAttachedToRef();
                return ref;
            }
        }
        return null;
    }

    ;
    
    
    Map build() {
        clear();
        String defId = definitions.getId();
        String name = definitions.getName();
        modelData.put("key", defId);
        modelData.put("text", name);
        modelData.put("participants", new ArrayList());
        //"participants": ["_1", "Pool"],
        return Auxilary.newMap(
                "class", "go.GraphLinksModel",
                "linkKeyProperty", "key",
                "modelData", modelData,
                "nodeDataArray", nodeDataArray,
                "linkDataArray", linkDataArray
        );

    }

    void makeDiagramElements() {
        if (!definitions.getBPMNDiagrams().isEmpty()) {
            BPMNDiagram diagram = definitions.getBPMNDiagrams().get(0);
            diagram.getBPMNPlane().getDiagramElements().stream()
                    .filter(element -> element instanceof BPMNEdge)
                    .map(element -> (BPMNEdge) element)
                    .forEach(edge -> {
                        String bpmnElement = edge.getBpmnElement();
                        if (!Auxilary.isEmptyOrNull(bpmnElement)) {
                            diagramElements.put(bpmnElement, edge);
                        }
                    });
            diagram.getBPMNPlane().getDiagramElements().stream()
                    .filter(element -> element instanceof BPMNShape)
                    .map(element -> (BPMNShape) element)
                    .forEach(shape -> {
                        String bpmnElement = shape.getBpmnElement();
                        if (!Auxilary.isEmptyOrNull(bpmnElement)) {
                            diagramElements.put(bpmnElement, shape);
                        }
                    });
        }
    }

    String getTypeFlowNode(FlowNode flowNode) {
        if (flowNode instanceof StartEvent) {
            return "StartEvent";
        } else if (flowNode instanceof EndEvent) {
            EndEvent endEvent = (EndEvent) flowNode;
            List<EventDefinition> lst = endEvent.getEventDefinitions();
            if (!lst.isEmpty()) {
                EventDefinition eventDefinition = lst.get(0);
                if (eventDefinition instanceof TerminateEventDefinition) {
                    return "TerminateEvent";
                }
            }

            return "EndEvent";
        } else if (flowNode instanceof UserTask) {
            return "UserTask";
        } else if (flowNode instanceof SendTask) {
            return "SendTask";
        } else if (flowNode instanceof ServiceTask){
            return "ServiceTask";
        } else if (flowNode instanceof ReceiveTask) {
            return "ReceiveTask";
        } else if (flowNode instanceof ScriptTask) {
            return "ScriptTask";
        } else if (flowNode instanceof ExclusiveGateway) {
            return "ExclusiveGateway";
        } else if (flowNode instanceof ParallelGateway) {
            return "ParallelGateway";
        } else if (flowNode instanceof EventBasedGateway) {
            return "EventGateway";
        } else if (flowNode instanceof IntermediateCatchEvent) {
            IntermediateCatchEvent catchEvent = (IntermediateCatchEvent) flowNode;
            List<EventDefinition> eventDefinitions = catchEvent.getEventDefinitions();
            if (eventDefinitions.size() == 1) {
                EventDefinition eventDefinition = eventDefinitions.get(0);
                if (eventDefinition instanceof MessageEventDefinition) {
                    return "CatchMessageEvent";
                } else if (eventDefinition instanceof TimerEventDefinition) {
                    return "TimerEvent";
                }
            }
            return "CatchMessageEvent";
        } else if (flowNode instanceof IntermediateThrowEvent) {
            IntermediateThrowEvent throwEvent = (IntermediateThrowEvent) flowNode;
            List<EventDefinition> eventDefinitions = throwEvent.getEventDefinitions();
            if (eventDefinitions.size() == 1) {
                EventDefinition eventDefinition = eventDefinitions.get(0);
                if (eventDefinition instanceof MessageEventDefinition) {
                    return "ThrowMessageEvent";
                }
            }
            return "ThrowMessageEvent";
        }
        throw new Error("unknoun type " + flowNode.getClass());

    }

    String getCategoryFlowNode(FlowNode flowNode) {
        if (flowNode instanceof Event) {
            return "event";
        } else if (flowNode instanceof Task) {
            return "task";
        } else if (flowNode instanceof Gateway) {
            return "gateway";
        }
        System.out.println("unknoun category " + flowNode.getClass());
        return null;
    }

    void makeShape(Map ret) {
        String key = (String) ret.get("key");
        BPMNShape share = (BPMNShape) diagramElements.get(key);
        if (share != null) {
            Bounds bounds = share.getBounds();
            double x = bounds.getX();
            double y = bounds.getY();
            double h = bounds.getHeight();
            double w = bounds.getWidth();
            String loc = Auxilary.numberToString(x) + " " + Auxilary.numberToString(y);
            ret.put("loc", loc);

            if (ret.get("category").equals("lane")) {
                String size = Auxilary.numberToString(w) + " " + Auxilary.numberToString(h);
                ret.put("size", size);
            }

        }
    }

    void makeSendTask(SendTask sendTask, Map ret) {
        Reference<Message> ref = sendTask.getMessageRef();
        if (ref != null) {
            String message = ref.getLocalPart();
            ret.put("message", message);
        }
    }

    void makeServiceTask(ServiceTask serviceTask,Map ret){
        Reference<Operation> ref=serviceTask.getOperationRef();
        if (ref != null) {
            String operation = ref.getLocalPart();
            ret.put("operation",operation);
        }
    }
    
    void makeReceiveTask(ReceiveTask flowNode, Map ret) {
        Reference<Message> ref = flowNode.getMessageRef();
        if (ref != null) {
            String message = ref.getLocalPart();
            ret.put("message", message);
        }
    }

    ;
    
    void makeScriptTask(ScriptTask flowNode, Map ret) {
        String script = flowNode.getScript();
        ret.put("script", script);
    }

    List<Map<String, String>> makeDataAssociation(List plst) {
        List<Map<String, String>> ret = new ArrayList();
        if (plst == null) {
            return ret;
        }

        List<DataAssociation> lst = plst;
        lst.stream()
                .flatMap(association -> association.getAssignment().stream())
                .forEach(assignment -> {
                    FormalExpression fromExp = (FormalExpression) assignment.getFrom();
                    FormalExpression toExp = (FormalExpression) assignment.getTo();
                    if (fromExp != null && toExp != null) {
                        String from = fromExp.getBody();
                        String to = toExp.getBody();
                        Map row = new HashMap();
                        row.put("from", from);
                        row.put("to", to);
                        ret.add(row);
                    }
                });

        return ret;
    }

    void makeActivity(Activity activity, Map ret) {

        List<DataOutputAssociation> oAssociation = activity.getDataOutputAssociations();
        List<DataInputAssociation> iAssociation = activity.getDatainputAssociations();

        List output = makeDataAssociation(oAssociation);
        List input = makeDataAssociation(iAssociation);
        ret.put("input", input);
        ret.put("output", output);
        String id = activity.getId();

        List<BoundaryEvent> events = boundaryEvents.get(id);
        if (events != null) {
            List<Map> boundaryEventArray = new ArrayList();
            ret.put("boundaryEventArray", boundaryEventArray);
            Integer i = 0;
            for (BoundaryEvent boundaryEvent : events) {
                List<EventDefinition> eventdefinitions = boundaryEvent.getEventDefinitions();
                if (eventdefinitions.size() == 1) {
                    EventDefinition definition = eventdefinitions.get(0);
                    if (definition instanceof TimerEventDefinition) {
                        Map row = new LinkedHashMap();
                        String portId = boundaryEvent.getId();
                        row.put("portId", portId);
                        row.put("type", "BoundaryTimerEvent");
                        row.put("eventDimension", new Integer(6));
                        row.put("color", "white");
                        row.put("alignmentIndex", i);
                        FormalExpression formalExpression = (FormalExpression) ((TimerEventDefinition) definition).getTimeDate();
                        if (formalExpression != null) {
                            String time = formalExpression.getBody();
                            row.put("time", time);
                        }
                        boundaryEventArray.add(row);
                        i = i + 1;
                    }
                }

            }
        }
    }

    /*
    "boundaryEventArray": [{
                    "portId": "K_3",
                    "type": "BoundaryTimerEvent",
                    "eventDimension": 6.0,
                    "color": "white",
                    "alignmentIndex": 0.0,
                    "time": "56"
                }
     */
    void makeUserTask(UserTask userTask, Map ret) {
        Rendering rendering = userTask.getRendering();
        if (rendering != null) {
            String formKey = rendering.getFormKey();
            Auxilary.putValue(ret, "formKey", formKey);
            String formContext= rendering.getFormContext();
            Auxilary.putValue(ret, "formContext", formContext);
        }
        List<ResourceRole> resourceRoles = userTask.getResources();

        resourceRoles.stream().forEach((resourceRole) -> {
            String ug = null;
            if (resourceRole instanceof PotentialOwner) {
                ug = "role";
            }
            if (resourceRole instanceof HumanPerformer) {
                if (ug == null) {
                    ug = "user";
                }
                ResourceAssignmentExpression resourceAssignmentExpression = resourceRole.getResourceAssignmentExpression();
                if (resourceAssignmentExpression != null) {
                    FormalExpression expression = (FormalExpression) resourceAssignmentExpression.getExpression();
                    if (expression != null) {
                        String body = expression.getBody();
                        Auxilary.putValue(ret, ug, body);
                    }
                }
            }
        });

    }

    void makeEventDefinition(CatchEvent catchEvent, Map ret) {
        List<EventDefinition> eventDefinitions = catchEvent.getEventDefinitions();
        if (!eventDefinitions.isEmpty()) {
            EventDefinition eventDefinition = eventDefinitions.get(0);
            if (eventDefinition instanceof MessageEventDefinition) {
                MessageEventDefinition messageEventDefinition = (MessageEventDefinition) eventDefinition;
                Reference<Message> ref = messageEventDefinition.getMessageRef();
                String message = ref.getLocalPart();
                ret.put("message", message);
            } else if (eventDefinition instanceof TimerEventDefinition) {
                TimerEventDefinition TimerEventDefinition = (TimerEventDefinition) eventDefinition;
                FormalExpression expression = (FormalExpression) TimerEventDefinition.getTimeDate();
                String exp = expression.getBody();
                ret.put("time", exp);
            }
        }
    }

    void makeEventDefinition(ThrowEvent throwEvent, Map ret) {
        List<EventDefinition> eventDefinitions = throwEvent.getEventDefinitions();
        if (!eventDefinitions.isEmpty()) {
            EventDefinition eventDefinition = eventDefinitions.get(0);
            if (eventDefinition instanceof MessageEventDefinition) {
                MessageEventDefinition messageEventDefinition = (MessageEventDefinition) eventDefinition;
                Reference<Message> ref = messageEventDefinition.getMessageRef();
                String message = ref.getLocalPart();
                ret.put("message", message);
            }
        }
    }

    // PotentialOwner
    void makeFlowNode(FlowNode flowNode, String group) {
        if (flowNode instanceof BoundaryEvent) {
            return;
        }
        String type = getTypeFlowNode(flowNode);
        if (type == null) {
            return;
        }
        String category = getCategoryFlowNode(flowNode);
        if (category == null) {
            return;
        }
        String key = flowNode.getId();
        String text = flowNode.getName();
        Map ret = new LinkedHashMap();
        Auxilary.putValue(ret, "key", key);
        Auxilary.putValue(ret, "text", text);
        Auxilary.putValue(ret, "type", type);
        Auxilary.putValue(ret, "category", category);
        Auxilary.putValue(ret, "group", group);

        if (flowNode instanceof CatchEvent) {
            CatchEvent catchEvent = (CatchEvent) flowNode;
            makeEventDefinition(catchEvent, ret);
            List<DataOutputAssociation> lst = catchEvent.getDataOutputAssociations();
            List outList = makeDataAssociation(lst);
            ret.put("output", outList);
        } else if (flowNode instanceof ThrowEvent) {
            ThrowEvent throwEvent = (ThrowEvent) flowNode;
            makeEventDefinition(throwEvent, ret);
            List<DataInputAssociation> lst = throwEvent.getDataInputAssociations();
            List inList = makeDataAssociation(lst);
            ret.put("input", inList);
        } else if (flowNode instanceof Activity) {
            makeActivity((Activity) flowNode, ret);
            if (flowNode instanceof ScriptTask) {
                makeScriptTask((ScriptTask) flowNode, ret);
            } else if (flowNode instanceof UserTask) {
                makeUserTask((UserTask) flowNode, ret);
            } else if (flowNode instanceof SendTask) {
                makeSendTask((SendTask) flowNode, ret);
            } else if (flowNode instanceof ReceiveTask) {
                makeReceiveTask((ReceiveTask) flowNode, ret);
            } else if (flowNode instanceof ServiceTask){
                makeServiceTask((ServiceTask)flowNode, ret);
            }
        }

        makeShape(ret);

        nodeDataArray.add(ret);

    }

    void makeEdge(Map ret) {
        String key = (String) ret.get("key");
        BPMNEdge edge = (BPMNEdge) diagramElements.get(key);
        if (edge != null) {
            List<Double> points = new ArrayList();
            edge.getWaypoints().forEach(point -> {
                double x = point.getX();
                double y = point.getY();
                points.add(x);
                points.add(y);
            });
            ret.put("points", points);
        }
    }

    void makeSequenceFlow(SequenceFlow flowElement) {
        String type = "SequenceFlow";
        Reference ref = flowElement.getSourceRef();
        String from = ref.getLocalPart();
        String fromPort = "";
        ref = flowElement.getTargetRef();
        String to = ref.getLocalPart();
        String key = flowElement.getId();
        Map ret = new HashMap();

        Reference<Activity> refOwner = findOwner(from);
        if (refOwner != null) {
            fromPort = from;
            from = refOwner.getLocalPart();
        }

        Auxilary.putValue(ret, "key", key);
        Auxilary.putValue(ret, "type", type);
        Auxilary.putValue(ret, "from", from);
        Auxilary.putValue(ret, "fromPort", fromPort);
        Auxilary.putValue(ret, "to", to);
        FormalExpression conditionExpression = (FormalExpression) flowElement.getConditionExpression();
        if (conditionExpression != null) {
            String condition = conditionExpression.getBody();
            Auxilary.putValue(ret, "condition", condition);
        }

        makeEdge(ret);
        linkDataArray.add(ret);
    }

    void makeMessageFlow(MessageFlow messageFlow) {
        String type = "MessageFlow";
        String category = "msg";
        String name = messageFlow.getName();
        Reference ref = messageFlow.getSourceRef();
        String from = ref.getLocalPart();
        ref = messageFlow.getTargetRef();
        String to = ref.getLocalPart();

        String key = messageFlow.getId();
        Map ret = new HashMap();
        Auxilary.putValue(ret, "key", key);
        Auxilary.putValue(ret, "type", type);
        Auxilary.putValue(ret, "category", category);
        Auxilary.putValue(ret, "from", from);
        Auxilary.putValue(ret, "to", to);
        ref = messageFlow.getMessageRef();
        if (ref != null) {
            String message = ref.getLocalPart();
            Auxilary.putValue(ret, "message", message);
        }

        Auxilary.putValue(ret, "text", name);

        makeEdge(ret);
        linkDataArray.add(ret);
    }

    void makeSubscriptions(org.xsystem.bpmn2.model.process.Process proc, Map ret) {
        Map<String, Object> retSubscriptions = new LinkedHashMap();
        proc.getCorrelationSubscriptions().stream()
                .flatMap(correlationSubscription -> correlationSubscription.getCorrelationPropertyBinding().stream())
                .forEach(correlationPropertyBinding -> {
                    Reference<CorrelationProperty> refCorrelationProperty = correlationPropertyBinding.getCorrelationPropertyRef();
                    CorrelationProperty correlationProperty = refCorrelationProperty.resolvedReference();
                    List<CorrelationPropertyRetrievalExpression> lst = correlationProperty.getCorrelationPropertyRetrievalExpressions();

                    String keyid = correlationProperty.getId();

                    if (!lst.isEmpty()) {
                        CorrelationPropertyRetrievalExpression correlationPropertyRetrievalExpression = lst.get(0);
                        Reference<Message> ref = correlationPropertyRetrievalExpression.getMessageRef();
                        String mesId = ref.getLocalPart();
                        List<Map<String, String>> msgSubscriptions = (List) retSubscriptions.get(mesId);
                        if (msgSubscriptions == null) {
                            msgSubscriptions = new ArrayList();
                            retSubscriptions.put(mesId, msgSubscriptions);
                        }
                        Map<String, String> row = new HashMap();
                        String exp = correlationPropertyBinding.getDataPath().getBody();
                        row.put(keyid, exp);
                        msgSubscriptions.add(row);
                    }
                });
        ret.put("subscriptions", retSubscriptions);
    }

    /*
    "subscriptions": {
                "K_1": [{
                        "K_2": "DDDD"
                    }]
     */
    void makeProcShaperes(Participant participant) {
        String participantId = participant.getId();
        Reference<org.xsystem.bpmn2.model.process.Process> ref = participant.getProcessRef();
        org.xsystem.bpmn2.model.process.Process proc = ref.resolvedReference();
        // BPMNShape share=(BPMNShape)diagramElements.get(participantId);
        List lstparticipants = (List) modelData.get("participants");

        lstparticipants.add(proc.getId());

        Map ret = Auxilary.newMap("key", participantId,
                "text", proc.getName(),
                "type", "Process",
                "category", "pool",
                "isGroup", "true");

        makeShape(ret);

        ret.put("key", proc.getId());
        nodeDataArray.add(ret);
        makeSubscriptions(proc, ret);

        proc.getLaneSets().stream()
                .flatMap(ls -> ls.getLanes().stream())
                .forEach(lane -> {
                    String lanekey = lane.getId();
                    Map laneRet = Auxilary.newMap("key", lanekey,
                            "group", proc.getId(),
                            "text", lane.getName(),
                            "type", "Lane",
                            "category", "lane",
                            "isGroup", "true");
                    makeShape(laneRet);
                    nodeDataArray.add(laneRet);
                    lane.getFlowNodeRefs().forEach(flowNodeRef -> {
                        FlowNode flowNode = flowNodeRef.resolvedReference();
                        makeFlowNode(flowNode, lanekey);
                    });
                });
    }

    void makeDefaultProcess() {
        Set<String> filter = new HashSet();
        if (!definitions.getCollaborations().isEmpty()) {
            Collaboration collaboration = definitions.getCollaborations().get(0);
            collaboration.getParticipants().forEach(participant -> {
                String procId = participant.getProcessRef().getLocalPart();
                filter.add(procId);
            });
        }
        org.xsystem.bpmn2.model.process.Process defaultProcess = definitions.getProceses().stream()
                .filter(proc -> !filter.contains(proc.getId()))
                .findFirst()
                .orElse(null);
        if (defaultProcess != null) {
            Map def = Auxilary.newMap("process", defaultProcess.getId(),
                    "name", defaultProcess.getName());

            modelData.put("default", def);
        };
        definitions.getProceses().stream()
                .filter(proc -> !filter.contains(proc.getId()))
                .flatMap(proc -> proc.getFlowElements().stream())
                .filter(element -> element instanceof FlowNode)
                .map(element -> (FlowNode) element)
                .forEach(flowNode -> {
                    makeFlowNode(flowNode, null);
                });
    }

    void makeMessages() {
        Map<String, Object> retMessages = new LinkedHashMap();
        definitions.getCorrelationPropertyes().forEach(correlationProperty -> {
            String keyId = correlationProperty.getId();
            List<CorrelationPropertyRetrievalExpression> lst = correlationProperty.getCorrelationPropertyRetrievalExpressions();
            if (!lst.isEmpty()) {
                CorrelationPropertyRetrievalExpression correlationPropertyRetrievalExpression = lst.get(0);
                Reference<Message> ref = correlationPropertyRetrievalExpression.getMessageRef();
                String mesId = ref.getLocalPart();
                Map<String, Object> retMessage = (Map) retMessages.get(mesId);
                if (retMessage == null) {
                    retMessage = new LinkedHashMap();
                    retMessages.put(mesId, retMessage);
                }
                Map<String, String> messagePaths = (Map) retMessages.get("messagePath");
                if (messagePaths == null) {
                    messagePaths = new LinkedHashMap();
                    retMessage.put("messagePath", messagePaths);
                }
                FormalExpression formalExpression = correlationPropertyRetrievalExpression.getMessagePath();
                String exp = formalExpression.getBody();
                messagePaths.put(keyId, exp);
            }
        });
        modelData.put("messages", retMessages);
    }

    
    /*  CorrelationPropertyRetrievalExpression  correlationProperty.getCorrelationPropertyRetrievalExpressions()
    "messages": {
            "K_1": {
                "messagePath": {
                    "K_2": "FFF"
                }
            }
        },
     */
    void makeInterfaces(){
       List<Map<String, Object>> retInterfaces = new ArrayList(); 
       definitions.getInterfaces().forEach(inter->{
           Map<String,Object> row=new LinkedHashMap();
           String id=inter.getId();
           String name=inter.getName();
           String implementationRef=inter.getImplementationRef();
           row.put("id",id);
           row.put("name",name);
           row.put("implementationRef",implementationRef);
           List retoperations=new ArrayList();
           row.put("operations",retoperations);
           inter.getOperations().forEach(op->{
                Map retOp=new HashMap();
                String opId=op.getId();
                Reference ref=op.getInMessageRef();
                String opName=ref.getLocalPart();
                retOp.put("id",opId);
                retOp.put("name",opName);
                retoperations.add(retOp);
           });
           retInterfaces.add(row);
       });
       modelData.put("interfaces",retInterfaces);
    }
    /*
    "interfaces": [{
                "id": "K_4",
                "name": "org.xsystem.bpm2machineservice:type\u003dBPManager",
                "implementationRef": "mbean",
                "operations": [{
                        "id": "K_5",
                        "name": "timerStart"
                    }]
            }],
    */
    
    public Map compose(Definitions resource) {
        definitions = (DefinitionsImpl) resource;
        Map ret = build();
        definitions.getProceses().stream()
                .flatMap(process -> process.getFlowElements().stream())
                .filter(element -> element instanceof BoundaryEvent)
                .map(element -> (BoundaryEvent) element)
                .forEach(action -> {
                    prepareBoundaryEvent(action);
                });

        makeDiagramElements();
        makeInterfaces();
        makeMessages();
        makeDefaultProcess();

        if (!definitions.getCollaborations().isEmpty()) {
            Collaboration collaboration = definitions.getCollaborations().get(0);
            collaboration.getParticipants().forEach(participant -> {
                makeProcShaperes(participant);
            });
        }

        definitions.getProceses().stream()
                .flatMap(process -> process.getFlowElements().stream())
                .filter(element -> element instanceof SequenceFlow)
                .map(element -> (SequenceFlow) element)
                .forEach(flowElement -> {
                    makeSequenceFlow(flowElement);
                });

        List<Collaboration> lstCollaboration = definitions.getCollaborations();
        if (!lstCollaboration.isEmpty()) {
            Collaboration collaboration = lstCollaboration.get(0);
            collaboration.getMessageFlow().forEach(messageFlow -> {
                makeMessageFlow(messageFlow);
            });
        }

        //org.xsystem.bpmn2.model.process.Process
        return ret;
    }
    /*
    void getProcess() {
        List<RootElement> lst = definitions.getRootElements();
        lst.stream()
                .filter(rootElements -> rootElements instanceof org.xsystem.bpmn2.model.process.Process) //    .(Collectors.toMap(rootElement -> rootElement.getId(),rootElement)    
                ;
    }

    void makeRootElements() {
        // org.xsystem.bpmn2.model.process.Process ret = def.getRootElements().stream()
        //           .filter(rootElements -> rootElements instanceof org.xsystem.bpmn2.model.process.Process)    
    }
     */
}
