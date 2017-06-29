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

package org.ballerinalang.composer.service.workspace.langserver;

import org.ballerinalang.model.symbols.BLangSymbol;

/**
 * Represents the symbol information
 */
public class SymbolInfo {
    private String symbolName;
    private BLangSymbol symbol;

    public SymbolInfo(String symbolName, BLangSymbol symbol) {
        this.symbolName = symbolName;
        this.symbol = symbol;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }

    public BLangSymbol getSymbol() {
        return symbol;
    }

    public void setSymbol(BLangSymbol symbol) {
        this.symbol = symbol;
    }
}
