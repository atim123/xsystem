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
import org.xsystem.bpmn2.model.activities.Activity;
import org.xsystem.bpmn2.model.activities.ResourceRole;
import org.xsystem.bpmn2.model.common.Expression;
import org.xsystem.bpmn2.model.common.FormalExpression;
import org.xsystem.bpmn2.model.data.DataAssociation;
import org.xsystem.bpmn2.model.data.DataInput;
import org.xsystem.bpmn2.model.data.InputOutputSpecification;
import org.xsystem.bpmn2.model.data.InputSet;
import org.xsystem.bpmn2.model.data.OutputSet;
import org.xsystem.bpmn2.model.foundation.BaseElement;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.utils.XMLUtil;

/**
 *
 * @author Andrey Timofeev
 */
abstract class ActivityComposer extends FlowElementComposer {
     
   void makeInputSet(Element root,InputSet inputSet){
       inputSet.getDataInputRefs().stream().forEach(ref->{
            ComposerFactory.setRefBody(root,"dataInputRefs",ref);
       });
   }  
    
   void makeOutputSet(Element root,OutputSet outputSet){
       outputSet.getDataOutputRefs().stream().forEach(ref->{
            ComposerFactory.setRefBody(root,"dataOutputRefs",ref);
       });
    }
   
    void makeioSpecification(Element root,InputOutputSpecification ioSpecification){
        List<DataInput> dataInPuts=ioSpecification.getDataInputs();
        dataInPuts.stream().forEach(action->{
            ComposerFactory.makeBaseElement(root,"dataInput",action);
        });
        ioSpecification.getDataOutputs().stream().forEach(action->{
            ComposerFactory.makeBaseElement(root,"dataOutput",action);
        });
        List<InputSet> inputSets= ioSpecification.getInputSets();
        inputSets.stream().forEach(action->{
              makeInputSet(ComposerFactory.makeBaseElement(root,"inputSet",action),action);        
        });
        
        List<OutputSet> outputSets= ioSpecification.getOutputSets();
        outputSets.stream().forEach(action->{
              makeOutputSet(ComposerFactory.makeBaseElement(root,"outputSet",action),action);        
        });
    }
   
    void makeAssociation(Element root,DataAssociation association){
        Reference ref=association.getTargetRef();
        ComposerFactory.setRefBody(root,"targetRef",ref);
        association.getAssignment().forEach(assignment->{
            Element assignmentElement=ComposerFactory.makeBaseElement(root,"assignment",assignment);
            
            FormalExpression fromExp =(FormalExpression)assignment.getFrom();
            XMLUtil.createNewCDATAElement(assignmentElement,"from",fromExp.getBody());
            
            FormalExpression toExp =(FormalExpression)assignment.getTo();
            XMLUtil.createNewCDATAElement(assignmentElement,"to",toExp.getBody());
        });
    }
    
    
    
    @Override
    Element makeBaseElement(Element root, String tag, BaseElement src) {
        Activity activity = (Activity) src;
        Element ret = super.makeBaseElement(root, tag, activity);
        
        
        InputOutputSpecification ioSpecification=activity.getIoSpecification();
        if (ioSpecification!=null){
          Element ioElement=  ComposerFactory.makeBaseElement(ret,"ioSpecification",ioSpecification);
          makeioSpecification(ioElement,ioSpecification);
        }
        
        activity.getDatainputAssociations().stream().forEach((bpmElement)->{
             Element dataInputAssociationElement=ComposerFactory.makeBaseElement(ret,"dataInputAssociation",bpmElement);
             makeAssociation(dataInputAssociationElement,bpmElement);
        });
        
        activity.getDataOutputAssociations().stream().forEach((bpmElement)->{
             Element dataOutputAssociationElement=ComposerFactory.makeBaseElement(ret,"dataOutputAssociation",bpmElement);
             makeAssociation(dataOutputAssociationElement,bpmElement);
        });
        
        List<ResourceRole> resourceRoles = activity.getResources();
        resourceRoles.stream().forEach((bpmElement) -> {
            ComposerFactory.makeDiagramElement(ret, bpmElement);
        });
        
        return ret;
    }
}
/*
<dataInputAssociation>
         <targetRef>_DataInput_40</targetRef>
         <assignment>
            <from>orderid</from>
            <to>orderid</to>
         </assignment>
         <assignment>
            <from>reportid</from>
            <to>reportid</to>
         </assignment>
       </dataInputAssociation>

*/