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
import org.ballerinalang.model.types.TypeEnum;
import org.ballerinalang.model.values.BConnector;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.natives.annotations.Argument;
import org.ballerinalang.natives.annotations.Attribute;
import org.ballerinalang.natives.annotations.BallerinaAction;
import org.ballerinalang.natives.annotations.BallerinaAnnotation;
import org.ballerinalang.natives.annotations.ReturnType;
import org.ballerinalang.natives.connectors.AbstractNativeAction;
import org.ballerinalang.natives.connectors.BalConnectorCallback;
import org.ballerinalang.util.exceptions.BallerinaException;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code Execute} action can be used to invoke execute a http call with any httpVerb.
 */
@BallerinaAction(
        packageName = "ballerina.net.cb",
        actionName = "execute",
        connectorName = "CircuitBreaker",
        args = {
                @Argument(name = "c",
                        type = TypeEnum.CONNECTOR),
                @Argument(name = "m", type = TypeEnum.MESSAGE)
        },
        returnType = {@ReturnType(type = TypeEnum.MESSAGE)},
        connectorArgs = {
                @Argument(name = "openTime", type = TypeEnum.INT),
                @Argument(name = "retryTime", type = TypeEnum.INT),
                @Argument(name = "retryCount", type = TypeEnum.INT)
        })
@BallerinaAnnotation(annotationName = "Description", attributes = {@Attribute(name = "value",
        value = "Invokes an HTTP call with the specified HTTP verb.") })
@BallerinaAnnotation(annotationName = "Param", attributes = {@Attribute(name = "c",
        value = "A connector object") })
@BallerinaAnnotation(annotationName = "Param", attributes = {@Attribute(name = "m",
        value = "A message object") })
@BallerinaAnnotation(annotationName = "Return", attributes = {@Attribute(name = "message",
        value = "The response message object") })
@Component(
        name = "action.net.cb.execute",
        immediate = true,
        service = AbstractNativeAction.class)
public class Execute extends AbstractCBAction {

    private static final Logger logger = LoggerFactory.getLogger(Execute.class);

    @Override
    public BValue execute(Context context) {

        logger.debug("Executing Native Action : Execute");
        try {
            // Execute the operation
            BConnector bConnector = (BConnector) getRefArgument(context, 0);
            context.getControlStackNew().getCurrentFrame().getRefLocalVars()[0] = bConnector.getRefField(0);
            if (getFilteredAction() != null) {
                return ((AbstractNativeAction) getFilteredAction()).execute(context);
            }
        } catch (Throwable t) {
            throw new BallerinaException("Failed to invoke 'execute' action in " + "CircuitBreaker"
                    + ". " + t.getMessage(), context);
        }
        return null;
    }

    @Override
    public void execute(Context context, BalConnectorCallback connectorCallback) {
        if (logger.isDebugEnabled()) {
            logger.debug("Executing Native Action (non-blocking): {}", this.getName());
        }
        try {
            BConnector bConnector = (BConnector) getRefArgument(context, 0);
            context.getControlStackNew().getCurrentFrame().getRefLocalVars()[0] = bConnector.getRefField(0);
            // Execute the operation
            if (getFilteredAction() != null) {
                ((AbstractNativeAction) getFilteredAction()).execute(context, connectorCallback);
            }
            //executeNonBlockingAction(context, createCarbonMsg(context), connectorCallback);
            return;
        } catch (Throwable t) {
            // This is should be a JavaError. Need to handle this properly.
            throw new BallerinaException("Failed to invoke 'execute' action in " + "CircuitBreaker"
                    + ". " + t.getMessage(), context);
        }
    }

    @Override
    public boolean isNonBlockingAction() {
        return false;
    }
}
