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
public class BPMNEdge extends LabeledEdge{
   protected BPMNLabel bpmnLabel;
   protected String bpmnElement;
   protected String sourceElement;
   protected String targetElement;
   protected MessageVisibleKind messageVisibleKind;

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

    public String getSourceElement() {
        return sourceElement;
    }

    public void setSourceElement(String sourceElement) {
        this.sourceElement = sourceElement;
    }

    public String getTargetElement() {
        return targetElement;
    }

    public void setTargetElement(String targetElement) {
        this.targetElement = targetElement;
    }

    public MessageVisibleKind getMessageVisibleKind() {
        return messageVisibleKind;
    }

    public void setMessageVisibleKind(MessageVisibleKind messageVisibleKind) {
        this.messageVisibleKind = messageVisibleKind;
    }
}
