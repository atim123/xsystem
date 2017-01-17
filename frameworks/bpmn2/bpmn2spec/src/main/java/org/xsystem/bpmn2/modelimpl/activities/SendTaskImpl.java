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
package org.xsystem.bpmn2.modelimpl.activities;

import org.xsystem.bpmn2.model.activities.SendTask;
import org.xsystem.bpmn2.model.common.Message;
import org.xsystem.bpmn2.model.service.Operation;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;


/**
 *
 * @author Andrey Timofeev
 */
public class SendTaskImpl extends TaskImpl implements SendTask{
    String implementation = "##webService";
    Reference<Message> messageRef;
    Reference<Operation> operationRef;
    
    @Override
    public String TypeName(){
        return "sendTask";
    }
    
    public SendTaskImpl(DefinitionsImpl definitions) {
        super(definitions);
    }

    @Override
    public Reference<Message> getMessageRef() {
        return messageRef;
    }

    @Override
    public void setMessageRef(Reference<Message> messageRef) {
        this.messageRef=messageRef;
    }

    @Override
    public Reference<Operation> getOperationRef() {
        return operationRef;
    }

    @Override
    public void setOperationRef(Reference<Operation> operationRef) {
        this.operationRef=operationRef;
    }

    @Override
    public String getImplementation() {
       return implementation;
    }

    @Override
    public void setImplementation(String implementation) {
        this.implementation=implementation;
    }

    
}
