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
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.w3c.dom.Element;
import org.xsystem.bpm2machine.BPManager;
import org.xsystem.bpm2machine.BPManagerFactory;

import org.xsystem.sql2.http.Executer;


/**
 *
 * @author Andrey Timofeev
 */
public class LoadDefinitionsAction implements Executer {
 
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
        String build = (String) params.get("path");
        String path = getPath() +"/"+ build+".xml";
        
        File bpmn = new File(path);

        try (InputStream input = new FileInputStream(bpmn)) {
            BPManager mngr=BPManagerFactory.create();            
            String defid=mngr.deployDefinitions(input);
            return defid;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void parse(Element element) {

    }

}
