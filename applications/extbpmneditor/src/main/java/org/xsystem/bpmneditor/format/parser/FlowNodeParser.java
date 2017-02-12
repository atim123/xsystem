/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.bpmneditor.format.parser;

import java.util.List;
import java.util.Map;
import org.xsystem.bpmn2.model.activities.Activity;
import org.xsystem.bpmn2.model.activities.Rendering;
import org.xsystem.bpmn2.model.activities.ResourceAssignmentExpression;
import org.xsystem.bpmn2.model.activities.ResourceRole;
import org.xsystem.bpmn2.model.activities.UserTask;
import org.xsystem.bpmn2.model.common.Expression;
import org.xsystem.bpmn2.model.common.FlowNode;
import org.xsystem.bpmn2.model.common.FormalExpression;
import org.xsystem.bpmn2.model.data.Assignment;
import org.xsystem.bpmn2.model.data.DataAssociation;
import org.xsystem.bpmn2.model.data.DataInput;
import org.xsystem.bpmn2.model.data.DataInputAssociation;
import org.xsystem.bpmn2.model.data.DataOutput;
import org.xsystem.bpmn2.model.data.DataOutputAssociation;
import org.xsystem.bpmn2.model.data.InputOutputSpecification;
import org.xsystem.bpmn2.model.data.InputSet;
import org.xsystem.bpmn2.model.data.OutputSet;
import org.xsystem.bpmn2.model.events.BoundaryEvent;
import org.xsystem.bpmn2.model.events.CatchEvent;
import org.xsystem.bpmn2.model.events.ThrowEvent;
import org.xsystem.bpmn2.model.events.TimerEventDefinition;
import org.xsystem.bpmn2.model.process.Lane;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.activities.RenderingImpl;
import org.xsystem.bpmn2.modelimpl.activities.ResourceAssignmentExpressionImpl;
import org.xsystem.bpmn2.modelimpl.common.FormalExpressionImpl;
import org.xsystem.bpmn2.modelimpl.data.AssignmentImpl;
import org.xsystem.bpmn2.modelimpl.data.DataInputAssociationImpl;
import org.xsystem.bpmn2.modelimpl.data.DataInputImpl;
import org.xsystem.bpmn2.modelimpl.data.DataOutputAssociationImpl;
import org.xsystem.bpmn2.modelimpl.data.DataOutputImpl;
import org.xsystem.bpmn2.modelimpl.data.InputOutputSpecificationImpl;
import org.xsystem.bpmn2.modelimpl.data.InputSetImpl;
import org.xsystem.bpmn2.modelimpl.data.OutputSetImpl;
import org.xsystem.bpmn2.modelimpl.events.BoundaryEventImpl;
import org.xsystem.bpmn2.modelimpl.events.TimerEventDefinitionImpl;
import org.xsystem.bpmn2.modelimpl.humaninteraction.HumanPerformerImpl;
import org.xsystem.bpmn2.modelimpl.humaninteraction.PotentialOwnerImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;
import org.xsystem.bpmneditor.format.GoParser2;
import org.xsystem.utils.Auxilary;

/**
 *
 * @author tim
 */
public class FlowNodeParser extends AbstractParser {

    public FlowNodeParser(GoParser2 owner) {
        super(owner);
    }

    InputOutputSpecification getIoSpecification(Activity activity) {
        if (activity.getIoSpecification() == null) {
            activity.setIoSpecification(new InputOutputSpecificationImpl(owner.getDefinitions()));
        }
        return activity.getIoSpecification();
    }

    void makeUserTask(UserTask userTask, Map resource){
            DefinitionsImpl definitions = owner.getDefinitions();
            String formKey = (String) resource.get("formKey");
            Rendering rendering=null;
            if (!Auxilary.isEmptyOrNull(formKey)) {
                if (rendering==null){
                   rendering = new RenderingImpl(definitions);
                }
                rendering.setFormKey(formKey);
            }
            String formContext = (String) resource.get("formContext");
            if (!Auxilary.isEmptyOrNull(formContext)) {
                if (rendering==null){
                   rendering = new RenderingImpl(definitions);
                }
                rendering.setFormContext(formContext);
            }
            
            if (rendering!=null){
               userTask.setRendering(rendering); 
            }
            
            String user = (String) resource.get("user");
            if (!Auxilary.isEmptyOrNull(user)) {
                ResourceRole potentialOwner = new HumanPerformerImpl(definitions);
                ResourceAssignmentExpression resourceAssignmentExpression = new ResourceAssignmentExpressionImpl(definitions);
                FormalExpression formalExpression = new FormalExpressionImpl(definitions);
                formalExpression.setBody(user);
                resourceAssignmentExpression.setExpression(formalExpression);
                potentialOwner.setResourceAssignmentExpression(resourceAssignmentExpression);
                userTask.getResources().add(potentialOwner);
            }
            
            String role = (String) resource.get("role");
            if (!Auxilary.isEmptyOrNull(role)) {
                ResourceRole potentialOwner = new PotentialOwnerImpl(definitions);
                ResourceAssignmentExpression resourceAssignmentExpression = new ResourceAssignmentExpressionImpl(definitions);
                FormalExpression formalExpression = new FormalExpressionImpl(definitions);
                formalExpression.setBody(role);
                resourceAssignmentExpression.setExpression(formalExpression);
                potentialOwner.setResourceAssignmentExpression(resourceAssignmentExpression);
                userTask.getResources().add(potentialOwner);
            }
    }
    
    void makeActivity(Activity activity, Map resource) {
        DefinitionsImpl definitions = owner.getDefinitions();
        List<Map> input = (List) resource.get("input");
        List<Map> output=  (List) resource.get("output");
        
        if (!Auxilary.isEmptyOrNull(input)||!Auxilary.isEmptyOrNull(output)){
            String dataInputid = owner.genId("DataInput");
            String dataOutputid= owner.genId("DataOutput");
            InputOutputSpecification ioSpecification = getIoSpecification(activity);
            activity.setIoSpecification(ioSpecification);
            
            DataInput dataInput = new DataInputImpl(definitions);
            dataInput.setId(dataInputid);
            ioSpecification.getDataInputs().add(dataInput);
            
            DataOutput dataOutput= new DataOutputImpl(definitions);
            dataOutput.setId(dataOutputid);
            ioSpecification.getDataOutputs().add(dataOutput);
            
            InputSet inputSet = new InputSetImpl(definitions);
            ioSpecification.getInputSets().add(inputSet);
            Reference ref = definitions.createReference(dataInputid);
            inputSet.getDataInputRefs().add(ref);
            
            OutputSet outputSet = new OutputSetImpl(definitions);
            ioSpecification.getOutputSets().add(outputSet);
            ref = definitions.createReference(dataOutputid);
            outputSet.getDataOutputRefs().add(ref);
            
            if (!Auxilary.isEmptyOrNull(input)){
                DataInputAssociation inputAssociation = new DataInputAssociationImpl(definitions);
                makeAssociation(dataInputid, inputAssociation, input);
                activity.getDatainputAssociations().add(inputAssociation);
            }
            if (!Auxilary.isEmptyOrNull(output)){
                DataOutputAssociation outputAssociation = new DataOutputAssociationImpl(definitions);
                makeAssociation(dataOutputid,outputAssociation,output);
                activity.getDataOutputAssociations().add(outputAssociation);
            }
        }
        if (activity instanceof UserTask) {
            makeUserTask((UserTask) activity,resource);
       }
        
    }
    
    void makeBoundaryEvents(Activity activity,Map resource){
      List<Map> boundaryEvents=(List)  resource.get("boundaryEventArray");
      if (!Auxilary.isEmptyOrNull(boundaryEvents)){
          DefinitionsImpl definitions = owner.getDefinitions();
          org.xsystem.bpmn2.model.process.Process pocess=definitions.getProcess(activity);
         
          Reference ref= activity.getReference();
        
         boundaryEvents.forEach(action->{
              String type=(String)action.get("type");
              if (type.equals("BoundaryTimerEvent")){
                  String portId =(String)action.get("portId");
                  BoundaryEvent boundaryEvent=new BoundaryEventImpl(definitions);
                  boundaryEvent.setId(portId);
                  boundaryEvent.setAttachedToRef(ref);
                  
                  TimerEventDefinition eventDefinition=new TimerEventDefinitionImpl(definitions);
                  String timeExp=(String)action.get("time");
                  if (timeExp==null){
                      timeExp="";
                  }
                  FormalExpression expression=new FormalExpressionImpl(definitions);
                  expression.setBody(timeExp);
                  eventDefinition.setTimeDate(expression);
                  boundaryEvent.getEventDefinitions().add(eventDefinition);
                  pocess.getFlowElements().add(boundaryEvent);
              }
         
         });
      }
      
    }
    
    void makeAssociation(String dataid, DataAssociation association, List<Map> input) {
        DefinitionsImpl definitions = owner.getDefinitions();
        Reference ref = definitions.createReference(dataid);
        association.setTargetRef(ref);
        input.forEach(action -> {
            String from = (String) action.get("from");
            String to = (String) action.get("to");
            Assignment assignment = new AssignmentImpl(definitions);
            FormalExpression fromExpression = new FormalExpressionImpl(definitions);
            fromExpression.setBody(from);
            assignment.setFrom(fromExpression);

            FormalExpression toExpression = new FormalExpressionImpl(definitions);
            toExpression.setBody(to);
            assignment.setTo(toExpression);
            association.getAssignment().add(assignment);
        });
    }

    void makeCatchEvent(CatchEvent catchEvent, Map<String, Object> modelData) {
        DefinitionsImpl definitions = owner.getDefinitions();
        List<Map> output = (List) modelData.get("output");
        if (output != null) {
            DataOutput dataOutput = new DataOutputImpl(definitions);
            String dataOutputid = owner.genId("DataOutput");
            dataOutput.setId(dataOutputid);
            catchEvent.getDataOutputs().add(dataOutput);
            DataOutputAssociation association = new DataOutputAssociationImpl(definitions);
            makeAssociation(dataOutputid, association, output);
            catchEvent.getDataOutputAssociations().add(association);
        }
    }
    
    void makeThrowEvent(ThrowEvent flowNode,Map<String, Object> modelData){
        DefinitionsImpl definitions = owner.getDefinitions();
        List<Map> input = (List) modelData.get("input");
        
        if (input!=null){
           DataInput dataInput= new DataInputImpl(definitions);
           String dataInputid = owner.genId("DataInput");
           dataInput.setId(dataInputid);
           flowNode.getDataInputs().add(dataInput);
                   
            DataInputAssociation association = new DataInputAssociationImpl(definitions);
            makeAssociation(dataInputid, association, input);
            flowNode.getDataInputAssociations().add(association);
        }
    };

    public void parse(Map<String, Object> modelData) {
        //    DefinitionsImpl definitions = owner.getDefinitions();
        FlowNode flowNode = FlowNodeBuilder.buildFlowNode(owner.getDefinitions(), modelData);
        if (flowNode != null) {
            String id = (String) modelData.get("key");
            flowNode.setId(id);
            String name = (String) modelData.get("text");
            flowNode.setName(name);

            String group = (String) modelData.get("group");
            Lane lane = owner.getLane(group);
            if (lane != null) {
                Reference ref = flowNode.getReference();
                lane.getFlowNodeRefs().add(ref);
            }
            if (flowNode instanceof Activity) {
                makeActivity((Activity) flowNode, modelData);
            } else if (flowNode instanceof CatchEvent) {
                makeCatchEvent((CatchEvent) flowNode, modelData);
            } else if (flowNode instanceof ThrowEvent) {
                makeThrowEvent((ThrowEvent) flowNode, modelData);
            }

            org.xsystem.bpmn2.model.process.Process process = owner.getProcess(group);
            process.getFlowElements().add(flowNode);
            if (flowNode instanceof Activity){
               makeBoundaryEvents((Activity) flowNode,modelData);
            }
        }
    }
}
