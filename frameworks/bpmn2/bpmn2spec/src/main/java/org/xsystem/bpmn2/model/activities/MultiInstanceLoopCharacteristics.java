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
import org.xsystem.bpmn2.model.common.Expression;
import org.xsystem.bpmn2.model.data.DataInput;
import org.xsystem.bpmn2.model.data.DataOutput;
import org.xsystem.bpmn2.model.data.ItemAwareElement;
import org.xsystem.bpmn2.model.events.EventDefinition;
import org.xsystem.bpmn2.model.system.Reference;


/**
 *
 * @author Andrey Timofeev
 */
public interface MultiInstanceLoopCharacteristics extends LoopCharacteristics{
    // isSequential: boolean = false
    public Boolean getIsSequential();
    public void setIsSequential(Boolean isSequential);
    
    // loopCardinality: Expression [0..1]
    public Expression getLoopCardinality();
    public void  setLoopCardinality(Expression loopCardinality);
    
    //loopDataInputRef: ItemAwareElement [0..1]
    public Reference<ItemAwareElement> getLoopDataInputRef();
    public void  setLoopDataInputRef(Reference<ItemAwareElement> loopDataInputRef);
    
    //loopDataOutputRef: ItemAwareElement [0..1]
    public Reference<ItemAwareElement> getLoopDataOutputRef();
    public void  setLoopDataOutputRef(Reference<ItemAwareElement> loopDataOutputRef);
    
    public DataInput getInputDataItem();
    public void setInputDataItem(DataInput inputDataItem);

    //outputDataItem: DataOutput [0..1]
    public DataOutput getOutputDataItem();
    public void setOutputDataItem(DataOutput outputDataItem);
    
    //behavior: MultiInstanceBehavior
    public MultiInstanceBehavior getBehavior();
    public void setBehavior(MultiInstanceBehavior behavior);
    
    //complexBehaviorDefinition: ComplexBehaviorDefinition [0..*]
    public List<ComplexBehaviorDefinition> getComplexBehaviorDefinition(); 
    
    // completionCondition: Expression [0..1]
    public Expression getCompletionCondition();
    public void setCompletionCondition(Expression completionCondition);
    
    // oneBehaviorEventRef: EventDefinition [0..1]
    public Reference<EventDefinition> getOneBehaviorEventRef();
    public void  setOneBehaviorEventRef(Reference<EventDefinition> oneBehaviorEventRef);
    
    // noneBehaviorEventRef: EventDefinition [0..1] 187
    public Reference<EventDefinition> getNoneBehaviorEventRef();
    public void  setNoneBehaviorEventRef(Reference<EventDefinition> noneBehaviorEventRef);
}
