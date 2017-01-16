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
package org.xsystem.bpmn2.model.infrastructure;

import java.util.List;
import java.util.stream.Collectors;
import javax.xml.namespace.NamespaceContext;
import org.xsystem.bpmn2.model.bpmndi.BPMNDiagram;
import org.xsystem.bpmn2.model.collaboration.Collaboration;
import org.xsystem.bpmn2.model.common.CorrelationProperty;
import org.xsystem.bpmn2.model.common.FlowNode;
import org.xsystem.bpmn2.model.common.Message;
import org.xsystem.bpmn2.model.foundation.BaseElement;
import org.xsystem.bpmn2.model.foundation.RootElement;
import org.xsystem.bpmn2.model.system.ImportResolver;
import org.xsystem.bpmn2.model.process.Process;
import org.xsystem.bpmn2.model.service.Interface;
import org.xsystem.bpmn2.model.system.Reference;
/**
 *
 * @author Andrey Timofeev
 */
public interface Definitions extends BaseElement, NamespaceContext {

    public void setNamespaceURI(String prfx, String uri);

    public String getName();

    public void setName(String name);

    public String getTargetNamespace();

    public void setTargetNamespace(String targetNamespace);

    public String getTypeLanguage();

    public void setTypeLanguage(String typeLanguage);

    public String getExpressionLanguage();

    public void setExpressionLanguage(String expressionLanguage);

    public List<Import> getImports();

    public List<RootElement> getRootElements();

    public List<BPMNDiagram> getBPMNDiagrams();

    public ImportResolver getResolver();

    public void setResolver(ImportResolver resolver);

    public Reference createReference(final String namespaceURI, final String localPart);

    public Reference createReference(String namespaceURI, String localPart, String prefix);

    public Reference createReference(String localPart);

    public default Process findProcess(String processId) {
        Process process = getRootElements().stream()
                .filter(rootElement -> rootElement instanceof org.xsystem.bpmn2.model.process.Process)
                .map(rootElement -> (org.xsystem.bpmn2.model.process.Process) rootElement)
                .filter(rootElement -> rootElement.getId().equals(processId))
                .findFirst().get();
        return process;
    }

    public default Process getProcess(FlowNode node) {
        Definitions def = node.getOwnerDefinitions();

        Process ret = def.getRootElements().stream()
                .filter(rootElements -> rootElements instanceof org.xsystem.bpmn2.model.process.Process)
                .map(rootElements -> (org.xsystem.bpmn2.model.process.Process) rootElements)
                .filter(process
                        -> process.getFlowElements().stream()
                        .filter(flowElements -> node.getId().equals(flowElements.getId()))
                        .findFirst().isPresent()
                )
                .findFirst().get();
        return ret;
    }

    public default List<org.xsystem.bpmn2.model.process.Process> getProceses() {
       List<org.xsystem.bpmn2.model.process.Process> ret=getRootElements().stream()
                .filter(rootElement -> rootElement instanceof org.xsystem.bpmn2.model.process.Process)
                .map(rootElement -> (org.xsystem.bpmn2.model.process.Process)rootElement)
                .collect(Collectors.toList());
        return ret;
    }
    
    public default List<Collaboration> getCollaborations(){
        List<Collaboration> ret=getRootElements().stream()
                .filter(rootElement -> rootElement instanceof Collaboration)
                .map(rootElement -> (Collaboration)rootElement)
                .collect(Collectors.toList());
        return ret;         
    }
    
    public default List<CorrelationProperty> getCorrelationPropertyes(){
        List<CorrelationProperty> ret=getRootElements().stream()
                .filter(rootElement -> rootElement instanceof CorrelationProperty)
                .map(rootElement -> (CorrelationProperty)rootElement)
                .collect(Collectors.toList());
        return ret;
    }
    
    public default List<Interface> getInterfaces(){
        List<Interface> ret=getRootElements().stream()
                .filter(rootElement -> rootElement instanceof Interface)
                .map(rootElement -> (Interface)rootElement)
                .collect(Collectors.toList());
        return ret;
    }
    
    public default List<Message> getMessages(){
        List<Message> ret=getRootElements().stream()
                .filter(rootElement -> rootElement instanceof Message)
                .map(rootElement -> (Message)rootElement)
                .collect(Collectors.toList());
        return ret;
    }
            
    public default void prepare() {
        getRootElements().stream()
                .filter(rootElement -> rootElement instanceof org.xsystem.bpmn2.model.process.Process)
                .map(rootElement -> (Process) rootElement)
                .forEach(process -> {
                    process.prepare();
                });
        //.map(rootElement -> (org.xsystem.bpmn2.model.process.Process) rootElement)
    }
}
