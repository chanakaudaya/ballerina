// Copyright (c) 2018 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
//
// WSO2 Inc. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.


import ballerina/log;
import ballerina/auth;

@Description {value:"Representation of Authentication handler chain"}
public type AuthnHandlerChain object {
    private {
        AuthHandlerRegistry authHandlerRegistry;
    }
    new (authHandlerRegistry) {
    }
    public function handle (Request req) returns (boolean);
    public function handleWithSpecificAuthnHandlers (string[] authProviderIds, Request req) returns (boolean);
};

@Description {value:"Tries to authenticate against any one of the available authentication handlers"}
@Param {value:"req: Request object"}
@Return {value:"boolean: true if authenticated successfully, else false"}
public function AuthnHandlerChain::handle (Request req) returns (boolean) {
    foreach currentAuthProviderType, currentAuthHandler in self.authHandlerRegistry.getAll() {
        var authnHandler = <HttpAuthnHandler> currentAuthHandler;
        if (authnHandler.canHandle(req)) {
            log:printDebug("Trying to authenticate with the auth provider: " + currentAuthProviderType);
            return authnHandler.handle(req);
        }
    }
    return false;
}

@Description {value:"Tries to authenticate against a specifc sub set of the authentication handlers, using the given 
array of auth provider ids"}
@Param {value:"authProviderIds: array of auth provider ids"}
@Param {value:"req: Request object"}
@Return {value:"boolean: true if authenticated successfully, else false"}
public function AuthnHandlerChain::handleWithSpecificAuthnHandlers (string[] authProviderIds, Request req)
                                                                                                    returns (boolean) {
    foreach authProviderId in authProviderIds {
        match self.authHandlerRegistry.get(authProviderId) {
            HttpAuthnHandler authnHandler => {
                if (authnHandler.canHandle(req)) {
                    log:printDebug("Trying to authenticate with the auth provider: " + authProviderId);
                    return authnHandler.handle(req);
                }
            }
            () => {
                // nothing to do
            }
        }
    }
    return false;
}
