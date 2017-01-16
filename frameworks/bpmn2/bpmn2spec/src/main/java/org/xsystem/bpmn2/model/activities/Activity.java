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
package org.xsystem.bpmn2.model.activities;

import java.util.List;
import org.xsystem.bpmn2.model.collaboration.InteractionNode;
import org.xsystem.bpmn2.model.common.FlowNode;
import org.xsystem.bpmn2.model.data.DataInputAssociation;
import org.xsystem.bpmn2.model.data.DataOutputAssociation;
import org.xsystem.bpmn2.model.data.InputOutputSpecification;


/**
 *
 * @author Andrey Timofeev
 */
public interface Activity extends FlowNode,InteractionNode{
    
    public List<DataOutputAssociation> getDataOutputAssociations();

    public List<DataInputAssociation> getDatainputAssociations();
    
    public InputOutputSpecification getIoSpecification();

    public void setIoSpecification(InputOutputSpecification ioSpecification);
    
    //loopCharacteristics: LoopCharacteristics [0..1]
    public  LoopCharacteristics getLoopCharacteristics();
    public  void  setLoopCharacteristics(LoopCharacteristics loopCharacteristics);
    
    public List<ResourceRole> getResources();
}
