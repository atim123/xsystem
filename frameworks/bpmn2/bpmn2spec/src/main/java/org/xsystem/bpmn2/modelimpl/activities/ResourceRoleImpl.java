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

import java.util.ArrayList;
import java.util.List;
import org.xsystem.bpmn2.model.activities.ResourceAssignmentExpression;
import org.xsystem.bpmn2.model.activities.ResourceParameterBinding;
import org.xsystem.bpmn2.model.activities.ResourceRole;
import org.xsystem.bpmn2.model.common.Resource;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.foundation.BaseElementImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;


/**
 *
 * @author Andrey Timofeev
 */
public class ResourceRoleImpl extends BaseElementImpl implements ResourceRole{
    Reference<Resource> resourceRef;
    ResourceAssignmentExpression resourceAssignmentExpression;
    List<ResourceParameterBinding> resourceParameterBindings=new ArrayList();
    
    public ResourceRoleImpl(DefinitionsImpl definitions) {
        super(definitions);
    }

    @Override
    public Reference<Resource> getResourceRef() {
        return this.resourceRef;
    }

    @Override
    public void setResourceRef(Reference<Resource> resourceRef) {
       this.resourceRef=resourceRef;
    }

    @Override
    public ResourceAssignmentExpression getResourceAssignmentExpression() {
       return this.resourceAssignmentExpression;
    }

    @Override
    public void setResourceAssignmentExpression(ResourceAssignmentExpression resourceAssignmentExpression) {
       this.resourceAssignmentExpression=resourceAssignmentExpression; 
    }

    @Override
    public List<ResourceParameterBinding> getResourceParameterBindings() {
        return this.resourceParameterBindings;
    }

    
}
