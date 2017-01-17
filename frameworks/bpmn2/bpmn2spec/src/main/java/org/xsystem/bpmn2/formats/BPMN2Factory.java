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
package org.xsystem.bpmn2.formats;
import org.xsystem.bpmn2.model.collaboration.ParticipantMultiplicity;
import org.xsystem.bpmn2.model.foundation.BaseElement;
import org.xsystem.bpmn2.modelimpl.activities.ActivityImpl;
import org.xsystem.bpmn2.modelimpl.activities.CallActivityImpl;
import org.xsystem.bpmn2.modelimpl.activities.ReceiveTaskImpl;
import org.xsystem.bpmn2.modelimpl.activities.RenderingImpl;
import org.xsystem.bpmn2.modelimpl.activities.ResourceAssignmentExpressionImpl;
import org.xsystem.bpmn2.modelimpl.activities.ScriptTaskImpl;
import org.xsystem.bpmn2.modelimpl.activities.SendTaskImpl;
import org.xsystem.bpmn2.modelimpl.activities.ServiceTaskImpl;
import org.xsystem.bpmn2.modelimpl.activities.TaskImpl;
import org.xsystem.bpmn2.modelimpl.activities.UserTaskImpl;
import org.xsystem.bpmn2.modelimpl.collaboration.CollaborationImpl;
import org.xsystem.bpmn2.modelimpl.collaboration.MessageFlowImpl;
import org.xsystem.bpmn2.modelimpl.collaboration.ParticipantImpl;
import org.xsystem.bpmn2.modelimpl.collaboration.ParticipantMultiplicityImpl;
import org.xsystem.bpmn2.modelimpl.common.CallableElementImpl;
import org.xsystem.bpmn2.modelimpl.common.CorrelationKeyImpl;
import org.xsystem.bpmn2.modelimpl.common.CorrelationPropertyBindingImpl;
import org.xsystem.bpmn2.modelimpl.common.CorrelationPropertyImpl;
import org.xsystem.bpmn2.modelimpl.common.CorrelationPropertyRetrievalExpressionImpl;
import org.xsystem.bpmn2.modelimpl.common.CorrelationSubscriptionImpl;
import org.xsystem.bpmn2.modelimpl.common.ErrorIpml;
import org.xsystem.bpmn2.modelimpl.common.ExpressionImpl;
import org.xsystem.bpmn2.modelimpl.common.FlowElementImpl;
import org.xsystem.bpmn2.modelimpl.common.FlowElementsContainerImpl;
import org.xsystem.bpmn2.modelimpl.common.FlowNodeImpl;
import org.xsystem.bpmn2.modelimpl.common.FormalExpressionImpl;
import org.xsystem.bpmn2.modelimpl.common.ItemDefinitionImpl;
import org.xsystem.bpmn2.modelimpl.common.MessageImpl;
import org.xsystem.bpmn2.modelimpl.common.SequenceFlowImpl;
import org.xsystem.bpmn2.modelimpl.conversation.ConversationImpl;
import org.xsystem.bpmn2.modelimpl.data.AssignmentImpl;
import org.xsystem.bpmn2.modelimpl.data.DataAssociationImpl;
import org.xsystem.bpmn2.modelimpl.data.DataInputAssociationImpl;
import org.xsystem.bpmn2.modelimpl.data.DataInputImpl;
import org.xsystem.bpmn2.modelimpl.data.DataObjectImpl;
import org.xsystem.bpmn2.modelimpl.data.DataObjectReferenceImpl;
import org.xsystem.bpmn2.modelimpl.data.DataOutputAssociationImpl;
import org.xsystem.bpmn2.modelimpl.data.DataOutputImpl;
import org.xsystem.bpmn2.modelimpl.data.DataStateImpl;
import org.xsystem.bpmn2.modelimpl.data.DataStoreImpl;
import org.xsystem.bpmn2.modelimpl.data.DataStoreReferenceImpl;
import org.xsystem.bpmn2.modelimpl.data.InputOutputSpecificationImpl;
import org.xsystem.bpmn2.modelimpl.data.InputSetImpl;
import org.xsystem.bpmn2.modelimpl.data.ItemAwareElementImpl;
import org.xsystem.bpmn2.modelimpl.data.OutputSetImpl;
import org.xsystem.bpmn2.modelimpl.data.PropertyImpl;
import org.xsystem.bpmn2.modelimpl.events.BoundaryEventImpl;
import org.xsystem.bpmn2.modelimpl.events.CatchEventImpl;
import org.xsystem.bpmn2.modelimpl.events.EndEventImpl;
import org.xsystem.bpmn2.modelimpl.events.EventImpl;
import org.xsystem.bpmn2.modelimpl.events.IntermediateCatchEventImpl;
import org.xsystem.bpmn2.modelimpl.events.IntermediateThrowEventImpl;
import org.xsystem.bpmn2.modelimpl.events.MessageEventDefinitionImpl;
import org.xsystem.bpmn2.modelimpl.events.StartEventImpl;
import org.xsystem.bpmn2.modelimpl.events.ThrowEventImpl;
import org.xsystem.bpmn2.modelimpl.foundation.BaseElementImpl;
import org.xsystem.bpmn2.modelimpl.foundation.RootElementImpl;
import org.xsystem.bpmn2.modelimpl.gateways.EventBasedGatewayImpl;
import org.xsystem.bpmn2.modelimpl.gateways.GatewayImpl;
import org.xsystem.bpmn2.modelimpl.gateways.ParallelGatewayImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;
import org.xsystem.bpmn2.modelimpl.process.ProcessImpl;
import org.xsystem.bpmn2.modelimpl.service.InterfaceImpl;
import org.xsystem.bpmn2.modelimpl.service.OperationImpl;
import org.xsystem.bpmn2.modelimpl.gateways.ExclusiveGatewayImpl;
import org.xsystem.bpmn2.modelimpl.humaninteraction.HumanPerformerImpl;
import org.xsystem.bpmn2.modelimpl.humaninteraction.PotentialOwnerImpl;
import org.xsystem.bpmn2.modelimpl.process.LaneImpl;
import org.xsystem.bpmn2.modelimpl.process.LaneSetImpl;

/**
 *
 * @author Andrey Timofeev
 */
public class BPMN2Factory {
    public static boolean isRootElement(String tag){
        switch (tag){
            case "rootElement":
            case "callableElement":    
            case "process":
            case "error":
            case "itemDefinition":    
            case "message":
            case "dataStore":
            case "dataStoreReference":
            case "interface":
            case "correlationProperty":  
            case "collaboration":
                 return true;
        }        
       return false;                  
    }
    
    public static boolean isImport(String tag){
        switch (tag){
            case "import":
                return true;
        }
        return false;
    }
    
    public static BaseElement createBaseElement(String resurceType, DefinitionsImpl definitions) {
        switch (resurceType) {
            case "activity":
                return new ActivityImpl(definitions);
            case "receiveTask":
                return new ReceiveTaskImpl(definitions);
             case "sendTask":
                return new SendTaskImpl(definitions);    
            case "serviceTask":
                return new ServiceTaskImpl(definitions);
            case "task":
                return new TaskImpl(definitions);
            case "userTask":
                return new UserTaskImpl(definitions);
            case "callableElement":
                return new CallableElementImpl(definitions);
            case "error":
                return new ErrorIpml(definitions);
            case "expression":
                return new ExpressionImpl(definitions);
            case "flowElement":
                return new FlowElementImpl(definitions);
            case "flowElementsContainer":
                return new FlowElementsContainerImpl(definitions);
            case "flowNode":
                return new FlowNodeImpl(definitions);
            case "formalExpression":
                return new FormalExpressionImpl(definitions);
            case "itemDefinition":
                return new ItemDefinitionImpl(definitions);
            case "message":
                return new MessageImpl(definitions);
            case "sequenceFlow":
                return new SequenceFlowImpl(definitions);
            case "Assignment":
                return new AssignmentImpl(definitions);
            case "dataAssociation":
                return new DataAssociationImpl(definitions);
            case "dataInputAssociation":
                return new DataInputAssociationImpl(definitions);
            case "dataInput":
                return new DataInputImpl(definitions);
            case "dataObject":
                return new DataObjectImpl(definitions);
            case "dataObjectReference":
                return new DataObjectReferenceImpl(definitions);
            case "dataOutputAssociation":
                return new DataOutputAssociationImpl(definitions);
            case "dataOutput":
                return new DataOutputImpl(definitions);
            case "dataState":
                return new DataStateImpl(definitions);
            case "dataStore":
                return new DataStoreImpl(definitions);
            case "dataStoreReference":
                return new DataStoreReferenceImpl(definitions);
            case "inputOutputSpecification":
                return new InputOutputSpecificationImpl(definitions);
            case "inputSet":
                return new InputSetImpl(definitions);
            case "itemAwareElement":
                return new ItemAwareElementImpl(definitions);
            case "outputSet":
                return new OutputSetImpl(definitions);
            case "Property":
                return new PropertyImpl(definitions);
            case "catchEvent":
                return new CatchEventImpl(definitions);
            case "intermediateCatchEvent":    
                return new IntermediateCatchEventImpl(definitions);
            case "boundaryEvent":
                return new BoundaryEventImpl(definitions);
            case "intermediateThrowEvent":
                return new IntermediateThrowEventImpl(definitions);
            case "endEvent":
                return new EndEventImpl(definitions);
            case "event":
                return new EventImpl(definitions);
            case "messageEventDefinition":
                return new MessageEventDefinitionImpl(definitions);
            case "startEvent":
                return new StartEventImpl(definitions);
            case "throwEvent":
                return new ThrowEventImpl(definitions);
            case "baseElement":
                return new BaseElementImpl(definitions);
            case "rootElement":
                return new RootElementImpl(definitions);
            case "gateway":
                return new GatewayImpl(definitions);
            case "parallelGateway":
                return new ParallelGatewayImpl(definitions);
            case "eventBasedGateway":
                return new EventBasedGatewayImpl(definitions);
            case "exclusiveGateway":
                return new ExclusiveGatewayImpl(definitions);
            case "process":
                return new ProcessImpl(definitions);
            case "interface":
                return new InterfaceImpl(definitions);
            case "operation":
                return new OperationImpl(definitions);
            case "correlationProperty":
                return new CorrelationPropertyImpl(definitions);  
            case "correlationPropertyRetrievalExpression":
                return new CorrelationPropertyRetrievalExpressionImpl(definitions);
            case "collaboration":
                return new CollaborationImpl(definitions);
            case "participant":
                return new ParticipantImpl(definitions); 
            case "messageFlow":
                return new MessageFlowImpl(definitions); 
            case "conversation":
                return new ConversationImpl(definitions);
            case "correlationKey":
                return new CorrelationKeyImpl(definitions);
            case "correlationSubscription":
                return new CorrelationSubscriptionImpl(definitions);
            case "correlationPropertyBinding":
                return new CorrelationPropertyBindingImpl(definitions);
            case "laneSet":
                return new LaneSetImpl(definitions); 
            case "lane":
                return new LaneImpl(definitions);
            case "scriptTask":
                return new ScriptTaskImpl(definitions);
            case "callActivity":    
                return new CallActivityImpl(definitions);
            case "potentialOwner":
                return new PotentialOwnerImpl(definitions);
            case "humanPerformer":
                return new HumanPerformerImpl(definitions);
            case "resourceAssignmentExpression":
                return new ResourceAssignmentExpressionImpl(definitions); 
            case "rendering":
                return new RenderingImpl(definitions);
            case "participantMultiplicity":    
                return new ParticipantMultiplicityImpl(definitions);
//  case "dataPath":
          //      return new DataPathImp(definitions);

        }
        return null;
    }
}
