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
package org.xsystem.bpm2machineservice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.w3c.dom.Document;
import org.xsystem.bpm2machine.BPManager;
import org.xsystem.bpm2machineservice.impl.DeployService;
import org.xsystem.bpm2machineservice.impl.ExecutionService;
import org.xsystem.bpm2machineservice.impl.MachineConfig;
import org.xsystem.bpmn2.formats.Parser;
import org.xsystem.bpmn2.formats.xml.XMLParser3;
import org.xsystem.bpmn2.model.infrastructure.Definitions;
import org.xsystem.sql2.page.PAGE;
import org.xsystem.sql2.page.PageHelper;
import org.xsystem.system.sql.JDBCTransationManager2;
import org.xsystem.utils.Auxilary;
import org.xsystem.utils.XMLUtil;


/**
 *
 * @author Andrey Timofeev
 */
public class Bpm2MachineService implements BPManager, PageHelper{
    Map<String, Definitions> definitions = new HashMap();
    Map<String, org.xsystem.bpmn2.model.process.Process> proceses = new HashMap();
    String definitionsKey = "";

    Scheduler scheduler;

    public Bpm2MachineService() {
        scheduler = new Scheduler();
    }

    //------------------CASH----------------------------------------------------
    private Definitions _getDefinitions(String definitionsId) {
        if (!definitions.containsKey(definitionsId)) {
            String content = (String) rowValue("loadDefinitions", PAGE.params("id", definitionsId), "content");
            Parser parser = new XMLParser3();
            InputStream input = null;
            try {
                input = new ByteArrayInputStream(content.getBytes("UTF-8"));
                Definitions def = parser.parse(input);
                definitions.put(definitionsId, def);
            } catch (Exception ex) {
                throw new Error(ex);
            } finally {
                Auxilary.close(input);
            }
        }
        return definitions.get(definitionsId);
    }

    synchronized public Definitions getDefinitions(String definitionsId) {
        String testKey = Session.getDefinitionsKey();
        if (!definitionsKey.equals(testKey)) {
            definitions.clear();
            proceses.clear();
        };
        Definitions ret = _getDefinitions(definitionsId);
        definitionsKey = testKey;
        return ret;
    }

    synchronized public org.xsystem.bpmn2.model.process.Process getProcess(String processId) {
        String testKey = Session.getDefinitionsKey();
        if (!definitionsKey.equals(testKey)) {
            definitions.clear();
            proceses.clear();
        }
        if (!proceses.containsKey(processId)) {
            String definitionsId = (String) rowValue("getProcessDefinitionId", PAGE.params("id", processId), "definitionid");
            Definitions def = _getDefinitions(definitionsId);
            org.xsystem.bpmn2.model.process.Process proc = def.findProcess(processId);
            if (proc != null) {
                proceses.put(processId, proc);
            }
        }
        definitionsKey = testKey;
        org.xsystem.bpmn2.model.process.Process ret = proceses.get(processId);
        return ret;
    }

    //----BPManager-------------------------------------------------------------
    @Override
    public String startProcess(String processDefId, Map<String, Object> contex) {
        Session session = Session.create();
        boolean isError = true;

        try {
            ExecutionService service = new ExecutionService(this);
            String ret = service.startProcess(processDefId, contex);
            session.close();
            isError = false;
            return ret;
        } finally {
            if (isError) {
                session.abort();
            }
        }
    }

    @Override
    public Map getProcessContext(String contextProcessInstance) {
        Session session = Session.create();
        boolean isError = true;

        try {
            ExecutionService service = new ExecutionService(this);
            Map ret = service.getProcessContext(contextProcessInstance, null);
            session.close();
            isError = false;
            return ret;
        } finally {
            if (isError) {
                session.abort();
            }
        }
    }

    @Override
    public void setProcessContext(String contextProcessInstance, Map context) {
        Session session = Session.create();
        boolean isError = true;

        try {
            ExecutionService service = new ExecutionService(this);
            service.setProcessContext(contextProcessInstance, context);
            session.close();
            isError = false;

        } finally {
            if (isError) {
                session.abort();
            }
        }
    }

    @Override
    public void setProcessContextByTaskId(String taskid, Map context) {
        Session session = Session.create();
        boolean isError = true;

        try {
            ExecutionService service = new ExecutionService(this);
            service.setProcessContextByTaskId(taskid, context);
            session.close();
            isError = false;

        } finally {
            if (isError) {
                session.abort();
            }
        }

    }

    @Override
    public void pushTimer(String timerId) {
        Session session = Session.create();
        boolean isError = true;

        try {
            ExecutionService service = new ExecutionService(this);
            service.pushTimer(timerId);
            session.close();
            isError = false;

        } finally {
            if (isError) {
                session.abort();
            }
        }

    }

    @Override
    public void stopProcess(String procId) {
        Session session = Session.create();
        boolean isError = true;

        try {
            ExecutionService service = new ExecutionService(this);
            service.stopProcess(procId);
            session.close();
            isError = false;

        } finally {
            if (isError) {
                session.abort();
            }
        }

    }

    @Override
    public Map takeTask(String taskId) {
        Session session = Session.create();
        boolean isError = true;

        try {
            ExecutionService service = new ExecutionService(this);
            Map ret = service.takeTask(taskId);
            session.close();

            isError = false;
            return ret;
        } finally {
            if (isError) {
                session.abort();
            }
        }

    }

    @Override
    public void compliteUserTask(String taskId, Map<String, Object> contex) {
        Session session = Session.create();
        boolean isError = true;

        try {
            ExecutionService service = new ExecutionService(this);
            service.compliteUserTask(taskId, contex);
            session.close();

            isError = false;

        } finally {
            if (isError) {
                session.abort();
            }
        }
    }

    @Override
    public String deployDefinitions(InputStream is) {
        Session session = Session.create();
        boolean isError = true;

        try {
            DeployService service = new DeployService();
            String ret = service.deployDefinitions(is);
            session.close();

            isError = false;
            return ret;
        } finally {
            if (isError) {
                session.abort();
            }
        }
    }

    @Override
    public void takeDefinitions(String key,OutputStream os){
        try {
            String content = (String) rowValue("loadDefinitions", PAGE.params("id",key ), "content");
            byte[] b= content.getBytes("UTF-8");
            os.write(b);
        } catch (UnsupportedEncodingException ex) {
            throw new Error(ex);
        } catch (IOException ex) {
            throw new Error(ex);
        }
            
         
    }
    
    @Override
    public void unDeployDefinitions(String key) {
        Session session = Session.create();
        boolean isError = true;

        try {
            DeployService service = new DeployService();
            service.unDeployDefinitions(key);
            session.close();

            isError = false;

        } finally {
            if (isError) {
                session.abort();
            }
        }
    }

    @Override
    public Boolean isTimerWorking() {
        Boolean ret = scheduler.isRunning();
        return ret;
    }

    @Override
    public Boolean timerStart() {
        scheduler.start();
        Boolean ret = isTimerWorking();
        return ret;
    }

    @Override
    public Boolean timerStop() {
        scheduler.stop();
        Boolean ret = isTimerWorking();
        return ret;
    }

// 
//----------PageHelper---------------------------------------------------------   
    @Override
    public Connection getConnection() {
        return JDBCTransationManager2.getConnection();
    }

    @Override
    public Document getDocument() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        return XMLUtil.getDocumentFromResourcesE(MachineConfig.cashxml, classLoader);
    }

    //-------------------------------------------------------------------------
    class Scheduler implements Runnable {

        ScheduledFuture scheduledFuture = null;
        ScheduledExecutorService service;

        Scheduler() {
            service = null;Executors.newSingleThreadScheduledExecutor();
        }

        boolean _isRunning() {
            if (scheduledFuture != null) {
                return true;
            }
            return false;
        }

        synchronized Boolean isRunning() {
            return _isRunning();
        }

        synchronized void start() {
            if (!_isRunning()) {
                service=Executors.newSingleThreadScheduledExecutor();
                scheduledFuture = service.scheduleWithFixedDelay(this, 0, 1 * 60, TimeUnit.SECONDS);
            }
        }

        synchronized void stop() {
            if (_isRunning()) {
                scheduledFuture.cancel(true);
                service.shutdown();
                scheduledFuture = null;
                service=null;
            }
        }

        List<String> getTimerList() {
            Session session = Session.create();
            boolean isError = true;

            try {
                List<String> ret = stream("timerlist", PAGE.params())
                        .map(row -> (String) row.get("id"))
                        .collect(Collectors.toList());
                session.close();
                return ret;
            } catch (Throwable err) {
                err.printStackTrace();
                session.abort();
                return null;
            }
        }

        @Override
        public void run() {

            List<String> tlist = getTimerList();
            if (tlist != null) {
                for (String id : tlist) {
                    try {
                        pushTimer(id);
                    } catch (Throwable err) {
                        err.printStackTrace();
                    }
                }
            }
        }
    }
}
