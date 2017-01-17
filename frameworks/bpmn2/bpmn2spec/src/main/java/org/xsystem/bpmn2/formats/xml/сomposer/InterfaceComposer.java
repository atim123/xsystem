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
package org.xsystem.bpmn2.formats.xml.сomposer;

import java.util.List;
import org.w3c.dom.Element;

import org.xsystem.bpmn2.model.service.Interface;
import org.xsystem.bpmn2.model.service.Operation;
import org.xsystem.bpmn2.model.system.Reference;

/**
 *
 * @author Andrey Timofeev
 */
public class InterfaceComposer extends BaseElementComposer{
    void makeOperation(Element root,Operation operation){
        Element ret = makeBaseElement(root, "operation",operation);
        String name = operation.getName();
        ComposerFactory.setAttr(ret, "name", name);
        
        Reference ref=operation.getInMessageRef();
                
        
        ComposerFactory.setRefBody(ret,"inMessageRef",ref);
    };
    
    
    @Override
    public Element composer(Element root, Object src) {
       Interface inter = (Interface) src;
       
       
       Element ret = makeBaseElement(root, "interface",inter);
        
        String name = inter.getName();
        ComposerFactory.setAttr(ret, "name", name);
        String implementationRef= inter.getImplementationRef();
        ComposerFactory.setAttr(ret,"implementationRef",implementationRef);
        
        List<Operation> operations=inter.getOperations();
        
        operations.stream().forEach(action -> {
            makeOperation(ret, action);
        });
        
        return ret;
    }
    
}
