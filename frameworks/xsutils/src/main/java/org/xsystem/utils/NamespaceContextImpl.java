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

import java.util.*;
import javax.xml.namespace.NamespaceContext;


/**
 *
 * @author Andrey Timofeev
 */
public class NamespaceContextImpl implements NamespaceContext{
    
    private Map<String, String> context = new HashMap<>();

    public NamespaceContextImpl() {
    }

    public NamespaceContextImpl(Map<String, String> context) {
        this.context = context;
    }

    public void set(String prfx, String uri) {
        context.put(prfx, uri);
    }
// setNameNamespaceURI(String prfx, String uri)
    public String getNamespaceURI(String prefix) {
        if (prefix == null) {
            throw new NullPointerException("Null prefix");
        }
        String res = context.get(prefix);
        return res;
    }

    // This method isn't necessary for XPath processing. but needed
    @Override
    public String getPrefix(String uri) {
        Iterator<String> itr=context.keySet().iterator();
        while (itr.hasNext()){
           String key=itr.next();
           String value=context.get(key);
           if (value.equals(uri))
              return key; 
        }
        return null;
    }

    // This method isn't necessary for XPath processing either.
    @Override
    public Iterator getPrefixes(String uri) {
        throw new UnsupportedOperationException();
    }
}
