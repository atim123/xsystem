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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.w3c.dom.Element;
import org.xsystem.sql2.http.Executer;
import org.xsystem.utils.Auxilary;
import org.xsystem.utils.ExtensionsFilter;

/**
 *
 * @author Andrey Timofeev
 */
public class DefListAction implements Executer{
    String getPath(){
    try {
            InitialContext initialContext = new InitialContext();
            String repositoryPath = (String) initialContext.lookup("java:/comp/env/bpmnrepo");
            return repositoryPath;
        } catch (NamingException ex) {
            throw new Error(ex);
        }
    }
    
    
    @Override
    public Object execute(Map params) throws RuntimeException {
       String repositoryPath= getPath();
      List ret=new ArrayList();
      File dir = new File(repositoryPath);
      File[] fliles=dir.listFiles(new ExtensionsFilter(new String[] {".xml"}));
      for (File f:fliles){
         String key= f.getName();
         key= Auxilary.getPathExtention(key);
         Map row=Auxilary.newMap("key",key);
         ret.add(row);
      }
      return ret;
    }

    @Override
    public void parse(Element element) {

    }
    
}
