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

import org.xsystem.bpmn2.model.data.DataOutput;
import java.util.ArrayList;
import java.util.List;
import org.xsystem.bpmn2.model.data.DataInput;
import org.xsystem.bpmn2.model.data.InputOutputSpecification;
import org.xsystem.bpmn2.model.data.InputSet;
import org.xsystem.bpmn2.model.data.OutputSet;
import org.xsystem.bpmn2.modelimpl.foundation.BaseElementImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;


/**
 *
 * @author Andrey Timofeev
 */
public class InputOutputSpecificationImpl extends BaseElementImpl implements InputOutputSpecification {

    List<InputSet> inputSets = new ArrayList(); // [1..*]
    /*
     * Используется для ссылки на элемент InputSets, определенный элементом
     * InputOutputSpecification. Любой элемент InputOutputSpecification ДОЛЖЕН
     * определять по-меньшей мере один InputSet
     */
    
    List<OutputSet> outputSets = new ArrayList();// OutputSet [1..*]
     /*
     * Используется для ссылки на элемент OutputSets, определенный элементом
     * InputOutputSpecification. Любой элемент Data Interface ДОЛЖЕН определять
     * по-меньшей мере один OutputSet.
     */
    List<DataInput> dataInputs = new ArrayList();// [0..*]
    /*
     * Используется для ссылки (по желанию) на Входные данные, определенные
     * элементом InputOutputSpecification. В случае, если элемент
     * InputOutputSpecification не определяет никакие Входные данные, это
     * означает, то для запуска Действия такие данные НЕ ОБЯЗАТЕЛЬНЫ. Является
     * упорядоченным набором данных
     */
    
    List<DataOutput> dataOutputs = new ArrayList();//[0..*]
   /*
     * Используется для ссылки (по желанию) на Выходные данные, определенные
     * элементом InputOutputSpecification. В случае, если элемент
     * InputOutputSpecification не определяет никакие Выходные данные, это
     * означает, то для завершения выполнения Действия такие данные НЕ
     * ОБЯЗАТЕЛЬНЫ. Является упорядоченным набором данных
     *
     */

      @Override
    public String TypeName(){
        return "InputOutputSpecification";
    }
    
    public InputOutputSpecificationImpl(DefinitionsImpl definitions) {
        super(definitions);
    }

    public List<DataInput> getDataInputs() {
        return dataInputs;
    }

    public List<DataOutput> getDataOutputs() {
        return dataOutputs;
    }

    public List<InputSet> getInputSets() {
        return inputSets;
    }

    public List<OutputSet> getOutputSets() {
        return outputSets;
    }

    
}
