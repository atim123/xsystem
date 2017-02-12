/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.bpmneditor.format.parser;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.xsystem.bpmn2.model.collaboration.Collaboration;
import org.xsystem.bpmn2.model.collaboration.Participant;
import org.xsystem.bpmn2.model.common.CorrelationProperty;
import org.xsystem.bpmn2.model.common.CorrelationPropertyRetrievalExpression;
import org.xsystem.bpmn2.model.common.FormalExpression;
import org.xsystem.bpmn2.model.common.Message;
import org.xsystem.bpmn2.model.service.Interface;
import org.xsystem.bpmn2.model.service.Operation;

import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.collaboration.ParticipantImpl;
import org.xsystem.bpmn2.modelimpl.common.CorrelationPropertyImpl;
import org.xsystem.bpmn2.modelimpl.common.CorrelationPropertyRetrievalExpressionImpl;
import org.xsystem.bpmn2.modelimpl.common.FormalExpressionImpl;
import org.xsystem.bpmn2.modelimpl.common.MessageImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;
import org.xsystem.bpmn2.modelimpl.service.InterfaceImpl;
import org.xsystem.bpmn2.modelimpl.service.OperationImpl;
import org.xsystem.bpmneditor.format.GoParser2;

/**
 * AbstractParser
 *
 * @author tim
 */
public class DefinitionsParser extends AbstractParser {

    public DefinitionsParser(GoParser2 owner) {
        super(owner);
    }

    void buildCorrelationProperty(String messageId, Map<String, String> correlationPropertyDef) {
        DefinitionsImpl definitions = owner.getDefinitions();

        Iterator<String> iter = correlationPropertyDef.keySet().iterator();
        while (iter.hasNext()) {
            String correlationPropertyId = iter.next();
            String messagePathBody = correlationPropertyDef.get(correlationPropertyId);
            CorrelationProperty correlationProperty = new CorrelationPropertyImpl(definitions);
            correlationProperty.setId(correlationPropertyId);

            definitions.getRootElements().add(correlationProperty);

            CorrelationPropertyRetrievalExpression correlationPropertyRetrievalExpression = new CorrelationPropertyRetrievalExpressionImpl(definitions);
            Reference ref = definitions.createReference(messageId);
            correlationPropertyRetrievalExpression.setMessageRef(ref);
            FormalExpression messagePath = new FormalExpressionImpl(definitions);
            messagePath.setBody(messagePathBody);
            correlationPropertyRetrievalExpression.setMessagePath(messagePath);
            correlationProperty.getCorrelationPropertyRetrievalExpressions().add(correlationPropertyRetrievalExpression);
        }
    }

    void buildMessages(Map<String, Object> messages) {
        DefinitionsImpl definitions = owner.getDefinitions();
        Iterator<String> iter = messages.keySet().iterator();
        while (iter.hasNext()) {
            String messageId = iter.next();
            Map messageDef = (Map) messages.get(messageId);
            Message message = new MessageImpl(definitions);
            message.setId(messageId);
            definitions.getRootElements().add(message);

            Map<String, String> correlationPropertyDef = (Map) messageDef.get("messagePath");
            if (correlationPropertyDef != null) {
                buildCorrelationProperty(messageId, correlationPropertyDef);
            }
        }
    }

    void buildParticipant(String procKey) {
        DefinitionsImpl definitions = owner.getDefinitions();
        Collaboration collaboration = owner.getCollaboration();
        Participant participant = new ParticipantImpl(definitions);
        String participantId = owner.genId("XXX_PARTICIPANT_" + procKey);
        participant.setId(participantId);
        Reference ref = definitions.createReference(procKey);
        participant.setProcessRef(ref);
        collaboration.getParticipants().add(participant);
    }

    void buildInterface(Map<String, Object> inter){
       String implementationRef= (String) inter.get("implementationRef");
       if (!"mbean".equals(implementationRef)){
           throw new Error("not suported interface implementation "+implementationRef);
       }
       
       List<Map> operations=(List)inter.get("operations");
       if (operations==null){
           return;
       }
       if (operations.isEmpty()){
           return;
       }
       DefinitionsImpl definitions = owner.getDefinitions();
       
       Interface interf=new InterfaceImpl(definitions);
       String id=(String)inter.get("id");
       String name=(String)inter.get("name");
       interf.setId(id);
       interf.setName(name);
       interf.setImplementationRef(implementationRef);
       definitions.getRootElements().add(interf);
       operations.forEach(op->{
            String opid=(String)op.get("id");
            String opName=(String)op.get("name");
            Operation operation=new OperationImpl(definitions);
            operation.setId(opid);
            operation.setName(opName);
            Reference<Message> inMessageRef=definitions.createReference(opName);
            operation.setInMessageRef(inMessageRef);
            interf.getOperations().add(operation);
            //OperationImpl
       });
       //"implementationRef": "mbean"
    }
    
    /*
    
    <operation name="timerStop" 
            id="addTicketOperation">
            <inMessageRef>timerStop</inMessageRef>
        </operation>
    
    {
                        "id": "K_4",
                        "name": "timerStart"
                    }, {
    
    "id": "K_2",
                "name": "org.xsystem.bpm2machineservice:type\u003dBPManager",
                "implementationRef": "mbean",
    */
    
    
    public void parse(Map<String, Object> modelData) {
        DefinitionsImpl definitions = owner.getDefinitions();
        String id = (String) modelData.get("key");
        definitions.setId(id);
        String name = (String) modelData.get("text");
        definitions.setName(name);

        Map messages = (Map) modelData.get("messages");
        if (messages != null) {
            buildMessages(messages);
        }
        List<String> participants = (List) modelData.get("participants");
        if (participants != null) {
            participants.forEach(action -> {
                buildParticipant(action);
            });
        }
        
        List<Map> interfacesList=(List) modelData.get("interfaces");
        if (interfacesList!=null){
            interfacesList.forEach(action -> {
                buildInterface(action);
            });
        }
    }
}
