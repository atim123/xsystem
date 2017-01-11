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
package org.xsystem.sql2.http.actions;

import java.lang.management.ManagementFactory;
import java.util.Map;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import org.w3c.dom.Element;
import org.xsystem.sql2.convertor.Converter;
import org.xsystem.sql2.convertor.DefaultConvertor;
import org.xsystem.sql2.http.Enviroment;
import org.xsystem.sql2.http.Executer;
import org.xsystem.utils.MBeanUtil;

/**
 *
 * @author Andrey Timofeev
 */
public class MBeanInfoExecuter implements Executer{
    private static final Converter converter = new DefaultConvertor();
    
    @Override
    public Object execute(Map params) throws RuntimeException {
        String name = (String) params.get("name");
        Enviroment env = Enviroment.getEnviroment();
        Map<String, String> objectNames = env.getObjectNames();
        String realName = objectNames.get(name);
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName objname = MBeanUtil.createObjectName(realName);
        try {
            MBeanInfo info = mbs.getMBeanInfo(objname);
            return info;
        } catch (InstanceNotFoundException | IntrospectionException | ReflectionException ex) {
            throw new Error(ex);

        }
    }

    @Override
    public void parse(Element element) {
    }
}
