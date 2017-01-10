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
package org.xsystem.sql2.http;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.jxpath.ClassFunctions;
import org.apache.commons.jxpath.JXPathContext;
import org.w3c.dom.Document;
import org.xsystem.http.RestApiTemplate;
import org.xsystem.system.sql.JDBCTransationManager2;

import static org.xsystem.sql2.http.Enviroment.FILEPRFX;
//import org.xsystem.sql2.dml.SqlHelper;
//import org.xsystem.sql2.http.impl.ActionExecuter;
//import org.xsystem.sql2.http.impl.Config;
//import org.xsystem.sql2.http.impl.FileFormat;
//import org.xsystem.sql2.http.impl.HttpHelper;
//import org.xsystem.sql2.http.impl.PageLoader;
import org.xsystem.utils.Auxilary;
import org.xsystem.utils.FileTransfer;
import org.xsystem.utils.XMLUtil;


/**
 *
 * @author Andrey Timofeev
 */
public class PageServlet2 {
    static Enviroment getEnviroment() {
        return null;
    }
}
