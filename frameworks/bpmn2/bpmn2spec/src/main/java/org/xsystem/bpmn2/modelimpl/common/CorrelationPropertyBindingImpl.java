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
package org.xsystem.bpmn2.modelimpl.common;

import org.xsystem.bpmn2.model.common.CorrelationProperty;
import org.xsystem.bpmn2.model.common.CorrelationPropertyBinding;
import org.xsystem.bpmn2.model.common.FormalExpression;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.foundation.BaseElementImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;


/**
 *
 * @author Andrey Timofeev
 */
public class CorrelationPropertyBindingImpl extends BaseElementImpl implements CorrelationPropertyBinding{
    FormalExpression dataPath;
    Reference<CorrelationProperty> correlationProperty;
    
    public CorrelationPropertyBindingImpl(DefinitionsImpl definitions) {
        super(definitions);
    }

    @Override
    public FormalExpression getDataPath() {
        return dataPath;
    }

    @Override
    public void setDataPath(FormalExpression dataPath) {
        this.dataPath=dataPath;
    }

    @Override
    public Reference<CorrelationProperty> getCorrelationPropertyRef() {
        return correlationProperty;
    }

    @Override
    public void setCorrelationPropertyRef(Reference<CorrelationProperty> correlationProperty) {
        this.correlationProperty=correlationProperty;
    }
}
