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
package org.xsystem.bpmn2.model.process;

import java.util.List;
import org.xsystem.bpmn2.model.activities.ResourceRole;
import org.xsystem.bpmn2.model.common.CallableElement;
import org.xsystem.bpmn2.model.common.CorrelationSubscription;
import org.xsystem.bpmn2.model.common.FlowElementsContainer;
import org.xsystem.bpmn2.model.data.Property;

/**
 *
 * @author Andrey Timofeev
 */
public interface Process extends CallableElement,FlowElementsContainer{
    
    public boolean getIsClosed();

    public void setIsClosed(boolean isClosed);

    public boolean getIsExecutable();

    public void setIsExecutable(boolean isExecutable);

    public ProcessType getProcessType();

    public void setProcessType(ProcessType processType);

    public String getState();

    public void setState(String state);
    
    public List<Property> getProperties();
    
    //CorrelationSubscription [0..*]
    public List<CorrelationSubscription> getCorrelationSubscriptions();
    
    public List<ResourceRole> getResources();
    
}
