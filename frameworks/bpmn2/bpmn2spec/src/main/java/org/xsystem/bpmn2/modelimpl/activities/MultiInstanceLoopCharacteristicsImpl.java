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

import java.util.List;
import org.xsystem.bpmn2.model.activities.ComplexBehaviorDefinition;
import org.xsystem.bpmn2.model.activities.MultiInstanceBehavior;
import org.xsystem.bpmn2.model.activities.MultiInstanceLoopCharacteristics;
import org.xsystem.bpmn2.model.common.Expression;
import org.xsystem.bpmn2.model.data.DataInput;
import org.xsystem.bpmn2.model.data.DataOutput;
import org.xsystem.bpmn2.model.data.ItemAwareElement;
import org.xsystem.bpmn2.model.events.EventDefinition;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;


/**
 *
 * @author Andrey Timofeev
 */
public class MultiInstanceLoopCharacteristicsImpl extends LoopCharacteristicsImpl implements MultiInstanceLoopCharacteristics{
    boolean isSequential=false;
    Expression loopCardinality;
    Reference<ItemAwareElement> loopDataInputRef;
    Reference<ItemAwareElement> loopDataOutputRef;
    DataInput inputDataItem;
    DataOutput outputDataItem;
    MultiInstanceBehavior behavior;
    List<ComplexBehaviorDefinition> complexBehaviorDefinition;
    Expression completionCondition;
    Reference<EventDefinition> oneBehaviorEventRef;
    Reference<EventDefinition> noneBehaviorEventRef;
    
    public MultiInstanceLoopCharacteristicsImpl(DefinitionsImpl definitions) {
        super(definitions);
    }

    @Override
    public Boolean getIsSequential() {
        return this.isSequential;
    }

    @Override
    public void setIsSequential(Boolean isSequential) {
        this.isSequential= isSequential;
    }

    @Override
    public Expression getLoopCardinality() {
        return this.loopCardinality;
    }

    @Override
    public void setLoopCardinality(Expression loopCardinality) {
        this.loopCardinality= loopCardinality;
    }

    @Override
    public Reference<ItemAwareElement> getLoopDataInputRef() {
        return this.loopDataInputRef;
    }

    @Override
    public void setLoopDataInputRef(Reference<ItemAwareElement> loopDataInputRef) {
        this.loopDataInputRef=loopDataInputRef;
    }

    @Override
    public Reference<ItemAwareElement> getLoopDataOutputRef() {
        return this.loopDataOutputRef;
    }

    @Override
    public void setLoopDataOutputRef(Reference<ItemAwareElement> loopDataOutputRef) {
        this.loopDataOutputRef=loopDataOutputRef;
    }

    @Override
    public DataInput getInputDataItem() {
        return this.inputDataItem;
    }

    @Override
    public void setInputDataItem(DataInput inputDataItem) {
        this.inputDataItem=inputDataItem;
    }

    @Override
    public DataOutput getOutputDataItem() {
       return this.outputDataItem;
    }

    @Override
    public void setOutputDataItem(DataOutput outputDataItem) {
        this.outputDataItem=outputDataItem;
    }

    @Override
    public MultiInstanceBehavior getBehavior() {
        return this.behavior;
    }

    @Override
    public void setBehavior(MultiInstanceBehavior behavior) {
        this.behavior=behavior;
    }

    @Override
    public List<ComplexBehaviorDefinition> getComplexBehaviorDefinition() {
        return complexBehaviorDefinition;
    }

    @Override
    public Expression getCompletionCondition() {
        return this.completionCondition;
    }

    @Override
    public void setCompletionCondition(Expression completionCondition) {
        this.completionCondition=completionCondition;
    }

    @Override
    public Reference<EventDefinition> getOneBehaviorEventRef() {
        return this.oneBehaviorEventRef;
    }

    @Override
    public void setOneBehaviorEventRef(Reference<EventDefinition> oneBehaviorEventRef) {
        this.oneBehaviorEventRef=oneBehaviorEventRef;
    }

    @Override
    public Reference<EventDefinition> getNoneBehaviorEventRef() {
        return this.noneBehaviorEventRef;
    }

    @Override
    public void setNoneBehaviorEventRef(Reference<EventDefinition> noneBehaviorEventRef) {
        this.noneBehaviorEventRef=noneBehaviorEventRef;
    }

    
}
