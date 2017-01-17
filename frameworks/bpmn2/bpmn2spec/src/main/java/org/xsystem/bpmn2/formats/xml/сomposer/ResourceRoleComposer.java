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
import org.xsystem.bpmn2.model.activities.ResourceAssignmentExpression;
import org.xsystem.bpmn2.model.activities.ResourceRole;
import org.xsystem.bpmn2.model.common.FormalExpression;
import org.xsystem.bpmn2.model.foundation.BaseElement;
import org.xsystem.utils.Auxilary;
import org.xsystem.utils.XMLUtil;

/**
 *
 * @author Andrey Timofeev
 */
abstract class ResourceRoleComposer extends BaseElementComposer {

    @Override
    Element makeBaseElement(Element root, String tag, BaseElement src) {
        ResourceRole resourceRole = (ResourceRole) src;
        Element ret = super.makeBaseElement(root, tag, resourceRole);
        ResourceAssignmentExpression assignmentExpression = resourceRole.getResourceAssignmentExpression();
        if (assignmentExpression != null) {
            Element assignment = XMLUtil.createNewElement(ret, "resourceAssignmentExpression");
            FormalExpression expression = (FormalExpression) assignmentExpression.getExpression();
            String expBody = expression.getBody();
            if (Auxilary.isEmptyOrNull(expBody)) {
                expBody = "";
            }
            XMLUtil.createNewCDATAElement(assignment, "formalExpression", expBody);

        }
        return ret;
    }
}
