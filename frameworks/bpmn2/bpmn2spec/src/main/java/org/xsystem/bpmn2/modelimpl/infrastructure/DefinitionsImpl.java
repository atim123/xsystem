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

import java.util.*;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import org.xsystem.bpmn2.model.bpmndi.BPMNDiagram;
import org.xsystem.bpmn2.model.foundation.BaseElement;
import org.xsystem.bpmn2.model.foundation.RootElement;
import org.xsystem.bpmn2.model.infrastructure.Definitions;
import org.xsystem.bpmn2.model.infrastructure.Import;
import org.xsystem.bpmn2.model.system.ImportResolver;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.foundation.BaseElementImpl;
import org.xsystem.utils.NamespaceContextImpl;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.xsystem.bpmn2.model.process.Process;

/**
 *
 * @author Andrey Timofeev
 */

/*
 * Класс Definitions – это крайний объект всех элементов BPMN. Посредством
 * элементов данного класса определяются границы видимости и пространство имен
 * элементов, содержащих элементы класса. Обмен файлами BPMN всегда
 * осуществляется благодаря использованию одного или более элементов
 * Definitions.
 *
 */
public class DefinitionsImpl extends BaseElementImpl implements Definitions {

    NamespaceContextImpl namespaceContext = new NamespaceContextImpl();

    //Название элемента Definition.
    String name;
    //При помощи данного атрибута определяется пространство имен
    //,ассоциированное с Definition. Использование данного атрибута
    // также позволяет удовлетворять требованиями языка XML Schema.
    String targetNamespace;
    //При помощи данного атрибута определяется язык формального Выражения (formal Expression),                       
    // МОЖЕТ БЫТЬ изменено для каждого отдельно взятого формального Выражения
    String expressionLanguage = "http://www.w3.org/1999/XPath";
    // При помощи данного атрибута определяется тип системы, используемый элементами Definitions
    // может быть изменено для каждого отдельно взятого ItemDefinition
    String typeLanguage = "http://www.w3.org/2001/XMLSchema";

    /*
     * При помощи данного атрибута осуществляется импорт определенных извне
     * элементов и открывается доступ для использования их элементами
     * Definitions.
     */
    List<Import> imports = new ArrayList();// [0..*]

    /*
     * При помощи данного атрибута определяется список корневых элементов
     * Definitions. На данные элементы может быть указана ссылка. Они видны
     * другим Definitions.
     *
     */
    List<RootElement> rootElements = new ArrayList();// [0..*]
    List<BPMNDiagram> diagrams = new ArrayList();// [0..*]

    ImportResolver resolver;

    //==========================================================================
    Map<QName, BaseElement> localRef = new HashMap();

    public boolean keyExist(String id){
       QName test= new QName(id);
       boolean ret=localRef.containsKey(test);
       return ret;
    }
    
    @Override
    public String TypeName() {
        return "Definitions";
    }

    //==========================================================================
    public DefinitionsImpl() {
        super(null);
        this.definitions = this;
        //setOwnerDefinitions(this);
    }

    //===================NamespaceContext=======================================
    @Override
    public void setNamespaceURI(String prfx, String uri) {
        namespaceContext.set(prfx, uri);
    }

    @Override
    public String getNamespaceURI(String prefix) {
        return namespaceContext.getNamespaceURI(prefix);
    }

    // This method isn't necessary for XPath processing.
    @Override
    public String getPrefix(String uri) {
        return namespaceContext.getPrefix(uri);

    }

    // This method isn't necessary for XPath processing either.
    @Override
    public Iterator getPrefixes(String uri) {
        throw new UnsupportedOperationException();
    }

    //==========================================================================
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getTargetNamespace() {
        return targetNamespace;
    }

    @Override
    public void setTargetNamespace(String targetNamespace) {
        this.targetNamespace = targetNamespace;
    }

    @Override
    public String getTypeLanguage() {
        return typeLanguage;
    }

    @Override
    public void setTypeLanguage(String typeLanguage) {
        this.typeLanguage = typeLanguage;
    }

    @Override
    public String getExpressionLanguage() {
        return expressionLanguage;
    }

    @Override
    public void setExpressionLanguage(String expressionLanguage) {
        this.expressionLanguage = expressionLanguage;
    }

    @Override
    public List<Import> getImports() {
        return imports;
    }

    @Override
    public List<RootElement> getRootElements() {
        return rootElements;
    }

    @Override
    public List<BPMNDiagram> getBPMNDiagrams() {
        return diagrams;
    }

    public void regístrate(BaseElement baseElement) {
        String id = baseElement.getId();
        QName qname = new QName(id);
        localRef.put(qname, baseElement);
    }

    public void unRegístrate(BaseElement baseElement) {
        String id = baseElement.getId();
        QName qname = new QName(id);
        localRef.remove(qname);
    }

    public Reference createReference(final String namespaceURI, final String localPart) {
        Reference r = new ReferenceImpl(namespaceURI, localPart);
        return r;
    }

    public Reference createReference(String namespaceURI, String localPart, String prefix) {
        Reference r = new ReferenceImpl(namespaceURI, localPart, prefix);
        return r;
    }

    
    public Reference createReference(String localPart) {
        Reference r = new ReferenceImpl(localPart);
        return r;
    }

    
    
    @Override
    public ImportResolver getResolver() {
        return resolver;
    }

    @Override
    public void setResolver(ImportResolver resolver) {
        this.resolver = resolver;
    }

    class ReferenceImpl<V> implements Reference<V> {//<T extends Object> {

        QName qname;
        Map fromEq = localRef; //???

        public ReferenceImpl(final String namespaceURI, final String localPart) {
            qname = new QName(namespaceURI, localPart);
        }

        public ReferenceImpl(String namespaceURI, String localPart, String prefix) {
            qname = new QName(namespaceURI, localPart, prefix);
        }

        public ReferenceImpl(String localPart) {
            qname = new QName(localPart);
        }

        public String toString(){
          String ret=  qname.toString();
          return ret;
        }
        
        @Override
        public QName getQName() {
            return qname;
        }

        @Override
        public V resolvedReference() {
            V ret = (V) localRef.get(qname);
            if (ret==null){
                throw new RuntimeException("System eroor can't resolve "+qname);
            }
            return ret;
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder()
                    .append(this.qname)
                    .toHashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ReferenceImpl == false) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            final ReferenceImpl otherObject = (ReferenceImpl) obj;

            return new EqualsBuilder()
                    .append(this.qname, otherObject.qname)
                    .isEquals();

        }

    }
}

