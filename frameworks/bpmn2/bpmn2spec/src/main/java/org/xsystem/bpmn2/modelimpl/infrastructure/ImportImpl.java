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
package org.xsystem.bpmn2.modelimpl.infrastructure;

import org.xsystem.bpmn2.model.infrastructure.Import;

/**
 *
 * @author Andrey Timofeev
 */
public class ImportImpl implements Import {
    /*
     * Посредством данного атрибута указывается тип документа, импортируемого
     * путем добавления абсолютного URL, который определяет используемую в
     * документе кодировку. При импорте документов типа XML Schema 1.0 значением
     * по умолчанию является «http://www.w3.org/2001/XMLSchema», при импорте
     * документов WSDL 2.0 таким значением является
     * «http://www.w3.org/TR/wsdl20/», а при импорте документов типа BPMN 2.0
     * значением по умолчанию является
     * «http://www.omg.org/spec/BPMN/20100524/MODEL». Также МОГУТ поддерживаться
     * и другие типы документов. Документы типов Xml Schema 1.0, WSDL 2.0 и BPMN
     * 2.0 ДОЛЖНЫ поддерживаться в обязательном порядке.
     */

    String importType;

    /*
     * Посредством данного атрибута указывается расположение импортированного
     * элемента.
     */
    String location;// [0..1] 
    /*
     * Посредством данного атрибута указывается пространство имен
     * импортированного элемента.
     */
    String namespace;
    
    
    
    public String TypeName(){
        return "Import";
    }
    
    public String getImportType() {
        return importType;
    }

    public void setImportType(String importType) {
        this.importType = importType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    
}
