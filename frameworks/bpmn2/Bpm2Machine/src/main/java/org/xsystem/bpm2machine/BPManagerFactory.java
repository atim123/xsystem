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
package org.xsystem.bpm2machine;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.util.Map;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import org.xsystem.utils.MBeanUtil;


/**
 *
 * @author Andrey Timofeev
 */
public class BPManagerFactory {
    public static BPManager create() {
        return new BPManagerClient();
    }

    static class BPManagerClient implements BPManager {

        ObjectName name;

        BPManagerClient() {
            name = MBeanUtil.createObjectName(BPManager.constObjectName);
        }

        MBeanServer getPlatformMBeanServer() {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            return mbs;
        }

        @Override
        public String startProcess(String processDefId, Map<String, Object> context) {
            MBeanServer mbs = getPlatformMBeanServer();
            try {
                String ret = (String) mbs.invoke(name,
                        "startProcess",
                        new Object[]{processDefId, context},
                        new String[]{
                            String.class.getName(),
                            Map.class.getName()
                        });
                return ret;
            } catch (InstanceNotFoundException | MBeanException | ReflectionException ex) {
                throw new Error(ex);
            }
        }

        @Override
        public Map getProcessContext(String contextProcessInstance) {
            MBeanServer mbs = getPlatformMBeanServer();
            try {
                Map ret = (Map) mbs.invoke(name,
                        "getProcessContext",
                        new Object[]{contextProcessInstance},
                        new String[]{
                            String.class.getName()
                        });
                return ret;
            } catch (InstanceNotFoundException | MBeanException | ReflectionException ex) {
                throw new Error(ex);
            }
        }

        @Override
        public void setProcessContext(String contextProcessInstance, Map context) {
            MBeanServer mbs = getPlatformMBeanServer();
            try {
                mbs.invoke(name,
                        "setProcessContext",
                        new Object[]{contextProcessInstance, context},
                        new String[]{
                            String.class.getName(),
                            Map.class.getName()
                        });
            } catch (InstanceNotFoundException | MBeanException | ReflectionException ex) {
                throw new Error(ex);
            }
        }

        @Override
        public void setProcessContextByTaskId(String taskid, Map context) {
            MBeanServer mbs = getPlatformMBeanServer();
            try {
                mbs.invoke(name,
                        "setProcessContextByTaskId",
                        new Object[]{taskid, context},
                        new String[]{
                            String.class.getName(),
                            Map.class.getName()
                        });
            } catch (InstanceNotFoundException | MBeanException | ReflectionException ex) {
                throw new Error(ex);
            }
        }

        @Override
        public void pushTimer(String timerId) {
            MBeanServer mbs = getPlatformMBeanServer();
            try {
                mbs.invoke(name,
                        "pushTimer",
                        new Object[]{timerId},
                        new String[]{
                            String.class.getName()
                        });
            } catch (InstanceNotFoundException | MBeanException | ReflectionException ex) {
                throw new Error(ex);
            }
        }

        @Override
        public void stopProcess(String procId) {
            MBeanServer mbs = getPlatformMBeanServer();
            try {
                mbs.invoke(name,
                        "stopProcess",
                        new Object[]{procId},
                        new String[]{
                            String.class.getName()
                        });
            } catch (InstanceNotFoundException | MBeanException | ReflectionException ex) {
                throw new Error(ex);
            }
        }

        @Override
        public Map takeTask(String taskId) {
            MBeanServer mbs = getPlatformMBeanServer();
            try {
                Map ret = (Map) mbs.invoke(name,
                        "takeTask",
                        new Object[]{taskId},
                        new String[]{
                            String.class.getName()
                        });
                return ret;
            } catch (InstanceNotFoundException | MBeanException | ReflectionException ex) {
                throw new Error(ex);
            }
        }

        @Override
        public void compliteUserTask(String taskId, Map<String, Object> context) {
            MBeanServer mbs = getPlatformMBeanServer();
            // System.out.println("Proxy compliteUserTask");
            try {
                mbs.invoke(name,
                        "compliteUserTask",
                        new Object[]{taskId, context},
                        new String[]{
                            String.class.getName(),
                            Map.class.getName()
                        });
            } catch (InstanceNotFoundException | MBeanException | ReflectionException ex) {
                throw new Error(ex);
            }
        }

        @Override
        public String deployDefinitions(InputStream is) {
            MBeanServer mbs = getPlatformMBeanServer();
            try {
                String ret = (String) mbs.invoke(name,
                        "deployDefinitions",
                        new Object[]{is},
                        new String[]{
                            InputStream.class.getName()
                        });
                return ret;
            } catch (InstanceNotFoundException | MBeanException | ReflectionException ex) {
                throw new Error(ex);
            }
        }

        @Override
        public void takeDefinitions(String key,OutputStream os){
            MBeanServer mbs = getPlatformMBeanServer();
            try {
                mbs.invoke(name,
                        "takeDefinitions",
                        new Object[]{key,os},
                        new String[]{
                            String.class.getName(),
                            OutputStream.class.getName()
                        });
        
            } catch (InstanceNotFoundException | MBeanException | ReflectionException ex) {
                throw new Error(ex);
            }
        };
        
        @Override
        public void unDeployDefinitions(String key) {
            MBeanServer mbs = getPlatformMBeanServer();
            try {
                mbs.invoke(name,
                        "unDeployDefinitions",
                        new Object[]{key},
                        new String[]{
                            String.class.getName()});
            } catch (InstanceNotFoundException | MBeanException | ReflectionException ex) {
                throw new Error(ex);
            }
        }

        @Override
        public Boolean timerStart() {
            MBeanServer mbs = getPlatformMBeanServer();
            try {
                Boolean ret = (Boolean) mbs.invoke(name,
                        "timerStart",
                        new Object[]{},
                        new String[]{}
                );
                return ret;
            } catch (InstanceNotFoundException | MBeanException | ReflectionException ex) {
                throw new Error(ex);
            }
        }

        @Override
        public Boolean timerStop() {
            MBeanServer mbs = getPlatformMBeanServer();
            try {
                Boolean ret = (Boolean) mbs.invoke(name,
                        "timerStop",
                        new Object[]{},
                        new String[]{}
                );
                return ret;
            } catch (InstanceNotFoundException | MBeanException | ReflectionException ex) {
                throw new Error(ex);
            }
        }

        @Override
        public Boolean isTimerWorking() {
            MBeanServer mbs = getPlatformMBeanServer();
            try {
                Boolean ret = (Boolean) mbs.invoke(name,
                        "isTimerWorking",
                        new Object[]{},
                        new String[]{}
                );
                return ret;
            } catch (InstanceNotFoundException | MBeanException | ReflectionException ex) {
                throw new Error(ex);
            }
        }
    }
}
