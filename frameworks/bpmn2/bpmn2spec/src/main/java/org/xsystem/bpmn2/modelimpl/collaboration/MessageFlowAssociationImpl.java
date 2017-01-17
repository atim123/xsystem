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
package org.xsystem.bpmn2.modelimpl.collaboration;

import org.xsystem.bpmn2.model.collaboration.MessageFlow;
import org.xsystem.bpmn2.model.collaboration.MessageFlowAssociation;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.foundation.BaseElementImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;


/**
 *
 * @author Andrey Timofeev
 */
public class MessageFlowAssociationImpl extends BaseElementImpl implements MessageFlowAssociation{
    Reference<MessageFlow> innerMessageFlowRef;
    Reference<MessageFlow> outerMessageFlowRef;
    
    public MessageFlowAssociationImpl(DefinitionsImpl definitions){
        super(definitions);
    }
    
    @Override
    public Reference<MessageFlow> getInnerMessageFlowRef() {
        return innerMessageFlowRef;
    }

    @Override
    public void setInnerMessageFlowRef(Reference<MessageFlow> ref) {
        this.innerMessageFlowRef=ref;
    }

    @Override
    public Reference<MessageFlow> getOuterMessageFlowRef() {
        return outerMessageFlowRef;
    }

    @Override
    public void setOuterMessageFlowRef(Reference<MessageFlow> ref) {
       this.outerMessageFlowRef=ref;
    }

    
}
