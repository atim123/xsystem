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
package org.xsystem.bpmn2.model.conversation;

import org.xsystem.bpmn2.model.collaboration.InteractionNode;
import org.xsystem.bpmn2.model.foundation.BaseElement;
import org.xsystem.bpmn2.model.system.Reference;


/**
 *
 * @author Andrey Timofeev
 */
public interface ConversationLink extends BaseElement{
    public String getName();

    public void setName(String name);
    
    //sourceRef: InteractionNode
    public Reference<InteractionNode> getSourceRef();
    public void setSourceRef(Reference<InteractionNode> ref);

    //targetRef: InteractionNode
            
    public Reference<InteractionNode> getTargetRef();
    public void setTargetRef(Reference<InteractionNode> ref);
    
}
