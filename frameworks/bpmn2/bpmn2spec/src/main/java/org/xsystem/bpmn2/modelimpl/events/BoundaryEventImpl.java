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
package org.xsystem.bpmn2.modelimpl.events;

import org.xsystem.bpmn2.model.activities.Activity;
import org.xsystem.bpmn2.model.events.BoundaryEvent;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;

/**
 *
 * @author Andrey Timofeev
 */
public class BoundaryEventImpl extends CatchEventImpl implements BoundaryEvent{
    Reference<Activity> attachedTo;
    Boolean cancelActivity=true;
    
    public BoundaryEventImpl(DefinitionsImpl definitions) {
        super(definitions);
    }

    @Override
    public Reference<Activity> getAttachedToRef() {
        return this.attachedTo;
    }

    @Override
    public void setAttachedToRef(Reference<Activity> ref) {
        this.attachedTo=ref;
    }

    @Override
    public Boolean getCancelActivity() {
        return this.cancelActivity;
    }

    @Override
    public void setCancelActivity(Boolean cancelActivity) {
       this.cancelActivity=cancelActivity;
    }

    
}
