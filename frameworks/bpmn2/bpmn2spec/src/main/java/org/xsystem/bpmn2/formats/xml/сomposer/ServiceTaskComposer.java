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

import org.w3c.dom.Element;
import org.xsystem.bpmn2.model.activities.ServiceTask;
import org.xsystem.bpmn2.model.service.Operation;
import org.xsystem.bpmn2.model.system.Reference;

/**
 *
 * @author Andrey Timofeev
 */
public class ServiceTaskComposer extends ActivityComposer{

    @Override
    public Element composer(Element root, Object src) {
        ServiceTask serviceTask=(ServiceTask) src;
        Element ret = makeBaseElement(root, "serviceTask",serviceTask);
        Reference<Operation> ref=serviceTask.getOperationRef();
        
        ComposerFactory.setAttr(ret,"operationRef", ref);
        
        return ret;
    }
    
}
/*
SendTask sendTask = (SendTask) src;
        Element ret = makeBaseElement(root, "sendTask",sendTask);
        
        
        ComposerFactory.setAttr(ret,"messageRef",sendTask.getMessageRef());
*/