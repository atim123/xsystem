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
package org.xsystem.bpm2machineservice.impl.execution;
import java.lang.management.ManagementFactory;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import org.xsystem.bpm2machineservice.impl.ExecutionService;
import org.xsystem.bpm2machineservice.impl.Token;
import org.xsystem.bpmn2.model.activities.ServiceTask;
import org.xsystem.bpmn2.model.common.FlowNode;
import org.xsystem.bpmn2.model.infrastructure.Definitions;
import org.xsystem.bpmn2.model.service.Interface;
import org.xsystem.bpmn2.model.service.Operation;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.sql2.convertor.Converter;
import org.xsystem.sql2.convertor.DefaultConvertor;
import org.xsystem.utils.MBeanUtil;

/**
 *
 * @author Andrey Timofeev
 */
public class ServiceTaskExecution extends Execution {

    private static final Converter converter = new DefaultConvertor();

    public ServiceTaskExecution(ExecutionService owner) {
        super(owner);
    }

    boolean existOperation(Interface inter, Operation operation) {
        boolean ret = inter.getOperations().stream()
                .allMatch(op -> op.getId().equals(operation.getId()));
        return ret;
    }

    Interface getOperationInterface(Operation operation) {
        Definitions definitions = operation.getOwnerDefinitions();
        Interface ret = definitions.getInterfaces().stream()
                .filter(inter -> existOperation(inter, operation))
                .findFirst()
                .orElse(null);
        return ret;
    }

    public MBeanInfo getMBeanInfo(ObjectName objname) {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        try {
            MBeanInfo info = mbs.getMBeanInfo(objname);
            return info;
        } catch (InstanceNotFoundException | IntrospectionException | ReflectionException ex) {
            throw new Error(ex);

        }
    }

    MBeanOperationInfo getOperation(MBeanInfo info, String work) {
        MBeanOperationInfo[] opList = info.getOperations();
        for (MBeanOperationInfo opInfo : opList) {
            String test = opInfo.getName();
            if (work.equals(test)) {
                return opInfo;
            }
        }
        return null;
    }

    String[] getParamSignature(MBeanParameterInfo[] paramInfo) {
        String[] ret = new String[paramInfo.length];
        for (int i = 0; i < paramInfo.length; i++) {
            ret[i] = paramInfo[i].getType();
        }
        return ret;
    }

    Object[] getParams(MBeanParameterInfo[] paramInfo, Map<String, Object> localContext, Map<String, Object> processContext) {
        Object[] ret = new Object[paramInfo.length];
        try {
            for (int i = 0; i < paramInfo.length; i++) {
                MBeanParameterInfo info = paramInfo[i];
                String paramName = info.getName();
                Object value;
                if (localContext.containsKey(paramName)) {
                    value = localContext.get(paramName);
                    //      throw new Error("Parametr "+paramName+" not define");
                } else if (processContext.containsKey(paramName)) {
                    value = processContext.get(paramName);
                } else {
                    throw new Error("Parametr " + paramName + " not define");
                }
                String paramType = info.getType();
                Class cparam = Class.forName(paramType);
                if (value != null) {
                    if (cparam.isInstance(value)) {
                        ret[i] = value;
                    } else {
                        Object v = converter.convert(value, cparam);
                        ret[i] = v;
                    }
                }
            }
        } catch (ClassNotFoundException ex) {
            throw new Error(ex);
        }
        return ret;
    }

    Object excec(ObjectName objname,String operation,Object[] params,String[] signature){
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        try {
         Object ret=mbs.invoke(objname,operation,params,signature);
         return ret;
        } catch (InstanceNotFoundException | MBeanException | ReflectionException ex) {
                throw new Error(ex);
            }
    }
    
    
    @Override
    public void execute(Token token) {
        String processInstanceId = token.getProcess();

        Reference<FlowNode> ref = owner.getFlowNode(token);
        ServiceTask serviceTask = (ServiceTask) ref.resolvedReference();

        Reference<Operation> opRef = serviceTask.getOperationRef();
        Operation operation = opRef.resolvedReference();

        Interface inter = getOperationInterface(operation);
        String impl = inter.getImplementationRef();

        if (!"mbean".equals(impl)) {
            throw new Error("not supported implementation " + impl);
        }
        String name = inter.getName();
        Reference opref = operation.getInMessageRef();
        String opName = opref.getLocalPart();

        ObjectName objname = MBeanUtil.createObjectName(name);
        MBeanInfo mbBeanInfo = getMBeanInfo(objname);
        MBeanOperationInfo opInfo = getOperation(mbBeanInfo, opName);
        MBeanParameterInfo[] paramInfo = opInfo.getSignature();

        String[] signature = getParamSignature(paramInfo);

        Map localContext = new LinkedHashMap();
        Map processContext = owner.getProcessContext(processInstanceId, null);
        AssociationsResolver resolver = new AssociationsResolver(processContext, localContext,owner.getScriptingService());

        List associationList = serviceTask.getDatainputAssociations();
        resolver.resolve(associationList);

        Object[] methodparams = getParams(paramInfo, localContext, processContext);
        Object ret=excec(objname,opName,methodparams,signature);
        
        Map returnContext = new LinkedHashMap();
        returnContext.put("response", ret);
        resolver= new AssociationsResolver(returnContext,processContext,owner.getScriptingService());
        associationList= serviceTask.getDataOutputAssociations();
        resolver.resolve(associationList);

        owner.setProcessContext(processInstanceId,processContext);
        
    }

    @Override
    public void complite(Token token, Map<String, Object> contex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
