/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.bpmneditor.format;



import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.xsystem.bpmn2.model.activities.Activity;
import org.xsystem.bpmn2.model.activities.SendTask;

import org.xsystem.bpmn2.model.bpmndi.BPMNDiagram;
import org.xsystem.bpmn2.model.bpmndi.BPMNPlane;
import org.xsystem.bpmn2.model.bpmndi.BPMNShape;
import org.xsystem.bpmn2.model.bpmndi.Bounds;
import org.xsystem.bpmn2.model.bpmndi.Point;
import org.xsystem.bpmn2.model.collaboration.Collaboration;
import org.xsystem.bpmn2.model.collaboration.Participant;
import org.xsystem.bpmn2.model.common.FlowNode;
import org.xsystem.bpmn2.model.common.FormalExpression;
import org.xsystem.bpmn2.model.common.Message;
import org.xsystem.bpmn2.model.data.Assignment;
import org.xsystem.bpmn2.model.data.DataInput;
import org.xsystem.bpmn2.model.data.DataInputAssociation;
import org.xsystem.bpmn2.model.data.InputOutputSpecification;
import org.xsystem.bpmn2.model.data.InputSet;
import org.xsystem.bpmn2.model.data.OutputSet;
import org.xsystem.bpmn2.model.events.IntermediateCatchEvent;
import org.xsystem.bpmn2.model.events.StartEvent;
import org.xsystem.bpmn2.model.infrastructure.Definitions;
import org.xsystem.bpmn2.model.process.Lane;
import org.xsystem.bpmn2.model.process.LaneSet;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.activities.SendTaskImpl;
import org.xsystem.bpmn2.modelimpl.collaboration.CollaborationImpl;
import org.xsystem.bpmn2.modelimpl.collaboration.ParticipantImpl;
import org.xsystem.bpmn2.modelimpl.common.FormalExpressionImpl;
import org.xsystem.bpmn2.modelimpl.common.MessageImpl;
import org.xsystem.bpmn2.modelimpl.data.AssignmentImpl;
import org.xsystem.bpmn2.modelimpl.data.DataInputAssociationImpl;
import org.xsystem.bpmn2.modelimpl.data.DataInputImpl;
import org.xsystem.bpmn2.modelimpl.data.InputOutputSpecificationImpl;
import org.xsystem.bpmn2.modelimpl.data.InputSetImpl;
import org.xsystem.bpmn2.modelimpl.data.OutputSetImpl;
import org.xsystem.bpmn2.modelimpl.events.IntermediateCatchEventImpl;
import org.xsystem.bpmn2.modelimpl.events.StartEventImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;
import org.xsystem.bpmn2.modelimpl.process.LaneImpl;
import org.xsystem.bpmn2.modelimpl.process.LaneSetImpl;
import org.xsystem.bpmn2.modelimpl.process.ProcessImpl;
import org.xsystem.utils.Auxilary;

/**
 *
 * @author tim
 */
public class GoParser {

    DefinitionsImpl definitions;

    Map<String, Object> modelData;
    List<Map> nodeDataArray;
    List<Map> linkDataArray;

    BPMNPlane plane = null;

    Collaboration collaboration = null;

    Map<String, Lane> laneMap = new HashMap();
    Map<String, org.xsystem.bpmn2.model.process.Process> processMap = new HashMap();
    Map<String, String> laneProcessMap = new HashMap();

    Map<String, BPMNShape> processShareMap = new HashMap();
    // Тут ключ ID процесса Нужно для диаграммы
    Map<String, Participant> participantMap = new HashMap();

    String genId(String base) {
        String ret = base;
        int corId = 1;
        while (definitions.keyExist(ret)) {
            ret = base + corId;
            corId = corId + 1;
        }
        return ret;
    }

    static String getPrefixNs(String ns) {
        int idx = ns.indexOf(":");
        if (idx < 0) {
            return null;
        }
        String ret = ns.substring(0, idx);
        return ret;
    }

    static String getLocal(String ns) {
        int idx = ns.indexOf(":");
        if (idx < 0) {
            return ns;
        }
        String ret = ns.substring(idx + 1);
        return ret;
    }

    Reference createReference(String value) {
        String prfx = getPrefixNs(value);
        String local = getLocal(value);
        Reference ref;
        if (prfx == null) {
            ref = definitions.createReference(value);
        } else {
            String namespaceURI = definitions.getNamespaceURI(prfx);
            ref = definitions.createReference(namespaceURI, local, prfx);
        }
        return ref;
    }

    Collaboration getCollaboration() {
        if (collaboration == null) {
            collaboration = new CollaborationImpl(definitions);
            String id = genId("COLLABORATION");
            collaboration.setId(id);
            definitions.getRootElements().add(collaboration);
        }
        return collaboration;
    }

    BPMNPlane getDiagram() {
        if (plane == null) {
            BPMNDiagram diagram = new BPMNDiagram();
            String id = genId("DIAGRAM");
            diagram.setId(id);
            definitions.getBPMNDiagrams().add(diagram);
            plane = new BPMNPlane();
            diagram.setBPMNPlane(plane);
            if (participantMap.isEmpty()) {
                // Связать диаграму с первым процессом
            } else {
                String bpmnElement = collaboration.getId();
                plane.setBpmnElement(bpmnElement);
                // Связать диаграму с collaboration 
            }
        }
        return plane;
    }

    void makeDefinitions() {
        String id = (String) modelData.get("key");
        definitions.setId(id);
        String name = (String) modelData.get("text");
        definitions.setName(name);
    }

    void makeMessages() {
        Map messages = (Map) modelData.get("messages");
        if (messages != null) {
            Iterator<String> iter = messages.keySet().iterator();
            while (iter.hasNext()) {
                String messageId = iter.next();
                Message message = new MessageImpl(definitions);
                message.setId(messageId);
                definitions.getRootElements().add(message);
            }
        }
    }

    org.xsystem.bpmn2.model.process.Process getProcess(String procref) {
        org.xsystem.bpmn2.model.process.Process process = processMap.get(procref);
        if (process == null) {
            process = new ProcessImpl(definitions);
            process.setId(procref);
            definitions.getRootElements().add(process);
            processMap.put(procref, process);
        }
        return process;
    }

    void buildParticipant(String procref) {
        if (participantMap.containsKey(procref)) {
            return;
        }

        Collaboration collaboration = getCollaboration();
        //org.xsystem.bpmn2.model.process.Process process = 
        getProcess(procref);

        Participant participant = new ParticipantImpl(definitions);
        String id = genId("PARTICIPANT");
        participant.setId(id);
        Reference ref = createReference(procref);
        participant.setProcessRef(ref);
        collaboration.getParticipants().add(participant);
        participantMap.put(procref, participant);
    }

    void makeParticipantMap() {
        List<String> participants = (List) modelData.get("participants");
        if (participants != null) {
            participants.stream().forEach(action -> {
                buildParticipant(action);
            });
        }
    }

    Point makePoint(String loc) {
        String[] twoWords = loc.split(" ", 2);
        Point point = new Point();
        double x = Double.valueOf(twoWords[0]);
        double y = Double.valueOf(twoWords[1]);
        point.setX(x);
        point.setY(y);
        return point;
    }

    BPMNShape makeBPMNShape(Map resource) {
        String type = (String) resource.get("type");
        BPMNShape shape = new BPMNShape();
        String bpmnElement = (String) resource.get("key");
        String id = genId("DIAGRAM_ELEMENT");
        shape.setId(id);
        shape.setBpmnElement(bpmnElement);
        double h = 0;
        double w = 0;
        if ("Process".equals(type) || "Lane".equals(type)) {
            shape.setIsExpanded(true);
            shape.setIsHorizontal(true);
        }
        String loc = (String) resource.get("loc");
        Point lPoint = makePoint(loc);

        String size = (String) resource.get("size");
        if (size != null) {
            Point sPoint = makePoint(size);
            h = sPoint.getX();
            w = sPoint.getY();
        } else {
            String category = (String) resource.get("category");//: "event",
            switch (category) {
                case "event": {
                    h = w = 42;
                    break;
                }
                case "task": {
                    w = 120;
                    h = 80;
                    break;
                }
            }
        };

        Bounds bounds = new Bounds();
        bounds.setX(lPoint.getX());
        bounds.setY(lPoint.getY());
        bounds.setHeight(h);
        bounds.setWidth(w);
        shape.setBounds(bounds);
        return shape;
    }

    /*
    <bpmndi:BPMNShape bpmnElement="_2" id="Yaoqiang-_2" isExpanded="true" isHorizontal="true">
        <dc:Bounds height="200.0" width="1078.0" x="50.0" y="64.63636363636363"/>
     */
    void makeProcess(Map resource) {
        String id = (String) resource.get("key");
        org.xsystem.bpmn2.model.process.Process process = getProcess(id);
        String name = (String) resource.get("name");
        process.setName(name);
        BPMNShape shape = makeBPMNShape(resource);

        processShareMap.put(id, shape);
    }

    void makeLane(Map resource) {
        String id = (String) resource.get("key");
        String name = (String) resource.get("text");
        String group = (String) resource.get("group");
        org.xsystem.bpmn2.model.process.Process process = getProcess(group);
        List<LaneSet> laneSets = process.getLaneSets();
        LaneSet laneSet;
        if (laneSets.isEmpty()) {
            laneSet = new LaneSetImpl(definitions);
            // laneSet.setProcess(process);
            laneSets.add(laneSet);
        } else {
            laneSet = laneSets.get(0);
        }
        Lane lane = new LaneImpl(definitions);
        lane.setId(id);
        lane.setName(name);
        laneSet.getLanes().add(lane);
        laneMap.put(id, lane);
        laneProcessMap.put(id, process.getId());
        BPMNShape lineShape = makeBPMNShape(resource);

        Bounds boundsLine = lineShape.getBounds();
        Bounds boundsProcess = processShareMap.get(group).getBounds();

        double h = boundsProcess.getHeight();
        double w = boundsProcess.getWidth();
        if (boundsLine.getHeight() > h) {
            boundsProcess.setHeight(boundsLine.getHeight());
        }
        if (boundsLine.getWidth() > w) {
            boundsProcess.setHeight(boundsLine.getWidth());
        }

    }

    ;
    
    /*
    <laneSet>
            <lane id="_3" name="Lane">
                <flowNodeRef>_5</flowNodeRef>
                <flowNodeRef>_4</flowNodeRef>
                <flowNodeRef>_7</flowNodeRef>
            </lane>
        </laneSet>
    */
    org.xsystem.bpmn2.model.process.Process getProcess(Map resource) {
        String group = (String) resource.get("key");
        String id = laneProcessMap.get(group);
        org.xsystem.bpmn2.model.process.Process ret = getProcess(id);
        return ret;
    }

    void makeFlowNode(FlowNode flowNode, Map resource) {
        String id = (String) resource.get("key");
        String name = (String) resource.get("text");
        String group = (String) resource.get("group");
        org.xsystem.bpmn2.model.process.Process process = getProcess(resource);
        //  StartEvent startEvent = new StartEventImpl(definitions);
        flowNode.setId(id);
        flowNode.setName(name);
        process.getFlowElements().add(flowNode);
        Lane lane = laneMap.get(group);
        if (lane != null) {
            Reference ref = createReference(id);
            lane.getFlowNodeRefs().add(ref);
        }
        makeBPMNShape(resource);
    }

    void makeStartEvent(Map resource) {
        makeFlowNode(new StartEventImpl(definitions), resource);
    }

    ;
    
    void makeActivity(Activity activity, Map resource) {
        makeFlowNode(activity,resource);
        List<Map> input = (List) resource.get("input");
        InputOutputSpecification ioSpecification = null;
        if (input != null) {
            ioSpecification = new InputOutputSpecificationImpl(definitions);
        }
        activity.setIoSpecification(ioSpecification);
        String dataInputid=genId("DataInput");
        if (input != null) {
            DataInput dataInput=new DataInputImpl(definitions);
            dataInput.setId(dataInputid);
            ioSpecification.getDataInputs().add(dataInput);
            InputSet inputSet=new InputSetImpl(definitions);
            ioSpecification.getInputSets().add(inputSet);
                    
            Reference ref= createReference(dataInputid);
            inputSet.getDataInputRefs().add(ref);
            OutputSet outputSet=new OutputSetImpl(definitions);
            ioSpecification.getOutputSets().add(outputSet);
        }
        if (input!=null){
           DataInputAssociation inputAssociation=new DataInputAssociationImpl(definitions);
           Reference ref= createReference(dataInputid);
           inputAssociation.setTargetRef(ref);
           input.forEach(action->{
              String from=(String)action.get("from");
              String to= (String)action.get("to");       
              Assignment assignment=new AssignmentImpl(definitions);
              FormalExpression fromExpression= new FormalExpressionImpl(definitions); 
              fromExpression.setBody(from);
              assignment.setFrom(fromExpression);
              
              FormalExpression toExpression= new FormalExpressionImpl(definitions); 
              toExpression.setBody(from);
              assignment.setFrom(toExpression);
           });
        }
        
    }

    /*
      <intermediateCatchEvent id="_6" name="Ожидание результата" parallelMultiple="false">
      <dataOutput id="XX_4_Output" isCollection="false" name="event"/>
      <dataOutputAssociation>
        <targetRef>XX_4_Output</targetRef>
        <assignment>
            <from>reportid</from>
            <to>reportid</to>
         </assignment>
         
      </dataOutputAssociation>
    */
    
    void makeSendTask(Map resource) {
        SendTask sendTask = new SendTaskImpl(definitions);
        makeActivity(sendTask,resource);
  
        String message = (String) resource.get("mesage");
        Reference ref = createReference(message);
        sendTask.setMessageRef(ref);
        
    }
    
    void makeCatchMessageEvent(Map resource){
        IntermediateCatchEvent catchEvent=new IntermediateCatchEventImpl(definitions);  
    }
    
    void makeNode(Map resource) {
        String tpy = (String) resource.get("type");
        switch (tpy) {
            case "Process": {
                makeProcess(resource);
                break;
            }
            case "Lane": {
                makeLane(resource);
                break;
            }
            case "StartEvent": {
                makeStartEvent(resource);
                break;
            }
            case "SendTask": {
                makeSendTask(resource);
                break;
            }
            case "CatchMessageEvent":{
                makeCatchMessageEvent(resource);
                break;
            }
            default: {
                System.out.println("Unknow type " + tpy);
            }
        }
    }

    public Definitions parse(Map<String, Object> resource) {
        modelData = (Map) resource.get("modelData");
        nodeDataArray = (List) resource.get("nodeDataArray");
        linkDataArray = (List) resource.get("linkDataArray");

        definitions = new DefinitionsImpl();
        makeDefinitions();
        makeMessages();
        makeParticipantMap();

        nodeDataArray.stream().forEach(action -> {
            makeNode(action);
        });

        return definitions;
    }
}
