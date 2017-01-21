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
package org.xsystem.bpm2machineservice.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Document;
import org.xsystem.bpmn2.formats.Parser;
import org.xsystem.bpmn2.formats.xml.XMLParser3;
import org.xsystem.bpmn2.model.common.CorrelationProperty;
import org.xsystem.bpmn2.model.common.CorrelationPropertyBinding;
import org.xsystem.bpmn2.model.common.CorrelationPropertyRetrievalExpression;
import org.xsystem.bpmn2.model.common.FormalExpression;
import org.xsystem.bpmn2.model.events.EventDefinition;
import org.xsystem.bpmn2.model.events.MessageEventDefinition;
import org.xsystem.bpmn2.model.events.StartEvent;
import org.xsystem.bpmn2.model.infrastructure.Definitions;
import org.xsystem.bpmn2.model.system.Reference;

import org.xsystem.sql2.page.PAGE;
import org.xsystem.sql2.page.PageHelper;
import org.xsystem.system.sql.JDBCTransationManager2;
import org.xsystem.utils.Auxilary;
import org.xsystem.utils.XMLUtil;

/**
 *
 * @author Andrey Timofeev
 */
public class DeployService implements PageHelper {
    void uploadDefinitions(String id, String name, String content) {
        Map params = PAGE.params(
                "id", id,
                "name", name,
                "content", content
        );
        int ok = (Integer)dml("updatedefinitions",params);
        
        
        if (ok == 0) {
            dml("insertdefinitions", params);
        }
    }
    
    List<Map<String, Object>> getProcesesDefinition(String id) {
        return list("getprocesesdefinition", PAGE.params("definitionid", id));
    }
    
    void deleteProcessdefinition(String id) {
        dml("deleteprocessdefinition", PAGE.params("id", id));
    }
    
    void createProcessDefinition(String definitionid, String id, String name) {
        dml("createprocessdefinition", PAGE.params(
                "definitionid", definitionid,
                "name", name,
                "id", id));
    }
    
    void updateProcessDefinition(String id, String name) {
        dml("updateprocessdefinition", PAGE.params(
                "name", name,
                "id", id));
    }
    
    void deleteWaitingProcess(String id){
        dml("deletewaitingprocess",PAGE.params("id", id));
    }
    
    void setWaitingProcess(String idProc,String message,String activityid){
        dml("insertwaitingprocess",PAGE.params(
                "id", idProc,
                "message",message,
                "activity",activityid
        ));
    }
    
   void buildsubscription(org.xsystem.bpmn2.model.process.Process proc){
      String procId=  proc.getId();
      dml("deletesubscription",PAGE.params("id",procId));
      
      Map<String,List<Map<String,String>>> subscription=new LinkedHashMap();
      
      proc.getCorrelationSubscriptions().forEach(correlationSubscription->{
        // Reference<CorrelationKey> correlationKeyRef =  correlationSubscription.getCorrelationKeyRef();
         List<CorrelationPropertyBinding> lstCorrelationPropertyBinding= correlationSubscription.getCorrelationPropertyBinding();
         
         lstCorrelationPropertyBinding.forEach(correlationPropertyBinding->{
            FormalExpression fromExpression=correlationPropertyBinding.getDataPath();
            Reference<CorrelationProperty> correlationPropertyRef= correlationPropertyBinding.getCorrelationPropertyRef();
            CorrelationProperty correlationProperty=correlationPropertyRef.resolvedReference();
            
            List<CorrelationPropertyRetrievalExpression> lstCorrelationPropertyRetrievalExpression= correlationProperty.getCorrelationPropertyRetrievalExpressions();
            lstCorrelationPropertyRetrievalExpression.forEach(correlationPropertyRetrievalExpression->{
               String message=correlationPropertyRetrievalExpression.getMessageRef().resolvedReference().getId();
               FormalExpression toExpression= correlationPropertyRetrievalExpression.getMessagePath();
               
               List<Map<String,String>> subscriptionLst= subscription.get(message);
               if (subscriptionLst==null){
                   subscriptionLst=new ArrayList();
                   subscription.put(message, subscriptionLst);
               }
               Map<String,String> row=new LinkedHashMap();
               row.put("to", toExpression.getBody());
               row.put("from",fromExpression.getBody());
               subscriptionLst.add(row);
            });
         });
      });
      
      Iterator<String> iterator= subscription.keySet().iterator();
      while (iterator.hasNext()){
         String message=iterator.next();
         Object value=subscription.get(message);
         dml("createsubscription",PAGE.params(
                 "message",message,
                 "process",procId,
                 "value",value
         ));
      }
    }
   
   
    // List<Reference<CorrelationProperty>>
    public String deployDefinitions(InputStream is) {
        byte[] content = Auxilary.readStreamE(is);
        Parser parser = new XMLParser3();
        Definitions def = parser.parse(new ByteArrayInputStream(content));
        String id = def.getId();
        String name = def.getName();
        uploadDefinitions(id, name, Auxilary.toUTF8String(content));
        
        List<org.xsystem.bpmn2.model.process.Process> procDefList= def.getProceses();
        List<Map<String, Object>> serverList = getProcesesDefinition(id);
        
        Set serverSet = new HashSet();
        Set defSet = new HashSet();
        
        serverList.stream().forEach(row -> {
            String tmpid = (String) row.get("id");
            serverSet.add(tmpid);
            deleteWaitingProcess(tmpid);
        });
        
        procDefList.stream().forEach(row -> {
            String tmpid = row.getId();
            defSet.add(tmpid);
        });
        
        // delete
        serverList.stream().
                filter(row -> !defSet.contains(row.get("id")))
                .forEach(row -> {
                    deleteProcessdefinition((String) row.get("id"));
                });
        // insert
        procDefList.stream().
                filter(row -> !serverSet.contains(row.getId()))
                .forEach(row -> {
                    String procId = row.getId();
                    String procName = row.getName();
                    createProcessDefinition(id, procId, procName);
                });
        // update
        procDefList.stream().
                filter(row -> serverSet.contains(row.getId()) & defSet.contains(row.getId()))
                .forEach(row -> {
                    String procId = row.getId();
                    String procName = row.getName();
                    updateProcessDefinition(procId, procName);
                });
        // SetWaiting
        
        procDefList.stream()
             .forEach(row -> {
                    String procId = row.getId();
                    List<StartEvent> eventList=row.getFlowElements(StartEvent.class);
                    eventList.stream()
                          .forEach(action->{
                             List<EventDefinition> eventDef=action.getEventDefinitions();
                             String activityid=action.getId();
                             eventDef.stream()
                                    .filter(predicate-> predicate instanceof MessageEventDefinition)
                                    .map(mapper->(MessageEventDefinition)mapper)
                                    .forEach(messageEventDefinition->{
                                       String messageId=  messageEventDefinition.getMessageRef().resolvedReference().getId();
                                       setWaitingProcess(procId,messageId,activityid);
                                    });
                          });
                });
        procDefList.forEach(action->{
          buildsubscription(action);
        });
   
        return id;  
    }
    
    public void unDeployDefinitions(String key){
        dml("deletedefinitions",PAGE.params("id", key ));
    }
    
    @Override
    public Connection getConnection() {
        return JDBCTransationManager2.getConnection();
    }

    @Override
    public Document getDocument() {
        ClassLoader classLoader=this.getClass().getClassLoader();
        return XMLUtil.getDocumentFromResourcesE(MachineConfig.deployxml,classLoader);
    }
}

