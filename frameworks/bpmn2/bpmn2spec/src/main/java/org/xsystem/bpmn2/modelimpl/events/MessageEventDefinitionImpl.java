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
package org.xsystem.bpmn2.modelimpl.events;
import org.xsystem.bpmn2.model.common.Message;
import org.xsystem.bpmn2.model.events.MessageEventDefinition;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;


/**
 *
 * @author Andrey Timofeev
 */
public class MessageEventDefinitionImpl extends EventDefinitionImpl implements MessageEventDefinition {
    Reference<Message> messageRef=null;
    
    public MessageEventDefinitionImpl(DefinitionsImpl definitions) {
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

    
}
