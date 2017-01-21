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
package org.xsystem.bpm2machineservice.impl.execution;

import java.util.List;
import java.util.Map;
import org.apache.commons.jxpath.JXPathContext;
import org.xsystem.bpm2machineservice.impl.ScriptingService;
import org.xsystem.bpmn2.model.common.FormalExpression;
import org.xsystem.bpmn2.model.data.DataAssociation;

/**
 *
 * @author Andrey Timofeev
 */
public class AssociationsResolver {

    Map fromCtx;
    Map toCtx;
    ScriptingService scriptingService;

    public AssociationsResolver(Map fromCtx, Map toCtx, ScriptingService scriptingService) {
        this.fromCtx = fromCtx;
        this.toCtx = toCtx;
        this.scriptingService = scriptingService;
    }

    public void resolve(List<DataAssociation> associations) {
        JXPathContext fromdata = scriptingService.makeJXPathContext(fromCtx);

        JXPathContext todata = scriptingService.makeJXPathContext(toCtx);

        associations.stream()
                .flatMap(association -> association.getAssignment().stream())
                .forEach(assignment -> {
                    FormalExpression fromExp = (FormalExpression) assignment.getFrom();
                    String from = fromExp.getBody();
                    Object fromValue = fromdata.getValue(from);

                    FormalExpression toExp = (FormalExpression) assignment.getTo();
                    String to = toExp.getBody();

                    todata.setValue(to, fromValue);
                });

    }
}
