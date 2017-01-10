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

import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;


/**
 *
 * @author Andrey Timofeev
 */
public class DateUtil {
   public static String nowISODateAsString() {
        Date d = new Date();
        long date = d.getTime();
        String result = ISODateTimeFormat.dateTimeNoMillis().print(date);
        return result;
    }

    public static String plusISODateAsString(String sd, long mc) {
        DateTime dt = DateTime.parse(sd);
        DateTime dt2 = dt.plus(mc);
        long l = dt2.getMillis();
        String result = ISODateTimeFormat.dateTimeNoMillis().print(l);
        return result;
    }

    public static String printISODate(Date d) {
        String result = null;
        if (d != null) {
            long l = d.getTime();
            result = ISODateTimeFormat.dateTimeNoMillis().print(l);
        }
        return result;
    }
    
    public static Date isoDateFromString(String s){
     // DateTimeFormatter formater=(isms)?ISODateTimeFormat.
      long l=  ISODateTimeFormat.dateTimeNoMillis().parseMillis(s);
      Date ret=new Date(l);
      return ret;
    } 
}
