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
import org.xsystem.bpmn2.model.common.FormalExpression;
import org.xsystem.bpmn2.model.data.Assignment;
import org.xsystem.bpmn2.model.data.DataInput;
import org.xsystem.bpmn2.model.data.DataInputAssociation;
import org.xsystem.bpmn2.model.data.DataOutput;
import org.xsystem.bpmn2.model.data.DataOutputAssociation;
import org.xsystem.bpmn2.model.events.CatchEvent;
import org.xsystem.bpmn2.model.events.ThrowEvent;
import org.xsystem.bpmn2.model.foundation.BaseElement;
import org.xsystem.utils.XMLUtil;

/**
 *
 * @author Andrey Timofeev
 */
abstract class ThrowEventComposer extends FlowElementComposer {

    void makeDataInput(Element root,DataInput dataInput){
           Element ret=ComposerFactory.makeBaseElement(root,"dataInput", dataInput);
           boolean isCollection= dataInput.getIsCollection();
           String name=dataInput.getName();
           ComposerFactory.setAttr(ret,"name", name);
           ComposerFactory.setAttr(ret,"isCollection",isCollection);
    } 
    
    void makeAssociation(Element root,Assignment assignment){
        Element ret=ComposerFactory.makeBaseElement(root,"assignment",assignment);
        FormalExpression expressionFrom=(FormalExpression)assignment.getFrom();
        FormalExpression  expressionTo=(FormalExpression )assignment.getTo();
       
        String toBody=expressionTo.getBody();
        String fromBody=expressionFrom.getBody();
        XMLUtil.createNewCDATAElement(ret,"from",fromBody);
        XMLUtil.createNewCDATAElement(ret,"to",toBody);
    }
    
    
    
    void makeDataInputAssociation(Element root,DataInputAssociation dataInputAssociation){
         Element ret=ComposerFactory.makeBaseElement(root,"dataInputAssociation",dataInputAssociation);

         ComposerFactory.setRefBody(ret,"targetRef",dataInputAssociation.getTargetRef());
         
         List<Assignment> assignments=dataInputAssociation.getAssignment();
         assignments.stream().forEach(action->{
             makeAssociation(ret,action);
         });
         
    }
    
    
    @Override
    Element makeBaseElement(Element root, String tag, BaseElement src) {
        ThrowEvent throwEvent = (ThrowEvent) src;
        Element ret = super.makeBaseElement(root, tag, throwEvent);

        List<DataInput> dataInputs = throwEvent.getDataInputs();

        dataInputs.stream().forEach(action -> {
            makeDataInput(ret, action);
        });

        List<DataInputAssociation> inputAssociations = throwEvent.getDataInputAssociations();

        inputAssociations.stream().forEach(action -> {
            makeDataInputAssociation(ret, action);
        });

        return ret;

    }
}
