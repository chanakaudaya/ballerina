/*
*  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*/
package org.ballerinalang.util.codegen;

import org.ballerinalang.natives.connectors.AbstractNativeAction;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * {@code ActionInfo} contains metadata of a Ballerina action entry in the program file.
 *
 * @since 0.87
 */
public class ActionInfo extends CallableUnitInfo {

    private AbstractNativeAction nativeAction;
    private ConnectorInfo connectorInfo;
    private ActionInfo filteredActionInfo;

    // This variable holds the method table for this type.
    protected Map<Integer, Integer> methodTableIndex = new HashMap<>();
    protected Map<AbstractNativeAction, ActionInfo> methodTableType = new HashMap<>();

    public ActionInfo(int pkgCPIndex, String pkgPath, int actionNameCPIndex, String actionName,
                      ConnectorInfo connectorInfo) {
        this.pkgPath = pkgPath;
        this.pkgCPIndex = pkgCPIndex;
        this.name = actionName;
        this.nameCPIndex = actionNameCPIndex;
        this.connectorInfo = connectorInfo;
    }

    public Map<Integer, Integer> getMethodTableIndex() {
        return methodTableIndex;
    }

    public void setMethodTableIndex(Map<Integer, Integer> methodTable) {
        this.methodTableIndex = methodTable;
    }

    public void addMethodIndex(int methodNameCPIndex, int ip) {
        methodTableIndex.put(methodNameCPIndex, new Integer(ip));
    }

    public void addMethodType(AbstractNativeAction nativeAction, ActionInfo actionInfo) {
        methodTableType.put(nativeAction, actionInfo);
    }

    public void setMethodTableType(Map<AbstractNativeAction, ActionInfo> methodTable) {
        this.methodTableType = methodTable;
    }

    public ActionInfo getMethodTypeAction(AbstractNativeAction nativeAction) {
        if (methodTableType.containsKey(nativeAction)) {
            return methodTableType.get(nativeAction);
        } else {
            return null;
        }
    }

    public AbstractNativeAction getNativeAction() {
        return nativeAction;
    }

    public void setNativeAction(AbstractNativeAction nativeAction) {
        this.nativeAction = nativeAction;
    }

    public ConnectorInfo getConnectorInfo() {
        return connectorInfo;
    }

    public ActionInfo getFilteredActionInfo() {
        return filteredActionInfo;
    }

    public void setFilteredActionInfo(ActionInfo filteredActionInfo) {
        this.filteredActionInfo = filteredActionInfo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pkgCPIndex, nameCPIndex);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ActionInfo && pkgCPIndex == (((ActionInfo) obj).pkgCPIndex)
                && nameCPIndex == (((ActionInfo) obj).nameCPIndex);
    }
}
