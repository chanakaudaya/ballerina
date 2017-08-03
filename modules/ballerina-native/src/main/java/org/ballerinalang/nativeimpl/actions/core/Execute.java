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
import org.ballerinalang.natives.annotations.ReturnType;
import org.ballerinalang.natives.connectors.AbstractNativeAction;
import org.ballerinalang.natives.connectors.BalConnectorCallback;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code Execute} default action of core connector
 */
@BallerinaAction(
        packageName = "ballerina.net.core",
        actionName = "execute",
        connectorName = "ClientConnector",
        args = {
                @Argument(name = "c",
                        type = TypeEnum.CONNECTOR),
                @Argument(name = "m", type = TypeEnum.MESSAGE)
        },
        returnType = {@ReturnType(type = TypeEnum.MESSAGE)})
@BallerinaAnnotation(annotationName = "Description", attributes = {@Attribute(name = "value",
        value = "Invokes an HTTP call with the specified HTTP verb.") })
@BallerinaAnnotation(annotationName = "Param", attributes = {@Attribute(name = "c",
        value = "A connector object") })
@BallerinaAnnotation(annotationName = "Param", attributes = {@Attribute(name = "m",
        value = "A message object") })
@BallerinaAnnotation(annotationName = "Return", attributes = {@Attribute(name = "message",
        value = "The response message object") })
@Component(
        name = "action.net.core.execute",
        immediate = true,
        service = AbstractNativeAction.class)
public class Execute extends AbstractCoreAction {

    private static final Logger logger = LoggerFactory.getLogger(Execute.class);

    @Override
    public BValue execute(Context context) {
        logger.debug("Executing Native Action : Execute");
        return null;
    }

    @Override
    public void execute(Context context, BalConnectorCallback connectorCallback) {
        if (logger.isDebugEnabled()) {
            logger.debug("Executing Native Action (non-blocking): {}", this.getName());
        }
        return;
    }

}
