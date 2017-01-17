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
import org.xsystem.bpmn2.formats.xml.XMLComposer;
import org.xsystem.bpmn2.model.bpmndi.BPMNShape;
import org.xsystem.bpmn2.model.bpmndi.Bounds;
import org.xsystem.utils.XMLUtil;

/**
 *
 * @author Andrey Timofeev
 */

class BPMNShapeComposer implements ElementComposer {

    @Override
    public Element composer(Element root, Object src) {
        BPMNShape bpmmShape = (BPMNShape) src;
        Element ret = XMLUtil.createNewElement(root, "bpmndi:BPMNShape");
        String id = bpmmShape.getId();
        String bpmnElement = bpmmShape.getBpmnElement();
        ComposerFactory.setAttr(ret, "id", id);
        ComposerFactory.setAttr(ret, "bpmnElement", bpmnElement);
        Boolean isHorizontal= bpmmShape.getIsHorizontal();
        if (isHorizontal!=null){
           String  isHorizontalAttr=(isHorizontal?"true":"false");
           ComposerFactory.setAttr(ret, "isHorizontal",isHorizontalAttr);
        }
            
// isHorizontal="true"
        Bounds bounds = bpmmShape.getBounds();
        ComposerFactory.makeDiagramElement(ret, bounds);
        return ret;
    }
}

