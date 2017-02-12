/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.bpmneditor;

import com.google.gson.Gson;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.xsystem.http.RestApiTemplate;
import static org.xsystem.http.RestApiTemplate.gsonBuilder;
import org.xsystem.utils.Auxilary;
import org.xsystem.utils.MBeanUtil;

/**
 * MbeansOpTree
 *
 * @author tim
 */
public class JMXConsoleRest extends RestApiTemplate {

    @Override
    protected Object getJson(ServletInputStream input) throws IOException {
        String json = getContentAsString(input);
        if (Auxilary.isEmptyOrNull(json)) {
            return new HashMap();
        }
        Gson gson = gsonBuilder.create();
        Object jsonContext = gson.fromJson(json, Object.class);
        return jsonContext;
    }

    String buildOperation(MBeanOperationInfo info) {
        String ret = info.getName();
        if (ret != null) {
            MBeanParameterInfo[] paramsInfo = info.getSignature();
            for (MBeanParameterInfo pinfo : paramsInfo) {
                String pname = pinfo.getName();
                if (Auxilary.isEmptyOrNull(pname)) {
                    return null;
                }
            }
        }
        return ret;
    }

    List beanList(String search) {
        List ret = new ArrayList();
        if (search!=null){
            search=search.trim();
        }
        if (Auxilary.isEmptyOrNull(search)){
            return ret;
        }
            
        ObjectName searchObName = MBeanUtil.createObjectName(search);
         
        
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        Set<ObjectInstance> instances = server.queryMBeans(searchObName, null);
        Iterator<ObjectInstance> iterator = instances.iterator();
        while (iterator.hasNext()) {
            try {
                Map row = new HashMap();
                ObjectInstance instance = iterator.next();
                ObjectName oName = instance.getObjectName();
                MBeanInfo info = server.getMBeanInfo(oName);
                MBeanOperationInfo[] opinfolst = info.getOperations();
                List operation = new ArrayList();
                for (MBeanOperationInfo opinfo : opinfolst) {
                    String op = buildOperation(opinfo);
                    if (op != null) {
                        String description = opinfo.getDescription();
                        Map opRow = new HashMap();
                        opRow.put("name", op);
                        opRow.put("type", "operation");
                        row.put("description", description);
                        opRow.put("leaf", true);
                        operation.add(opRow);
                    }
                }

                if (operation.size() > 0) {
                    String description = info.getDescription();
                    row.put("name", oName.toString());
                    row.put("data", operation);
                    row.put("description", description);
                    row.put("type", "interface");
                    ret.add(row);
                }
            } catch (InstanceNotFoundException | IntrospectionException | ReflectionException ex) {
                Logger.getLogger(JMXConsoleRest.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return ret;
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        prepareJsonResponse(response);
        String path = request.getPathInfo();
        Matcher matcher = Pattern.compile("").matcher(path);
        try (ServletOutputStream out = response.getOutputStream();
                ServletInputStream input = request.getInputStream()) {
            try {
                String search = request.getParameter("search");
                
               
                List ret = beanList(search);
                writeJson(Auxilary.makeJsonSuccess("data", ret), out);
            } catch (Throwable err) {
                error(err, out);
            }
        }
    }
}
