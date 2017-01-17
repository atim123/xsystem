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

import org.xsystem.bpmn2.model.data.DataStore;
import org.xsystem.bpmn2.model.data.DataStoreReference;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;

/**
 *
 * @author Andrey Timofeev
 */
public class DataStoreReferenceImpl extends ItemAwareElementImpl implements DataStoreReference {
    Reference<DataStore> dataStoreRef;
    
      @Override
    public String TypeName(){
        return "DataStoreReference";
    }
    
    public DataStoreReferenceImpl(DefinitionsImpl definitions){
        super(definitions);
    }

    public Reference<DataStore> getDataStoreRef() {
        return dataStoreRef;
    }

    public void setDataStoreRef(Reference<DataStore> dataStoreRef) {
        this.dataStoreRef = dataStoreRef;
    }

    
}
