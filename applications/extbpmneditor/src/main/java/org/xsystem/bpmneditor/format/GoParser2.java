/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.bpmneditor.format;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xsystem.bpmn2.model.bpmndi.Point;
import org.xsystem.bpmn2.model.collaboration.Collaboration;
import org.xsystem.bpmn2.model.common.CorrelationKey;
import org.xsystem.bpmn2.model.infrastructure.Definitions;
import org.xsystem.bpmn2.model.process.Lane;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.collaboration.CollaborationImpl;
import org.xsystem.bpmn2.modelimpl.common.CorrelationKeyImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;
import org.xsystem.bpmn2.modelimpl.process.ProcessImpl;
import org.xsystem.bpmneditor.format.parser.BPMNDiagramParser;
import org.xsystem.bpmneditor.format.parser.DefinitionsParser;
import org.xsystem.bpmneditor.format.parser.FlowElementParser;
import org.xsystem.bpmneditor.format.parser.FlowNodeParser;
import org.xsystem.bpmneditor.format.parser.LaneParser;
import org.xsystem.bpmneditor.format.parser.ProcessParser;

/**
 *
 * @author tim
 */
public class GoParser2 {

    // DefinitionsParser definitionsParser;
    DefinitionsImpl definitions;
    org.xsystem.bpmn2.model.process.Process defProcess = null;
    List<Map> nodeDataArray;
    List<Map> linkDataArray;
    Map modelData;

    Map<String, Map> elementMap = new HashMap();

    private final Map<String, Object> cash = new HashMap();

    public void toCash(String key, Object obj) {
        cash.put(key, obj);
    }

    public Lane getLane(String group) {
        Object tgt = cash.get(group);
        if (tgt instanceof Lane) {
            return (Lane) tgt;
        }
        return null;
    }

    public org.xsystem.bpmn2.model.process.Process getProcess(String group) {
        if (group == null) {
            if (defProcess == null) {
                Map def = (Map) modelData.get("default");
                defProcess = new ProcessImpl(definitions);

                String id = (String) def.get("process");
                defProcess.setId(id);
                String name = (String) def.get("name");
                defProcess.setName(name);
                definitions.getRootElements().add(defProcess);
            }
            return defProcess;
        }
        Object tgt = cash.get(group);
        if (tgt instanceof Lane) {
            Map node = nodeDataArray.stream()
                    .filter(resurce -> {
                        if (isType("Lane", resurce)) {
                            String key = (String) resurce.get("key");
                            boolean ret = key.equals(group);
                            return ret;
                        }
                        return false;
                    })
                    .findFirst().orElse(null);
            String key = (String) node.get("group");
            tgt = cash.get(key);
            if (tgt instanceof org.xsystem.bpmn2.model.process.Process) {
                return (org.xsystem.bpmn2.model.process.Process) tgt;
            }
        } else if (tgt instanceof org.xsystem.bpmn2.model.process.Process) {
            return (org.xsystem.bpmn2.model.process.Process) tgt;
        }
        return null;
    }

    public String genId(String base) {
        String ret = base;
        int corId = 1;
        while (definitions.keyExist(ret)) {
            ret = base + corId;
            corId = corId + 1;
        }
        return ret;
    }

    public DefinitionsImpl getDefinitions() {
        if (definitions == null) {
            definitions = new DefinitionsImpl();
        }
        return definitions;
    }

    boolean isType(String tpy, Map resurce) {
        String testType = (String) resurce.get("type");
        return tpy.equals(testType);
    }

    public Collaboration getCollaboration() {

        if (definitions.getCollaborations().isEmpty()) {
            Collaboration collaboration = new CollaborationImpl(definitions);
            String id = genId("XXX_COLLABORATION");
            collaboration.setId(id);
            definitions.getRootElements().add(collaboration);
        }
        return definitions.getCollaborations().get(0);
    }

    public Reference getCorrelationKeyRef() {
        Collaboration collaboration = getCollaboration();
        if (collaboration.getCorrelationKey().isEmpty()) {
            CorrelationKey correlationKey = new CorrelationKeyImpl(definitions);
            String keyId = genId("XXX_CK");
            correlationKey.setId(keyId);
            collaboration.getCorrelationKey().add(correlationKey);
        }
        Reference ref = collaboration.getCorrelationKey().get(0).getReference();
        return ref;
    }

    DefinitionsImpl makeDefinitions() {
        definitions = new DefinitionsImpl();

        DefinitionsParser definitionsParser = new DefinitionsParser(this);
        definitionsParser.parse(modelData);

        return definitions;
    }

    void buildElementMap(Map<String, Object> resource) {
        modelData = (Map) resource.get("modelData");
        nodeDataArray = (List) resource.get("nodeDataArray");
        linkDataArray = (List) resource.get("linkDataArray");

        nodeDataArray.stream().forEach(action -> {
            String key = (String) action.get("key");
            elementMap.put(key, action);
        });

        linkDataArray.stream().forEach(action -> {
            String key = (String) action.get("key");
            elementMap.put(key, action);
        });
    }

    public Point calcPoolsize(String pooLkey) {
        Point ret = new Point();
        ret.setX(0);
        ret.setY(0);
        nodeDataArray.stream().filter(resurce -> isType("Lane", resurce))
                .forEach(resurce -> {
                    String group = (String) resurce.get("group");
                    if (group.equals(pooLkey)) {
                        String sSize = (String) resurce.get("size");
                        Point p = BPMNDiagramParser.makePoint(sSize);
                        double x = p.getX();
                        double y = p.getY();
                        ret.setY(y);
                        double retX = ret.getX();
                        retX = retX + x;
                        ret.setX(retX);
                    }
                });
        return ret;
    }

    public Definitions parse(Map<String, Object> resource) {
        buildElementMap(resource);

        definitions = makeDefinitions();

        ProcessParser processParser = new ProcessParser(this);

                nodeDataArray.stream()
                .filter(resurce -> isType("Process", resurce))
                .forEach(resurce -> processParser.parse(resurce));

        LaneParser laneParser = new LaneParser(this);

        nodeDataArray.stream()
                .filter(resurce -> isType("Lane", resurce))
                .forEach(resurce -> laneParser.parse(resurce));

        FlowNodeParser flowNodeParser = new FlowNodeParser(this);
        nodeDataArray.stream()
                .filter(resurce -> !(isType("Lane", resurce) || isType("Process", resurce)))
                .forEach(resurce -> flowNodeParser.parse(resurce));

        FlowElementParser flowElementParser = new FlowElementParser(this);

        linkDataArray.stream()
                .forEach(resurce -> flowElementParser.parse(resurce));
        if (!nodeDataArray.isEmpty()) {
            BPMNDiagramParser diagramParser = new BPMNDiagramParser(this);

            nodeDataArray.stream().forEach(action -> {
                diagramParser.parse(action);
            });

            linkDataArray.stream().forEach(action -> {
                diagramParser.parse(action);
            });
        }
        return definitions;
    }
}
