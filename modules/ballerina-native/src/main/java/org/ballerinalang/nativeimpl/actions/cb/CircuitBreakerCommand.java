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

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import org.ballerinalang.bre.Context;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.natives.connectors.AbstractNativeAction;
import org.ballerinalang.natives.connectors.BalConnectorCallback;

/**
 * {@code CircuitBreakerCommand} class which extends a HystrixCommand class to provide
 * Resiliency aspects to ballerina native actions
 */
public class CircuitBreakerCommand extends HystrixCommand<BValue> {
    private final AbstractNativeAction action;
    private final Context context;
    private final BalConnectorCallback connectorCallback;

    public CircuitBreakerCommand(AbstractNativeAction action, Context context, BalConnectorCallback connectorCallback) {
        super(Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("Ballerina-Resiliency"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("CircuitBreaker"))
                .andCommandPropertiesDefaults(
                        HystrixCommandProperties.Setter()
                                .withExecutionTimeoutInMilliseconds(5000)
                                .withMetricsHealthSnapshotIntervalInMilliseconds(1000)
                                .withMetricsRollingStatisticalWindowInMilliseconds(20000)
                                .withCircuitBreakerSleepWindowInMilliseconds(10000)
                )
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("CircuitBreaker-" + action.getName()))
                .andThreadPoolPropertiesDefaults(
                        HystrixThreadPoolProperties.Setter()
                                .withCoreSize(4)
                                .withMaxQueueSize(10)
                ));
        this.action = action;
        this.connectorCallback = connectorCallback;
        this.context = context;
    }

    public CircuitBreakerCommand(AbstractNativeAction action, Context context) {
        super(Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("Ballerina-Resiliency"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("CircuitBreaker"))
                .andCommandPropertiesDefaults(
                        HystrixCommandProperties.Setter()
                                .withExecutionTimeoutInMilliseconds(5000)
                                .withMetricsHealthSnapshotIntervalInMilliseconds(1000)
                                .withMetricsRollingStatisticalWindowInMilliseconds(20000)
                                .withCircuitBreakerSleepWindowInMilliseconds(10000)
                )
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("CircuitBreaker-" + action.getName()))
                .andThreadPoolPropertiesDefaults(
                        HystrixThreadPoolProperties.Setter()
                                .withCoreSize(4)
                                .withMaxQueueSize(10)
                ));
        this.action = action;
        this.connectorCallback = null;
        this.context = context;
    }

    @Override
    protected BValue run() {
        if (connectorCallback == null) {
            return action.execute(context);
        } else {
            action.execute(context, connectorCallback);
            return null;
        }
    }

//    @Override
//    protected BValue getFallback() {
//        return null;
//    }
}
