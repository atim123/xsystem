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

import java.util.List;
import org.xsystem.bpmn2.model.common.Resource;
import org.xsystem.bpmn2.model.foundation.BaseElement;
import org.xsystem.bpmn2.model.system.Reference;


/**
 *
 * @author Andrey Timofeev
 */
public interface ResourceRole extends BaseElement{
    //должен быть указан, если используется атрибут resourceAssignmentExpression
    
    public Reference<Resource> getResourceRef();
    public void  setResourceRef(Reference<Resource> resourceRef);
    
    public ResourceAssignmentExpression getResourceAssignmentExpression();
    public void setResourceAssignmentExpression(ResourceAssignmentExpression resourceAssignmentExpression);        
    
    public List<ResourceParameterBinding> getResourceParameterBindings();
}
