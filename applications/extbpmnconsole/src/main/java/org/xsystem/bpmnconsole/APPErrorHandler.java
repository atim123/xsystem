/*
 * Copyright (C) 2017 Andrey Timofeev
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.xsystem.bpmnconsole;

import java.util.Map;
import javax.management.RuntimeMBeanException;
//import org.xsystem.bpm2machine.BPManager;
import org.xsystem.sql2.http.ErrorHandler;
import org.xsystem.utils.Auxilary;

/**
 *
 * @author Andrey Timofeev
 */
public class APPErrorHandler implements ErrorHandler {

    Map mbeanErrorHandler(RuntimeMBeanException runtimeMBeanException) {
        RuntimeException targetException = runtimeMBeanException.getTargetException();
        if (targetException != null) {
            String cn = targetException.getClass().getCanonicalName();
            switch (cn){
              case "org.xsystem.bpm2machine.BPManager.TaskNotFound":
                 return Auxilary.makeJsonSuccess("apperror","BPMN-TASK-NOTFOUND");
            }
        }
        return null;
    }

    @Override
    public Object handler(Throwable e) {
        e.printStackTrace();
        Map errRet = Auxilary.makeJsonThrowable(e);
        if (e instanceof RuntimeMBeanException) {
            RuntimeMBeanException runtimeMBeanException = (RuntimeMBeanException) e;
            Map ret=mbeanErrorHandler(runtimeMBeanException);
            if (ret!=null){
                errRet=ret;
            }
           
        }
        return errRet;
    }

}
