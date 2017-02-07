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
package org.xsystem.sql2.page;

/**
 *
 * @author Andrey Timofeev
 */
public class ContextDefinition {
    String name;
    String type;
    String objecttype;
    String ifExpr;
    String value;
    boolean in;
    boolean out;
    
    // -------
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    
    public String getObjectType() {
        return objecttype;
    }

    public void setObjectType(String type) {
        this.objecttype = type;
    }
    
    public String getIfExpr() {
        return ifExpr;
    }

    public void setIfExpr(String ifExpr) {
        this.ifExpr = ifExpr;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isIn() {
        return in;
    }

    public void setIn(boolean in) {
        this.in = in;
    }

    public boolean isOut() {
        return out;
    }

    public void setOut(boolean out) {
        this.out = out;
    }
}
