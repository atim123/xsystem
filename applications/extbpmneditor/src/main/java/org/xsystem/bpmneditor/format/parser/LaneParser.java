/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author tim
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
