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

import java.io.InputStream;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.jxpath.JXPathContext;
//import org.apache.commons.jxpath.JXPathContext;
import org.w3c.dom.Document;
import org.xsystem.bpm2machine.BPManager;
import org.xsystem.bpm2machineservice.Bpm2MachineService;
import org.xsystem.bpm2machineservice.impl.execution.Execution;
import org.xsystem.bpm2machineservice.impl.execution.ExecutionBuilder;
import org.xsystem.bpmn2.model.activities.Rendering;
import org.xsystem.bpmn2.model.activities.UserTask;
import org.xsystem.bpmn2.model.common.FlowNode;
import org.xsystem.bpmn2.model.common.FormalExpression;
import org.xsystem.bpmn2.model.common.SequenceFlow;
import org.xsystem.bpmn2.model.events.BoundaryEvent;
import org.xsystem.bpmn2.model.events.EventDefinition;
import org.xsystem.bpmn2.model.events.TimerEventDefinition;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;

import org.xsystem.sql2.page.PAGE;
import org.xsystem.sql2.page.PageHelper;
import org.xsystem.system.sql.JDBCTransationManager2;
import org.xsystem.utils.Auxilary;
import org.xsystem.utils.XMLUtil;

/**
 *
 * @author Andrey Timofeev
 */
public class ExecutionService implements PageHelper {

    Bpm2MachineService owner;
    LinkedList<Token> queue = new LinkedList();

    ScriptingService scriptingService;

    public ExecutionService(Bpm2MachineService owner) {
        this.owner = owner;
        scriptingService = new ScriptingService(this);
    }

    public ScriptingService getScriptingService() {
        return scriptingService;
    }

    public org.xsystem.bpmn2.model.process.Process getProcess(String processId) {
        return owner.getProcess(processId);
    }

    //getDefinitions
    
    public Reference<FlowNode> getFlowNode(Token token) {
        String processId = token.getProcessDefinition();

        String nodeId = token.getActivity();
        org.xsystem.bpmn2.model.process.Process process = getProcess(processId);
        Reference<FlowNode> ref = ((DefinitionsImpl) process.getOwnerDefinitions()).createReference(nodeId);
        return ref;
    }

    //----------Token----------------------------------------------------------- 
    public void takeToken(Token token, Reference<FlowNode> newref) {

        token.setActivity(newref.getQName().getLocalPart());

        saveToken(token);
        queue.addLast(token);

    }

    public String saveToken(Token token) {
        String id;
        String parent_id = token.getParent();

        String activity = token.getActivity();

        String process = token.getProcess();

        Boolean isactive = token.isIsactive();
        Integer loopcounter = token.getLoopcounter();
        Map params = PAGE.params(
                "parent", parent_id,
                "isactive", isactive,
                "process", process,
                "activity", activity,
                "loopcounter", loopcounter
        );

        if (token.getId() == null) {

            Map<String, String> ret = (Map) dml("token/insert", params);
            id = ret.get("id");

        } else {
            id = token.getId();
            params.put("id", id);
            Map<String, String> ret = (Map) dml("token/update", params);
            id = ret.get("id");
        }
        return id;
    }

    public Token newToken(String procInstance, Reference<FlowNode> ref) {
        Token.Builder builder = new Token.Builder();
        FlowNode node = ref.resolvedReference();

        String processId = node.getProcess().getId();
        String nodeId = node.getId();

        //String processId = process.getId();
        Token token = builder.activity(nodeId)
                .process(procInstance)
                .isactive(true)
                .processDefinition(processId)
                .build();
        token.setId(saveToken(token));
        return token;
    }

    public Token newToken(Token parent, Reference<FlowNode> ref) {
        Token.Builder builder = new Token.Builder();
        FlowNode node = ref.resolvedReference();

        String nodeId = node.getId();
        String processInstanceId = parent.getProcess();

        String processDefinition = parent.getProcessDefinition();

        String parentId = parent.getId();

        Token token = builder.activity(nodeId)
                .process(processInstanceId)
                .parent(parentId)
                .processDefinition(processDefinition)
                .build();

        token.setId(saveToken(token));
        return token;
    }

    public void destroyToken(Token token) {
        String id = token.getId();
        dml("token/destroy", PAGE.params("id", id));
        queue.forEach(action -> {
            String testId = action.getId();
            if (testId.equals(id)) {
                queue.remove(action);
            }
        });
    }

    Token createToken(Map tuple) {
        if (tuple == null) {
            return null;
        }

        String id = (String) tuple.get("id");

        String parent = (String) tuple.get("parent");
        boolean isactive = (Boolean) tuple.get("isactive");
        String process = (String) tuple.get("process");
        String activity = (String) tuple.get("activity");
        Integer loopcounter = (Integer) tuple.get("loopcounter");
        String processdefinition = (String) tuple.get("processdefinition");

        Token ret = new Token.Builder()
                .id(id)
                .activity(activity)
                .parent(parent)
                .process(process)
                .isactive(isactive)
                .processDefinition(processdefinition)
                .loopcounter(loopcounter)
                .build();
        return ret;
    }

    public Token getToken(String id) {
        Map tuple = row("token/get", PAGE.params("id", id));
        return createToken(tuple);
    }

    public Token getTaskToken(String id) {
        Map tuple = row("token/task", PAGE.params("id", id));
        return createToken(tuple);
    }

    public List<Token> getActiveToken(String id) {
        List<Token> ret = stream("token/process", PAGE.params("id", id))
                .map(tuple -> createToken(tuple))
                .collect(Collectors.toList());
        return ret;
    }

    public Token getParentToken(Token child) {
        String parent = child.getParent();
        return getToken(parent);
    }

    public List<Token> getChildList(Token token) {
        String id = token.getId();
        List<Token> ret = stream("token/childList", PAGE.params("id", id))
                .map(tuple -> createToken(tuple))
                .collect(Collectors.toList());
        return ret;

    }

    public void toWaitsToken(String tokenId, String mesageid) {
        dml("waitstoken/insert", PAGE.params(
                "message", mesageid,
                "token", tokenId
        ));
    }

    //--Context-----------------------------------------------------------------
    public void setProcessContext(String procInstance, Map<String, Object> contex) {
        if (contex == null) {
            return;
        }
        Iterator<String> itr = contex.keySet().iterator();
        while (itr.hasNext()) {
            String name = itr.next();
            Object value = contex.get(name);
            Map params = PAGE.params(
                    "value", value,
                    "id", procInstance,
                    "name", name
            );
            int ok = (Integer) dml("setProcessContext/update", params);
            if (ok == 0) {
                dml("setProcessContext/insert", params);
            }
        }
    }

    public Map getProcessContext(String processId, Set<String> vars) {
        Map ret = new HashMap();
        if (vars == null) {
            stream("getProcessContext", PAGE.params("id", processId))
                    .forEach(record -> {
                        String name = (String) record.get("name");
                        Object value = record.get("value");
                        ret.put(name, value);
                    });
        } else {
            stream("getProcessContext", PAGE.params("id", processId))
                    .forEach(record -> {
                        String name = (String) record.get("name");
                        if (vars.contains(name)) {
                            Object value = record.get("value");
                            ret.put(name, value);
                        }
                    });
        }

        return ret;
    }

    public void setProcessContextByTaskId(String taskId, Map<String, Object> contex) {
        Token token = getTaskToken(taskId);
        String proc = token.getProcess();
        setProcessContext(proc, contex);
    }

    // -------------------------------------------------------------------------
    public String startProcess(String processDefId, Map<String, Object> contex) {
        org.xsystem.bpmn2.model.process.Process process = getProcess(processDefId);
        processDefId = process.getId();

        String procInstance = (String) rowValue("startProcess", PAGE.params("processdefinition", processDefId), "id");

        setProcessContext(procInstance, contex);

        FlowNode startNode = process.getFlowElements().stream()
                .filter(flowElement -> flowElement instanceof FlowNode)
                .map(flowElement -> (FlowNode) flowElement)
                .filter(flowNode -> flowNode.getIncoming().isEmpty())
                .findFirst().get();

        Token token = newToken(procInstance, startNode.getReference());
        excecute(token);

        return procInstance;
    }

    public void stopProcess(String id) {
        dml("stopProcess", PAGE.params("id", id));
    }

    public Map takeTask(String taskId) {
        Map result = new LinkedHashMap();
        Token token = getTaskToken(taskId);
        if (token==null) {
            throw new BPManager.TaskNotFound("TASK "+taskId+" not found"); 
        //RuntimeException("BPMN2-TASK-NOTFOUND");
        }
        Reference<FlowNode> ref = getFlowNode(token);
        UserTask node = (UserTask) ref.resolvedReference();
        Rendering rendering = node.getRendering();
        if (rendering != null) {
            String formKey = rendering.getFormKey();
            result.put("formKey", formKey);
            String script = rendering.getFormContext();
            Object context = null;
            if (script != null) {
                Map evalCtx = new HashMap();
                String processInstanceId = token.getProcess();
                Map<String, Object> processContext = getProcessContext(processInstanceId, null);
                evalCtx.put("processContext", processContext);
                context = scriptingService.mvelEval(script, evalCtx, Object.class);
                result.put("formContext", context);
            }
        }
        return result;
    }

    public void compliteUserTask(String taskid, Map<String, Object> contex) {
        Token token = getTaskToken(taskid);
        
        if (token==null) {
            throw new BPManager.TaskNotFound("TASK "+taskid+" not found"); 
        //RuntimeException("BPMN2-TASK-NOTFOUND");
        }
        
        Reference<FlowNode> ref = getFlowNode(token);
        FlowNode flowNode = ref.resolvedReference();
        Execution execution = ExecutionBuilder.build(flowNode, this);

        execution.complite(token, contex);
        token = fromQueue();
        while (token != null) {
            ref = getFlowNode(token);
            flowNode = ref.resolvedReference();
            execution = ExecutionBuilder.build(flowNode, this);
            execution.execute(token);
            token = fromQueue();
        }
    }

    //--------------------------------------------------------------------------
    boolean checkSubscription(String processId, Map<String, Object> messageData, Object testExpr) {
        List<Map<String, String>> expList = (List) testExpr;
        if (Auxilary.isEmptyOrNull(expList)) {
            return true;
        }
        boolean ok = true;
        Map processContext = getProcessContext(processId, null);
        JXPathContext msgContext =scriptingService.makeJXPathContext(messageData);
     
        JXPathContext procContext = scriptingService.makeJXPathContext(processContext);

        for (Map<String, String> row : expList) {
            String fromExp = row.get("from");
            String toExp = row.get("to");
            Object fromValue = msgContext.getValue(fromExp);
            Object toValue = procContext.getValue(toExp);
            if (!fromValue.equals(toValue)) {
                return false;
            }
        }

        return ok;
    }

    List<Token> getWaitingToken(String messageId, Map<String, Object> messageData) {
        List<Token> ret = stream("waitstoken/get", PAGE.params("message", messageId))
                .filter(row -> checkSubscription((String) row.get("process"), messageData, row.get("value")))
                .map(tuple -> createToken(tuple))
                .collect(Collectors.toList());
        return ret;
    }

    public void sendMessage(String messageId, Map<String, Object> messageData) {
        List<Map<String, Object>> waitingProcess = this.list("getwaitingprocess", PAGE.params("message", messageId));
        List<Token> waitingToken = getWaitingToken(messageId, messageData);
        int cnt = waitingProcess.size() + waitingToken.size();
        if (cnt == 0) {
            throw new Error("Not waiting activity");
        } else if (cnt > 1) {
            throw new Error("waiting activity >1");
        } else if (waitingToken.isEmpty()) {
            Map<String, Object> row = waitingProcess.get(0);
            String processDefId = (String) row.get("id");
            String activity = (String) row.get("activity");
            String procInstance = (String) rowValue("startProcess", PAGE.params("processdefinition", processDefId), "id");
            setProcessContext(procInstance, messageData);
            org.xsystem.bpmn2.model.process.Process process = getProcess(processDefId);
            FlowNode startNode = process.getFlowElements().stream()
                    .filter(flowElement -> flowElement instanceof FlowNode)
                    .map(flowElement -> (FlowNode) flowElement)
                    .filter(flowNode -> flowNode.getIncoming().isEmpty() && flowNode.getId().equals(activity))
                    .findFirst().get();
            Token token = newToken(procInstance, startNode.getReference());
            takeToken(token, startNode.getReference());
        } else {
            Token token = waitingToken.get(0);
            dml("waitstoken/delete", PAGE.params("message", messageId,
                    "token", token.getId()));
            Reference<FlowNode> ref = getFlowNode(token);
            FlowNode flowNode = ref.resolvedReference();
            Execution execution = ExecutionBuilder.build(flowNode, this);
            execution.complite(token, messageData);
        }
    }
    //--------------------------------------------------------------------------

    public void destroyTask(Token token) {

        Reference<FlowNode> ref = getFlowNode(token);

        FlowNode node = ref.resolvedReference();

        if (node instanceof UserTask) {

            String tokenId = token.getId();
            String taskId = (String) this.rowValue("gettaskbytoken", PAGE.params("token", tokenId), "id");
            if (taskId != null) {
                dml("complitetask", PAGE.params("id", taskId));
            }
        }
    }

    ;
    
    public void pushTimer(String timerId) {
        Map data = row("gettimer", PAGE.params("id", timerId));
        String tokenId = (String) data.get("token");
        String targetactivity = (String) data.get("targetactivity");

        Token token = getToken(tokenId);
        if (token != null) {
            String processId = token.getProcessDefinition();
            org.xsystem.bpmn2.model.process.Process process = getProcess(processId);
            Reference<FlowNode> newref = ((DefinitionsImpl) process.getOwnerDefinitions()).createReference(targetactivity);
            destroyTask(token);
            takeToken(token, newref);
            excecute();
        }
        //  SELECT id, token, created, responsetime, targetactivity       
        //<gettimer>
    }

    public void createBoundaryEvents(Token token) {
        Reference<FlowNode> ref = getFlowNode(token);
        String processDefId = token.getProcessDefinition();
        org.xsystem.bpmn2.model.process.Process process = getProcess(processDefId);

        process.getFlowElements().stream()
                .filter(element -> element instanceof BoundaryEvent)
                .map(element -> (BoundaryEvent) element)
                .filter(element -> element.getAttachedToRef() != null)
                .filter(element -> element.getAttachedToRef().equals(ref))
                .forEach(boundaryEvent -> {
                    createBoundaryEventDefinition(token, boundaryEvent);
                });
    }

    FlowNode getTarget(org.xsystem.bpmn2.model.process.Process process, Reference srcRef) {

        Reference<FlowNode> tgtRef = process.getFlowElements().stream()
                .filter(element -> element instanceof SequenceFlow)
                .map(element -> (SequenceFlow) element)
                .filter(sequenceFlow -> sequenceFlow.getSourceRef().equals(srcRef))
                .map(sequenceFlow -> sequenceFlow.getTargetRef())
                .findFirst().orElse(null);
        if (tgtRef != null) {
            FlowNode ret = tgtRef.resolvedReference();
            return ret;
        }
        return null;
    }

    void createBoundaryEventDefinition(Token token, BoundaryEvent boundaryEvent) {
        String tokenId = token.getId();
        String processDefId = token.getProcessDefinition();
        org.xsystem.bpmn2.model.process.Process process = getProcess(processDefId);
        String processInstanceId = token.getProcess();
        Map<String, Object> evalCtx = getProcessContext(processInstanceId, null);
        Reference boundaryEventRef = boundaryEvent.getReference();

        boundaryEvent.getEventDefinitions().forEach(
                definition -> {
                    if (definition instanceof TimerEventDefinition) {
                        TimerEventDefinition timerEventDefinition = (TimerEventDefinition) definition;
                        FormalExpression expr = (FormalExpression) timerEventDefinition.getTimeDate();
                        String script = expr.getBody();
                        Date responsetime = scriptingService.mvelEval(script, evalCtx, Date.class);
                        FlowNode target = getTarget(process, boundaryEventRef);
                        String targetid = target.getId();

                        dml("createtimer", PAGE.params(
                                "token", tokenId,
                                "responsetime", responsetime,
                                "targetactivity", targetid
                        ));

                    }
                });
    }

    //--------------------------------------------------------------------------
    public String getDatastore(String id) {
        String sql = (String) rowValue("datastore/get", PAGE.params("id", id), "sql");
        return sql;
    }

    //--------------------------------------------------------------------------
    Token fromQueue() {
        if (queue.isEmpty()) {
            return null;
        }
        Token ret = queue.removeFirst();
        return ret;
    }

    public void excecute(Token token) {
        while (token != null) {
            Reference<FlowNode> ref = getFlowNode(token);
            FlowNode flowNode = ref.resolvedReference();

            Execution execution = ExecutionBuilder.build(flowNode, this);

            execution.execute(token);
            token = fromQueue();
        }
    }

    public void excecute() {
       Token token= fromQueue();
        excecute(token);
    }
    
    //----------PageHelper------------------------------------------------------   
    @Override
    public Connection getConnection() {
        return JDBCTransationManager2.getConnection();
    }

    @Override
    public Document getDocument() {
        ClassLoader classLoader = this.getClass().getClassLoader();

        return XMLUtil.getDocumentFromResourcesE(MachineConfig.executionxml, classLoader);
    }

}

    

