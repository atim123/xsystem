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
package org.xsystem.bpmn2.modelimpl.gateways;

import org.xsystem.bpmn2.model.common.SequenceFlow;
import org.xsystem.bpmn2.model.gateways.ExclusiveGateway;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;


/**
 *
 * @author Andrey Timofeev
 */
public class ExclusiveGatewayImpl extends GatewayImpl implements ExclusiveGateway{
    
    Reference<SequenceFlow> def;
    
       @Override
    public String TypeName(){
        return "exclusiveGateway";
    }
    
    public ExclusiveGatewayImpl(DefinitionsImpl definitions) {
        super(definitions);
    }

    @Override
    public Reference<SequenceFlow> getDefault() {
        return def;
    }

    @Override
    public void setDefult(Reference<SequenceFlow> def) {
      this.def=def;
      
    }

    
}
