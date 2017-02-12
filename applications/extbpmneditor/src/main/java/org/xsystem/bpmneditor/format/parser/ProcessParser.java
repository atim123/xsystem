/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author tim
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
