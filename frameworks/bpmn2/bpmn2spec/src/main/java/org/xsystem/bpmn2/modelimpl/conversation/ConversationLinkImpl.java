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
package org.xsystem.bpmn2.modelimpl.conversation;

import org.xsystem.bpmn2.model.collaboration.InteractionNode;
import org.xsystem.bpmn2.model.conversation.ConversationLink;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.foundation.BaseElementImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;


/**
 *
 * @author Andrey Timofeev
 */
public class ConversationLinkImpl extends BaseElementImpl implements ConversationLink{
    String name;
    Reference<InteractionNode> sourceRef;
    Reference<InteractionNode> targetRef;
    
    public ConversationLinkImpl(DefinitionsImpl definitions){
        super(definitions);
    }
    
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name=name;
    }

    @Override
    public Reference<InteractionNode> getSourceRef() {
        return sourceRef;
    }

    @Override
    public void setSourceRef(Reference<InteractionNode> ref) {
       this.sourceRef=ref;
    }

    @Override
    public Reference<InteractionNode> getTargetRef() {
        return targetRef;
    }

    @Override
    public void setTargetRef(Reference<InteractionNode> ref) {
        this.targetRef=ref;
    }

    
}
