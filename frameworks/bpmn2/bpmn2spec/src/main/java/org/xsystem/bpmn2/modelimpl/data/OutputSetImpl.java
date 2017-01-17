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
import org.xsystem.bpmn2.model.data.InputSet;
import org.xsystem.bpmn2.model.data.OutputSet;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.foundation.BaseElementImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;

/**
 *
 * @author Andrey Timofeev
 */
public class OutputSetImpl extends BaseElementImpl implements OutputSet{
  String  name;// [0..1]
  List<Reference<DataOutput>>dataOutputRefs=new ArrayList();//[0..*]
  /*
  Данный атрибут определяет элементы DataOutput, которые МОГУТ БЫТЬ произведены на выходе
  */
  List<Reference<DataOutput>>optionalOutputRefs=new ArrayList();// [0..*]
  /*
    Данная ассоциация определяет элементы DataOutput, являющиеся частью элемента OutputSet,
   которые не обязательно должны быть сформированы при завершении выполнения Действия.
   Такая ассоциация НЕ ДОЛЖНА ссылаться на элементы DataOutput,
   не вошедшие в список значений для dataOutputRefs
  */
  List<Reference<DataOutput>>whileExecutingOutputRefs=new ArrayList();//[0..*]
  /*
   Данная ассоциация определяет элементы DataOutput, являющиеся частью элемента OutputSet,
   которые могут быть сформированы в ходе выполнения Действия.
   Такая ассоциация НЕ ДОЛЖНА ссылаться на элементы DataOutput, 
   не вошедшие в список значений для dataOutputRefs
  */
  List<Reference<InputSet>> inputSetRefs=new ArrayList();// [0..*]
  /*
    Данный атрибут указывает на правило входа\выхода данных, 
    определяющее, какой элемент InputSet должен получить значение,
   верное для возможного создания набора выходных данных. 
   Данный атрибут используется в паре с атрибутом outputSetRefs элемента InputSets. 
   Такая комбинация заменяет описанный в BPMN 1.2
   атрибут Действий IORules
  */ 

    
    @Override
    public String TypeName(){
        return "OutputSet";
    }
    
    public OutputSetImpl(DefinitionsImpl definitions){
        super(definitions);
    }

    public List<Reference<DataOutput>> getDataOutputRefs() {
        return dataOutputRefs;
    }

    public List<Reference<InputSet>> getInputSetRefs() {
        return inputSetRefs;
    }

    public List<Reference<DataOutput>> getOptionalOutputRefs() {
        return optionalOutputRefs;
    }

    public List<Reference<DataOutput>> getWhileExecutingOutputRefs() {
        return whileExecutingOutputRefs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
}
