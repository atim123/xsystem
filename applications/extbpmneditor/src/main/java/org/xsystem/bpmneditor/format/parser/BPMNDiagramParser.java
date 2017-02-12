/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.bpmneditor.format.parser;

import java.util.List;
import java.util.Map;
import org.xsystem.bpmn2.model.bpmndi.BPMNDiagram;
import org.xsystem.bpmn2.model.bpmndi.BPMNEdge;
import org.xsystem.bpmn2.model.bpmndi.BPMNPlane;
import org.xsystem.bpmn2.model.bpmndi.BPMNShape;
import org.xsystem.bpmn2.model.bpmndi.Bounds;
import org.xsystem.bpmn2.model.bpmndi.Point;
import org.xsystem.bpmn2.model.collaboration.Collaboration;
import org.xsystem.bpmn2.model.collaboration.Participant;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;
import org.xsystem.bpmneditor.format.GoParser2;
import org.xsystem.utils.Auxilary;

/**
 *
 * @author tim
 */
public class BPMNDiagramParser extends AbstractParser {

    BPMNPlane plane;

    public BPMNDiagramParser(GoParser2 owner) {
        super(owner);
        makeBPMNDiagram();
    }

    String getBpmElement(String pollId) {
        DefinitionsImpl definitions = owner.getDefinitions();
        Collaboration collaboration = definitions.getCollaborations().get(0);
        List<Participant> participants = collaboration.getParticipants();
        Reference ref = definitions.createReference(pollId);
        Participant p = participants.stream()
                .filter(predicate -> ref.equals(predicate.getProcessRef()))
                .findFirst().orElse(null);
        String ret = p.getId();
        return ret;
    }

    final void makeBPMNDiagram() {
        DefinitionsImpl definitions = owner.getDefinitions();
        String bpmnElement = null;
        BPMNDiagram diagram = new BPMNDiagram();
        if (!definitions.getCollaborations().isEmpty()) {
            bpmnElement = definitions.getCollaborations().get(0).getId();
        }
        if (bpmnElement == null) {
            bpmnElement = owner.getProcess(null).getId();
        }
        plane = new BPMNPlane();
        plane.setBpmnElement(bpmnElement);
        diagram.setBPMNPlane(plane);
        definitions.getBPMNDiagrams().add(diagram);
    }

    public static Point makePoint(String loc) {
        if (loc == null) {
            return null;
        }
        String[] twoWords = loc.split(" ", 2);
        Point point = new Point();
        double x = Double.valueOf(twoWords[0]);
        double y = Double.valueOf(twoWords[1]);
        point.setX(x);
        point.setY(y);
        return point;
    }

    boolean isShape(Map<String, Object> modelData) {
        String tpy = (String) modelData.get("type");
        if (tpy.equals("SequenceFlow") || tpy.equals("MessageFlow")) {
            return false;
        }

        return true;
    }

    @Override
    public void parse(Map<String, Object> modelData) {
        String category = (String) modelData.get("category");
        String bpmElement = (String) modelData.get("key");

        if (isShape(modelData)) {
            BPMNShape shape = new BPMNShape();
            String sLoc = (String) modelData.get("loc");
            Point loc = makePoint(sLoc);
            String sSize = (String) modelData.get("size");
            Point size = makePoint(sSize);;

            if (category.equals("pool")) {
                shape.setIsHorizontal(Boolean.TRUE);
                if (size == null) {
                    size = owner.calcPoolsize(bpmElement);
                }
                bpmElement = getBpmElement(bpmElement);
            } else if (category.equals("lane")) {
                shape.setIsHorizontal(Boolean.TRUE);
            } else if (category.equals("event") && size == null) {
                size = new Point();
                size.setX(42.0);
                size.setY(42.0);
            } else if (category.equals("gateway") && size == null) {
                size = new Point();
                size.setX(80.0);
                size.setY(80.0);
            } else if (category.equals("task") && size == null) {
                size = new Point();
                size.setX(120.0);
                size.setY(80.0);
            }
            shape.setBpmnElement(bpmElement);
            Bounds bounds = new Bounds();
            bounds.setX(loc.getX());
            bounds.setY(loc.getY());
            bounds.setWidth(size.getX());
            bounds.setHeight(size.getY());
            shape.setBounds(bounds);
            plane.getDiagramElements().add(shape);
            if (category.equals("task")) {
                System.out.println(modelData);
                List<Map> boundaryEventArray = (List) modelData.get("boundaryEventArray");
                if (!Auxilary.isEmptyOrNull(boundaryEventArray)) {
                    boundaryEventArray.forEach(action -> {
                        BPMNShape eventShape = new BPMNShape();
                        String portId = (String) action.get("portId");
                        eventShape.setBpmnElement(portId);
                        Bounds eventBounds = new Bounds();
                        eventBounds.setX(loc.getX());
                        eventBounds.setY(loc.getY());
                        eventBounds.setWidth(42.0);
                        eventBounds.setHeight(42.0);
                        eventShape.setBounds(eventBounds);
                        plane.getDiagramElements().add(eventShape);
                    });
                }
            }
        } else {
            BPMNEdge edge = new BPMNEdge();
            String fromPort = (String) modelData.get("fromPort");
            if (!Auxilary.isEmptyOrNull(fromPort)) {
                edge.setBpmnElement(fromPort);
            } else {
                edge.setBpmnElement(bpmElement);
            }
            List<Double> wp = (List) modelData.get("points");
            int size = wp.size() / 2;
            List<Point> wayPoints = edge.getWaypoints();
            for (int i = 0; i < size; i++) {
                int idx = 2 * i;
                double x = wp.get(idx);
                double y = wp.get(idx + 1);
                Point point = new Point();
                point.setX(x);
                point.setY(y);
                wayPoints.add(point);
            }
            plane.getDiagramElements().add(edge);
        }

    }

}
