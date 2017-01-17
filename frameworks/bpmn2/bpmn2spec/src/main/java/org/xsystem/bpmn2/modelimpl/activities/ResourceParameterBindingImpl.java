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
import org.xsystem.bpmn2.model.activities.ResourceParameterBinding;
import org.xsystem.bpmn2.model.common.Expression;
import org.xsystem.bpmn2.model.common.ResourceParameter;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.foundation.BaseElementImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;

/**
 *
 * @author Andrey Timofeev
 */
public class ResourceParameterBindingImpl extends BaseElementImpl  implements ResourceParameterBinding {
    Expression expression;
    Reference<ResourceParameter> parameterRef;
    
    public ResourceParameterBindingImpl(DefinitionsImpl definitions) {
        super(definitions);
    }

    @Override
    public Expression getExpression() {
        return this.expression;
    }

    @Override
    public void setExpression(Expression expression) {
        this.expression=expression;
    }

    @Override
    public Reference<ResourceParameter> getParameterRef() {
       return this.parameterRef;
    }

    @Override
    public void setParameterRef(Reference<ResourceParameter> parameterRef) {
       this.parameterRef=parameterRef;
    }

    
}
