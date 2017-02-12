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
package org.xsystem.bpmneditor;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.xsystem.bpmn2.formats.Composer;
import org.xsystem.bpmn2.formats.Parser;
import org.xsystem.bpmn2.formats.xml.XMLComposer;
import org.xsystem.bpmn2.formats.xml.XMLParser3;
import org.xsystem.bpmn2.model.infrastructure.Definitions;
import org.xsystem.bpmneditor.format.GoComposer;
import org.xsystem.bpmneditor.format.GoParser2;
import org.xsystem.http.RestApiTemplate;
import static org.xsystem.http.RestApiTemplate.gsonBuilder;
import org.xsystem.utils.Auxilary;
import org.xsystem.utils.ExtensionsFilter;

/**
 * 
 * @author Andrey Timofeev
 */
public class BpmnEditorRest extends RestApiTemplate {

    String repositoryPath;
    Pattern savePattern = Pattern.compile("^\\/save$");
    Pattern getPattern = Pattern.compile("^\\/get\\/(.+)$");
    Pattern defListPattern=Pattern.compile("^\\/deflist$");
    
    Pattern test = Pattern.compile("^\\/test$");

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        try {
            InitialContext initialContext = new InitialContext();
            repositoryPath = (String) initialContext.lookup("java:/comp/env/bpmnrepo");
        } catch (NamingException ex) {
            Logger.getLogger(BpmnEditorRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        File f = new File(repositoryPath+"/read");
        if (!(f.exists() && f.isDirectory())) {
           f.mkdir();
        }
        f = new File(repositoryPath+"/write");
        if (!(f.exists() && f.isDirectory())) {
           f.mkdir();
        }
    }

    final Map getData(ServletInputStream input) throws IOException {
        Map ctx = (Map) getJson(input);
        if (ctx == null) {
            return new HashMap();
        }
        return ctx;
    }

    void saveJson(Map data, String fn) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(fn, "UTF-8");
            Gson gson = gsonBuilder.create();
            gson.toJson(data, data.getClass(), writer);
            writer.flush();
        } finally {
            Auxilary.close(writer);
        }
    }


    void save(Map data) throws FileNotFoundException, UnsupportedEncodingException {
        Map modelData = (Map) data.get("modelData");
        String key = (String) modelData.get("key");
        String xfn = repositoryPath+"/" + key + ".xml";
        File tmpxfn =new File(xfn+"~");
        String jfn = repositoryPath + "/write/" + key + ".json";

        saveJson(data, jfn);

        GoParser2 goParser = new GoParser2();
        Definitions definitions = goParser.parse(data);
        XMLComposer xmlComposer = new XMLComposer();

        xmlComposer.compose(tmpxfn, definitions);
        
        File tgt=new File(xfn);
        if (tgt.exists()){
            tgt.delete();
        }
        tmpxfn.renameTo(tgt);
        

    }
// fromJson(Reader json, Class<T> classOfT)
    List getDefList(){
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
    

    Map load(String key) throws FileNotFoundException, UnsupportedEncodingException {
        String xfn = repositoryPath +"/"+ key + ".xml";
        String jfn = repositoryPath + "/read/" + key + ".json";
        Parser parser = new XMLParser3();
        Definitions def = parser.prepare(new File(xfn));

        GoComposer composer = new GoComposer();
        Map ret = composer.compose(def);
        saveJson(ret, jfn);

        return ret;
    }

    ;

    
    Map testWork() {
        return null;
    }

    
// FIRSTSTEPDEF
    // "Onkogematologic","OnkogematologicheskyResearch"
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        prepareJsonResponse(response);
        String path = request.getPathInfo();
        Matcher matcher = Pattern.compile("").matcher(path);
        try (ServletOutputStream out = response.getOutputStream();
                ServletInputStream input = request.getInputStream()) {
            try {
                if (matcher.reset().usePattern(savePattern).find()) {
                    Map data = getData(input);
                    save(data);
                    writeJson(Auxilary.makeJsonSuccess(), out);
                } else if (matcher.reset().usePattern(getPattern).find()) {
                    String key = matcher.group(1);
                    Map ret = load(key);
                    writeJson(Auxilary.makeJsonSuccess("data", ret), out);
                } else if (matcher.reset().usePattern(defListPattern).find()) {
                    List ret= getDefList();
                    writeJson(Auxilary.makeJsonSuccess("data", ret), out);
                } else if (matcher.reset().usePattern(test).find()) {
                    Map ret = testWork();
                    writeJson(Auxilary.makeJsonSuccess("data", ret), out);
                }
            } catch (Throwable err) {
                err.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                error(err, out);
            }

        }
    }
}
