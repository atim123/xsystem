/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.bpmneditor.format.parser;

import java.util.Map;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmneditor.format.GoParser2;

/**
 *
 * @author tim
 */
public abstract class AbstractParser {
   GoParser2 owner;

    public AbstractParser(GoParser2 owner) {
        this.owner = owner;
    }    
    
    public abstract void parse(Map<String, Object> modelData);
}
