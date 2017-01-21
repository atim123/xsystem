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

import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.xsystem.utils.MBeanUtil;

/**
 *
 * @author Andrey Timofeev
 */
public class WebServiceHolder implements ServletContextListener{
    Bpm2MachineMBean bmnsMean=null;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName nameExec = MBeanUtil.createObjectName(Bpm2MachineService.constObjectName);
            bmnsMean=new Bpm2MachineMBean(new Bpm2MachineService());
            bmnsMean.timerStart();
            MBeanUtil.registerMBean(mbs,bmnsMean, nameExec, true);
        } catch (NotCompliantMBeanException ex) {
            System.out.println(ex);
            throw new Error(ex);
        }
        
        System.out.println("Bpm2MachineMBean-I am starting");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
      MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
      ObjectName nameExec = MBeanUtil.createObjectName(Bpm2MachineService.constObjectName);
      bmnsMean.timerStop();
      MBeanUtil.unregisterMBean(mbs, nameExec);
      System.out.println("Bpm2MachineMBean-I am stoping");
    }
    
}
