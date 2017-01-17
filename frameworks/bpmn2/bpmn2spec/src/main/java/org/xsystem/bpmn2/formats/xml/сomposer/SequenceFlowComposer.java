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
import org.xsystem.bpmn2.model.common.Expression;
import org.xsystem.bpmn2.model.common.FormalExpression;
import org.xsystem.bpmn2.model.common.SequenceFlow;
import org.xsystem.utils.XMLUtil;

/**
 *
 * @author Andrey Timofeev
 */

class SequenceFlowComposer extends FlowElementComposer {

    void makeFormalExpression(Element root,FormalExpression formalExpression){
        Element ret = XMLUtil.createNewElement(root, "conditionExpression");
        String language=formalExpression.getLanguage();
        String body=formalExpression.getBody();
        XMLUtil.createNewCDATA(ret,body);
        ComposerFactory.setAttr(ret, "language",language);
        ComposerFactory.setAttr(ret, "xsi:type","tFormalExpression");
    }
    
    @Override
    public Element composer(Element root, Object src) {
        SequenceFlow sequenceFlow = (SequenceFlow) src;
        Element ret = makeBaseElement(root, "sequenceFlow", sequenceFlow);
        String sourceRef = ComposerFactory.getRefId(sequenceFlow.getSourceRef());
        ComposerFactory.setAttr(ret, "sourceRef", sourceRef);
        String targetRef = ComposerFactory.getRefId(sequenceFlow.getTargetRef());
        ComposerFactory.setAttr(ret, "targetRef", targetRef);
        Expression expression=sequenceFlow.getConditionExpression();
        if (expression instanceof FormalExpression){
           makeFormalExpression(ret,(FormalExpression)expression);
        }
        return ret;
    }
}

