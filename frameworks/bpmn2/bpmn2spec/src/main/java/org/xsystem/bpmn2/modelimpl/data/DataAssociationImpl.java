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

import java.util.ArrayList;
import java.util.List;
import org.xsystem.bpmn2.model.common.Expression;
import org.xsystem.bpmn2.model.data.Assignment;
import org.xsystem.bpmn2.model.data.DataAssociation;
import org.xsystem.bpmn2.model.data.ItemAwareElement;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.foundation.BaseElementImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;


/**
 *
 * @author Andrey Timofeev
 */
public class DataAssociationImpl extends BaseElementImpl implements DataAssociation {

    Expression transformation;//:  [0..1]
    /*
     * Служит для указания опционального выражения трансформации. Фактическая
     * область доступных данных для данного выражения определяется источником и
     * целью типа Ассоциации данных
     */
    List<Assignment> assignment = new ArrayList();// [0..*]
    /*
     * Служит для указания одного или более элементов данных Assignments.
     * Посредством элемента Assignment в структуру цели могут быть добавлены
     * отдельные элементы структуры данных из структуры источника
     */
    List<Reference<ItemAwareElement>> sourceRef = new ArrayList();//[0..*]
    
    Reference<ItemAwareElement> targetRef;

    @Override
    public String TypeName(){
        return "DataAssociation";
    }
    
    public DataAssociationImpl(DefinitionsImpl definitions) {
        super(definitions);
    }

    @Override
    public Reference<ItemAwareElement> getTargetRef() {
        return targetRef;
    }

    @Override
    public void setTargetRef(Reference<ItemAwareElement> targetRef) {
        this.targetRef = targetRef;
    }

    @Override
    public List<Reference<ItemAwareElement>> getSourceRef() {
        return sourceRef;
    }

    public List<Assignment> getAssignment() {
        return assignment;
    }

    public Expression getTransformation() {
        return transformation;
    }

    public void setTransformation(Expression transformation) {
        this.transformation = transformation;
    }

    
}
