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
package org.xsystem.utils;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;

/**
 *
 * @author Andrey Timofeev
 */
public class MBeanUtil {
    
    public static ObjectName createObjectName(String strName){
        try {  
            ObjectName ret=new ObjectName(strName);
            return ret;
        } catch (MalformedObjectNameException ex) {
            throw new Error(ex);
        }
    }
    
    public static Object invoke(MBeanServer mbs,ObjectName name, String operationName,
                         Object params[], String signature[]){
        try {
            Object ret=mbs.invoke(name, operationName, params, signature);
            return ret;
        } catch (InstanceNotFoundException | MBeanException | ReflectionException ex) {
            throw new Error(ex);
        }
        
    }
    
    public static String  unregisterMBean(MBeanServer mbs,ObjectName name){
        if (name==null){
            return null;
        }
        try {
            mbs.unregisterMBean(name);
            return null;
        } catch (InstanceNotFoundException | MBeanRegistrationException ex) {
            return ex.getMessage();
        }
    }
    
    public static ObjectInstance registerMBean(MBeanServer mbs,Object object, ObjectName name,boolean unreg){
        if (unreg){
            if (mbs.isRegistered(name)){
                unregisterMBean(mbs,name);
            }
        }
        try {
            ObjectInstance ret=mbs.registerMBean(object, name);
            return ret;
        } catch (InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException ex) {
           throw new Error(ex);         
        }
    }
    
}
