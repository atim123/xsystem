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
package org.xsystem.bpm2machineservice.impl.scripting;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Andrey Timofeev
 */
public class CalendarHelper {
    public static Date now(){
        Calendar c=Calendar.getInstance();
        Date ret=c.getTime();
        return ret;    
    }
    
    public static Date addNow(int field, int amount){
        Calendar c=Calendar.getInstance();
        c.add(field, amount);
        Date ret=c.getTime();
        return ret;
    }
}
