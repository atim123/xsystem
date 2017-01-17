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
package org.xsystem.bpmn2.modelimpl.gateways;

import org.xsystem.bpmn2.model.gateways.Gateway;
import org.xsystem.bpmn2.model.gateways.GatewayDirection;
import org.xsystem.bpmn2.modelimpl.common.FlowNodeImpl;
import org.xsystem.bpmn2.modelimpl.infrastructure.DefinitionsImpl;


/**
 *
 * @author Andrey Timofeev
 */
public class GatewayImpl extends FlowNodeImpl implements Gateway {

    GatewayDirection gatewayDirection = GatewayDirection.Unspecified;
    /*
     * Unspecified: В данном случае ограничения отсутствуют. Шлюз МОЖЕТ иметь
     * любое количество входящих и исходящих Потоков Операций. 
     * 
     * Converging: Шлюз МОЖЕТ иметь любое количество входящих Потоков Операций,
     * однако, количество исходящих от данного Шлюза Потоков Операций ДОЛЖНО БЫТЬ не
     * более одного . 
     * 
     * Diverging: Шлюз МОЖЕТ иметь любое количество исходящих Потоков Операций,
     * однако, количество входящих Потоков Операций  ДОЛЖНО БЫТЬ не более одного .
     * 
     * Mixed: Шлюз соединен с множеством
     * исходящих и входящих Потоков Операций.
     */

       @Override
    public String TypeName(){
        return "gateway";
    }
    
    public GatewayImpl(DefinitionsImpl definitions) {
        super(definitions);
    }

    public GatewayDirection getGatewayDirection() {
        return gatewayDirection;
    }

    public void setGatewayDirection(GatewayDirection gatewayDirection) {
        this.gatewayDirection = gatewayDirection;
    }

    
}
