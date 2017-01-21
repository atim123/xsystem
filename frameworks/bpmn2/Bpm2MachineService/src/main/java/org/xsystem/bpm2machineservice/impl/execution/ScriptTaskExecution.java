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
import java.util.HashMap;
import java.util.Map;
import org.xsystem.bpmn2.model.activities.ScriptTask;
import org.xsystem.bpmn2.model.common.FlowNode;
import org.xsystem.bpmn2.model.system.Reference;

import org.xsystem.bpm2machineservice.impl.ExecutionService;
import org.xsystem.bpm2machineservice.impl.ScriptingService;
import org.xsystem.bpm2machineservice.impl.Token;

/**
 *
 * @author Andrey Timofeev
 */
public class ScriptTaskExecution extends Execution{

    public ScriptTaskExecution(ExecutionService owner) {
        super(owner);
    }

    @Override
    public void execute(Token token) {
        String processInstanceId = token.getProcess();
        Map<String, Object> processContext = owner.getProcessContext(processInstanceId,null);
        Reference<FlowNode> ref = owner.getFlowNode(token);
        ScriptTask scriptTask = (ScriptTask) ref.resolvedReference();
        String  script=scriptTask.getScript();
        Map evalCtx=new HashMap();
        evalCtx.put("processContext",processContext);
        ScriptingService scriptingService=owner.getScriptingService();
        scriptingService.mvelEval(script, evalCtx);
        owner.setProcessContext(processInstanceId, processContext); //!!!
        moveToken(token);
    }

    @Override
    public void complite(Token token, Map<String, Object> contex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
