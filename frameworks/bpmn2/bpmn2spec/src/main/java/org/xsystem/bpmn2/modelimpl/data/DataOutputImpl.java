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
import org.xsystem.bpmn2.model.data.DataOutput;
import org.xsystem.bpmn2.model.data.OutputSet;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;

/**
 *
 * @author Andrey Timofeev
 */
public class DataOutputImpl extends ItemAwareElementImpl implements DataOutput{
   String name;// [0..1]
  
  List<Reference< OutputSet>>outputSetRefs=new ArrayList();// [1..*]
  /*
    Указывает, для какого количества элементов (одного или более) OutputSets 
    используется элемент DataOutput. Данный атрибут порождается элементом OutputSets
  */

  List<OutputSet> outputSetwithOptional=new ArrayList();// [0..*]
  /*
    Для каждого элемента Outputset, использующего элемент Dataoutput, 
    можно указать то, может ли Действие быть завершено без формирования на Выходе Данных. 
    Данный атрибут предоставляет список таких значений.
  */
  
  List<OutputSet> outputSetWithWhileExecuting=new ArrayList();// [0..*]
   /*
    Для каждого элемента OutputSet, использующего элемент DataOutput, можно указать то,
    может ли Действие во время выполнения сформировать выходные данные. 
    Данный атрибут предоставляет список таких значений.
  */

   boolean isCollection = false;
   /*
   Данный атрибут определяет, является ли DataOutput набором элементом.
   Необходим тогда, когда нет ни одной ссылки на элемент itemDefinition.
   В случае, если ссылка на itemDefinition все же существует,
   то данный атрибут ДОЛЖЕН иметь то же значение, 
   что и атрибут isCollection элемента itemDefinition, на который он ссылается. 
   */
    @Override
    public String TypeName(){
        return "DataOutput";
    }
    
    public DataOutputImpl(DefinitionsImpl definitions){
        super(definitions);
    }

    public List<Reference<OutputSet>> getOutputSetRefs() {
        return outputSetRefs;
    }

    public List<OutputSet> getOutputSetWithWhileExecuting() {
        return outputSetWithWhileExecuting;
    }

    public List<OutputSet> getOutputSetwithOptional() {
        return outputSetwithOptional;
    }

    public boolean getIsCollection() {
        return isCollection;
    }

    public void setIsCollection(boolean isCollection) {
        this.isCollection = isCollection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
}
