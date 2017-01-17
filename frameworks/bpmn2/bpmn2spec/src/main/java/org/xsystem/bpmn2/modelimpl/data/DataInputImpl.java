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
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;

/**
 *
 * @author Andrey Timofeev
 */
public class DataInputImpl extends ItemAwareElementImpl implements DataInput{
    String name;// [0..1]
    
    List <Reference<InputSet>> inputSetRefs=new ArrayList();// [1..*]
    /*
       Указывает, для какого количества элементов (одного или более)
       InputSets используется элемент DataInput.
    */
    
    List<InputSet> inputSetwithOptional=new ArrayList();// [0..*]
    /*
       Для каждого элемента InputSet, использующего элемент DataInput, можно указать 
      то, может ли выполнение Действия начаться, если состояние DataInput равно «unavailable»
    */    
 
    List<InputSet> inputSetWithWhileExecuting=new ArrayList();//[0..*]
    /*
     Для каждого элемента InputSet, использующего элемент DataInput, можно указать то,
     может ли Действие во время выполнения обработать входные данные
    */
    boolean isCollection = false;
    /*Данный атрибут определяет, является ли DataInput набором элементом. 
      Необходим тогда, когда нет  ссылки на элемент itemDefinition. 
      В случае, если ссылка на itemDefinition все же существует,
      то данный атрибут ДОЛЖЕН иметь то же значение, 
      что и атрибут isCollection элемента itemDefinition, на который он ссылается.
      Значением по умолчанию является «false».
     */

    @Override
    public String TypeName(){
        return "DataInput";
    }
    
    public DataInputImpl(DefinitionsImpl definitions){
        super(definitions);
    }

    public List<Reference<InputSet>> getInputSetRefs() {
        return inputSetRefs;
    }

    public List<InputSet> getInputSetWithWhileExecuting() {
        return inputSetWithWhileExecuting;
    }

    public List<InputSet> getInputSetwithOptional() {
        return inputSetwithOptional;
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
