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
import org.xsystem.bpmn2.model.bpmndi.Bounds;
import org.xsystem.utils.Auxilary;
import org.xsystem.utils.XMLUtil;

/**
 *
 * @author Andrey Timofeev
 */

class BoundsComposer implements ElementComposer {

    @Override
    public Element composer(Element root, Object src) {
        Bounds bounds = (Bounds) src;
        Element ret = XMLUtil.createNewElement(root, "dc:Bounds");
        ComposerFactory.setAttr(ret, "height", Auxilary.numberToString(bounds.getHeight()));
        ComposerFactory.setAttr(ret, "width", Auxilary.numberToString(bounds.getWidth()));
        ComposerFactory.setAttr(ret, "x", Auxilary.numberToString(bounds.getX()));
        ComposerFactory.setAttr(ret, "y", Auxilary.numberToString(bounds.getY()));
        return ret;
    }
}

