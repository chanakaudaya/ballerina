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

/**
 * Constants required for CircuitBreaker implementation
 */
public class Constants {
    public static final boolean ENABLED = true;
    public static final long DEFAULT_TIMEOUT_MILLIS = 30000;
    public static final long DEFAULT_SLEEP_WINDOW_MILLIS = 60000;
    public static final long DEFAULT_ROLLING_WINDOW_MILLIS = 10000;
    public static final long DEFAULT_REQUEST_VOLUME_THRESHOLD = 20;
    public static final long DEFAULT_ERROR_PERCENTAGE_THRESHOLD = 50;
    public static final long DEFAULT_CORE_SIZE = 4;
    public static final long DEFAULT_MAX_SIZE = 10;
    public static final long DEFAULT_MAX_QUEUE_LENGTH = -1;


}
