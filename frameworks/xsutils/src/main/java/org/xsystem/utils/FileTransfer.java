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

import java.io.UnsupportedEncodingException;

/**
 *
 * @author Andrey Timofeev
 * 
 * Не совсем верная структура для передачи файлов
 */
public class FileTransfer {
    byte[] data = null;
    String fileName = null;
    String contentType = null;
    String fileType = null;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setDataAsString(String data) {
        if (data == null) {
            this.data = null;
            return; 
        }
        try {
            this.data = data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new Error(ex);
        }
    }

    public String getDataAsString() {
        if (this.data == null) {
            return null;
        }
        try {
            String ret = new String(this.data, "UTF-8");
            return ret;
        } catch (UnsupportedEncodingException ex) {
            throw new Error(ex);
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Integer getSize() {
        Integer ret = null;
        if (data != null) {
            ret = data.length;
        }
        return ret;
    }
}
