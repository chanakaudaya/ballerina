package ballerina.net.cb;

import ballerina.doc;
import ballerina.net.core;

connector CircuitBreaker<core:ClientConnector c> (int openTime, int retryTime, int retryCount) {

    @doc:Description { value:"The execute action implementation of the CircuitBreaker."}
    @doc:Param { value:"c: A CircuitBreaker connector object" }
    @doc:Param { value:"m: A message object" }
    @doc:Return { value:"message: The response message object" }
    native action execute (CircuitBreaker c, message m) (message);

}