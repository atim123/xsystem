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
package org.xsystem.bpmn2.formats;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import org.w3c.dom.Document;
import org.xsystem.bpmn2.model.infrastructure.Definitions;
import org.xsystem.utils.XMLUtil;

/**
 *
 * @author Andrey Timofeev
 */
public interface Composer {

    public Document compose(Definitions resource);

    public default void compose(OutputStream stream, Definitions resource) {
        XMLUtil.xmlToStreamE(compose(resource), stream);
    }

    public default void compose(File f, Definitions resource) {
        try (OutputStream os = new FileOutputStream(f)) {
            compose(os, resource);
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }
}
