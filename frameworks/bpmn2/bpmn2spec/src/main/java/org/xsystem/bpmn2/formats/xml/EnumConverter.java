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
package org.xsystem.bpmn2.formats.xml;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

/**
 *
 * @author Andrey Timofeev
 */
public class EnumConverter implements Converter {

    @Override
    public <T> T convert(Class<T> tClass, Object o) {
        String enumValName = (String) o;
        Enum[] enumConstants = (Enum[]) tClass.getEnumConstants();

        for (Enum enumConstant : enumConstants) {
            if (enumConstant.name().equals(enumValName)) {
                return (T) enumConstant;
            }
        }

        throw new ConversionException(String.format("Failed to convert %s value to %s class", enumValName, tClass.toString()));
    }

}
