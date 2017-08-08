package ballerina.net.cb;

import ballerina.doc;
import ballerina.net.core;

@doc:Description { value:"Implementation of the Circuit Breaker functionality in Ballerina"}
@doc:Param {value:"configuration: Struct represents the configuration values for Circuit Breaker"}
connector CircuitBreaker<core:ClientConnector c> (config configuration) {

    @doc:Description { value:"The execute action implementation of the CircuitBreaker."}
    @doc:Param { value:"c: A CircuitBreaker connector object" }
    @doc:Param { value:"m: A message object" }
    @doc:Return { value:"message: The response message object" }
    native action execute (CircuitBreaker c, message m) (message);

}

struct config {
    boolean enabled;
    int timeoutMillis;
    int sleepWindowMillis;
    int rollingWindowMillis;
    int requestVolumeThreshold;
    int errorPercentage;
    int coreSize;
    int maxSize;
    int queueLength;
}