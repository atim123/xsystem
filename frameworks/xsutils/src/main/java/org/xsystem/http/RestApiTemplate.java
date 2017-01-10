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
package org.xsystem.http;

import javax.servlet.http.HttpServlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.xsystem.utils.Auxilary;

/**
 *
 * @author Andrey Timofeev
 */
public class RestApiTemplate extends HttpServlet{
   
    public final static GsonBuilder gsonBuilder = new GsonBuilder()
            .serializeNulls()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .registerTypeHierarchyAdapter(byte[].class,new BytesSerializer());
            
    protected static void prepareJsonResponse(HttpServletResponse response) {
        response.setHeader("Pragma", "No-cache");
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
    }
    
    protected static void prepareXmlResponse(HttpServletResponse response) {
        response.setHeader("Pragma", "No-cache");
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type","application/xml; charset=utf-8");
     }
    
    protected static void write(byte b[], ServletOutputStream out) throws IOException {
        out.write(b);
    }

    protected static void write(String s, ServletOutputStream out) throws IOException {
        out.write(s.getBytes("UTF-8"));
    }

    protected static void writeJson(Object o, ServletOutputStream out) throws IOException {
        Gson gson = gsonBuilder.create();
        String s = gson.toJson(o);
        write(s, out);
    }

    protected static void error(Throwable e, ServletOutputStream out) throws IOException {

        e.printStackTrace();
        Map errRet = Auxilary.makeJsonThrowable(e);
           
        writeJson(errRet, out);
    }

    protected static String getContentAsString(ServletInputStream input) throws IOException {
        byte[] b = IOUtils.toByteArray(input);
        String ret = new String(b, "UTF-8");
        return ret;
    }

    protected Object getJson(ServletInputStream input) throws IOException {
        String json = getContentAsString(input);
        Gson gson = gsonBuilder.create();
        Object jsonContext = gson.fromJson(json, Object.class);
        return jsonContext;
    }
    
    protected String getRequestParameter(HttpServletRequest request,String paramName,String def){
        String ret = request.getParameter(paramName);
        if (Auxilary.isEmptyOrNull(ret)){
            return def;
        }
        return ret;
    }
    
}
