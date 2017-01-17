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

import java.util.Map;
import org.w3c.dom.Element;
import org.xsystem.bpmn2.model.activities.Rendering;
import org.xsystem.bpmn2.model.activities.UserTask;
import org.xsystem.utils.XMLUtil;

/**
 *
 * @author Andrey Timofeev
 */
class UserTaskComposer extends ActivityComposer {

    void makeRendering(Element root, Rendering rendering) {
        Element renderingElem = XMLUtil.createNewElement(root, "rendering");
        Object obj = rendering.getExtensionElements();
        if (obj instanceof Map) {
            Map map = (Map) obj;
            String formKey = (String) map.get("formKey");
            if (formKey != null) {
                Element extensionElements = XMLUtil.createNewElement(renderingElem, "extensionElements");
                XMLUtil.createNewTextElement(extensionElements, "xsystem:formKey", formKey);
            }
            String formContext=(String) map.get("formContext");
            if (formContext != null) {
                Element extensionElements = XMLUtil.createNewElement(renderingElem, "extensionElements");
                XMLUtil.createNewCDATAElement(extensionElements, "xsystem:formContext", formContext);
            }
        }

    }

    @Override
    public Element composer(Element root, Object src) {
        UserTask userTask = (UserTask) src;
        Element ret = makeBaseElement(root, "userTask", userTask);

        Rendering rendering = userTask.getRendering();
        if (rendering != null) {
            makeRendering(ret, rendering);
        }
        return ret;
    }
}

