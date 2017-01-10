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
package org.xsystem.sql2.dml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.xsystem.utils.Auxilary;

/**
 *
 * @author Andrey Timofeev
 */
public interface ParamsHelper {
    public static Map<String, Object> params(Object... args) {
        return Auxilary.newMap(args);
    }
    
    public static List list(Object... args) {
       List res = new ArrayList();
        if (args != null) {
            res.addAll(Arrays.asList(args));
        }
        return res;
    }
}
