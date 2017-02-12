/* 
 * Copyright (C) 2017 Andrey Timofeev
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.xsystem.bpmneditor.format.parser;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.xsystem.bpmn2.model.common.CorrelationPropertyBinding;
import org.xsystem.bpmn2.model.common.CorrelationSubscription;
import org.xsystem.bpmn2.model.common.FormalExpression;
import org.xsystem.bpmn2.model.system.Reference;
import org.xsystem.bpmn2.modelimpl.common.CorrelationPropertyBindingImpl;
import org.xsystem.bpmn2.modelimpl.common.CorrelationSubscriptionImpl;
import org.xsystem.bpmn2.modelimpl.common.FormalExpressionImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;
import org.xsystem.bpmn2.modelimpl.process.ProcessImpl;
import org.xsystem.bpmneditor.format.GoParser2;

/**
 *
 * @author Andrey Timofeev
 */
public class ProcessParser extends AbstractParser{

    public ProcessParser(GoParser2 owner) {
        super(owner);
    }

    

    void buildSubscription(CorrelationSubscription subscription, Map<String, List> subscriptionsDef) {
        DefinitionsImpl definitions = owner.getDefinitions();

        Iterator<String> iter = subscriptionsDef.keySet().iterator();

        while (iter.hasNext()) {
            String key = iter.next();
            List<Map<String, String>> subscriptionDef = subscriptionsDef.get(key);
            subscriptionDef.forEach(action -> {
                CorrelationPropertyBinding correlationPropertyBinding = new CorrelationPropertyBindingImpl(definitions);
                String[] keys = new String[1];
                action.keySet().toArray(keys);
                String strProp = keys[0];
                Reference propRef = definitions.createReference(strProp);
                correlationPropertyBinding.setCorrelationPropertyRef(propRef);
                String dataBody = action.get(strProp);
                FormalExpression data = new FormalExpressionImpl(definitions);
                data.setBody(dataBody);
                correlationPropertyBinding.setDataPath(data);
                subscription.getCorrelationPropertyBinding().add(correlationPropertyBinding);

            });
        }
    }

    public void parse(Map<String, Object> modelData) {
        DefinitionsImpl definitions = owner.getDefinitions();
        org.xsystem.bpmn2.model.process.Process process = new ProcessImpl(definitions);

        String id = (String) modelData.get("key");
        process.setId(id);
        String name = (String) modelData.get("text");
        process.setName(name);
        owner.toCash(id, process);
        definitions.getRootElements().add(process);

        Map<String, List> subscriptions = (Map) modelData.get("subscriptions");
        if (subscriptions != null && !subscriptions.isEmpty()) {
            CorrelationSubscription subscription = new CorrelationSubscriptionImpl(definitions);
            process.getCorrelationSubscriptions().add(subscription);
            Reference ref = owner.getCorrelationKeyRef();
            subscription.setCorrelationKeyRef(ref);
            buildSubscription(subscription, subscriptions);
        };

    }
}
