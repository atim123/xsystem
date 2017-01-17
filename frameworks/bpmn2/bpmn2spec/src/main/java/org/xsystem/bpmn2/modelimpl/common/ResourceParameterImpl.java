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

import org.xsystem.bpmn2.model.common.ItemDefinition;
import org.xsystem.bpmn2.model.common.ResourceParameter;
import org.xsystem.bpmn2.modelimpl.foundation.BaseElementImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;


/**
 *
 * @author Andrey Timofeev
 */
public class ResourceParameterImpl extends BaseElementImpl implements ResourceParameter{
    boolean isRequired=false;
    String name;
    ItemDefinition type;
    
    public ResourceParameterImpl(DefinitionsImpl definitions) {
        super(definitions);
    }

    @Override
    public String TypeName(){
        return "ResourceParameter";
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
    public ItemDefinition geType() {
        return this.type;
    }

    @Override
    public void setType(ItemDefinition type) {
       this.type=type;
    }

    @Override
    public boolean getIsRequired() {
       return this.isRequired;
    }

    @Override
    public void setIsRequired(boolean isRequired) {
       this.isRequired=isRequired;
    }
}
