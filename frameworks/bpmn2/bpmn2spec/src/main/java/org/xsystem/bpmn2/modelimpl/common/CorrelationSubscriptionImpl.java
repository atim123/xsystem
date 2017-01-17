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

import java.util.ArrayList;
import java.util.List;
import org.xsystem.bpmn2.model.common.CorrelationKey;
import org.xsystem.bpmn2.model.common.CorrelationPropertyBinding;
import org.xsystem.bpmn2.model.common.CorrelationSubscription;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.foundation.BaseElementImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;


/**
 *
 * @author Andrey Timofeev
 */
public class CorrelationSubscriptionImpl extends BaseElementImpl implements CorrelationSubscription{
    Reference<CorrelationKey> correlationKey;
    List<CorrelationPropertyBinding> correlationPropertyBinding=new ArrayList();
    
    public CorrelationSubscriptionImpl(DefinitionsImpl definitions) {
        super(definitions);
    }

    @Override
    public Reference<CorrelationKey> getCorrelationKeyRef() {
        return correlationKey;
    }
    
    @Override
     public void setCorrelationKeyRef(Reference<CorrelationKey> ref){
         this.correlationKey=ref;
     };
    // correlationPropertyBinding: CorrelationPropertyBinding [0..*]
    @Override
    public List<CorrelationPropertyBinding> getCorrelationPropertyBinding(){
        return correlationPropertyBinding;
    };
}
