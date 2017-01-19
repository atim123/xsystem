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
import java.util.Map;

/**
 *
 * @author Andrey Timofeev
 */
public interface BPManager {

    public static final String constObjectName = "org.xsystem.bpm2machineservice:type=BPManager";

    public String startProcess(String processDefId, Map<String, Object> contex);

    public Map getProcessContext(String contextProcessInstance);

    public void setProcessContext(String contextProcessInstance, Map context);

    public void setProcessContextByTaskId(String taskid, Map context);

    public void stopProcess(String procId);

    void pushTimer(String timerId);

    public Map takeTask(String taskId);

    public void compliteUserTask(String taskId, Map<String, Object> contex);

    public String deployDefinitions(InputStream is);

    public void unDeployDefinitions(String key);

    public void takeDefinitions(String key, OutputStream os);

    public Boolean timerStart();

    public Boolean timerStop();

    public Boolean isTimerWorking();

    public static class TaskNotFound extends RuntimeException {

        public TaskNotFound(String message) {
            super(message);
        }
    }
}
