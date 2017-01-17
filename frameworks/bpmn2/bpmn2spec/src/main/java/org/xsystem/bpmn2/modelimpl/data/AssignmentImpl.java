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

import org.xsystem.bpmn2.model.common.Expression;
import org.xsystem.bpmn2.model.data.Assignment;
import org.xsystem.bpmn2.modelimpl.foundation.BaseElementImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;

/**
 *
 * @author Andrey Timofeev
 */
public class AssignmentImpl extends BaseElementImpl implements Assignment{

    Expression from;
    //Атрибут, вычисляющий источник элемента Assignment
    Expression to;
    /*
     * Атрибут, определяющий активную в текущий момент операцию Assignment, а
     * также элемент данных, являющийся целью
     *
     */
    
    @Override
    public String TypeName(){
        return "Assignment";
    }
    
    public AssignmentImpl(DefinitionsImpl definitions) {
        super(definitions);
    }

    public Expression getFrom() {
        return from;
    }

    public void setFrom(Expression from) {
        this.from = from;
    }

    public Expression getTo() {
        return to;
    }

    public void setTo(Expression to) {
        this.to = to;
    }

    
}
