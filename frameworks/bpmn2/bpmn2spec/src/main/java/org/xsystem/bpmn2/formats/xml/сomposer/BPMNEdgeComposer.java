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

import java.util.List;
import org.w3c.dom.Element;
import org.xsystem.bpmn2.formats.xml.XMLComposer;
import org.xsystem.bpmn2.model.bpmndi.BPMNEdge;
import org.xsystem.bpmn2.model.bpmndi.Point;
import org.xsystem.utils.XMLUtil;

/**
 *
 * @author Andrey Timofeev
 */

class BPMNEdgeComposer implements ElementComposer {

    @Override
    public Element composer(Element root, Object src) {
        BPMNEdge bpmnEdge = (BPMNEdge) src;
        Element ret = XMLUtil.createNewElement(root, "bpmndi:BPMNEdge");
        String id = bpmnEdge.getId();
        String bpmnElement = bpmnEdge.getBpmnElement();
        ComposerFactory.setAttr(ret, "id", id);
        ComposerFactory.setAttr(ret, "bpmnElement", bpmnElement);

        List<Point> points = bpmnEdge.getWaypoints();
        points.stream().forEach((bpmElement) -> {
            ComposerFactory.makeDiagramElement(ret, bpmElement);
        });

        return ret;
    }
}

