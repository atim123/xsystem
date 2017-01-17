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
package org.xsystem.bpmn2.modelimpl.data;

import org.xsystem.bpmn2.model.data.DataObject;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;

/**
 *
 * @author Andrey Timofeev
 */
public class DataObjectImpl extends ItemAwareElementImpl implements DataObject{
    String name;// [0..1]
    
    boolean isCollection = false;
    
    @Override
    public String TypeName(){
        return "DataObject";
    }
    
    public DataObjectImpl(DefinitionsImpl definitions){
        super(definitions);
    }
    
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean getIsCollection() {
        return isCollection;
    }

    @Override
    public void setIsCollection(boolean isCollection) {
        this.isCollection = isCollection;
    }

    
}
