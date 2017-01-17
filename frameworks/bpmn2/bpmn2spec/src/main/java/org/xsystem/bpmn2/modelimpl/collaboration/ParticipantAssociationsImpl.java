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
package org.xsystem.bpmn2.modelimpl.collaboration;

import org.xsystem.bpmn2.model.collaboration.Participant;
import org.xsystem.bpmn2.model.collaboration.ParticipantAssociations;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.foundation.BaseElementImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;

/**
 *
 * @author Andrey Timofeev
 */
public class ParticipantAssociationsImpl extends BaseElementImpl implements ParticipantAssociations {

    Reference<Participant> innerParticipantRef;
    Reference<Participant> outerParticipantRef;

    public ParticipantAssociationsImpl(DefinitionsImpl definitions) {
        super(definitions);
    }

    @Override
    public Reference<Participant> getInnerParticipantRef() {
        return this.innerParticipantRef;
    }

    @Override
    public void setInnerParticipantRef(Reference<Participant> ref) {
        this.innerParticipantRef = ref;
    }

    @Override
    public Reference<Participant> getOuterParticipantRef() {
        return this.outerParticipantRef;
    }

    @Override
    public void setOuterParticipantRef(Reference<Participant> ref) {
        this.outerParticipantRef = ref;
    }

    
}
