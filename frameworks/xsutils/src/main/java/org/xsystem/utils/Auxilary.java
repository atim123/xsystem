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

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Andrey Timofeev
 * Набор вспомогательных методов Многие из них просто перебивают стандартные 
 * исключения
 */
public class Auxilary {
    
    /**
     * Ничего не делает использцется исключительно в целях отладки
     * @param o
     */
    public static void XXX(Object o) {
        Object x = o;
    }
    
    
    public static Map newMap(Object... args) {
        Map res = new LinkedHashMap();
        if (args == null) {
            return res;
        }
        int size = args.length / 2;
        for (int i = 0; i < size; i++) {
            int idx = 2 * i;
            Object key = args[idx];
            Object value = args[idx + 1];
            res.put(key, value);
        }
        return res;
    }
    /**
     * 
     * @param content
     * @return
     */
    public static String toUTF8String(byte[] content) {
        try {
            String ret = new String(content, "UTF-8");
            return ret;
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }
    
    
    public static Connection close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
        return null;
    }

    public static ResultSet close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
        return null;
    }

    public static PreparedStatement close(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
        return null;
    }

    public static PreparedStatement close(Statement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
        return null;
    }

    public static Closeable close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        return null;
    }
    
    
    public static InputStream loadResource(String path) {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream stream = classLoader.getResourceAsStream(path);
        return stream;
    }

    public static InputStream loadResource(String path,ClassLoader classLoader) {

        InputStream stream = classLoader.getResourceAsStream(path);
        return stream;
    }
    
    public static Boolean isEmptyOrNull(String v) {
        if (v == null) {
            return true;
        }
        boolean ret = v.isEmpty();
        return ret;
    }

    
    public static Boolean isEmptyOrNull(Collection v) {
        if (v == null) {
            return true;
        }
        boolean ret = v.isEmpty();
        return ret;
    }
    
    public static byte[] decodeBase64(String src) {
       byte[] ret=Base64.getDecoder().decode(src);
       return ret;
    }
    
    public static String throwableToString(Throwable t) {
        OutputStream out = new ByteArrayOutputStream();
        try {
            PrintStream strm = new PrintStream(out);
            try {
                t.printStackTrace(strm);
                String rez = out.toString();//strm.toString();
                return rez;
            } finally {
                try {
                    strm.close();
                } catch (Exception ex) {
                };
            }
        } finally {
            try {
                out.close();
            } catch (Exception ex) {
            };
        }
    }
    
    public static Map makeJsonSuccess() {
        return makeJsonSuccess(null, null);
    }

    public static String makeJsonSuccessAsString(String resultName, String jsonRes) {
        char q = '"';
        String json = "{" + q + "success" + q + ":true,"
                + q + resultName + q + ":" + jsonRes
                + "}";
        return json;
    }

    public static String makeJsonSuccessAsString() {
        char q = '"';
        String json = "{" + q + "success" + q + ":true}";
        return json;
    }

    public static Map makeJsonSuccess(String resultName, Object result) {
        Map ret = new LinkedHashMap();
        ret.put("success", new Boolean(true));

        if (resultName != null) {
            ret.put(resultName, result);
        }
        return ret;
    }

    public static Map makeJsonSuccessMap(Map<String, Object> responce) {
        Map ret = new LinkedHashMap();
        ret.put("success", true);
        Iterator<String> itr = responce.keySet().iterator();
        while (itr.hasNext()) {
            String key = itr.next();
            ret.put(key, responce.get(key));
        }
        return ret;
    }

    public static Map makeJsonSuccess(Object result) {
        Map ret = new LinkedHashMap();
        ret.put("success", true);
        if (result != null) {
            ret.put("success", result);
        } else {
            ret.put("success", "true");
        }
        return ret;
    }
    
    public static Map makeJsonError(String msg) {
        Map ret = new LinkedHashMap();
        ret.put("success", new Boolean(false));
        ret.put("error", msg);
        return ret;
    }
    
    public static Map makeJsonError(String msg, Integer code) {
        Map ret = new LinkedHashMap();
        ret.put("success", new Boolean(false));
        Map error = new LinkedHashMap();
        error.put("message", msg);
        error.put("code", code);
        ret.put("error", error);
        return ret;
    }
    
    public static Map makeJsonThrowable(Throwable e) {
        Map ret = new LinkedHashMap();
        ret.put("success", new Boolean(false));
        ret.put("error",e.getMessage());
        return ret;
    }
    
    public static String getPathExtention(String filename) {
        int pathPos = filename.lastIndexOf(".");
        if (pathPos == -1) {
            return filename;
        }
        String ret = filename.substring(0, pathPos);
        return ret;
    }

    public static String getFileExtention(String filename) {
        int dotPos = filename.lastIndexOf(".") + 1;
        if (dotPos == 0) {
            return "";
        }
        return filename.substring(dotPos);
    }
    
    public static byte[] readBinaryFile(File f) throws IOException {
        byte[] ret;
        FileInputStream fs = new FileInputStream(f);
        try {
            ret = IOUtils.toByteArray(fs);

        } finally {
            Auxilary.close(fs);
        }
        return ret;
    }
    
    public static <T> T getJndiResource(String path, Class<T> type) {
        try {
            InitialContext cxt = new InitialContext();

            Object ret = cxt.lookup(path);

            return (T) ret;
        } catch (NamingException ex) {
            throw new Error(ex);
        }
    }

    public static java.sql.Connection getJndiJDBConnection(String path) {
        try {
            javax.sql.DataSource ds = getJndiResource(path, javax.sql.DataSource.class);
            return ds.getConnection();
        } catch (SQLException ex) {
            throw new Error(ex);
        }
    }
}
