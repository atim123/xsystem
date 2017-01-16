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
package org.xsystem.bpmn2.model.gateways;

import java.util.List;
import java.util.Optional;
import org.xsystem.bpmn2.model.common.SequenceFlow;
import org.xsystem.bpmn2.model.system.Reference;


/**
 *
 * @author Andrey Timofeev
 */
public interface ExclusiveGateway extends Gateway{
    public Reference<SequenceFlow> getDefault();

    public void setDefult(Reference<SequenceFlow> def);

    public default void prepare() {
        Reference<ExclusiveGateway> baseRef = this.getReference();
        List<SequenceFlow> sequenceFlows = this.getProcess().getFlowElements(SequenceFlow.class);
        Optional<SequenceFlow> defSeq = sequenceFlows.stream()
                .filter(sequenceFlow
                        -> sequenceFlow.getTargetRef().equals(baseRef) && sequenceFlow.getConditionExpression() == null)
                .findFirst();
        if (defSeq.isPresent()) {
            Reference<SequenceFlow> defRef = defSeq.get().getReference();
            setDefult(defRef);
            
        }
        
                
    }
}
