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
import org.xsystem.bpmn2.model.common.CorrelationProperty;
import org.xsystem.bpmn2.model.common.CorrelationPropertyRetrievalExpression;
import org.xsystem.bpmn2.model.common.FormalExpression;
import org.xsystem.bpmn2.model.common.Message;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.utils.XMLUtil;

/**
 *
 * @author Andrey Timofeev
 */
public class CorrelationPropertyComposer extends BaseElementComposer {

    void makeMasegePath(Element root, CorrelationPropertyRetrievalExpression correlationPropertyRetrievalExpression) {
        Element ret = XMLUtil.createNewElement(root, "correlationPropertyRetrievalExpression");

        Reference<Message> ref = correlationPropertyRetrievalExpression.getMessageRef();

        String messageRef = ComposerFactory.getRefId(ref);
        ComposerFactory.setAttr(ret, "messageRef", messageRef);

        FormalExpression formalExpression = correlationPropertyRetrievalExpression.getMessagePath();
        if (formalExpression != null) {
            Element messagePath = XMLUtil.createNewElement(ret, "messagePath");
            String body = formalExpression.getBody();
            XMLUtil.createNewCDATA(messagePath, body);
        }
    }

    ;
    
    /*
    <correlationPropertyRetrievalExpression messageRef="waitOrder">
          <messagePath>orderid</messagePath>
      </correlationPropertyRetrievalExpression>
    */
    
    @Override
    public Element composer(Element root, Object src) {
        CorrelationProperty correlationProperty = (CorrelationProperty) src;

        Element ret = makeBaseElement(root, "correlationProperty", correlationProperty);

        String name = correlationProperty.getName();
        ComposerFactory.setAttr(ret, "name", name);

        List<CorrelationPropertyRetrievalExpression> lst = correlationProperty.getCorrelationPropertyRetrievalExpressions();

        lst.stream().forEach(action -> {
            makeMasegePath(ret, action);
        });
        // List<CorrelationPropertyRetrievalExpression>

        return ret;

    }
}
