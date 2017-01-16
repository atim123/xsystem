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
package org.xsystem.bpmn2.model.bpmndi;

/**
 *
 * @author Andrey Timofeev
 */
public class BPMNShape extends LabeledShape{
   protected BPMNLabel bpmnLabel;
    protected String bpmnElement;
    protected Boolean isHorizontal;
    protected Boolean isExpanded;
    protected Boolean isMarkerVisible;
    protected Boolean isMessageVisible;
    protected ParticipantBandKind participantBandKind;
    protected String choreographyActivityShape;

    public BPMNLabel getBpmnLabel() {
        return bpmnLabel;
    }

    public void setBpmnLabel(BPMNLabel bpmnLabel) {
        this.bpmnLabel = bpmnLabel;
    }

    public String getBpmnElement() {
        return bpmnElement;
    }

    public void setBpmnElement(String bpmnElement) {
        this.bpmnElement = bpmnElement;
    }

    public Boolean getIsHorizontal() {
        return isHorizontal;
    }

    public void setIsHorizontal(Boolean isHorizontal) {
        this.isHorizontal = isHorizontal;
    }

    public Boolean getIsExpanded() {
        return isExpanded;
    }

    public void setIsExpanded(Boolean isExpanded) {
        this.isExpanded = isExpanded;
    }

    public Boolean getIsMarkerVisible() {
        return isMarkerVisible;
    }

    public void setIsMarkerVisible(Boolean isMarkerVisible) {
        this.isMarkerVisible = isMarkerVisible;
    }

    public Boolean getIsMessageVisible() {
        return isMessageVisible;
    }

    public void setIsMessageVisible(Boolean isMessageVisible) {
        this.isMessageVisible = isMessageVisible;
    }

    public ParticipantBandKind getParticipantBandKind() {
        return participantBandKind;
    }

    public void setParticipantBandKind(ParticipantBandKind participantBandKind) {
        this.participantBandKind = participantBandKind;
    }

    public String getChoreographyActivityShape() {
        return choreographyActivityShape;
    }

    public void setChoreographyActivityShape(String choreographyActivityShape) {
        this.choreographyActivityShape = choreographyActivityShape;
    } 
}
