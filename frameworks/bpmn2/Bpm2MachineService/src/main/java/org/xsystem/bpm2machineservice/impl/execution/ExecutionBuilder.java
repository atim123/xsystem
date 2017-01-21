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

import org.xsystem.bpm2machineservice.impl.ExecutionService;
import org.xsystem.bpmn2.model.activities.ReceiveTask;
import org.xsystem.bpmn2.model.activities.ScriptTask;
import org.xsystem.bpmn2.model.activities.SendTask;
import org.xsystem.bpmn2.model.activities.ServiceTask;
import org.xsystem.bpmn2.model.activities.UserTask;
import org.xsystem.bpmn2.model.common.FlowNode;
import org.xsystem.bpmn2.model.events.EndEvent;
import org.xsystem.bpmn2.model.events.IntermediateCatchEvent;
import org.xsystem.bpmn2.model.events.IntermediateThrowEvent;
import org.xsystem.bpmn2.model.events.StartEvent;
import org.xsystem.bpmn2.model.gateways.EventBasedGateway;
import org.xsystem.bpmn2.model.gateways.ExclusiveGateway;
import org.xsystem.bpmn2.model.gateways.ParallelGateway;


/**
 *
 * @author Andrey Timofeev
 */
public class ExecutionBuilder {
    public static Execution build(FlowNode flowNode,ExecutionService owner){
      if (flowNode instanceof EndEvent) 
           return new EndEventExecution(owner); 
        else if (flowNode instanceof EventBasedGateway)
            return new  EventBasedGatewayExecution(owner);        
        else if (flowNode instanceof ExclusiveGateway)
            return new  ExclusiveGatewayExecution(owner);
        else if (flowNode instanceof IntermediateCatchEvent)
            return new IntermediateCatchEventExecution(owner);        
        else if (flowNode instanceof ParallelGateway)
            return new ParallelGatewayExecution(owner);        
        else if (flowNode instanceof ReceiveTask)
            return new ReceiveTaskExecution(owner);
        else if (flowNode instanceof SendTask)
            return new SendTaskExecution(owner);
        else if (flowNode instanceof StartEvent)
            return new StartEventExecution(owner);
        else if (flowNode instanceof UserTask)
            return new UserTaskExecution(owner);
        else if (flowNode instanceof IntermediateThrowEvent){
            return new IntermediateThrowEventExecution(owner);
        } else if (flowNode instanceof ScriptTask){
            return new ScriptTaskExecution(owner);
        }  else if (flowNode instanceof ServiceTask){
            return new ServiceTaskExecution(owner);
        }      
        
        throw new Error("Not implemented executer from " + flowNode.getClass());
    }
}
