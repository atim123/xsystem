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
package org.xsystem.bpm2machineservice.impl;

/**
 *
 * @author Andrey Timofeev
 */
public class Token {

    private String id;
    private String parent;
    private boolean isactive;
    private String process;
    private String processDefinition;

    private String activity;
    private Integer loopcounter;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public boolean isIsactive() {
        return isactive;
    }

    public void setIsactive(boolean isactive) {
        this.isactive = isactive;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getProcessDefinition() {
        return processDefinition;
    }

    public void setProcessDefinition(String processDefinition) {
        this.processDefinition = processDefinition;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public Integer getLoopcounter() {
        return loopcounter;
    }

    public void setLoopcounter(Integer loopcounter) {
        this.loopcounter = loopcounter;
    }

    public String toString() {
        String ret = "tokenId->" + id
                + " parentTokenId->" + parent
                + " isActive->" + isactive
                + " processInstance->" + process
                + " nodeId->" + activity
                + " process->" + processDefinition
                + " loopCounter->" + loopcounter;
        return ret;
    }

    public static class Builder {

        private String id;
        private String parent;
        private boolean isactive = true;
        private String process;
        private String processDefinition;

        private String activity;
        private Integer loopcounter;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder parent(String parent) {
            this.parent = parent;
            return this;
        }

        public Builder isactive(boolean isactive) {
            this.isactive = isactive;
            return this;
        }

        public Builder process(String process) {
            this.process = process;
            return this;
        }

        public Builder processDefinition(String processDefinition) {
            this.processDefinition = processDefinition;
            return this;
        }

        public Builder activity(String activity) {
            this.activity = activity;
            return this;
        }

        public Builder loopcounter(Integer loopcounter) {
            this.loopcounter = loopcounter;
            return this;
        }

        public Token build() {
            return new Token(this);
        }
    }

    private Token(Builder builder) {
        this.id = builder.id;
        this.parent = builder.parent;
        this.isactive = builder.isactive;
        this.activity = builder.activity;
        this.process = builder.process;
        this.processDefinition = builder.processDefinition;
        this.loopcounter = builder.loopcounter;
    }
    
}
