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

package org.ballerinalang.nativeimpl.actions.cb;

import org.ballerinalang.bre.Context;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.nativeimpl.actions.http.Constants;
import org.ballerinalang.natives.connectors.AbstractNativeAction;
import org.ballerinalang.natives.connectors.BalConnectorCallback;
import org.ballerinalang.natives.connectors.BallerinaConnectorManager;
import org.ballerinalang.util.exceptions.BallerinaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.messaging.CarbonMessage;
import org.wso2.carbon.messaging.exceptions.ClientConnectorException;


/**
 * {@code AbstractHTTPAction} is the base class for all CB Connector Actions.
 *
 * @since 0.92
 */
public abstract class AbstractCBAction extends AbstractNativeAction {

    private static final Logger logger = LoggerFactory.getLogger(AbstractCBAction.class);


    protected BValue executeAction(Context context, CarbonMessage message) {

        try {
            BalConnectorCallback balConnectorCallback = new BalConnectorCallback(context);
            org.wso2.carbon.messaging.ClientConnector clientConnector = BallerinaConnectorManager.getInstance().
                    getClientConnector("cb");

            if (clientConnector == null) {
                throw new BallerinaException("Http client connector is not available");
            }
            clientConnector.send(message, balConnectorCallback);
            return balConnectorCallback.getValueRef();
        } catch (ClientConnectorException e) {
            throw new BallerinaException("Failed to send the message to an endpoint ", context);
        } catch (Throwable e) {
            throw new BallerinaException(e.getMessage(), context);
        }
    }

    void executeNonBlockingAction(Context context, CarbonMessage message, BalConnectorCallback balConnectorCallback)
            throws ClientConnectorException {
        balConnectorCallback.setNonBlockingExecution(true);
        org.wso2.carbon.messaging.ClientConnector clientConnector = BallerinaConnectorManager.getInstance().
                getClientConnector(Constants.PROTOCOL_HTTP);

        if (clientConnector == null) {
            throw new BallerinaException("Http client connector is not available");
        }

        Object sourceHandler = message.getProperty(Constants.SRC_HANDLER);
        if (sourceHandler != null) {
            context.setProperty(Constants.SRC_HANDLER, sourceHandler);
        } else {
            message.setProperty(Constants.SRC_HANDLER, context.getProperty(Constants.SRC_HANDLER));
        }

        clientConnector.send(message, balConnectorCallback);
    }

    @Override
    public boolean isNonBlockingAction() {
        return true;
    }



}
