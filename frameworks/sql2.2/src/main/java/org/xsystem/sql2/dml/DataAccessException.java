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

import java.sql.SQLException;

/**
 *
 * @author Andrey Timofeev
 */
public class DataAccessException extends RuntimeException{
    public int errorCode;
    public String state;

    public DataAccessException() {
    }

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
        getCodeAndState(cause);
    }

    private void getCodeAndState(Throwable cause) {
        if (cause instanceof SQLException) {
            errorCode = ((SQLException) cause).getErrorCode();
            state = ((SQLException) cause).getSQLState();
        } else {
            errorCode = -1;
            state = cause.getMessage();
        }
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getState() {
        return state;
    }

    public DataAccessException(Throwable cause) {
        super(cause);
    }

    public DataAccessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String toString() {
        return new StringBuilder(getClass().getName()).append('[').append(errorCode).append(',').append(state).append("]: ").append(getLocalizedMessage()).toString();
    }
}
