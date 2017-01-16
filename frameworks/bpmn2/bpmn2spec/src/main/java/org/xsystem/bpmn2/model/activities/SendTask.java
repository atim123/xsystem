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
package org.xsystem.bpmn2.model.activities;

import org.xsystem.bpmn2.model.common.Message;
import org.xsystem.bpmn2.model.service.Operation;
import org.xsystem.bpmn2.model.system.Reference;


/**
 *
 * @author Andrey Timofeev
 */
public interface SendTask extends Task{
   //messageRef: Message [0..1]
    public Reference<Message> getMessageRef();
    public void  setMessageRef(Reference<Message> messageRef);
    //operationRef: Operation
    public Reference<Operation> getOperationRef();
    public void  setOperationRef(Reference<Operation> operationRef);
   
    
    public String getImplementation();
    public void setImplementation(String implementation); 
}
