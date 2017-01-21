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
package org.xsystem.bpm2machineservice.impl.execution;
import java.sql.Connection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Document;
import org.xsystem.bpm2machineservice.impl.ExecutionService;
import org.xsystem.bpm2machineservice.impl.MachineConfig;
import org.xsystem.bpm2machineservice.impl.Token;
import org.xsystem.bpmn2.model.activities.Rendering;
import org.xsystem.bpmn2.model.activities.ResourceAssignmentExpression;
import org.xsystem.bpmn2.model.activities.ResourceRole;
import org.xsystem.bpmn2.model.activities.UserTask;
import org.xsystem.bpmn2.model.common.FlowNode;
import org.xsystem.bpmn2.model.humaninteraction.HumanPerformer;
import org.xsystem.bpmn2.model.humaninteraction.PotentialOwner;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.sql2.page.PAGE;
import org.xsystem.sql2.page.PageHelper;
import org.xsystem.system.sql.JDBCTransationManager2;
import org.xsystem.utils.XMLUtil;

/**
 *
 * @author Andrey Timofeev
 */
public class UserTaskExecution extends Execution implements PageHelper {

    public UserTaskExecution(ExecutionService owner) {
        super(owner);
    }

    public String createUserTask(Token token) {
        String processId = token.getProcess();
        String tokenId = token.getId();

        Reference<FlowNode> ref = owner.getFlowNode(token);

        UserTask node = (UserTask) ref.resolvedReference();

        String name = node.getName();

        String formKey = null;
        Rendering rendering = node.getRendering();
        if (rendering != null) {
            formKey = rendering.getFormKey();
        }
        Map<String, String> ret = (Map) dml("createtask", PAGE.params(
                "process", processId,
                "token", tokenId,
                "name", name,
                "formkey", formKey
        ));

        String taskId = ret.get("id");

        return taskId;
    }

    @Override
    public void execute(Token token) {

        String processInstanceId = token.getProcess();
        Map<String, Object> evalCtx = owner.getProcessContext(processInstanceId, null);

        Reference<FlowNode> ref = owner.getFlowNode(token);

        owner.createBoundaryEvents(token);

        UserTask node = (UserTask) ref.resolvedReference();
        String taskId = createUserTask(token);
        List<ResourceRole> resourceRoles = node.getResources();
        Set groups = new HashSet();
        Set users = new HashSet();
        resourceRoles.stream().forEach((role) -> {
            if (role instanceof PotentialOwner) {
                ResourceAssignmentExpression resourceAssignmentExpression = role.getResourceAssignmentExpression();
                String group = resolveResourceAssignmentExpression(evalCtx, resourceAssignmentExpression);
                groups.add(group);
            } else if (role instanceof HumanPerformer) {
                ResourceAssignmentExpression resourceAssignmentExpression = role.getResourceAssignmentExpression();
                String user = resolveResourceAssignmentExpression(evalCtx, resourceAssignmentExpression);
                users.add(user);
            }
        });
        setUsersTaskOwner(taskId, groups, users);

    }

    String resolveResourceAssignmentExpression(Map<String, Object> evalCtx, ResourceAssignmentExpression resourceAssignmentExpression) {
        String script = resourceAssignmentExpression.getExpressionBody();
        String ret = owner.getScriptingService().mvelEval(script, evalCtx, String.class);
        return ret;
    }

    public void setUsersTaskOwner(String taskId, Set<String> groups, Set<String> users) {

        Iterator<String> iter = groups.iterator();
        while (iter.hasNext()) {
            String group = iter.next();
            dml("creategroup", PAGE.params(
                    "name", group,
                    "id", taskId
            ));
        }
        iter = users.iterator();
        while (iter.hasNext()) {
            String user = iter.next();
            dml("createuser", PAGE.params(
                    "name", user,
                    "id", taskId
            ));
        }
    }

    @Override
    public void complite(Token token, Map<String, Object> contex) {
        if (contex == null) {
            contex = new HashMap();
        }
        String procInstance = token.getProcess();
        Reference<FlowNode> ref = owner.getFlowNode(token);
        UserTask flowNode = (UserTask) ref.resolvedReference();
        Map outPutContext=new HashMap();
        Map retContext=new HashMap();
        List associationList = flowNode.getDataOutputAssociations();
        AssociationsResolver resolver= new AssociationsResolver(contex,outPutContext,owner.getScriptingService());
        resolver.resolve(associationList);
        retContext.putAll(contex);
        retContext.putAll(outPutContext);
        
        if (!retContext.isEmpty()){
           owner.setProcessContext(procInstance, retContext); 
        }
        
        List<Reference<FlowNode>> outgoing = flowNode.getOutgoingNode();

        owner.destroyTask(token);
        if (outgoing.size() == 1) {// !!!!!
            Reference<FlowNode> newref = outgoing.get(0);
            owner.takeToken(token, newref);
        }

    }

    //--------------------------------------------------------------------------
    @Override
    public Connection getConnection() {
        return JDBCTransationManager2.getConnection();
    }

    @Override
    public Document getDocument() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        return XMLUtil.getDocumentFromResourcesE(MachineConfig.taskxml, classLoader);
    }

}
