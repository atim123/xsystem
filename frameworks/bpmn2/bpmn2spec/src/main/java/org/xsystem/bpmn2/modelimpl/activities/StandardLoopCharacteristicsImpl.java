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

import org.xsystem.bpmn2.model.activities.StandardLoopCharacteristics;
import org.xsystem.bpmn2.model.common.Expression;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;


/**
 *
 * @author Andrey Timofeev
 */
public class StandardLoopCharacteristicsImpl extends LoopCharacteristicsImpl implements StandardLoopCharacteristics{
    boolean testBefore = false;
    Integer loopMaximum;
    Expression loopCondition;
    
    public StandardLoopCharacteristicsImpl(DefinitionsImpl definitions) {
        super(definitions);
    }

    @Override
    public Boolean getTestBefore() {
       return testBefore;
    }

    @Override
    public void setTestBefore(Boolean testBefore) {
       this.testBefore=testBefore;
    }

    @Override
    public Integer getLoopMaximum() {
      return this.loopMaximum;
    }

    @Override
    public void setLoopMaximum(Integer loopMaximum) {
       this.loopMaximum=loopMaximum;
    }

    @Override
    public Expression getLoopCondition() {
        return this.loopCondition;
    }

    @Override
    public void setLoopCondition(Expression loopCondition) {
       this.loopCondition=loopCondition;
    }

    
}
