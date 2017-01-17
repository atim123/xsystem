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

package org.xsystem.bpmn2.formats.xml;

import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xsystem.bpmn2.formats.Composer;
import org.xsystem.bpmn2.formats.xml.сomposer.ComposerFactory;
import org.xsystem.bpmn2.model.bpmndi.BPMNDiagram;
import org.xsystem.bpmn2.model.foundation.RootElement;
import org.xsystem.bpmn2.model.infrastructure.Definitions;
import org.xsystem.utils.XMLUtil;
import org.xsystem.utils.Auxilary;

/**
 *
 * @author Andrey Timofeev
 */
public class XMLComposer implements Composer {

    Document rootDoc = null;
    Definitions definitions;

    Element makeDefintion() {
        Element rootElem = XMLUtil.createNewElement(rootDoc, "definitions");
        String targetNamespace = definitions.getTargetNamespace();
        rootElem.setAttribute("xmlns", "http://www.omg.org/spec/BPMN/20100524/MODEL");
        rootElem.setAttribute("xmlns:bpmndi", "http://www.omg.org/spec/BPMN/20100524/DI");
        rootElem.setAttribute("xmlns:dc", "http://www.omg.org/spec/DD/20100524/DC");
        rootElem.setAttribute("xmlns:di", "http://www.omg.org/spec/DD/20100524/DI");
        rootElem.setAttribute("xmlns:tns", targetNamespace);
        rootElem.setAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
        rootElem.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        rootElem.setAttribute("targetNamespace", targetNamespace);
        rootElem.setAttribute("xmlns:xsystem", "http://xsystem.org/bpmn");
        String id = definitions.getId();
        id = (Auxilary.isEmptyOrNull(id) ? "" : id);
        rootElem.setAttribute("id", id);
        String name = definitions.getName();
        name = (Auxilary.isEmptyOrNull(name) ? "" : name);
        rootElem.setAttribute("name", name);
        return rootElem;
    }

    void makeRootElements(Element root) {
        List<RootElement> bpmRootElements = definitions.getRootElements();
        bpmRootElements.stream().forEach((bpmElement) -> {
            ComposerFactory.makeDiagramElement(root,bpmElement);
            
        });
    }

    void makeDiagram(Element root, BPMNDiagram diagram) {
        ComposerFactory.makeDiagramElement(root, diagram);
    }

    

    @Override
    public Document compose(Definitions resource) {
        definitions = resource;
        rootDoc = XMLUtil.newDocumentE();
        Element root = makeDefintion();
        makeRootElements(root);
        List<BPMNDiagram> diagrams = resource.getBPMNDiagrams();
        if (!diagrams.isEmpty()) {
            BPMNDiagram diagram = diagrams.get(0);
            makeDiagram(root, diagram);
        }

        return rootDoc;
    }

    
    
}
