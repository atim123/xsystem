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
package org.xsystem.bpmn2.formats.xml.сomposer;

import org.w3c.dom.Element;
import org.xsystem.bpmn2.model.bpmndi.BPMNDiagram;
import org.xsystem.bpmn2.model.bpmndi.BPMNPlane;
import org.xsystem.utils.Auxilary;
import org.xsystem.utils.XMLUtil;

/**
 *
 * @author Andrey Timofeev
 */

class BPMNDiagramComposer implements ElementComposer {

    @Override
    public Element composer(Element root, Object src) {
        BPMNDiagram diagram = (BPMNDiagram) src;
        Element ret = XMLUtil.createNewElement(root, "bpmndi:BPMNDiagram");
        String id = diagram.getId();
        Double resolution = diagram.getResolution();
        String name = diagram.getName();
        ComposerFactory.setAttr(ret, "id", id);
        ComposerFactory.setAttr(ret, "name", name);
        ComposerFactory.setAttr(ret, "resolution", Auxilary.numberToString(resolution));
        BPMNPlane plane = diagram.getBPMNPlane();
        ComposerFactory.makeDiagramElement(ret, plane);
        return ret;
    }
}

