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
package org.xsystem.bpmn2.modelimpl.common;

import org.xsystem.bpmn2.model.common.Expression;
import org.xsystem.bpmn2.model.common.FlowNode;
import org.xsystem.bpmn2.model.common.SequenceFlow;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;

/**
 *
 * @author Andrey Timofeev
 */
public class SequenceFlowImpl extends FlowElementImpl implements SequenceFlow{
    Reference<FlowNode> sourceRef;
    Reference<FlowNode> targetRef;
    Expression conditionExpression;
    
    @Override
    public String TypeName(){
        return "sequenceFlow";
    }
    
    public SequenceFlowImpl(DefinitionsImpl definitions){
        super(definitions);
    }
    
    public String toString(){
      String src="NULL";
      String trg="NULL";
      
      if (sourceRef!=null){
            src =sourceRef.toString();
       } 
      
      if (targetRef!=null){
          trg =targetRef.toString();
      }
      
      String ret ="("+TypeName()+")("+this.getId()+")"+src +"->"+trg;
      return ret;
    }
    
    public Reference<FlowNode> getSourceRef() {
        return sourceRef;
    }

    @Override
    public void setSourceRef(Reference<FlowNode> sourceRef) {
        this.sourceRef = sourceRef;
    }

    public Reference<FlowNode> getTargetRef() {
        return targetRef;
    }

    public void setTargetRef(Reference<FlowNode> targetRef) {
        this.targetRef = targetRef;
    }
    
    public Expression getConditionExpression(){
        return this.conditionExpression;
    }
    
    public void setConditionExpression(Expression conditionExpression){
        this.conditionExpression=conditionExpression;
    }
}
    

