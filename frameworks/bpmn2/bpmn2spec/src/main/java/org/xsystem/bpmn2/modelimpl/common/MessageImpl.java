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

import org.xsystem.bpmn2.model.common.ItemDefinition;
import org.xsystem.bpmn2.model.common.Message;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.foundation.RootElementImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;

/**
 *
 * @author Andrey Timofeev
 */
public class MessageImpl extends RootElementImpl implements Message {

    String name;
    Reference<ItemDefinition> itemRef;// [0..1]

    @Override
    public String TypeName() {
        return "Message";
    }

    public MessageImpl(DefinitionsImpl definitions) {
        super(definitions);
    }

    @Override
    public Reference<ItemDefinition> getItemRef() {
        return itemRef;
    }

    @Override
    public void setItemRef(Reference<ItemDefinition> itemRef) {
        this.itemRef = itemRef;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
