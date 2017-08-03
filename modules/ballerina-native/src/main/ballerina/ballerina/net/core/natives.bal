package ballerina.net.core;

import ballerina.doc;

connector ClientConnector () {

    @doc:Description { value:"The execute action implementation of the ClientConnector."}
    @doc:Param { value:"c: A ClientConnector connector object" }
    @doc:Param { value:"m: A message object" }
    @doc:Return { value:"message: The response message object" }
    native action execute (ClientConnector c, message m) (message);

}