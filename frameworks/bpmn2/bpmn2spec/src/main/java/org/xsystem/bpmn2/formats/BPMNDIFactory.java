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
package org.xsystem.bpmn2.formats;

import org.xsystem.bpmn2.model.bpmndi.BPMNEdge;
import org.xsystem.bpmn2.model.bpmndi.BPMNLabel;
import org.xsystem.bpmn2.model.bpmndi.BPMNPlane;
import org.xsystem.bpmn2.model.bpmndi.BPMNShape;
import org.xsystem.bpmn2.model.bpmndi.DiagramElement;


/**
 *
 * @author Andrey Timofeev
 */
public class BPMNDIFactory {
    public static boolean isBPMNPlane(String tag){
        boolean ret=tag.equals("BPMNPlane");
        return  ret;
    }

    public static boolean isBPMNShape(String tag){
        boolean ret=tag.equals("BPMNShape");
        return  ret;
    }
    
    public static boolean isBPMNEdge(String tag){
        boolean ret=tag.equals("BPMNEdge");
        return  ret;
    }
    
    public static boolean isBPMNLabel(String tag){
        boolean ret=tag.equals("BPMNLabel");
        return  ret;
    }
    
    public static boolean isBPMNDI(String tag) {
        switch (tag) {
            case "BPMNPlane":
            case "BPMNShape":
            case "BPMNEdge":
            case "BPMNLabel":    
                return true;
        }
        return false;
    }
    
    
    
    public static DiagramElement createBPMNElement(String resurceType){
        switch (resurceType) {
            case "BPMNPlane":
                return new BPMNPlane();
            case "BPMNShape": 
                return new BPMNShape();
            case "BPMNEdge":
                return new BPMNEdge();
            case "BPMNLabel":    
                return new BPMNLabel();
                
        }
        return null;
    }
    
    public static boolean isBounds(String tag){
        switch (tag) {
            case "Bounds":
               return true;
        }
        return false;
    }
    
    
    
    public static boolean iswaypoint(String tag){
        switch (tag) {
            case "waypoint":
               return true;
        }
        return false;
    }
}
