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
package org.xsystem.bpmn2.modelimpl.process;

import java.util.ArrayList;
import java.util.List;
import org.xsystem.bpmn2.model.process.Lane;
import org.xsystem.bpmn2.model.process.LaneSet;
import org.xsystem.bpmn2.model.process.Process;
import org.xsystem.bpmn2.modelimpl.foundation.BaseElementImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;

/**
 *
 * @author Andrey Timofeev
 */
public class LaneSetImpl extends BaseElementImpl implements LaneSet {

    String name;
    Process process;
    List<Lane> lanes = new ArrayList();
    Lane parentLane;

    public LaneSetImpl(DefinitionsImpl definitions) {
        super(definitions);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Process getProcess() {
        return process;
    }

    @Override
    public void setProcess(Process process) {
        this.process = process;
    }

    @Override
    public List<Lane> getLanes() {
        return lanes;
    }

    @Override
    public Lane getParentLane() {
        return parentLane;
    }

    @Override
    public void setParentLane(Lane parentLane) {
        this.parentLane = parentLane;
    }

    
}
