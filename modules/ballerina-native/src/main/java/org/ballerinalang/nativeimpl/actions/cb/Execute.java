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

import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import org.ballerinalang.bre.Context;
import org.ballerinalang.model.types.TypeEnum;
import org.ballerinalang.model.values.BConnector;
import org.ballerinalang.model.values.BStruct;
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
    private String connectorName;
    private String actionName;
    private boolean enabled;
    private long timeoutMillis;
    private long sleepWindowMillis;
    private long rollingWindowMillis;
    private long requestVolumeThreshold;
    private long errorThresholdPercentage;
    private long coreSize;
    private long maxSize;
    private long maxQueueLength;

    @Override
    public BValue execute(Context context) {

        logger.debug("Executing Native Action : Execute");
        try {
            // Execute the operation
            BConnector bConnector = (BConnector) getRefArgument(context, 0);
            if (bConnector.getRefField(1) != null && bConnector.getRefField(1) instanceof BStruct) {
                BStruct configuration = (BStruct) bConnector.getRefField(1);
                enabled = configuration.getBooleanField(0) > 0 ? true : false;
                timeoutMillis = configuration.getIntField(0) > 0 ? configuration.getIntField(0) :
                        Constants.DEFAULT_TIMEOUT_MILLIS;
                sleepWindowMillis = configuration.getIntField(1) > 0 ? configuration.getIntField(1) :
                        Constants.DEFAULT_SLEEP_WINDOW_MILLIS;
                rollingWindowMillis = configuration.getIntField(2) > 0 ? configuration.getIntField(2) :
                        Constants.DEFAULT_ROLLING_WINDOW_MILLIS;
                requestVolumeThreshold = configuration.getIntField(3) > 0 ? configuration.getIntField(3) :
                        Constants.DEFAULT_REQUEST_VOLUME_THRESHOLD;
                errorThresholdPercentage = configuration.getIntField(4) > 0 ? configuration.getIntField(4)
                         : Constants.DEFAULT_ERROR_PERCENTAGE_THRESHOLD;
                coreSize = configuration.getIntField(5) > 0 ? configuration.getIntField(5)
                        : Constants.DEFAULT_CORE_SIZE;
                maxSize = configuration.getIntField(6) > 0 ? configuration.getIntField(6)
                        : Constants.DEFAULT_MAX_SIZE;
                maxQueueLength = configuration.getIntField(7) > 0 ? configuration.getIntField(7)
                        : Constants.DEFAULT_MAX_QUEUE_LENGTH;
            }
            BConnector filteredConnector = (BConnector) bConnector.getRefField(0);
            filteredConnector.setTimeout(timeoutMillis);
            connectorName = filteredConnector.getConnectorType().getPackagePath() + ":" +
                    filteredConnector.getConnectorType().getName();
            context.getControlStackNew().getCurrentFrame().getRefLocalVars()[0] = filteredConnector;
            AbstractNativeAction abstractNativeAction = (AbstractNativeAction) getFilteredAction();
            if (abstractNativeAction != null) {
                actionName = abstractNativeAction.getName();
                CircuitBreakerCommand circuitBreakerCommand = new CircuitBreakerCommand(abstractNativeAction,
                        context, enabled, timeoutMillis, sleepWindowMillis, rollingWindowMillis,
                        requestVolumeThreshold, errorThresholdPercentage, coreSize, maxSize, maxQueueLength);
                return circuitBreakerCommand.execute();
                //return ((AbstractNativeAction) getFilteredAction()).execute(context);
            }
        } catch (Throwable t) {
            if (t instanceof HystrixBadRequestException) {
                throw new BallerinaException("Error occurred during execution of circuit breaker for connector: " +
                        connectorName + " action: " +  actionName + ": failed with " +
                        t.getMessage(), context);
            } else if (t instanceof HystrixRuntimeException) {
                switch(((HystrixRuntimeException) t).getFailureType()) {
                    case TIMEOUT:
                        // Execute the timeout handling logic here
                        throw new BallerinaException("Error occurred during execution of circuit breaker for " +
                                "connector: " + connectorName + " action: " +  actionName + ": timed out." , context);
                    case SHORTCIRCUIT:
                        throw new BallerinaException("Error occurred during execution of circuit breaker for " +
                                "connector: " + connectorName + " action: " +  actionName + ": short circuited." ,
                                context);
                    case COMMAND_EXCEPTION:
                        throw new BallerinaException("Error occurred during execution of circuit breaker for " +
                                "connector: " + connectorName + " action: " +  actionName + ": failed with " +
                                t.getCause().getMessage(), context);
                    case REJECTED_THREAD_EXECUTION:
                        throw new BallerinaException("Error occurred during execution of circuit breaker for " +
                                "connector: " + connectorName + " action: " +  actionName + ": thread was rejected",
                                context);
                    case BAD_REQUEST_EXCEPTION:
                        throw new BallerinaException("Error occurred during execution of circuit breaker for " +
                                "connector: " + connectorName + " action: " +  actionName + ": failed with " +
                                t.getMessage(), context);
                }
            } else if (t instanceof BallerinaException) {
                throw new BallerinaException("Error occurred during execution of circuit breaker for connector: " +
                        connectorName + " action: " +  actionName + ". " + t.getMessage(), context);
            } else {
                throw new BallerinaException("Error occurred during execution of circuit breaker for connector: " +
                        connectorName + " action: " +  actionName + "." , context);
            }
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
            if (bConnector.getRefField(1) != null && bConnector.getRefField(1) instanceof BStruct) {
                BStruct configuration = (BStruct) bConnector.getRefField(1);
                enabled = configuration.getBooleanField(0) > 0 ? true : false;
                timeoutMillis = configuration.getIntField(0) > 0 ? configuration.getIntField(0) :
                        Constants.DEFAULT_TIMEOUT_MILLIS;
                sleepWindowMillis = configuration.getIntField(1) > 0 ? configuration.getIntField(1) :
                        Constants.DEFAULT_SLEEP_WINDOW_MILLIS;
                rollingWindowMillis = configuration.getIntField(2) > 0 ? configuration.getIntField(2) :
                        Constants.DEFAULT_ROLLING_WINDOW_MILLIS;
                requestVolumeThreshold = configuration.getIntField(3) > 0 ? configuration.getIntField(3) :
                        Constants.DEFAULT_REQUEST_VOLUME_THRESHOLD;
                errorThresholdPercentage = configuration.getIntField(4) > 0 ? configuration.getIntField(4)
                        : Constants.DEFAULT_ERROR_PERCENTAGE_THRESHOLD;
                coreSize = configuration.getIntField(5) > 0 ? configuration.getIntField(5)
                        : Constants.DEFAULT_CORE_SIZE;
                maxSize = configuration.getIntField(6) > 0 ? configuration.getIntField(6)
                        : Constants.DEFAULT_MAX_SIZE;
                maxQueueLength = configuration.getIntField(7) > 0 ? configuration.getIntField(7)
                        : Constants.DEFAULT_MAX_QUEUE_LENGTH;
            }
            BConnector filteredConnector = (BConnector) bConnector.getRefField(0);
            filteredConnector.setTimeout(timeoutMillis);
            connectorName = filteredConnector.getConnectorType().getPackagePath() + ":" +
                    filteredConnector.getConnectorType().getName();
            context.getControlStackNew().getCurrentFrame().getRefLocalVars()[0] = filteredConnector;
            AbstractNativeAction abstractNativeAction = (AbstractNativeAction) getFilteredAction();
            if (abstractNativeAction != null) {
                actionName = abstractNativeAction.getName();
                CircuitBreakerCommand circuitBreakerCommand = new CircuitBreakerCommand(abstractNativeAction,
                        context, connectorCallback, enabled, timeoutMillis, sleepWindowMillis, rollingWindowMillis,
                        requestVolumeThreshold, errorThresholdPercentage, coreSize, maxSize, maxQueueLength);
                circuitBreakerCommand.execute();
                return;
                //return ((AbstractNativeAction) getFilteredAction()).execute(context);
            }
            //executeNonBlockingAction(context, createCarbonMsg(context), connectorCallback);
            return;
        } catch (Throwable t) {
            if (t instanceof HystrixTimeoutException) {
                // Execute the timeout handling logic here
                throw new BallerinaException("Error occurred during execution of circuit breaker for connector: " +
                        connectorName + " action: " +  actionName + " has been timed out ", t.getCause(), context);
            } else if (t instanceof HystrixRuntimeException) {
                // Execute the timeout handling logic here
                throw new BallerinaException("Error occurred during execution of circuit breaker for connector: " +
                        connectorName + " action: " +  actionName + ".", t.getCause(), context);
            } else if (t instanceof BallerinaException) {
                // Execute the timeout handling logic here
                throw new BallerinaException("Error occurred during execution of circuit breaker for connector: " +
                        connectorName + " action: " +  actionName + ". " + t.getMessage(), context);
            } else {
                // Execute the timeout handling logic here
                throw new BallerinaException("Error occurred during execution of circuit breaker for connector: " +
                        connectorName + " action: " +  actionName + "." , context);
            }
        }
    }

    @Override
    public boolean isNonBlockingAction() {
        return false;
    }
}
