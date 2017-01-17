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

import org.xsystem.bpmn2.model.activities.ComplexBehaviorDefinition;
import org.xsystem.bpmn2.model.common.FormalExpression;
import org.xsystem.bpmn2.model.events.ImplicitThrowEvent;
import org.xsystem.bpmn2.modelimpl.foundation.BaseElementImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;


/**
 *
 * @author Andrey Timofeev
 */
public class ComplexBehaviorDefinitionImpl extends BaseElementImpl implements ComplexBehaviorDefinition{
    FormalExpression condition;
    ImplicitThrowEvent event;
    
    public ComplexBehaviorDefinitionImpl(DefinitionsImpl definitions) {
        super(definitions);
    }

    @Override
    public FormalExpression getCondition() {
        return this.condition;
    }

    @Override
    public void setCondition(FormalExpression condition) {
        this.condition=condition;
    }

    @Override
    public ImplicitThrowEvent getEvent() {
         return this.event;
    }

    @Override
    public void setEvent(ImplicitThrowEvent event) {
          this.event=event;
    }
    
}
