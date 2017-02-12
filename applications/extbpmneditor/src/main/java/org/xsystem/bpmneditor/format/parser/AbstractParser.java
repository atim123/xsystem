/* 
 * Copyright (C) 2017 Andrey Timofeev
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.xsystem.bpmneditor.format.parser;

import java.util.Map;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmneditor.format.GoParser2;

/**
 *
 * @author Andrey Timofeev
 */
public abstract class AbstractParser {
   GoParser2 owner;

    public AbstractParser(GoParser2 owner) {
        this.owner = owner;
    }    
    
    public abstract void parse(Map<String, Object> modelData);
}
