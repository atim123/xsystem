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
package org.xsystem.bpmn2.model.bpmndi;

/**
 *
 * @author Andrey Timofeev
 */
public class Font {
   protected String name;
    protected Double size;
    protected Boolean isBold;
    protected Boolean isItalic;
    protected Boolean isUnderline;
    protected Boolean isStrikeThrough;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public Boolean getIsBold() {
        return isBold;
    }

    public void setIsBold(Boolean isBold) {
        this.isBold = isBold;
    }

    public Boolean getIsItalic() {
        return isItalic;
    }

    public void setIsItalic(Boolean isItalic) {
        this.isItalic = isItalic;
    }

    public Boolean getIsUnderline() {
        return isUnderline;
    }

    public void setIsUnderline(Boolean isUnderline) {
        this.isUnderline = isUnderline;
    }

    public Boolean getIsStrikeThrough() {
        return isStrikeThrough;
    }

    public void setIsStrikeThrough(Boolean isStrikeThrough) {
        this.isStrikeThrough = isStrikeThrough;
    } 
}
