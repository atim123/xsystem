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

import java.util.ArrayList;
import java.util.List;
import org.xsystem.bpmn2.model.common.FlowNode;
import org.xsystem.bpmn2.model.common.SequenceFlow;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;

/**
 *
 * @author Andrey Timofeev
 */
public class FlowNodeImpl extends FlowElementImpl implements FlowNode {

    protected List<Reference<SequenceFlow>> incoming = new ArrayList();// [0..*]
    protected List<Reference<SequenceFlow>> outgoing = new ArrayList();//[0..*]

    @Override
    public String TypeName() {
        return "FlowNode";
    }

    public FlowNodeImpl(DefinitionsImpl definitions) {
        super(definitions);
    }

    public List<Reference<SequenceFlow>> getIncoming() {
        return incoming;
    }

    public List<Reference<SequenceFlow>> getOutgoing() {
        return outgoing;
    }
}
