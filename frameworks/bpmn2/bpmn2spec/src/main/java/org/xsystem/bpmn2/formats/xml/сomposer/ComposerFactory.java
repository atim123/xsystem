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
package org.xsystem.bpmn2.formats.xml.сomposer;

import org.w3c.dom.Element;
import org.xsystem.bpmn2.model.activities.ReceiveTask;
import org.xsystem.bpmn2.model.activities.ScriptTask;
import org.xsystem.bpmn2.model.activities.SendTask;
import org.xsystem.bpmn2.model.activities.ServiceTask;
import org.xsystem.bpmn2.model.activities.UserTask;
import org.xsystem.bpmn2.model.bpmndi.BPMNDiagram;
import org.xsystem.bpmn2.model.bpmndi.BPMNEdge;
import org.xsystem.bpmn2.model.bpmndi.BPMNPlane;
import org.xsystem.bpmn2.model.bpmndi.BPMNShape;
import org.xsystem.bpmn2.model.bpmndi.Bounds;
import org.xsystem.bpmn2.model.bpmndi.Point;
import org.xsystem.bpmn2.model.collaboration.Collaboration;
import org.xsystem.bpmn2.model.common.CorrelationProperty;
import org.xsystem.bpmn2.model.common.Message;
import org.xsystem.bpmn2.model.common.SequenceFlow;
import org.xsystem.bpmn2.model.events.BoundaryEvent;
import org.xsystem.bpmn2.model.events.EndEvent;
import org.xsystem.bpmn2.model.events.IntermediateCatchEvent;
import org.xsystem.bpmn2.model.events.IntermediateThrowEvent;
import org.xsystem.bpmn2.model.events.MessageEventDefinition;
import org.xsystem.bpmn2.model.events.StartEvent;
import org.xsystem.bpmn2.model.events.TerminateEventDefinition;
import org.xsystem.bpmn2.model.events.TimerEventDefinition;
import org.xsystem.bpmn2.model.foundation.BaseElement;
import org.xsystem.bpmn2.model.gateways.EventBasedGateway;
import org.xsystem.bpmn2.model.gateways.ExclusiveGateway;
import org.xsystem.bpmn2.model.gateways.ParallelGateway;
import org.xsystem.bpmn2.model.humaninteraction.HumanPerformer;
import org.xsystem.bpmn2.model.humaninteraction.PotentialOwner;
import org.xsystem.bpmn2.model.service.Interface;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.utils.Auxilary;
import org.xsystem.utils.XMLUtil;

/**
 *
 * @author Andrey Timofeev
 */
public class ComposerFactory {

    static public ElementComposer getComposer(Object obj) {

        if (obj instanceof org.xsystem.bpmn2.model.process.Process) {
            return new ProcessComposer();
        } else if (obj instanceof MessageEventDefinition) {
            return new MessageEventDefinitionComposer();
        } else if (obj instanceof TimerEventDefinition) {
            return new TimerEventDefinitionComposer();
        } else if (obj instanceof TerminateEventDefinition) {
            return new TerminateEventDefinitionComposer();
        } else if (obj instanceof Message) {
            return new MessageComposer();
        } else if (obj instanceof SendTask) {
            return new SendTaskComposer();
        } else if (obj instanceof ReceiveTask) {
            return new ReceiveTaskComposer();
        } else if (obj instanceof IntermediateCatchEvent) {
            return new IntermediateCatchEventComposer();
        } else if(obj instanceof BoundaryEvent){
            return new BoundaryEventComposer();
        } else if (obj instanceof IntermediateThrowEvent) {
            return new IntermediateThrowEventComposer();
        } else if (obj instanceof Collaboration) {
            return new CollaborationComposer();
        } else if (obj instanceof CorrelationProperty) {
            return new CorrelationPropertyComposer();
        } else if (obj instanceof SequenceFlow) {
            return new SequenceFlowComposer();
        } else if (obj instanceof StartEvent) {
            return new StartEventComposer();
        } else if (obj instanceof EndEvent) {
            return new EndEventComposer();
        } else if (obj instanceof ExclusiveGateway) {
            return new ExclusiveGatewayComposer();
        } else if (obj instanceof EventBasedGateway) {
            return new EventBasedGatewayComposer();
        } else if (obj instanceof ParallelGateway) {
            return new ParallelGatewayComposer();
        } else if (obj instanceof UserTask) {
            return new UserTaskComposer();
        } else if (obj instanceof ScriptTask) {
            return new ScriptTaskComposer();
        } else if (obj instanceof PotentialOwner) {
            return new PotentialOwnerComposer();
        } else if (obj instanceof HumanPerformer) {
            return new HumanPerformerComposer();
        } else if (obj instanceof BPMNDiagram) {
            return new BPMNDiagramComposer();
        } else if (obj instanceof BPMNPlane) {
            return new BPMNPlaneComposer();
        } else if (obj instanceof BPMNShape) {
            return new BPMNShapeComposer();
        } else if (obj instanceof Bounds) {
            return new BoundsComposer();
        } else if (obj instanceof BPMNEdge) {
            return new BPMNEdgeComposer();
        } else if (obj instanceof Point) {
            return new PointComposer();
        } else if (obj instanceof Interface){
            return new InterfaceComposer();
        }  else if (obj  instanceof ServiceTask){
            return new ServiceTaskComposer();
        }
        throw new Error("Not suported composer from " + obj.getClass());
    }

    public static void makeDiagramElement(Element root, Object diagram) {
        ElementComposer composer = getComposer(diagram);
        if (composer != null) {
            composer.composer(root, diagram);
        } else {
            System.out.println("Not suported composer from " + diagram.getClass());
        }
    }

    public static Element setAttr(Element elem, String attr, String value) {
        if (!Auxilary.isEmptyOrNull(value)) {
            elem.setAttribute(attr, value);
        }
        return elem;
    }

    public static Element setAttr(Element elem, String attr, boolean value) {
        if (value) {
            elem.setAttribute(attr, "true");
        } else {
            elem.setAttribute(attr, "false");
        }
        return elem;
    }

    public static String getRefId(Reference ref) {
        if (ref != null) {
            String ret = ref.getQName().getLocalPart();
            return ret;
        }
        return null;
    }

    public static Element setAttr(Element elem, String attr, Reference ref) {
        String strref = getRefId(ref);
        setAttr(elem, attr, strref);
        return elem;
    }

    public static void setRefBody(Element root, String name, Reference ref) {
        if (ref != null) {
            String strref = getRefId(ref);
            XMLUtil.createNewTextElement(root, name, strref);
        }
    }

    public static Element makeBaseElement(Element root, String tag, BaseElement src) {
        Element ret = XMLUtil.createNewElement(root, tag);
        ComposerFactory.setAttr(ret, "id", src.getId());
        return ret;
    }
}
