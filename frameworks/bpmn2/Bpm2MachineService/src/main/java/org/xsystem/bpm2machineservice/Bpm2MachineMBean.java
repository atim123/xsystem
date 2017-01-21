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

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;
import org.xsystem.bpm2machine.BPManager;

/**
 *
 * @author Andrey Timofeev
 */
public class Bpm2MachineMBean extends StandardMBean implements  BPManager{
   
    private BPManager theRef; 
    
   public Bpm2MachineMBean(BPManager theRef) throws NotCompliantMBeanException {
        //WARNING Uncomment the following call to super() to make this class compile (see BUG ID 122377)
        super(BPManager.class);
        this.theRef = theRef;
    }
    
    public MBeanInfo getMBeanInfo() {
        MBeanInfo mbinfo = super.getMBeanInfo();
        return new MBeanInfo(mbinfo.getClassName(),
                mbinfo.getDescription(),
                mbinfo.getAttributes(),
                mbinfo.getConstructors(),
                mbinfo.getOperations(),
               getNotificationInfo());
    } 
   
    public MBeanNotificationInfo[] getNotificationInfo() {
        return new MBeanNotificationInfo[]{};
    }

    @Override
    protected String getDescription(MBeanInfo info) {
        return "Bpm2 Service";
    }
    
    @Override
    protected String getDescription(MBeanAttributeInfo info) {
        String description = null;
        return description;
    }
    
    protected String getDescription(MBeanOperationInfo op, MBeanParameterInfo param, int sequence) {
        if (op.getName().equals("compliteUserTask")) {
            switch (sequence) {
                case 0:
                    return "";
                case 1:
                    return "";
                default:
                    return null;
            }
        } else if (op.getName().equals("takeTask")) {
            switch (sequence) {
                case 0:
                    return "";
                default:
                    return null;
            }
        }
        return null;
    }
    
    @Override
    protected String getParameterName(MBeanOperationInfo op, MBeanParameterInfo param, int sequence) {
        String opName=op.getName();
  
        if (opName.equals("startProcess")) {
            switch (sequence) {
                case 0:
                    return "processDefId";
                case 1:
                    return "context";
                default:
                    return null;
            }
        } else if (op.getName().equals("getProcessContext")) {
            switch (sequence) {
                case 0:
                    return "id";
                default:
                    return null;
            }
        
        } else if (op.getName().equals("setProcessContext")) {
            switch (sequence) {
                case 0:
                    return "id";
                case 1:    
                    return "context";
                default:
                    return null;
            }
        } else if (op.getName().equals("setProcessContextByTaskId")) {
            switch (sequence) {
                case 0:
                    return "id";
                case 1:    
                    return "context";
                default:
                    return null;
            }
        } else if (op.getName().equals("stopProcess")) {
            switch (sequence) {
                case 0:
                    return "id";
                default:
                    return null;
            }
        } else if (op.getName().equals("pushTimer")) {
            switch (sequence) {
                case 0:
                    return "id";
                default:
                    return null;
            } 
        } else if (op.getName().equals("takeTask")) {
            switch (sequence) {
                case 0:
                    return "id";
                default:
                    return null;
            }
        } else if (op.getName().equals("compliteUserTask")) {
            switch (sequence) {
                case 0:
                    return "id";
                case 1:    
                    return "context";    
                default:
                    return null;
            }
        } else if (op.getName().equals("deployDefinitions")) {
            switch (sequence) {
                case 0:
                    return "is";
                default:
                    return null;
            }
    // takeDefinitions        
        } else if (op.getName().equals("takeDefinitions")){
             switch (sequence) {
                case 0:
                    return "id";
                case 1:
                    return "os";    
                default:
                    return null;
             }    
        }else if (op.getName().equals("unDeployDefinitions")) {
            switch (sequence) {
                case 0:
                    return "id";
                default:
                    return null;
            }
        } else if (op.getName().equals("timerStart")) {
            return null;
        } else if (op.getName().equals("timerStop")) {
            return null;
        } else if (op.getName().equals("isTimerWorking")) {
            return null;
        }
    /*
        if (opName.equals("compliteUserTask")) {
            switch (sequence) {
                case 0:
                    return "param0";
                case 1:
                    return "param1";
                default:
                    return null;
            }
        } else if (op.getName().equals("takeTask")) {
            switch (sequence) {
                case 0:
                    return "param0";
                default:
                    return null;
            }
        }*/
        return null;
    }
    
    @Override
    protected String getDescription(MBeanOperationInfo info) {
        String description = null;
        MBeanParameterInfo[] params = info.getSignature();
        String[] signature = new String[params.length];
        for (int i = 0; i < params.length; i++) {
            signature[i] = params[i].getType();
        }
        String[] methodSignature;
        methodSignature = new String[]{java.lang.String.class.getName(), java.util.Map.class.getName()};
        if (info.getName().equals("compliteUserTask") && Arrays.equals(signature, methodSignature)) {
            description = "Operation exposed for management";
        }
        methodSignature = new String[]{java.lang.String.class.getName()};
        if (info.getName().equals("takeTask") && Arrays.equals(signature, methodSignature)) {
            description = "Operation exposed for management";
        }
        return description;
    }
    
    
    @Override
    public String startProcess(String processDefId, Map<String, Object> context) {
        return theRef.startProcess(processDefId,context);
    }

    @Override
    public Map getProcessContext(String contextProcessInstance) {
        return theRef.getProcessContext(contextProcessInstance);
    }

    @Override
    public void setProcessContext(String processInstance, Map context) {
         theRef.setProcessContext(processInstance,context);
    }

    @Override
    public void setProcessContextByTaskId(String taskid, Map context) {
        theRef.setProcessContextByTaskId(taskid,context);
    }

    @Override
    public void stopProcess(String procId) {
        theRef.stopProcess(procId);
    }

    @Override
    public Map takeTask(String taskId) {
       return theRef.takeTask(taskId);
    }

    @Override
    public void pushTimer(String timerId){
        theRef.pushTimer(timerId);
    }
    
    @Override
    public void compliteUserTask(String taskId, Map<String, Object> contex) {
        theRef.compliteUserTask(taskId,contex);
    }

    @Override
    public String deployDefinitions(InputStream is) {
       return theRef.deployDefinitions(is);     
    }

    @Override
    public void unDeployDefinitions(String key) {
       theRef.unDeployDefinitions(key);
    }
    
    @Override
    public void takeDefinitions(String key,OutputStream os){
        theRef.takeDefinitions(key,os);
    }
    
    @Override
    public Boolean timerStart(){
       return theRef.timerStart();
    }
    
    public Boolean timerStop(){
       return theRef.timerStop();
    }

    @Override
    public Boolean isTimerWorking(){
       return theRef.isTimerWorking();
    }
}
