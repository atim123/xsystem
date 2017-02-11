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
package org.xsystem.bpmnconsole;

import java.util.List;
import java.util.Map;
import org.w3c.dom.Element;
import org.xsystem.sql2.http.Executer;
import org.xsystem.sql2.page.PAGE;


/**
 *
 * @author Andrey Timofeev
 */
public class GetDefinitionsAction implements Executer{

    @Override
    public Object execute(Map params) throws RuntimeException {
        List<Map<String, Object>> ret = Executer.list("definitions", PAGE.params());
        ret.stream().forEach(row -> {
            String rootId = (String) row.get("id");
            List child=Executer.list("processDefinition", PAGE.params("definitionid",rootId));
            row.put("data",child);
        });
        return ret;
   
    }

    @Override
    public void parse(Element element) {
        
    }
    
}
