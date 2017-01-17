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
import org.xsystem.bpmn2.model.collaboration.ParticipantMultiplicity;
import org.xsystem.bpmn2.model.common.PartnerEntity;
import org.xsystem.bpmn2.model.common.PartnerRole;
import org.xsystem.bpmn2.model.service.Interface;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.foundation.BaseElementImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;
import org.xsystem.bpmn2.model.process.Process;

/**
 *
 * @author Andrey Timofeev
 */
public class ParticipantImpl extends BaseElementImpl implements Participant{
    String name;
    Reference<Process> process;
    Reference<PartnerRole> partnerRole;
    Reference<PartnerEntity> partnerEntity;
    Reference<Interface> _interface;
    ParticipantMultiplicity participantMultiplicity;
    
    public ParticipantImpl(DefinitionsImpl definitions){
        super(definitions);
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name=name;
    }

    @Override
    public Reference<Process> getProcessRef() {
        return process;
    }

    @Override
    public void setProcessRef(Reference<Process> process) {
        this.process=process;
    }

    @Override
    public Reference<PartnerRole> getPartnerRole() {
        return this.partnerRole;
    }

    @Override
    public void setPartnerRole(Reference<PartnerRole> partnerRole) {
        this.partnerRole=partnerRole;
    }

    @Override
    public Reference<PartnerEntity> getPartnerEntity() {
        return this.partnerEntity;
    }

    @Override
    public void setPartnerEntity(Reference<PartnerEntity> partnerEntity) {
        this.partnerEntity=partnerEntity;
    }

    @Override
    public Reference<Interface> getInterface() {
        return this._interface;
    }

    @Override
    public void setInterface(Reference<Interface> _interface) {
        this._interface=_interface;
    }
    
    @Override
    public ParticipantMultiplicity getParticipantMultiplicity(){
        return this.participantMultiplicity;
    };
    
    @Override
    public void setParticipantMultiplicity(ParticipantMultiplicity participantMultiplicity){
        this.participantMultiplicity=participantMultiplicity;
    }

    
}
