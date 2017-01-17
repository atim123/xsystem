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
package org.xsystem.bpmn2.modelimpl.common;

import org.xsystem.bpmn2.model.common.ItemDefinition;
import org.xsystem.bpmn2.model.foundation.RootElement;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.foundation.RootElementImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;


/**
 *
 * @author Andrey Timofeev
 */

/*
 * Ошибка (Error) представляет собой содержимое События типа Ошибка (Error
 * Event) либо Ошибку (Fault) при сбое Операции (Operation). Посредством класса
 * ItemDefinition указывается структура Ошибки (Error). Ошибка генерируется
 * тогда, когда в ходе обработки Действия (Activity) возникает критическая
 * проблема, либо когда выполнение Операции завершается ошибкой.
 */

public class ErrorIpml extends RootElementImpl implements org.xsystem.bpmn2.model.common.Error {
    Reference<ItemDefinition> structureRef;// [0..1]
    String name;
    String errorCode;

    @Override
    public String TypeName(){
        return "Error";
    }
    
    public ErrorIpml(DefinitionsImpl definitions){
        super(definitions);
    }
    
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Reference<ItemDefinition> getStructureRef() {
        return structureRef;
    }

    public void setStructureRef(Reference<ItemDefinition> structureRef) {
        this.structureRef = structureRef;
    }
    
}
