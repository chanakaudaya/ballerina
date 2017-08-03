/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ballerinalang.nativeimpl.actions.core;

import org.ballerinalang.bre.Context;
import org.ballerinalang.model.types.TypeEnum;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.natives.annotations.Argument;
import org.ballerinalang.natives.annotations.Attribute;
import org.ballerinalang.natives.annotations.BallerinaAction;
import org.ballerinalang.natives.annotations.BallerinaAnnotation;
import org.ballerinalang.natives.connectors.AbstractNativeAction;
import org.ballerinalang.natives.connectors.BallerinaConnectorManager;
import org.osgi.service.component.annotations.Component;
import org.wso2.carbon.messaging.CarbonMessageProcessor;
import org.wso2.carbon.messaging.ClientConnector;

import java.util.ServiceLoader;

/**
 * {@code Init} is the Init action implementation of the Core Connector.
 *
 * @since 0.92
 */
@BallerinaAction(
        packageName = "ballerina.net.core",
        actionName = "<init>",
        connectorName = "ClientConnector",
        args = {@Argument(name = "c", type = TypeEnum.CONNECTOR)
        }
)
@BallerinaAnnotation(annotationName = "Description", attributes = { @Attribute(name = "value",
                                                       value = "The init action implementation for core connector.") })
@Component(
        name = "action.net.core.init",
        immediate = true,
        service = AbstractNativeAction.class)
public class Init extends AbstractNativeAction {

    @Override
    public BValue execute(Context context) {
        if (BallerinaConnectorManager.getInstance().
                getClientConnector("core") == null) {
            CarbonMessageProcessor carbonMessageProcessor = BallerinaConnectorManager.getInstance()
                    .getMessageProcessor();
            ServiceLoader<ClientConnector> clientConnectorLoader = ServiceLoader.load(ClientConnector.class);
            clientConnectorLoader.forEach((clientConnector) -> {
                clientConnector.setMessageProcessor(carbonMessageProcessor);
                BallerinaConnectorManager.getInstance().registerClientConnector(clientConnector);
            });
        }
        return null;
    }

    @Override
    public boolean isNonBlockingAction() {
        return false;
    }
}
