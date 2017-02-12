/* 
 * Copyright (C) 2017 Andrey Timofeev
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.xsystem.bpmneditor.format.parser;

import java.util.Map;
import org.xsystem.bpmn2.model.process.Lane;
import org.xsystem.bpmn2.model.process.LaneSet;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;
import org.xsystem.bpmn2.modelimpl.process.LaneImpl;
import org.xsystem.bpmn2.modelimpl.process.LaneSetImpl;
import org.xsystem.bpmneditor.format.GoParser2;

/**
 *
 * @author Andrey Timofeev
 */
public class LaneParser extends AbstractParser{

    public LaneParser(GoParser2 owner) {
        super(owner);
    }
    

    public void parse(Map<String, Object> modelData) {
        DefinitionsImpl definitions = owner.getDefinitions();
        Lane lane = new LaneImpl(definitions);
        String id = (String) modelData.get("key");
        lane.setId(id);
        String name = (String) modelData.get("text");
        lane.setName(name);

        String group = (String) modelData.get("group");
        org.xsystem.bpmn2.model.process.Process process = owner.getProcess(group);

        if (process.getLaneSets().isEmpty()) {
            process.getLaneSets().add(new LaneSetImpl(definitions));
        }
        LaneSet laneSet = process.getLaneSets().get(0);
        laneSet.getLanes().add(lane);
        owner.toCash(id, lane);
    }
}
