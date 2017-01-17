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
import org.xsystem.bpmn2.model.common.Message;
import org.xsystem.bpmn2.model.service.Operation;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.foundation.BaseElementImpl;
import org.xsystem.bpmn2.model.common.Error;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;

/**
 *
 * @author Andrey Timofeev
 */

/*
 * Операция (Operation) определяет Сообщения (Message), которые удаляются и, по
 * желанию, могут создаваться тогда, когда вызывается Операция. Операция также
 * может определять ошибки (от нуля и более), возвращаемые при сбое операции.
 */

public class OperationImpl extends BaseElementImpl implements Operation {

    String name;
    /*
     * Посредством данного атрибута указывается входное Сообщение Операции.
     * Операция имеет лишь одно такое Сообщение.
     */
    Reference<Message> inMessageRef;
    /*
     * Посредством данного атрибута указывается выходное Сообщение Операции.
     * Операция может иметь максимум одно такое Сообщение.
     */
    Reference<Message> outMessageRef;// [0..1]
    /*
     * Посредством данного атрибута указываются ошибки, возвращаемые Операцией.
     * Для Операции МОЖЕТ БЫТЬ указано любое количество элементов Ошибка (Error)
     * – от нуля и более.
     */
    List<Reference<org.xsystem.bpmn2.model.common.Error>> errorRef = new ArrayList();// [0..*]
    /*
     * Данный атрибут позволяет указывать ссылку на конкретный артефакт базовой
     * технологии реализации, представляющий операцию (например, операция WSDL).
     */
    Reference<Object> implementationRef;// [0..1]

    @Override
     public String TypeName(){
        return "Operation";
    }
    
    public OperationImpl(DefinitionsImpl definitions){
        super(definitions);
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
    public Reference<Message> getInMessageRef() {
        return inMessageRef;
    }

    @Override
    public void setInMessageRef(Reference<Message> inMessageRef) {
        this.inMessageRef = inMessageRef;
    }

    @Override
    public Reference<Message> getOutMessageRef() {
        return outMessageRef;
    }

    @Override
    public void setOutMessageRef(Reference<Message> outMessageRef) {
        this.outMessageRef = outMessageRef;
    }

    @Override
    public List<Reference<org.xsystem.bpmn2.model.common.Error>> getErrorRef() {
        return errorRef;
    }

    @Override
    public Reference<Object> getImplementationRef() {
        return implementationRef;
    }

    @Override
    public void setImplementationRef(Reference<Object> implementationRef) {
        this.implementationRef = implementationRef;
    }

    
}
