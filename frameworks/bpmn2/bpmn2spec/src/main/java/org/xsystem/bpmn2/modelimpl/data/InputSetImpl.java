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
import org.xsystem.bpmn2.model.data.DataInput;
import org.xsystem.bpmn2.model.data.InputSet;
import org.xsystem.bpmn2.model.data.OutputSet;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.foundation.BaseElementImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;

/**
 *
 * @author Andrey Timofeev
 */
public class InputSetImpl extends BaseElementImpl implements InputSet {

    String name; //[0..1]
    List<Reference<DataInput>> dataInputRefs = new ArrayList(); //[0..*]
    /*
     * Данная ассоциация определяет элементы Datainput, являющиеся частью
     * элемента Inputset, которые могут быть недоступны в начале выполнения
     * Действия. Такая ассоциация НЕ ДОЛЖНА ссылаться на элементы Datainput, не
     * вошедшие в список значений для datainputrefs
     */
    List<Reference<DataInput>> whileExecutingInputRefs = new ArrayList(); //[0..*]
    /*
     * Данная ассоциация определяет элементы Datainput, являющиеся частью
     * элемента Inputset, значения которых могут быть обработаны в ходе
     * выполнения Действия. Такая ассоциация НЕ ДОЛЖНА ссылаться на элементы
     * Datainput, не вошедшие в список значений для datainputrefs.
     */
    
    List<Reference<OutputSet>> outputSetRefs = new ArrayList(); //[0..*]
    /*
     * Данный атрибут указывает на правило входа\выхода данных, определяющее,
     * какой элемент OutputSet будет с большой вероятностью создан Действием,
     * когда данный элемент InputSet будет иметь верное значение. Данный атрибут
     * используется в паре с атрибутом inputSetRefs элемента OutputSets. Такая
     * комбинация заменяет описанный в BPMN 1.2 атрибут Действий IORules.
     *
     */
    
      @Override
    public String TypeName(){
        return "InputSet";
    }
    
    public InputSetImpl(DefinitionsImpl definitions) {
        super(definitions);
    }

    public List<Reference<DataInput>> getDataInputRefs() {
        return dataInputRefs;
    }

    public List<Reference<OutputSet>> getOutputSetRefs() {
        return outputSetRefs;
    }

    public List<Reference<DataInput>> getWhileExecutingInputRefs() {
        return whileExecutingInputRefs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
}
