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
import org.xsystem.bpmn2.model.data.DataOutput;
import org.xsystem.bpmn2.model.data.DataOutputAssociation;
import org.xsystem.bpmn2.model.events.CatchEvent;
import org.xsystem.bpmn2.model.foundation.BaseElement;
import org.xsystem.utils.XMLUtil;

/**
 *
 * @author Andrey Timofeev
 */
abstract  class CatchEventComposer extends FlowElementComposer{
    
    void makeDataOutput(Element root,DataOutput dataOutput){
           Element ret=ComposerFactory.makeBaseElement(root,"dataOutput", dataOutput);
           boolean isCollection= dataOutput.getIsCollection();
           String name=dataOutput.getName();
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
    
    
    
    void makeDataOutputAssociation(Element root,DataOutputAssociation dataOutputAssociation){
         Element ret=ComposerFactory.makeBaseElement(root,"dataOutputAssociation",dataOutputAssociation);

         ComposerFactory.setRefBody(ret,"targetRef",dataOutputAssociation.getTargetRef());
         
         List<Assignment> assignments=dataOutputAssociation.getAssignment();
         assignments.stream().forEach(action->{
             makeAssociation(ret,action);
         });
         
    }
    
    @Override
     Element makeBaseElement(Element root, String tag, BaseElement src) {
        CatchEvent catchEvent=(CatchEvent)src;
        Element ret = super.makeBaseElement(root, tag, catchEvent);
        
        List<DataOutput> dataOutputs = catchEvent.getDataOutputs();
        dataOutputs.stream().forEach(action -> {
            makeDataOutput(ret,action);
        });
        
        List<DataOutputAssociation> dataOutputAssociations= catchEvent.getDataOutputAssociations();
        
        dataOutputAssociations.stream().forEach(action -> {
            makeDataOutputAssociation(ret,action);
        });
        
        return ret;
    } 
}
//  <dataOutput id="XX_4_Output" isCollection="false" name="event"/>
/*

<dataOutput id="XX_4_Output" isCollection="false" name="event"/>
      <dataOutputAssociation>
        <targetRef>XX_4_Output</targetRef>
        <assignment>
            <from>reportid</from>
            <to>reportid</to>
         </assignment>
         
      </dataOutputAssociation>
       <messageEventDefinition messageRef="waitOrder"/>

public interface DataOutput extends ItemAwareElement{
    
    public List<Reference<OutputSet>> getOutputSetRefs();

    public List<OutputSet> getOutputSetWithWhileExecuting();

    public List<OutputSet> getOutputSetwithOptional();

    public boolean getIsCollection();

    public void setIsCollection(boolean isCollection);

    public String getName();

    public void setName(String name);
}


*/