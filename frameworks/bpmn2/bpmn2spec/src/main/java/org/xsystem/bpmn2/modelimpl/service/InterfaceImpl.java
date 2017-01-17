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
package org.xsystem.bpmn2.modelimpl.service;

import java.util.ArrayList;
import java.util.List;
import org.xsystem.bpmn2.model.common.CallableElement;
import org.xsystem.bpmn2.model.service.Interface;
import org.xsystem.bpmn2.model.service.Operation;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.foundation.RootElementImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;

/**
 *
 * @author Andrey Timofeev
 */
public class InterfaceImpl extends RootElementImpl implements Interface {

    String name;
    
    /*
     * Посредством данного атрибута указываются операции, являющиеся частью
     * Интерфейса (должна быть указана по-меньшей мере одна Операция).
     */
    List<Operation> operations = new ArrayList();// [1..*]
   
    /*
     * Посредством данного атрибута указываются Вызываемые Элементы
     * (CallableElements), использующие данный Интерфейс.
     */
    List<CallableElement> callableElements = new ArrayList();// [0..*]
    
    /*
     * Данный атрибут позволяет указывать ссылку на конкретный артефакт базовой
     * технологии реализации, представляющий интерфейс
     */
    String implementationRef;//: Element [0..1]
    
    @Override
     public String TypeName(){
        return "Interface";
    }
    
    public InterfaceImpl(DefinitionsImpl definitions){
        super(definitions);
    }
    
    @Override
    public String getImplementationRef() {
        return implementationRef;
    }

    @Override
    public void setImplementationRef(String implementationRef) {
        this.implementationRef = implementationRef;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<CallableElement> getCallableElements() {
        return callableElements;
    }

    @Override
    public List<Operation> getOperations() {
        return operations;
    }

    
}
