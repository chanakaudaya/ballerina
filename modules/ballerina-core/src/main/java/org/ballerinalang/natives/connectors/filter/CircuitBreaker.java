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

package org.ballerinalang.natives.connectors.filter;

import org.wso2.carbon.messaging.CarbonCallback;
import org.wso2.carbon.messaging.CarbonMessage;
import org.wso2.carbon.messaging.CarbonMessageProcessor;
import org.wso2.carbon.messaging.ClientConnector;
import org.wso2.carbon.messaging.exceptions.ClientConnectorException;

import java.io.PrintStream;
import java.util.Map;

/**
 * {@code CircuitBreaker} Implementation of Circuit Breaker pattern for ballerina client connectors
 * as a filter connector
 * @param <T> Base connector of the Circuit Breaker filter connector
 */
public class CircuitBreaker<T>  implements ClientConnector {

    private T baseConnector;

    public CircuitBreaker() {

    }

    public CircuitBreaker(T baseConnector) {
        this.baseConnector = baseConnector;
    }

    public T getBaseConnector() {
        return this.baseConnector;
    }

    public void setBaseConnector(T baseConnector) {
        this.baseConnector = baseConnector;
    }

    @Override
    public Object init(CarbonMessage carbonMessage, CarbonCallback carbonCallback, Map<String, Object> map)
            throws ClientConnectorException {
        PrintStream out = System.out;
        out.println("Within init method of Circuit Breaker");
        return ((ClientConnector) baseConnector).init(carbonMessage, carbonCallback, map);
    }

    @Override
    public boolean send(CarbonMessage carbonMessage, CarbonCallback carbonCallback) throws ClientConnectorException {
        PrintStream out = System.out;
        out.println("Within send method of Circuit Breaker");
        return ((ClientConnector) baseConnector).send(carbonMessage, carbonCallback);
    }

    @Override
    public boolean send(CarbonMessage carbonMessage, CarbonCallback carbonCallback, Map<String, String> map)
            throws ClientConnectorException {
        PrintStream out = System.out;
        out.println("Within send method 2 of Circuit Breaker");
        return ((ClientConnector) baseConnector).send(carbonMessage, carbonCallback, map);
    }

    @Override
    public String getProtocol() {
        PrintStream out = System.out;
        out.println("Within getProtocol method of Circuit Breaker");
        return ((ClientConnector) baseConnector).getProtocol();
    }

    @Override
    public void setMessageProcessor(CarbonMessageProcessor carbonMessageProcessor) {
        PrintStream out = System.out;
        out.println("Within setMessageProcessor method of Circuit Breaker");
        ((ClientConnector) baseConnector).setMessageProcessor(carbonMessageProcessor);
    }
}
