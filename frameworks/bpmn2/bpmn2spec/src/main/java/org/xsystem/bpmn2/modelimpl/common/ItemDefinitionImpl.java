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

import javax.xml.namespace.QName;
import org.xsystem.bpmn2.model.common.ItemDefinition;
import org.xsystem.bpmn2.model.common.ItemKind;
import org.xsystem.bpmn2.model.infrastructure.Import;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.foundation.RootElementImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;


/**
 *
 * @author Andrey Timofeev
 */
public class ItemDefinitionImpl extends RootElementImpl implements ItemDefinition{
    /*
     * Определяет, является ли компонент физическим или информационным.
     */
    ItemKind itemKind = ItemKind.Information;
    /*
     * Конкретная структура данных, которая будет использована.
     */
    QName structureRef;//[0..1]
    /*
     * Определяет расположение структуры данных и её формат. В случае если
     * значение атрибута importType не указано, берется значение typeLanguage,
     * определенное для элемента Definitions, содержащего данный элемент
     * ItemDefinition
     */
    Import imprt;//: Import [0..1]
    
    /*
     * Значение «true» данного атрибута указывает на то, что текущий тип
     * структуры данных – коллекция.
     */
    boolean isCollection = false;

    @Override
    public String TypeName(){
        return "ItemDefinition";
    }
    
    public ItemDefinitionImpl(DefinitionsImpl definitions){
        super(definitions);
    }
    
    @Override
    public Import getImport() {
        return imprt;
    }

    @Override
    public void setImport(Import imprt) {
        this.imprt = imprt;
    }

    @Override
    public boolean isIsCollection() {
        return isCollection;
    }

    @Override
    public void setIsCollection(boolean isCollection) {
        this.isCollection = isCollection;
    }

    @Override
    public ItemKind getItemKind() {
        return itemKind;
    }

    @Override
    public void setItemKind(ItemKind itemKind) {
        this.itemKind = itemKind;
    }

    @Override
    public QName getStructureRef() {
        return structureRef;
    }

    @Override
    public void setStructureRef(QName structureRef) {
        this.structureRef = structureRef;
    }
}
